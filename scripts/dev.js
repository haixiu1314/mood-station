/**
 * 情绪小站一键启动脚本
 * 启动：小程序后端(8080)、管理端后端(8081)、管理端前端(3000)
 * 小程序端请在微信开发者工具中手动打开 mood-front 运行
 */
const { spawn, spawnSync } = require('child_process');
const fs = require('fs');
const os = require('os');
const path = require('path');
const net = require('net');

const ROOT = path.resolve(__dirname, '..');
const isWin = process.platform === 'win32';

const SERVICES = [
  {
    name: '小程序后端',
    prefix: '[小程序后端]',
    dir: 'mood-later',
    port: 8080,
    url: 'http://localhost:8080',
    type: 'maven'
  },
  {
    name: '管理端后端',
    prefix: '[管理端后端]',
    dir: 'mood-admin_later',
    port: 8081,
    url: 'http://localhost:8081',
    type: 'maven'
  },
  {
    name: '管理端前端',
    prefix: '[管理端前端]',
    dir: 'mood-admin_front',
    port: 3000,
    url: 'http://localhost:3000',
    type: 'npm'
  }
];

const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

function log(text) {
  process.stdout.write(text + '\n');
}

function logService(service, text) {
  process.stdout.write(`${service.prefix} ${text}\n`);
}

function findMaven() {
  const mvnName = isWin ? 'mvn.cmd' : 'mvn';

  if (process.env.MAVEN_HOME) {
    const fromEnv = path.join(process.env.MAVEN_HOME, 'bin', mvnName);
    if (fs.existsSync(fromEnv)) return fromEnv;
  }

  const probe = spawnSync(isWin ? 'where' : 'which', ['mvn'], { encoding: 'utf8' });
  if (probe.status === 0 && probe.stdout) {
    const first = probe.stdout.split(/\r?\n/).map((s) => s.trim()).filter(Boolean)[0];
    if (first && fs.existsSync(first)) return first;
  }

  // 回退：使用 Maven Wrapper 缓存的发行版
  const distsDir = path.join(os.homedir(), '.m2', 'wrapper', 'dists');
  if (fs.existsSync(distsDir)) {
    const versions = fs
      .readdirSync(distsDir)
      .filter((name) => name.startsWith('apache-maven-') && fs.statSync(path.join(distsDir, name)).isDirectory())
      .sort()
      .reverse();

    for (const version of versions) {
      const versionDir = path.join(distsDir, version);
      for (const sub of fs.readdirSync(versionDir)) {
        const candidates = [
          path.join(versionDir, sub, version.replace(/-bin$/, ''), 'bin', mvnName),
          path.join(versionDir, sub, 'bin', mvnName)
        ];
        for (const candidate of candidates) {
          if (fs.existsSync(candidate)) return candidate;
        }
      }
    }
  }

  return null;
}

function probePort(port) {
  return new Promise((resolve) => {
    const socket = net.connect({ port, host: '127.0.0.1' });
    const done = (result) => {
      socket.destroy();
      resolve(result);
    };
    socket.setTimeout(800);
    socket.on('connect', () => done(true));
    socket.on('error', () => done(false));
    socket.on('timeout', () => done(false));
  });
}

function attachOutput(child, service) {
  const handle = (buf) => {
    buf
      .toString('utf8')
      .split(/\r?\n/)
      .forEach((line) => {
        if (line.trim() !== '') logService(service, line);
      });
  };
  child.stdout.on('data', handle);
  child.stderr.on('data', handle);
}

function ensureFrontendDeps(service) {
  const dir = path.join(ROOT, service.dir);
  if (fs.existsSync(path.join(dir, 'node_modules'))) return true;

  logService(service, '未检测到 node_modules，正在安装依赖...');
  const install = spawnSync(isWin ? 'npm.cmd' : 'npm', ['install'], {
    cwd: dir,
    stdio: 'inherit',
    shell: isWin
  });
  if (install.status !== 0) {
    logService(service, '依赖安装失败，请进入该目录手动执行 npm install');
    return false;
  }
  return true;
}

function startService(service, mavenPath) {
  const cwd = path.join(ROOT, service.dir);

  if (!fs.existsSync(cwd)) {
    logService(service, `目录不存在：${cwd}`);
    return false;
  }

  let commandLine;

  if (service.type === 'maven') {
    if (!mavenPath) {
      logService(service, '未找到 Maven，请安装 Maven 或配置 MAVEN_HOME 后重试');
      return false;
    }
    const mvn = mavenPath.includes(' ') ? `"${mavenPath}"` : mavenPath;
    commandLine = `${mvn} -q spring-boot:run`;
  } else {
    if (!ensureFrontendDeps(service)) return false;
    commandLine = 'npm run dev';
  }

  logService(service, `启动中（${service.url}）...`);

  const child = spawn(commandLine, {
    cwd,
    shell: true,
    env: { ...process.env, FORCE_COLOR: '1' }
  });

  service.proc = child;
  attachOutput(child, service);

  child.on('exit', (code) => {
    if (code !== 0 && !shuttingDown) {
      logService(service, `进程已退出，退出码 ${code}，请查看上方日志`);
    }
  });

  return true;
}

async function waitUntilReady(service, timeoutMs) {
  const deadline = Date.now() + timeoutMs;

  while (Date.now() < deadline) {
    if (service.proc && service.proc.exitCode !== null) return false;
    if (await probePort(service.port)) {
      logService(service, `已就绪 -> ${service.url}`);
      return true;
    }
    await delay(1000);
  }

  logService(service, `等待就绪超时，请查看上方日志`);
  return false;
}

let shuttingDown = false;

function killAll() {
  if (shuttingDown) return;
  shuttingDown = true;
  log('\n正在关闭所有服务...');
  for (const service of SERVICES) {
    if (service.proc && service.proc.exitCode === null) {
      spawnSync('taskkill', ['/pid', String(service.proc.pid), '/T', '/F'], { stdio: 'ignore' });
    }
  }
  process.exit(0);
}

async function main() {
  process.on('SIGINT', killAll);
  process.on('SIGTERM', killAll);

  log('');
  log('========== 情绪小站 · 开发环境一键启动 ==========');
  log('  管理端前端   http://localhost:3000');
  log('  管理端后端   http://localhost:8081');
  log('  小程序后端   http://localhost:8080');
  log('  小程序端     请在微信开发者工具中打开 mood-front（本项目不启动）');
  log('================================================');
  log('');

  const mavenPath = findMaven();
  if (!mavenPath) {
    log('[提示] 未在 PATH / MAVEN_HOME / Maven Wrapper 缓存中找到 Maven');
  }

  const started = [];

  for (const service of SERVICES) {
    if (await probePort(service.port)) {
      logService(service, `端口 ${service.port} 已被占用，跳过启动（可能已在运行）-> ${service.url}`);
      continue;
    }
    if (startService(service, mavenPath)) started.push(service);
  }

  if (started.length === 0) {
    log('');
    log('没有需要启动的服务。');
    return;
  }

  log('');
  log('服务启动中，等待端口就绪（首次启动编译可能较慢）...');

  const results = await Promise.all(started.map((service) => waitUntilReady(service, 180000)));
  const ready = started.filter((_, i) => results[i]);

  log('');
  log('================ 启动结果 ================');
  for (const service of started) {
    const ok = ready.includes(service);
    log(`  ${ok ? '[OK]  ' : '[FAIL]'} ${service.name.padEnd(8, ' ')} ${service.url}`);
  }
  log('==========================================');
  log('按 Ctrl+C 可停止所有服务');
  log('');

  process.on('SIGINT', killAll);
  process.on('SIGTERM', killAll);
}

main().catch((err) => {
  console.error(err);
  killAll();
});

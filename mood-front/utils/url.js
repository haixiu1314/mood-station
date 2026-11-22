const config = require('../config/api')

// 把后端返回的 localhost / 相对 /uploads/ 地址，统一解析为当前环境可访问的完整 URL。
// 用于 wx.previewImage、InnerAudioContext.src 等需要真实网络地址的场景（WXS 无法在这些地方使用）。
function resolveMediaUrl(value) {
  if (!value || typeof value !== 'string') return value

  // 1. 将 localhost / 127.0.0.1 替换为当前环境的 baseUrl host
  const localhostPrefixes = [
    'http://localhost:',
    'https://localhost:',
    'http://127.0.0.1:',
    'https://127.0.0.1:'
  ]
  for (let i = 0; i < localhostPrefixes.length; i++) {
    const p = localhostPrefixes[i]
    if (value.indexOf(p) === 0) {
      const pathStart = value.indexOf('/', p.length)
      if (pathStart < 0) return value
      return config.baseUrl + value.slice(pathStart)
    }
  }

  // 2. 已是完整 http(s) 地址，原样返回
  if (value.indexOf('http://') === 0 || value.indexOf('https://') === 0) {
    return value
  }

  // 3. 以 / 开头的相对路径，拼接 baseUrl
  if (value.indexOf('/') === 0) {
    return config.baseUrl + value
  }

  // 4. 其他相对路径，拼接 baseUrl + /
  return config.baseUrl + '/' + value
}

module.exports = { resolveMediaUrl }

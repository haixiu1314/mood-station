module.exports = {
  baseUrl: 'http://localhost:8080',
  api: {
    login: '/api/user/login',
    loginAccount: '/api/user/login/account',
    register: '/api/user/register',
    userInfo: '/api/user/info',
    userPublicInfo: '/api/user/',
    updateUserInfo: '/api/user/info',
    userBalance: '/api/user/balance',
    updateNickname: '/api/user/nickname',
    updateAvatar: '/api/user/avatar',
    changePassword: '/api/user/password',
    
    diaryCreate: '/api/diary',
    diaryList: '/api/diary/list',
    diaryRecent: '/api/diary/recent',
    diaryDetail: '/api/diary/',
    diaryUpdate: '/api/diary/',
    diaryDelete: '/api/diary/',
    diaryTrash: '/api/diary/trash',
    diaryRestore: '/api/diary/trash/',
    diaryFilter: '/api/diary/filter',
    diarySearch: '/api/diary/search',
    
    postCreate: '/api/post',
    postList: '/api/post/list',
    postMy: '/api/post/my',
    postDetail: '/api/post/',
    postLike: '/api/post/like/',
    postUnlike: '/api/post/like/',
    postComment: '/api/post/comment',
    postCommentList: '/api/post/comment/',
    postDelete: '/api/post/',
    postTrash: '/api/post/trash',
    postRestore: '/api/post/trash/',
    postFilter: '/api/post/filter',
    postSearch: '/api/post/search',
    postAppeal: '/api/post/appeal',
    
    productList: '/api/product/list',
    productDetail: '/api/product/',
    productStock: '/api/product/',
    
    cartAdd: '/api/cart/add',
    cartList: '/api/cart/list',
    cartUpdate: '/api/cart/update',
    cartRemove: '/api/cart/remove/',
    cartClear: '/api/cart/clear',
    
    orderCreate: '/api/order/create',
    orderList: '/api/order/list',
    orderByStatus: '/api/order/list/',
    orderDetail: '/api/order/',
    orderCancel: '/api/order/cancel/',
    orderRefund: '/api/order/refund/',
    orderRefundApply: '/api/order/refund/',
    orderConfirm: '/api/order/confirm/',
    
    homeBanners: '/api/home/banners',
    homeQuote: '/api/home/quote',
    
    uploadImage: '/api/upload/image',
    
    addressList: '/api/address/list',
    addressDefault: '/api/address/default',
    addressAdd: '/api/address/add',
    addressUpdate: '/api/address/update',
    addressDelete: '/api/address/delete/',
    addressSetDefault: '/api/address/setDefault/',
    
    notificationList: '/api/notification/list',
    notificationUnreadCount: '/api/notification/unread-count',
    notificationUnreadCountByType: '/api/notification/unread-count-by-type',
    notificationRead: '/api/notification/read',
    notificationReadAll: '/api/notification/read-all',
    
    diaryPublic: '/api/diary/public/',
    
    messageSend: '/api/message/send',
    messageHug: '/api/message/hug',
    messageReceived: '/api/message/received',
    messageSent: '/api/message/sent',
    messageUnreadCount: '/api/message/unread-count',
    messageRead: '/api/message/read/',
    messageReadAll: '/api/message/read-all',

    // AI 情绪小喵
    aiChatSend: '/api/ai/chat',
    aiChatHistory: '/api/ai/history',
    aiChatClear: '/api/ai/history',
    aiChatTodayCount: '/api/ai/today-count'
  }
}

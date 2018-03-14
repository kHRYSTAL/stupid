import wepy from 'wepy'
import interfaces from '../interfaces'

export default async function request(options) {
  if (options.header) {
    options.header['x-wechat-session'] = '2d4ee676e40f6b35caed9dfa61fad807'
  } else {
    options.header = {
      'x-wechat-session': '2d4ee676e40f6b35caed9dfa61fad807'
    }
  }
  let response = await wepy.request(options)
  // 如果返回401错误 说明未登录 需要执行登录
  if (response.statusCode === 401) {
    // 执行登录
    await interfaces.login()
    // 重新请求
    return await request(options)
  } else if (response.statusCode === 500) {
    wepy.showModal({
      title: '提示',
      content: `服务器错误，请截图本提示，并联系深大汪峰。${response.data.errmsg}`
    })
  } else {
    return response
  }
}

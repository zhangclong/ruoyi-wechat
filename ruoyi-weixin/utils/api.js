// 后端API基础地址，需要根据实际部署情况修改
const BASE_URL = 'http://localhost:8080'

// 封装请求方法
function request(url, method, data) {
  return new Promise((resolve, reject) => {
    wx.request({
      url: BASE_URL + url,
      method: method,
      data: data,
      header: {
        'content-type': 'application/json'
      },
      success(res) {
        if (res.data.code === 200) {
          resolve(res.data)
        } else {
          wx.showToast({
            title: res.data.msg || '请求失败',
            icon: 'none'
          })
          reject(res.data)
        }
      },
      fail(err) {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

// 会员注册
function register(data) {
  return request('/wechat/member/register', 'POST', data)
}

// 会员登录
function login(phonenumber) {
  return request('/wechat/member/login', 'POST', { phonenumber })
}

// 获取会员信息
function getMemberInfo(phonenumber) {
  return request('/wechat/member/info?phonenumber=' + phonenumber, 'GET')
}

// 更新会员信息
function updateMember(data) {
  return request('/wechat/member/update', 'PUT', data)
}

module.exports = {
  register,
  login,
  getMemberInfo,
  updateMember
}

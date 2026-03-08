const api = require('../../utils/api.js')

Page({
  data: {
    phonenumber: ''
  },

  onPhoneInput(e) {
    this.setData({
      phonenumber: e.detail.value
    })
  },

  // 处理登录
  handleLogin() {
    const { phonenumber } = this.data

    // 表单验证
    if (!phonenumber) {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      })
      return
    }

    if (!/^1[3-9]\d{9}$/.test(phonenumber)) {
      wx.showToast({
        title: '请输入正确的手机号',
        icon: 'none'
      })
      return
    }

    // 调用登录接口
    wx.showLoading({ title: '登录中...' })

    api.login(phonenumber).then(res => {
      wx.hideLoading()
      wx.showToast({
        title: '登录成功',
        icon: 'success'
      })

      // 保存会员信息到本地
      wx.setStorageSync('memberInfo', res.data)

      // 跳转到个人信息页
      setTimeout(() => {
        wx.switchTab({
          url: '/pages/profile/profile'
        })
      }, 1500)
    }).catch(err => {
      wx.hideLoading()
    })
  },

  // 跳转到注册页
  goToRegister() {
    wx.navigateTo({
      url: '/pages/register/register'
    })
  }
})

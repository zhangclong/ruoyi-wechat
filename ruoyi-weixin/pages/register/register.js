const api = require('../../utils/api.js')

Page({
  data: {
    nickName: '',
    phonenumber: '',
    avatar: ''
  },

  onNickNameInput(e) {
    this.setData({
      nickName: e.detail.value
    })
  },

  onPhoneInput(e) {
    this.setData({
      phonenumber: e.detail.value
    })
  },

  // 选择头像
  chooseAvatar() {
    const that = this
    wx.chooseMedia({
      count: 1,
      mediaType: ['image'],
      sourceType: ['album', 'camera'],
      success(res) {
        that.setData({
          avatar: res.tempFiles[0].tempFilePath
        })
        // 实际项目中这里应该上传图片到服务器
        // 这里简化处理，直接使用本地路径
      }
    })
  },

  // 处理注册
  handleRegister() {
    const { nickName, phonenumber, avatar } = this.data

    // 表单验证
    if (!nickName) {
      wx.showToast({
        title: '请输入昵称',
        icon: 'none'
      })
      return
    }

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

    // 调用注册接口
    wx.showLoading({ title: '注册中...' })

    api.register({
      nickName,
      phonenumber,
      avatar: avatar || ''
    }).then(res => {
      wx.hideLoading()
      wx.showToast({
        title: '注册成功',
        icon: 'success'
      })

      // 保存会员信息到本地
      wx.setStorageSync('memberInfo', res.data)

      // 跳转到个人信息页
      setTimeout(() => {
        wx.redirectTo({
          url: '/pages/profile/profile'
        })
      }, 1500)
    }).catch(err => {
      wx.hideLoading()
    })
  },

  // 跳转到登录页
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    })
  }
})

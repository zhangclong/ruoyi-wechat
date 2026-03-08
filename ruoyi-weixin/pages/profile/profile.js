const api = require('../../utils/api.js')

Page({
  data: {
    memberInfo: {}
  },

  onShow() {
    // 每次显示页面时重新加载会员信息
    this.loadMemberInfo()
  },

  // 加载会员信息
  loadMemberInfo() {
    const memberInfo = wx.getStorageSync('memberInfo')
    if (memberInfo) {
      this.setData({
        memberInfo: memberInfo
      })
      // 从服务器获取最新信息
      this.refreshMemberInfo(memberInfo.phonenumber)
    }
  },

  // 刷新会员信息
  refreshMemberInfo(phonenumber) {
    api.getMemberInfo(phonenumber).then(res => {
      this.setData({
        memberInfo: res.data
      })
      wx.setStorageSync('memberInfo', res.data)
    }).catch(err => {
      console.error('获取会员信息失败', err)
    })
  },

  // 跳转到编辑页面
  goToEdit() {
    wx.navigateTo({
      url: '/pages/edit/edit'
    })
  },

  // 退出登录
  handleLogout() {
    wx.showModal({
      title: '提示',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('memberInfo')
          this.setData({
            memberInfo: {}
          })
          wx.showToast({
            title: '已退出登录',
            icon: 'success'
          })
        }
      }
    })
  },

  // 跳转到登录页
  goToLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    })
  }
})

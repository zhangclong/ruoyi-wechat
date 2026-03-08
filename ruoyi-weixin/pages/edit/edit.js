const api = require('../../utils/api.js')

Page({
  data: {
    memberId: null,
    nickName: '',
    phonenumber: '',
    avatar: ''
  },

  onLoad() {
    // 加载会员信息
    const memberInfo = wx.getStorageSync('memberInfo')
    if (memberInfo) {
      this.setData({
        memberId: memberInfo.memberId,
        nickName: memberInfo.nickName,
        phonenumber: memberInfo.phonenumber,
        avatar: memberInfo.avatar
      })
    } else {
      wx.showToast({
        title: '请先登录',
        icon: 'none'
      })
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    }
  },

  onNickNameInput(e) {
    this.setData({
      nickName: e.detail.value
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

  // 保存修改
  handleSave() {
    const { memberId, nickName, avatar } = this.data

    // 表单验证
    if (!nickName) {
      wx.showToast({
        title: '请输入昵称',
        icon: 'none'
      })
      return
    }

    // 调用更新接口
    wx.showLoading({ title: '保存中...' })

    api.updateMember({
      memberId,
      nickName,
      avatar
    }).then(res => {
      wx.hideLoading()
      wx.showToast({
        title: '保存成功',
        icon: 'success'
      })

      // 更新本地存储
      wx.setStorageSync('memberInfo', res.data)

      // 返回上一页
      setTimeout(() => {
        wx.navigateBack()
      }, 1500)
    }).catch(err => {
      wx.hideLoading()
    })
  }
})

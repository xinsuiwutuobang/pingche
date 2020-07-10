var app = getApp();
var util = require('../../utils/util.js');
Page({
  data: {
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    appInfo: {},
    login: false
  },
  onLoad: function (options) {
    var that = this;
    var vdata = {};
    wx.getSetting({
      success: function (res) {
        if (res.authSetting['scope.userInfo']) {
          wx.redirectTo({
            url: '/pages/index/index',
          })
        }
        return false;
      }
    })

    that.setData({
      appInfo: util.wxAppinfo
    });

  },
  bindMessage: function(e){
    wx.requestSubscribeMessage({
      tmplIds: ['XX_jgKWA13DomV7ZZ_oI-UGRABqJZtmRrU7Uogtxwhc'],
      success(res){
        console.log('授权评论回复消息成功',res)
      },
      fail(res){
        console.log('授权评论回复消息失败',res)
      }
    })
  },
  bindGetUserInfo: function (e) {
   
    var that = this;
    var userinfo = e.detail;
    wx.login({
      success: function (res) {
       
        
        console.log(res);
        wx.getUserInfo({
          success: function (userinfo) {
            util.req('user/login', {
              "code": res.code,
              "encryptedData": userinfo.encryptedData,
              "iv": userinfo.iv,
              "nickName": userinfo.userInfo.nickName,
              "avatarUrl": userinfo.userInfo.avatarUrl,
              "gender": userinfo.userInfo.gender,
              "country": userinfo.userInfo.country,
              "province": userinfo.userInfo.province,
              "city": userinfo.userInfo.city,
              "language": userinfo.userInfo.language
            }, function (data) {
              console.log(data);
              app.setUserInfo(data.data);
              wx.reLaunch({
                url: '/pages/index/index',
              })
            })
          },
          fail: function (res) {
            // that.loginFail();
          }
        })
        
      }
    })
  }
})
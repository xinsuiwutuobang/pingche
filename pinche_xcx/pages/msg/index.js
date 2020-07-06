// pages/msg/index.js
var util = require('../../utils/util.js');
var app = getApp();
Page({
  data:{},
  msg:function(){
    var that = this;
    util.req('msg/getall', { sk: app.globalData.sk,uid: app.globalData.userInfo.id}, function (data) {

      var zan = 0;
      var comment = 0;
      var notice = 0;
      if (data.data == null) {
        var data = { zan: zan, comment: comment, notice: notice };
        that.setData({ data: data });
        return false;      
      }


      for(let key in data.data){
        if(key == 'zan'){
          zan = data.data[key].length;
        }else if(key == 'comment'){
          comment = data.data[key].length;
        }else if(key == 'notice'){
          notice = data.data[key].length;
        }
      }
  
      var data = { zan: zan, comment: comment, notice: notice };
      that.setData({ data: data });
  })
  },
  onShow: function () {
    this.msg();
  },
  onPullDownRefresh: function () {
    this.msg();
    wx.stopPullDownRefresh();
  },

})
var util = require('../../utils/util.js');
Page({
  data:{},
  onLoad:function(options){
    var that = this;
    util.req('notice/list',{},function(data){
      if(data.status == 1){
        //消息通知
        that.setData({data:data.data});
      }
    })
  }
})
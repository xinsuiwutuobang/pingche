var util = require('../../utils/util.js');
Page({
  data:{},
  onLoad:function(options){
    var that = this;
    util.req('notice/detail',{id:options.id},function(data){
      if(data.code == 200){
        that.setData({data:data.data});
      }
    })
  }
})
var app = getApp();
var util = require('../../utils/util.js');
var page = 1;
var list = new Array();
Page({
  data:{},
  onLoad:function(){
    console.log("pub list onload")
    var that = this;
    this.getList();
  },
  getList(){
    var that = this;
    util.req('info/mylist',{page:page},function(data){
      if(data.data.length == 0){
          if(page == 1){  
            console.log(page)        ;
            that.setData({'isnull':true});
          }
          that.setData({nomore:true});
          return false;
        } 

      that.setData({list:data.data});
    })
  },
  onReachBottom:function(){
    if(!this.data.nomore){
      page++;
      this.getList();
    }
  },
  onPullDownRefresh: function(){
    page = 1;
    this.getList();
    wx.stopPullDownRefresh();
  },
  // onShow:function(){
  //   page = 1;
  //   this.getList();
  // }
})
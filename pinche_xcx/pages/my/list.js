// pages/my/list.js
var app = getApp();
var util = require('../../utils/util.js');
var page = 1;
var list = new Array();
Page({
  data:{
  tabs: ["全部", "车找人", "人找车"]
  },
  del:function(e){
    var that = this;
    var currentTarget = e.currentTarget.id;
    wx.showModal({
      title: '提示',
      content: '确定删除?',
      success: function(res) {
        if (res.confirm) {
          util.req('info/del',{sk:app.globalData.sk,uid:app.globalData.userInfo.id,id:list[currentTarget].id},function(data){
            if(data.code == 200){
              list.splice(currentTarget,1);
              that.setData({list:list});
              wx.showToast({
                title: '删除成功',
                icon: 'success',
                duration: 2000
              })
            }else{
                util.isError('删除失败,请重试', that);
                return false;
            }
          })
        }
      }
    })
    return false;
  },
  edit:function(e){
    var currentTarget = e.currentTarget.id;
    console.log('/pages/info/add?id='+list[currentTarget].id);
    wx.navigateTo({
      url: '/pages/info/edit?id='+list[currentTarget].id,
      complete:function(res){
          console.log(res)
      }
    })
  },
  onReachBottom:function(){
    if(!this.data.nomore){
      page++;
      this.getList();
    }
  },
  getList(){
    var that = this;
    util.req('info/mylist',{uid:app.globalData.userInfo.id,sk:app.globalData.sk,page:page},function(data){
      if(data.data.length == 0){
          if(page == 1){  
            console.log(page)        ;
            that.setData({'isnull':true});
          }
          that.setData({nomore:true});
          return false;
        } 

        if(page == 1){          
          list = new Array();
        }

        var surp = new Array('','空位','人');
        data.data.forEach(function(item){
          var obj = {
            //start:((item.departure).split('市')[1]).replace(/([\u4e00-\u9fa5]+[县区]).+/,'$1'),
            //over:((item.destination).split('市')[1]).replace(/([\u4e00-\u9fa5]+[县区]).+/,'$1'),
            start:item.departure,
            over:item.destination,
            type:that.data.tabs[item.type],
            tp:item.type,
            time:item.time.substring(0,16),
            surplus:item.surplus+surp[item.type],
            see:item.see,
            gender:item.gender,
            url:'/pages/info/index?id='+item.id,
            tm:util.getDateDiff(Date.parse(item.time)),
            id:item.id
          };
          list.push(obj);
        })
      that.setData({list:list});
    })
  },  
  onPullDownRefresh: function(){
    page = 1;
    this.getList();
    wx.stopPullDownRefresh();
  },
  onShow:function(){
    page = 1;
    this.getList();
  }
})
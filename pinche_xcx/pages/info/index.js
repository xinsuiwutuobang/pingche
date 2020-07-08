// pages/info/index.js
var util = require('../../utils/util.js');
var app = getApp();
var page = 1;
var comment = new Array();
Page({
  data:{
    'data.id':0,
    'back':false,
    'nomore':false,
    'shoucang':false,
    //是否显示预约按钮
    'notme':false,
    //预约弹出层
    'modalFlag':false
  },
  //点击电话图标事件，调用小程序拨打电话
  tel:function(){
    var that = this;
    wx.makePhoneCall({
      phoneNumber: that.data.data.phone
    })
  },
  //点击评论按钮，滑动到评论view
  //<scroll-view scroll-y="true" style="height: {{height}}px" scroll-into-view="{{toview}}" bindscrolltolower="tobottom" scroll-top="0">
  tocomment:function(){
    this.setData({toview:'comment_list'});
  },
  //赞
  zan:function(event){
    var that = this;
    var Commentdata = this.data.comment;
    util.req('comment/zan',{
      //wiew id属性
      'cid':Commentdata[event.currentTarget.id].id,
      'uid':app.globalData.userInfo.id
      },function(data){
      if(data.code == 200){
        //this.data.comment[event.currentTarget.id].zan = this.data.comment[event.currentTarget.id].zan  + 1;
        Commentdata[event.currentTarget.id].zan = Commentdata[event.currentTarget.id].zan + 1;
        Commentdata[event.currentTarget.id].iszan = true;
        that.setData({comment:Commentdata});
      }else{
        console.log(data.msg);
        wx.showModal({
            title: '提示',
            content: data.msg,
            showCancel: false,
            success: function (res) {
            }
        });
      }
    })
  },
  //收藏
  shoucang:function(){
    var that = this;
    util.req('fav/addfav',{iid:that.data.data.id,uid:app.globalData.userInfo.id,sk:app.globalData.sk},function(data){
      if(data.code == 200){
        that.setData({'shoucang':true});
        wx.showToast({
          title: '收藏成功',
          icon: 'success',
          duration: 2000
        })
      }
    })
  },
  //取消收藏
  qxshoucang:function(){
    var that = this;
    util.req('fav/delFav',{iid:that.data.data.id,uid:app.globalData.userInfo.id,sk:app.globalData.sk},function(data){
      if(data.code == 200){
        that.setData({'shoucang':false});
        wx.showToast({
          title: '取消收藏成功',
          icon: 'success',
          duration: 2000
        })
      }
    })
  },

  //预约弹出层开关
  madal:function(){
    this.setData({modalFlag:true});
  },
  //预约弹出层关闭
  modalOk:function(){
    this.setData({modalFlag:false});
  },
  //预约
  appointment:function(e){
    var fId = e.detail.formId;
    var that = this;
    console.log(e.detail.value.surplus);
    if(e.detail.value.name == ''){
        util.isError('请输入姓名',that);
        return false;
    }
    if(e.detail.value.phone == ''){
        util.isError('请输入手机号',that);
        return false;
    }
    if(!(/^1[34578]\d{9}$/.test(e.detail.value.phone))){
      util.isError('手机号码错误', that);
      return false;
    }
    if(e.detail.value.surplus == 0){
        util.isError('请选择人数',that);
        return false;
    }
    util.clearError(that);
    util.req('appointment/add',{form_id:fId,uid:app.globalData.userInfo.id,iid:this.data.data.id,name:e.detail.value.name,phone:e.detail.value.phone,surplus:e.detail.value.surplus,sk:app.globalData.sk},function(data){
      if(data.code == 200){
        that.setData({modalFlag:false});
        wx.showToast({
          title: '预约成功',
          icon: 'success',
          duration: 2000
        })
      }else{
        util.isError(data.msg,that);
        return false;
      }
    })
  },
  //选择剩余座位
  setsurplus:function(e){
    this.setData({surplus:e.detail.value})
  },

  //初始化执行
  onLoad:function(options){
    var that = this;
    wx.getSystemInfo({
      success: function(res) {
        that.setData({height:res.windowHeight});
      }
    })

    that.setData({
      'userInfo.gender':app.globalData.userInfo.gender,
      'userInfo.name':(app.globalData.userInfo.name == '')?app.globalData. userInfo.nickName:app.globalData.userInfo.name,
      'userInfo.phone':app.globalData.userInfo.phone
    })


    //是否收藏
    util.req('fav/isfav',{iid:options.id,uid:app.globalData.userInfo.id,sk:app.globalData.sk},function(data){
      if(data.code == 200){
        that.setData({'shoucang':data.data});
      }
    }) 

    //详情
    util.req('info/index',{id:options.id},function(data){
      that.setData({data:data.data});
      if(data.data.uid == app.globalData.userInfo.id){
        var notme = false;
        //已过期状态等待确认
      }else if(1 == data.data.type && data.data.surplus > 0){
        var notme = true;
      }
      var Surpluss = new Array('请选择人数');
      for(var i = 1; i <= data.data.surplus; i++){
        Surpluss.push(i);
      }

      console.log("data.data.time:" + data.data.time)
      that.setData({
        //'data.tm':util.formatTime(Date.parse(data.data.time)),
        'data.tm':data.data.time.substring(0,16),
        'data.price':(data.data.price == null)?'面议':data.data.price,
        'notme':notme,
        'Surpluss':Surpluss,
        'surplus':0,
        });
    })   
    page = 1; 
    this.getCount(options.id);
    this.getComment(options.id);
    //获取当前页面栈。数组中第一个元素为首页，最后一个元素为当前页面。
    if(getCurrentPages().length == 1){
      that.setData({'back':true});
    }
  },
  //图片预览
  previmg:function(e){
    var that = this;    
    wx.previewImage({
      current: that.data.comment[e.target.dataset.iid].img[e.target.dataset.id],
      urls: that.data.comment[e.target.dataset.iid].img,
    })
  },
  //获取评论
  getComment:function(id){
    var that = this;
    util.req('comment/get',{id:id,type:'info',current:page},function(data){
      if(data.code == 200){
        if(page == 1){          
          comment = new Array();
        }
        if(data.data == null){
          that.setData({'nomore':true});
        }else{         
          data.data.forEach(function(item){
            comment.push({
              id:item.id,
              avatarUrl:item.avatarUrl,
              content:item.content,
              fid:item.fid,
              nickName:item.nickName,
              img:JSON.parse(item.img),
              zan:item.zan,
              reply:item.reply,
              time:util.getDateBiff(Date.parse(item.time))
            })
          })
        }
        console.log(page+':'+comment);
        that.setData({comment:comment});
      }
    })
  },
  //返回首页
  toIndex:function(){
    wx.reLaunch({
      url: '/pages/index/index'
    })
  },
 
  //评论数
  getCount:function(id){  
    var that = this;  
    util.req('comment/get_count',{iid:id,type:'info'},function(data){  //获取评论总数
      if(data.code == 200){
        that.setData({comnum:data.data});
      }
    })
  },

  onShow:function(){      
    page = 1; 
    if(this.data.data) {
      this.getCount(this.data.data.id);
      this.getComment(this.data.data.id);
    }
  },
   /**
   * 用户点击右上角转发
    监听用户点击页面内转发按钮（<button> 组件 open-type="share"）或右上角菜单“转发”按钮的行为，并自定义转发内容。
    注意：只有定义了此事件处理函数，右上角菜单才会显示“转发”按钮
    此事件需要 return 一个 Object，用于自定义转发内容
   */
  onShareAppMessage: function () { 
    return {
      title: '拼车详情',
      path: 'pages/info/index?id='+this.data.data.id
    }
  },
  //滚动到底部触发 分页
  tobottom:function(){
    if(!this.data.nomore){
      page++;
      this.getComment(this.data.data.id);
    }
  }
})
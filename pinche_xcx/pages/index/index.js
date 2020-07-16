//index.js
//获取应用实例

var util = require('../../utils/util.js');
var app = getApp();
//七天
var today = util.formatTime(new Date((new Date()).getTime()+(1000*60*60*24*7))).split(' ')[0];
var minday = util.formatTime(new Date()).split(' ')[0];
var maxday =  util.formatTime(new Date((new Date()).getTime()+(1000*60*60*24*62))).split(' ')[0];
var sliderWidth = 96; // 需要设置slider的宽度，用于计算中间位置
var page = 1;
var list = new Array();
var list1 = new Array();
var list2 = new Array();

Page({
  data: {
    all:'act',
    //date:,
    minday:"",
    maxday:maxday,
    tabs: ["全部", "车找人", "人找车"],
    activeIndex: 0,
    sliderOffset: 0,
    sliderLeft: 0,
    start:'',
    over:''
  },

  /**
   * 切换tab
   * @param {} e 
   */
  tabClick: function (e) {
        this.setData({
            //currentTarget	Object	当前组件的一些属性值集合
            sliderOffset: e.currentTarget.offsetLeft,
            activeIndex: e.currentTarget.id
        });
    },

  bindDateChange:function(e){
    this.setData({
      date:e.detail.value
    })
    this.getList(e.detail.value,this.data.start,this.data.over);
  },
  onShareAppMessage: function () {
    return {
      title: '同城拼车',
      path: 'pages/index/index'
    }

  },
  //获取列表
  getList:function(date='',start='',over=''){
    var that = this;
    util.req('info/lists',
    
      {start:start,over:over,date:date,current:page},
      function(data){
        data = data.data;
        if(!data.records){
          that.setData({nomore:true});
          return false;
        } 
        if(page == 1){          
          list = new Array();
          list1 = new Array();
          list2 = new Array();
        }
        var surp = new Array('','空位','人');
        data.records.forEach(function(item){
          try{
            var start = ((item.departure).split('市')[1]).replace(/([\u4e00-\u9fa5]+[县区]).+/, '$1');
          }catch(e){
            //var start = (item.departure).split(/[县区]/)[0];
            var start = (item.departure).replace(/([\u4e00-\u9fa5]+[县区]).+/, '$1');
          }

          try {
            var over = ((item.destination).split('市')[1]).replace(/([\u4e00-\u9fa5]+[县区]).+/, '$1');
          } catch (e) {
            //var over = (item.destination).split(/[县区]/)[0];
            var over = (item.destination).replace(/([\u4e00-\u9fa5]+[县区]).+/, '$1');
          }

          var obj = {
            start:start,
            over:over,
            //枚举值作为下标，与数组下表有对应关系。0不会被用到
            type:that.data.tabs[item.type],
            tp:item.type,
            //time:util.formatTime(item.time),
            time:item.time.substring(11,16),
            date:item.date.substring(0,11),
            //0不会被用到
            surplus:item.surplus+surp[item.type],
            see:item.see,
            gender:item.gender,
            avatarUrl:item.avatarUrl,
            //点击跳转详情
            url:'/pages/info/index?id='+item.id,
            tm:util.getDateDiff(Date.parse(item.time))
            };
            list.push(obj);
            if(item.type == 1){
              list1.push(obj);
            }else{
              list2.push(obj);
            }
        })

        that.setData({list:list,list1:list1,list2:list2});
        that.setData({nomore:true});
    })

  },
  //始发地失去焦点
  getstart:function(e){
    this.setData({
      start:e.detail.value
    })
    page = 1;
    this.getList(this.data.date,e.detail.value,this.data.over);
  },
  //目的地失去焦点
  getover:function(e){
    this.setData({
      over:e.detail.value
    })
    page = 1;
    this.getList(this.data.date,this.data.start,e.detail.value);
  },
  onLoad: function () {
    var that = this;
    //在用户未授权过的情况下调用此接口，将不再出现授权弹窗，会直接进入 fail 回调（详见[《公告》](https://developers.weixin.qq.com/community/develop/doc/0000a26e1aca6012e896a517556c01))。在用户已授权的情况下调用此接口，可成功获取用户信息。
    wx.getSystemInfo({
        success: function(res) {
            that.setData({
                sliderLeft: (res.windowWidth / that.data.tabs.length - sliderWidth) / 2,
                sliderOffset: res.windowWidth / that.data.tabs.length * that.data.activeIndex,
                windowHeight: res.windowHeight,
                windowWidth: res.windowWidth
            });
        }
    });

    //获取信息
    that.getList();

    /**
    wx.getLocation({

      success: function (res) { 
        var latitude = res.latitude
        var longitude = res.longitude       
        wx.request({
          url: 'https://api.map.baidu.com/geocoder/v2/?ak=zIOkoO8wWrWA22ObIHPNkCgtLZpkP5lE&location=' + latitude + ',' + longitude + '&output=json&pois=0',
          data: {},
          method: 'GET', 
          header: { 'Content-Type': 'application/json' },
          success: function(res){
            that.setData({
              start:res.data.result.addressComponent.city
            })
            that.getList(that.data.date,res.data.result.addressComponent.city);
          }
        })
      }
    }) */
  },
  /**
   *  onPullDownRefresh 监听用户下拉动作
      监听用户下拉刷新事件。
      需要在app.json的window选项中或页面配置中开启enablePullDownRefresh。
      可以通过wx.startPullDownRefresh触发下拉刷新，调用后触发下拉刷新动画，效果与用户手动下拉刷新一致。
      当处理完数据刷新后，wx.stopPullDownRefresh可以停止当前页面的下拉刷新
   */
  onPullDownRefresh: function(){
    page = 1;
    this.getList(this.data.date,this.data.start,this.data.over);
    wx.stopPullDownRefresh();
  },
  /**
   *  页面上拉触底事件的处理函数
      监听用户上拉触底事件。
      可以在app.json的window选项中或页面配置中设置触发距离onReachBottomDistance。
      在触发距离内滑动期间，本事件只会被触发一次。
   */
  onReachBottom:function(){
    if(!this.data.nomore){
      page++;
      this.getList(this.data.date,this.data.start,this.data.over);
    }
  }
})

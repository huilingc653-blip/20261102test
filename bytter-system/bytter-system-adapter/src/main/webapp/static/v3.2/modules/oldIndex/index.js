
// $(function () {
var loadPendingInformationTimeOut = null;

/**
 * 消息通知功能
 */

var Notice =function(){
    this.config={
        show:false,
        loading:false,
        emptyContent:false,
        reload:false,
        notificationType:1,//1代表通知 2代表待处理 3代表待审批
        element:"message-btn",
        shakeDot:{},
        notificationData:[],
        offsetX:10,
        page:0,
        totalPage:0,
        notificationNumber:0,
        notificationNumberRead:false,
        container:$('#notification'),
    };
}

Notice.fn = Notice.prototype;

Notice.fn.init = function () {
    var that = this;
    if(that.config.element){
        var elem = $("."+that.config.element);
        var elemWidth = elem.outerWidth() / 2;
        var offsetRight = $(document).width() - elem.offset().left - elem.outerWidth();
        // var right = elemWidth + offsetRight - 175;
        var right = elemWidth + offsetRight - 230;
        that.config.offsetX = right>0?right:0;
        that.config.container.css('right',that.config.offsetX);

        elem.off().click(function(){
            that.config.show = !that.config.show;
            // that.config.notificationType = 1;
            // $("#notice-type").find("button").removeClass('active');
            // $("#notice-type").find("button").eq(0).addClass('active');
            that.showFooterMessage();
            $("#notification-content").html("");
            that.config.notificationData = [];
            that.config.container.show();
            that.config.notificationNumberRead = true;
            if(that.config.notificationData.length===0){
                that.getData(true);
            }
        });
    }
    //注册加载更多方法
    $('#notification-content').on('click','#loading-more a',function () {
        that.getData();
    })
    //注册关闭窗口方法
    that.closeNotification();
    //注册信息类别切换
    that.toggleNotificationType();

    //注册标记已读的方法
    that.deleteNotification();

    //注册加载更多的方法
    that.getMoreData();

    //注册前往处理的方法
    that.jumpListPage();

    try{$(".notification .notification-body").mCustomScrollbar({theme:"minimal-dark"});}catch(e){};

    that.config.shakeDot = $(".shake-dot");

    that.checkNotificationNumber();

    $('#notification').bind("mousedown", function( event ) {
        event.preventDefault();
    }).hover(function(){
    },function(){
        $("#notification").hide();
    });

    var intervalTime = 1000 * 60 * 5;
    setInterval(function(){
       // that.checkNotificationNumber();
    },intervalTime);
}

Notice.fn.checkNotificationNumber = function () {
    var that = this;
    $.get(basePath+'/sys/notification/count',function(result){
        if(result){
            if(that.config.notificationNumber !== result){
                that.config.notificationNumberRead = false;
            }
            that.config.notificationNumber = result;
        }else{
            that.config.shakeDot.animate({ opacity: "0"});
        }
        if(!that.config.notificationNumberRead && that.config.notificationNumber > 0) {
            that.shake(that.config.shakeDot);
        }
    },'JSON');
}


Notice.fn.shake = function (jqNode) {
    var that = this;
    if(that.config.notificationNumber > 0){
        if(that.config.notificationNumberRead){
            that.config.shakeDot.animate({ opacity: "1"});
        }else{
            jqNode.animate({ opacity: "1"},500).animate({ opacity: "0"},200,function(){
                that.shake(jqNode);
            });
        }
    }else{
        that.config.shakeDot.animate({ opacity: "0"});
    }
}

Notice.fn.closeNotification = function () {
    var that = this;
    $("#notification").on('click','#close-notice',function () {
        that.config.show = false;
        $("#notification").hide();
    })

}
Notice.fn.toggleNotificationType = function (flag) {
    var that = this;
    $("#notice-type").on('click','button',function () {
        $("#notification-content").html("");
        $(this).siblings().removeClass('active');
        $(this).addClass('active');
        that.config.notificationType = $(this).data("type");
        that.config.reload = true;
        that.config.page = 0;
        that.getData();
    })
}

Notice.fn.deleteNotification = function () {
    var that = this;
    $('#notification').on('click','.read-item',function () {
        var currentElem = $(this);
        var id = $(this).data('key');
        if (id == undefined){
            return
        }
        var ids = [];
        if (id =="all"){
            var data = that.config.notificationData;
            for (var i = 0,length = data.length; i< length; i++){
                ids.push(data[i].id);
            }
        } else {
            ids.push(id);
        }

        if(ids.length === 0){
            return;
        }

        $.post(basePath + "/sys/notification/delete", {ids: ids}, function (result) {
            if(result.success){
                if(id != "all"){
                    var data = that.config.notificationData;
                    for (var i = 0,length = data.length; i< length; i++){
                        if (data[i].id == id){
                            that.config.notificationData.splice(i,1);
                            break
                        }
                    }
                    currentElem.parent().remove();
                }else{
                    that.config.notificationData = [];
                    $("#notification-content").html("");
                    that.config.page = 0;
                }
            }
            layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    })

}

Notice.fn.getMoreData = function () {
    var that = this;
    $("#loading-more").on("click","a",function () {
        that.getData()
    })
}

Notice.fn.jumpListPage = function () {
    var that = this;

    $('#notification-content').on('click','.solve-item',function () {
        var id = $(this).data('key');
        if (id == undefined){
            return
        }
        var data = that.config.notificationData;
        for (var i = 0,length = data.length; i< length; i++){
            if (data[i].id == id){
                if(bt && bt.navtab && data[i].listUrl){
                    var menuIdCondition = '';
                    var contactChar = '?';
                    if(data[i].menuId && data[i].menuId.length>0){
                        if(data[i].listUrl.indexOf('?')>0){
                            contactChar = '&';
                        }
                        menuIdCondition = contactChar + 'menuId=' + data[i].menuId;
                    }
                    bt.navtab.addNavtab(bt.lang('sys.notification.todo')+"- " + data[i].categoryName, basePath + data[i].listUrl + menuIdCondition);
                    that.config.show = false;
                    $("#notification").hide();
                }
                break
            }
        }
    })
}

Notice.fn.toggleContent = function () {
    var temps = this.config.notificationData.$model;
    temps[index].expand = true;
    this.config.notificationData.removeAll();
    this.config.notificationData = temps;
}
// flag 为true 分页不++
Notice.fn.getData = function (flag) {
    var that = this;
    if(that.config.loading){
        return;
    }
    if(that.config.reload){
        that.config.notificationData =[];
    }
    that.config.loading = true;
    that.config.reload = false;

    that.showFooterMessage();

    var tempData = {
        pageNumber:flag?that.config.page:++that.config.page,
        type:that.config.notificationType
    };
    $.ajax({
        type: "POST",
        url: basePath + "/sys/notification/list",
        data: JSON.stringify(tempData),//将对象序列化成JSON字符串
        dataType:"json",
        contentType : 'application/json;charset=utf-8', //设置请求头信息
        success: function(result){
            setTimeout(function(){
                that.config.page = result.pageNumber;
                that.config.totalPage = result.totalPage;
                that.config.emptyContent = !result.data.length>0;

                if(result.data.length>0){
                    for(var i=0;i<result.data.length;i++){
                        result.data[i].expand = false;
                        if(!result.data[i].name){
                            result.data[i].name = " ";
                        }
                        if(!result.data[i].createDate){
                            result.data[i].createDate = " ";
                        }else{
                            //liuyj 将时间戳转为时间
                            var time = result.data[i].createDate;
                            var date = new Date(time);
                            var Y = date.getFullYear() + '-';
                            var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
                            var D = ( date.getDate() < 10 ? ( '0'+ date.getDate() ):date.getDate() ) + ' ';
                            var h = ( date.getHours() < 10 ? ( '0'+ date.getHours() ):date.getHours() ) + ':';
                            var m = ( date.getMinutes() < 10 ? ( '0'+ date.getMinutes() ):date.getMinutes() ) + ':';
                            var s = date.getSeconds() < 10 ? ( '0'+ date.getSeconds() ):date.getSeconds() ;
                            result.data[i].createDate = Y+M+D+h+m+s;
                        }
                    }
                    that.config.notificationData = that.config.notificationData.concat(result.data)
                }
                that.config.loading = false;
                that.doHTML()
            },500)
        }
    });
}
Notice.fn.doHTML = function () {

    var html ='',that = this,data = that.config.notificationData;

    bt.each(data, function (index,item) {
        html += '<li><div class="content-header layui-clear">'
        html += '<div class="category">'+item.categoryName+'</div>'
        html += '<div class="time">'+item.createDate+'</div>'
        html += '</div>'
        html += '<div class="content-detail">'+item.name+'</div>'
        if (that.config.notificationType == '1'){
            html += '<span class="delete-icon read-item" title="标记已读" data-key="'+item.id+'">' +
                '<span class="font_family icon-danchuang_lajitong1"></span></span>'
        } else if (that.config.notificationType == '2') {
            html += '<span class="delete-icon solve-item" title="前往处理" data-key="'+item.id+'">' +
                '<span class="font_family icon-danchuang_tiaozhuan"></span>'
        }
        html += '</li>'
    })
    that.showFooterMessage();
    $('#notification-content').html(html)
}

Notice.fn.showFooterMessage = function () {
    var config = this.config;
    if (!config.loading && config.totalPage>0 && config.page!=config.totalPage){
        $("#loading-more").show()
    }else {
        $("#loading-more").hide()
    }
    if (config.loading){
        $("#loading").show()
    }else {
        $("#loading").hide()
    }
    if (!config.loading && config.emptyContent){
        $("#text-empty").show()
    }else {
        $("#text-empty").hide()
    }
}

$(function () {
    new Notice().init();
    // 点击常用菜单删除按钮
    $("#commonMenus").on("click",".menu-delete",function () {
        layer.closeAll('tips');
        var menuId = $(this).siblings(".content-body-menu").data("id");
        var targetElem = ($("#sideMenuNav").find('[data-id="'+menuId+'"]'));
        var navMoreElem = targetElem.parent("div").siblings().find(".bt-menu-collect");
        if (!navMoreElem[0]){
            navMoreElem = targetElem.siblings().find(".bt-menu-collect")
        }
        navMoreElem.trigger("click",menuId)
    });

    // 点击常用菜单置顶操作
    $("#commonMenus").on("click",".menu-top",function () {
        layer.closeAll('tips');
        var mcId = $(this).siblings(".content-body-menu").attr("mc-id");
        $.ajax({
            type:"post",
            url:basePath + "/sys/menuCollect/menuTop?id="+mcId,
            dataType: "json",
            contentType: "application/json",
            success:function(result) {
                if(result.success){
                    loadCommonMenus();
                }else{
                    layer.alert(bt.lang(result.msg), {icon: 2});
                }
            }
        });
    });
    //常用菜单点击添加tab
    $("#commonMenus").on('click','.content-body-menu',function () {
        var that = $(this);
        var name = that.data('name');
        var url = that.data('href');
        var contact = '?';
        if(url.indexOf("?")>-1){
            contact = '&';
        }
        url = url + contact + "menuId=" + $(this).data("id");
        bt.navtab.addNavtab(name, basePath + url);
    });

    //常用菜单刷新
    $("#commonMenusRefresh").on("click",function () {
        loadCommonMenus();
    })
    //待办信息刷新
    $("#pendingInformationRefresh").on("click",function () {
        loadPendingInformation();
    })
    //指定30秒刷新一次
    //setInterval(loadPendingInformation,30000);

    //待办增加自动刷新，频率1分钟一次
    loadPendingInformationTimeOut = window.setInterval(function(){
        // 定时刷新，获取公告信息的数据
       // loadArticleCount();
    }, 60000);

    // 获取公告信息的数据
    loadArticleCount();

    loadCommonMenus();
    loadPendingInformation();

    // 消息弹框内容头部点击事件，点击时，可以展开或收起所有内容
    $("#notification").on("click",".content-header",function () {
        $(this).parent("li").toggleClass("content-show-all");
    })
    // 消息弹框内容头部点击事件，点击时，可以展开或收起所有内容
    $("#notification").on("click",".content-detail",function () {
        $(this).parent("li").toggleClass("content-show-all");
    })
    //加载常用菜单方法
    function loadCommonMenus() {
        $.ajax({
            type:"post",
            url:basePath + "/sys/menuCollect/widget",
            dataType: "html",
            contentType: "application/json",
            // data:JSON.stringify(data2),
            success:function(result) {
                $("#commonMenus").html(result);
                $("#commonMenus").dad({
                    draggable:'.icon-caidan',
                    callback:function (elem) {
                        var commonMenus = [];
                        bt.each($("#commonMenus").children("div"),function () {
                            var obj = {},$menuElem = $(this).find(".content-body-menu");
                            if (!$(this).hasClass('dads-children-clone')){
                                obj.id = $menuElem.attr('mc-id');
                                obj.menuId = $menuElem.data('id');
                                obj.sort = $(this).data('dad-position');
                                commonMenus.push(obj)
                            }
                        })
                        $.post(basePath + "/sys/menuCollect/dragSaveMenuWidget", {menuCollectList:commonMenus}, function (result) {
                        }, 'JSON');
                    }
                })
            }
        });
    }
    //加载待办信息
    var loadingHtml = '<div style="padding: 80px;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop" style="font-size: 26px;"></i></div>'

    // 加载公告信息条数
    function loadArticleCount() {
        $.ajax({
            type:"get",
            url:basePath + "/sys/article/getArticleCount",
            dataType: "json",
            contentType: "application/json",
            success:function(result) {
                if(result == 0){
                    $('#shakeAnnouncementCount').text(0).addClass('layui-hide')
                }else {
                    var count = result
                    if(result > 99){
                        count = '99+'
                    }
                    $('#shakeAnnouncementCount').text(count).removeClass('layui-hide')
                }
            },
            error:function (error) {
                clearInterval(loadPendingInformationTimeOut)
            }
        });
    }

    function loadPendingInformation() {
        // 加载动画启动
        $("#workFlow").html(loadingHtml)
        $.ajax({
            type:"get",
            url:basePath + "/bpm/my/todoInfo",
            dataType: "html",
            contentType: "application/json",
            // data:JSON.stringify(data1),
            success:function(result) {
                //  $('#pendingTab').addClass('layui-this');
                //  $('#finishedTab').removeClass('layui-this');
                $("#workFlow").html(result);
                bt.each($(".content-boby-name"),function () { //当屏面能够显示全时，去掉title属性
                    var $c = $(this)
                        .clone()
                        .css({display: 'inline', width: 'auto', visibility: 'hidden'})
                        .appendTo('body');

                    var c_width = $c.width();
                    $c.remove();
                    if ( !c_width > $(this).width() ){
                        $(this).removeAttr('title')
                    }
                })
                $("#approvalImg").attr('src',basePath +'/static/v3.2/images/index/approval.png')

                // 选择的已处理信息时
                if($('#finishedTab').hasClass('layui-this')){
                    $('#pendingInformation').removeClass('layui-show')
                    $('#finishedInformation').addClass('layui-show')
                }
            }
        });
    }
})

layui.use('element',function () {
    var  element = layui.element()
    element.on('tab(workflow)',function (data) {
        if(data.index == 0){
            $('#pendingInformation').addClass('layui-show')
            $('#finishedInformation').removeClass('layui-show')
        }else{
            $('#pendingInformation').removeClass('layui-show')
            $('#finishedInformation').addClass('layui-show')
        }
    })
})
// })

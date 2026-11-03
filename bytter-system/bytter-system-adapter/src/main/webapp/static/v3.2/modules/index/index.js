
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
        offsetX: '20px',
        page:1,
        totalPage:0,
        notificationNumber:0,
        notificationNumberRead:false,
        container:$('#notification'),
        messageTimer:null // 消息弹框定时器
    };

}

Notice.fn = Notice.prototype;

Notice.fn.init = function () {
    var that = this;
    if(that.config.element){
        var elem = $("."+that.config.element);
        // var elemWidth = elem.outerWidth() / 2;
        // var offsetRight = $(document).width() - elem.offset().left - elem.outerWidth();
        // // var right = elemWidth + offsetRight - 175;
        // var right = elemWidth + offsetRight - 230;
        // that.config.offsetX = right>0?right:0;
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
            that.config.page = 1;
            that.getData(true);
            // 开启定时器
            that.messageTimeOut()
        });
    }
    //注册加载更多方法
    $('#notification-content').on('click','#loading-more a',function () {
        that.getData();
    })
    //注册信息类别切换
    that.toggleNotificationType();

    // 点击每条信息
    that.clickMessageItem();
    // 清空所有消息
    that.clearAllNotification()

    //注册加载更多的方法
    that.getMoreData();


    try{$(".notification .notification-body").mCustomScrollbar({theme:"minimal-dark"});}catch(e){};

    that.config.shakeDot = $(".shake-dot");

    that.checkNotificationNumber();

    $('#notification').bind("mousedown", function( event ) {
        event.preventDefault();
    }).hover(function(){
        clearTimeout(that.config.messageTimer)
        that.config.messageTimer = null
    },function(){
        $("#notification").hide();
        clearTimeout(that.config.messageTimer)
        that.config.messageTimer = null
    });

    // 定时器关闭消息弹框


    var intervalTime = 1000 * 60 * 5;
    setInterval(function(){
        // that.checkNotificationNumber();
    },intervalTime);
}

Notice.fn.messageTimeOut = function(){
    var that = this
    /**
     * 1. 消息弹框展示时，开启定时器
     * 2. 鼠标经过消息弹框页面时，清除定时器
     * 3. 每次开启定时器时，清除旧定时器
     */
    if(this.config.messageTimer){
        clearTimeout(that.config.messageTimer)
        that.config.messageTimer = null
    }
    this.config.messageTimer = setTimeout(function () {
        $("#notification").hide();
        clearTimeout(that.config.messageTimer)
        that.config.messageTimer = null
    },3000)
}

Notice.fn.checkNotificationNumber = function () {
    var that = this;
    $.get(basePath+'/sys/notification/count',function(result){
        if(result){
            // 超过99显示99+
            $('#shakeCount').show().text(result > 99? '99+': result)
        }else{
            $('#shakeCount').hide()
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
// 切换消息类型
Notice.fn.toggleNotificationType = function (flag) {
    var that = this;
    $("#notice-type").on('click','div',function () {
        $("#notification-content").html("");
        $(this).siblings().removeClass('active');
        $(this).addClass('active');
        that.config.notificationType = $(this).data("type");
        that.config.reload = true;
        that.config.page = 0;
        that.getData();
    })
}

// 点击每一条消息
Notice.fn.clickMessageItem = function(){
    var that = this
    $('#notification-content').on('click','.message-item',function () {
        var elem = $(this)
        var id = elem.data('key');
        var url = elem.data('url');
        var businessId = elem.data('businessid');
        var tag = elem.data('tag');
        if (tag == "netbank:bdlink:bisexc:fail" && businessId) {
            url += "/" + businessId;
        }
        if(!id){
            return
        }

        if(url){
            var menuId = elem.data('menuid');
            url +='?menuId='+menuId;

            if(bt && bt.navtab){
                bt.navtab.addNavtab(bt.lang('sys.notification.todo')+"- " + elem.data('category'), basePath + url);
                that.config.show = false;
                $("#notification").hide();
            }
        }else { // 没有Url 代表是消息通知，不用操作页面
            //  that.clearNotification(id)
            elem.toggleClass('message-show-all')
        }
    })
    // 删除消息
    $('#notification-content').on('click','.item-delete',function () {
        var id = $(this).parents('li.message-item').data('key')
        if(id){
            that.clearNotification(id,$(this).parents('li.message-item'))
        }
        return false
    })
}
// 清空所有消息
Notice.fn.clearAllNotification = function(){
    var that = this
    $('#notification .notification-clear').click(function () {
        that.clearNotification('all')
    })
}
// 清空消息
Notice.fn.clearNotification = function(id , currentElem){
    var ids = [];
    var that = this;
    if(id == 'all'){
        var data = that.config.notificationData;
        for (var i = 0,length = data.length; i< length; i++){
            ids.push(data[i].id);
        }
    }else{
        ids.push(id)
    }
    if(ids.length == 0){
        layer.alert(bt.lang('sys.message.currently.no.messages.to.clear'));
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
                currentElem.remove();
            }else{
                that.config.notificationData = [];
                $("#notification-content").html("");
                that.config.page = 0;
            }
        }
        layer.alert(result.msg, {icon: result.success ? 1 : 2});
        //刷新消息数
        that.checkNotificationNumber();
    }, 'JSON');
}
// 加载更多
Notice.fn.getMoreData = function () {
    var that = this;
    $("#loading-more").on("click","a",function () {
        // that.config.reload = true
        that.getData()
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
                        else{
                            result.data[i].name = result.data[i].name.replace(/\\n/g, ' ');
                        }
                        if(!result.data[i].createDate){
                            result.data[i].createDate = " ";
                        }
                        // else{
                        //     //liuyj 将时间戳转为时间
                        //     var time = result.data[i].createDate;
                        //     var date = new Date(time);
                        //     var Y = date.getFullYear() + '-';
                        //     var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
                        //     var D = ( date.getDate() < 10 ? ( '0'+ date.getDate() ):date.getDate() ) + ' ';
                        //     var h = ( date.getHours() < 10 ? ( '0'+ date.getHours() ):date.getHours() ) + ':';
                        //     var m = ( date.getMinutes() < 10 ? ( '0'+ date.getMinutes() ):date.getMinutes() ) + ':';
                        //     var s = date.getSeconds() < 10 ? ( '0'+ date.getSeconds() ):date.getSeconds() ;
                        //     result.data[i].createDate = Y+M+D+h+m+s;
                        //     console.log(result.data[i].createDate);
                        // }
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
        var textArr = item.name.split(/\r\n|\n/);
        var addNames = "";
        for(var i = 0; i < textArr.length; i++){
            if(addNames == ""){
                addNames = textArr[i];
            }else {
                addNames = addNames + '<br>' + textArr[i];
            }
        }
        html += '<li class="message-item" data-key="'+item.id+'" data-menuid="'+item.menuId+'" data-url="'+ item.listUrl +'" data-category="'+item.categoryName+'" data-businessId="'+item.businessId+'" data-tag="'+item.tag+'"> <div class="item-header">'
        html += '<span class="iconfont bynotice_a"></span>'
        html += '<span class="item-text">'+item.categoryName+'</span>' +
            '<div class="item-delete"><span class="iconfont byicon_delete"></span></div></div>'
        html += '<div class="item-detail">'+addNames+'</div>'
        html += '<div class="item-detail date">'+item.createDate+'</div>'
        html += '</li>'
    })
    that.showFooterMessage();
    $('#notification-content').html(html)
}


Notice.fn.showFooterMessage = function () {
    var config = this.config;
    // 当totalPage 大于零，同时page 不等 totalPage时，显示更多按钮，否则隐藏更多按钮
    if (!config.loading && config.totalPage>0 && config.page !=config.totalPage ){
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

var allSearchMenusList = [];
var loadingHtml = '<div style="padding-top: 40px;padding-left: 50%;"><i class="layui-icon layui-anim layui-anim-rotate layui-anim-loop" style="font-size: 26px;"></i></div>'

bt.use(['jquery', 'navtab','element','laydate','date'], function () {
    var $ = bt.jquery;
    var element = layui.element();

    new Notice().init();

    // 首页页签更多时，点击切换页签，点击的页签要位于可显示
    // '<div class="more-nav-item '+liActive+'" data-id="'+layid+'" data-url="'+url+'" title="'+text+'">'+text+'<i class="layui-icon layui-unselect layui-tab-close">ဆ</i></div>
    $('#tabMoreContainer').on('click','.more-nav-item',function () {
        var layid = $(this).data('id')
        if(!layid){
            return
        }
        var targetLi = $('#navtabContainer').find('[lay-id="'+layid+'"]')
        // 1. 获取所有页签lis
        var lis = $('#navtabContainer').find('>li');
        // 2. 找到可显示的最后一个位置;
        var width = $('#navtabContainer').width()
        var targetLiWidth = targetLi.outerWidth()
        for (var i = 0; i < lis.length; i++) {
            var $li = $(lis[i])
            width = width - $li.outerWidth();
            // 判断条：余下的宽度刚好够选中页签位置
            if( width < targetLiWidth){
                $li.before(targetLi)
                break
            }
        }
        // 调用 tabChane方法，切换页签
        element.tabChange('card', layid);
        // 关闭并清空 #tabMoreContainer 容器
        $('#tabMoreContainer').html('').hide()
    })

    // 首页页签更多时，点击删除按钮，删除页签
    $('#tabMoreContainer').on('click','.layui-tab-close',function () {
        var layid = $(this).parent().data('id')
        // 调用 tabDelete，删除页签
        element.tabDelete('card', layid);
        // 删除当前项
        $(this).parent('div').remove()
        // 如果没有内容时，将期隐藏
        if(!$('#tabMoreContainer').find('.more-nav-item')[0]){
            $('#tabMoreContainer').hide()
        }
        return false
    })

    // 点击常用菜单置顶操作
    $("#commonMenus").on("click",".menu-top",function () {
        var mcId = $(this).parents('.common-menu-operate').siblings(".content-body-menu").attr("mc-id");
        layer.closeAll('tips')
        $.ajax({
            type:"post",
            url:basePath + "/sys/menuCollect/menuTop?id="+mcId,
            dataType: "json",
            //contentType: "application/json",
            success:function(result) {
                if(result.success){
                    loadCommonMenus();
                }else{
                    layer.alert(bt.lang(result.msg), {icon: 2});
                }
            }
        });
    });

    // 点击删除常用菜单按钮
    $('#commonMenus').on("click",'.menu-delete',function () {
        layer.closeAll('tips');
        var menuId = $(this).parents('.common-menu-operate').siblings(".content-body-menu").data("id");
        if (!menuId) {
            return
        }
        $.get(basePath + '/sys/menuCollect/collect', {menuId: menuId}, function (res) {
            try {
                var result = JSON.parse(res)
            }catch (e) {
                var result = res;
            }
            if (result.success) {
                var targetIconElems = $('#collectIcon'+menuId)
                if (targetIconElems.hasClass('byicon_collection_full')) {
                    targetIconElems
                        .removeClass('byicon_collection_full')
                        .addClass('byicon_collection')
                    // 取消收藏成功
                    // 刷新常用菜单收藏模块
                    // loadCommonMenus();
                    layer.msg(bt.lang('sys.index.deleteCollectMenu.failure'));

                }else {
                    targetIconElems
                        .addClass('byicon_collection_full')
                        .removeClass('byicon_collection')
                    // 添加收藏成功
                    // 刷新常用菜单收藏模块
                    //loadCommonMenus();
                    layer.msg(bt.lang('sys.index.addCollectMenu.success'));

                }
                // 刷新常用菜单收藏模块
                loadCommonMenus();
            }
        })

    })
    //常用菜单点击添加tab
    $("#commonMenus").on('click','.content-body-menu',function () {
        var $data = $(this).data()
        var contact = '?';
        if($data.href.indexOf("?")>-1){
            contact = '&';
        }
        $data.url = $data.href + contact + "menuId=" + $data.id;
        if($('body').data('key') === 'newUI'){
            // 如果是首次新增页签时，需要调整高度
            if(!$('#menusTabCard').hasClass('is-add-menu')){
                $('#menusTabCard').addClass('is-add-menu')
                var height = $(window).height() - $('.layout-header').height() - 32
                $('#tab-content').height(height);

                //设置首页工作台常用菜单父容器的高度
                //  var cardHeight = height - $('#tabsHeader').height() - 20

                // $('#tabsContent').height(cardHeight)
            }
        }

        bt.navtab.addNavtabBySearch($data.name, basePath + $data.url, $data.name,$data.topparentid)
        $('#navtabContainer').show()
        loadCommonMenus();
    });
    //常用菜单刷新
    $("#commonMenusRefresh").on("click",function () {
        $('#commonMenus').html(loadingHtml);
        loadCommonMenus();
    })
    //待办信息刷新
    $("#pendingInformationRefresh").on("click",function () {
        // 加载动画启动
        $("#workFlow").html(loadingHtml)
        loadPendingInformation();
    })

    //待办增加自动刷新，频率1分钟一次
    loadPendingInformationTimeOut = window.setInterval(function(){
        //loadPendingInformation();
        // 定时刷新，获取公告信息的数据
        //loadArticleCount()
        // 定时刷新，获取指标预警的总条数
        //loadWarnDataCount()

    }, 60000);

    // 待处理信息tab切换逻辑
    var activeClass ='active'
    var showHtmlClass = 'layui-show'
    $('#infoContent').on('click','.info-content-title', function () {
        $('#pendingTab').removeClass(activeClass)
        $('#finishedTab').removeClass(activeClass)
        $(this).addClass(activeClass)

        var index = $(this).data('name')
        if( index == 0 ){
            $('#pendingInformation').addClass(showHtmlClass)
            $('#finishedInformation').removeClass(showHtmlClass)
        }else{
            $('#pendingInformation').removeClass(showHtmlClass)
            $('#finishedInformation').addClass(showHtmlClass)
        }
    })

    // 生成日历
    initExpireCalendar()

    // 获取公告信息的数据
    loadArticleCount()

    // 加载指标预警条数
    loadWarnDataCount()
    // 加载重点关注内容
    loadListStarTarget();
    //指定30秒刷新一次
    //setInterval(loadPendingInformation,30000);

    loadCommonMenus();
    // 待处理数据
    loadPendingInformation();

    // 消息弹框内容头部点击事件，点击时，可以展开或收起所有内容
    $("#notification").on("click",".content-header",function () {
        $(this).parent("li").toggleClass("content-show-all");
    })
    // 消息弹框内容头部点击事件，点击时，可以展开或收起所有内容
    $("#notification").on("click",".content-detail",function () {
        $(this).parent("li").toggleClass("content-show-all");
    })

    //首页待处理信息栏点击事件
    $("#workFlow").on("click",".content-information-item",function () {
        var title   = $.trim($(this).find(".text").text());      // 导航栏text
        var src     = basePath + $(this).attr('href-url');   // 导航栏跳转URL
        bt.navtab.addNavtab(title,src)
    });
    //首页重点关注内容刷新事件
    $('#freshListStarTarget').click(function () {
        // 加载动画启动
        $('#listStarTarget').html(loadingHtml)
        loadListStarTarget()
    })

    //首页重点关注内容点击事件
    $("#reminderListBox").on("click",">div",function () {
        var title   = $.trim($(this).data('name'));      // 导航栏text
        if ($(this).data('url')) {
            var src = basePath + $(this).data('url');   // 导航栏跳转URL
            bt.navtab.addNavtab(title, src)
        }
    });

    //首页到期提醒内容点击事件
    $("#listStarTarget").on("click",">div",function () {
        var title   = bt.lang('sys.home.widget.starTarget')+ '-' +$(this).data('name').trim();      // 导航栏text
        if ($(this).data('url')) {
            var src = basePath + $(this).data('url');   // 导航栏跳转URL
            bt.navtab.addNavtab(title, src)
        }
    });
    $('#thisMonthIcon').on('click',function () {
        $('#homeCalendar').find('.laydate-btns-clear').trigger('click');
    })
    // 刷新到期提醒功能
    $('#refreshListExpire').on('click',function () {
        // 获取当前月时间范围
        var currentStartDate = $('#homeCalendar').find('.layui-laydate-content tbody').children(":first").children(":first").attr('lay-ymd');
        var currentEndDate = $('#homeCalendar').find('.layui-laydate-content tbody').children(":last-child").children(":last-child").attr('lay-ymd');
        // 按日期范围查询提醒标识
        loadListExpire(currentStartDate,currentEndDate);
        // 加载当前日期的提醒内容
        loadListExpireByCreateDate($('#selectDate').text())
    })



    /*------------微前端开始---------------*/
    var originalAddNavtab = bt.navtab.addNavtab;

    // 全局状态管理
    if (!window._allMicroAppContainerIds) {
        window._allMicroAppContainerIds = new Set();
    }
    if (!window.microAppPool) {
        window.microAppPool = {};
    }

    // 获取当前激活 Tab 的核心信息
    function getActiveTabInfo() {
        var $activeLi = $('#navtabContainer li.layui-this');
        if (!$activeLi.length) return null;

        var layId = $activeLi.attr('lay-id');
        var uniqueContainerId = $activeLi.data('micro-app-container-id');
        var microPath = $activeLi.data(window.BT_MICRO_APP_CONFIG.microAppPathDataKey);
        // 如果路由里包含 /comPage（你的公共空白壳组件路由）
        if(microPath && microPath.indexOf('/comPage') > -1 ){
            // 就用页签上的真实 url 作为 microPath
            microPath = $activeLi.attr('url')
        }
        var isMicroApp = $activeLi.hasClass('micro-app-tab');

        return {
            $li: $activeLi,
            layId: layId,
            containerId: uniqueContainerId,
            path: microPath,
            isMicroApp: isMicroApp,
            container: document.getElementById(uniqueContainerId)
        };
    }

    // 彻底卸载微应用
    function destroyMicroApp(uniqueContainerId) {
        if (!uniqueContainerId) return;

        // 1. 处理实例
        var instance = window.microAppPool[uniqueContainerId];
        if (instance) {
            try {
                if (typeof instance.unmount === 'function') {
                    instance.unmount();
                }
                delete window.microAppPool[uniqueContainerId];
            } catch (e) {
                console.error('[卸载] 实例卸载出错:', e);
            }
        }

        // 2. 清理 DOM
        var container = document.getElementById(uniqueContainerId);
        if (container) {
            container.innerHTML = '';
            container.style.display = 'none';
        }

        // 3. 从全局记录中移除
        if (window._allMicroAppContainerIds) {
            window._allMicroAppContainerIds.delete(uniqueContainerId);
        }
    }

    // 加载微应用
    function loadMicroAppLogic(microPath, uniqueContainerId) {
        if (!microPath || !uniqueContainerId) return;

        // 匹配配置
        var targetConfig = null;
        for (var prefix of window.BT_MICRO_APP_CONFIG.microAppPrefixes) {
            if (microPath.startsWith(prefix)) {
                targetConfig = window.microAppConfigs[prefix];
                break;
            }
        }

        if (!targetConfig) return;

        // 合并配置
        var microAppOptions = Object.assign({}, targetConfig);
        microAppOptions.container = '#' + uniqueContainerId;
        microAppOptions.props = $.extend({}, targetConfig.props || {}, {
            basePath: basePath,
            currentPath: microPath,
            menuList:window.microMenuList
        });

        // 激活状态
        document.body.classList.add('micro-app-active');
        window.__microAppFakePath__ = microPath;

        try {
            var instance = window.qiankun.loadMicroApp(microAppOptions, { autoPublicPath: true });

            // 存入池子
            window.microAppPool[uniqueContainerId] = instance;
            window.microAppInstance = instance;

            // 更新 URL
            // history.replaceState(null, '', basePath + microPath);

        } catch (e) {
            console.error('加载微应用失败:', e);
        }
    }

    // 重写addNavtab
    bt.navtab.addNavtab = function(title, src, name, top, topParentId) {
        var isMicroApp = false;
        var cleanPath = '';

        if (src) {
            cleanPath = src.split('?')[0].replace(basePath, '');
            isMicroApp = window.BT_MICRO_APP_CONFIG.microAppPrefixes.some(function(prefix) {
                return cleanPath.startsWith(prefix);
            });
        }

        if (isMicroApp) {
            // 微应用逻辑
            window.isMicroAppRoute = true;
            var id = 'card_' + new Date().getTime();
            var flag = getTitleId(src,title.length > 8 ? title.substring(0, 8) + '...' : title);
            if(flag === -1){
                var uniqueContainerId = window.BT_MICRO_APP_CONFIG.microAppContainerId + '_' + id;

                // 注册 ID
                window._allMicroAppContainerIds.add(uniqueContainerId);

                layui.element().tabAdd('card', {
                    title: '<span title="'+title+'">' +
                        (title.length > 8 ? title.substring(0, 8) + '...' : title) +
                        '<span class="nav-tab-close"><i class="layui-icon layui-unselect layui-tab-close">ဆ</i></span>' +
                        '</span>',
                    content: '<div id="' + uniqueContainerId + '" class="bt-subapp-container" style="height:100%;width:100%;"></div>',
                    id: id,
                    src: src
                });

                var $newLi = $('#navtabContainer').find('li[lay-id="' + id + '"]');
                $newLi.addClass('micro-app-tab')
                    .data(window.BT_MICRO_APP_CONFIG.microAppPathDataKey, cleanPath)
                    .data('micro-app-container-id', uniqueContainerId);

                // 关键：加载微应用到容器中（使用完整路径包含查询参数，让子应用能获取到 component、mode 等参数）
                var fullPath = src.replace(basePath, '');
                console.log('[addNavtab] 加载微应用, fullPath:', fullPath, ', containerId:', uniqueContainerId);
                loadMicroAppLogic(fullPath, uniqueContainerId);
            }else{
                id = flag
            }
            // 获取父id
            if(!top){ // 代表为父级菜单
                var parentId = getParentId();
            }

            layui.element().tabChange('card', id);
            $('#bodyContainer').addClass('navtab-active');
            $('#navtabContainer').show();

            if(parentId){
                setParentId(parentId)
            }

            return { done: function(){}, fail: function(){} };
        } else {
            // 非微应用，走原生逻辑
            window.isMicroAppRoute = false;
            // history.replaceState(null, '', basePath + window.BT_MICRO_APP_CONFIG.defaultRoutePath);
            return originalAddNavtab.call(this, title, src, name, top, topParentId);
        }
    };

    bt.navtab.addNavtabBySearch = function(title,src,name,topParentId) {
        bt.navtab.addNavtab(title,src,name,undefined,topParentId)
    };

    // Tab 切换
    layui.element().on('tab(card)', function(data) {
        var info = getActiveTabInfo();
        if (info && info.isMicroApp&& info.container) {
            // 隐藏所有微应用容器
            window._allMicroAppContainerIds.forEach(function(id) {
                var dom = document.getElementById(id);
                if (dom) dom.style.display = 'none';
            });

            // 显示当前容器
            if (info.container) {
                info.container.style.display = 'block';
            }
            window.isMicroAppRoute = true;

            // 判断是“复用”还是“加载”
            if (window.microAppPool[info.containerId]) {
                // 实例存在 -> 复用
                // history.replaceState(null, '', basePath + info.path);
            } else {
                // 实例不存在 -> 加载
                loadMicroAppLogic(info.path, info.containerId);
            }

        } else {
            // --- 非微应用逻辑 ---
            if (info) {
                window.isMicroAppRoute = false;
                // history.replaceState(null, '', basePath + window.BT_MICRO_APP_CONFIG.defaultRoutePath);
            }
        }
    });

    // 监听Tab 关闭
    bt.navtab.on('beforeClose', function(e, data){
        var $li = data.$element;
        var uniqueContainerId = $li.data('micro-app-container-id');

        if ($li.hasClass('micro-app-tab') && uniqueContainerId) {
            destroyMicroApp(uniqueContainerId);
        }
    });

    // 监听Tab 刷新
    bt.navtab.on('beforeRefresh', function(e, data){
        var $li = data.$element;
        var uniqueContainerId = $li.data('micro-app-container-id');

        if ($li.hasClass('micro-app-tab') && uniqueContainerId) {
            // 阻止 Layui 默认刷新 (因为它只刷新 iframe)
            e.preventDefault();
            var currentLayId = $li.attr('lay-id');

            // 彻底销毁旧实例和 DOM
            destroyMicroApp(uniqueContainerId);
            bt.loading.show();

            // 模拟切换刷新
            setTimeout(function() {
                var allTabs = $('#navtabContainer li');
                var tempTab = null;

                allTabs.each(function(){
                    var id = $(this).attr('lay-id');
                    if (id !== currentLayId) {
                        tempTab = id;
                        return false;
                    }
                });

                if (tempTab) {
                    // layui.element().tabChange('card', tempTab);
                    setTimeout(function() {
                        layui.element().tabChange('card', currentLayId);
                        bt.loading.hide();
                    }, 100);
                } else {
                    // 只有一个 Tab 的情况
                    layui.element().tabChange('card', currentLayId);
                    bt.loading.hide();
                }
            }, 50);
        }

    });
    // 根据src / title 判断是否已经添加
    function getTitleId(src,title) {
        var id = -1;
        $("#navtabContainer").find("li").each(function(){
            // if(title === $(this).find('span').text()){
            var $span = $(this).find('>span');
            // 2. 只取第一个文本节点，过滤掉子 span（关闭按钮）的内容
            var spanTitle = $span.contents().first().text().trim();
            if(src === $(this).attr('url') && spanTitle === title){
                id = $(this).attr('lay-id');
            }
        });

        return id;
    }

    /**
     * 获取当前激活标签页 lay-id
     * @returns {string} lay-id
     */
    function getParentId() {
        const $currentLi = $('#navtabContainer li.layui-this');
        // 取不到属性返回空字符串
        return $currentLi.attr('lay-id') || '';
    }

    /**
     * 给当前激活标签设置 parentId 属性
     * @param {string|number} pid 父ID
     */
    function setParentId(pid) {
        const $currentLi = $('#navtabContainer li.layui-this');
        const currentId = $currentLi.attr('lay-id');

        // 无选中元素 / pid为空 / id相等，直接退出
        if (!$currentLi.length || !pid || currentId === pid) return;

        // 赋值parentId自定义属性
        $currentLi.attr('parentId', pid);
    }


    // /*------------微前端结束---------------*/
})
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

// 加载指标预警条数
function loadWarnDataCount() {
    $.ajax({
        type:"get",
        url:basePath + "/fundwarn/btwarndata/getWarnDataCount",
        dataType: "json",
        contentType: "application/json",
        success:function(result) {
            if(result == 0){
                $('#shakeEarlyWarningCount').text(0).addClass('layui-hide')
            }else {
                var count = result
                if(result > 99){
                    count = '99+'
                }
                $('#shakeEarlyWarningCount').text(count).removeClass('layui-hide')
            }

        },
        error:function (error) {
            clearInterval(loadPendingInformationTimeOut)
        }
    });
}

// 加载重点关注内容
function loadListStarTarget() {
    var tempData = {
        1:1
    };
    $.ajax({
        type:"POST",
        url:basePath + "/sys/notification/listStarTarget",
        data: JSON.stringify(tempData),
        dataType: "json",
        contentType : 'application/json;charset=utf-8',
        success:function(result) {
            var html = ''
            if(result && result.length){
                for (var i = 0; i < result.length; i++) {
                    var item = result[i]
                    if (item.tag == "netbank:bdlink:bisexc:fail") {
                        item.listUrl += "/0";
                    }
                    if (item.type == 1) {
                        item.listUrl = "/sys/notification/query/"+item.categoryId;
                    }
                    html += '<div data-name="' + item.categoryName + '" data-url="' + item.listUrl + '">'
                    html += '<div class="focus-important-title">'+item.categoryName;
                    html +='</div>'
                    var count = item.startTargetCount;
                    if(count > 999){
                        count = '999+'
                    }
                    html += '<div class="focus-important-amount">'+count
                    html +='</div>'
                    html +='</div>'
                }
            }else {
                html = '<div class="article-empty focus-empty"><img src="'+ basePath +'/static/v3.2/images/pic_blank.png"><div class="empty-text">'+bt.lang('common.msg.temporarily_no_data')+'</div></div>'
            }
            $('#listStarTarget').html(html)
        }
    });
}
//加载常用菜单方法
function loadCommonMenus() {
    $.ajax({
        type:"post",
        url:basePath + "/sys/menuCollect/widget",
        dataType: "html",
        contentType: "text/html",
        // data:JSON.stringify(data2),
        success:function(result) {
            $("#commonMenus").html(result);
            bt.use(['element'],function () {
                var $ = bt.jquery;
                bt.element().init()
                if(hoverCommonMenus){
                    hoverCommonMenus()
                }
            })
        }
    });
}
//加载待处理信息
function loadPendingInformation() {
    $.ajax({
        type:"get",
        url:basePath + "/bpm/my/todoInfo",
        dataType: "html",
        contentType: "application/json",
        success:function(result) {
            if(result && result.indexOf('toLoginType')!== -1){
                return
            }
            $("#workFlow").html(result);
            // 选择的已处理信息时
            if($('#finishedTab').hasClass('active')){
                $('#pendingInformation').removeClass('layui-show')
                $('#finishedInformation').addClass('layui-show')
            }
        },
        error:function (error) {
            clearInterval(loadPendingInformationTimeOut)
        }
    });
}

// 初始化日期提醒日历
function initExpireCalendar() {
    layui.laydate.render({
        elem: '#homeCalendar'
        ,position: 'static',
        showBottom: true,
        calendarTable:true, // 日历表格
        // 选择单个日期回调方法
        done: function(value, date, endDate){
            // 如果value无值，表示点击了重置，需要显示当天日期
            if(!value){
                value = bt.date.format(new Date).slice(0,10);
                // 加载当天日期提醒
                loadListExpireOnToday()
            }else{
                $('#selectDate').html(value);
                // 删除月份面的0
                var strArr = value.split("-");
                // 删除月份前面的0
                strArr[1] = strArr[1].replace(/^0/,'');
                // 如果有提醒标识，则查询提醒内容
                if( $('#calendar'+strArr.join("-")).hasClass('is-has')){
                    // 查询提醒内容
                    loadListExpireByCreateDate(value)
                }else {
                    emptyListExpire();
                }
                // 是否显示今字
                var today = bt.date.format(new Date).slice(0,10);
                if(value == today){
                    $('#thisMonthIcon').addClass('layui-hide')
                }else{
                    $('#thisMonthIcon').removeClass('layui-hide')
                }
            }
        },
        // 更改月份回调方法
        change:function (value, date, endDate) {
            // 判断是否切换月份，比较selectDate与当前月份是否一致，如果一致代表是选择单个日期，change不处理；如果不一致，代码切换月份
            var currentMonth = value.slice(0,7); // 当前月份
            var selectMonth =  $('#selectMonth').text(); // 选中月份
            // 如果不相等，说明切换了月份
            if(currentMonth != selectMonth){
                $('#selectDate').html(value)
                $('#selectMonth').html(currentMonth)
                var today = bt.date.format(new Date).slice(0,10);
                // 是否显示今字
                if(value == today){
                    $('#thisMonthIcon').addClass('layui-hide')
                }else{
                    $('#thisMonthIcon').removeClass('layui-hide')
                }
                // 获取当前月时间范围
                var currentStartDate = $('#homeCalendar').find('.layui-laydate-content tbody').children(":first").children(":first").attr('lay-ymd');
                var currentEndDate = $('#homeCalendar').find('.layui-laydate-content tbody').children(":last-child").children(":last-child").attr('lay-ymd');
                // 按日期范围查询提醒标识
                loadListExpire(currentStartDate,currentEndDate);
            }
        }
    });
    // 加载当天日期提醒
    loadListExpireOnToday()
}
// 加载当天日期提醒
function loadListExpireOnToday() {
    // 初始化日历的当前日期
    var thisMonth = bt.date.format(new Date).slice(0,7);
    $('#selectMonth').html(thisMonth);
    // 获取当前月时间范围
    var currentStartDate = $('#homeCalendar').find('.layui-laydate-content tbody').children(":first").children(":first").attr('lay-ymd');
    var currentEndDate = $('#homeCalendar').find('.layui-laydate-content tbody').children(":last-child").children(":last-child").attr('lay-ymd');
    // 按日期范围查询提醒标识
    loadListExpire(currentStartDate,currentEndDate);
    // 按日期查询提醒内容
    var today = bt.date.format(new Date).slice(0,10);
    // 隐藏今字
    $('#thisMonthIcon').addClass('layui-hide');
    // 加载当天的提醒内容
    loadListExpireByCreateDate(today)
    // 显示提醒内容标题
    $('#reminderTotal').parent().removeClass('layui-hide');
}
// 按日期查询提醒内容
function loadListExpireByCreateDate(value) {
    $('#selectDate').html(value);
    var tempData = {
        searchDate:value,
    };
    $.ajax({
        type:"POST",
        url:basePath + "/sys/notification/listExpireByCreateDate",
        data: JSON.stringify(tempData),
        dataType: "json",
        contentType : 'application/json;charset=utf-8',
        success:function(result) {

            if(result && result.length){
                var html = ''
                var length = result.length
                var currentDateCount = 0;
                for (var i = 0; i < length; i++) {
                    var item = result[i]
                    currentDateCount += Number(item.expireCount);
                    if (item.type == 1) {
                        item.listUrl = "/sys/notification/query/"+item.categoryId;
                    }
                    html += '<div class="list-content-item" data-name="'+item.categoryName+'" data-url="'+item.listUrl+'">';
                    html += '<span class="list-item-icon"></span>'
                    html += '<span>'+item.categoryName+'</span>'
                    var count = item.expireCount;
                    if(count > 99){
                        count = '99+'
                    }
                    html += '<span class="list-item-amount">'+count+'</span>'
                    html += '</div>'
                }
                $('#reminderTotal').text('有'+currentDateCount+'条提醒');
                $('#reminderListBox').html(html)
            }else {
                // 无数据时
                emptyListExpire()
            }

        }
    });
}
// 按日期范围查询提醒标识
function loadListExpire(startDate,endDate) {
    var tempData = {
        beginDate:startDate,
        endDate:endDate
    };
    $.ajax({
        type:"POST",
        url:basePath + "/sys/notification/listExpire",
        data: JSON.stringify(tempData),
        dataType: "json",
        contentType : 'application/json;charset=utf-8',
        success:function(result) {
            // 获取时间范围的提醒标识日期
            if(result){
                for (var i = 0; i < result.length; i++) {
                    var item = result[i];
                    if(item.expireDate) {
                        var strArr = item.expireDate.split("-");
                        // 删除月份前面的0
                        strArr[1] = strArr[1].replace(/^0/,'');
                        $('#calendar' + strArr.join('-')).addClass('is-has')
                    }
                }
            }
        }
    });
}

// 按日期查询提醒内容为空
function emptyListExpire() {
    var html = '<div class="article-empty focus-empty" style="text-align: center;"><img src="'+ basePath +'/static/v3.2/images/pic_blank.png"><div class="empty-text">'+bt.lang('common.msg.temporarily_no_data')+'</div></div>'
    $('#reminderTotal').text('有0条提醒');
    $('#reminderListBox').html(html)
}

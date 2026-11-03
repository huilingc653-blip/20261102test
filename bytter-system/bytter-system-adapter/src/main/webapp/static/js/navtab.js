/**
 * navtab功能
 * 本组件依赖layui的from，以及jquery
 * 周亮
 * 2017年4月27日
 * 0.0.1 tab功能封装。
 * 0.0.2 增加 新增Navtab api
 * 0.0.3 增加记录当前tab id 功能，增加关闭当前tab功能
 * 0.0.4 新增多个调用内部的方法
 * 0.0.5 关闭当前页面时，回到父页面
 * 0.0.6 增加getCurrentDom方法，获取当前页面对象
 * 0.0.7 对标题太长的，进行截取8个字符处理
 * 0.0.8 选项卡添加时由比较title改成比较url
 * 0.0.9 刷新父页面时，刷新页码
 * 0.0.10 点击主菜单时，新增加top参数，用于控制是否为顶级选项卡
 * 0.0.11 新增加刷新当前表格的方法refreshCurrentTable()
 * 0.0.11 新增加刷新父表格的方法refreshParentTable()
 * 0.0.12 新增加刷新父表格的方法refreshParentTable()
 * 0.0.13 解决firefox浏览器刷新的问题
 * 0.0.14 解决不点击刷新窗口时，关闭刷新窗口
 * 0.0.15 修改了refresh方法，解决子页面不关闭时，父页面要刷新
 * 0.0.16 当选卡换行后，其li index要减去span/br的个数
 * 0.0.17 修改了refresh方法，新增layid去查找对应的tabItem页面
 * 0.0.18 修改了refresh方法，重绘表格时，要按照当前页码与每页显示多少条来重绘
 * 0.0.19 refreshParent方法中，新增加第二个参数，解决IE浏览器表格刷新的问题
 * 0.0.20 修改了refresh方法 获取iframe层级结构
 * 0.0.21 新增加refreshParentWithoutDatatable方法，用于刷新没有datatable的父级页面
 */
layui.define(['jquery','element'],function(exports){
    var $  = layui.jquery,element = (parent.layui.element && parent.layui.element()) || layui.element();

    // === 微前端内部事件总线 ===
    var _eventEmitter = $({});

    // 安全触发器：确保业务代码报错不会搞崩组件
    function _safeTrigger(eventType, data) {
        try {
            var eventObj = $.Event(eventType);
            _eventEmitter.trigger(eventObj, [data]);
            return !eventObj.isDefaultPrevented();
        } catch (e) {
            console.error('Navtab Event Error [' + eventType + ']:', e);
            return true; // 即使报错也继续执行原有逻辑
        }
    }
    // =========================

    var Navtab = function () {
        this.config ={
            elem:undefined,
            cardName:'card',
            currentId:-1
        }
    }

    Navtab.prototype.v = '0.0.13';

    /**
     * 方法入口，参数判断，初始化
     * @param options
     */
    Navtab.prototype.init = function (options){
        // if (options.elem === undefined) {
        //     throwError('Navtab Error:请配置参数elem,此参数为jQuery选择器!');
        // }

        var that = this;
        $.extend(true, that.config, options);


        // that.bindNavEvent(); 解决jtree.js加载更慢的问题，影响到菜单添加的问题
        that.bindNavEvent1();
        that.bindRightClickEvent();
        that.appentMenuContext();

    }


    Navtab.prototype.setCurrentId = function(currentId){
        var select = parent?$(parent.document): $(document);
        select.find('.admin-contextmenu').attr('currentId',currentId);
    }

    Navtab.prototype.getCurrentId = function(){
        var select = parent?$(parent.document): $(document);
        return select.find('.layui-tab-title-menu .layui-this').attr('lay-id');
    }

    Navtab.prototype.addNavtabByMenus = function(elem){
        var that = this
        var _config = that.config;
        var card    = _config.cardName;  // 选项卡对象
        var title   = elem.find('.menu-name').text().trim(); // 导航栏text
        var src     = elem.attr('href-url');      // 导航栏跳转URL
        var name    = elem.attr('name');      // name属性
        var top     = true;//新增加top参数，代表选项卡为顶级选项卡，无父级
        if(src == "#"){
            return;
        }
        var id = elem.data('id')
        var contact = '?';
        if(src.indexOf("?")>-1){
            contact = '&';
        }
        src = src + contact + "menuId=" + id;
        title = title?title:name;
        that.addNavtab(title,src,name,undefined,elem.data('topparentid'));//新增加top参数，代表菜单为顶级菜单，无父级
        elem.addClass("active")
    }

    // 收藏菜单
    Navtab.prototype.collectMenu = function(elem) {
        var menuId = elem.data('id')
        if (!menuId) {
            return
        }
        $.get(basePath + '/sys/menuCollect/collect', {menuId: menuId}, function (res) {
            var result = JSON.parse(res)
            if (result.success) {
                var className = 'menus-'+menuId
                var targetIconElems = $('.'+ className)
                if (targetIconElems.hasClass('byicon_collection_full')) {
                    targetIconElems
                        .removeClass('byicon_collection_full')
                        .addClass('byicon_collection')
                    // 取消收藏成功
                    layer.msg(bt.lang('sys.index.deleteCollectMenu.failure'));
                } else {
                    targetIconElems
                        .addClass('byicon_collection_full')
                        .removeClass('byicon_collection')
                    // 添加收藏成功
                    layer.msg(bt.lang('sys.index.addCollectMenu.success'));
                }
                // 刷新收藏模块

                $.ajax({
                    type: "post",
                    url: basePath + "/sys/menuCollect/widget",
                    dataType: "html",
                    contentType: "application/json",
                    // data:JSON.stringify(data2),
                    success: function (result) {
                        $("#commonMenus").html(result);
                        if(hoverCommonMenus){
                            hoverCommonMenus()
                        }
                        element.init();
                    }
                })
            }
        })
    }
    Navtab.prototype.bindNavEvent = function(){
        var that = this;
        //监听导航(side)点击切换页面
        $('[data-type="tabAdd"]').off('click').on('click',function (){
            that.addNavtabByMenus($(this))
        })
    }

    Navtab.prototype.bindNavEvent1 = function(){
        var that = this;
        //监听导航(side)点击切换页面
        $('#childMenu').on('click','[data-type="tabAdd"]',function(e){
            if( e.target.className.indexOf('collection') !== -1 ){
                that.collectMenu($(this))
            }else{
                that.addNavtabByMenus($(this))
               // $('#childMenu').hide()
               // $('#menuLayer').removeClass('child-open').hide()
            }
            e.stopPropagation()
        })
        //监听导航(side)点击切换页面
        $('#menuLayer').on('click','[data-type="tabAdd"]',function(e){
            if( e.target.className.indexOf('collection') !== -1 ){
                that.collectMenu($(this))
            }else{
                that.addNavtabByMenus($(this))
               // $('#menuLayer').removeClass('child-open').hide()
              //  $('#childMenu').hide()
            }
        })

        // 监听导航side 关闭icon 关闭页面
        $('#navtabContainer').on('click','.layui-tab-close', function () {
            var id = $(this).parents('li').attr('lay-id')
            var parentId = $(this).parents('li').attr('parentId')

            if(id==0){
                return;
            }

            // === [新增] 关闭前事件 (可阻止) ===
            var $li = $(this).parents('li');
            if (!tab._fire('beforeClose', { id: id, $element: $li, type: 'click_x' })) {
                return; // 如果业务层阻止，直接返回
            }
            // =========================

            //当提交关闭当前页面时，移除上传标识，代表已提交了；解决有上传组件页面，点击保存时关闭当前页，提示上传内容未提交的问题。
            var select = parent?$(parent.document): $(document);
            select.find('.layui-tab-title').find('li[lay-id=' + id + ']').removeAttr('upload');

            //回到父页面并刷新
            if(parentId && parentId.indexOf("card") != -1){
                element.tabChange('card',parentId);
                // if(tableRefresh !=='false'){
                //     that.refresh(parentId,'datatable');
                // }
            }
            //关闭当前页面
            that.close(id);

            // === 关闭后事件 ===
            tab._fire('afterClose', { id: id, type: 'click_x' });
        })

        var tips,tipsBgc = '#fff';
        if($('body').hasClass('dark-skin')){
            tipsBgc = '#25262B'
        }

        $('#navtabContainer ').on('mouseenter','.navtab-tips',function(){
            tips = layer.tips('<span class="navtab-tips-text">'+$(this).data('title')+'</span>',$(this).parent(),{tips:[3,tipsBgc]})
        })
        $('#navtabContainer ').on('mouseleave','.navtab-tips',function(){
            layer.close(tips)
        })
    }

    Navtab.prototype.bindRightClickEvent = function (){
        var that = this;
        var _config = this.config;
        var timerId;
        var mouseEnterFlag = false;
        //绑定右键菜单
        $(document).on('contextmenu','.layui-tab-title-menu li',function(e) {
            e.preventDefault();
            clearTimeout(timerId);
            //IE8 e.which ===0
            if ( !0 === e.which || !3 === e.which) {
                return;
            }
            var $that = $(e.target);
            if($that[0].nodeName!="LI"){
                $that = $that.parents("li");
            }
            _config.currentId = $that.attr('lay-id');
            if(_config.currentId==0){
                return;
            }
            var left = $that.offset().left + 5;
            var top = $that.offset().top + $that.height() + 5;
            $(".admin-contextmenu").css({'left':left,'top':top}).show();
            //解决不点击刷新窗口时，隔2秒隐藏它
            if(!mouseEnterFlag){
                timerId = setTimeout(function () {
                    if(!mouseEnterFlag){
                        $('.admin-contextmenu').hide();
                    }
                },2000)
            }
        });

        $('body').unbind('click').bind("click",function(e){
            var e = e || window.event;
            var targetElem = e.target || e.srcElement;
            var target;
            if( targetElem.tagName === 'I'){ //修复点击图标时，出现无效的问题
                targetElem = e.target.parentNode;
            }
            target = $(targetElem).data('target');

            $(".admin-contextmenu").hide();

            if(typeof (target)!="undefined"){
                var currentId = _config.currentId||''; // 获取当前ID用于传递数据
                var $currentLi = $('.layui-tab-title-menu li[lay-id="' + currentId + '"]');
                switch (target) {
                    case 'refresh': //刷新当前

                        tab._fire('beforeRefresh', { id: currentId, $element: $currentLi });
                        that.refreshCurrent();
                        tab._fire('afterRefresh', { id: currentId });

                        break;
                    case 'closeCurrent': //关闭当前
                        if (!tab._fire('beforeClose', { id: currentId, $element: $currentLi, type: 'context_current' })) {
                            break; // 阻止关闭
                        }
                        that.closeCurrent();
                        tab._fire('afterClose', { id: currentId, type: 'context_current' });
                        break;
                    case 'closeOther': //关闭其他
                        $('.layui-tab-title').children('li').each(function () {
                            // var layId = $(this).attr('lay-id');
                            // if (layId !== undefined && layId !=0 && layId != _config.currentId) {
                            //     element.tabDelete(_config.cardName, layId);
                            // }
                            var layId = $(this).attr('lay-id');
                            var $li = $(this);
                            if (layId !== undefined && layId !=0 && layId != _config.currentId) {
                                try {
                                    if (!tab._fire('beforeClose', { id: layId, $element: $li, type: 'context_other' })) {
                                        return true; // continue
                                    }

                                    element.tabDelete(_config.cardName, layId);

                                    tab._fire('afterClose', { id: layId, type: 'context_other' });
                                } catch (e) {
                                    console.warn('Close other error', e);
                                    return true;
                                }
                            }
                        });
                        break;
                    case 'closeAll': //全部关闭
                        $('.layui-tab-title').children('li').each(function () {
                            // var $t = $(this);
                            // if ($t.index() !== 0) {
                            //     element.tabDelete(_config.cardName, $t.attr('lay-id'));
                            // }
                            var $t = $(this);
                            if ($t.index() !== 0) {
                                var layId = $t.attr('lay-id');
                                try {
                                    if (!tab._fire('beforeClose', { id: layId, $element: $t, type: 'context_all' })) {
                                        return true; // continue
                                    }

                                    element.tabDelete(_config.cardName, $t.attr('lay-id'));

                                    tab._fire('afterClose', { id: layId, type: 'context_all' });
                                } catch (e) {
                                    console.warn('Close all error', e);
                                    return true;
                                }
                            }
                        });
                        break;
                }
            }
        });
        // 解决不点击刷新窗口时，隔2秒隐藏它
        $(document).on('mouseleave ','.admin-contextmenu',function () {
            mouseEnterFlag = false;
            clearTimeout(timerId);
            var that = this;
            timerId = setTimeout(function () {
                $(that).hide();
            },2000);
        })
        // 解决不点击刷新窗口时，隔2秒隐藏它,添加进入的标识
        $(document).on('mouseenter','.admin-contextmenu',function () {
            mouseEnterFlag = true;
            clearTimeout(timerId);
        })
    }

    Navtab.prototype.appentMenuContext = function (){
        var content = $('<div class="admin-contextmenu layui-anim layui-anim-upbit"></div>');
        var ul = '<ul>';
        // ul += '<li data-target="refresh" title="'+ bt.lang("sys.main.label.title.refresh") +'"><i class="fa fa-refresh" aria-hidden="true"></i>' + bt.lang("sys.main.label.refresh") + '</li>';
        // ul += '<li data-target="closeCurrent" title="'+ bt.lang("sys.main.label.title.close.current") +'"><i class="fa fa-close" aria-hidden="true"></i>' + bt.lang("sys.main.label.close.current") + '</li>';
        // ul += '<li data-target="closeOther" title="'+ bt.lang("sys.main.label.title.close.other") +'"><i class="fa fa-window-close-o" aria-hidden="true"></i>' + bt.lang("sys.main.label.close.other") + '</li>';
        // ul += '<li data-target="closeAll" title="'+ bt.lang("sys.main.label.title.close.all") +'"><i class="fa fa-window-close-o" aria-hidden="true"></i>' + bt.lang("sys.main.label.close.all") + '</li>';
        ul += '<li data-target="refresh" ><div data-target="refresh">' + bt.lang("sys.main.label.refresh") + '</div></li>';
        ul += '<li data-target="closeCurrent" ><div data-target="closeCurrent">' + bt.lang("sys.main.label.close.current") + '</div></li>';
        ul += '<li data-target="closeOther" ><div data-target="closeOther">' + bt.lang("sys.main.label.close.other") + '</div></li>';
        ul += '<li data-target="closeAll" ><div data-target="closeAll">' + bt.lang("sys.main.label.close.all") + '</div></li>';
        ul += '</ul>';

        content.html(ul);
        $("body").append(content);
    }

    /**
     * 刷新当前
     */
    Navtab.prototype.refreshCurrent = function (id){
        var that = this;
        var _config = this.config;
        if(id){
            _config.currentId = id;
        }
        element.tabChange(_config.cardName, _config.currentId);
        that.refresh(_config.currentId);
    }

    /**
     * 判定浏览器类型
     * @param id
     */
    function getOs(){
        var OsObject = "";
        if (!!window.ActiveXObject || "ActiveXObject" in window){
        // if(navigator.userAgent.indexOf("MSIE")>0) {
            return "MSIE";
        }
        if(navigator.userAgent.indexOf("Firefox")>0){
            return "Firefox";
        }
        if(navigator.userAgent.indexOf("Safari")>0) {
            return "Safari";
        }
        if(navigator.userAgent.indexOf("Camino")>0){
            return "Camino";
        }
        if(navigator.userAgent.indexOf("Gecko/")>0){
            return "Gecko";
        }
    }

    function ieVersion() {
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
        var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器
        var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器
        var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
        if(isIE) {
            var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
            reIE.test(userAgent);
            var fIEVersion = parseFloat(RegExp["$1"]);
            if(fIEVersion == 7) {
                return 7;
            } else if(fIEVersion == 8) {
                return 8;
            } else if(fIEVersion == 9) {
                return 9;
            } else if(fIEVersion == 10) {
                return 10;
            } else {
                return 6;//IE版本<=7
            }
        } else if(isEdge) {
            return 'edge';//edge
        } else if(isIE11) {
            return 11; //IE11
        }else{
            return -1;//不是ie浏览器
        }
    }


    /**
     * 刷新指定id 的navtab
     * @param id
     * liuyj 2018-04-27 由原来的index去获取item，改成由layid去查找对应的item
     */
    Navtab.prototype.refresh = function (id,param){
        var _config = this.config;
        var select = parent?$(parent.document): $(document);
        var cardElem = select.find('.layui-tab-title').find('li[lay-id=' + id + ']');
        var index   = cardElem.index();
        if( id.indexOf('card')> -1 ){ //当是首页选项卡时
            var content = select.find('.layui-tab-content').children('[data-tab-id ="'+ id +'"]');
        }else{ //当为页面内部嵌套选项卡时
            var content = select.find('.layui-tab-content').find('.layui-tab-item:eq('+index+')');
        }
        // var content = select.find('.layui-tab-content').find('.layui-tab-item:eq('+index+')');

        // var index   = $('.layui-tab-title').find('li[lay-id=' + id + ']').index();
        // var content = $('.layui-tab-content').find('.layui-tab-item:eq('+index+')');

        if(!content.length){ //当找不到指定页面时，返回空
            return '';
        }

        var isFirefox= getOs() =='Firefox'? true:false; //判断是否是Firefox
        var isIe= ieVersion() !== -1 ? true:false; // 解决ie不能刷新表格的问题
        if(isFirefox){
            if(!content.hasClass('layui-show')){
                content.data('refresh','true');//解决火狐浏览器刷新父页面时的问题
            }
        }
        var iframe  =   content.find('iframe')[0];
        if(!param){
            if(iframe){
                iframe.src = iframe&&iframe.src;
            }
            if(!cardElem.hasClass('layui-this')){
                cardElem.addClass('innerTab').removeClass('init');//添加点击刷新标识，解决添加时数据时，刷新父页面，子页面未关闭
            }
        }else{
            var fname = iframe&&iframe.name;
            //var iframeDom = top.frames[fname];
            //var iframeDom = window.frames[fname];//修改为window，解决iframe嵌套选项卡的问题
            var currentWindow = parent?parent.window: window;
            var iframeDom = currentWindow.frames[fname];//修改为window，解决iframe嵌套选项卡的问题
            if(param !='datatable'){
                return iframeDom;
            }else{
                //var lengthMenu = iframeDom.$(".dataTables_wrapper .datatable-page").find('select').val();//获取当前表格的每页显示多少条信息；
               if(!iframeDom || !iframeDom.$){
                    return
               }
               // var lengthMenu = iframeDom.$(".dataTables_length select option:selected").val();
                var lengthMenu = iframeDom.$.fn.dataTable?iframeDom.$.fn.dataTable.tables( { visible: true, api: true} ).page.len() :10;
                if(iframeDom.$.fn.dataTable && lengthMenu ){
                    iframeDom.$.fn.dataTable.tables( { visible: true, api: true} ).page.len( lengthMenu ).draw('page');// 对表格进行重绘，要求页码/每页多少条不变
                }
                if( isIe ){ //解决ie浏览器刷新样式的问题
                    if(!cardElem.hasClass('layui-this')){
                        cardElem.addClass('innerTab').removeClass('init');//添加点击刷新标识，解决添加时数据时，刷新父页面，子页面未关闭
                    }
                }
                return  iframeDom
            }
        }
    }
    /**
     * 刷新当前表格
     */
    Navtab.prototype.refreshCurrentTable = function (id){
        var that = this;
        var _config = this.config;
        if(id){
            _config.currentId = id;
        }
        element.tabChange(_config.cardName, _config.currentId);
        that.refresh(_config.currentId,'datatable');
    }

    Navtab.prototype.closeCurrent = function (tableRefresh){
        var $this = this;
        if ($this.config.currentId !== 0) {
            var parentId = tab.getParentId();
            var currentId = $this.getCurrentId();
            if(currentId==0){
                return;
            }
            //当提交关闭当前页面时，移除上传标识，代表已提交了；解决有上传组件页面，点击保存时关闭当前页，提示上传内容未提交的问题。
            var select = parent?$(parent.document): $(document);
            select.find('.layui-tab-title').find('li[lay-id=' + currentId + ']').removeAttr('upload');

            //回到父页面并刷新
            if(parentId && parentId.indexOf("card") != -1){
                element.tabChange('card',parentId);
                if(tableRefresh !=='false'){
                    $this.refresh(parentId,'datatable');
                }
            }
            // 如果有微前端
            var currentLi = select.find('.layui-tab-title').find('li[lay-id=' + currentId + ']');
            var uniqueContainerId =currentLi.data('micro-app-container-id');

            if (currentLi.hasClass('micro-app-tab') && uniqueContainerId) {
                console.log('closeTab_微前端')
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

            //关闭当前页面
            $this.close(currentId);
        }
    }

    Navtab.prototype.close = function (id){
        element.tabDelete(this.config.cardName, id);
        // 如果导航只余下首页，删除navtab-active(更改导航背影色)
        var lis = $('#navtabContainer').find('li')
        if(lis && lis.length && lis.length === 1){
            $('#bodyContainer').removeClass('navtab-active')
            $('#navtabContainer').find('li').addClass('layui-this')
        }
        $('#sideMenuNav').find('a.active').removeClass('active')
    }


    // 根据导航栏text获取lay-id
    Navtab.prototype.getTitleId = function (card,title,src){
        var id = -1;
        var select = parent?$(parent.document): $(document);
        select.find(".layui-tab[lay-filter=" + card + "] ul li").each(function(){
            // if(title === $(this).find('span').text()){
            if(src === $(this).attr('url')){
                id = $(this).attr('lay-id');
            }
        });

        return id;
    }
    Navtab.prototype.checkRepeatId = function (card,id){
        var flag = false;
        var select = parent?$(parent.document): $(document);
        select.find(".layui-tab[lay-filter=" + card + "] ul li").each(function(){
            if(id == $(this).attr('lay-id')){
                flag = true;
                return;
            }
        });
        return flag;
    }

    Navtab.prototype.addNavtab = function (title,src,name,top,topParentId){ //新增加top参数，代表菜单为顶级菜单，无父级
        var that = this;
        var id      = that.config.cardName + new Date().getTime();     //使用时间戳作为ID
        that.addNavtabWithId(title,src,id,name,top,topParentId);//新增加top参数，代表菜单为顶级菜单，无父级
    }

    Navtab.prototype.addNavtabWithId = function (title,src,id,name,top,topParentId){ //新增加top参数，代表菜单为顶级菜单，无父级
        var that = this;
        var _config = that.config;
        var flag;
        //组件生成的ID，带有card开头
        if(id.indexOf(_config.cardName) != -1){
            flag = that.getTitleId(_config.cardName, title,src);       // 是否有该选项卡存在
        }else {
            if(that.checkRepeatId(id)){
                layer.alert(bt.lang('common.tab.repeatedTab')+'!'); // 选项卡重复！
                return;
            }else{
                flag = id;
            }
        }
        // 大于0就是有该选项卡了
        if(flag != -1){
            id = flag;
        }else{
            //新
            //标题太长的，截取字段
           // var titleHtml = '<div class="navtab-left"></div>';
            var titleHtml = '';
            var sampleLength = 8;
            if (window.top.webLocale){
                if (window.top.webLocale == 'en_US'){ //如果为英文环境时，标题截取字段为24
                    sampleLength = 24
                }
            }
            // topParentId设置最顶级的id,用于选中顶菜单
            var text = ''
            if(title !==undefined && title.length > sampleLength){
                text = title.substring( 0, sampleLength )+'...'
                titleHtml += '<span data-topparentid="'+topParentId+'" data-title = "'+title+'" class="navtab-name navtab-tips">'+ text+'<span class="nav-tab-close"><i class="layui-icon layui-unselect layui-tab-close">ဆ</i></span></span>'
            }else{
                text = title
                titleHtml += '<span data-topparentid="'+topParentId+'" class="navtab-name" data-title = "'+title+'">'+ text+'<span class="nav-tab-close"><i class="layui-icon layui-unselect layui-tab-close">ဆ</i></span></span>'
            }
           // titleHtml += '<div class="navtab-center navtab-name" title = "'+title+'">'+ text+'<i class="layui-icon layui-unselect layui-tab-close">ဆ</i></div>'
           // titleHtml += '<span title = "'+title+'" class="navtab-name">'+ text+'<span class="nav-tab-close"><i class="layui-icon layui-unselect layui-tab-close">ဆ</i></span></span>'
           // titleHtml += '<div class="navtab-right"></div>'

            // 如果首页页签不显示时，需要显示，并调整容器的高度
            // 如果是首次新增页签时，需要调整高度
            if(!$('#navtabContainer').is(':visible')){
                $('#menusTabCard').addClass('is-add-menu')
                var height = $(window).height() - $('.layout-header').height() - 32
                $('#tab-content').height(height);

                //设置首页工作台常用菜单父容器的高度
                // var cardHeight = height - $('#tabsHeader').height() - 20

                // $('#tabsContent').height(cardHeight)
            }
            element.tabAdd(_config.cardName, {
                titleText:text,
                title: titleHtml,
                src:src
                , content: '<iframe src="' + src + '" frameborder="0" name ="' + (name?name:id)+ '"></iframe>'
                , id: id
            });
            that.setParentId();
        }
        that.setCurrentUrl(src,id);
        var parentId;
        if(!top){ //非顶级菜单不用设置父 id
            parentId = that.getCurrentId();
        }
        // var parentId = that.getCurrentId();
        // 切换相应的ID tab
        element.tabChange(_config.cardName, id);
        that.setCurrentId(id);
        that.setParentId(parentId);


        $('#bodyContainer').addClass('navtab-active')
        $('#navtabContainer').show()
    }

    Navtab.prototype.setParentId = function (parentId){
        if ( !parentId || parentId == "0"){ //无父id时return
            return;
        }
        var select = parent?$(parent.document): $(document);
        var parentLi = select.find('.layui-tab-title-menu li[lay-id="'+ parentId +'"]');
        if ( parentLi.hasClass('layui-this') ){ // 如果父li就是当前页面时retrun 原因：当页签添加个数达到最大时，当页就是父，子未被添加，会造成关闭页签错误
            return
        }
        select.find('.layui-tab-title-menu .layui-this').attr('parentId',parentId);
    }

    Navtab.prototype.getParentId = function (){
        var select = parent?$(parent.document): $(document);
        return select.find('.layui-tab-title-menu .layui-this').attr('parentId');
    };
    //设置当前选项卡的url
    Navtab.prototype.setCurrentUrl = function (src,id){
        var select = parent?$(parent.document): $(document);
        src = src ? src:'';
        select.find('.layui-tab-title-menu li[lay-id="'+id+'"]').attr('url',src);
    };

    Navtab.prototype.getIdByURL = function (url){
        var select = parent?$(parent.document): $(document);
        return select.find('.layui-tab-title-menu li[href-url="'+url+'"]').attr("lay-id");
    };


    function throwError(msg) {
        throw new Error(msg);
    };

    var tab = new Navtab();
    var obj = {
        init: function(option){
            tab.init(option);
            if(bt){
                bt.navtab = this;
            }
        },
        addNavtab:function(title,src,name){
            tab.addNavtab(title,src,name);
        },
        addNavtabBySearch:function(title,src,name,topParentId){ // 用于首页搜索菜单添加页面签
            tab.addNavtab(title,src,name,undefined,topParentId);
        },
        addNavtabWithId:function(title,src,id,name){
            tab.addNavtabWithId(title,src,id,name);
        },
        closeCurrent:function(tableRefresh){ //关闭当前tab时，刷父datatable页,如果tableRefresh为false时，不刷新datable;
            tab.closeCurrent(tableRefresh);
        },
        close:function(id){
            tab.close(id);
        },
        refreshCurrent:function(){
            var currentId = tab.getCurrentId();
            tab.refreshCurrent(currentId);
        },
        refreshParent:function(){
            var parentId = tab.getParentId();
            if(parentId && parentId.indexOf("card") != -1){
                tab.refresh(parentId,'datatable');//解决IE浏览器隐藏表格刷新后变形的问题
            }
        },
        refresh:function(id){
            tab.refresh(id);
        },
        getCurrentNavTabId:function(){
            return tab.getCurrentId();
        },
        getParentDom:function () {
            var parentId = tab.getParentId();
            if(parentId && parentId.indexOf("card") !== -1){
                var dom = tab.refresh(parentId,true);
                return dom;
            }
        },
        getCurrentDom:function () {
            var currentId = tab.getCurrentId();
            if(currentId && currentId.indexOf("card") !== -1){
                var dom = tab.refresh(currentId,true);
                return dom;
            }
        },
        refreshCurrentTable:function(){ //刷新当前页面datatable
            var currentId = tab.getCurrentId();
            tab.refreshCurrentTable(currentId);
        },
        refreshParentTable:function(){ //刷新父页面datatable  不切换选项卡
            var parenttId = tab.getParentId();
            tab.refresh(parenttId,'datatable');
        },
        getNavtabIdByURL:function(url){
            return tab.getIdByURL(url);
        },
        reload:function(){
            var currentId = tab.getCurrentId();
            tab.reload(currentId);
        },
        refreshParentWithoutDatatable:function(){ //刷新没有datable的父级页面
            var parentId = tab.getParentId();
            if(parentId && parentId.indexOf("card") != -1){
                tab.refresh(parentId);
            }
        }
    };

    obj.on = function(eventType, callback) {
        _eventEmitter.on(eventType, callback);
        return obj;
    };
    obj.off = function(eventType, callback) {
        _eventEmitter.off(eventType, callback);
        return obj;
    };
    // 将触发器挂载到 tab 实例上，供内部调用
    tab._fire = _safeTrigger;

    //输出navtab接口
    exports('navtab', obj);
});

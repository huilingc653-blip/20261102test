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

        that.bindNavEvent();
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

    Navtab.prototype.bindNavEvent = function(){
        var that = this;
        var _config = that.config;
        //监听导航(side)点击切换页面
        $('[data-type="tabAdd"]').click(function(){
            var elem = $(this);
            var card    = _config.cardName;  // 选项卡对象
            var title   = elem.find('.menu-name').text().trim(); // 导航栏text
            var src     = elem.attr('href-url');      // 导航栏跳转URL
            var name    = elem.attr('name');      // name属性
            var top     = true;//新增加top参数，代表选项卡为顶级选项卡，无父级
            if(src == "#"){
                return;
            }
            var id = $(this).data('id')
            var contact = '?';
            if(src.indexOf("?")>-1){
                contact = '&';
            }
            src = src + contact + "menuId=" + id;
            title = title?title:name;
            that.addNavtab(title,src,name,top);//新增加top参数，代表菜单为顶级菜单，无父级
            $(this).addClass("active")
        })
        // element.on('nav(side)', function (elem) {
        //     var card    = _config.cardName;                                   // 选项卡对象
        //     var title   = elem.text();                              // 导航栏text
        //     var src     = elem.children('a').attr('href-url');      // 导航栏跳转URL
        //     if(src == "#"){
        //         return;
        //     }
        //     that.addNavtab(title,src);
        // });
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
                switch (target) {
                    case 'refresh': //刷新当前
                        that.refreshCurrent();
                        break;
                    case 'closeCurrent': //关闭当前
                        that.closeCurrent();
                        break;
                    case 'closeOther': //关闭其他
                        $('.layui-tab-title').children('li').each(function () {
                            var layId = $(this).attr('lay-id');
                            if (layId !== undefined && layId !=0 && layId != _config.currentId) {
                                element.tabDelete(_config.cardName, layId);
                            }
                        });
                        break;
                    case 'closeAll': //全部关闭
                        $('.layui-tab-title').children('li').each(function () {
                            var $t = $(this);
                            if ($t.index() !== 0) {
                                element.tabDelete(_config.cardName, $t.attr('lay-id'));
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
        ul += '<li data-target="refresh" >' + bt.lang("sys.main.label.refresh") + '</li>';
        ul += '<li data-target="closeCurrent" >' + bt.lang("sys.main.label.close.current") + '</li>';
        ul += '<li data-target="closeOther" >' + bt.lang("sys.main.label.close.other") + '</li>';
        ul += '<li data-target="closeAll" >' + bt.lang("sys.main.label.close.all") + '</li>';
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
            iframe.src =    iframe.src;
            if(!cardElem.hasClass('layui-this')){
                cardElem.addClass('innerTab').removeClass('init');//添加点击刷新标识，解决添加时数据时，刷新父页面，子页面未关闭
            }
        }else{
            var fname = iframe.name;
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
                var lengthMenu = iframeDom.$.fn.dataTable.tables( { visible: true, api: true} ).page.len()
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
            //关闭当前页面
            $this.close(currentId);
        }
    }

    Navtab.prototype.close = function (id){
        element.tabDelete(this.config.cardName, id);
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

    Navtab.prototype.addNavtab = function (title,src,name,top){ //新增加top参数，代表菜单为顶级菜单，无父级
        var that = this;
        var id      = that.config.cardName + new Date().getTime();     //使用时间戳作为ID
        that.addNavtabWithId(title,src,id,name,top);//新增加top参数，代表菜单为顶级菜单，无父级
    }

    Navtab.prototype.addNavtabWithId = function (title,src,id,name,top){ //新增加top参数，代表菜单为顶级菜单，无父级
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
            var titleHtml = '';
            var sampleLength = 8;
            if (window.top.webLocale){
                if (window.top.webLocale == 'en_US'){ //如果为英文环境时，标题截取字段为24
                    sampleLength = 24
                }
            }
            if(title !==undefined && title.length > sampleLength){
                titleHtml += '<span title = "'+title+'">' + title.substring( 0, sampleLength )+'...</span><span class="t2-tab-divider"></span>'
            }else{
                titleHtml += '<span title = "'+title+'">' + title +'</span><span class="t2-tab-divider"></span>'
            }
            element.tabAdd(_config.cardName, {
                title: titleHtml
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
    //输出navtab接口
    exports('navtab', obj);
});

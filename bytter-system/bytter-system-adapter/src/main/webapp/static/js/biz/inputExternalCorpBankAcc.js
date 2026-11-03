/**
 * Created by liuyj on 2018/7/4.
 */
/**
 * 下拉客商帐号组件
 * 本组件依赖jquery form
 * liuyj
 * 2018年7月04日
 * 0.0.1 封装组件 输入匹配才显示下拉框
 */
bt.define(['jquery','form'], function(exports){
    "use strict";
    var $ = bt.jquery,form = bt.form();
    var THIS = 'layui-this', SHOW = 'layui-show', HIDE = 'layui-hide', DISABLED = 'layui-disabled', CLASS = 'layui-form-select', TITLE = 'layui-select-title';
    var CorpBank =function(){
        this.config={
            elem:".bt-biz-inputExternalCorpBankAcc",//容器
            name:undefined,
            defaultValue:undefined,//默认值
            placeholder:undefined,//占位符
            url:undefined,//请求
            method:"GET",//请求方式
            valueField: "",
            textField:"",
            required:false,//是否必填
            change:undefined,//选中回调
            initCallback:undefined //初始化完成回调
        };
        this.bizName = "inputExternalCorpBankAcc";
        this.selectObjArr =[];
        this.currentObject = null;
    }
    /**
     * 参数设置
     * @param {Object} options
     */
    CorpBank.prototype.set = function(elem){
        var that = this;
        var settings = {};
        var config = {
            method:"GET",//请求方式
            required:false,//是否必填
        }
        var $container = settings.elem = elem;
        settings.url = $container.data("url");
        settings.valueField = $container.data("valueField".toLowerCase());
        settings.textField = $container.data("textField".toLowerCase());
        settings.defaultValue = $container.data("defaultvalue".toLowerCase());
        settings.bizName = $container.data("biz-name");
        settings.placeholder = $container.data("placeholder");
        settings.method = $container.data("method");
        settings.width = $container.data("width");
        settings.urldata = $container.data("urlData".toLowerCase());
        var initCallback = $container.data("initCallback".toLowerCase());//初始完成的回
        var change = $container.data("callback");//选择时的回调
        var search = $container.data("search");//选择检索回调

        settings.change = makeCallBack(change);
        settings.search = makeCallBack(search);
        settings.initCallback = makeCallBack(initCallback);

        //if (settings.url === undefined) {
        //    throwError('inputExternalCorpBankAcc Error:请配置远程数据URL!');
        //}

        var config = $.extend(true,config, settings);

        return config;
    }
    //初始化
    CorpBank.prototype.init = function(ops){
        var that = this;
        var domObj = $('.bt-biz-inputExternalCorpBankAcc:not(.init)');
        bt.each(domObj,function(){
            var settings = that.set($(this));//初始化参数
            that.initCorpBank(settings);
        })
    }
    //重绘方法
    CorpBank.prototype.render = function(){
        this.initCorpBank(this.currentObject);
    }
    //初始化组件内容
    CorpBank.prototype.initCorpBank = function(settings){
        var that = this;
        if( settings.url === "" || !settings.url ){
            that.initData(settings);
        }else{
            if( !settings.isAjaxing ){
                settings.isAjaxing = true;
                if(settings.url != undefined && settings.method.toLowerCase() =="post"){
                    $.post(settings.url,settings.urldata,function(data){
                        if(data){
                            settings.data = data;
                            settings.isAjaxing = false;
                            that.initData(settings);
                            settings.elem.parent().parent().addClass("layui-form-selected");
                        }
                    },"json")
                }
                if(settings.url != undefined && settings.method.toLowerCase() =="get"){
                    $.get(settings.url,function(data){
                        if(data){
                            settings.data = data;
                            settings.isAjaxing = false;
                            that.initData(settings);
                            settings.elem.parent().parent().addClass("layui-form-selected");
                        }
                    },"json")
                }
            }
        }
    }
    //初始化数据
    CorpBank.prototype.initData = function(settings){
        var that = this;
        that.initContainer(settings);
        if( settings.initCallback ){ //初始化完成回调
            settings.initCallback(settings.data);
        }
        that.listenEvent(settings);

        //初始的配置
        if(!bt.inputExternalCorpBankAcc.config){
            bt.inputExternalCorpBankAcc.config  = [];
        }
        if( settings.bizName ){
            bt.inputExternalCorpBankAcc.config[settings.bizName] = settings;
        }
    }
    //初始化容器
    CorpBank.prototype.initContainer = function(settings){
        var that = this;
        if(!settings.reset){
            var $hiddenInput = $('<a href="javascript:void(0);" class="delIcon"><i class="layui-icon bt-selectTableType-close" style="right: 4px;top:4px">ဆ</i></a>');
            settings.elem.wrap('<div class="layui-unselect  inputExternalCorpBankAccContainer '+ CLASS + (settings.disabled ? ' layui-select-disabled' : '') +'"><div class="'+TITLE+'" title="'+settings.elem.val()+'"></div>');
            settings.reset = false;
        }
        var html = '<dl class="layui-anim layui-anim-upbit">'+ function(result){
                var arr = [];
                var value = settings.valueField,text = settings.textField,defaultvalue = settings.defaultvalue;
                bt.each(result, function(index, item){
                    arr.push('<dd lay-value="'+ item[value] +'" data-index="'+ index +'"  class="'+ ( defaultvalue === item[value] ?  THIS : '') + (item.disabled ? (' '+DISABLED) : '') +'">'+ item[text] +'</dd>');
                });
                arr.length === 0 && arr.push('<dd lay-value="" class="'+ DISABLED +'">'+bt.lang("common.select.noData")+'</dd>'); //没有选项 layui2.0 liuyj
                return arr.join('');
            }(settings.data) +'</dl>';
        settings.elem.parent().parent().append($(html));
        settings.elem.parent().append( $hiddenInput );
    }
    //注册监听事件
    CorpBank.prototype.listenEvent = function(settings){
        var that = this;
        //注册键盘按下去事件
        settings.elem.off('keyup').on('keyup',function(e){
            var value = $.trim($(this).val());
            var $container =  $(this).parent().parent();
            var len = 0;
            var keyCode = e.keyCode;
            if( value !== ''){ //有值时
                //获取字符串长度（汉字算两个字符，字母数字算一个） 汉字两个或者三个数字时，才执行检索功能
                for (var i = 0; i < value.length; i++) {
                    var a = value.charAt(i);
                    if (a.match(/[^\x00-\xff]/ig) != null) {
                        len += 2;
                    }
                    else {
                        len += 1;
                    }
                }
            }

            if( keyCode === 32 || keyCode === 13 ){
                if( !$container.find('dl').is(':visible')){ //如果元素不可见，那么删除选中样式；
                    $container.find('.'+THIS).removeClass(THIS);
                }
                if( len > 0){
                    if( settings.search  && !$container.find('.'+THIS).length){
                        settings.search(value);
                        return false;
                    }
                }
                if( $container.find('.'+THIS).length ){
                    $container.find('.'+THIS).trigger('click');
                }
            }
            if( len < 3 ){ //当删除内容时输入框的字符长度小于3时，隐藏框
                $container.removeClass('layui-form-selected');
            }
            return true;
        })

        var index = 0;//下拉dd索引
        settings.elem.off('keydown').on('keydown',function(e){
            var keyCode = e.keyCode;
            if( keyCode === 32 || keyCode === 13 ){
                return false;
            }
            var dl = $(this).parent().siblings('dl');
            var dds = dl.find('dd');
            if(!dl[0]){ //dl被删除时，直接return
                return
            }
            var flag = dl[0].scrollHeight > dl[0].clientHeight;
            var ddHeight = dds.height();//行高
            // 检验到第几行时才滚动
            var stage = Math.floor(dl[0].clientHeight/ddHeight)-2;
            var selectedElem = dl.find('.'+THIS);
            index = dds.index(selectedElem);
            //Tab键
            if( keyCode === 9 ){
                $(this).parent().parent().removeClass("layui-form-selected");
            } else if (( keyCode === 38 || keyCode === 40 )){ //向上38，向下40，回车13 liuyj
                $(dds[index]).removeClass(THIS);//清除上一个选中样式
                index = indexUpdate(index,keyCode,dds);
                var othis = $(dds[index]);
                othis.addClass(THIS);//新的添加选中样式
                if( keyCode === 38 ){ //38向上时
                    if((dds.length - index) > stage){
                        if(flag){
                            var totop = dl.scrollTop() - ddHeight;
                            dl.scrollTop(totop)
                        }
                    }
                }else{ //40向下
                    if(index > stage){
                        if(flag){
                            var top = (index - stage)*ddHeight;
                            dl.scrollTop(top)
                        }
                    }
                }
                return false;
            }else{
                $(this).parent().parent().find('.'+THIS).removeClass(THIS);
            }
        })
        //注册点击事件
        settings.elem.parent().parent().find('dd').off('click').on('click',function(e){
            var data = {};
            data.elem = $(this).parent().siblings().find('input');
            var change = settings.change;
            var settingsData = settings.data;
            var index =  $(this).data('index');
            if( settingsData.length ){
                data.data = settingsData[index];
            }
            if( change ){
                change(data);
                data.elem.parent().attr('title',data.elem.val());
                data.elem.data('index',index);
            }
            $(this).parent('dl').hide()
        })
        //注册清空事件
        settings.elem.siblings('a.delIcon').off('click').on('click',function(){
            $(this).siblings('input').val('');
        })
    }

    var indexUpdate = function(index,keycode,dds){
        var maxIndex = dds.length -1 ;
        if(keycode === 40 || keycode === 38){
            if(index <= maxIndex ){
                if(index === maxIndex){
                    if(keycode === 40){
                        return index;
                    }else{
                        index--;//正常情况下的index 运算;
                    }
                }else{
                    keycode === 40 ? index++ : index--;//正常情况下的index 运算;
                }
                if( $(dds[index]).hasClass(DISABLED)){
                    if(index === maxIndex){
                        keycode === 40 ? index -- : index ++; //最后一个index 运算;
                    }
                    index = indexUpdate(index,keycode,dds)
                }
            }
            return index;
        }
    }

    //获取值
    CorpBank.prototype.val = function(value){
        var that = this;
        if(!that.currentObject){
            return;
        }
        var currentObj = that.currentObject;
        if( value !== undefined ){
            currentObj.elem.val(value);
            currentObj.elem.parent().attr('title',value);

        }else{
            var index = currentObj.elem.data('index');
            if( index !== undefined && currentObj.data.length ){
                return currentObj.data[index];
            }else{
                return [];
            }
        }
    }

    //获取值
    CorpBank.prototype.attr = function(obj){
        var that = this;
        if(!that.currentObject){
            return;
        }
        if( obj !== undefined ){
            for( var key in obj ){
                if( key == 'url' ){
                    that.currentObject.data = [];
                    that.currentObject.elem.parent().siblings('dl').remove();
                }
                that.currentObject.elem.data(key,obj[key]);
                that.currentObject[key] = obj[key];
            }
        }
        that.val('');
        that.render();
    }

    /*
    * 设置url
    * obj有两个参数url urldata
    * */

    CorpBank.prototype.setUrl = function(obj){
        var that = this;
        if(!that.currentObject){
            return;
        }
        if( obj === undefined || obj === ''){
            throwError('inputExternalCorpBankAcc Error:请配置远程数据URL!')
        }
        that.currentObject.elem.parent().siblings('dl').remove();
        that.currentObject.elem.removeAttr('data-index');
        that.currentObject.elem.data('url',obj.url);
        that.currentObject.url = obj.url;
        that.currentObject.urldata = obj.urldata;
        that.currentObject.reset = true;
        that.render();
    }

    var makeCallBack = function(callBack){
        if(callBack && typeof(callBack) != "function"){
            try {
                callBack = eval(callBack);
            } catch(e) {
                alert(callBack + 'not exist！');
            }
        }
        return callBack;
    }

    function throwError(msg) {
        throw new Error("[bt-biz-inputExternalCorpBankAcc:" + msg);
    };

    var corpBank = new CorpBank();
    var obj = {
        init:function(){
            corpBank.init();
            return this;
        },
        get:function(bizName){
            corpBank.currentObject = bt.inputExternalCorpBankAcc.config[bizName];
            if(!corpBank.currentObject ){
                throwError('inputExternalCorpBankAcc Error:data-biz-name属性!');
            }
            return corpBank
        }
    }
    //输出口
    exports('inputExternalCorpBankAcc', obj);
})

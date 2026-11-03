/**
 * 业务组件：省市联动
 * 周亮
 * 2017年6月29日
 * 0.0.1 初始化参数
 * 0.0.2 新增加callback 点击回调方法；initcallback 初始化完成回调方法
 * 0.0.3 新增data-disabled属性，设置不可选
 */
layui.define(['jquery','linkage'], function (exports) {
    var $ = layui.jquery,linkage = bt.linkage
    var Region = function () {
        this.config = {
            elem: undefined,
            name: undefined,
            defaultVal: undefined,
            valueField: undefined,
            textField: undefined,
            search: true,
            disabled: false,
        }
        this.bizName = "region";
        this.currentObject = null;
    }

    Region.prototype.v = '0.0.1';

    /**
     * 方法入口，初始化
     */
    Region.prototype.init = function () {
        var that = this;
        that.render();
    }

    Region.prototype.render = function () {
        var that = this;
        var arr = [];
        $('.bt-biz-region:not(.init)').each(function(){
            var jThis = $(this);
            var group = jThis.data('group');
            if(!arr[group]){
                arr[group] = [];
            }
            arr[group].push(jThis);
        });

        for(k in arr){

            var items = [];
            var jItem = arr[k];
            for(var i=0;i<jItem.length;i++){
                var item = jItem[i];
                var initCallback = item.data('initCallback'.toLowerCase());//表格初始完成回调方法
                var callback = item.data('callback'.toLowerCase());//表格初始完成回调方法
                if(initCallback){
                    initCallback = that.makeCallBack(initCallback);
                }
                items.push({
                    name:item.data('name'),
                    change:item.data('change'),
                    required:item.data('required'),
                    label:item.data('label'),
                    disabled:item.data('disabled'),
                    selected:item.data('defaultValue'.toLowerCase()),
                    group:k,
                    appendTo:item,
                    initCallback:initCallback,
                    callback:callback
                });
            }

            var linkAge = linkage.initLinkAge({
                url:basePath+"/sys/region/json?id=",
                items:items
            });

            var bizName = items[0].group;
            if(bizName){
                if(!window.biz){
                    window.biz = [];
                }
                if(!window.biz[that.bizName]){
                    window.biz[that.bizName] = [];
                }
                window.biz[that.bizName][bizName] =  linkAge;
            }
        }
    }

    function throwError(msg) {
        throw new Error("[bt-biz-region]:" + msg);
    };


    Region.prototype.makeCallBack = function (callBack) {
        //校验回调方法是否存在
        if (callBack && typeof(callBack) != "function") {
            try {
                callBack = eval(callBack);
            } catch (e) {
                alert(callBack + 'not exist！');
            }

        }
        return callBack;
    }

    // Region.prototype.val = function(bizName){
    //     var that = this;
    //     if(!bizName){
    //         if(!that.currentObject){
    //             return "";
    //         }
    //         return that.currentObject.find("option:selected").val();
    //     }else{
    //         that.currentObject.find("select").val(bizName);
    //     }
    //
    // }

    Region.prototype.set = function(object){
        var that = this;
        that.currentObject.set(object);
    }

    // Region.prototype.set = function(object,callback){
    //     if(callback){
    //         callback = makeCallBack(callback);
    //     }else{
    //         throwError("未配置回调方法")
    //     }
    //     var that = this;
    //     that.currentObject.set(object,callback);
    // }

    var region = new Region();
    var obj = {
        init: function () {
            region.init();
            return this;
        },
        render:function(){
            region.render();
        },
        get:function(bizName){
            var currentObj = window.biz[region.bizName][bizName];
            if(!currentObj){
                throwError(bizName+"尚未配置data-group属性！")
            }
            region.currentObject = currentObj;
            return region;
        }
    };
    //输出test接口
    exports('region', obj);
});
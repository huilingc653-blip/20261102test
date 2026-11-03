/**
 * 业务组件：银行类型
 * 周亮
 * 2017年6月27日
 * 0.0.1 初始化参数
 */
layui.define(['jquery','select'], function (exports) {
    var $ = layui.jquery;
    var BankType = function () {
        this.config = {
            elem: undefined,
            name: undefined,
            defaultVal: undefined,
            valueField: undefined,
            textField: undefined,
            search: true,
            disabled: false,
            url: basePath + '/basic/business/btbanktype/json'
        }
        this.bizName = "bankType";
        this.currentObject = null;
    }

    BankType.prototype.v = '0.0.1';

    /**
     * 方法入口，初始化
     */
    BankType.prototype.init = function () {
        var that = this;
        that.render();
    }

    BankType.prototype.render = function () {
        var that = this;
        $('.bt-biz-bankType:not(.init)').each(function(){
            var jThis = $(this);
            var select = bt.select.init({
                elem: jThis,
                name: jThis.data("name"),
                defaultVal: jThis.data("defaultVal".toLowerCase()),
                valueField: 'id',
                textField: 'typeName',
                search: true,
                disabled: false,
                url: jThis.data("url") ? jThis.data("url") : that.config.url,
                initCallBack:jThis.data("initCallback".toLowerCase())

            });

            var bizName = jThis.data('biz-name'.toLowerCase());
            if(bizName){
                if(!window.biz){
                    window.biz = [];
                }
                if(!window.biz[that.bizName]){
                    window.biz[that.bizName] = [];
                }
                window.biz[that.bizName][bizName] =  jThis;
            }
            jThis.addClass('init');
        });
    }

    function throwError(msg) {
        throw new Error("[bt-biz-bankType]:" + msg);
    };
    BankType.prototype.setInputBankTypeCallBack = function () {
        top.setInputBankTypeCallBack = function (data) {
            top.bankType.config.currentCallBack(data,obj.config.currentDom);
            top.layer.close(obj.config.currentIndex);
        }
    }

    BankType.prototype.makeCallBack = function (callBack) {
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

    BankType.prototype.val = function(bizName){
        var that = this;
        if(!bizName){
            if(!that.currentObject){
                return "";
            }
            return that.currentObject.find("option:selected").val();
        }else{
            that.currentObject.find("select").val(bizName);
        }

    }

    var bankType = new BankType();
    var obj = {
        init: function () {
            bankType.init();
            return this;
        },
        render:function(){
            bankType.render();
        },
        get:function(bizName){
            var currentObj = window.biz[bankType.bizName][bizName];
            if(!currentObj){
                throwError(bizName+"尚未配置data-biz-name属性！")
            }
            bankType.currentObject = currentObj;
            return bankType;
        }
    };
    //输出test接口
    exports('bankType', obj);
});
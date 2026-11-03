/**
 * 业务组件：内部理财信息
 * 黄茜
 * 2019-11-6 11:38:32
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery;
    var ChoInnerFinance = function () {
        this.config = {
            name:'choInnerFinance'
        };
        this.selectCallback = null;
    }

    ChoInnerFinance.prototype.v = '0.0.1';
    var choInnerFinance = new ChoInnerFinance();
    var obj = {
        init: function () {
            choInnerFinance.selectCallback = bt.selectCallback.init({
                name:choInnerFinance.config.name,
                url:basePath + "/chowmatistic/choinnerfinance/select/",
                width:900,
            });
            return this;
        },
        set:function(obj){
            choInnerFinance.selectCallback.set(choInnerFinance.config.name,obj);
        }
    };

    exports('choInnerFinance', obj);
});
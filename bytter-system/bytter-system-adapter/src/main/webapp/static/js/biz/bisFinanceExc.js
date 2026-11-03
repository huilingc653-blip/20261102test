/**
 * 业务组件：理财指令
 * 黄茜
 * 2017-8-26 11:38:32
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery;
    var BisFinanceExc = function () {
        this.config = {
            name:'bisFinanceExc'
        };
        this.selectCallback = null;
    }

    BisFinanceExc.prototype.v = '0.0.1';
    var bisFinanceExc = new BisFinanceExc();
    var obj = {
        init: function () {
        	bisFinanceExc.selectCallback = bt.selectCallback.init({
                name:bisFinanceExc.config.name,
                url:basePath + "/bdlink/bisfinanceexc/select/",
                width:900,
            });
            return this;
        },
        set:function(obj){
        	bisFinanceExc.selectCallback.set(bisFinanceExc.config.name,obj);
        }
    };

    exports('bisFinanceExc', obj);
});
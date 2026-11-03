/**
 * 业务组件：外汇产品查看
 * huangx
 * 2020年3月5日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var BankAccView = function () {
        this.config = {
            name:'bankAccView'
        };
        this.selectCallback = null;
    }
    BankAccView.prototype.v = '0.0.1';
    var btBankAccView = new BankAccView();
    var obj = {
        init: function () {
            btBankAccView.selectCallback = selectCallback.init({
                name:btBankAccView.config.name,
                maxmin:true,
                url:basePath + "/bankacc/btbankacc/selectBankAccView",
            });
            return this;
        },
        set:function(obj){
            if(!btBankAccView.selectCallback){
                console.error("bankAccView组件未初始化")
            }
            btBankAccView.selectCallback.set(btBankAccView.config.name,obj);
        }
    };
    exports('bankAccView', obj);
});
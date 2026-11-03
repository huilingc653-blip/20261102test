/**
 * 业务组件：银行账号
 * huangx
 * 2020年3月5日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var BankAccInfo = function () {
        this.config = {
            name:'bankAccInfo'
        };
        this.selectCallback = null;
    }
    BankAccInfo.prototype.v = '0.0.1';
    var btBankAccInfo = new BankAccInfo();
    var obj = {
        init: function () {
            btBankAccInfo.selectCallback = selectCallback.init({
                name:btBankAccInfo.config.name,maxmin:true,
                url:basePath + "/bankacc/btbankacc/selectBankAcc",
            });
            return this;
        },
        set:function(obj){
            if(!btBankAccInfo.selectCallback){
                console.error("bankAccInfo组件未初始化")
            }
            btBankAccInfo.selectCallback.set(btBankAccInfo.config.name,obj);
        }
    };
    exports('bankAccInfo', obj);
});
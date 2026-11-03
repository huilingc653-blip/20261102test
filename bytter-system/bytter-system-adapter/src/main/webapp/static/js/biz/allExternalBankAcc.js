/**
 * 业务组件：客商交易对手关联账户组件
 * @author tongqr
 * 2025年05月20日
 * 传参 innerBankAcc,outerBankAcc,corpBankAcc,fps,payPal
 * 回调 type:innerBankAcc,outerBankAcc,corpBankAcc,fps,payPal
 * 0.0.1
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var allExternalBankAcc= function () {
        this.config = {
            name:'allExternalBankAcc'
        }
        this.selectCallback = null;
        this.bizName="allExternalBankAcc";
    }
    allExternalBankAcc.prototype.v = '0.0.1';
    var allExternalBankAcc= new allExternalBankAcc();
    var obj = {
        init: function () {
            allExternalBankAcc.selectCallback = selectCallback.init({
                name:allExternalBankAcc.config.name,
                url:basePath + "/sys/externalCorp/selectallExternalBankAccTab",
                width:1100,
                hight:1000,
            });
            return this;
        },
        set:function(obj){
            if(!allExternalBankAcc.selectCallback){
                console.error("allExternalBankAcc组件未初始化")
            }
            allExternalBankAcc.selectCallback.set(allExternalBankAcc.config.name,obj);
        }
    };
    //输出test接口
    exports('allExternalBankAcc', obj);
});


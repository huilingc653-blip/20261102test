/**
 * 业务组件：单位持有应付票据信息
 * fangcf
 * 2023年4月12日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback1'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback1;
    var BillInfo = function () {
        this.config = {
            name:'accebillInfoPay'
        };
        this.selectCallback = null;
    }
    BillInfo.prototype.v = '0.0.1';
    var accebillInfoPay = new BillInfo();
    var obj = {
        init: function () {
            accebillInfoPay.selectCallback = selectCallback.init({
                name:accebillInfoPay.config.name,maxmin:true,
                url:basePath + "/bill/accebill/abvoucherinfoVice/accebillInfoPayPage",
                sendParamsToChildFrame:'getQueryPayParams'
            });
            return this;
        },
        set:function(obj){
            if(!accebillInfoPay.selectCallback){
                console.error("accebillInfoPay组件未初始化")
            }
            accebillInfoPay.selectCallback.set(accebillInfoPay.config.name,obj);
        }
    };
    exports('accebillInfoPay', obj);
});
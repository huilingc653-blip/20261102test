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
            name:'accebillInfoReceive'
        };
        this.selectCallback = null;
    }
    BillInfo.prototype.v = '0.0.1';
    var accebillInfoReceive = new BillInfo();
    var obj = {
        init: function () {
            accebillInfoReceive.selectCallback = selectCallback.init({
                name:accebillInfoReceive.config.name,maxmin:true,
                url:basePath + "/bill/accebill/abvoucherinfoVice/accebillInfoReceivePage",
                sendParamsToChildFrame:'getQueryReceiveParams'
            });
            return this;
        },
        set:function(obj){
            if(!accebillInfoReceive.selectCallback){
                console.error("accebillInfoReceive组件未初始化")
            }
            accebillInfoReceive.selectCallback.set(accebillInfoReceive.config.name,obj);
        }
    };
    exports('accebillInfoReceive', obj);
});
/**
 * 业务组件：单位持有票据信息 
 * 郑刚
 * 2017年7月20日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var BillInfo = function () {
        this.config = {
            name:'accebillInfo'
        };
        this.selectCallback = null;
    }
    BillInfo.prototype.v = '0.0.1';
    var accebillInfo = new BillInfo();
    var obj = {
        init: function () {
            accebillInfo.selectCallback = selectCallback.init({
                name:accebillInfo.config.name,maxmin:true,
                url:basePath + "/bill/accebill/abvoucherinfoVice/accebillInfoPage",
                sendParamsToChildFrame:'getQueryParams'
            });
            return this;
        },
        set:function(obj){
            if(!accebillInfo.selectCallback){
                console.error("accebillInfo组件未初始化")
            }
            accebillInfo.selectCallback.set(accebillInfo.config.name,obj);
        }
    };
    exports('accebillInfo', obj);
});
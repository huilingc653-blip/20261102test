/**
 * 业务组件：智能选票
 * 郑鸿杰
 * 2022年10月11日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var BillInfo = function () {
        this.config = {
            name:'accebillIntellect'
        };
        this.selectCallback = null;
    }
    BillInfo.prototype.v = '0.0.1';
    var accebillIntellect = new BillInfo();
    var obj = {
        init: function () {
            accebillIntellect.selectCallback = selectCallback.init({
                name:accebillIntellect.config.name,maxmin:true,
                url:basePath + "/bill/accebill/intellect/accebillIntellectPage",
                sendParamsToChildFrame:'getQueryParams'
            });
            return this;
        },
        set:function(obj){
            if(!accebillIntellect.selectCallback){
                console.error("accebillIntellect组件未初始化")
            }
            accebillIntellect.selectCallback.set(accebillIntellect.config.name,obj);
        }
    };
    exports('accebillIntellect', obj);
});
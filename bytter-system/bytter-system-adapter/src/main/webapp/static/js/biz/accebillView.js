/**
 * 业务组件：根据票据ID查看票据信息 
 * 郑刚
 * 2017年7月20日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var BillView = function () {
        this.config = {
            name:'accebillView'
        };
        this.selectCallback = null;
    }
    BillView.prototype.v = '0.0.1';
    var accebillView = new BillView();
    var obj = {
        init: function () {
            accebillView.selectCallback = selectCallback.init({
                name:accebillView.config.name,
                title:'查看',
                maxmin:true,
                url:basePath + "/bill/accebill/abvoucherinfoVice/accebillViewPage",
            });
            return this;
        },
        set:function(obj){
            if(!accebillView.selectCallback){
                console.error("accebillView组件未初始化")
            }
            accebillView.selectCallback.set(accebillView.config.name,obj);
        }
    };
    exports('accebillView', obj);
});
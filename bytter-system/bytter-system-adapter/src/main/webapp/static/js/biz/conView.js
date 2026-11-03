/**
 * 业务组件：根据票据ID查看票据信息 
 * 郑刚
 * 2017年7月20日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var ConView = function () {
        this.config = {
            name:'conView'
        };
        this.selectCallback = null;
    }
    ConView.prototype.v = '0.0.1';
    var conView = new ConView();
    var obj = {
        init: function () {
            conView.selectCallback = selectCallback.init({
                name:conView.config.name,
                title:true,
                maxmin:true,
                url:basePath + "/contract/conCommon/conView",
            });
            return this;
        },
        set:function(obj){
            if(!conView.selectCallback){
                console.error("conView组件未初始化")
            }
            conView.selectCallback.set(conView.config.name,obj);
        }
    };
    exports('conView', obj);
});
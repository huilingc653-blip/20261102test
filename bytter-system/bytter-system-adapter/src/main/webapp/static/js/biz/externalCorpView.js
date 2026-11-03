/**
 * 业务组件：外汇产品查看
 * huangx
 * 2020年3月5日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var ExternalCorpView = function () {
        this.config = {
            name:'externalCorpView'
        };
        this.selectCallback = null;
    }
    ExternalCorpView.prototype.v = '0.0.1';
    var sysExternalCorpView = new ExternalCorpView();
    var obj = {
        init: function () {
            sysExternalCorpView.selectCallback = selectCallback.init({
                name:sysExternalCorpView.config.name,
                maxmin:true,
                url:basePath + "/sys/externalCorp/selectExternalCorpView",
            });
            return this;
        },
        set:function(obj){
            if(!sysExternalCorpView.selectCallback){
                console.error("externalCorpView组件未初始化")
            }
            sysExternalCorpView.selectCallback.set(sysExternalCorpView.config.name,obj);
        }
    };
    exports('externalCorpView', obj);
});
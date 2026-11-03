/**
 * 业务组件：银行账号
 * huangx
 * 2020年3月5日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var ExternalCorpInfo = function () {
        this.config = {
            name:'externalCorpInfo'
        };
        this.selectCallback = null;
    }
    ExternalCorpInfo.prototype.v = '0.0.1';
    var sysExternalCorpInfo = new ExternalCorpInfo();
    var obj = {
        init: function () {
            sysExternalCorpInfo.selectCallback = selectCallback.init({
                name:sysExternalCorpInfo.config.name,maxmin:true,
                url:basePath + "/sys/externalCorp/selectExternalCorp",
                width:1000,
                height:750
            });
            return this;
        },
        set:function(obj){
            if(!sysExternalCorpInfo.selectCallback){
                console.error("externalCorpInfo组件未初始化")
            }
            sysExternalCorpInfo.selectCallback.set(sysExternalCorpInfo.config.name,obj);
        }
    };
    exports('externalCorpInfo', obj);
});
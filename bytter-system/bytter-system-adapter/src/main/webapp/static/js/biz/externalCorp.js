/**
 * 业务组件：单位客商选择组件
 * zhengg
 * 2020年7月10日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var ExternalCorp = function () {
        this.config = {
            name:'externalCorp'
        };
        this.selectCallback = null;
    }
    ExternalCorp.prototype.v = '0.0.1';
    var externalCorp = new ExternalCorp();
    var obj = {
        init: function (ops) {
            externalCorp.selectCallback = selectCallback.init({
                name:externalCorp.config.name,
                url:basePath + "/sys/externalCorp/selectExternalCorpTab",
                width:1000,
                height:662,
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
                sendParamsToChildFrame:'getQueryParams',
                isSpliceParamsOnUrl:false
            });
            return this;
        },
        set:function(obj){
            if(!externalCorp.selectCallback){
                console.error("externalCorp组件未初始化")
            }
            externalCorp.selectCallback.set(externalCorp.config.name,obj);
        }
    };
    exports('externalCorp', obj);
});

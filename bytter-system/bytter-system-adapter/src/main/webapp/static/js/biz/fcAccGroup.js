/**
 * 业务组件:账户组维护
 * zhoup
 * 2024年5月6日
 * 0.0.1 新增必填参数
 * 0.0.2 新增加multiLayer参数，支持弹框中弹框功能
 */
layui.define(['selectCallback'], function (exports) {
    var fcAccGroup = function () {
        this.config = {
            name:'fcAccGroup'
        }
        this.selectCallback = null;
    }

    fcAccGroup.prototype.v = '0.0.1';
    var fcAccGroup = new fcAccGroup();
    var obj = {
        init: function (ops) {
            fcAccGroup.selectCallback = bt.selectCallback.init({
                width: 1100,
                height: 700,
                name:fcAccGroup.config.name,
                url:basePath + "/netbank/bankAccGroup/viewList",
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            fcAccGroup.selectCallback.set(fcAccGroup.config.name,obj);
        }
    };
    //输出test接口
    exports('fcAccGroup', obj);
});
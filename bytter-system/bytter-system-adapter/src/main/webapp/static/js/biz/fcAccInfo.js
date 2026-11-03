/**
 * 业务组件：内部账号信息
 * 王智星
 * 2020年6月22日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var AccInfo = function () {
        this.config = {
            name:'fcAccInfo'
        };
        this.selectCallback = null;
    }
    AccInfo.prototype.v = '0.0.1';
    var fcAccInfo = new AccInfo();
    var obj = {
        init: function () {
            fcAccInfo.selectCallback = selectCallback.init({
                name:fcAccInfo.config.name,
                url:basePath + "/inneraccount/fcacc/fcAccInfoPage",

            });
            return this;
        },
        set:function(obj){
            if(!fcAccInfo.selectCallback){
                console.error("fcAccInfo组件未初始化")
            }
            fcAccInfo.selectCallback.set(fcAccInfo.config.name,obj);
        }
    };
    exports('fcAccInfo', obj);
});
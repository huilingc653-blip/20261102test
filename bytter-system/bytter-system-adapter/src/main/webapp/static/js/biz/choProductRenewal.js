/**
 * 业务组件：理财赎回
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery;
    var ChoProductRenewal = function () {
        this.config = {
            name:'choProductRenewal'
        };
        this.selectCallback = null;
    }

    ChoProductRenewal.prototype.v = '0.0.1';
    var choProductRenewal = new ChoProductRenewal();
    var obj = {
        init: function () {
            choProductRenewal.selectCallback = bt.selectCallback.init({
                name:choProductRenewal.config.name,
                url:basePath + "/chowmatistic/choproductRenewal/select",
                width:900,
            });
            return this;
        },
        set:function(obj){
            choProductRenewal.selectCallback.set(choProductRenewal.config.name,obj);
        }
    };

    exports('choProductRenewal', obj);
});
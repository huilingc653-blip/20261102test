/**
 * 业务组件：理财产品
 * linqp
 * 2024年03月05日
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery;
    var ChoProductType = function () {
        this.config = {
            name:'choProductType'
        };
        this.selectCallback = null;
    }

    ChoProductType.prototype.v = '0.0.1';
    var choProductType = new ChoProductType();
    var obj = {
        init: function () {
            choProductType.selectCallback = bt.selectCallback.init({
                name:choProductType.config.name,
                url:basePath + "/chowmatistic/choproduct/list2",
                width:1000,
            });
            return this;
        },
        set:function(obj){
            choProductType.selectCallback.set(choProductType.config.name,obj);
        }
    };

    exports('choProductType', obj);
});
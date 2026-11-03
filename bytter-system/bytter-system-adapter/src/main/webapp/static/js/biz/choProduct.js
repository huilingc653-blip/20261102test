/**
 * 业务组件：理财产品
 * 沈聪
 * 2018-09-07
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery;
    var ChoProduct = function () {
        this.config = {
            name:'choProduct'
        };
        this.selectCallback = null;
    }

    ChoProduct.prototype.v = '0.0.1';
    var choProduct = new ChoProduct();
    var obj = {
        init: function () {
            choProduct.selectCallback = bt.selectCallback.init({
                name:choProduct.config.name,
                url:basePath + "/chowmatistic/choproductreq/select",
                width:1000,
            });
            return this;
        },
        set:function(obj){
            choProduct.selectCallback.set(choProduct.config.name,obj);
        }
    };

    exports('choProduct', obj);
});
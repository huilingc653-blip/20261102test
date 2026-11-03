/**
 * 业务组件：外汇产品
 * huangx
 * 2020年3月5日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var ProductInfo = function () {
        this.config = {
            name:'fexProductInfo'
        };
        this.selectCallback = null;
    }
    ProductInfo.prototype.v = '0.0.1';
    var acceProductInfo = new ProductInfo();
    var obj = {
        init: function () {
            acceProductInfo.selectCallback = selectCallback.init({
                name:acceProductInfo.config.name,maxmin:true,
                url:basePath + "/foreign/fexproduct/selectFexProduct",
            });
            return this;
        },
        set:function(obj){
            if(!acceProductInfo.selectCallback){
                console.error("fexProductInfo组件未初始化")
            }
            acceProductInfo.selectCallback.set(acceProductInfo.config.name,obj);
        }
    };
    exports('fexProductInfo', obj);
});
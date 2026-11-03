/**
 * 业务组件：外汇产品查看
 * huangx
 * 2020年3月5日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var ProductView = function () {
        this.config = {
            name:'fexProductView'
        };
        this.selectCallback = null;
    }
    ProductView.prototype.v = '0.0.1';
    var fexProductView = new ProductView();
    var obj = {
        init: function () {
            fexProductView.selectCallback = selectCallback.init({
                name:fexProductView.config.name,
                title: bt.lang('common.button.view'),
                maxmin:true,
                url:basePath + "/foreign/fexproduct/selectFexProductView",
            });
            return this;
        },
        set:function(obj){
            if(!fexProductView.selectCallback){
                console.error("fexProductView组件未初始化")
            }
            fexProductView.selectCallback.set(fexProductView.config.name,obj);
        }
    };
    exports('fexProductView', obj);
});
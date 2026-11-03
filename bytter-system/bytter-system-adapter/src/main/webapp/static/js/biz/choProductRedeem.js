/**
 * 业务组件：国家
 * 秦雷
 * 2019年07月16日
 */
layui.define(['selectCallback'], function (exports) {
    var ChoProductRedeem = function () {
        this.config = {
            name:'choProductRedeem'
        }
        this.selectCallback = null;
    }

    ChoProductRedeem.prototype.v = '0.0.1';
    var choProductRedeem = new ChoProductRedeem();
    var obj = {
        init: function () {
            choProductRedeem.selectCallback = bt.selectCallback.init({
                name:choProductRedeem.config.name,
                url:basePath + "/chowmatistic/choproductredeem/select",
            });
            return this;
        },
        set:function(obj){
            choProductRedeem.selectCallback.set(choProductRedeem.config.name,obj);
        }
    };
    //输出test接口
    exports('choProductRedeem', obj);
});
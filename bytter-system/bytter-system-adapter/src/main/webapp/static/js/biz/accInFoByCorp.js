/**
 * 业务组件：根据单位取内部帐号
 * 王智星
 * 2017年7月5日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var PayeeAcc = function () {
        this.config = {
            name:'accInFoByCorp'
        };
        this.selectCallback = null;
    }
    PayeeAcc.prototype.v = '0.0.1';
    var accInFoByCorp = new PayeeAcc();
    var obj = {
        init: function () {
        	accInFoByCorp.selectCallback = selectCallback.init({
                name:accInFoByCorp.config.name,
                url:basePath + "/netbank/cgsbill/payeeAccPage",
                width:980,
            });
            return this;
        },
        set:function(obj){
            if(!accInFoByCorp.selectCallback){
                console.error("accInFoByCorp组件未初始化")
            }
            accInFoByCorp.selectCallback.set(accInFoByCorp.config.name,obj);
        }
    };
    //输出test接口
    exports('accInFoByCorp', obj);
});
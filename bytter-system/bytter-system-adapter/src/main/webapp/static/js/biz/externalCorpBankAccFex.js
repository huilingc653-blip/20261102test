/**
 * 业务组件：国外客商帐号
 * 王智星
 * 2019年7月17日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var BankAcc = function () {
        this.config = {
            name:'externalCorpBankAccFex'
        };
        this.selectCallback = null;
    }
    BankAcc.prototype.v = '0.0.1';
    var externalCorpBankAccFex = new BankAcc();
    var obj = {
        init: function (ops) {
            externalCorpBankAccFex.selectCallback = selectCallback.init({
                name:externalCorpBankAccFex.config.name,
                url:basePath + "/sys/externalCorp/selectExternalCorpFexPage",
                width:980,
                hight:900,
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            if(!externalCorpBankAccFex.selectCallback){
                console.error("externalCorpBankAccFex组件未初始化")
            }
            externalCorpBankAccFex.selectCallback.set(externalCorpBankAccFex.config.name,obj);
        }
    };
    //输出test接口
    exports('externalCorpBankAccFex', obj);
});
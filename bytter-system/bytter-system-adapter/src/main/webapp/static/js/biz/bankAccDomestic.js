/**
 * 业务组件：银行账户-境外
 * @author zhengxn
 * 2020/06/19
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var BankAccDomestic = function () {
        this.config = {
            name:'bankAccDomestic'
        }
        this.selectCallback = null;
        //this.bizName="bankAccDomestic";
    }

    BankAccDomestic.prototype.v = '0.0.1';
    var bankAccDomestic = new BankAccDomestic();
    var obj = {
        init: function (ops) {
            bankAccDomestic.selectCallback = bt.selectCallback.init({
                name:bankAccDomestic.config.name,
                url:basePath +"/bankacc/btbankacchandle/listByType?type=1",
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            bankAccDomestic.selectCallback.set(bankAccDomestic.config.name,obj);
        }
    };
    //输出test接口
    exports('bankAccDomestic', obj);
});
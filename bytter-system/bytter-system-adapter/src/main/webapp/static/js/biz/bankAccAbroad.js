/**
 * 业务组件：银行账户-境内
 * @author zhengxn
 * 2020/06/19
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
    var BankAccAbroad = function () {
        this.config = {
            name:'bankAccAbroad'
        }
        this.selectCallback = null;
        //this.bizName="bankAccAbroad";
    }

    BankAccAbroad.prototype.v = '0.0.1';
    var bankAccAbroad = new BankAccAbroad();
    var obj = {
        init: function (ops) {
            bankAccAbroad.selectCallback = bt.selectCallback.init({
                name:bankAccAbroad.config.name,
                url:basePath +  "/bankacc/btbankacchandle/listByType?type=0",
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            bankAccAbroad.selectCallback.set(bankAccAbroad.config.name,obj);
        }
    };
    //输出test接口
    exports('bankAccAbroad', obj);
});
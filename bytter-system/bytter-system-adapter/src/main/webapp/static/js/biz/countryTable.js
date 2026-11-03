/**
 * 业务组件：国家
 * 秦雷
 * 2019年07月15日
 */
layui.define(['selectCallback'], function (exports) {
    var BankInfo = function () {
        this.config = {
            name:'bankInfo'
        }
        this.selectCallback = null;
    }

    BankInfo.prototype.v = '0.0.1';
    var bankInfo = new BankInfo();
    var obj = {
        init: function () {
            bankInfo.selectCallback = bt.selectCallback.init({
                name:bankInfo.config.name,
                url:basePath + "/basic/business/syscountry/select",
            });
            return this;
        },
        set:function(obj){
            bankInfo.selectCallback.set(bankInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('countryTable', obj);
});
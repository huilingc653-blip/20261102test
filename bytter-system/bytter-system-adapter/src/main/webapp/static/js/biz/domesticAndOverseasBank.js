/**
 * 业务组件：境内银行和境外银行
 * zuoy
 * 2020-04-13
 * 0.0.1 新增组件
 */
layui.define(['selectCallback'], function (exports) {
    var DomesticAndOverseasBank = function () {
        this.config = {
            name: 'domesticAndOverseasBank'
        }
        this.selectCallback = null;
    }

    DomesticAndOverseasBank.prototype.v = '0.0.1';
    var domesticAndOverseasBank = new DomesticAndOverseasBank();
    var obj = {
        init: function () {
            domesticAndOverseasBank.selectCallback = bt.selectCallback.init({
                name: domesticAndOverseasBank.config.name,
                url: basePath + "/basic/business/btinputbankinfo/selectInputBankAndSwiftBankInfo",
                width: 980,
                // 下面为t2优化代码
                //width:1058,
                hight: 900
            });
            return this;
        },
        set: function (obj) {
            domesticAndOverseasBank.selectCallback.set(domesticAndOverseasBank.config.name, obj);
        }
    };
    //输出test接口
    exports('domesticAndOverseasBank', obj);
});
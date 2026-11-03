/**
 * 业务组件：联行号
 * 周亮
 * 2017年6月22日
 * 0.0.1 新增必填参数
 * 0.0.2 新增加multiLayer参数，支持弹框中弹框功能
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
        init: function (ops) {
            bankInfo.selectCallback = bt.selectCallback.init({
                name:bankInfo.config.name,
                url:basePath + "/basic/business/btinputbankinfo/select",
                height:637,
                width:950,
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            bankInfo.selectCallback.set(bankInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('bankInfo', obj);
});

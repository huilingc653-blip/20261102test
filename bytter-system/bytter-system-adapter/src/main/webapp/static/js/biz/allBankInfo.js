/**
 * 业务组件：境内境外联行号
 * @author tongqr
 * 2025年05月20日
 * 传参 页面回调方法 type : innerBank,outerBank
 * 0.0.1
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var allBankInfo= function () {
        this.config = {
            name:'allBankInfo'
        }
        this.selectCallback = null;
        this.bizName="allBankInfo";
    }
    allBankInfo.prototype.v = '0.0.1';
    var allBankInfo= new allBankInfo();
    var obj = {
        init: function () {
            allBankInfo.selectCallback = selectCallback.init({
                name:allBankInfo.config.name,
                url: basePath + "/basic/business/btinputbankinfo/selectAllBankInfo",
                width:1100,
                hight:1000,
            });
            return this;
        },
        set:function(obj){
            allBankInfo.selectCallback.set(allBankInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('allBankInfo', obj);
});


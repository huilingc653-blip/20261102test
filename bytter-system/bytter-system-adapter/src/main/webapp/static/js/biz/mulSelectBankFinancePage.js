/**
 * 业务组件：理财多选组件
 * 秦雷
 * 2020年5月18日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var UserSelect = function () {
        this.config = {
            name:'mulSelectBankFinancePage'
        }
        this.selectCallback = null;
    }

    UserSelect.prototype.v = '0.0.1';
    var mulSelectBankFinancePage = new UserSelect();
    var obj = {
        init: function () {
            mulSelectBankFinancePage.selectCallback = bt.selectCallback.init({
                name:mulSelectBankFinancePage.config.name,
                url:basePath + "/chowmatistic/chobankfinance/mulSelectBankFinancePage",
            });
            return this;
        },
        set:function(obj){
            mulSelectBankFinancePage.selectCallback.set(mulSelectBankFinancePage.config.name,obj);
        }
    };
    //输出test接口
    exports('mulSelectBankFinancePage', obj);
});
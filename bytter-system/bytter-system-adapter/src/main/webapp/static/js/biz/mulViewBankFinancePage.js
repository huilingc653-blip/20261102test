/**
 * 业务组件：查询理财已选择的数据
 * 秦雷
 * 2020年5月12日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var UserSelect = function () {
        this.config = {
            name:'mulViewBankFinancePage'
        };
        this.selectCallback = null;
    }
    UserSelect.prototype.v = '0.0.1';
    var mulViewBankFinancePage = new UserSelect();
    var obj = {
        init: function () {
            mulViewBankFinancePage.selectCallback = selectCallback.init({
                name:mulViewBankFinancePage.config.name,
                title:true,
                maxmin:true,
                url:basePath + "/chowmatistic/chobankfinance/mulViewBankFinancePage",
            });
            return this;
        },
        set:function(obj){
            if(!mulViewBankFinancePage.selectCallback){
                console.error("mulViewBankFinancePage组件未初始化")
            }
            mulViewBankFinancePage.selectCallback.set(mulViewBankFinancePage.config.name,obj);
        }
    };
    exports('mulViewBankFinancePage', obj);
});
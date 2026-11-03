/**
 * 资金计划科目
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var FbsItem = function () {
        this.config = {
            name:'budgetFbsItem'
        };
        this.selectCallback = null;
    }
    FbsItem.prototype.v = '0.0.1';
    var budgetFbsItem = new FbsItem();
    var obj = {
        init: function (ops) {
            budgetFbsItem.selectCallback = selectCallback.init({
                title:ops.title,
                name:budgetFbsItem.config.name,
                url:basePath + "/budget/fbsitem/selectBudgetFbsItem",
                width:1058,
                // 下面为t2优化代码
                //width:1058,
                hight:900,
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            if(!budgetFbsItem.selectCallback){
                console.error("budgetFbsItem组件未初始化")
            }
            budgetFbsItem.selectCallback.set(budgetFbsItem.config.name,obj);
        }
    };
    //输出test接口
    exports('budgetFbsItem', obj);
});

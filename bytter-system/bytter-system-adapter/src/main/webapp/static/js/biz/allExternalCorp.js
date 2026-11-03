/**
 * 业务组件：客商交易对手组件
 * @author tongqr
 * 2025年05月20日
 * 传参 external 单独客商界面
 * 页面回调 type：external   corp
 * 0.0.1
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var allExternalCorp= function () {
        this.config = {
            name:'allExternalCorp'
        }
        this.selectCallback = null;
        this.bizName="allExternalCorp";
    }
    allExternalCorp.prototype.v = '0.0.1';
    var allExternalCorp= new allExternalCorp();
    var obj = {
        init: function () {
            allExternalCorp.selectCallback = selectCallback.init({
                name:allExternalCorp.config.name,
                url:basePath + "/sys/externalCorp/selectAllExternalCorpTab",
                width:1100,
                hight:1000,
            });
            return this;
        },
        set:function(obj){
            if(!allExternalCorp.selectCallback){
                console.error("allExternalCorp组件未初始化")
            }
            allExternalCorp.selectCallback.set(allExternalCorp.config.name,obj);
        }
    };
    //输出test接口
    exports('allExternalCorp', obj);
});


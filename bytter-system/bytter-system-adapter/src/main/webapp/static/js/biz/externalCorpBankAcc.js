/**
 * 业务组件：客商帐号
 * 周亮
 * 2017年6月30日
 * 0.0.1 组件初始化
 * 0.0.2 新增加multiLayer参数，支持弹框中弹框功能
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var BankAcc = function () {
        this.config = {
            name:'externalCorpBankAcc'
        };
        this.selectCallback = null;
    }
    BankAcc.prototype.v = '0.0.1';
    var externalCorpBankAcc = new BankAcc();
    var obj = {
        init: function (ops) {
            externalCorpBankAcc.selectCallback = selectCallback.init({
                name:externalCorpBankAcc.config.name,
                url:basePath + "/sys/externalCorp/selectExternalCorpPage",
                width:980,
                // 下面为t2优化代码
                //width:1058,
                height:672,
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            if(!externalCorpBankAcc.selectCallback){
                console.error("externalCorpBankAcc组件未初始化")
            }
            externalCorpBankAcc.selectCallback.set(externalCorpBankAcc.config.name,obj);
        }
    };
    //输出test接口
    exports('externalCorpBankAcc', obj);
});

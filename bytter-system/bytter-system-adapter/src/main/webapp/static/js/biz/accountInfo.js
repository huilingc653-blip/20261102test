/**
 * 业务组件：内部账户
 * 周亮
 * 2022年6月28日
 * 0.0.1 组件初始化
 * 0.0.2 新增加multiLayer参数，支持弹框中弹框功能
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var FcAcc = function () {
        this.config = {
            name:'accountInfo'
        };
        this.selectCallback = null;
    }
    FcAcc.prototype.v = '0.0.1';
    var accountInfo = new FcAcc();
    var obj = {
        init: function (ops) {
            accountInfo.selectCallback = selectCallback.init({
                name:accountInfo.config.name,
                url:basePath + "/inneraccount/fcacc/selectAccountByCorp",
                width:980,
                // 下面为t2优化代码
                //width:1058,
                hight:900,
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            if(!accountInfo.selectCallback){
                console.error("accountInfo组件未初始化")
            }
            accountInfo.selectCallback.set(accountInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('accountInfo', obj);
});

/**
 * 业务组件：保证金账户
 * @author luxy
 * 2018年9月19日
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
    var BondAccInfo = function () {
        this.config = {
            name:'bondAccInfo'
        }
        this.selectCallback = null;
        this.bizName="bondAccInfo";
    }

    BondAccInfo.prototype.v = '0.0.1';
    var bondAccInfo = new BondAccInfo();
    var obj = {
        init: function (ops) {
        	bondAccInfo.selectCallback = bt.selectCallback.init({
                name:bondAccInfo.config.name,
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
                url:basePath + "/bond/fcbondpreserve/selectBondAccPage",
            });
            return this;
        },
        set:function(obj){
        	bondAccInfo.selectCallback.set(bondAccInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('bondAccInfo', obj);
});
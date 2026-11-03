/**
 * 业务组件：招行保证金账户
 * @author zhous
 * 2024年9月6日
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
    var cmbBailAccountInfo = function () {
        this.config = {
            name:'cmbBailAccountInfo'
        }
        this.selectCallback = null;
        this.bizName="cmbBailAccountInfo";
    }

    cmbBailAccountInfo.prototype.v = '0.0.1';
    var cmbBailAccountInfo = new cmbBailAccountInfo();
    var obj = {
        init: function (ops) {
            cmbBailAccountInfo.selectCallback = bt.selectCallback.init({
                name:cmbBailAccountInfo.config.name,
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
                url:basePath + "/bill/accebill/abemitbill/selectZhBondAccPage",
            });
            return this;
        },
        set:function(obj){
            cmbBailAccountInfo.selectCallback.set(cmbBailAccountInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('cmbBailAccountInfo', obj);
});
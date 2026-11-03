/**
 * 业务组件：招行合作银行号
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var cmbBnkbilneg = function () {
        this.config = {
            name:'cmbBnkbilneg'
        };
        this.selectCallback = null;
    }
    cmbBnkbilneg.prototype.v = '0.0.1';
    var cmbBnkbilneg = new cmbBnkbilneg();
    var obj = {
        init: function (ops) {
            cmbBnkbilneg.selectCallback = selectCallback.init({
                name:cmbBnkbilneg.config.name,
                title:"银承合作协议编号",
                url:basePath + "/bill/accebill/abemitbill/cooperativeBankNumberList",
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
            if(!cmbBnkbilneg.selectCallback){
                console.error("cmbBnkbilneg组件未初始化")
            }
            cmbBnkbilneg.selectCallback.set(cmbBnkbilneg.config.name,obj);
        }
    };
    //输出test接口
    exports('cmbBnkbilneg', obj);
});

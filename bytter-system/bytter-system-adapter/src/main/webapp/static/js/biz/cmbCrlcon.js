/**
 * 业务组件：招行合作银行号
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var cmbCrlcon = function () {
        this.config = {
            name:'cmbCrlcon'
        };
        this.selectCallback = null;
    }
    cmbCrlcon.prototype.v = '0.0.1';
    var cmbCrlcon = new cmbCrlcon();
    var obj = {
        init: function (ops) {
            cmbCrlcon.selectCallback = selectCallback.init({
                name:cmbCrlcon.config.name,
                title:"额度编号信息",
                url:basePath + "/bill/accebill/abemitbill/cmbCrlconList",
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
            if(!cmbCrlcon.selectCallback){
                console.error("cmbCrlcon组件未初始化")
            }
            cmbCrlcon.selectCallback.set(cmbCrlcon.config.name,obj);
        }
    };
    //输出test接口
    exports('cmbCrlcon', obj);
});

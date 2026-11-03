/**
 * 业务组件：单位仓库维护
 * zouxs
 * 2017年6月22日
 * 0.0.1 新增必填参数
 * 0.0.2 新增加multiLayer参数，支持弹框中弹框功能
 */
layui.define(['selectCallback'], function (exports) {
    var UnitStore = function () {
        this.config = {
            name:'inputUnitStore'
        }
        this.selectCallback = null;
    }

    UnitStore.prototype.v = '0.0.1';
    var unitStore = new UnitStore();
    var obj = {
        init: function (ops) {
            unitStore.selectCallback = bt.selectCallback.init({
                name:unitStore.config.name,
                url:basePath + "/sys/unitStore/selectPage",
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            unitStore.selectCallback.set(unitStore.config.name,obj);
        }
    };
    //输出test接口
    exports('inputUnitStore', obj);
});
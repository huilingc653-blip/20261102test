/**
 * 业务组件：营运区维护
 * zouxs
 * 2017年6月22日
 * 0.0.1 新增必填参数
 * 0.0.2 新增加multiLayer参数，支持弹框中弹框功能
 */
layui.define(['selectCallback'], function (exports) {
    var InputArea = function () {
        this.config = {
            name:'inputOperationarea'
        }
        this.selectCallback = null;
    }

    InputArea.prototype.v = '0.0.1';
    var inputArea = new InputArea();
    var obj = {
        init: function (ops) {
            inputArea.selectCallback = bt.selectCallback.init({
                name:inputArea.config.name,
                url:basePath + "/sys/operationArea/selectPage",
                multiLayer:(ops && ops.multiLayer)?ops.multiLayer:false,
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            inputArea.selectCallback.set(inputArea.config.name,obj);
        }
    };
    //输出test接口
    exports('inputOperationarea', obj);
});
/**
 * 业务组件：选择合同信息
 * 郑刚
 * 2019年11月6日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var ConInfo = function () {
        this.config = {
            name:'conSelect'
        };
        this.selectCallback = null;
    }
    ConInfo.prototype.v = '0.0.1';
    var conSelect = new ConInfo();
    var obj = {
        init: function () {
            conSelect.selectCallback = selectCallback.init({
                name:conSelect.config.name,maxmin:true,
                url:basePath + "/contract/conCommon/conSelect",
            });
            return this;
        },
        set:function(obj){
            if(!conSelect.selectCallback){
                console.error("conSelect组件未初始化")
            }
            conSelect.selectCallback.set(conSelect.config.name,obj);
        }
    };
    exports('conSelect', obj);
});
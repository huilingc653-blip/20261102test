/**
 * 业务组件：国家
 * 秦雷
 * 2019年07月16日
 */
layui.define(['selectCallback'], function (exports) {
    var SwiftInfo = function () {
        this.config = {
            name:'swiftInfo'
        }
        this.selectCallback = null;
    }

    SwiftInfo.prototype.v = '0.0.1';
    var swiftInfo = new SwiftInfo();
    var obj = {
        init: function (ops) {
            swiftInfo.selectCallback = bt.selectCallback.init({
                name:swiftInfo.config.name,
                url:basePath + "/basic/business/btswiftinfo/select",
                multiIframes:(ops && ops.multiIframes)?ops.multiIframes:false,
            });
            return this;
        },
        set:function(obj){
            swiftInfo.selectCallback.set(swiftInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('swiftInfo', obj);
});
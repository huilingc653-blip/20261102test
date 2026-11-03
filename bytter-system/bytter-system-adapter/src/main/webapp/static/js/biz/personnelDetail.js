/**
 * 业务组件：职工号
 * 左宇
 * 2017年6月23日
 * 0.0.1 新增必填参数
 *
 * 0.0.2 重构选择，by周亮
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery;
    var PersonnelDetail = function () {
        this.config = {
            name:'personnelDetail'
        };
        this.selectCallback = null;
    }

    PersonnelDetail.prototype.v = '0.0.1';
    var personnelDetail = new PersonnelDetail();
    var obj = {
        init: function () {
            personnelDetail.selectCallback = bt.selectCallback.init({
                name:personnelDetail.config.name,
                url:basePath + "/netbank/nispersonneldetail/select/",
                width:900,
            });
            return this;
        },
        set:function(obj){
            personnelDetail.selectCallback.set(personnelDetail.config.name,obj);
        }
    };

    exports('personnelDetail', obj);
});
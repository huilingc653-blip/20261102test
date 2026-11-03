/**
 * 业务组件：查询用户已选择的数据
 * 秦雷
 * 2020年4月16日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var UserSelect = function () {
        this.config = {
            name:'mulViewUserPage'
        };
        this.selectCallback = null;
    }
    UserSelect.prototype.v = '0.0.1';
    var mulViewUserPage = new UserSelect();
    var obj = {
        init: function () {
            mulViewUserPage.selectCallback = selectCallback.init({
                name:mulViewUserPage.config.name,
                title:bt.lang("table.query.message"),
                maxmin:true,
                url:basePath + "/bankacc/btbankacchandle/mulViewUserPage",
            });
            return this;
        },
        set:function(obj){
            if(!mulViewUserPage.selectCallback){
                console.error("mulViewUserPage组件未初始化")
            }
            mulViewUserPage.selectCallback.set(mulViewUserPage.config.name,obj);
        }
    };
    exports('mulViewUserPage', obj);
});
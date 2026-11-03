/**
 * 业务组件：用户多选组件
 * 秦雷
 * 2020年4月16日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var UserSelect = function () {
        this.config = {
            name:'mulSelectUserPage'
        };
        this.selectCallback = null;
    }
    UserSelect.prototype.v = '0.0.1';
    var mulSelectUserPage = new UserSelect();
    var obj = {
        init: function () {
            mulSelectUserPage.selectCallback = selectCallback.init({
                name:mulSelectUserPage.config.name,maxmin:true,
                url:basePath + "/bankacc/btbankacchandle/mulSelectUserPage",
            });
            return this;
        },
        set:function(obj){
            if(!mulSelectUserPage.selectCallback){
                console.error("mulSelectUserPage组件未初始化")
            }
            mulSelectUserPage.selectCallback.set(mulSelectUserPage.config.name,obj);
        }
    };
    exports('mulSelectUserPage', obj);
});
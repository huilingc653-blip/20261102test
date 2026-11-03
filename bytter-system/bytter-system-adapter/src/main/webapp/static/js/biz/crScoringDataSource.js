/**
 * 业务组件：银行账号
 * zhoup
 * 2024年9月5日
 * 0.0.1 组件初始化
 */
layui.define(['selectCallback'], function (exports) {
    var $ = layui.jquery,selectCallback = bt.selectCallback;
    var CrScoringDataSource = function () {
        this.config = {
            name:'crScoringDataSource'
        };
        this.selectCallback = null;
    }
    CrScoringDataSource.prototype.v = '0.0.1';
    var crScoringDataSource = new CrScoringDataSource();
    var obj = {
        init: function () {
            crScoringDataSource.selectCallback = selectCallback.init({
                name:crScoringDataSource.config.name,maxmin:true,
                url:basePath + "/creditRating/dataSource/list",
            });
            return this;
        },
        set:function(obj){
            if(!crScoringDataSource.selectCallback){
                console.error("dataSource组件未初始化")
            }
            crScoringDataSource.selectCallback.set(crScoringDataSource.config.name,obj);
        }
    };
    exports('crScoringDataSource', obj);
});
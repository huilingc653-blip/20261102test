/**
 * 业务组件：银行账户流水
 * @author jiangw
 * 2024年7月9日
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
	 var $ = layui.jquery,selectCallback = bt.selectCallback;
    var BisAccDtl = function () {
        this.config = {
            name:'bisAccDtl'
        }
        this.selectCallback = null;
    }

    BisAccDtl.prototype.v = '0.0.1';
    var bisAccDtl = new BisAccDtl();
    var obj = {
    	init: function () {
            bisAccDtl.selectCallback = selectCallback.init({
    	                name: bisAccDtl.config.name,
    	                url:basePath + "/chowmatistic/choproductredeem/query",
    	                width:980,
    	                hight:900
    	            });
    	            return this;
    	        },
        set:function(obj){
        	 if(!bisAccDtl.selectCallback){
                 console.error("bisAccDtl组件未初始化")
             }
            bisAccDtl.selectCallback.set(bisAccDtl.config.name,obj);
        }
    };
    //输出test接口
    exports('bisAccDtl', obj);
});


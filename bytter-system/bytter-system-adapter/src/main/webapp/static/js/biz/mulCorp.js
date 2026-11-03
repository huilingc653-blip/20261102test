/**
 * 业务组件：单位下拉树
 * @author zhoup
 * 2018年9月28日
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
	 var $ = layui.jquery,selectCallback = bt.selectCallback;
    var MulCorp= function () {
        this.config = {
            name:'mulCorp'
        }
        this.selectCallback = null;
    }

    MulCorp.prototype.v = '0.0.1';
    var mulCorp = new MulCorp();
    var obj = {
    	init: function () {
    	        	mulCorp.selectCallback = selectCallback.init({
    	                name:mulCorp.config.name,
    	                url:basePath + "/budget/fbscorpbudget/mulcorp",
    	                width:980,
    	                hight:900
    	            });
    	            return this;
    	        },
        set:function(obj){
        	 if(!mulCorp.selectCallback){
                 console.error("mulCorp组件未初始化")
             }
        	mulCorp.selectCallback.set(mulCorp.config.name,obj);
        }
    };
    //输出test接口
    exports('mulCorp', obj);
});


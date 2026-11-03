/**
 * 业务组件：多选内部账号
 * @author zhoup
 * 2018年9月29日
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
	 var $ = layui.jquery,selectCallback = bt.selectCallback;
    var MulAccInfo= function () {
        this.config = {
            name:'mulAccInfo'
        }
        this.selectCallback = null;
    }

    MulAccInfo.prototype.v = '0.0.1';
    var mulAccInfo = new MulAccInfo();
    var obj = {
    	init: function () {
    		mulAccInfo.selectCallback = selectCallback.init({
    	                name:mulAccInfo.config.name,
    	                url:basePath + "/netbank/cgsbill/mulAccInfo",
    	                width:980,
    	                hight:900
    	            });
    	            return this;
    	        },
        set:function(obj){
        	 if(!mulAccInfo.selectCallback){
                 console.error("mulAccInfo组件未初始化")
             }
        	 mulAccInfo.selectCallback.set(mulAccInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('mulAccInfo', obj);
});


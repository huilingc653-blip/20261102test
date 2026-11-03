/**
 * 业务组件：多选内部账号
 * @author chenf
 * 2022年6月23日
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
	 var $ = layui.jquery,selectCallback = bt.selectCallback;
    var InterAccInfo= function () {
        this.config = {
            name:'interAccInfo'
        }
        this.selectCallback = null;
    }

    InterAccInfo.prototype.v = '0.0.1';
    var interAccInfo = new InterAccInfo();
    var obj = {
    	init: function () {
            interAccInfo.selectCallback = selectCallback.init({
    	                name:interAccInfo.config.name,
    	                url:basePath + "/finacial/check/fccompareAutoCenter/interAccInfo",
    	                width:980,
    	                hight:900
    	            });
    	            return this;
    	        },
        set:function(obj){
        	 if(!interAccInfo.selectCallback){
                 console.error("interAccInfo组件未初始化")
             }
            interAccInfo.selectCallback.set(interAccInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('interAccInfo', obj);
});


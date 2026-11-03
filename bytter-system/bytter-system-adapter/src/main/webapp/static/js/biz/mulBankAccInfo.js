/**
 * 业务组件：多选银行账号
 * @author zhoup
 * 2018年9月30日
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
	 var $ = layui.jquery,selectCallback = bt.selectCallback;
    var MulBankAccInfo= function () {
        this.config = {
            name:'mulBankAccInfo'
        }
        this.selectCallback = null;
    }

    MulBankAccInfo.prototype.v = '0.0.1';
    var mulBankAccInfo = new MulBankAccInfo();
    var obj = {
    	init: function () {
    		mulBankAccInfo.selectCallback = selectCallback.init({
    	                name:mulBankAccInfo.config.name,
    	                url:basePath + "/bankacc/btbankaccquery/mulBankAccInfo",
    	                width:980,
    	                hight:900
    	            });
    	            return this;
    	        },
        set:function(obj){
        	 if(!mulBankAccInfo.selectCallback){
                 console.error("mulBankAccInfo组件未初始化")
             }
        	 mulBankAccInfo.selectCallback.set(mulBankAccInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('mulBankAccInfo', obj);
});


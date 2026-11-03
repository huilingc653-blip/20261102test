/**
 * erp对账多选银行账号
 */
layui.define(['selectCallback'], function (exports) {
	 var $ = layui.jquery,selectCallback = bt.selectCallback;
    var ErpMulBankAccInfo= function () {
        this.config = {
            name:'erpMulBankAccInfo'
        }
        this.selectCallback = null;
    }

    ErpMulBankAccInfo.prototype.v = '0.0.1';
    var erpMulBankAccInfo = new ErpMulBankAccInfo();
    var obj = {
    	init: function () {
            erpMulBankAccInfo.selectCallback = selectCallback.init({
    	                name:erpMulBankAccInfo.config.name,
    	                url:basePath + "/finacial/check/erpDataManager/erpMulBankAccInfo",
    	                width:980,
    	                hight:900
    	            });
    	            return this;
    	        },
        set:function(obj){
        	 if(!erpMulBankAccInfo.selectCallback){
                 console.error("erpMulBankAccInfo组件未初始化")
             }
            erpMulBankAccInfo.selectCallback.set(mulBankAccInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('erpMulBankAccInfo', obj);
});


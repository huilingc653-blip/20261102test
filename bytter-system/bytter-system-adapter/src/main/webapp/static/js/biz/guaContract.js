/**
 * 业务组件：抵质押物信息
 * @author luxy
 * 2019年04月29日
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
	var $ = layui.jquery,selectCallback = bt.selectCallback;
    var GuaContract= function () {
        this.config = {
            name:'guaContract'
        }
        this.selectCallback = null;
        this.bizName="guaContract";
    }
    GuaContract.prototype.v = '0.0.1';
    var guaContract= new GuaContract();
    var obj = {
    	init: function () {
    		guaContract.selectCallback = selectCallback.init({
    	                name:guaContract.config.name,
    	                url:basePath + "/cms/guarantee/guaContractInfo/guaContractlist",
    	                width:980,
    	                hight:900
    	            });
    	            return this;
    	        },
        set:function(obj){
        	 if(!guaContract.selectCallback){
                 console.error("guaContract组件未初始化")
             }
        	 guaContract.selectCallback.set(guaContract.config.name,obj);
        }
    };
    //输出test接口
    exports('guaContract', obj);
});


/**
 * 业务组件：抵质押物信息
 * @author fusb
 * 2018年12月19日
 * 0.0.1 
 */
layui.define(['selectCallback'], function (exports) {
	var $ = layui.jquery,selectCallback = bt.selectCallback;
    var GuaPledgeInfo= function () {
        this.config = {
            name:'guaPledgeInfo'
        }
        this.selectCallback = null;
        this.bizName="guaPledgeInfo";
    }
    GuaPledgeInfo.prototype.v = '0.0.1';
    var guaPledgeInfo = new GuaPledgeInfo();
    var obj = {
    	init: function () {
    		guaPledgeInfo.selectCallback = selectCallback.init({
    	                name:guaPledgeInfo.config.name,
    	                url:basePath + "/cms/guarantee/guaContractInfo/toListPageInfo",
    	                width:980,
    	                hight:900
    	            });
    	            return this;
    	        },
        set:function(obj){
        	 if(!guaPledgeInfo.selectCallback){
                 console.error("guaPledgeInfo组件未初始化")
             }
        	 guaPledgeInfo.selectCallback.set(guaPledgeInfo.config.name,obj);
        }
    };
    //输出test接口
    exports('guaPledgeInfo', obj);
});


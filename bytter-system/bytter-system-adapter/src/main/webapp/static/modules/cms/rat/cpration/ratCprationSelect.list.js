bt.use(['form','datatable','btdate','money','dateType','dicts', 'moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    
    /*
     * 授信占用
     * itemMoney ： 授信占用的最高额度
     */
    bt.rationselect = function(ratCode, itemFlag, itemCode, overPlusMoney,leftMoney,assurepercent,isHangGuarantee) {
    	var params = {};
    	if(itemFlag == "0"){
    		var sumMoney = 
    		params = {
    				"ratCode" : ratCode,
    				"itemFlag" : itemFlag,
    				"itemCode" : itemCode,
    				"itemMoney" : overPlusMoney,
				"assurepercent": assurepercent,
				"leftMoney":leftMoney,
				"isHangGuarantee":isHangGuarantee
    			}
    	} else {
    		//输入综合品种各品种的占用额度
    		var rationDlts = [];
    		var sumMoney = 0;
    		var index = 0;
        	/*$.each($("input[data-id^='"+ratCode+"maxMoney']"), function(i,item){
        		sumMoney = bt.money.add(sumMoney, $(item).val());
            });
    		if(sumMoney - 0 == 0){
    			layer.alert(bt.lang("table.ratUration.maxMoney.null"));
    			return false;
    		}*/
        	if(bt.money.toNumber(overPlusMoney) - 0 >= 0){
        		var rationDlt = {};
        		rationDlt["itemCode"] = itemCode;
        		rationDlt["itemMoney"] = overPlusMoney;
        		rationDlts.push(rationDlt);
        	}
    		params = {
    				"ratCode" : ratCode,
    				"itemFlag" : itemFlag,
    				"itemCode" : itemCode,
    				"rationDlts" : rationDlts,
    				"itemMoney" : overPlusMoney,
				"assurepercent": assurepercent,
                "leftMoney":leftMoney,
                "isHangGuarantee":isHangGuarantee
    			}
    	}
    	bt.useRation(params);
    }
    
    
    //回传授信调用页面数据
    bt.useRation = function(params){
        parent.setCpRation(params);
        
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭    
    }
    
    //取消
    bt.cancelRation = function(){
    	parent.layer.closeAll();
    }
    
    bt.dicts.init();
    bt.moneyType.init();
});


rationselect = function(ratCode, itemFlag, itemCode, overPlusMoney,leftMoney,assurepercent,isHangGuarantee){
	bt.rationselect(ratCode, itemFlag, itemCode, overPlusMoney,leftMoney,assurepercent,isHangGuarantee);
}

cancelRation = function(){
	bt.cancelRation();
}
bt.use(['form','datatable','btdate','money','dateType','dicts', 'moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    
    /*
     * 授信占用
     * itemMoney ： 授信占用的最高额度
     */
    bt.rationselect = function(ratCode, itemFlag, itemCode, overPlusMoney,leftMoney,assurepercent,isHangGuarantee,bankCode,contractNo,bankTypeName,bankName) {
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
				"isHangGuarantee":isHangGuarantee,
				"bankCode":bankCode,
				"contractNo":contractNo,
				"bankTypeName":bankTypeName,
				"bankName":bankName
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
                "isHangGuarantee":isHangGuarantee,
                "bankCode":bankCode,
				"contractNo":contractNo,
				"bankTypeName":bankTypeName,
				"bankName":bankName
    			}
    	}
    	bt.useRation(params);
    }
    
    
    //回传授信调用页面数据
    bt.useRation = function(params){
        parent.setRation(params);
        
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


rationselect = function(ratCode, itemFlag, itemCode, overPlusMoney,leftMoney,assurepercent,isHangGuarantee,bankCode,contractNo,bankTypeName,bankName){
	bt.rationselect(ratCode, itemFlag, itemCode, overPlusMoney,leftMoney,assurepercent,isHangGuarantee,bankCode,contractNo,bankTypeName,bankName);
}

cancelRation = function(){
	bt.cancelRation();
}

function exportOccupyDtl() {
	var url = basePath + "/cms/rat/raturation/exportOccupyDtl";
	var params = {
		ratCode: $("#ratCode").val(),
		itemFlag: $("#itemFlag").val(),
		itemCode: $("#itemCode").val(),
		corpId: $("#corpId").val(),
		useType: $("#useType").val(),
		businessName: $("#businessName").val()
	}
	DownLoadFile({url: url, data: params, method: 'post'});
	return false;
}

var DownLoadFile = function (options) {
	var config = $.extend(true, {method: 'post'}, options);
	var $iframe = $('<iframe id="down-file-iframe" />');
	var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
	$form.attr("action", config.url);
	for (var key in config.data) {
		$form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
	}
	$iframe.append($form);
	$(document.body).append($iframe);
	$form[0].submit();
	$iframe.remove();
}
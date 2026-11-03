/**内部贷款获取合同类型 CON_PD_INNERLOAN**/
function getConTypeInnerLoan(corpId){
	return getConType(corpId,"CON_PD_INNERLOAN");
}

/**委托贷款获取合同类型 CON_PD_ENTRUSTLOAN**/
function getConTypeEntrustLoan(corpId){
	return getConType(corpId,"CON_PD_ENTRUSTLOAN");
}

/**银行借款获取合同类型 CON_PD_BANKBORROW**/
function getConTypeBankBorrow(corpId){
	return getConType(corpId,"CON_PD_BANKBORROW");
}

/**授信管理获取合同类型 CON_PD_RAT**/
function getConTypeRat(corpId){
	return getConType(corpId,"CON_PD_RAT");
}

/**担保管理获取合同类型 CON_PD_GUA**/
function getConTypeGua(corpId){
	return getConType(corpId,"CON_PD_GUA");
}

/**对外担保管理获取合同类型 CON_PD_EXTGUA**/
function getConTypeExtGua(corpId){
    return getConType(corpId,"CON_PD_EXTGUA");
}

/**内部拆借获取合同类型 CON_PD_INTERNALBORROW_LEND**/
function getConTypeInl(corpId){
	return getConType(corpId,"CON_PD_INTERNALBORROW_LEND");
}
var conTypeId;
/**
 * 公共进行获取合同类型方法
 * @param corpId 所属单位
 * @param pdCode 产品系统类型：
 * CON_PD_INNERLOAN	内部贷款
 * CON_PD_ENTRUSTLOAN	委托贷款
 * CON_PD_BANKBORROW	银行借款
 * CON_PD_RAT	授信管理
 * CON_PD_GUA	担保管理
 * @return false和空没有合同类型不允许保存
 */
function getConType(corpId, pdCode){
	if(!corpId){
		layer.alert(bt.lang("contract.corpId.isNotNull"), 2);
		return false;
	}
	if(!pdCode){
		layer.alert(bt.lang("contract.pdCode.isNotNull"), 2);
		return false;
	}
//	if(conTypeId){
//		return conTypeId;
//	}
	var params={"corpId":corpId,"pdCode":pdCode};
	var data;
	$.ajax({
    	type: "GET",
    	url: basePath + "/contract/contractType/getAllConType",
       	data: params,//将对象序列化成JSON字符串
       	dataType:"json",
       	async: false, 
    	success: function(result){
    		if(result){
    			data=result;
        	}
    	}
    });
	if(!data || data.length==0){
        bt.loading.hide(); //关闭加载中
		layer.alert(bt.lang('contract.pdCode.isNotConType'), 2);
		return false;
	}
	if(data instanceof Array && data.length==1){
		conTypeId = data[0].id;
		return conTypeId;
	}else{
		layer.open({
			type : 2,
			title : bt.lang('contract.conType'),
	       	async: false, 
			content : basePath + "/contract/contractType/conTypeSelect?corpId="+corpId+"&pdCode="+pdCode,
			btn : [ bt.lang('common.button.confirm'),bt.lang('common.button.close') ],
			yes : function(index) {
				// 当点击‘确定'按钮的时候，获取弹出层返回的值
				conTypeId = window["layui-layer-iframe" + index].callbackdata();
				// 最后关闭弹出层
				layer.close(index);
				if(conTypeId){
				if(typeof setConTypeCallback === "function"){
					//回调业务回写合同类型方法
					setConTypeCallback(conTypeId);
				}else{
					layer.alert(bt.lang('contract.pdCode.isNotSetConTypeCallback'), 2);
				}
				}
			},
			cancel : function() {
			}
		});   
	}
	return false;
}


/**单个检查客商信息
 * @param data {isControl:是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递） ,name:检查名称,corpId:单位ID}
 * @return {reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
 */
function externalCorpCheck(data){
	var dataParams={"isControl":data.isControl,params:[{"name":data.name,"corpId":data.corpId}],"isCallBack":data.isCallBack};
	return externalCorpCheckBatch(dataParams);
}

/**单个检查承兑行信息
 * @param data {isControl:是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递） ,name:检查名称,corpId:单位ID}
 * @return {reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
 */
function bankAcceptanceCheck(data){
	var dataParams={"isControl":data.isControl,params:[{"name":data.name,"corpId":data.corpId}],"isCallBack":data.isCallBack};
	return bankAcceptanceCheckBatch(dataParams);
}


/**批量检查客商信息
 * @param data {isControl:是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递） ,params:[{name:检查名称,appNo:申请单据号,corpId:单位ID},{name:检查名称,appNo:申请单据号,corpId:单位ID}]}
 * @return {reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
 */
function externalCorpCheckBatch(data){
	return blackWhiteCheckBatch(data.params,data.isControl,"01");
}

/**批量检查承兑行信息
 * @param data {isControl:是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递） ,params:[{name:检查名称,appNo:申请单据号,corpId:单位ID},{name:检查名称,appNo:申请单据号,corpId:单位ID}]}
 * @return {reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
 */
function bankAcceptanceCheckBatch(data){
	return blackWhiteCheckBatch(data.params,data.isControl,"02");
}

/**
 * 公共进行获取合同类型方法
 * @param params 验证信息集合
 * @param isControl 是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递）
 * @param listType 名单类别：01客商、02承兑行
 * @return {reCode:1不提醒验证通过、2提醒不限制业务、3提醒且限制业务不允许操作，msg:失败信息reCode为2和3时必填！提醒内容}
 */
function blackWhiteCheckBatch(params,isControl,listType){
	var blacklistCheckType;
	$.ajax({
		type: "get",
		url: basePath + "/blackwhite/configure/blacklistCheckType",
		async: false,
		dataType:"json",
		success: function(result){
			blacklistCheckType=result;
		}
	});
	if(blacklistCheckType=="1") {
		return {"reCode": "1", "msg": ""};
	}
	var data;
	$.ajax({
    	type: "post",
    	url: basePath + "/blackwhite/configure/blackWhiteCheck",
       	data: JSON.stringify({"dtoList":params,"isControl":isControl,"listType":listType}),//将对象序列化成JSON字符串
		dataType:"json",
		contentType : 'application/json;charset=utf-8', //设置请求头信息
		async: false,
    	success: function(result){
    		if(result){
    			if(result.success===false){
    				data={"reCode": "3", "msg": result.msg};
				}else{
					data=result;
				}
        	}
    	},
		error: function(res){
			data={"reCode": "3", "msg": res};
		}
    });
	return data;
}


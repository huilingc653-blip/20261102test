/**
 * 选择合同时校验信息
 */
function conSelectBefore() {
	var corpId = $('#corpId').val();
	params = new Object();
	params.corpId=corpId;
	if(bt.empty(corpId)){
        layer.alert(bt.lang('common.sys.corp.null'), {icon:5});
        return false;
	}else{
        bt.conSelect.set(params);
	}
	return true;
}

/**
 * 合同选择的回调函数
 * @param data
 * @param dom
 */
function conSelectCallback(data,dom){
	$("#conId").val(data.id);
	if (document.getElementById("contractCode")){
		$("#contractCode").val(data.conNo);
	}
	if (document.getElementById("contractNo")){
		$("#contractNo").val(data.conNo);
	}
	if (document.getElementById("conTypeId")){
		$("#conTypeId").val(data.typeId);
	}
	$("#viewCon").show();
}

/**
 * 查看挂接的合同信息
 */
function conViewBefore() {
	var conId = $('#conId').val();
	var params = new Object();
	params.id = conId;
	if(bt.empty(conId)){
        layer.alert(bt.lang("cms.conInfo.unSelect"), {icon:5});
        return false;
	}else{
        bt.conView.set(params);
	}
	return true;
}

/**
 * 合同查看回调函数
 * @param data
 * @param dom
 * @returns {boolean}
 */
function conViewCallback(data,dom){
	return true;
}

/**
 * 清空业务对接的合同信息
 */
function clearConInfo(){
	if ($("#relateCon").val() == '1'){
        $("#conId").val("");
        $("#conTypeId").val("");
        $("#viewCon").hide();
        if (document.getElementById("contractCode")){
            $("#contractCode").val("");
        }
        if (document.getElementById("contractNo")){
            $("#contractNo").val("");
        }
	}
}



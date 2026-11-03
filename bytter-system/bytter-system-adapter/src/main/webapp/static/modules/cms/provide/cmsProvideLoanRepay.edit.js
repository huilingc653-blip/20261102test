var dateTypes, moneyTypes,selectTypes,rateTypes,dicts;
bt.use(['form','layedit','dateType','moneyType','dicts','btUpload','selectType','selectTable','rateType', 'bankInfo', 'btImport'], function(){
    var $ = bt.jquery,form = bt.form();fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
	moneyTypes = bt.moneyType.init();//金额
	dicts = bt.dicts.init();//字典
	selectTypes = bt.selectType.init();//下拉框
	rateTypes = bt.rateType.init();//利率
	bt.bankInfo.init();//联行号

    initBankAccTable();
	var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    renderBudget();
	renderSyndicated();
	initIsAdvancedRepay($("#isAdvancedRepay").data("value")+"");

	//监听提交
    form.on('submit', function(data){
        var nm = $(this).attr("id");
        var saveType="0";
        if(nm == "submit"){
        	//data.field.saveType = "1";
        	saveType="1";
        }
        var action = "/cms/provide/loanrepay/tempOrSubmit?sign="+$("#downSign").val()+"&saveType="+saveType;
        var acrossRepair = dicts.get("acrossRepair").val();
        var onlineSign = $("#onlineSign").val();
        data.field.uploadId = up.getGroupId();
        var budgetSel = $("#budgetSel").val();
        var tempOrSubmitSign=$("#tempOrSubmitSign").val();
        data.field.tempOrSubmitSign = tempOrSubmitSign;

    	if(budgetSel == "1"){
    		if(acrossRepair == 1){
    			if($("input[name='sumMoney']").length){
    				//判断是否挂接资金计划
    				var sumMoney = fun.toNumber(moneyTypes.get("sumMoney").val());
    				var repayNum = fun.toNumber(moneyTypes.get("repayMoney").val());
    				var repayAccrualNum = fun.toNumber(moneyTypes.get("repayAccrual").val());
    				var repayMoney = repayNum - 0 + repayAccrualNum;
    				// 校验金额与资金计划总金额一致
    				if (fun.toNumber(sumMoney) != fun.toNumber(repayMoney)) {
    					bt.loading.hide();
    					layer.alert(bt.lang("provide.loanrepay.message.budgetMoney"), {icon: 2});
    					return false;
    				}
    			}
    		}else if(onlineSign == 1){
    			if($("input[name='sumMoney']").length){
    				var sumMoney = fun.toNumber(moneyTypes.get("sumMoney").val());
    				// 校验金额与资金计划总金额一致
    				if (fun.toNumber(sumMoney) != fun.toNumber(data.field.onlineMoney)) {
    					bt.loading.hide();
    					layer.alert(bt.lang("provide.loanrepay.message.check.budgetMoney"), {icon: 2});
    					return false;
    				}
    			}
    		}
    	}
		// 银团信息校验
		if ($("#plClass").val() === "1000") {
			var trs = $("#dataTableSy").children("tbody").children("tr");
			if (trs.length == 0) {
				parent.layer.alert(bt.lang("cms.loan.provideSyndicatedDtl.notExists"), {icon: 7});
				return false;
			}
		}

		if (repayType && accrualType) {
			if (repayType.interestFrequencyType != "N" && repayType.capitalFrequencyType != "N") {
				if (!$("#provideInterestId").val()) {
					parent.layer.alert(bt.lang("cms.provide.loan.repay.select.repayPlanAndTest"), {icon: 7});
					return false;
				}
			}
		} else {
			if (!$("#provideInterestId").val()) {
				parent.layer.alert(bt.lang("cms.provide.loan.repay.select.repayPlanAndTest"), {icon: 7});
				return false;
			}
		}


        parent.layer.confirm(bt.lang('table.cmsProvideLoanRepay.repayAccrualMsgOk'),function(){
			if ($("#overdueSign").val() == "1") {
				parent.layer.confirm(bt.lang('table.cmsProvideLoanRepay.alert.info'),function(){
					submitForm(action, data);
				});
			}else{
				submitForm(action, data);
			}
        });
        return false;
    });
});

function submitForm(action, data) {
	bt.loading.show();
	$.post(basePath + action, data.field, function(result){
		parent.layer.msg(result.msg, {icon: result.success?1:2});
		if(result.success){
			if(!bt.navtab){
				bt.navtab.refreshCurrentTable();
				parent.layer?parent.layer.closeAll():layer.closeAll();
			} else {
				bt.navtab.closeCurrent();
				bt.navtab.refreshCurrentTable();
				var webObj = bt.navtab.getParentDom();
				webObj.frames['repayApp'].$.fn.dataTable.tables( {api: true} ).draw();
				webObj.frames['repayFollow'].$.fn.dataTable.tables( { api: true} ).draw();
				bt.navtab.closeCurrent();
			}
		}else{
			bt.loading.hide();
		}
	},'JSON');
}

/**选择还款计划后执行的方法*/
function setSelectInterestData(repayDate,repayMoney,payInterest,id){
	dateTypes.get("repayDate").val(repayDate);
	moneyTypes.get("repayMoney").val(repayMoney);
	moneyTypes.get("repayAccrual").val(payInterest);
	moneyTypes.get("onlineRealityInterest").val(payInterest);
	bt.jquery("#provideInterestId").val(id);
	if("1" == $("#onlineSign").val()){//在线
		countOnlineMoney();
	}
	renderBudget();
	// 计算银团贷款数据
	if ("1000" === $("#plClass").val()) {
		calCustomMoney();
	}
}

/**设置自动计算在线金额*/
function countOnlineMoney(){
	if("1" == $("#onlineSign").val()){//在线
		var interest = fun.toNumber(moneyTypes.get("onlineRealityInterest").val());
		var repayMoney = fun.toNumber(moneyTypes.get("repayMoney").val());
		moneyTypes.get("onlineMoney").val(fun.add(interest + repayMoney));
	}
	//按还款本金计算随借随还利息
	nonSetRepayAccrual();
}

function nonSetRepayAccrual() {
	if(repayType.capitalAndInterest=="1" && repayType.interestFrequencyType == "N" && repayType.capitalFrequencyType == "N") {
		var repayMoney = fun.toNumber(moneyTypes.get("repayMoney").val());
		var repayDate = dateTypes.get('repayDate').val();
		var sendDate = $("#sendDate").val();
		var days = Math.abs((new Date(repayDate).getTime() - new Date(sendDate).getTime())) / (1000 * 60 * 60 * 24);
		var rateBase = $("#rateBase").val();
		var plRate = $("#plRate").val();
		var interest = Number(repayMoney)*days*Number(plRate)/Number(rateBase);
		moneyTypes.get("repayAccrual").val(interest);
	}
}

/**选择离线帐号的下拉列表初始化*/
function initBankAccTable(){
	//银行账号下拉表格初始化
	var corpId = $("#corpId").val();
	$.post(basePath+"/cms/provide/loanrepay/queryPaymentAccListByCoprIdCount",{"corpId":corpId},function(result){
		/*var htl = "";
        if("0" == result){//没有帐号
        	htl = bt.lang('table.cmsProvideLoanRepay.onlinePaymentAccMsg');
        }
        $("#accMsg").html(htl);*/
    },'JSON');

	// scrollY:true,
	var seelectTab = bt.selectTable.init({
		elem: "#paymentAccTable",
		searchInput: '#onlinePaymentAcc',
		scrollX:true,
		url: basePath+"/cms/provide/loanrepay/queryPaymentAccListByCoprId?corpId=" + corpId +"&curId="+ $("#curId").val(),
		columns: [
		    {data:"bankAcc"},
		    {data:"accName"},
		    {data:"bankName"},
		],
		clickCallback:function(severData,data){
			 $("#onlinePaymentAcc").val(severData.bankAcc);
			 $("#onlineAccName").val(severData.accName);
			 $("#onlineBankName").val(severData.bankName);
			 $("#onlinePaymentAccId").val(severData.id);
        },
		handleDeleteCallback : function (){
			clearOnlineAcc();
		}
	});
}

//清空还款账号信息
function clearOnlineAcc () {
	$("#onlinePaymentAcc").val("");
	$("#onlineAccName").val("");
	$("#onlineBankName").val("");
	$("#onlinePaymentAccId").val("");
}
/**
 * 资金计划挂接
 */
function renderBudget(arr){
	//判断支付结算是否挂接资金计划
	//if(moneyTypes.get("repayMoney").val() != 0){//本金不为0加载 资金计划
		var budgetSel = $("#budgetSel").val();
		var onlineSign = $("#onlineSign").val();
		var ar = $("#acrossRepair").val();
		var downSign = $("#downSign").val();
		if(budgetSel == "1"){
			if(downSign != "0"){
				if(onlineSign == "1" || arr == "1" || ar == "1"){
					//判断银行借款是否挂资金计划
		    		var sendItem = $("#repayItem").val();
		    		if(sendItem != null && sendItem != ""){
		    			$genOtherHtml();
		    		}else{
		    			$("#tagItemDetail").html("");
		    		}
				}else {
                    $("#tagItemDetail").html("");
                }
			}else {
                if (arr == "1" || ar == "1") {
                    //判断银行借款是否挂资金计划
                    var sendItem = $("#repayItem").val();
                    if (sendItem != null && sendItem != "") {
                        $genOtherHtml();
                    } else {
                        $("#tagItemDetail").html("");
                    }
                }else {
                    $("#tagItemDetail").html("");
                }
            }
		}
	/*}else{
		$("#tagItemDetail").html("");
	}*/

}

/**
 * 银团明细
 */
function renderSyndicated(){
	//判断借款种类是否为“银团贷款”
	var plClass = $("#plClass").val();
	if (plClass == "1000") {
		$("#syndicatedShow").show();
		initSyndicatedDtl()
	} else {
		$("#syndicatedShow").hide();
	}
}

/**
 * 生成公共的属性
 * @return
 */
/**
 * 生成公共的属性
 * @return
 */
$genOtherHtml=function(){
    var itemCode = $('#repayItem').val();
    var defItemCode = $("#defItemCode").val();
    var processDate = dateTypes.get("repayDate").val();
    var corpId = $("#corpId").val();
    var url=basePath;
    var repayId = $("#id").val();
    var curId=$("#curId").val();
    var param = null;
    if(repayId == null || repayId == ""){
    	url += "/budget/fbsBudgetProcess/getItemDetail";
    	param = {
	        corpId:corpId,
	        itemCode:itemCode,
	        defItemCode:defItemCode,
	        processDate:processDate,
            curId:curId,
	        type:'select',
	        date:(new Date()).getTime()
	    };
    }else{
    	url=basePath+"/budget/fbsBudgetProcess/getEditItemDetail";
        param={
            corpId:corpId,
            itemCode:itemCode,
            billNo: $("#billCode").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            defItemCode:defItemCode,
            processDate:processDate,
            curId:curId,
            type:'select',
            date:(new Date()).getTime()
        };
    }

    $.ajax({
    	url:url,
    	method: 'POST',
    	data: param,
    	async:true,
    	success: function (json) {
	        $("#tagItemDetail").html("");
	        $("#tagItemDetail").html(json);
	        $("#tagItemDetail").show();
	        bt.form().render();
	    }
	});
};

/**
 * 设置还款利息是否可输入
 */
function showAccrual(){
	var ar = dicts.get("acrossRepair").val();
	if(ar == "1"){
		$("#acrossRepair").val("1");
		renderBudget(ar);
	}else{
		$("#acrossRepair").val("0");
		renderBudget(ar);
	}
}

/**加载还款计划*/
function repayLan(){
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),basePath + "/cms/basedata/repaymentschedule/toRepayPlanSave?billCode="+$("#sendCode").val() );
    }
}


function initIsAdvancedRepay(flag) {
	var repayMinDate = $('#repayMinDate').val()
	var repayMaxDate = $('#repayMaxDate').val()
	var mind = $("#mind").val();
	var maxd = $("#maxd").val();
	var repayDate=dateTypes.get('repayDate').val();
	if (flag == "1") {
		dateTypes.get('repayDate').attr('data-min', repayMinDate);
		dateTypes.get('repayDate').attr('data-max', repayMaxDate);
		$("#repayInterestOn").addClass('layui-hide');
		$("#repayInterestTest").removeClass('layui-hide');
	} else {
		dateTypes.get('repayDate').attr('data-min', mind);
		dateTypes.get('repayDate').attr('data-max', maxd);
		$("#repayInterestOn").removeClass('layui-hide');
		$("#repayInterestTest").addClass('layui-hide');
	}
	dateTypes.get('repayDate').val(repayDate);
}
/**是否提前还款按钮回调*/
function isAdvancedRepayCallback() {
	var flag = dicts.get("isAdvancedRepay").val() + '';
	initIsAdvancedRepay(flag);
	//dateTypes.get('repayDate').val("");
}
/**加载利息预计明细*/
function repayInterest(){
	var flag = dicts.get("isAdvancedRepay").val() + '';
	if (flag == "0"){
		if(bt.navtab){
			bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),basePath + "/cms/provide/cmsprovideinterest/list?sendBillCode="+$("#sendCode").val()+"&interestID="+$("#provideInterestId").val()+"&repairSign=1");
		}
	} else {
		//试算
		var repayDate = $('#repayDate').val()
		var repayMoney = moneyTypes.get("repayMoney").val();
		var repayMaxLeftMoney = $('#repayMaxLeftMoney').val()
		if (!repayDate) {
			parent.layer.msg(bt.lang('cms.provide.loaninfo.repaydateempty'), {icon: 5});
			return false
		}
		if (!repayMoney || Number(repayMoney) > Number(repayMaxLeftMoney)) {
			parent.layer.msg("还款金额不能为空且不可超出剩余应还本金金额：" + repayMaxLeftMoney, {icon: 5});
			return false
		}
		if (bt.navtab) {
			bt.navtab.addNavtab(bt.lang('cms.button.repayPlanTest'), basePath + "/cms/provide/cmsprovideinterest/testList?sendBillCode=" + $("#sendCode").val() + "&interestId=" + $("#provideInterestId").val()
				+ "&repairSign=1" + "&loanMoney=" + $("#repayMoney").val() + "&repayDate=" + $("#repayDate").val() + "&sendId=" + $("#sendId").val() + "&repayType=" + $("#repayType").val());
		}
	}

}

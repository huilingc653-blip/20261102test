bt.use(['form','layedit','dateType','moneyType','dicts','btUpload'], function(){
    $ = bt.jquery,form = bt.form();
    fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
	moneyTypes = bt.moneyType.init();//金额
	dicts = bt.dicts.init();//字典

	var gid = $("#uploadId").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid,
				success:function (data) {
        	// 有数据时，把groupid保存在 $("#uploadId")
        	if(data && data.length && data[0].data ){
						$("#uploadId").val(data[0].data.groupId)
					}else{
						$("#uploadId").val('')
					}
			}
    });

	//监听提交
    form.on('submit', function(data){
        var action = "/cms/provide/loanrepay/repairTempOrSubmit";
        //var resData = data.field;
        var nm = $(this).attr("id");
        var saveType="0";
    	if(nm == "submit"){
        	//resData.saveType = "1";
        	saveType="1";
        }
    	var resData = $("#inputForm").serialize()+"&saveType="+saveType;
    	//data.field.uploadId = up.getGroupId();
    	var budgetSel = $("#budgetSel").val();
    	if(budgetSel == "1"){
    		if($("input[name='sumMoney']").length){
    			//判断是否挂接资金计划
    			var sumMoney = fun.toNumber(moneyTypes.get("sumMoney").val());
    			var repayNum = fun.toNumber(moneyTypes.get("repayMoney").val());
				var repayAccrualNum = fun.toNumber(moneyTypes.get("onlineRealityInterest").val());
				var repayMoney = fun.add(repayNum,repayAccrualNum);
    			// 校验金额与资金计划总金额一致
    			if (fun.toNumber(sumMoney) != fun.toNumber(repayMoney)) {
    				bt.loading.hide();
    				layer.alert(bt.lang("provide.loanrepay.message.budgetMoney"), {icon: 2});
    				return false;
    			}
    		}
    	}
    	submitAjax(action,resData);
        return false;
    });

    submitAjax = function (url,data){
    	bt.loading.show();
    	$.post(basePath + url,data,function(result){
    		parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(!bt.navtab){
            		bt.navtab.refreshCurrentTable();
                    parent.layer?parent.layer.closeAll():layer.closeAll();
            	} else {
           			bt.navtab.closeCurrent();
            	}
            }else{
            	bt.loading.hide();
            }
        },'JSON');
    }

    renderBudget();
});

/**
 * 资金计划挂接
 */
function renderBudget(){
	//if(moneyTypes.get("repayMoney").val() != 0){//本金不为0加载 资金计划
		//判断支付结算是否挂接资金计划
		var budgetSel = $("#budgetSel").val();
		if(budgetSel == "1"){
			//判断银行借款是否挂资金计划
	    	var sendItem = $("#repayItem").val();
	    	if(sendItem != null && sendItem != ""){
	    		$genOtherHtml();
	    	}else{
	    		$("#tagItemDetail").html("");
	    	}
		}else{
			$("#tagItemDetail").html("");
		}
	/*}else{

	}*/

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
    var curId=$("#curCode").val();//币别
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
	        form.render();
	    }
	});
};

function back(){
	var id = $("#id").val();
	var refuseWhy = $("#refuseWhy").val();
	if(refuseWhy.length > 0 && refuseWhy.length <= 50){
		resData = {id:id,refuseWhy:refuseWhy};
    	action = "/cms/provide/loanrepay/repairBack";
    	//parent.layer.confirm(bt.lang('common.message.confirm.back'),function(index){parent.layer.close(index);});
        submitAjax(action,resData);
    }else if(refuseWhy.length >50){
    	parent.layer.alert(bt.lang('common.message.config.tooLength'));
    }else{
    	parent.layer.alert(bt.lang('common.message.config.please'));
    }
}

/**加载还款计划*/
function repayLan(){
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),basePath + "/cms/basedata/repaymentschedule/toRepayPlanSave?billCode="+$("#sendCode").val() );
    }
}

/**加载利息预计明细*/
function repayInterest(){
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('cms.button.provideInterest'),basePath + "/cms/provide/cmsprovideinterest/list?sendBillCode="+$("#sendCode").val()+"&interestID="+$("#provideInterestId").val()+"&repairSign=0");
    }
}


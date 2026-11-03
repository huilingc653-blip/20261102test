var form;
bt.use(['form','layedit','dateType','corp','bank','bankInfo','personnelDetail','moneyType','region','selectType','dicts','btUpload'], function(){
    var $ = bt.jquery;
    form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    /**
     * 选择日期回调
     */
    dateCallBack = function (data, $elem) {
        renderBudget();
    }
    var dateType = bt.dateType.init();
    bt.bank.init({elem:'#bankAcc'});
    bt.bankInfo.init();
    var dicts = bt.dicts.init();
    //业务类型选择
    busTypeCallBack = function (data) {
        selectType.get("opType").attr("data-url", basePath + "/expense/nisbusinesstype/getOpType?busType=" + data.bustype);
    }
    var selectType = bt.selectType.init();
    bt.personnelDetail.init();
    var region = bt.region.init();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();
    	$("#status").val($(this).attr("value"));
    	$("#attachmentId").val(up.getGroupId());
    	//判断是否挂接资金计划
    	if($("input[name='sumMoney']").length){
    		var sumMoney = moneyFun.toNumber(moneyType.get("sumMoney").val());
    		var expenseMoney = moneyFun.toNumber(moneyType.get("expenseMoney").val());
            // 校验金额与资金计划总金额一致
            if(sumMoney != expenseMoney){
            	bt.loading.hide();
                layer.alert(bt.lang("netbank.message.budgetMoney"),{icon:2});
                return false;
            }
    	}
        var action = "/expense/nisexpensedetail/modify";
        $.post(basePath + action,$('#inputForm').serialize(),function(result){
        	bt.loading.hide();
            parent.layer.alert(paymentLimitAlert.content(result), paymentLimitAlert.alertOptions(result),function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
    
    //开户行选择
    bt.bankinfoCallback = function(data,dom){
    	$("#bankCode").val(data.bankCode);
    	$("#bankName").val(data.bankName);
    	//设置省市组的值
        region.get('payeeAddr').set([{
            name:'prov',
            defaultValue:data.prov
        },{
            name:'city',
            defaultValue:data.city
        }]);
    }
    
    //职工信息选择
    bt.personnelDetailCallback = function(data,dom){
    	$("#workId").val(data.id);
    	$("#userName").val(data.userName);
    	$("#deptName").val(data.deptName);
    	$("#bifCode").val(data.bifCode);
    	$("#bankCode").val(data.bankCode);
    	$("#bankName").val(data.bankName);
    	$("#cardType").val(data.cardType);
    	$("#bankAcc").val(data.bankAcc);
    	$("#accName").val(data.accName);
    	//设置省市组的值
        region.get('payeeAddr').set([{
            name:'prov',
            defaultValue:data.prov
        },{
            name:'city',
            defaultValue:data.city
        }]);
    }
    
    //单位回调函数
    bt.corpCallback = function(id){
    	bt.personnelDetail.set({
    		corpId:id
    	});
        // 重新设置用途
        var purposeUrl = basePath + "/basic/business/btpurpose?getByListSort&sysBusType=1&busDtl=1&corpId=" + id;
        bt.selectType.get("purpose").attr("data-url", purposeUrl);
    	//资金计划
    	renderBudget();
    }
    bt.corp.init();
    
    var id = $("#corpIdDef").val();
    bt.personnelDetail.set({
		corpId:id
	});

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml=function(){
    	var corpId = $("#corpIdDef").val();
        if($('input[name="corpId"]').val()){
        	corpId = $('input[name="corpId"]').val();
        }
        var itemCode = $('input[name="payItem"]').val();
        var processDate = dateType.get("expenseDate").val();
        var url=basePath+"/budget/fbsBudgetProcess/getEditItemDetail";
        var param={
            corpId:corpId,
            itemCode:itemCode ? itemCode : "400",
            billNo: $("#expenseId").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            processDate:processDate,
            type:'select',
            date:(new Date()).getTime()
        };
        $.ajax({url:url,method: 'POST',data: param,async:true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    };
    
    /**
     * 资金计划挂接
     */
    function renderBudget(){
    	//判断费用报销是否挂接资金计划
    	var budgetSel = $("#budgetSel").val();
    	if(budgetSel == "1"){
    		//判断资金计划挂接环节
	    	var isBudget = $("#expFbsin").val();
	    	if(isBudget == "0"){
	    		$genOtherHtml();
	    	}else{
	    		$("#tagItemDetail").html("");
	    	}
    	}
    }
    
    //资金计划
	renderBudget();

    /**
     * 重置
     */
    resetSelect = function () {
        selectType.get("opType").attr("data-url", basePath + "/expense/nisbusinesstype/getOpType?busType=" + $("#defBusType").val());
    }
});

/**
 * 计划执行输入金额汇总后的回调
 * @param data
 */
function sumMoneyCallBack(data){
    bt.moneyType.get("expenseMoney").val(data);
}

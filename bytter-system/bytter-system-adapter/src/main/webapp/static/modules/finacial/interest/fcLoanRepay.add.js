bt.use(['form','datatable','corp','selectType','moneyType','dateType'], function(){
	$ = bt.jquery,form = bt.form(),moneyFun=bt.moneyType.getFun();
	
	//单位回调
	changeCorp=function(ele){
		seletTypeL.get("loanAcc").attr("data-url",basePath+"/finacial/interest/fcloanrepay/getLoanAccByCorpCode?corpId="+ele);
		seletTypeL.get("loanAcc").render();
		
    	if(ele!== null && ele !== undefined && ele.trim() !== ''){
    		$.ajax({
	    		type: "POST",
	    		url: basePath+"/finacial/interest/fcloanrepay?getDayCloseDate",
	    		data: {"netId":ele},
	    		dataType : "json",
	    		success: function(rs){
	    			dataType.get("tallyDate").attr("data-min",rs.obj);
	    			dataType.get("tallyDate").render();
	    		}
	    	});
    	}
	}
	
	//贷款账号变更回调
	loacAccChange = function(ele){
        reSetFormData();
        if($('#repayAcc').hasClass('first-init')){
            $('#repayAcc').removeClass('init')
                .removeClass('first-init')
                .data('url',basePath+"/finacial/interest/fcloanrepay/getCorpCurAccInfo?loanAcc="+ele.acc);
            bt.selectType.init();
        }else {
            seletTypeL.get("repayAcc").attr("data-url",basePath+"/finacial/interest/fcloanrepay/getCorpCurAccInfo?loanAcc="+ele.acc);
        }
	}
	//还款账号变更回调
	repayAccChange = function(ele){
		if(ele.account){
			$("#account1").val(ele.account);//借方(还款账号)
			$("#account2").val($("#needinterestAcc").val());//贷款(应收利息账号)
			
			$.ajax({
	    		type: "POST",
	    		url: basePath+"/finacial/interest/fcloanrepay?getAccountBalance",
	    		data: {"account":ele.account},
	    		dataType : "json",
	    		success: function(rs){
	    			$("#checkBalance").html(moneyFun.format(rs.obj.checkBalance));
	    			$("#accName1").val(rs.obj.accName);
	    		}
	    	});
		}
	}
	
	//凭证种类变更回调
	voucherTypeChange = function(ele){
		$("#voucherType0").val(ele.id);
		$("#voucherType1").val(ele.id);
	}
	
	//初始化下拉框
	seletTypeL = bt.selectType.init();
	//初始化单位
    bt.corp.init();
    dataType = bt.dateType.init();
    moneyType = bt.moneyType.init();
    
  //监听提交
    form.on('submit(submit)', function(data){
    	var opType=$(this).attr("value");
        var action = "/finacial/interest/fcloanrepay/save";
        data.field.opType = opType;//提交状态
        if(!validateFrom()) return false;
         $.post(basePath + action,data.field,function(result){
        	 parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
             if(result.success){
             	if(!bt.navtab){
                     parent.layer?parent.layer.closeAll():layer.closeAll();
                 } else {
                 	bt.navtab.refreshParent();
                 	bt.navtab.closeCurrent();
                 }
             }
        },'JSON');
        return false;
    });
    
    validateFrom = function(){
    	var interet = $("#interest").val();
		if(interet==null ||interet==""||interet==undefined){
			layer.alert(bt.lang("fc.loanRepay.message.selectRepayDtl"),{icon:5});//请先选择归还明细！
			return false;
		}
		return true;
    }
    
  //关闭窗口
    doClose = function(){
    	if(bt.navtab){
			bt.navtab.closeCurrent();
		}
    }
    
    reSetFormData = function(){
    	$("#accshow").html("");
    	$("#accnameshow").html("");
    	$("#needinterestAccNameShow").html("");
		$("#needinterestAccShow").html("");
		$("#needinterestAcc").val("")
    	moneyType.get("interest").val("");
    	seletTypeL.get("repayAcc").val("");
    	$("#checkBalance").html("");
    	
    	$("#account1").val("");//借方(还款账号)
    	$("#account2").val("");//贷款(应收利息账号)
    	moneyType.get("debitMoney1").val("");
    	moneyType.get("creditMoney2").val("");
    	$("#debitMoneySum").html("");
    	$("#creditMoneySum").html("");
    }
    
	
});
//选择归还明细
function selectDtl(){
	var loanAcc = seletTypeL.get("loanAcc").val();
	if(loanAcc.trim()==""||loanAcc==null||loanAcc=="undifined"){
		layer.alert(bt.lang("fc.loanRepay.message.selectloanAcc"),{icon:5});//请选择贷款账号
		return false;
	}
	parent.layer.open({
        type: 2,
        title:bt.lang('interet.button.selectDtl'),
        area: ['950px', '630px'],
        maxmin: true,
        content: basePath + "/finacial/interest/fcloanrepay/selectRepayDtl?acc="+seletTypeL.get("loanAcc").val(),
        end:function(){
        	
        }
    });
} 


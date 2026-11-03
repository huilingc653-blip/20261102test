bt.use(['form','layedit','selectTree','select','dateType','corp','datatable','bisFinanceExc','dateForm','moneyType','rateType','dicts','radioType'], function(){
    var $ = bt.jquery,form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var rateType =bt.rateType.init();
    var dicts = bt.dicts.init();
    bt.bisFinanceExc.init();
    bt.radioType.init();

    //理财指令选择
    bt.bisFinanceExcCallback = function(data,dom){
    	$('#payerAcc').val(data.payeeAcc);
		$('#payeeAcc').val(data.payerAcc);
		$('#curId').val(data.curId);
		moneyType.get("balance").val(data.timeBalance);
		$("#balanceDiv").text(moneyFun.format(data.timeBalance));
		moneyType.get("payMoney").val(data.payMoney);
		dicts.get('dayLimitSel').val(data.dayLimit+""+data.dateUnit);
		$('#dayLimit').val(data.dayLimit);
		$('#dateUnit').val(data.dateUnit);
		rateType.get('interestRate').val(data.blInterestRate);
        $('#payBillId').val(data.id);
        dateType.get("interestStartDate").val(dateFun.format(data.interestStartDate));
        dateType.get("deadlineDate").val(dateFun.format(data.deadlineDate));
        bt.dicts.get('isOnline').val(data.isOnline);
        $('#bankAccOnline').val(data.bankAccOnline);
        if(data.bankAccOnline == "1"){
            form.setReadonlyForRadio('#isOnlineDiv',false);
        }else {
            //离线账号取款只能是离线
            bt.dicts.get('isOnline').val(data.bankAccOnline);
            form.setReadonlyForRadio('#isOnlineDiv', true);
        }
    }
    
    //格式化金额
    var balanceDiv = $('div[name="balanceDiv"]');
    for(var i=0; i<balanceDiv.length; i++){
    	$(balanceDiv[i]).text(moneyFun.format($(balanceDiv[i]).text()));
    }
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();
    	data.field.status=$(this).attr("value");
        var action = "/chowmatistic/chobankfinance/modify";
        $.post(basePath + action,data.field,function(result){
        	bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2},function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.closeCurrent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
    
    var purposeDef=$("#purposeDef").val(); 
    //单位回调函数
    bt.corpCallback = function(id){
    	//设置定期账号
    	bt.bisFinanceExc.set({corpId:id,busType:"10"});
    	
    	//获取单位用途
    	$.ajax({
            url: basePath+"/basic/business/btpurpose?getPurpose",
            method: 'POST',
            data: {corpId: id,sysBusType:4},
            dataType: 'json',
            success: function(data){
            	$("#purpose").empty();
            	$("#purpose").append("<option value=' '></option><option value='inputText'></option>");
            	$("#purpose").append("<option value="+purposeDef+" selected='selected'>"+purposeDef+"</option>");
            	if(data && data.obj &&  data.obj.length>0){
            		$.each(data.obj,function(index,item){
            			$("#purpose").append("<option value="+item.purpose+">"+item.purpose+"</option>");
            		});
            	}else{
            		$("#purpose").append("<option value=''></option>");
            	}
            	form.render();
            }
        });
    }
    
    //初始化单位
    bt.corp.init();
    
    //初始化页面
	var corpId = $("#corpIdDef").val();

    //设置定期账号
    bt.bisFinanceExc.set({corpId:corpId,busType:"10"});

    //获取单位用途
	$.ajax({
        url: basePath+"/basic/business/btpurpose?getPurpose",
        method: 'POST',
        data: {corpId: corpId,sysBusType:4},
        dataType: 'json',
        success: function(data){
        	$("#purpose").empty();
        	$("#purpose").append("<option value=' '></option><option value='inputText'></option>");
        	$("#purpose").append("<option value="+purposeDef+" selected='selected'>"+purposeDef+"</option>");
        	if(data && data.obj &&  data.obj.length>0){
        		$.each(data.obj,function(index,item){
        			$("#purpose").append("<option value="+item.purpose+">"+item.purpose+"</option>");
        			$("select[name='purpose']").val($("#purposeDef").val());
        		});
        	}else{
        		$("#purpose").append("<option value=''></option>");
        	}
        	form.render();
        }
    });

    function isOnlineShow() {
        //银行账号是否在线
        var bankAccOnline = $("#bankAccOnline").val();
        if(bankAccOnline == "1"){
            form.setReadonlyForRadio('#isOnlineDiv',false);
        }else {
            form.setReadonlyForRadio('#isOnlineDiv', true);
        }
    }
    isOnlineShow();
});

bt.use(['form','layedit','selectTree','select','dateType','corp','datatable','selectTable','dateForm','moneyType','rateType','dicts','radioType'], function(){
    var $ = bt.jquery,form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    bt.rateType.init();
	bt.radioType.init();

    //付款账号
    var payerAccInit = bt.selectTable.init({
      	 elem: "#payerAccTable",
      	 url: basePath+"/chowmatistic/chobankfinance/getBankAcc",
      	 urldata: {corpId: $("input[name='corpId']").val(), busType: $("#busType").val()},
      	 scrollX: true,
      	 scrollY:"200px",
      	 searchInput:"#payerAcc",
         paging:true,
      	 columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
      	 ],
      	 clickCallback:function(data){
      		 if(data){
      			 $('#payerAcc').val(data.bankAcc);
      			 $('#payerAccId').val(data.id);
      			 $('#curId').val(data.curId);
				 //获取账户余额
				 $.ajax({
					 url: basePath + "/bankacc/bisaccbal?getBal",
					 method: 'POST',
					 data: {bankAcc: data.bankAcc,curId: data.curId},
					 dataType: 'json',
					 success: function (data) {
						 moneyType.get("balance").val(data);
						 $("#balanceDiv").text(moneyFun.format(data));
						 form.render();
					 }
				 });
				 $('#bankAccOnline').val(data.isOnline);
				 bt.dicts.get('isOnline').val(data.isOnline);
				 isOnlineShow();
      		 }else{
      			 $('input.text').val('');
      		 }
      	 }
    });
    
    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();
    	data.field.status=$(this).attr("value");
		if($("#isOpenLimit").val()=="1") {
			bankAccLimit(data);
		}else{
			submit(data);
		}
		return false;
    });

	//单位限额
	function corpLimit(data,msg){
		//获取单位本位币换算汇率
		var rate = 0;
		$.ajax({
			url: basePath + "/netbank/nisbillhead/standardRate",
			method: 'GET',
			async: false,
			data: {curId: data.field.curId, corpId: data.field.corpId},
			dataType: 'json',
			success: function (da) {
				rate = bt.money.mul(data.field.payMoney,da,2)
			}
		});
		rate = parseFloat(rate);
		var result=0;
		$.ajax({
			url: basePath + "/netbank/nisbillhead/limitForCorp",
			method: 'GET',
			async: false,
			data: {curId: data.field.curId, payMoney: rate, corpId: data.field.corpId,temp:1},
			dataType: 'json',
			success: function (da) {
				result = da.obj;
			}
		});
		if (result=== 1) {
			msg += '<br>'+bt.lang('alert.over.daylimit.corp')//日累计支付限额
			msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
		} else if(result === 2){
			msg += '<br>'+bt.lang('alert.over.monthlimit.corp')//月累计支付限额
			msg += '<br><br>'+bt.lang('billPool.sign.isContinue')
		}

		if(msg.length>0){
			bt.loading.hide();
			parent.layer.confirm(msg, {icon: 3, title:bt.lang('fbs.budget.input.message.info')},function (index) {
				submit(data);
			});
		}else{
			submit(data);
		}
	}

	//账号限额
	function bankAccLimit(data){
		var result=0;
		$.ajax({
			url: basePath + "/netbank/nisbillhead/limit",
			method: 'GET',
			async: false,
			data: {curId: data.field.curId, payMoney: data.field.payMoney, payerAcc: data.field.payerAcc,temp:1},
			dataType: 'json',
			success: function (da) {
				result = da.obj;
			}
		});
		var msg='';
		if (result=== 1) {
			msg = bt.lang('alert.over.paylimit')//单笔支付限额
		} else if(result === 2){
			msg = bt.lang('alert.over.daylimit')//日累计支付限额
		}else if(result === 3){
			msg = bt.lang('alert.over.monthlimit')//月累计支付限额
		}
		corpLimit(data,msg);
	}

	//提交
	function submit(data){
		var action = "/chowmatistic/chobankfinance/save";
		var payMoney = moneyFun.toNumber(moneyType.get("payMoney").val());
		var balance = moneyFun.toNumber(moneyType.get("balance").val());//账户余额
		var isOnline = bt.dicts.get('isOnline').val();
		if(isOnline == "1" && payMoney > balance){
			bt.loading.hide();
			layer.alert(bt.lang("netbank.message.balance"), {icon: 2});
			return false;
		}else{
			$.post(basePath + action,data.field,function(result){
				bt.loading.hide();
				parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
					parent.layer.closeAll();
					if(result.success){
						if(bt.navtab) {
							bt.navtab.refreshParent();
							bt.navtab.refreshCurrent()
						}else {
							parent.layer ? parent.layer.closeAll() : layer.closeAll();
						}
					}
				})
			},'JSON');
			return false;
		}
	}
    
    //单位回调函数
    bt.corpCallback = function(id){
    	//重新加载付款账号
    	payerAccInit.get("payerAcc").reload({corpId: id, busType: $("#busType").val()});
    	
    	//获取单位用途
    	$.ajax({
            url: basePath+"/basic/business/btpurpose?getPurpose",
            method: 'POST',
            data: {corpId: id,sysBusType:4},
            dataType: 'json',
            success: function(data){
            	$("#purpose").empty();
            	$("#purpose").append("<option value=' '></option><option value='inputText'></option>");
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

    //期限回调函数
    bt.dayLimitCallback = function(dayLimit){
        if(dayLimit.value && dayLimit.value != ""){
            //计算到期日期
            var interestStartDate = dateType.get("interestStartDate").val();
            deadlineDateShow(interestStartDate, dayLimit.value);
        }
    }
    bt.dicts.init();

    //到期日期计算
    function deadlineDateShow(interestStartDate, dayLimit){
		dateType.get("deadlineDate").val(dateFun.format(dateFun.addDay(interestStartDate, dayLimit)));
    }
    deadlineDateShow(dateType.get("interestStartDate").val(), $("select[name='dayLimit']").val());

	bt.interestStartDateCallback = function (data) {
		isOnlineShow();
	}

	function isOnlineShow() {
		//银行账号是否在线
		var bankAccOnline = $("#bankAccOnline").val();
		//起息日期
		var interestStartDate = dateType.get("interestStartDate").val();
		var days = dateFun.getBetweenDay(interestStartDate, dateFun.format(new Date()));
		if(bankAccOnline == "1" && days <= 0){
			bt.dicts.get('isOnline').val(1);
			form.setReadonlyForRadio('#isOnlineDiv',false);
		}else {
			bt.dicts.get('isOnline').val(0);
			form.setReadonlyForRadio('#isOnlineDiv', true);
		}
	}
});

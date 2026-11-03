var payerAccInit;
var payeeAccInit;
var selectType;
var corpId;
bt.use(['form','layedit','selectTree','select','dateType','corp','datatable','selectTable','dateForm','moneyType','rateType','dicts','radioType','selectType','bankInfo','date','btUpload'], function(){
	var $ = bt.jquery,form = bt.form();
	var moneyType = bt.moneyType.init();
	var moneyFun = bt.moneyType.getFun();
	var dateType = bt.dateType.init();
	var dateFun = bt.dateType.getFun();
	var rateType = bt.rateType.init();
	bt.radioType.init();
	selectType = bt.selectType.init();
	bt.corp.init();
	bt.dicts.init();

	var bankInfo = bt.bankInfo.init();
	bt.bankInfoBeforeClick = function () {
		bankInfo.set({
			"joinBankType": true,
			"bankTypeId": selectType.get("bankTypeId").val(),
		});
		return true;
	}
	var up = bt.btUpload.init({
		elem: "#uploader",
		auto: false,
		gid: $("#attachmentId").val()
	});

	//初始化页面
	corpId = $("#corpIdDef").val();
	//格式化金额
	var balanceDiv = $('div[name="balanceDiv"]');
	for(var i=0; i<balanceDiv.length; i++){
		$(balanceDiv[i]).text(moneyFun.format($(balanceDiv[i]).text()));
	}
	//付款账号
	payerAccInit = bt.selectTable.init({
		elem: "#payerAccTable",
		url: basePath+"/chowmatistic/chobankfinance/getBankAcc",
		urldata: {corpId: corpId, busType: $("#busType").val()},
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
				//联动设置银行类别和购买银行
				selectType.get("bankTypeId").val(data.bankTypeId);
				$('#bankName').val(data.bankName);

				$('#payerAcc').val(data.bankAcc);
				$('#payerAccId').val(data.id);
				$('#curId').val(data.curId);
				//获取账户余额
				$.ajax({
					url: basePath + "/bankacc/bisaccbal.do?getBal",
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
				$("#curName").html(data.curName);
				if($("#notRegularRecording").val() == "1" ) {
					payeeAccInit.get("payeeAcc").reload({
						corpId: corpId,
						bankTypeId: selectType.get("bankTypeId").val(),
						bankName: $('#bankName').val(),
						accType: "02"
					});
				}
			}else{
				$('input.text').val('');
			}
		}
	});


	if($("#notRegularRecording").val() == "1" ) {
		//收款账号
		payeeAccInit = bt.selectTable.init({
			elem: "#payeeAccTable",
			url: basePath+"/chowmatistic/chobankfinance/getBankAccByAccType",
			urldata: {corpId: corpId, bankTypeId: $("#bankTypeIdDef").val(), bankName: $('#bankName').val(),accType:"02"},
			scrollX: true,
			scrollY:"200px",
			searchInput:"#payeeAcc",
			paging:true,
			columns: [
				{data:"bankAcc",},
				{data:"accName",},
				{data:"bankName",},
				{data:"curName",}
			],
			clickCallback:function(data){
				if(data){
					$('#payeeAcc').val(data.bankAcc);
					$('#payeeAccId').val(data.id);
				}else{
					$('input.text').val('');
				}
			}
		});
	}

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
			data: {curId: data.field.curId, payMoney: rate, corpId: data.field.corpId,temp:0,id:data.field.id},
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
			data: {curId: data.field.curId, payMoney: data.field.payMoney, payerAcc: data.field.payerAcc,temp:0,id:data.field.id},
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
		$("#attachmentId").val(up.getGroupId());
		data.field.attachmentId = up.getGroupId();
		var action = "/chowmatistic/chobankfinance/modify";
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
				parent.layer.alert(result.msg, {icon: result.success?1:2},function(){
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
		}
	}

	var purposeDef=$("#purposeDef").val();
	//单位回调函数
	bt.corpCallback = function(id) {
		//重新加载付款账号
		payerAccInit.get("payerAcc").reload({
			corpId: id,
			busType: $("#busType").val(),
			bankTypeId: selectType.get("bankTypeId").val(),
			bankName: $('#bankName').val()
		});
		if ($("#notRegularRecording").val() == "1") {
			//重新加载收款款账号
			payeeAccInit.get("payeeAcc").reload({
				corpId: id,
				bankTypeId: selectType.get("bankTypeId").val(),
				bankName: $('#bankName').val(),
				accType: "02"
			});
		}

		//获取单位用途
		$.ajax({
			url: basePath + "/basic/business/btpurpose.do?getPurpose",
			method: 'POST',
			data: {corpId: id, sysBusType: 4},
			dataType: 'json',
			success: function (data) {
				$("#purpose").empty();
				$("#purpose").append("<option value=' '></option><option value='inputText'></option>");
				$("#purpose").append("<option value=" + purposeDef + " selected='selected'>" + purposeDef + "</option>");
				if (data && data.obj && data.obj.length > 0) {
					$.each(data.obj, function (index, item) {
						if (item.purpose !== purposeDef) {
							$("#purpose").append("<option value=" + item.purpose + ">" + item.purpose + "</option>");
						}
					});
				} else {
					$("#purpose").append("<option value=''></option>");
				}
				form.render();
				initPurposeMaxlength();
			}
		});
	}

	//初始化单位
	bt.corp.init();

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
					if(item.purpose !== purposeDef){
						$("#purpose").append("<option value="+item.purpose+">"+item.purpose+"</option>");
					}
				});
				$("select[name='purpose']").val($("#purposeDef").val());
			}else{
				$("#purpose").append("<option value=''></option>");
			}
			form.render();
			initPurposeMaxlength();
		}
	});

	//期限回调函数
	bt.dayLimitCallback = function(dayLimit){
		if(dayLimit.value && dayLimit.value != "" && dayLimit.value != " " ){
			if(dayLimit.value == "0"){
				//期限自定义
				var htl = '<div class="layui-inline">' +
					'<input id="limit" data-name="limit" class="layui-input bt-biz-moneyType" data-decimal="0" data-min="1" data-callback="bt.limitCallback" data-biz-name="limit" />' +
					'</div>' +
					'<div class="bt-biz-dict" data-type="select" data-group="choBankFinance.dayLimitCustom" data-name="unit" data-biz-name="unit" data-callback="bt.unitCallback"></div>'
				$("#nextDetail").html(htl);
				bt.dicts.render();
				bt.selectType.init();
				bt.moneyType.init();

				//去除计息规则限制
				bt.dicts.get('calculationRule').attr({
					'data-displayData':'all'
				});
				$("#dayLimitIsCustom").val(1);
			}else{
				$("#dayLimit").val(dayLimit.value.substr(0,1));
				$("#dateUnit").val(dayLimit.value.substr(1,1));
				$("#dayLimitIsCustom").val(0);

				//计算到期日期
				var interestStartDate = dateType.get("interestStartDate").val();
				deadlineDateShow(interestStartDate, dayLimit.value);

				//删除页面元素
				$("#nextDetail").html("");
				//固定计息规则为月利率
				bt.dicts.get('calculationRule').attr({
					'data-displayData':0
				});
				//修改到期日只读
				$('[data-name="deadlineDate"]').attr("readonly", "readonly");
			}
		}else{
			//去除计息规则限制
			bt.dicts.get('calculationRule').attr({
				'data-displayData':'all'
			});
		}
	}

	//是否上线回调函数
	bt.isOnlineCallback = function(isOnline){
		dealIsOnlineCallback(isOnline.value);
	}

	function dealIsOnlineCallback(isOnline) {
		if(isOnline && isOnline != "0"){
			//去除期限自定义
			bt.dicts.get('dayLimitSel').attr({
				'data-filterData':'0'
			});
		}else{
			//去除期限限制
			bt.dicts.get('dayLimitSel').attr({
				'data-filterData':''
			});
		}
		//删除页面元素
		$("#nextDetail").html("");
		//去除计息规则限制
		bt.dicts.get('calculationRule').attr({
			'data-displayData':'all'
		});
	}

	//期限自定义日期单位回调函数
	bt.unitCallback = function(unit){
		if(unit.value && unit.value != "" && unit.value != " " ){
			if(unit.value != "D"){
				//固定计息规则为月利率
				bt.dicts.get('calculationRule').attr({
					'data-displayData':0
				});
				//修改到期日只读
				$('[data-name="deadlineDate"]').attr("readonly", "readonly");
			}else{
				//固定计息规则为日利率
				bt.dicts.get('calculationRule').attr({
					'data-displayData':'1,2'
				});
				//取消到期日只读
				$('[data-name="deadlineDate"]').removeAttr('readonly');
			}
			//计算到期日期
			var interestStartDate = dateType.get("interestStartDate").val();
			var limit = moneyType.get("limit").val();
			deadlineDateShow(interestStartDate, limit+unit.value);
			$("#dateUnit").val(unit.value);
		}else{
			//去除计息规则限制
			bt.dicts.get('calculationRule').attr({
				'data-displayData':'all'
			});
			//取消到期日只读
			$('[data-name="deadlineDate"]').removeAttr('readonly');
		}
	}

	bt.limitCallback = function(nextValue,elem,preValue){
		//计算到期日期
		var interestStartDate = dateType.get("interestStartDate").val();
		var unit = bt.dicts.get('unit').val();
		$("#dayLimit").val(nextValue);
		deadlineDateShow(interestStartDate, nextValue+unit);
		dealAnticInterest();
	}

	bt.deadlineDateCallback = function(value,$elem){
		//删除页面元素
		$("#nextDetail").html("");

		bt.dicts.get('isOnline').val(0);
		dealIsOnlineCallback("0");

		//期限自定义
		bt.dicts.get('dayLimitSel').val(0);
		var htl = '<div class="layui-inline">' +
			'<input id="limit" data-name="limit" class="layui-input bt-biz-moneyType" data-decimal="0" data-min="1" data-callback="bt.limitCallback" data-biz-name="limit" />' +
			'</div>' +
			'<div class="bt-biz-dict" data-type="select" data-group="choBankFinance.dayLimitCustom" data-name="unit" data-biz-name="unit" data-callback="bt.unitCallback"></div>'
		$("#nextDetail").html(htl);
		bt.dicts.render();
		bt.selectType.init();
		bt.moneyType.init();

		//计算起息日和到期日之间的天数
		var interestStartDate = dateType.get("interestStartDate").val();
		var days = dateFun.getBetweenDay(interestStartDate,value);
		moneyType.get("limit").val(days);
		//期限自定义日期单位为天
		bt.dicts.get('unit').val("D");
		//固定计息规则为日利率
		bt.dicts.get('calculationRule').attr({
			'data-displayData':'1,2',
			'data-value':'1'
		});
		dealAnticInterest();
	}

	bt.calculationRuleCallback = function(value){
		dealAnticInterest();
	}
	bt.dicts.init();
	initPurposeMaxlength();

	//用途输入框长度限制（对应数据库 CHO_BANK_FINANCE.PURPOSE 字段长度 60）
	function initPurposeMaxlength() {
		$("#purpose").next(".layui-form-select").find(".layui-select-title input").attr("maxlength", "60");
	}

	//到期日期计算
	function deadlineDateShow(interestStartDate, dayLimit){
		// var dayLimitNum = dayLimit.substr(0,1) - 0;
		// if(dayLimit == "3M" || dayLimit == "6M"){
		//     dateType.get("deadlineDate").val(dateFun.format(dateFun.addMonth(interestStartDate, dayLimitNum)));
		// }else if(dayLimit == "1Y" || dayLimit == "2Y" || dayLimit == "3Y" || dayLimit == "5Y"){
		//     dateType.get("deadlineDate").val(dateFun.format(dateFun.addYear(interestStartDate, dayLimitNum)));
		// }
		var limit = dayLimit.substr(0,dayLimit.length-1);
		var unit = dayLimit.substr(dayLimit.length-1,1);
		//判断是月利率（对年对月对日），还是日利率
		if(unit === "D"){
			dateType.get("deadlineDate").val(dateFun.format(dateFun.addDay(interestStartDate, limit)));
		}else{
			interestStartDate = dateFun.addDay(interestStartDate, 0);
			var year = interestStartDate.getFullYear();
			var month = interestStartDate.getMonth();
			var day = interestStartDate.getDate();
			if(unit === "Y"){
				year = year + Number(limit);
			}else if(unit === "M"){
				month = month + Number(limit);
			}
			var newDate =  new Date(year, month+1, 0);
			var lastDay =newDate.getDate();
			if(day<=lastDay){
				newDate =  new Date(year, month, day);
			}
			dateType.get("deadlineDate").val(dateFun.format(newDate));
		}
	}

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
			if(bt.dicts.get('isOnline').val() == "") {
				bt.dicts.get('isOnline').val(0);
			}
			form.setReadonlyForRadio('#isOnlineDiv',false);
		}else {
			bt.dicts.get('isOnline').val(0);
			form.setReadonlyForRadio('#isOnlineDiv', true);
		}
		dealIsOnlineCallback(bt.dicts.get('isOnline').val());
	}
	//处理预计利息
	function dealAnticInterest(){
		var payMoney = moneyType.get("payMoney").val();
		var interestRate = rateType.get("interestRate").val();
		var interestStartDate = dateType.get("interestStartDate").val();
		var deadlineDate = dateType.get("deadlineDate").val();
		var calculationRule = bt.dicts.get('calculationRule').val();

		var days;
		var num;
		if(calculationRule !== '' && calculationRule !== ' '){
			if(calculationRule === '0'){
				num = 360;
				days = (bt.date.get(deadlineDate).getFullYear()-bt.date.get(interestStartDate).getFullYear())*360 + (bt.date.get(deadlineDate).getMonth()-bt.date.get(interestStartDate).getMonth())*30;
			}else{
				days = dateFun.getBetweenDay(interestStartDate, deadlineDate);
				if(calculationRule === '1'){
					num = 360;
				}else{
					num = 365;
				}
			}
		}
		var anticInterest = payMoney * interestRate * days / num;
		moneyType.get("anticInterest").val(anticInterest);
	}

	isOnlineShow();
});

// 银行类别回调函数
function bankTypeIdCallback(data) {
	$("#bankName").val('');
	$('#payerAcc').val('');
	$('#payerAccId').val('');
	$('#curId').val('');
	//重新加载付款账号
	payerAccInit.get("payerAcc").reload({corpId: corpId, busType: $("#busType").val(), bankTypeId: selectType.get("bankTypeId").val(), bankName: $('#bankName').val()});
	if($("#notRegularRecording").val() == "1" ) {
		//重新加载收款款账号
		payeeAccInit.get("payeeAcc").reload({
			corpId: corpId,
			bankTypeId: selectType.get("bankTypeId").val(),
			bankName: $('#bankName').val(),
			accType: "02"
		});
	}
}

//购买银行选择回调回调方法
var callbackBuyBank = function (data) {
	$("#bankName").val(data.bankName);
	selectType.get("bankTypeId").val(data.bankTypeId);

	$('#payerAcc').val('');
	$('#payerAccId').val('');
	$('#curId').val('');
	//重新加载付款账号
	payerAccInit.get("payerAcc").reload({corpId: corpId, busType: $("#busType").val(), bankTypeId: selectType.get("bankTypeId").val(), bankName: $('#bankName').val()});
	if($("#notRegularRecording").val() == "1" ) {
		payeeAccInit.get("payeeAcc").reload({
			corpId: corpId,
			bankTypeId: selectType.get("bankTypeId").val(),
			bankName: $('#bankName').val(),
			accType: "02"
		});
	}
}

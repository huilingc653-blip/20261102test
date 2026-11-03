var dicts,moneyFun,form;
bt.use(['form','datatable','corp','moneyType','btUpload','dicts','jquery','dateType', 'selectTable','selectType'], function(){
	$ = bt.jquery,form = bt.form();
	bt.moneyType.init();
	moneyFun = bt.moneyType.getFun();
	selectTypes = bt.selectType.init();//下拉框
	bt.dateType.init();
	var gInfoId = $("#annexNameInfo").val();
    bt.btUpload.init({
        elem: "#uploaderInfo",
        hasUpload: false,
        hasDelete: false,
        isExpand: true,
        gid: gInfoId
    });

	//生成列表
	datatable = bt.datatable.init({
		elem: "#appTable",
		dom: 'Brtlip',
		order: [[3, 'desc']],
		pageSizeOptions:[10, 25, 50, 100, 500],
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath + "/bond/fcbondAddition/addHisDatatable"
		},
		buttons: [
	 		{
				extend: "colvis",
				text: bt.lang('common.button.theadHide'),
				columns: ':not(:first):not(:last)'
			}
		],
		columns:[
			{data: "", bSortable: false, className: 'center',
				render: function (data, type, full, meta) {
					return meta.row+1;
				}
			},
			{data: "createByName", bSortable: false},
			{data: "outBankAcc", bSortable: false},
			{data: "addDate", bSortable: true},
			{
				data: "addMoney", bSortable: false, className: 'textRight',
				render: function (data, type, full, meta) {
					var html = '';
					html = bt.money.format(full.addMoney);
					return html;
				}
			},
			{data: "status", bSortable: false, className: 'center',
				render: function (data, type, full, meta) {
					return bt.dict("common.status",full.status);
				}
			}
		]
	});
    /**
     * 验证数据
     * @returns {Boolean}
     */
    validateFrom = function(){
    	if($("#addMoney").val()<=0){
    		layer.alert(bt.lang("fcBond.bondAdd.addMoneyErro"), {icon:5});//追加金额不能小于0!    
    		return false;
    	}
    	if(dicts.get("isPayed").val()==0){
        	if($("#remark").val()==null||$("#remark").val()==""){//如果未付款，备注不能为空，将作为“用途”写入付款单
        		layer.alert(bt.lang("fcBond.bond.remarkErro"), {icon:5});
        		return false;
        	}	
    	}
    	return true;
    }

	//转出账号
	var outBankAccInit = bt.selectTable.init({
		elem: "#outBankAccTable",
		url: basePath + "/bond/fcbondtype/getOutBankAcc",
		urldata: {corpId: $("#corpId").val(),curId: $("#curId").val()},
		scrollX: true,
		scrollY: "200px",
		searchInput: "#outBankAcc",
		paging: true,
		columns: [
			{data: "bankAcc",},
			{data: "accName",},
			{data: "bankName",},
			{data:"curName",},
			{data:"isOnline",
				render: function ( data, type, full, meta ) {
					return bt.dict("sf_yn",full.isOnline);
				}
			}
		],
		clickCallback: function (data) {
			if (data) {
				if(!data.balance){
					$("#accMoney").val(0);
				}else {
					$("#accMoney").val(data.balance);
				}
				var money = moneyFun.format(data.balance, {separator: ",", decimal: 2});
				$("#accBalance").html(money);
				$("#outBankAcc").val(data.bankAcc);
				$("#isOnline").val(data.isOnline);
				var isOnline = data.isOnline;
				if(isOnline == "1"){
					$(".isPay").show();
				}else {
					$(".isPay").hide();
				}
			} else {
				$("#accBalance").html("0.00");
				$("#accMoney").val("0");
			}
		}
	});


	var purposeDef=$("#purposeDef").val();
	if(purposeDef == undefined){
		purposeDef = "";
	}
	//获取单位用途
	$.ajax({
		url: basePath+"/basic/business/btpurpose?getPurpose",
		method: 'POST',
		data: {corpId: $("#corpId").val(),sysBusType:4},
		dataType: 'json',
		success: function(data){
			$("#purpose").empty();
			$("#purpose").append("<option value=' '></option><option value='inputText'></option>");
			if(data && data.obj &&  data.obj.length>0){
				$.each(data.obj,function(index,item){
					if(item.purpose == purposeDef){
						$("#purpose").append("<option value="+purposeDef+" selected='selected'>"+purposeDef+"</option>");
					}else {
						$("#purpose").append("<option value=" + item.purpose + ">" + item.purpose + "</option>");
					}
				});
			}else{
				$("#purpose").append("<option value=''></option>");
			}
			form.render();
		}
	});

	//监听提交
	form.on('submit', function(data) {
		var action = "/bond/fcbondAddition/saveFcBondAdd";
		var nm = $(this).attr("id");
		if(nm == "submit"){
			data.field.saveType = "1";
		}
		if(!validateFrom()) return false;

		var isOk = checkMoney();
		$(".btns button").attr("disabled",true)
		if(isOk == 0){
			layer.alert(bt.lang("balance.not.enough"));
			$(".btns button").attr("disabled",false);
			return false;
		}else{
			if(isOk == 2){
				parent.layer.confirm(bt.lang('netbank.message.confirm.balance'), function () {
					$.post(basePath + action, data.field, function (result) {
						parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
						bt.loading.hide(); //关闭加载中
						// $(".btns button").attr("disabled",false)
						if (result.success) {
							if (!bt.navtab) {
								parent.layer ? parent.layer.closeAll() : layer.closeAll();
							} else {
								bt.navtab.closeCurrent();
								bt.navtab.refreshParentTable();
							}
						}
					}, 'JSON');
					return false;
				},function () {
					parent.layer ? parent.layer.closeAll() : layer.closeAll();
					$(".btns button").attr("disabled",false);
				});
				return false;
			}else{
				$.post(basePath + action,data.field,function(result){
					parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
					bt.loading.hide(); //关闭加载中
					// $(".btns button").attr("disabled",false)
					if(result.success){
						if(!bt.navtab){
							parent.layer?parent.layer.closeAll():layer.closeAll();
						} else {
							bt.navtab.closeCurrent();
							bt.navtab.refreshParentTable();
						}
					}
				},'JSON');
				return false;
			}
		}
	});

	bt.isPayedCallback = function(data){
		var value = data.value;
		var remark = $('#remark');
		if(value === '0'){
			remark.attr('lay-verify', 'required');
		}else{
			remark.removeAttr('lay-verify');
		}
		form.renderIcon();
	};
	dicts = bt.dicts.init();//字典
	isPayCall();

});

/**判断保证金金额是否大于账号余额*/
function checkMoney(){
	if($("#outBankAcc").val() == ""){
		$("#accMoney").val("");
	}
	var addMoney = $("#addMoney").val();
	var accMoney = $("#accMoney").val();
	var isOnline = $("#isOnline").val();
	var isPayed = dicts.get("isPayed").val();
	if(isOnline == "0" || isPayed == 1){
		if((accMoney-addMoney-0) < 0){
			return 2;
		}
		return 1;
	}
	if( "" == accMoney || (accMoney-addMoney-0) >= 0 ){
		return 1;
	}
	return 0;
}

function isPayCall() {
	var isOnline = $("#isOnline").val();
	var isPayed = $("#isPayedValue").val();
	if(isOnline == "1"){
		$(".isPay").show();
		var remark = $('#remark');
		if(isPayed === '0'){
			remark.attr('lay-verify', 'required');
		}else{
			remark.removeAttr('lay-verify');
		}
		form.renderIcon();
	}else {
		$(".isPay").hide();
	}
	var outBankAccMoney =  $("#outBankAccMoney").val();
	$("#accBalance").html(moneyFun.format(outBankAccMoney));
	$("#accMoney").val(outBankAccMoney);
}
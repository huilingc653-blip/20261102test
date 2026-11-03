bt.use(['form', 'layedit', 'corp', 'select','bank', 'dateType','bankInfo','moneyType', 'itemType','dicts','externalCorpBankAcc','accountTable','selectType','dateType'], function(){
	var $ = bt.jquery,form = bt.form();
	bt.dateType.init();
	bt.corp.init();
	bt.dateType.init();
	bt.bank.init({
		elem:'.bank'
	});
	bt.moneyType.init();
	bt.moneyType.getFun();


	//客商组件
	var externalCorpBankAcc=bt.externalCorpBankAcc.init();

	//加载银行选择组件
	bt.bankInfo.init();

	//所属收款人账户下拉框
	bt.accountTable.init({urldata:{accountType :'2,3'}});

	//格式化数字
	$.each($("input[decimal]"),function(i,item){
		if($(item).attr("decimal")!=""){
		}
	});
	//时间
	$.each($("div[type='date']"),function(i,item){
		bt.btdate.init({
			elem: "[field='"+$(item).attr("field")+"']",
			name: $(item).attr("field"),
			start: $(item).attr("defaultVal"),
			format: "YYYY-MM-DD",
			required:true,
			istime: false
		});
	});
	$.each($("div[type='datetime']"),function(i,item){
		bt.btdate.init({
			elem: "[field='"+$(item).attr("field")+"']",
			name: $(item).attr("field"),
			start: $(item).attr("defaultVal"),
			format: "YYYY-MM-DD hh:mm:ss",
			istime: true
		});
	});

	//选择客商
	bt.bailCorpCallback=function(data,dom){
		var opType=$(dom).attr("opType");
		if(opType){
			//付款人账号信息
			if(opType=='payerExternalClick'){
				$("#payer").val(data.name);
				$("#payerAcc").val(data.externalAcc);
				$('#payerBankNo').val(data.bankSourceCode);
				$('#payerBankName').val(data.bank);
			}
		}
	}

// 选择银行
	bt.bailBankInfoCallback = function(data,dom){
		$("#payerBankNo").val(data.bankCode);
		$("#payerBankName").val(data.bankName);
	}

	//监听保存
	form.on('submit(save)', function(data){
		bt.loading.show();
		$.post(basePath+ "/bill/blankvou/bvpayeecheque/saveOrstorage/save",data.field,function(result){
			if(result.success){
				parent.layer.alert(result.msg, {icon: result.success?1:2});
				// parent.layer?parent.layer.closeAll():layer.closeAll();
				bt.navtab.refreshParentTable();//刷新父页面表格
				location.reload();//重刷页面
			}else{
				parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
			}
			bt.loading.hide();
		},'JSON');
		return false;
	});

	//监听提交
	form.on('submit(submit)', function(data){
		bt.loading.show();
		//对黑白名单进行验证
		var reData=externalCorpCheck({"isControl":"0","name":data.field.payer,"corpId":data.field.corpId});
		bt.loading.hide();
		if(reData.reCode=='3'){
			parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
		}else if(reData.reCode=='2'){
			parent.layer.confirm(reData.msg,function(){
				bvpayeechequeSubmit(data);
			});
		}else{
			bvpayeechequeSubmit(data);
		}
		return false;
	});

});

function bvpayeechequeSubmit(data){
	bt.loading.show();
	$.post(basePath+ "/bill/blankvou/bvpayeecheque/saveOrstorage/check",data.field,function(result){
		if(result.success){
			parent.layer.alert(result.msg, {icon: result.success?1:2});
			// parent.layer?parent.layer.closeAll():layer.closeAll();
			bt.navtab.refreshParentTable();//刷新父页面表格
			location.reload();//重刷页面
		}else{
			parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
		}
		bt.loading.hide();
	},'JSON');
}

// 所属单位回调
function corpCallback(data){
	$("#payee").val(data.corpName);
	bt.accountTable.get("payeeAcc").initCorp(data.id);
}
//收款人选择
function clickCallback(data){
	$('#payeeAcc').val(data.bankAcc);
	$('#payeeBankNo').val(data.bankCode);
	$('#payeeBankName').val(data.bankName);
	$('#curName').html(data.curName);
	$('#curId').val(data.curId);
}
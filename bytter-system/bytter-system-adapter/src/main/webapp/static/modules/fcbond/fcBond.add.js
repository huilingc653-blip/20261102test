var dateTypes, moneyTypes, selectTypes, dicts,dateFun,rateType,moneyFun;
bt.use([ 'form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType',
		'dicts', 'rateType', 'bankInfo', 'btUpload', 'selectTable' ],
		function() {
			$ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();
			dateTypes = bt.dateType.init();//时间
			dateFun = bt.dateType.getFun();
			moneyTypes = bt.moneyType.init();//金额
			moneyFun = bt.moneyType.getFun();
			var moneyFun = bt.moneyType.getFun();
			selectTypes = bt.selectType.init();//下拉框
			dicts = bt.dicts.init();//字典
			rateType = bt.rateType.init();
			bt.bankInfo.init();//联行号

			var gid = $("#annexName").val();
			var up = bt.btUpload.init({
				elem : "#uploader",
				isExpand : true,
				auto : false,
				gid : gid
			});

		    //付款账号
		    var bankAccInit = bt.selectTable.init({
		        elem: "#bankAccTable",
		        url: basePath + "/bond/fcbond/getBondAcc",
		        urldata: {corpId: $("#accCorpId").val()},
		        scrollX: true,
		        scrollY: "200px",
		        searchInput: "#bankAcc",
		        paging: true,
		        columns: [
		            {data: "bankAcc",},
		            {data: "accName",},
		            {data: "bankName",},
		            {data:"curName",}
		        ],
		        clickCallback: function (data) {
		            if (data) {
		                $.ajax({
		                    url: basePath + "/bond/fcbond/queryBondAcc/"+data.id,
		                    method: 'POST',
		                    dataType: 'json',
		                    success: function () { 
		                    	$('#bankAcc').val(data.bankAcc);
		                        $('#accName').html(data.accName);
		                        $('#bankName').html(data.bankName);
		                        $('#bankAccId').val(data.id);
		                        $('#curId').val(data.curId);
                                $('#curName').val(data.curName);
                                $('#rate').val(data.rates == null?0:data.rates);
								var isRegular = dicts.get("isRegular").val();
								if(isRegular == "0") {
									rateType.get('interestRate').val(data.rates);
								}
		                    }, 
		                    error:function(){	
		                        parent.layer.msg(bt.lang("fcbond.add.checkBondAcc"), {icon: 2});
		                        return;
		                        form.render();
		                    }
		                });
						//重新加载存入账号
						outBankAccInit.get("outBankAcc").reload({
							corpId : data.corpId,
							curId : data.curId
						});
						//form.render();
		            } else {
		                $('input.text').val('');
		            }
		        }
		    });
			//单位回调函数
			bt.corpCallback = function(id) {
				//获取单位信息
				$.ajax({
					url : basePath + "/sys/corp/getSysCorp",
					method : 'POST',
					data : {
						id : id
					},
					dataType : 'json',
					success : function(data) {
						if (data.obj) {
							if (data.obj.userAccountCode) {
								$("#accCorpId").val(data.obj.id);
							}
							//重新加载存入账号
							bankAccInit.get("bankAcc").reload({
								corpId : $("#accCorpId").val()
							});
							//清空存入账号信息
							$('#bankAcc').val("");
							$('#accName').val("");
							$('#bankName').val("");
							$("#bankAccId").val("");
                            $('#curId').val("");
                            $('#curName').val("");
						}
						form.render();
					}
				});

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
			};

			var corp = bt.corp.init();
		    /**
		     * 验证数据
		     * @returns {Boolean}
		     */
		    validateFrom = function(){
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
				urldata: {corpId: $("#accCorpId").val(),curId: $("#curId").val()},
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
							$("#outBankAccMoney").val(0);
						}else {
							$("#outBankAccMoney").val(data.balance);
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
						$("#outBankAccMoney").val("0");
					}
				}
			});

			//监听提交
			form.on('submit', function(data) {
				var action = "/bond/fcbond/save";
				data.field.status = $(this).attr("value");
				data.field.attachmentId = up.getGroupId();
				if(!validateFrom()) return false;
				bt.loading.show();
				var isOk = checkMoney();
				if(isOk == 0){
					layer.alert(bt.lang("balance.not.enough"));
					bt.loading.hide();
					return false;
				}else{
					if(isOk == 2){
						parent.layer.confirm(bt.lang('netbank.message.confirm.balance'), function () {
							bt.loading.show();
							$.post(basePath + action,data.field,function(result){
								parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
								bt.loading.hide(); //关闭加载中
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
						},function () {
							parent.layer ? parent.layer.closeAll() : layer.closeAll();
							bt.loading.hide();
						});
						return false;
					}else{
						$.post(basePath + action,data.field,function(result){
							parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
							bt.loading.hide(); //关闭加载中
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
		});


/**是否存入定期*/
function toRegular() {
	var isRegular = dicts.get("isRegular").val();
	if(isRegular == "1"){
		$(".toRegular").show();
		rateType.get("interestRate").attr("lay-verify","required");
		// dateTypes.get("endDate").attr("lay-verify","required");
	}else {
		$(".toRegular").hide();
		rateType.get("interestRate").attr("lay-verify","false");
		// dateTypes.get("endDate").attr("lay-verify","false");
		var rate = $("#rate").val();
		rateType.get('interestRate').val(rate);
	}
	form.renderIcon();
}

/**判断保证金金额是否大于账号余额*/
function checkMoney(){
	if($("#outBankAcc").val() == ""){
		$("#outBankAccMoney").val("");
	}
	var bondMoney = $("#bondMoney").val();
	var accMoney = $("#outBankAccMoney").val();
	var isOnline = $("#isOnline").val();
	var isPayed = dicts.get("isPayed").val();
	if(isOnline == "0" || isPayed == 1){
		if((accMoney-bondMoney-0) < 0){
			return 2;
		}
		return 1;
	}
	if( "" == accMoney || (accMoney-bondMoney-0) >= 0 ){
		return 1;
	}
	return 0;
}

function endDateCallback() {
	var repayDate = dateTypes.get("repayDate").val();
	var endDate = dateTypes.get("endDate").val();
	if (repayDate && endDate) {
		var dayLimit = dateFun.getBetweenDay(repayDate, endDate);
		moneyTypes.get("dayLimit").val(dayLimit);
		dayLimitCallback(dayLimit);
	}
}

//期限回调函数
function dayLimitCallback(dayLimit){
	if(dayLimit && moneyFun.div(dayLimit, 365, 2)<=5){
		var dayLimitVal = '5Y';
		//获取利率
		var curId = $("#curId").val();
		bt.jquery.ajax({
			url: basePath + "/bond/fcbond/getRate?dayLimi=" + dayLimitVal +"&curId="+curId,
			method: 'POST',
			dataType: 'json',
			success: function (data) {
				rateType.get('interestRate').val(data);
				if(data == 0){
					layer.alert(bt.lang("fcbond.not.get.rate"));//定价管理中没有维护定期存款利率
				}
			},
		})
	}
}

function isPayCall() {
	var isOnline = $("#isOnline").val();
	if(isOnline == "1"){
		$(".isPay").show();
	}else {
		$(".isPay").hide();
	}
	var outBankAccMoney =  $("#outBankAccMoney").val();
	$("#accBalance").html(moneyFun.format(outBankAccMoney));
	$("#outBankAccMoney").val(outBankAccMoney);
}


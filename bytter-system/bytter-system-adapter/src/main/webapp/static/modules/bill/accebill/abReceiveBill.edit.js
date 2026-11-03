var corps,dicts,moneyType,dateType,item,rateType,dateFmt,maxBillMoney,newStmIssuofpprInd;
var netId;
var externalCorpBankAcc={};
bt.use(['form','layedit','moneyType','money','select','dicts','corp','element','dateType','selectTable','bankInfo','externalCorpBankAcc','accountTable','bank','itemType','rateType','date'], function(){
    var $ = bt.jquery,form = bt.form();
    corps=bt.corp.init();
    dicts=bt.dicts.init();
    moneyType=bt.moneyType.init();
    dateType=bt.dateType.init();
    item=bt.itemType.init();
    rateType=bt.rateType.init();
    dateFmt=bt.date;
    //纸票最大金额
    maxBillMoney = Number($("#maxBillMoney").val());
    //出票标志
	newStmIssuofpprInd = $("#newStmIssuofpprInd").val()
    externalCorpBankAcc=bt.externalCorpBankAcc.init();
	//设置票据单位
	bt.corp.init().get("corpId").setUrl2("accCorp",$("#applyCorpIdOld").val());
    bt.bankInfo.init();
    bt.accountTable.init({urldata:{accountType :'2,3',corpId:$("#accCorpId").val(),electricBill:'1'}});
    bt.bank.init({
        elem:'.bank'
    })

	// 说明显示隐藏
	showDesc();
	changeNewStmIssuofpprInd({value: newStmIssuofpprInd});

    //初始化
	  setTimeout(function(){
		  //changeFormEle();
          changeBillType();
		  renderBudget();
          loadIsNetByCorpId();
	  },1000);

    //初始化加载票据来源
	var lastid=$("#lastid").val();
	if(lastid){
		var billSource=dicts.get("billSource").val();
		//如果为内部业务,票据来源不能更改
		dicts.get("billSource").attr({'data-displayData':billSource});
	}

	form.verify({
		//校验业务办理日期
		checkBusinessDate:function(){
			var businessDate=$("#businessDate").val();
			var startDate=$("#startDate").val();
			var endDate=$("#endDate").val();
 			if(businessDate&&startDate&&endDate){
			var businessFmt=dateFmt.get(businessDate);
			var startFmt=dateFmt.get(startDate);
			var endFmt=dateFmt.get(endDate);
            if(dateFmt.compare(endFmt,startFmt)>0){ //出票日期必须大于到期日期
            if(!(dateFmt.compare(businessFmt,startFmt)>=0&&dateFmt.compare(endFmt,businessFmt)>0)){
               	 return bt.lang("bill.accebill.abReceiveBill.checkBusinessDate");
		      }
           }
          }
		},
		//校验出票日期,到期日期之差
		checkStartWithEnd:function(){
                var startDate=$("#startDate").val();
                var endDate=$("#endDate").val();
                if(startDate&&endDate){
                var startFmt=dateFmt.get(startDate);
                var endFmt=dateFmt.get(endDate);
                var billFlag=dicts.get("billFlag").val();
                if(dateFmt.compare(endFmt,startFmt)<=0){ //出票日期必须大于到期日期
                    return bt.lang("bill.accebill.abReceiveBill.checkStartWithEnd");
                }
                if(billFlag=='10'){ //纸票不能超过半年
                    var startFmt=dateFmt.addMonth(startFmt, 6);
                    if(dateFmt.compare(endFmt, startFmt)>0){
                        return bt.lang("bill.accebill.billFlag_10");
                    }
                }

                if(billFlag=='11'){ //电票不能超过一年
                        var startFmt=dateFmt.addYear(startFmt, 1);
                        if(dateFmt.compare(endFmt,startFmt)>0){
                            return bt.lang("bill.accebill.billFlag_11");
                        }
                    }
				}
		},
		//校验金额
		checkBillMoney:function(value){
			var billFlag=dicts.get("billFlag").val();
			var billMoney=moneyType.get("billMoney").val();
			if(billFlag=='10'&&Number(billMoney)>maxBillMoney){
				return  parent.layer.alert(bt.lang("table.abVoucherinfo.billMoney.billFlag10",[maxBillMoney]),{icon: 7});
			}
			if(billFlag=='11'&&Number(billMoney)>1000000000){
				return parent.layer.alert(bt.lang("table.abvoucherinfo.billmoney.billflag11"));
			}
			var newStmIssuofpprInd = dicts.get("newStmIssuofpprInd").val();
			var blgrdbagcrclPrmtInd = dicts.get("blgrdbagcrclPrmtInd").val();
			var sbbllRngStrtSn = $("#sbbllRngStrtSn").val();
			var sbbllRngEndSn = $("#sbbllRngEndSn").val();
			// 新系统出票标志才需要验证
			if (newStmIssuofpprInd === '1' && sbbllRngStrtSn && sbbllRngEndSn) {
				// 票据金额计算公式
				var result = (Number(sbbllRngEndSn) - Number(sbbllRngStrtSn) + 1) / 100
				if (blgrdbagcrclPrmtInd == "1" && Number(result) !== Number(billMoney)) {
					return parent.layer.alert(bt.lang("table.abvoucherinfo.billmoney.differenceBillMoney"));
				}
			}
		},
		//校验资金计划总金额
		checkSumMoney:function(){
			 var sumMoney=moneyType.get("sumMoney").val();
			 var billMoney=moneyType.get("billMoney").val();
			 if((bt.money.toNumber(sumMoney)!=bt.money.toNumber(billMoney))){
				//执行总金额与票据金额不一致!
				return bt.lang("common.accebill.fbsBudget.errorMessage");
			}
		},
		//校验票据号是否重复
		checkOrigNo:function(value) {
			var corpId=corps.get("corpId").val();//持票单位
			var vouId=$("#vouId").val();//主信息表id
			var viceId=$("#viceId").val();//从表id
			var sbbllRngStrtSn =$("#sbbllRngStrtSn").val()
			var sbbllRngEndSn =$("#sbbllRngEndSn").val()
			var billMoney =$("#billMoney").val()
			if (value&&corpId&&viceId) {
				var success = false;
				$.ajax({
					type : 'POST',
					contentType :'application/json; charset=UTF-8',
					url : basePath + "/bill/accebill/abreceivebill/checkOrigNo/"+"update",
					data : JSON.stringify({origNo:value,corpId:corpId,vouId:vouId,viceId:viceId,sbbllRngStrtSn:sbbllRngStrtSn,sbbllRngEndSn:sbbllRngEndSn,billMoney:billMoney}),
					async : false,
					dataType :'json',
					success : function(result) {
						if (result.success) {
							success = true;
						}
					}
				});
				if (success) {
					return bt.lang('bill.accebill.abReceiveBill.checkOrigNo');
				}
			 }
		},
		// 校验子票区间结束序号填入规则
		checkSbbllRngEndSn: function(value) {
			var sbbllRngStrtSn = $("#sbbllRngStrtSn").val();
			if (value && sbbllRngStrtSn) {
				if (Number(value) < Number(sbbllRngStrtSn)) {
					return bt.lang('bill.accebill.abReceiveBill.checkSbbllRngEndSn');
				}
			}
		}
	});

    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
    	var opType=$(data.elem).val();
    	var dataInfo=data.field;
		// 出票标志：新系统出票与老系统出票数据不同
		var newStmIssuofpprInd = dataInfo.newStmIssuofpprInd;
		var blgrdbagcrclPrmtInd = dataInfo.blgrdbagcrclPrmtInd;
		var sbbllRngStrtSn = dataInfo.sbbllRngStrtSn;
		var sbbllRngEndSn = dataInfo.sbbllRngEndSn;
		var blStdAmt = dataInfo.blStdAmt;
		var blRmtrUnnIdNo = dataInfo.blRmtrUnnIdNo;
		var blRcvpymtpsUnnIdNo = dataInfo.blRcvpymtpsUnnIdNo;
		var blAcptrUnnIdNo = dataInfo.blAcptrUnnIdNo;
		var blHoldUnnIdNo = dataInfo.blHoldUnnIdNo;
		if (newStmIssuofpprInd === '0') {
			// 老系统出票则重置数据
			blgrdbagcrclPrmtInd = '0';
			sbbllRngStrtSn = '0';
			sbbllRngEndSn = '0';
			blStdAmt = 0;
			blRmtrUnnIdNo = '';
			blRcvpymtpsUnnIdNo = '';
			blAcptrUnnIdNo = '';
			blHoldUnnIdNo = '';
		}
    	//组装数据
    	var abVoucherInfo={newStmIssuofpprInd: newStmIssuofpprInd, blgrdbagcrclPrmtInd: blgrdbagcrclPrmtInd,
						   sbbllRngStrtSn: sbbllRngStrtSn, sbbllRngEndSn: sbbllRngEndSn,
						   blStdAmt: blStdAmt, blRmtrUnnIdNo: blRmtrUnnIdNo,
			               blRcvpymtpsUnnIdNo: blRcvpymtpsUnnIdNo, blAcptrUnnIdNo: blAcptrUnnIdNo,
						   id:dataInfo.vouId,
    			           origNo:dataInfo.origNo,billType:dataInfo.billType,
    			           banendrsmtmk:dataInfo.banendrsmtmk,billFlag:dataInfo.billFlag,
    			           billMoney:dataInfo.billMoney,startDate:dataInfo.startDate,
    			           endDate:dataInfo.endDate,payer:dataInfo.payer,
    			           payeracc:dataInfo.payeracc,payerBankName:dataInfo.payerBankName,
    			           payerbankno:dataInfo.payerbankno,payee:dataInfo.payee,
    			           payeeacc:dataInfo.payeeacc,payeeBankName:dataInfo.payeeBankName,
    			           payeebankno:dataInfo.payeebankno,acceptor:dataInfo.acceptor,
    			           acceptoracc:dataInfo.acceptoracc,acceptorbankno:dataInfo.acceptorbankno,
    			           acceptorBankName:dataInfo.acceptorBankName,
    	                   acontractNo:dataInfo.acontractNo,ainvcnb:dataInfo.ainvcnb,
			               payerCdtRatgs:dataInfo.payerCdtRatgs,payerCdtRatgGgcy:dataInfo.payerCdtRatgGgcy,
			               payerCdtRatgDuedt:dataInfo.payerCdtRatgDuedt}
     var abVoucherinfoVice={id:dataInfo.viceId,corpId:dataInfo.corpId,bankAcc:dataInfo.bankAcc,blHoldUnnIdNo: blHoldUnnIdNo,
    			           bankCode:dataInfo.bankCode,enterStock:dataInfo.enterStock,
    			           cnAccrecords:dataInfo.cnAccrecords,billSource:dataInfo.billSource,
    			           abs:dataInfo.abs,rmk:dataInfo.rmk,insideSing:dataInfo.insideSing,
		 					applyCorpId:dataInfo.applyCorpId,busCorpId:dataInfo.busCorpId,busPersonnel:dataInfo.busPersonnel};
    	  var abAccrecords={id:dataInfo.accId,businessDate:dataInfo.businessDate,bankendrsmtmk:dataInfo.bankendrsmtmk,feemoney:dataInfo.feemoney,
    			           cnAccrecords:dataInfo.cnAccrecords,abs:dataInfo.abs,rmk:dataInfo.rmk,insideSing:dataInfo.insideSing,trader:dataInfo.trader};
    	  var abDiscount={};
    	  if(dataInfo.billSource=='14'){
    	    abDiscount={rpdmk:dataInfo.rpdmk,sttlmmk:dataInfo.sttlmmk,rpdopendt:dataInfo.rpdopendt,
    			            rpdduedt:dataInfo.rpdduedt,discountDate:dataInfo.discountDate,interestRates:dataInfo.interestRates,
    			            daynum:dataInfo.daynum,interest:dataInfo.interest,paydate:dataInfo.paydate};
    	  }
    	//资金计划数据
    	var budgetList=[];
    	//资金计划科目代码信息
      	var itemCodeStr="";
      	var trs = $("#budgetProcessDataTable").children("div[row]");
      	if(trs&&trs.length){
      		$(trs).each(function(m,n){
        		var i=$(n).attr("row");
        		var budgetObj={};
        		budgetObj['itemCode']=item.get("itemCode"+i).val();
        		budgetObj['itemName']=$("#itemName"+i).val();
        		budgetObj['processMoney']=moneyType.get("processMoney"+i).val();
        		budgetObj['exchangeRate']=moneyType.get("exchangeRate"+i).val();
        		budgetObj['processCurMoney']=moneyType.get("processCurMoney"+i).val();
        		if(itemCodeStr){
        			itemCodeStr=itemCodeStr+";"+budgetObj['itemCode'];
        		}else{
        			itemCodeStr=budgetObj['itemCode'];
        		}
        		budgetList.push(budgetObj);
        	});
      	}
		var submitData=JSON.stringify({"abVoucherInfo":abVoucherInfo,
			"abVoucherinfoVice":abVoucherinfoVice,
			"abAccrecords":abAccrecords,
			"abDiscount":abDiscount,
			"budgetList":budgetList,
			"itemCodeStr":itemCodeStr});
		if(opType=="submit"){
			bt.loading.show();
			//对黑白名单进行验证
			var reData=externalCorpCheck({"isControl":"0","name":dataInfo.cnAccrecords,"corpId":dataInfo.corpId});
			bt.loading.hide();
			if(reData.reCode=='3'){
				parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
			}else if(reData.reCode=='2'){
				parent.layer.confirm(reData.msg,function(){
					receiveSubmit(submitData,opType);
				});
			}else{
				receiveSubmit(submitData,opType);
			}
		}else{
			receiveSubmit(submitData,opType);
		}
        return false;
    });

	var receiveSubmit=function(submitData,opType){
		bt.loading.show();
		$.ajax({
			type:'post',
			url: basePath+"/bill/accebill/abreceivebill/modify/"+opType,
			contentType :'application/json; charset=UTF-8',
			data:submitData,
			success:function(result){
				bt.loading.hide();
				parent.layer.alert(result.msg,{icon: result.success?1:2});
				if(result.success){
					//刷新父页面
					bt.navtab.closeCurrent();//关闭当前页
				}
			},
			dataType:'json'
		});
	}

    //前手客商过滤,其他不过滤
    bt.externalCorpBankAccParams = function (dom) {
        //查询所属单位对应的客商
        var opType=$(dom).attr("opType");
        if(opType){
//            if(opType=='cnAccExternalClick'){
//            	if($("#insideSing6101or010").val() || dicts.get("insideSing").val()=="1"){
//            		//客商只加载成员单位
//                    externalCorpBankAcc.set({"corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0","type":"1"});
//				}else{
//                    externalCorpBankAcc.set({"corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0"});
//				}
//            }else
        	var paramsExternalCorp={"corpId": corps.get("corpId").val() ? corps.get("corpId").val() : "0","type":4};
            if(opType=='payerExternalClick'){
                //externalCorpBankAcc.set({});
                externalCorpBankAcc.set(paramsExternalCorp);
            }else
            if(opType=='payeeExternalClick'){
                //externalCorpBankAcc.set({});
                externalCorpBankAcc.set(paramsExternalCorp);
            }else
            // 承兑人
            if(opType=='acceptorExternalClick'){
                externalCorpBankAcc.set(paramsExternalCorp);
            }else{
                externalCorpBankAcc.set(paramsExternalCorp);
            }
        }
        return true;
    }

    //选择客商
    bt.bailCorpCallback=function(data,dom){
    	var opType=$(dom).attr("opType");
    	if(opType){
    		if(opType=='cnAccExternalClick'){
    			$("#cnAccrecords").val(data.name);
    		}
    		if(opType=='payerExternalClick'){
    			$("#payer").val(data.name);
    			$("#payeracc").val(data.externalAcc);
    			$("#payerBankName").val(data.bank);
				$("#blRmtrUnnIdNo").val((data.socCode == null || data.socCode == undefined) ? "" : data.socCode);
				payerbankno = data.bankSourceCode;
				var billType = dicts.get("billType").val();
				if(billType =='10'){
					//为银票时。承兑人默认为出票人银行信息，帐号为0
					$("#acceptor").val($("#payerBankName").val());
					$("#acceptoracc").val("0");
					$("#acceptorBankName").val($("#payerBankName").val());
					$("#acceptorbankno").val(payerbankno);
				}
				if(billType =='11'){
					//为商票时。承兑人默认为出票人信息
					$("#acceptor").val($("#payer").val());
					$("#acceptoracc").val($("#payeracc").val());
					$("#acceptorBankName").val($("#payerBankName").val());
					$("#acceptorbankno").val(payerbankno);
					$("#blAcptrUnnIdNo").val($("#blRmtrUnnIdNo").val());
				}
    		}
    		if(opType=='payeeExternalClick'){
    			$("#payee").val(data.name);
    			$("#payeeacc").val(data.externalAcc);
    			$("#payeeBankName").val(data.bank);
				$("#blRcvpymtpsUnnIdNo").val((data.socCode == null || data.socCode == undefined) ? "" : data.socCode);
    		}
    		// 承兑人
    		if(opType=='acceptorExternalClick'){
    			$("#acceptor").val(data.name);
    			$("#acceptoracc").val(data.externalAcc);
    			$("#acceptorBankName").val(data.bank);
    			$("#acceptorbankno").val(data.bankSourceCode);
				if(dicts.get("newStmIssuofpprInd").val() == 1){
					$("#blAcptrUnnIdNo").val((data.socCode == null || data.socCode == undefined) ? "" : data.socCode);
				}
    		}

    	}
    }

  // 选择银行
    bt.bailBankInfoCallback = function(data,dom){
    	var opType=$(dom).attr("opType");
        var billType=dicts.get("billType").val();
    	if(opType){
    		//前手
    		if(opType=='cnAccBankClick'){
    			$("#cnAccrecords").val(data.bankName);
    		}
    		// 出票人
    		if(opType=='payerBankClick'){
    			$("#payerBankName").val(data.bankName);
    		}
    		// 收票人
    		if(opType=='payeeBankClick'){
    			$("#payeeBankName").val(data.bankName);
    		}
    		// 承兑人
    		if(opType=='acceptorBankClick'){
                if(billType=='10'){
                    $("#acceptor").val(data.bankName);
                }
    			$("#acceptorBankName").val(data.bankName);
    			$("#acceptorbankno").val(data.bankCode);
                $("#acceptoracc").val("0");
    		}
    	}
    }

});

function showDesc() {
	var tip_index1 = 0;
	$('#sbbllRngStrtSn-desc').mouseenter(function () {
		tip_index1 = layer.tips(bt.lang('config.bill.sbbllRngStrtSn.desc'), '#sbbllRngStrtSn-desc', {time: 30000});
	}).mouseleave(function () {
		layer.close(tip_index1);
	});
	var tip_index2 = 0;
	$('#sbbllRngEndSn-desc').mouseenter(function () {
		tip_index2 = layer.tips(bt.lang('config.bill.sbbllRngStrtSn.desc'), '#sbbllRngEndSn-desc', {time: 30000});
	}).mouseleave(function () {
		layer.close(tip_index2);
	});
}


// 变更出票标志时：新系统出票显示相关字段，老系统出票隐藏相关字段
function changeNewStmIssuofpprInd(data){
	var newStmIssuofpprInd = data.value;
	var $ = bt.jquery;
	var form = bt.form();
	// 重置单位
	bt.corp.get("applyCorpId").val('');
	bt.corp.get("corpId").val('');
	bt.corp.get("busCorpId").val('');

	var blggd = $("#blgrdbagcrclPrmtInd").val();
	if (newStmIssuofpprInd === '1') {
		$("#blHoldUnnIdNo").attr("lay-verify","strlength");
		// 出票人统一信用代码
		$("#blRmtrUnnIdNo").attr("lay-verify","strlength");
		// 收票人统一信用代码
		$("#blRcvpymtpsUnnIdNo").attr("lay-verify","strlength");
		// 承兑人统一信用代码
		$("#blAcptrUnnIdNo").attr("lay-verify","strlength");
		if(blggd === "1"){
			$(".drawBillShow2").show();
			$(".drawBillShow").show();
			// 子票区间开始序号 siblings：添加其兄弟姐妹的必填属性
			$("#sbbllRngStrtSn").attr("lay-verify","required|strlength");
			// 子票区间结束序号
			$("#sbbllRngEndSn").attr("lay-verify","required|strlength|checkSbbllRngEndSn");
			// 标准金额
			$("#blStdAmt").attr("lay-verify","required").siblings().attr("lay-verify","required");
			// 票据单位统一识别号码
			$("#blHoldUnnIdNo").attr("lay-verify","strlength");
			// 出票人统一信用代码
			$("#blRmtrUnnIdNo").attr("lay-verify","strlength");
			// 收票人统一信用代码
			$("#blRcvpymtpsUnnIdNo").attr("lay-verify","strlength");
			// 承兑人统一信用代码
			$("#blAcptrUnnIdNo").attr("lay-verify","strlength");
			var sbbllRngStrtSn = $("#sbbllRngStrtSn").val();
			var sbbllRngEndSn = $("#sbbllRngEndSn").val();
			var blStdAmt = moneyType.get("blStdAmt").val();
			if (sbbllRngStrtSn === '0') {
				$("#sbbllRngStrtSn").val('');
			}
			if (sbbllRngEndSn === '0') {
				$("#sbbllRngEndSn").val('');
			}
			if (Number(blStdAmt) === 0) {
				moneyType.get("blStdAmt").val(0.01)
			}
			sbbllRngSnBlur("");
		}else {
			// 老系统出票时，隐藏部分字段，并移除必填以及其他校验属性
			$(".drawBillShow2").hide();
			$(".drawBillShow").show();
			// 子票区间开始序号
			$("#sbbllRngStrtSn").removeAttr('lay-verify');
			// 子票区间结束序号
			$("#sbbllRngEndSn").removeAttr('lay-verify');
			$("#sbbllRngStrtSn").val('0');
			// 子票区间结束序号
			$("#sbbllRngEndSn").val('0');
			//moneyType.get("billMoney").val("");
			// 票据金额输入框删除只读属性
			$("#billMoney").removeAttr("readonly").siblings().removeAttr("readonly");
		}
		bt.dicts.get("billFlag").attr({"data-disabled": true});
		dicts.get("billFlag").val("11");
	} else {
		// 老系统出票时，隐藏部分字段，并移除必填以及其他校验属性
		$(".drawBillShow").hide();
		$(".drawBillShow2").hide();
		// 子票区间开始序号  siblings：删除其兄弟姐妹的必填属性
		$("#sbbllRngStrtSn").removeAttr('lay-verify');
		// 子票区间结束序号
		$("#sbbllRngEndSn").removeAttr('lay-verify');
		// 标准金额
		$("#blStdAmt").removeAttr('lay-verify').siblings().removeAttr('lay-verify');
		// 票据单位统一识别号码
		$("#blHoldUnnIdNo").removeAttr('lay-verify');
		// 出票人统一信用代码
		$("#blRmtrUnnIdNo").removeAttr('lay-verify');
		// 收票人统一信用代码
		$("#blRcvpymtpsUnnIdNo").removeAttr('lay-verify');
		// 承兑人统一信用代码
		$("#blAcptrUnnIdNo").removeAttr('lay-verify');
		// 票据金额输入框删除只读属性
		$("#billMoney").removeAttr("readonly").siblings().removeAttr("readonly");
		bt.dicts.get("billFlag").attr({"data-disabled": false});
	}
	form.render();
	form.renderIcon();
}

function changeblgrdbagcrclPrmtInd(data){
	var blggdd = data.value;
	if(blggdd === "1"){
		$(".drawBillShow2").show();
		// 子票区间开始序号
		$("#sbbllRngStrtSn").attr("lay-verify","required|strlength");
		// 子票区间结束序号
		$("#sbbllRngEndSn").attr("lay-verify","required|strlength|checkSbbllRngEndSn");
		$("#sbbllRngStrtSn").val('');
		// 子票区间结束序号
		$("#sbbllRngEndSn").val('');
	}else {
		// 老系统出票时，隐藏部分字段，并移除必填以及其他校验属性
		$(".drawBillShow2").hide();
		// 子票区间开始序号
		$("#sbbllRngStrtSn").removeAttr('lay-verify');
		// 子票区间结束序号
		$("#sbbllRngEndSn").removeAttr('lay-verify');
		$("#sbbllRngStrtSn").val('0');
		// 子票区间结束序号
		$("#sbbllRngEndSn").val('0');
	}
	moneyType.get("billMoney").val("");
	// 票据金额输入框删除只读属性
	$("#billMoney").removeAttr("readonly").siblings().removeAttr("readonly");
}
//票据金额光标移除事件
function billMoneyBlur(data){
	var $ = bt.jquery;
	var form = bt.form();
	var result = '';
	var billMoney = $("#billMoney").val();
	var blStdAmt = $('[name="blStdAmt"]').val();
	var sbbllRngStrtSn = $("#sbbllRngStrtSn").val();
	// if("" === $("#sbbllRngStrtSn").val() || null == $("#sbbllRngStrtSn").val() || undefined == $("#sbbllRngStrtSn").val() ){
	// 	return;
	// }

	if(Number(billMoney) == 0){
		$("#sbbllRngStrtSn").val("000000000000");
		$("#sbbllRngEndSn").val("000000000000");
		return;
	}
	if (Number(billMoney) != 0 && ("" === sbbllRngStrtSn || null == sbbllRngStrtSn || undefined == sbbllRngStrtSn)){
		$("#sbbllRngStrtSn").val("000000000001");
		sbbllRngStrtSn = $("#sbbllRngStrtSn").val();
	}
	var end = bt.money.sub( bt.money.add(sbbllRngStrtSn,bt.money.div(billMoney,blStdAmt)), 1);
	for (var i = 0; i < 12 - end.toString().length; i++){
		result += '0';
	}
	$("#sbbllRngEndSn").val(result + end);

}
// 子票区间开始/结束序号光标移除事件
function sbbllRngSnBlur(data) {
	var $ = bt.jquery;
	var form = bt.form();
	var result = '';
	// 子票区间开始序号
	var start = $("#sbbllRngStrtSn").val();
	// 子票区间结束序号
	var end = $("#sbbllRngEndSn").val();
	var billMoney = $("#billMoney").val();
	var blStdAmt = $('[name="blStdAmt"]').val();
	if (data === 'start') {
		if (!start) {
			return;
		}
		if (Number(start) === 0) {
			$("#sbbllRngStrtSn").val('000000000001');
			return;
		}
		for (var i = 0; i < 12 - start.length; i++) {
			result += '0'
		}
		$("#sbbllRngStrtSn").val(result + start);
		result = '';
		var endSn = bt.money.sub(bt.money.add($("#sbbllRngStrtSn").val(),bt.money.div(billMoney,blStdAmt)), 1);
		for (var i = 0; i < 12 - endSn.toString().length; i++){
			result += '0'
		}
		$("#sbbllRngEndSn").val(result + endSn);
		end = $("#sbbllRngEndSn").val();
	}
	//这里注释是因为"子票区间结束序号"不能输入，只能通过计算所得
	// else if (data === 'end') {
	// 	if (!end) {
	// 		return;
	// 	}
	// 	if (Number(end) === 0) {
	// 		$("#sbbllRngEndSn").val('000000000001');
	// 		return;
	// 	}
	// 	for (var j = 0; j < 12 - end.length; j++) {
	// 		result += '0'
	// 	}
	// 	$("#sbbllRngEndSn").val(result + end);
	// }
	if (start && end && (Number(end) >= Number(start))) {
		// moneyType.get("billMoney").val((Number(end) - Number(start) + 1) / 100);
		// // 票据金额输入框改成只读
		// $("#billMoney").attr("readonly", true).siblings().attr("readonly", true);
	} else {
		moneyType.get("billMoney").val('');
		// 票据金额输入框删除只读属性
		$("#billMoney").removeAttr("readonly").siblings().removeAttr("readonly");
	}
	form.render();
}

//申请单位回调函数
function applyCorpCallback(data){
	var $ = bt.jquery,form = bt.form();
	if(!data){
		return;
	}
	//设置票据单位
	//bt.corp.init().get("corpId").setUrl2("accCorp",data.id);
	//设置票据单位统一社会信用代码
	var newStmIssuofpprInd = dicts.get("newStmIssuofpprInd").val()
	if (newStmIssuofpprInd === '1') {
		$("#blHoldUnnIdNo").val(data.socCode);
	} else {
		$("#blHoldUnnIdNo").val('');
	}
	//初始化票据单位和业务单位
	initCorp(data.id);
	bt.accountTable.get("bankAcc").initCorp(data.id,"1");
	var billFlag=dicts.get("billFlag").val();
	netId=data.netId;
	isNetChagerEnterStock(data.id,data.netId);
	setBillSource();

	//挂接资金预算
	renderBudget();
}

//初始化票据单位和业务单位
function initCorp(id){
	if(id==undefined || id==null || id==""){
		return
	}
	bt.corp.get("corpId").val(id);
	bt.corp.get("busCorpId").val(id);
}

//持票单位回调函数
function corpCallback(data){
	var $ = bt.jquery,form = bt.form();
	if(!data){
		return;
	}
	bt.accountTable.get("bankAcc").initCorp(data.id,"1");
	//设置票据单位统一社会信用代码
	var newStmIssuofpprInd = dicts.get("newStmIssuofpprInd").val()
	if (newStmIssuofpprInd === '1') {
		$("#blHoldUnnIdNo").val(data.socCode);
	} else {
		$("#blHoldUnnIdNo").val('');
	}
    var billFlag=dicts.get("billFlag").val();
    netId=data.netId;
    isNetChagerEnterStock(data.id,data.netId);
	setBillSource();
	//挂接资金预算
	renderBudget();
}

//判断是否是顶级网点，是的话所属库存不能选择中心代管
function isNetChagerEnterStock(corpId,netIds){
    if(dicts.get("billFlag").val()=="10"){
        if (netIds != null && netIds != "") {
            if (corpId == netIds) {
                dicts.get("enterStock").attr({"data-disabledValue": "11"});
            }else{
                dicts.get("enterStock").attr({"data-disabledValue": ""});
            }
        }
    }
}

function clickCallback(data){
	$('#bankAcc').val(data.bankAcc);
	$('#bankName').val(data.bankName);
    isInSideCorp();
}

//判断是否是网点，是的话则中心库存单选按钮置灰（初始化页面使用）
function loadIsNetByCorpId(){
    if(dicts.get("billFlag").val()=="10" ){
        var corpId=corps.get("corpId").val();
        $.ajax({
            type:'post',
            url:basePath + "/bill/accebill/abreceivebill/isTopNetByCorpId",
            data:{"corpId":corpId},
            async:false,
            dataType:'json',
            success:function(result){
                if(result.success){
                    netId=result.obj.netId;
					dicts.get("enterStock").attr({"data-disabledValue": "11"});
                }else{
                    dicts.get("enterStock").attr({"data-disabledValue": ""});
                }
            }
        });
    }
}

//是否第三方票据类型非商票和银票即为第三方票据类型
function isBillTypeTP(billType){
	if(billType!=10&&billType!=11){
		return true;
	}
	return false;
}

//变更流通介质时,变更表单要素属性
function changeFormEle(){
	var $=bt.jquery,form=bt.form();
	var	 billFlag=dicts.get("billFlag").val();
	var  enterStock=dicts.get("enterStock").val();
    var billSource=dicts.get("billSource").val();
	var billType=dicts.get("billType").val();
		if(billFlag=='10'||isBillTypeTP(billType)){//纸票
            //票据所属账号,持票单位银行名称非必填
            $("#bankAcc").attr("lay-verify","strlength");
            $("#bankName").attr("lay-verify","strlength");
			//出票人账号,开户银行非必填
			$("#payerBankName").attr("lay-verify","strlength");
			$("#payeracc").attr("lay-verify","strlength");
			//收票人账号,开户银行非必填
			$("#payeeBankName").attr("lay-verify","strlength");
			$("#payeeacc").attr("lay-verify","strlength");
			//承兑人账号,开户银行,开户行号非必填
			$("#acceptorBankName").attr("lay-verify","strlength");
			$("#acceptoracc").attr("lay-verify","strlength");
			$("#acceptorbankno").attr("lay-verify","strlength");
		}
		if(billFlag=='11'&&!isBillTypeTP(billType)){
            //票据所属账号,持票单位银行名称必填
            $("#bankAcc").attr("lay-verify","required|strlength");
            $("#bankName").attr("lay-verify","required|strlength");
			//出票人信息必填
			$("#payerBankName").attr("lay-verify","required|strlength");
			$("#payeracc").attr("lay-verify","required|strlength");
			//收票人信息必填
			$("#payeeBankName").attr("lay-verify","required|strlength");
			$("#payeeacc").attr("lay-verify","required|strlength");
			//承兑人信息必填
			$("#acceptorBankName").attr("lay-verify","required|strlength");
			$("#acceptoracc").attr("lay-verify","required|strlength");
			$("#acceptorbankno").attr("lay-verify","required");
		}
		if(billFlag=='10'){
			if(billSource=='12') {
	            dicts.get("enterStock").attr({"data-disabledValue":"11,12"});
            }else{
	            dicts.get("enterStock").attr({"data-disabledValue":""});
	            dicts.get("enterStock").val(enterStock);
			}
            isNetChagerEnterStock(corps.get("corpId").val(),netId);
		}else{
			dicts.get("enterStock").attr({"data-disabledValue":"11,12"});
		}
		if(billSource=='10'){
			$("#cnAccrecords").attr("lay-verify","strlength");
		}else{
			$("#cnAccrecords").attr("lay-verify","required|strlength");
		}
		//设置必填图标
		setBillSource();
		form.renderIcon();
}

//票据类型选择银票时,承兑人隐藏客商按钮
function changeBillType(){
    changeFormEle();
    var billType=dicts.get("billType").val();
    if(billType=='10'){
        $("#acceptorExternal").hide();
    }else{
        $("#acceptorExternal").show();
    }
}

function resetForm(){
	changeNewStmIssuofpprInd({value: newStmIssuofpprInd});
	setTimeout(function(){
		//重置属性
		//changeFormEle();
        changeBillType();
		//加载资金计划
		renderBudget()
	},1000);
}

function clickRpdmk(){
	var rpdmk=dicts.get("rpdmk").val();
	if(rpdmk=='RM00'){
		$("#rpddtTr").hide();
	}
	if(rpdmk=='RM01'){
		$("#rpddtTr").show();
	}
}

function setBillSource(){
var corpId=corps.get("corpId").val();
var billFlag=dicts.get("billFlag").val();
var billSource=dicts.get("billSource").val();
var rpdmk=dicts.get("rpdmk").val();
if(billFlag=='10'&&corpId){
	$.ajax({
		type:'post',
		url:basePath + "/bill/accebill/abreceivebill/isNetByCorpId",
		data:{"corpId":corpId},
		async:false,
		dataType:'json',
		success:function(result){
			if(result.success){
				dicts.get("billSource").attr({"data-filterData":""});
				dicts.get("billSource").val(billSource);
				billSource=dicts.get("billSource").val();
				if(billSource=='14'){	
					$("#discountDiv").show();
					if(rpdmk=='RM01'){
					   $("#rpddtTr").show();	
					}else{
					   $("#rpddtTr").hide();
					}
				}else{
					$("#discountDiv").hide();
				}
			}else{
				dicts.get("billSource").attr({"data-filterData":"14"});
				if(billSource!='14'){
					dicts.get("billSource").val(billSource);
				}
				$("#discountDiv").hide();
			}
		}
	});
}else{
	dicts.get("billSource").attr({"data-filterData":"14"});
	if(billSource!='14'){
		dicts.get("billSource").val(billSource);
	}
	$("#discountDiv").hide();
}
}

function clickBillSource(){
var form=bt.form();
var billSource=dicts.get("billSource").val();
var billFlag=dicts.get("billFlag").val();
var enterStock=dicts.get("enterStock").val();
if(billSource=='14'){
	setBillSource();
}else{
	$("#discountDiv").hide();
}
 if(billSource=='12'){
        dicts.get("enterStock").attr({"data-disabledValue":"11,12"});
    }else{
        if(billFlag=='10'){
            dicts.get("enterStock").attr({"data-disabledValue":""});
            dicts.get("enterStock").val(enterStock);
        }
    }
	if(billSource=='10'){
		$("#cnAccrecords").attr("lay-verify","strlength");
	}else{
		$("#cnAccrecords").attr("lay-verify","required|strlength");
	}
	form.renderIcon();
}

//对外收票判断前手是否是内部成员单位
function  isInSideCorp(){
	var cnAccrecords=$("#cnAccrecords").val();
//    if(dicts.get("insideSing").val()=="0"){
//        $("#bankDiv").show();
//    }else{
//        $("#bankDiv").hide();
//    }
	if(cnAccrecords){
		var insideSing=dicts.get("insideSing").val();
		if(insideSing=="0"){
		$.ajax({
	            type:'post',
	            url:basePath + "/bill/accebill/abreceivebill/getCorpByName",
	            data:{"name":cnAccrecords},
	            async:false,
	            dataType:'json',
	            success:function(result){
	            	if(result.success){
                        //收票方式选择对外时,前手不能为内部成员单位,请了解!
                        return bt.lang("bill.accebill.abreceive.checkCnAccrecords",{icon:7});
                    }
	            }
		  });
	   }
	}
}

//变更业务办理日期时，重新加载数据
function changeBusdt(){
    renderBudget();
}

/**
 * 生成公共的属性
 * @return
 */
$genOtherHtml=function(){
	 var itemCode =$("#recItem").val();
	 var defItemCode =$("#defItemCode").val();
	 var businessName=$("#businessName").val();
	 var processSysname=$("#processSysname").val();
	 var viceId=$("#viceId").val();
	 var curId = $("#curId").val();
	 var corpId=$('input[name="corpId"]').val();
	 var processDate=$("#businessDate").val();
	 var url;
	 var param;
	$.ajax({
		    type:'post',
		    url:basePath+"/budget/fbsBudgetProcess/getBudgetProcesInfo",
		    data:{"businessName":businessName,
		    	  "processSysname":processSysname,
		    	  "billNo":viceId,
				  "billId":viceId
			},
		    async:false,	
		    dataType:'json',
		    success:function(result){
	    	if(result.length&&result[0].corpId==corpId){
	    	    	url=basePath+"/budget/fbsBudgetProcess/getEditItemDetail";
	    	    	param={
	    	    			corpId:corpId,
	    	    			itemCode:itemCode,
                            billId:viceId,
	    	    			businessName: businessName,
	    	    			processSysname:processSysname,
	    	    			processDate: processDate,
	    	    			type:'select',
	    	    			curId:curId,
	    	    			date:(new Date()).getTime()
	    	    	};
	    	}else{
	    		   url=basePath+"/budget/fbsBudgetProcess/getItemDetail";
    			   param={
    					   corpId:corpId,
    					   itemCode:itemCode,
    					   defItemCode:defItemCode,
    					   processDate:processDate,
    					   curId:curId,
    					   type:'select',
    					   date:(new Date()).getTime()
 	    	        };
	           }
           }
	  });
    $.ajax({
    	    type:'post',
    	    url:url,
    	    data:param,
    	    async:false, 
    	    success: function (json) {
    	    $("#budget").show();
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
          //设置执行总金额lay-verify属性
            bt.use('moneyType',function(){
            	bt.moneyType.init().get("sumMoney").attr("lay-verify","checkSumMoney");
            });
        }
    });
};

/**
 * 资金计划挂接
 */
function renderBudget(){
	var corpId=$('input[name="corpId"]').val();
	var businessDate=$("#businessDate").val();
	if(!(corpId&&businessDate)){
		return;
	}
	$.ajax({ 
		type: 'POST',
		url: basePath+"/bill/accebill/abvoucherinfoVice/isBudget", 
		data:{'corpId':corpId,'briefId':'6001'},  
		async: false, 
		dataType:'json',
		success: function(result){
			if(result.success){
				if(result.attributes.isBudget){
					$genOtherHtml(); 
				}else{
					$("#budget").hide();
					$("#tagItemDetail").html("");
				}
			}else{
				$("#budget").hide();
				$("#tagItemDetail").html("");
			}
		}});
}

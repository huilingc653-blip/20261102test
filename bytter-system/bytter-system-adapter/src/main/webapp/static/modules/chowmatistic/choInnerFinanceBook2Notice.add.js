bt.use(['form','layedit','selectTree','select','dateType','corp','datatable','selectTable','dateForm','moneyType','rateType','dicts', 'selectType','choInnerFinance'], function(){
    var $ = bt.jquery,form = bt.form();
    var dateType = bt.dateType.init();
    var dateFun = bt.dateType.getFun();
    var dicts = bt.dicts.init();
    var rateType = bt.rateType.init();

    bt.choInnerFinance.init();

    //存款信息选择
    bt.depositCallback = function(data,dom){
        $('#relatedBillId').val(data.id);
        $('#relatedBillNo').val(data.billNo);
        $('#curId').val(data.curId);
        $('#payerAccId').val(data.payerAccId);
        $('#payerAcc').val(data.payerAcc);
        $('#payeeAccId').val(data.payeeAccId);
        $('#payeeAcc').val(data.payeeAcc);
        $('#capitalAccId').val(data.capitalAccId);
        $('#capitalAcc').val(data.capitalAcc);
        $('#capitalAccName').val(data.capitalAccName);
        $('#interestAccId').val(data.interestAccId);
        $('#interestAcc').val(data.interestAcc);
        $('#interestAccName').val(data.interestAccName);
        $('#dayLimit').val(data.dayLimit);
        $('#dateUnit').val(data.dateUnit);
        $('#depositRateType').val(data.depositRateType);
        dicts.get('noticeTypeShow').val(data.noticeType);
        //dicts.get('certificateWayShow').val(data.certificateWay);
        $('#noticeType').val(data.noticeType);
        //$('#certificateWay').val(data.certificateWay);
        moneyType.get("depositMoney").val(data.payMoney);
        rateType.get('interestRate').val(data.interestRate);
        moneyType.get("payMoney").val(data.payMoney);
        dateType.get("interestStartDate").val(dateFun.format(data.interestStartDate));
        dateType.get("deadlineDate").val(dateFun.format(data.deadlineDate));
        $('#purpose').val(data.purpose);
        //预计利息计算
        setExpectInterest();
    }
    var selectType = bt.selectType.init();

    //到期日期计算
    function setExpectInterest(){
        //预计利息=支取金额*活期利率*（支取日期-起息日期）/365
        var payMoney = moneyType.get("payMoney").val();
        var interestRate = rateType.get('interestRate').val();
        var payDate = dateType.get('payDate').val();
        var interestStartDate = dateType.get("interestStartDate").val();
        var dayLimit = dateFun.getBetweenDay(interestStartDate,payDate);
        var expectInterest = moneyFun.div(moneyFun.mul(moneyFun.mul(payMoney,interestRate),dayLimit),365,2);
        moneyType.get("expectInterest").val(expectInterest);
    }

    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();

    //监听提交
    form.on('submit(submit)', function(data){
    	bt.loading.show();
    	data.field.status=$(this).attr("value");
        var action = "/chowmatistic/choinnerfinance/save";
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
    });
    
    //单位回调函数
    bt.corpCallback = function(id){
        //清空选择的单号
        $('#relatedBillNo').val('');
        //设置通知账号
        bt.choInnerFinance.set({corpId:id,busType:"14"});
    }
    //初始化单位
    bt.corp.init();
    //设置通知账号
    bt.choInnerFinance.set({corpId:"",busType:"14"});
});



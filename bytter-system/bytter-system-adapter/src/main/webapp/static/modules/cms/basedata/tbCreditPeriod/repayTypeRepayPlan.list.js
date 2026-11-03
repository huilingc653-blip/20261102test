var rowNum = 0;
var moneyType = "";
var surplusMoney = "";// 用于计算用的剩余金额
var surplusMoneyFixed = "";// 固定的剩余金额（即除去已还金额，剩余的金额）
var loanDate = "";// 发放日期
var repayDate = "";// 还款日期
var loanMoney = "";// 发放金额
var loanRate = "";// 利率
var repayType = "";// 还款方式
var dateFun;
var dateType;
var repayCycel;
var rateBase = 360;// 利息测算基数
var timeParms;// 等额本金中，控制利率需要的参数
bt.use([ 'form', 'element', 'dateType', 'moneyType', 'date', 'bttable', 'dicts'], function() {
    $ = bt.jquery, form = bt.form(), moneyFun = bt.moneyType.getFun();
    moneyType = bt.moneyType.init();
    dicts = bt.dicts.init();
    // 加载
    initData();
    form.on('submit(save)', function(data) {
        bt.loading.show();
        if (verification() == false) {
            bt.loading.hide();
            return false;
        }
        var action = "/cms/basedata/repaymentschedule/save";
        $.post(basePath + action, data.field, function(result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {
                icon : result.success ? 1 : 2
            });
            if (result.success) {
                parent.layer ? parent.layer.closeAll() : layer
                        .closeAll();
                var webObj = bt.navtab.getParentDom();
                // 给父级选项卡页面中id为test的input赋值;
                webObj.bt.jquery("#planId").val(result.obj);
                webObj.bt.jquery("#planStatus").val("0");
                bt.navtab.closeCurrent("false");
            }
        }, 'JSON');
        return false;
    });

});

/**
 * 还款周期改变事件
 *
 * @param data
 */
function cycleChange(data) {
	// 还款方式为一次性还款就返回
	if (data.value == "3")
		return;
	repayCycel = data.value;
	// 若没有选择还款开始日则提示选择日期
	if (isNull($('#repayBeginDate').val())) {
		parent.layer.alert(bt.lang("cms.RepaymentSchedule.noSelectBeginDate"));
		return;
	}

	loadData();
}

bt.use([ 'dateType' ], function() {
	dateType = bt.dateType.init();
	dateFun = dateType.getFun();
})

// 重新渲染日期、金额控件
function addFun() {

	// 重新渲染datatype和moneyType
	moneyType.render();
	dateType.render();
	sumMoney();

	// +1为了以防id重复
	rowNum++;
}
/**
 * 判断第一个字符串是否包含第二个字符串
 *
 * @param str
 * @param substr
 * @returns {Boolean}
 */
function isContains(str, substr) {
	return str.indexOf(substr) >= 0;
}
/**
 * 初始化数据
 */
function initData() {
	loanDate = $("#loanDate").val();// 发放日期
	repayDate = $("#repayDate").val();// 还款日期
	loanMoney = $("#loanMoney").val();// 发放金额
	loanRate = Number($("#loanRate").val())// 利率
	surplusMoney = Number(loanMoney).toFixed(2);// 剩余本金（初始化为发放金额）
	surplusMoneyFixed = Number(loanMoney).toFixed(2);// 剩余本金（初始化为发放金额）
	repayType = $("#repayType").val();// 还款方式
	if (!isNull($("#rateBase").val())) {
		rateBase = Number($("#rateBase").val());
	}
	var planId = $("#planId").val();
	// 还本付息 （只有这种方式刚开始就需要加载数据）
	if (repayType == "01") {
		$("#repayBeginDate").attr("disabled", "true");
		$("#repayBeginDate").removeAttr("lay-verify");
		//还本付息去掉计算按钮 2023/9/22
		$('#calcBtn').hide()
		// add(repayDate,);
		var payDate = repayDate;
		var payMoney = loanMoney;
		dicts.get('repayCycel').attr({
			'data-value' : 3,
			'data-disabledValue' : '0,1,2'
		});
		addOneOffPayment(payDate, payMoney, loanDate);
	} else {
		dicts.get('repayCycel').attr({
			'data-value' : '0',
			'data-disabledValue' : '3'
		});
		repayCycel = "0";
		$("#repayBeginDate").attr("lay-verify", "required");
	}
	// 需要回显
	if (!isNull(planId)) {
		// 非还本付息
		if (repayType != "01") {
			$.ajax({
                type : 'POST',
                url : basePath + "/cms/basedata/repaymentschedule/getRepayMentScheduleById",
                data : {'planId' : planId},
                async : false,
                dataType : 'json',
                success : function(result) {
                    if (result.success) {
                        repayCycel = result.attributes.repayCycel;
                        dateType.get('repayBeginDate').val(
                                result.attributes.beginDate);
                        dicts.get('repayCycel').attr({
                            'data-value' : repayCycel
                        });
                        loadData();
                    }
                }
            });
		}

	}
	dicts.render();
}
function beginDateChange() {
	if (isNull(dicts.get('repayCycel').val())) {
		return;
	}
	loadData();
}
/**
 * 还款方式为02,04时，加载的数据(通过点击还款周期加载数据)
 */
function loadData() {
	var payMoney = new Array();// 还款本金数组
	var payDate = getDates();// 还款日期数组/
	if (!payDate) {
		parent.layer.alert(bt.lang("cms.RepaymentSchedule.noSelectBeginDate"));
		return;
	}
	// console.log('***********************');
	// $.each(payDate, function(i,n){
	// var date=dateFun.format(n, "yyyy-mm-dd");
	// console.log(date);
	// })
	// 删除之前的table数据
	$("#repayPlanTB tr.dataRow").remove();
	$("#repayPlanTB").find('tr[data-rownum]').remove();
	// 分期等额本金
	if (repayType == "02" || repayType == "05") {
		var money = (loanMoney / (payDate.length)).toFixed(2);// 还款本金
		for (var i = 0; i < payDate.length; i++) {
			payMoney.push(money);
		}
		var payInterest = true;
		if(repayType == "05") {
            payInterest = false;
        }
		addEqualPrincipal(payDate, payMoney, payInterest);
	}
	// 分期等额本息
	if (repayType == "04") {
		addEqualPrincipalInterest(payDate);
	}
}

/**
 * 合计
 */
function sumMoney() {
	// 计算还款本金
	var repaymentMoneySum = 0;
	$("input[divName^='repaymentMoney']").each(function(i) {
		repaymentMoneySum = bt.money.add(repaymentMoneySum, $(this).val());
	});
	moneyType.get("repaymentMoneySum").val(loanMoney);

	// 计算利息
	var repaymentIstSum = 0;
	$("input[divName^='repaymentIst']").each(function(i) {
		repaymentIstSum = bt.money.add(repaymentIstSum, $(this).val());
	});
	moneyType.get("repaymentIstSum").val(repaymentIstSum);

	// 计算还款金额
	var currentRepayMoneySum = 0;
	$("input[divName^='currentRepayMoney']").each(
			function(i) {
				currentRepayMoneySum = bt.money.add(currentRepayMoneySum, $(
						this).val());
			});
	moneyType.get("currentRepayMoneySum").val(currentRepayMoneySum);beginDateChange

}

function clickEvent(value) {
	loadData();
}

/**
 * 关闭窗口
 */
function doClose() {
	if(bt.navtab){
		bt.navtab.closeCurrent("false");
	}
}

/**
 * 提交时验证()
 */
function verification() {
	var type = repayCycel;
	if (repayType != '01') {
		if (isNull(repayCycel)) {
			parent.layer.alert(bt.lang("common.message.cycle"));
			return false;
		}
	}
	return true;
}

/**
 * 判断是否null
 *
 * @param data
 */
function isNull(data) {
	return (data == "" || data == undefined || data == null) ? true : false;
}

var dateTypes, moneyTypes, selectTypes, selectTable, dicts, rateTypes;
bt.use(['form', 'jquery', 'corp', 'dateType', 'moneyType', 'selectType', 'selectTable', 'dicts', 'rateType', 'btUpload', 'bankInfo', 'btImport'], function () {
    $ = bt.jquery, form = bt.form(), fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    bt.corp.init();//单位
    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典
    rateTypes = bt.rateType.init();//利率
    bt.bankInfo.init();//联行号
    moneyFun = bt.moneyType.getFun();

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    var initSendDate = $("#initSendDate").val();
    setMinDate(initSendDate);//设置到期日期的最小值

    var gid = $("#annexName").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        hasDownload: true,
        gid: gid
    });

    //预算控制
    renderBudget();//银行借款发放

    initBankAccTable();

    /**选择帐号的下拉列表初始化*/
    function initBankAccTable() {
        var corpId = $("#corpId").val();
        var curId = $("#curCode").val()
        var bankCode = $("#bankId").val()
        selectTable = bt.selectTableType.init({
            elem: ".onlinePaymentAcc",
            type: 'get',
            paging: true,
            url: basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprId?corpId=" + corpId + "&curId=" + curId + "&bankCode=" + bankCode,
            columns: [
                {data: "bankAcc", title: bt.lang("table.btBankAcc.bankAcc")},
                {data: "accName", title: bt.lang("table.btBankAcc.accName")},
                {data: "bankName", title: bt.lang("table.btBankAcc.bankName")},
                {data: "curName", title: bt.lang("international.payment.curName")}
            ],
            clickCallback: function (severData, data) {
                $("#onlinePaymentAcc").val(severData.bankAcc);
                $("#onlineAccName").val(severData.accName);
                $("#onlineBankName").val(severData.bankName);
                $("#onlinePaymentAccId").val(severData.id);
                selectTypes.get("curId").val(severData.curId);
                $("#curId").val(severData.curId);
            },
            handleDeleteCallback: function () {
                clearOnlineAcc();
            }
        });


    }

    // 银团信息
    if (plClass == "1000") {
        $("#syndicatedShow").show();
        initSyndicatedSend()
    }
    //监听提交
    form.on('submit', function (data) {
        // 发放日期检查
        if (!checkSendDate()) {
            return false;
        }
        var action = "/cms/provide/loansend/tempOrSubmit";
        var nm = $(this).attr("id");
        var saveType = "0";
        if (nm == "submit") {
            saveType = "1";
        }
        // 银团信息校验
        if (plClass === "1000") {
            var trs = $("#dataTableSy").children("tbody").children("tr");
            if (trs.length == 0) {
                parent.layer.alert(bt.lang("cms.loan.provideSyndicatedDtl.notExists"), {icon: 7});
                return false;
            }
        }

        //判断发放日期和到期日期
        var repayDate = dateTypes.get("repayDate").val();
        var sendDate = dateTypes.get("sendDate").val();
        if (repayDate < sendDate){
            parent.layer.alert(bt.lang("cms.precative.sendate.pass.deadlinedate"), {icon: 7});
            return false;
        }

        bt.loading.show();
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            // var tagHtml=$("#tagItemDetail").html()
            // tagHtml=tagHtml.replace(/[\r\n]/g,"");//没有上报返回得json内容是/r/n
            if ($("input[name='sumMoney']").length) {
                //判断是否挂接资金计划
                var sumMoney = fun.toNumber(moneyTypes.get("sumMoney").val());
                // 校验金额与资金计划总金额一致
                if (fun.toNumber(sumMoney) != fun.toNumber(data.field.money)) {
                    bt.loading.hide();
                    layer.alert(bt.lang("provide.loansend.message.budgetMoney"), {icon: 2});
                    return false;
                }
            }
        }
        // 附件
        data.field.uploadId = up.getGroupId();
        data.field.saveType = saveType;
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    bt.navtab.refreshCurrentTable();
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                }
            } else {
                bt.loading.hide();
            }
        }, 'JSON');
        return false;
    });

    //获取利率
    $("#getRate").click(function () {
        getRate();
    });

});

//清空还款账号信息
function clearOnlineAcc() {
    $("#onlinePaymentAcc").val("");
    $("#onlineAccName").val("");
    $("#onlineBankName").val("");
    $("#onlinePaymentAccId").val("");
}

//获取利率
function getRate() {
    var data = selectTypes.get("repayCycle").currentObject.find("option:selected").data();//获取币别选择的全部数据

    if (bt.empty(data.rate)) {
        parent.layer.msg(bt.lang("cms.precative.no.rate"), {icon: 2});
        return;
    }

    var pointsProportionSelect = dicts.get("pointsProportionSelect").val();//浮动比例或点数类型
    var pointsProportionText = moneyTypes.get("pointsProportionText").val();//浮动点数或比例值

    if (pointsProportionText != "") {
        if (pointsProportionSelect == "01") {//点数
            rateTypes.get("plRate").val(data.rate + (pointsProportionText / 100));//使用百分比  不使用万分比
            return;
        } else {
            rateTypes.get("plRate").val((data.rate) * (1 + (pointsProportionText / 100)));
            return;
        }
    }
    if (bt.empty(data.rate)) {
        rateTypes.get("plRate").val(0);
    } else {
        rateTypes.get("plRate").val(data.rate);
    }
}

/**浮动方式选择月、季、半年、年生产对应的详情选择 用来计算利率生效日期*/
function setNextDetail() {
    var floatWay = dicts.get("floatWay").val();
    var rateData = $("#rateEntriesEffectDate");//利率生效日期控制
    rateData.attr("readonly", false);
    rateData.attr("lay-verify", "required");
    var postfix = "";
    var htl = '';
    var flag = false;
    if (floatWay == "01") {//固定利率
        rateData.val("");
        rateData.attr("readonly", true);
        rateData.attr("lay-verify", "");
        flag = true;
    } else if (floatWay == "02") {//即时浮动
        $("#rateEntriesEffectDate").val(dateTypes.get("sendDate").val());
    } else if (floatWay == "03") {//按月浮动
        postfix = "Month"
    } else if (floatWay == "04") {//按季浮动
        postfix = "Season"
    } else if (floatWay == "05") {//按半年浮动
        postfix = "HY"
    } else if (floatWay == "06") {//按年浮动
        postfix = "Year"
    }

    if (postfix != "") {
        htl = '<div class="bt-biz-dict" data-name="floatWayNext" data-type="select" data-biz-name="floatWayNext" data-callback="setRateEntriesEffectDate" data-group="Cms.FloatWay' + postfix + '" ></div>';
    }
    $("#nextDetail").html(htl);
    bt.dicts.render();
    //disabledLoanRate(flag);//暂时不需要控制固定利率不能选择浮动方式
    dateTypes.get("rateEntriesEffectDate").reset();
}

/**根据浮动方式详情设置利率调整生效日期*/
function setRateEntriesEffectDate() {
    var floatWay = dicts.get("floatWay").val();
    if (floatWay == "03") {//按月浮动
        nextFloatWay(1);
    } else if (floatWay == "04") {//按季浮动
        nextFloatWay(3);
    } else if (floatWay == "05") {//按半年浮动
        nextFloatWay(6);
    } else if (floatWay == "06") {//按年浮动
        nextFloatWay(12);
    }
}

/**基准利率加载完成后 加载利率周期*/
function loanRepayCycle() {
    plDate = $.trim(bt.dateType.init().get("sendDate").val());//借款申请日期
    if (bt.empty(plDate)) {
        plDate = $("#createDate").val();//为空默认录入日期
    }

    var naturalsign = $("#naturalSign").val();
    if (naturalsign == "1") {//本位币
        var rateType = selectTypes.get("referenceRateType").getData();
        var code = rateType.obj.code;
        if (code == "lpr") {
            $("#inputMark").text("BP");
            $("#deferred").show();
            $("#overdue").show();
        } else {
            $("#inputMark").text("%");
            $("#deferred").hide();
            $("#overdue").hide();
        }
    }

    var referenceRateType = selectTypes.get("referenceRateType").val();//基准利率类型
    var curId = $("#curCode").val();//币别

    if (bt.empty(curId)) {
        selectTypes.get("repayCycle").attr("data-url", "");
    } else {//本位币
        selectTypes.get("repayCycle").attr("data-url", basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + plDate + "&curId=" + curId + "&baseId=" + referenceRateType);
    }
}

/**加载浮动利率的周期信息*/
function loadrepayCycle() {
    var data = selectTypes.get("repayCycle").currentObject.find("option:selected").data();//获取币别选择的全部数据
    var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
    var sel = dicts.get("pointsProportionSelect").val();
    if (data.isbaseloanrate == "0") {
        dicts.get("pointsProportionSelect").val("01");
        if (sel != "01") {
            pointsProportionTextObj.val(0);
        }
    }
    if (data.isbaseloanrate == "1") {//本位币
        dicts.get("pointsProportionSelect").val("02");
        if (sel != "02") {
            pointsProportionTextObj.val(0);
        }
    }

    //设置text的输入范围
    pointsProportionTextObj.attr("data-min", data.ratedownlimit);
    pointsProportionTextObj.attr("data-max", data.rateuplimit);
    pointsProportionTextObj.reset();
}

/**处理生效日期*/
function nextFloatWay(count) {
    var plDate = $.trim(dateTypes.get("sendDate").val());

    var expiryDate = dicts.get("expiryDate").val();
    var floatWayNext = dicts.get("floatWayNext").val();
    var date = getFloatDate(plDate, expiryDate, floatWayNext, true, count);
    $("#rateEntriesEffectDate").val(date);

}

//选择发放日期回调函数
function computeplTerm1(ele, data) {
    computeplTerm(ele, data);
    var sendDate = $.trim(dateTypes.get("sendDate").val());
    setMinDate(sendDate);
    setRateEntriesEffectDate();//日期变动之后修改浮动生效日期
}

function setMinDate(sendDate) {
    //重新选择发放日期时设置到期日期最小值
    var minDate = bt.date.format(bt.date.addDay(sendDate, 1), 'yyyy-MM-dd');//到期日期要大于发放日期
    dateTypes.get("repayDate").attr("data-min", minDate);
    var sendRepayDate = $("#sendRepayDate").val();
    if (!bt.empty(sendRepayDate)) {
        if (bt.date.getBetweenDay(bt.date.format(sendRepayDate, 'yyyy-MM-dd'), bt.date.format(sendDate, 'yyyy-MM-dd')) == 0) {//到期日期等于发放日期
            dateTypes.get("repayDate").val(bt.date.format(bt.date.addDay(sendRepayDate, 1), 'yyy-MM-dd'));//到期日期在原来的基础上+1天
        } else {
            dateTypes.get("repayDate").val(sendRepayDate);
        }
    }
    dateTypes.render();
}

/**根据借款日期 - 还款日期 - 期限单位 计算借款期限*/
var historySendDate = null;

function computeplTerm(ele, data) {
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();//还款日期
    var sendDate = $.trim(dateTypes.get("sendDate").val());
    var repayDate = $.trim(dateTypes.get("repayDate").val());//到期日期
    var closeDay = $("#closeDay").val();
    var loanSendOutType = $("#loanSendOutType").val();
    if (sendDate !== "") {
        if (bt.date.compare(sendDate, startDate) < 0 && bt.date.compare(sendDate, closeDay) && bt.date.compare(sendDate, repayDate)) {//发放日期不能小于开始日期&&大于已日结日期
            dateTypes.get($(data).attr("data-biz-name")).val("");
            dateTypes.get($(data).attr("data-biz-name")).render();
            layer.tips(bt.lang('cms.loanSend.dateWarn'), '#' + $(data).attr("data-biz-name"));
            return false;
        }
    }
    // if(loanSendOutType == 1){//一次性发放
    // 	if(repayDate !==""){
    // 		if(bt.date.compare( endDate , repayDate ) < 0){//到期日期应小于等于结束日期
    // 			dateTypes.get($(data).attr("data-biz-name")).val("");
    // 			dateTypes.get($(data).attr("data-biz-name")).render();
    // 			layer.tips(bt.lang('cms.dueDate.warn'), '#'+$(data).attr("data-biz-name"));
    // 			return false;
    // 		}
    // 	}
    // }

    if (historySendDate != sendDate) {
        renderBudget();
    }
    historySendDate = sendDate;
    var repayDate = $.trim(dateTypes.get("repayDate").val());
    var plTerm;
    if (bt.empty(sendDate) || bt.empty(repayDate)) {
        plTerm = "";
    } else {
        var ternUnit = $.trim($("#ternUnit").val());
        if (!bt.empty(ternUnit)) {
            plTerm = dataSubtract(sendDate, repayDate, ternUnit);
        } else {
            //为空或者null默认月处理
            plTerm = dataSubtract(sendDate, repayDate, "M");
        }
    }
    $("#plTerm").val(plTerm);
    /*loanRepayCycle();*/
    checkRepayLanPrameter();
}

/**计算data1 与 data2 相差多少param单位 data为字符串*/
function dataSubtract(data1, data2, param) {
    var dates;
    if (param == "D") {
        dates = Math.abs((new Date(data1).getTime() - new Date(data2).getTime())) / (1000 * 60 * 60 * 24);
    } else {
        var start = new Date(data1);
        var end = new Date(data2);

        var addMonth = end.getMonth() - start.getMonth();//月份差

        var startLastDay = mGetDate(start.getYear(), start.getMonth());//开始时间当前月总共多少天
        var endLastDay = mGetDate(end.getYear(), end.getMonth());//结束时间当前月总共多少天

        if (startLastDay == start.getDate()) {//开始日期月末最后一天
            if (endLastDay == end.getDate()) {//结束日期月末最后一天
                addMonth++;//月份+1
            } else {
                if (end.getDate() > start.getDate()) {//结束日期当月天数 > 开始日期单月天数
                    addMonth++;//月份+1
                }
            }
        } else {
            if (endLastDay == end.getDate()) {//结束日期月末最后一天
                addMonth++;//月份+1
            } else {
                if (end.getDate() > start.getDate()) {//结束日期当月天数 > 开始日期单月天数
                    addMonth++;//月份+1
                }
            }
        }

        dates = (end.getYear() - start.getYear()) * 12 + addMonth;
    }
    return dates;
}

function mGetDate(year, month) {
    var d = new Date(year, month, 0);
    return d.getDate();
}

/**显示计息日和预提日*/

/*function shouExpiryAccrued() {
    var interestWay = dicts.get("interestWay").val();//计息方式
    if (bt.empty(interestWay)) {
        $("#showExpiryDate").html("");
        $("#showAccruedDailyDate").html("");
    } else {
        if (interestWay != "5") {
            $("#showExpiryDate").html(bt.lang('table.cmsProvideLoanSend.expiryDate') + '<div class="bt-biz-dict" data-width="130px" data-name="expiryDate" data-type="select" data-biz-name="expiryDate" data-group="bisGatherPolicy.month" data-callback="setRateEntriesEffectDate" data-defaultvalue="21"></div>');
        } else {
            $("#showExpiryDate").html("");
        }
        if (interestWay == "1" || interestWay == "4") {
            $("#showAccruedDailyDate").html(bt.lang('table.cmsProvideLoanSend.accruedDailyDate') + '<div class="bt-biz-dict" data-width="130px" data-name="accruedDailyDate" data-type="select" data-biz-name="accruedDailyDate" data-group="bisGatherPolicy.month" data-defaultvalue="21"></div>');
        } else {
            $("#showAccruedDailyDate").html("");
        }
    }
    bt.dicts.render();
}*/

/**检查还款计划参数 是否发生变化*/
function checkRepayLanPrameter() {
    var loanRate = rateTypes.get("plRate").val();//借款利率
    var loanDate = dateTypes.get("sendDate").val();//借款日期
    var repayDate = dateTypes.get("repayDate").val();//还款日期
    var repayType = dicts.get("repayType").val();//还款方式
    var loanMoney = moneyTypes.get("money").val();//金额

    if (beforeLoanRate != loanRate || beforeLoanDate != loanDate || beforeRepayDate != repayDate || beforeRepayType != repayType || beforeLoanMoney != loanMoney) {
        $("#planStatus").val("1");
    }
    beforeLoanRate = loanRate;
    beforeLoanDate = loanDate;
    beforeRepayDate = repayDate;
    beforeRepayType = repayType;
    beforeLoanMoney = loanMoney;

    var repayType = dicts.get("repayType").val();
    if ("07" === repayType) {
        dicts.get("interestWay").attr({"data-filterData": "1,2,3,4", "data-value": "5"});
    } else {
        dicts.get("interestWay").attr({"data-filterData": "", "data-value": "5"});
    }
    // shouExpiryAccrued();
    // 金额改变时重新计算银团信息
    if (plClass === "1000") {
        calCustomMoney();
    }
}

/**加载还款计划*/
function repayLan() {
    var id = $("#id").val();
    var loanCode = $("#id").val();//发放id
    var planId = $("#planId").val();
    var loanRate = rateTypes.get("plRate").val();//借款利率
    var loanDate = dateTypes.get("sendDate").val();//借款日期
    var repayDate = dateTypes.get("repayDate").val();//还款日期
    var repayType = dicts.get("repayType").val();//还款方式
    var loanMoney = moneyTypes.get("money").val();//金额
    var rateBase = $("#rateBase").val();
    if (bt.empty(loanDate)) {
        parent.layer.msg(bt.lang("cms.provide.loansend.senddateempty"), {icon: 2});
        return false;
    }
    if (bt.empty(repayDate)) {
        parent.layer.msg(bt.lang("cms.provide.loansend.repaydateempty"), {icon: 2});
        return false;
    }
    if (bt.empty(repayType)) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.repaytypeempty"), {icon: 2});
        return false;
    }
    if (bt.empty(loanMoney)) {
        parent.layer.msg(bt.lang("cms.provide.loansend.sendmoneyzero"), {icon: 2});
        return false;
    }

    var params = "billCode=" + loanCode + "&loanRate=" + loanRate + "&loanDate=" + loanDate + "&repayDate=" + repayDate
        + "&repayType=" + repayType + "&loanMoney=" + loanMoney + "&planId=" + planId + "&rateBase=" + rateBase;

    if (bt.navtab) {
        // bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'), basePath + "/cms/basedata/repaymentschedule/toRepayPlan?" + params);
        bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'), basePath + "/cms/basedata/repaymentschedule/makeRepayPlan?" + params);
    }
}

/**
 * 资金计划挂接
 */
function renderBudget() {
    //判断支付结算是否挂接资金计划
    var budgetSel = $("#budgetSel").val();
    if (budgetSel == "1") {
        //判断银行借款是否挂资金计划
        var sendItem = $("#sendItem").val();
        if (sendItem != null && sendItem != "") {
            $genOtherHtml();
        } else {
            $("#tagItemDetail").html("");
        }
    }
}


/**
 * 生成公共的属性
 * @return
 */
$genOtherHtml = function () {
    var itemCode = $('#sendItem').val();
    var defItemCode = $("#defItemCode").val();
    var processDate = dateTypes.get("sendDate").val();
    var corpId = $("#corpId").val();
    var curId = $("#curCode").val();//币别
    var url = basePath;
    var sendId = $("#id").val();
    var param = null;
    if (sendId == null || sendId == "") {
        url += "/budget/fbsBudgetProcess/getItemDetail";
        param = {
            corpId: corpId,
            itemCode: itemCode,
            defItemCode: defItemCode,
            processDate: processDate,
            curId: curId,
            type: 'select',
            date: (new Date()).getTime()
        };
    } else {
        url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
        param = {
            corpId: corpId,
            itemCode: itemCode,
            billNo: $("#billCode").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            defItemCode: defItemCode,
            processDate: processDate,
            curId: curId,
            type: 'select',
            date: (new Date()).getTime()
        };
    }

    $.ajax({
        url: url,
        method: 'POST',
        data: param,
        async: true,
        success: function (json) {
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
            bt.form().render();
        }
    });
};

/**根据plRate * (1+上浮百分比) */
function setOverdueRate() {
    var plRate = rateTypes.get("plRate").val();//借款利率
    var overduePercent = moneyTypes.get("overdueProportion").val();
    if (!overduePercent) {
        overduePercent = 0.00;
        moneyTypes.get("overdueProportion").val(overduePercent)
    }
    var overdueRate = fun.format(parseFloat(plRate) * parseFloat(1 + overduePercent / 100), {decimal: 6});
    rateTypes.get("overdueRate").val(overdueRate);
}


/**根据plRate + 上浮BP点数/10000 */
function setDeferredRate() {
    var plRate = rateTypes.get("plRate").val();//借款利率
    var deferredPoints = moneyTypes.get("deferredPoints").val();
    if (!deferredPoints) {
        deferredPoints = 0.00;
        moneyTypes.get("deferredPoints").val(deferredPoints)
    }
    var deferredRate = fun.format(parseFloat(plRate) + parseFloat(deferredPoints / 10000), {decimal: 6});
    rateTypes.get("deferredRate").val(deferredRate);
}

/*$genOtherHtml=function(corpId){
    var itemCode = $('input[name="endorseItem"]').val();
    var defItemCode = $('input[name="defItemCode"]').val();
    var processDate = $('input[name="createDate"]').val();
    var url=basePath+"/budget/fbsBudgetProcess/getItemDetail";
    var param={
        corpId:corpId,
        itemCode:itemCode,
        defItemCode:defItemCode,
        processDate:processDate,
        type:'select',
        date:(new Date()).getTime()
    };
    $.ajax({url:url,method: 'POST',data: param,async:true, success: function (json) {
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
        }
    });
};*/

/**
 *发放日期检查
 * @returns {boolean}
 */
function checkSendDate() {
    var startDate = $("#startDate").val();
    var sendDate = $.trim(dateTypes.get("sendDate").val());
    var repayDate = $.trim(dateTypes.get("repayDate").val());//到期日期
    var closeDay = $("#closeDay").val();
    var checkResult = true;
    if (bt.date.compare(sendDate, startDate) < 0 && bt.date.compare(sendDate, closeDay) && bt.date.compare(sendDate, repayDate)) {//发放日期不能小于开始日期&&大于已日结日期
        dateTypes.get("sendDate").val("");
        layer.msg(bt.lang('cms.loanSend.dateWarn'), {icon: 2});
        checkResult = false;
    }
    return checkResult;
}


/**
 * 还款方式选择回调
 */
function repayTypeCallBack(data, el) {
    if (data.capitalfrequencytype == "N" && data.interestfrequencytype == "N") {
        $("#makePlanBtn").addClass("layui-disabled");
        $("#makePlanBtn").attr("disabled", "disabled");
        $("#planStatus").val("0");
        $("#planId").val("");
    } else {
        $("#makePlanBtn").removeClass("layui-disabled");
        $("#makePlanBtn").removeAttr("disabled", "disabled");
        $("#planStatus").val("1");
    }
}

/**
 * 开发测试使用
 */
function viewInterest() {
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loansend/viewInterest?sendId=" + $("#id").val(),
    });
}

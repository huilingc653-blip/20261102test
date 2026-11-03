var form, dateType, dict, selectType,rateTye;
var floatWayChangeFlag = {
    isChange: false,
    flag: false
}
bt.use(['form', 'layedit', 'moneyType', 'btdate', 'dateType', 'corp', 'selectType', 'dicts', 'rateType', 'btUpload'], function () {
    var $ = bt.jquery;
    form = bt.form();

    form.verify({
        loanCurCode: [
            /[\S]+/
            ,bt.lang('没有满足贷款利率条件') //'必填项不能为空'
        ]
    })


    getLoanRate = function (data) {
        moneyType.get("pointsProportionText").val(data);
        getRate();
    }

    function strLen(str) {
        var len = 0;
        for (var i = 0; i < str.length; i++) {
            if (str.charCodeAt(i) > 127 || str.charCodeAt(i) == 94) {
                len += 2;
            } else {
                len++;
            }
        }
        return len;
    }

    /**
     * 担保方式变更
     */
    changeLoanType = function () {
        if (dict.get("loanType").val() != "01") {
            $("input[data-name=suretyMoney]").attr("lay-verify", "required");
            $("[name=suretyExplain]").attr("lay-verify", "required");
        } else {
            $("input[data-name=suretyMoney]").attr("lay-verify", "");
            $("[name=suretyExplain]").attr("lay-verify", "");
        }
        form.renderIcon();
    }

    /**
     * 贷款期限，天转成月
     */
    ternUnitTran = function () {
        var startDate = new Date(dateType.get("loanDate").val());
        var endDate = new Date(dateType.get("repayDate").val());
        var tern = 0;
        var ternUnit = dict.get("ternUnit").val();
        if (ternUnit == 1) {//月
            tern = $("#loanTerm").val();
        } else if (ternUnit == 2) {//天
            var loanTerm = $("#loanTerm").val();
            tern = dataSubtract(startDate, endDate, "M");
        }
        return tern;
    }


    /**计算data1 与 data2 相差多少param单位 data为字符串*/
    dataSubtract = function (data1, data2, param) {
        var dates;
        if (param == "D") {
            dates = Math.abs((new Date(data1).getTime() - new Date(data2).getTime())) / (1000 * 60 * 60 * 24) + 1;
        } else {
            var start = new Date(data1);
            var end = new Date(data2);

            var addMonth = end.getMonth() - start.getMonth();//月份差

            var startLastDay = mGetDate(start.getYear(), start.getMonth());//开始时间当前月总共多少天
            var endLastDay = mGetDate(end.getYear(), end.getMonth());//结束时间当前月总共多少天

            if (startLastDay == start.getDate()) {//开始日期月末最后一天
                // if (endLastDay == end.getDate()) {//结束日期月末最后一天
                //     addMonth++;//月份+1
                // } else {
                    if (end.getDate() > start.getDate()) {//结束日期当月天数 > 开始日期单月天数
                        addMonth++;//月份+1
                    }
                // }
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

    mGetDate = function (year, month) {
        var d = new Date(year, month + 1, 0);
        return d.getDate();
    }

    /**
     * 验证数据
     * @returns {Boolean}
     */
    validateFrom = function () {
        if ($("#loanTerm").val() < 0) {
            layer.alert(bt.lang("cms.loan.termErro"), {icon: 5});//贷款期限不能小于0!
            return false;
        }
        var loanTerm = ternUnitTran();
        if (dict.get("loanClass").val() == "01" && loanTerm > 12) {
            layer.alert(bt.lang("cms.loan.shortTermErro"), {icon: 5});//短期贷款月份不允许超过12个月！
            return false;
        } else if (dict.get("loanClass").val() == "02" && (loanTerm <= 12 || loanTerm > 60)) {
            layer.alert(bt.lang("cms.loan.middleTermErro"), {icon: 5});//中期贷款月份不允许小于12个月，大于60个月!
            return false;
        } else if (dict.get("loanClass").val() == "03" && loanTerm <= 60) {
            layer.alert(bt.lang("cms.loan.longTermErro"), {icon: 5});//长期贷款月份不允许小于60个月!
            return false;
        }
        if (strLen(bt.selectType.init().get("purpose").val()) >= 50) {//用途长度控制
            layer.tips(bt.lang("cms.loan.purposemorelength"), '#purposeShow');
            return false;
        }
        var isHangCredit = bt.dicts.init().get("isHangCredit").val();
        if ("1" == isHangCredit) {
            var ratCode = $("#cpRatCode");
            if (!ratCode) {
                parent.layer.msg(bt.lang("cms.rat.ratbkration.ratcodenull"), {icon: 2});
                return false;
            }
            if (bt.empty(ratCode.val())) {
                parent.layer.msg(bt.lang("cms.rat.ratbkration.ratcodenull"), {icon: 2});
                return false;
            }
        }
        return true;
    }

    //初始化日期
    dateType = bt.dateType.init();
    //初始化字典
    dict = bt.dicts.init();
    moneyType = bt.moneyType.init();
    rateTye = bt.rateType.init();
    //初始化单位
    bt.corp.init();
    //初始化利率
    bt.rateType.init();
    //初始化下拉框
    selectType = bt.selectType.init();
    //格式化数字
    bt.moneyType.init();

    var attachments = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        isExpand: true,
        gid: ''
    });

    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
//获取利率
    $("#getRate").click(function () {
        // 校验贷款币别选择
       var isPass =  form.verifyFun($("#curCode"))
        if(isPass){
            getRate();
        }
    });
    //监听提交
    form.on('submit(submit)', function (data) {
        var status = $(this).attr("value");
        var action = "/cms/loan/cmsloaninfoApply/save";
        if (!validateFrom()) return false;
        data.field.status = status;//提交状态
        data.field.attachmentId = attachments.getGroupId();
        var isHangCredit = bt.dicts.init().get("isHangCredit").val();
        if (!bt.empty(isHangCredit) && isHangCredit == "1") {
            setCpRationData(data);//设置内部授信数据信息
        }
        var mask = layer.load(1, {shade: [0.1, '#FFF']});//#393D49//遮罩层
        $.post(basePath + action, data.field, function (result) {
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                layer.close(mask);
                if (!bt.navtab) {
                    location.reload();
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    if (data.field.id == "") {
                        bt.navtab.refreshCurrent();
                    } else {
                        bt.navtab.closeCurrent();
                    }
                }
            } else {
                layer.close(mask);
            }
        }, 'JSON');
        return false;
    });

    //关闭窗口
    doClose = function () {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }
    //是否跳过补录
    changeOverRepair = function () {
        if (dict.get("isOverRepair").val() == "0") {//否   贷款办理信息灰选
            $(".isOver").hide();
            $("#contractCode").attr("disabled", "disabled");//贷款合同编号
            $("#contractCode").attr("lay-verify", "");
            dict.get("interestWay").attr({'data-disabled': true});//计息方式
            dict.get("floatWay").attr({'data-disabled': true});//浮动方式
            selectType.get("referenceRateType").val({"disabled": true});//利率期限
            $("#loanRate").attr("lay-verify", "");//贷款利率
            // $("#loanRate").attr("disabled","disabled");
            // moneyType.get("pointsProportionText").attr("disabled", true);//bp点数
            // rateData.attr("lay-verify","required");
            rateTye.get("overdueRate").attr('lay-verify', false);//逾期利率
        } else {//是
            $(".isOver").show();
            $("#contractCode").removeAttr("disabled");
            $("#contractCode").attr("lay-verify", "required");
            dict.get("interestWay").attr({'data-disabled': false});
            dict.get("floatWay").attr({'data-disabled': false});//浮动方式
            selectType.get("referenceRateType").val({"disabled": false});
            $("#loanRate").attr("lay-verify", "required");
            // $("#loanRate").removeAttr("disabled");
            // moneyType.get("pointsProportionText").attr("disabled", false);
            rateTye.get("overdueRate").attr('lay-verify', 'required');
        }
        form.renderIcon();
    };
});

/**加载还款计划*/
function repayLan() {
    var loanCode = $("#id").val();//借款单号
    var planId = $("#planId").val();
    var loanRate = rateTye.get("loanRate").val();//借款利率
    var loanDate = dateType.get("loanDate").val();//借款日期
    var repayDate = dateType.get("repayDate").val();//还款日期
    var repayType = selectType.get("repayType").val();//还款方式
    var loanMoney = moneyType.get("loanMoney").val();//金额

    // var rateBase = $("[name='rateBase']:checked").val();

    if (bt.empty(loanDate)) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.pldateempty"), {icon: 2});
        return false;
    }
    if (bt.empty(repayDate)) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.repaydateempty"), {icon: 2});
        return false;
    }
    if (bt.empty(repayType)) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.repaytypeempty"), {icon: 2});
        return false;
    }
    if (bt.empty(loanMoney)) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.plmoneyempty"), {icon: 2});
        return false;
    }
    if (bt.empty(loanRate) || loanRate == 0) {
        parent.layer.msg(bt.lang("cms.provide.loaninfo.plrateempty"), {icon: 2});
        return false;
    }

    var params = "billCode=" + loanCode + "&loanRate=" + loanRate + "&loanDate=" + loanDate + "&repayDate=" + repayDate
        + "&repayType=" + repayType + "&loanMoney=" + loanMoney + "&planId=" + planId;

    if (bt.navtab) {
        // 计提计息改造暂时注掉，使用新版计提计息页面
        // bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'), basePath + "/cms/basedata/repaymentschedule/toRepayPlan?" + params);
        bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'), basePath + "/cms/basedata/repaymentschedule/makeRepayPlan?" + params);
    }
}

// function openRepayPlan(data) {
//     var loanCode = $("#id").val();//借款单号
//     var planId = $("#planId").val();
//     var loanRate = rateTye.get("loanRate").val();//借款利率
//     var loanDate = dateType.get("loanDate").val();//借款日期
//     var repayDate = dateType.get("repayDate").val();//还款日期
//     var repayType = dict.get("repayType").val();//还款方式
//     var loanMoney = moneyType.get("loanMoney").val();//金额
//     var rateBase = $("[name='rateBase']:checked").val();
//     if (bt.empty(loanDate)) {
//         parent.layer.msg(bt.lang("cms.provide.loaninfo.pldateempty"), {icon: 2});
//         return false;
//     }
//     if (bt.empty(repayDate)) {
//         parent.layer.msg(bt.lang("cms.provide.loaninfo.repaydateempty"), {icon: 2});
//         return false;
//     }
//     if (bt.empty(repayType)) {
//         parent.layer.msg(bt.lang("cms.provide.loaninfo.repaytypeempty"), {icon: 2});
//         return false;
//     }
//     if (bt.empty(loanMoney)) {
//         parent.layer.msg(bt.lang("cms.provide.loaninfo.plmoneyempty"), {icon: 2});
//         return false;
//     }
//
//     var params = "loanRate=" + loanRate + "&loanDate=" + loanDate + "&repayDate=" + repayDate
//         + "&repayType=" + repayType + "&loanMoney=" + loanMoney + "&planId=" + $("#planId").val();
//
//     if (bt.navtab) {
//         bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'), basePath + "/cms/basedata/repaymentschedule/toRepayPlan?" + params);
//         return false;
//     }
//     return false;
// };

/**
 * 还款方式选择回调1
 */
function repayTypeCallBack(data, el) {
        $("#planStatus").val("1")
}


/**
 * 浮动利率是否禁用
 */
function elementDisabledOrEnabled(flag) {
    var rateData = $("input[name='rateEntriesEffectDate']");
    //selectType.get("referenceRateType").attr({"data-disabled":!flag});
    if (flag) {
        moneyType.get("pointsProportionText").attr("data-disabled", false);
        selectType.get("repayCycle").val({"disabled": false});
        //$("#getRate").show();
        rateData.attr("lay-verify", "required");
    } else {
        $("input[data-id='pointsProportionText']").attr("data-disabled", "disabled");
        selectType.get("repayCycle").val({"disabled": true});
        //$("#getRate").hide();
        rateData.removeAttr("lay-verify");
    }
    form.renderIcon();
    selectType.render();
    dict.get("pointsProportionSelect").render();
}

/**
 * 获取日期sendDate的浮动生效日期
 * @param plDate        日期
 * @param floatWay        发动方式
 * @param floatWayNext  详细浮动方式
 * @param isStanderd    自然季(false)或标准季(true)
 * @param count            不同浮动方式对应的数字
 * @return    浮动日期准确的年月信息
 */
function getFloatDate(plDate, floatWay, floatWayNext, isStanderd, count) {
    var date = new Date(plDate);
    var day = date.getDate();//获取plDate的借款日

    date = new Date(date.getFullYear(), date.getMonth(), 1);//当月的第一天

    var intersetDay = 21;//计息日默认为21号
    intersetDay = parseInt(intersetDay);

    if ("02" == floatWay) {//即时浮动
        return date;
    }
    var pd = isStanderd;
    if (count == 1) {
        pd = false;
    }

    if (pd) {//标准
        var plDateMonth = date.getMonth() + 1;
        if ("1" == floatWayNext) {//次《什么》第一天
            do {
                var a = getMonthStr(1, date);
                date = new Date(a);
                plDateMonth = date.getMonth() + 1;
            } while (plDateMonth % count != 1);
        } else {
            if ("3" == floatWayNext) {//次**计息日次日
                if (day > intersetDay) {//借款日在计息日之后，计息日取次月计息日，月份+13；借款日在计息日之前，计息日取当月计息日，月份+12
                    count += 1;
                }
                day += 1;
            }

            var a = getMonthStr(count, date);
            date = new Date(a);
            // while (plDateMonth % count != 0){
            // 	var a = getMonthStr(1, date);
            // 	date = new Date(a);
            // 	plDateMonth = date.getMonth() + 1;
            // }
        }
    } else {
        var a = getMonthStr(count, date);
        date = new Date(a);
    }

    return this.setMonthDay(day, date);
}

function setCpRationData(data) {
    data.field.ratCode = $("#cpRatCode").val();//内部授信编号
    data.field.ratItemCode = bt.selectType.init().get("cpRatItemCode").val();//内部授信品种
}

getLoanRate = function (data) {
    moneyType.get("pointsProportionText").val(data);
    getRate();
}

//获取利率
function getRate() {
    var pointsProportionSelect = dict.get("pointsProportionSelect").val();//浮动比例或点数类型
    var pointsProportionText = moneyType.get("pointsProportionText").val();//浮动点数或比例值
    var data = selectType.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    var pl_rate = bt.rateType.init().get("loanRate");//贷款利率值

    var floatWay = dict.get("floatWay").val();
    if (pointsProportionText != "" && floatWay != "01") {//非固定利率
        if (pointsProportionSelect == "01") {//点数
            //pl_rate.val(data.rate+(pointsProportionText/100));
            //基础利率+浮动的点数*0.01%
            pl_rate.val(data.rate + pointsProportionText * (0.01 / 100));
            return;
        } else {
            //pl_rate.val((data.rate)*(1+(pointsProportionText/100)));
            //比例 基准利率+浮动的比例*基准利率
            pl_rate.val((data.rate + (pointsProportionText / 100) * data.rate));
            return;
        }
    }

    if (bt.empty(data.rate)) {
        rateTye.get("loanRate").val(0);
    } else {
        rateTye.get("loanRate").val(data.rate);
    }

}

/**基准利率加载完成后 加载利率周期*/
function loanRepayCycle() {
    var curId = selectType.get("curCode").val();
    loanDate = $("input[name='loanDate']").val();//借款申请日期
    if (bt.empty(loanDate)) {
        loanDate = $("#inputDate").val();//为空默认录入日期
    }

    var referenceRateType = selectType.get("referenceRateType").val();//基准利率类型

    if (bt.empty(curId)) {
        selectType.get("repayCycle").attr("data-url", "");
    } else {//本位币
        if (floatWayChangeFlag.isChange) {
            selectType.get("repayCycle").attr({
                "data-required": floatWayChangeFlag.flag,
                "data-disabled": !floatWayChangeFlag.flag,
                "data-url": basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + loanDate + "&curId=" + curId + "&baseId=" + referenceRateType
            });
            floatWayChangeFlag.isChange = false;
            form.renderIcon();
        } else {
            selectType.get("repayCycle").attr("data-url", basePath + "/cms/loanrate/maintence/querRateMaintence?time=" + loanDate + "&curId=" + curId + "&baseId=" + referenceRateType);
        }
        //selectType.get("repayCycle").attr("data-url",basePath+"/cms/loanrate/maintence/querRateMaintence?time="+loanDate+"&curId="+curId+"&baseId="+referenceRateType);
    }
}

/**
 * 设置期限
 */
setTerm = function () {
    var startValue = dateType.get("loanDate").val();
    var endValue = dateType.get("repayDate").val();
    var startDate = new Date(startValue);
    var endDate = new Date(endValue);
    var days = 0;
    var month = 0;
    var tern = 0;
    if (startValue !== "" && endValue !== "") {
        var ternUnit = $("select[name='ternUnit']>option:selected").val();
        month = dataSubtract(startValue, endValue, "M");
        days = (endDate - startDate) / (1000 * 60 * 60 * 24);
        tern = month;
        if (ternUnit == 1) {//月
            $("#loanTerm").val(month);
        } else if (ternUnit == 2) {//天
            $("#loanTerm").val(days);
        }
        if (tern <= 12) {
            dict.get("loanClass").val("01");
        } else if (tern > 12 && tern <= 60) {
            dict.get("loanClass").val("02");
        } else if (tern > 60) {
            dict.get("loanClass").val("03");
        }
    }
}

setTerm1 = function () { //贷款日期变化时需重新选择授信额度单
    setTerm();
    $("#cpRatCode").val("");
    $("#cpRationBtn").hide();
}

/**
 * 日期增加多少个月
 */
function setMonthDay(day, date) {
    var strYear = date.getFullYear();
    var strMonth = date.getMonth();
    day = daysInMonth[strMonth + 1] >= day ? day : daysInMonth[strMonth + 1];
    return date = new Date(strYear, strMonth, day);
}

/**
 * 日期增加多少个月
 * @param addMonthCount    增加几个月
 * @param date            需要增加月份的日期
 */
var daysInMonth = new Array([0], [31], [28], [31], [30], [31], [30], [31], [31], [30], [31], [30], [31]);

function getMonthStr(addMonthCount, date) {
    date.setMonth(date.getMonth() + addMonthCount);//获取addMonthCount天后的日期
    var strYear = date.getFullYear();
    var strDay = date.getDate();
    var strMonth = date.getMonth();
    if (strYear % 4 == 0 && strYear % 100 != 0) {
        daysInMonth[2] = 29;
    }
    if (strMonth + 1 == 13) {
        strYear += 1;
        strMonth = 1;
    } else {
        strMonth += 1;
    }
    strDay = daysInMonth[strMonth] >= strDay ? strDay : daysInMonth[strMonth];
    if (strMonth < 10) {
        strMonth = "0" + strMonth;
    }
    if (strDay < 10) {
        strDay = "0" + strDay;
    }
    datastr = strYear + "-" + strMonth + "-" + strDay;
    return datastr;
}

/**
 * 单位改变回调
 * @param ele
 */
changeCorp = function (ele) {

    $.get(basePath + "/cms/loan/cmsloaninfo/getCenNetId?corpId="+ ele,function(data){
        $('#netName').val(data.corpName);
        $('#netCode').val(data.corpId);
        $('#corpName').val(data.corpName);
    },'JSON');

    /*$.ajax({
        url: basePath+"/basic/business/btpurpose?getPurpose",
        method: 'POST',
        data: {corpId: ele,sysBusType:2},
        dataType: 'json',
        success: function(data){
            $("#purpose").empty();
            $("#purpose").append("<option value=' '></option><option value='inputText'></option>");
            if(data && data.obj.length>0){
                $.each(data.obj,function(index,item){
                    $("#purpose").append("<option value="+item.purpose+">"+item.purpose+"</option>");
                });
            }
            form.render();
        }
    });*/
    //查询对应网点的用途
    var url = basePath + "/cms/loan/cmsloaninfo/getPurpose?corpId=" + ele;
    bt.selectType.init().get("purpose").attr('data-url', url);
}


/**
 * 用途回调
 */
purposeChange = function (ele, data) {
    if (strLen(data.value) >= 50) {
        layer.tips(bt.lang("cms.loan.purposemorelength"), '#purposeShow');
    }
}

/**加载浮动利率的周期信息*/
function loadrepayCycle() {
    var data = selectType.get("repayCycle").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
    var textVal = $("#pointsText").val();
    if (textVal == "") {
        textVal = 0;
    }
    var sel = dict.get("pointsProportionSelect").val();
    if (data.isbaseloanrate == "0") {//|| data.isbaseloanrate == undefined
        dict.get("pointsProportionSelect").val("01");
        if (sel != "01") {
            pointsProportionTextObj.val(textVal);
        }
    } else {//本位币
        var rateType = selectType.get("referenceRateType").getData();
        var code = rateType.obj.code;
        if (code == "lpr") {
            dict.get("pointsProportionSelect").val("01");
            $("#inputMark").text("BP");
        } else {
            dict.get("pointsProportionSelect").val("02");
            $("#inputMark").text("%");
        }

        if (sel != "02") {
            pointsProportionTextObj.val(textVal);
        }
    }

    //设置text的输入范围
    pointsProportionTextObj.attr("data-min", data.ratedownlimit);
    pointsProportionTextObj.attr("data-max", data.rateuplimit);
    pointsProportionTextObj.reset();
    readonlyText();
}

function readonlyText() {
    var floatWay = dict.get("floatWay").val();
    if (floatWay == "01") {//固定利率
        var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
        pointsProportionTextObj.val(0);
        pointsProportionTextObj.attr("data-min", 0);
        pointsProportionTextObj.attr("data-max", 0);
        pointsProportionTextObj.attr("readonly", true);
        //selectType.get("repayCycle").attr("readonly",true);
    } else {
        var pointsProportionTextObj = bt.moneyType.init().get("pointsProportionText");
        pointsProportionTextObj.attr("readonly", false);
        //bt.selectType.init().get("repayCycle").attr("readonly",false);
    }
}

/**浮动方式变更*/
function setChangeFloatWay() {
    var floatWay = dict.get("floatWay").val();
    floatWayChangeFlag.isChange = true;
    var rateData = $("input[name='rateEntriesEffectDate']");
    rateData.attr("readonly", false);
    rateData.attr("lay-verify", "required");
    var postfix = "";
    var htl = '';
    if (floatWay == "01") {//固定利率
        rateData.val("");
        rateData.attr("readonly", true);
        rateData.attr("lay-verify", "");
        floatWayChangeFlag.flag = false;
        elementDisabledOrEnabled(false);
    } else if (floatWay == "02") {//即时浮动
        rateData.val($("input[name='loanDate']").val());
        floatWayChangeFlag.flag = true;
        elementDisabledOrEnabled(true);
    } else if (floatWay == "03") {//按月浮动
        postfix = "Month";
        floatWayChangeFlag.flag = true;
        elementDisabledOrEnabled(true);
    } else if (floatWay == "04") {//按季浮动
        postfix = "Season";
        floatWayChangeFlag.flag = true;
        elementDisabledOrEnabled(true);
    } else if (floatWay == "05") {//按半年浮动
        postfix = "HY";
        floatWayChangeFlag.flag = true;
        elementDisabledOrEnabled(true);
    } else if (floatWay == "06") {//按年浮动
        postfix = "Year";
        floatWayChangeFlag.flag = true;
        elementDisabledOrEnabled(true);
    }

    if (postfix == "") {
        $("#ChangeFloatWay").html("");//删除页面元素
    } else {
        var htl = '<div class="bt-biz-dict" data-name="floatWayNext" data-type="select" data-biz-name="floatWayNext' + postfix + '" data-callback="setRateEntriesEffectDate" data-group="Cms.FloatWay' + postfix + '" ></div>';
        $("#ChangeFloatWay").html(htl);
        bt.dicts.render();
    }
}

/**
 * 设置贷款期限
 */
function setTerm(ele, data) {
    var startValue = dateType.get("loanDate").val();
    var endValue = dateType.get("repayDate").val();
    var startDate = new Date(startValue);
    var endDate = new Date(endValue);
    var days = 0;
    var month = 0;
    var tern = 0;
    if (startValue !== "" && endValue !== "") {
        if (startValue > endValue) {//开始日期不能大于结束日期
            dateType.get($(data).attr("data-biz-name")).val("");
            layer.tips(bt.lang("cms.loan.startDateEndDate.more"), '#' + $(data).attr("data-biz-name") + 'Show');
        }
        var ternUnit = $("select[name='ternUnit']>option:selected").val();
        month = dataSubtract(startDate, endDate, "M");
        days = (endDate - startDate) / (1000 * 60 * 60 * 24);
        tern = month;
        if (ternUnit == 1) {
            $("#loanTerm").val(month);
        } else if (ternUnit == 2) {
            $("#loanTerm").val(days);
        }
        if (tern <= 12) {
            dict.get("loanClass").val("01");
        } else if (tern > 12 && tern <= 60) {
            dict.get("loanClass").val("02");
        } else if (tern > 60) {
            dict.get("loanClass").val("03");
        }
    }

    var floatWay = dict.get("floatWay").val();
    if (floatWay == "02") {
        setChangeFloatWay();
    }
    var effectDate = $("input[name='rateEntriesEffectDate']").val();
    if (effectDate != null) {
        setRateEntriesEffectDate();
    }
    if (checkdate()) {//日期发生变化时需重新选择授信
        $("#ratCode").val("");
        $("#rationBtn").hide();
        ;
    }
}

/**根据浮动方式详情设置利率调整生效日期*/
function setRateEntriesEffectDate() {
    var floatWay = dict.get("floatWay").val();
    //var date =new Date($("input[name='loanDate']").val());
    // var rateData = $("input[name='rateEntriesEffectDate']");
    if (floatWay == "03") {//按月浮动
        //rateData.val(bt.date.format(bt.date.addMonth(date,1)));
        nextFloatWay(1, "Month");
    } else if (floatWay == "04") {//按季浮动
        nextFloatWay(3, "Season");
        //rateData.val(bt.date.format(bt.date.addMonth(date,3)));
    } else if (floatWay == "05") {//按半年浮动
        nextFloatWay(6, "HY");
        //rateData.val(bt.date.format(bt.date.addMonth(date,6)));
    } else if (floatWay == "06") {//按年浮动
        nextFloatWay(12, "Year");
        //rateData.val(bt.date.format(bt.date.addYear(date,1)));
    }
}

/**处理生效日期**/
function nextFloatWay(i, postfix) {
    var plDate = $.trim($("input[name='loanDate']").val());
    if (bt.empty(plDate)) {
        plDate = $.trim($("input[name='inputDate']").val());//为空默认录入日期
    }
    var floatWay = dict.get("floatWay").val();
    var floatWayNext = dict.get("floatWayNext" + postfix).val();
    var date = getFloatDate(plDate, floatWay, floatWayNext, true, i);
    var strYear = date.getFullYear();
    var strMonth = date.getMonth() + 1;
    if (strMonth < 10) {
        strMonth = "0" + strMonth;
    }
    var rateData = $("input[name='rateEntriesEffectDate']");
    if (floatWayNext == "1") {
        rateData.val(strYear + "-" + strMonth + "-01");
    } else if (floatWayNext == "2") {
        var day = parseInt(date.getDate());
        if (day < 10) {
            day = "0" + day;
        }
        rateData.val(strYear + "-" + strMonth + "-" + day);//getMonthStr(i,date)
    } else if (floatWayNext == "3") {
        var action = "/cms/provide/loaninfo/queryBatchInstDay";
        $.get(basePath + action, function (data) {
            var day = parseInt(data) + 1;
            if (day < 10) {
                day = "0" + day;
            }
            rateData.val(strYear + "-" + strMonth + "-" + day + "");
        }, 'JSON');
    } else {
        rateData.val("");
    }
}

/**
 * 授信额度选择
 * @param params
 */
function beforeCpRation() {
    var corpId = bt.corp.init().get("corpId").val();
    if (bt.empty(corpId) || corpId == 0) {
        //选择单位
        parent.layer.msg(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
        return "";
    }
    var curCode = bt.selectType.init().get("curCode").val();//币别
    if (bt.empty(curCode)) {
        //选择币别
        parent.layer.msg(bt.lang("cms.provide.loaninfo.curIdnull"), {icon: 2});
        return "";
    }
    var loanDate = dateType.get("loanDate").val();//贷款日期
    if (bt.empty(loanDate)) {
        loanDate = $("#inputDate").val();//为空默认录入日期
    }
    var itemCode = bt.selectType.init().get("cpRatItemCode").val();//授信品种
    if (bt.empty(itemCode)) {
        itemCode = "";
    }
    var beginDate = bt.dateType.init().get("loanDate").val();
    var isRmbRat = $("#isRmbRat").is(':checked') ? "1" : "0";//是否折合本位币
    var params = {
        corpId: corpId,
        curCode: curCode,
        ratItemCode: itemCode,
        beginDate: beginDate.replace(" ", ""),
        isRmbRat: isRmbRat
    }
    return params;
}

function setOtherParam(param) {
    var corpId = bt.corp.init().get("corpId").val();//网点id
    var startDate;//开始日期
    var endDate;//结束日期
    startDate = bt.dateType.init().get("loanDate").val();//开始日期
    endDate = bt.dateType.init().get("repayDate").val();//结束日期

    var curId = bt.selectType.init().get("curCode").val();//币别
    param["corpId"] = bt.empty(corpId) ? "" : corpId;
    param["startDate"] = bt.empty(startDate) ? "" : startDate;
    param["endDate"] = bt.empty(endDate) ? "" : endDate;
    param["curId"] = bt.empty(curId) ? "" : curId;
}

function selectBefore() {
    var corpId = bt.corp.init().get("corpId").val();
    if (bt.empty(corpId)) {
        layer.alert(bt.lang("cms.provide.loaninfo.corpIdnull"), {icon: 2});
        return false;
    } else {
        return true;
    }
}

//币种改变
function changeCur() {
    loadLoanRate();
    // clearRatModel();//清空授信信息
    getRate();
}

/**加载基准利率和利率期限*/
function loadLoanRate() {
    var data = selectTypes.get("curCode").currentObject.elem.find("option:selected").data();//获取币别选择的全部数据
    $('#curName').val(data.curname);
    if (bt.empty(data.id)) {
        selectTypes.get("referenceRateType").attr("data-url", "");
    } else if (data.naturalsign != 1) {

        selectTypes.get("referenceRateType").attr({
            "data-url": basePath + "/cms/loanrate/maintence/queryNotBaseRate",
            "data-defaultvalue": "first"
        });

    } else {//本位币
        selectTypes.get("referenceRateType").attr("data-url", basePath + "/cms/loanrate/maintence/queryBaseRate");
    }
}


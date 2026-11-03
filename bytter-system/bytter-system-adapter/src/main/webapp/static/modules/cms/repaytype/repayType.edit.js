bt.use(['form', 'element','layedit', 'selectType', 'dicts', 'selectTree', 'rateType', 'moneyType',], function () {
    var $ = bt.jquery, form = bt.form();
    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典
    rateType = bt.rateType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();


    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "") {
            return true;
        }
        return false;
    }
    showDesc();
    form.verify({
        numMax: function (val,item) {
            if (Number(val)>50) {
                return bt.lang("cms.repayType.error1");
            }
        },
        numMin: function (val,item) {
            if (Number(val) <= 0) {
                return bt.lang("cms.repayType.error2");
            }
        }
    });

    function showDesc() {
        var tip_index1 = 0;
        $('#interestRepayType-Next').mouseenter(function () {
            tip_index1 = layer.tips(bt.lang('付剩余本金产生的利息：还本时还需另付距上一次还款日至当前还本日剩余本金产生的利息；</br>' +
                '付还款本金产生利息：还本时还需另付距上一次付息日至当前还本日还款本金产生的利息；'), '#interestRepayType-Next', {time: 30000,maxWidth:400});
        }).mouseleave(function () {
            layer.close(tip_index1);
        });
    }
    /**
     * 监听本息关系
     */
    form.on('radio(capitalAndInterest)', function (data) {
        var expiryDateMonth = "expiryDateMonth";
        if ($("div[data-biz-name = 'expiryDateWeek']").length > 0) {
            expiryDateMonth = "expiryDateWeek";
        }
        // 等额本息
        if (data.value === "2") {
            // 只保留本金按时间周期
            dicts.get("capitalFrequencyType").attr({"data-filterData": "C,P,N", "data-value": ""});
            // 禁用利息
            dicts.get("interestFrequencyType").attr({"data-value": "","data-disabled": true, "data-required": false});
            // 清除利息相关信息
            $("#showInterestFrequencyNext").html("");
            $("#showInterestFrequency").html("");
            $("#showInterestFrequencyInput").html("");
            $("#showInterestFrequencyThis").html("");
            // 禁用计息日选项
            dicts.get(expiryDateMonth).attr({"data-value":"", "data-disabled": true, "data-required": false});
            // 还本时是否支付利息
            $("#showInterestRepayType").hide();
            // 利息计算方式隐藏
            $("#showInterestWay").hide();
        } else if (data.value === "3") {
            // 利息与本金无关
            // 去掉利息按照次数,周期选项，并设置利息为可选择必填
            dicts.get("interestFrequencyType").attr({
                "data-displayData": "P,N",
                "data-value": "",
                "data-disabled": false,
                "data-required": true
            });
            // 显示本金所有选项
            dicts.get("capitalFrequencyType").attr({"data-filterData": "", "data-value": ""});
            // 清空利息选项相关数据
            $("#showInterestFrequencyNext").html("");
            $("#showInterestFrequency").html("");
            $("#showInterestFrequencyNumInput").html("");
            $("#showInterestFrequencyThis").html("");
            // 禁用计息日选项
            dicts.get(expiryDateMonth).attr({"data-value":"", "data-disabled": false, "data-required": true});
            // 还本时是否支付利息
            $("#showInterestRepayType").hide();
            // 利息计算方式隐藏
            $("#showInterestWay").hide();
        } else {
            // 按剩余本金
            // 去掉利息按照次数选项，并设置利息为可选择必填
            dicts.get("interestFrequencyType").attr({
                "data-displayData": "D",
                "data-value": "",
                "data-disabled": false,
                "data-required": true
            });
            // 去掉本金随借随还
            //支持本金随借随还
            dicts.get("capitalFrequencyType").attr({"data-filterData": "", "data-value": ""});
            // 禁用计息日选项
            dicts.get(expiryDateMonth).attr({"data-value":"", "data-disabled": false, "data-required": true});
            // 还本时是否支付利息
            $("#showInterestRepayType").show();
            // 利息计算方式显示
            $("#showInterestWay").show();
        }
        // 清除本金后续选项设置
        $("#showCapitalFrequencyNext").html("");
        $("#showCapitalFrequency").html("");
        $("#showCapitalFrequencyNumInput").html("");
        // 清除利息后续选项设置
        $("#showInterestFrequencyNext").html("");
        $("#showInterestFrequency").html("");
        $("#showInterestFrequencyNumInput").html("");
        $("#showInterestFrequencyThis").html("");
        // 删除按比例设置内容数据
        $("#dataTableDetailBody").html("");
        // 隐藏按比例设置相关内容
        $("#capitalFrequencyDetailShow").css("display", "none");
        dicts.render();
        form.render();
        form.renderIcon();
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        //本金支付频率为按比例时获得次数
        var capitalFrequencyNext = dicts.get("capitalFrequencyNextByNumber").val();
        var capitalFrequencyNumInput = $("#capitalFrequencyNumInput").val();
        var detailNum = Number(capitalFrequencyNumInput);
        if (capitalFrequencyNext == "1" && detailNum) {
            var details = "";
            var percent = 0;
            for (var rowNum = 1; rowNum <= detailNum; rowNum++) {
                details += $("#percent" + rowNum).val() + ",";
                percent = moneyFun.add(percent, moneyFun.toNumber($("#percent" + rowNum).val()));
            }
            data.field.capitalFrequencyDetail = details.substr(0, details.length - 1);
            // 判断比例是否为100%
            if (Number(percent) != 1) {
                layer.alert(bt.lang("cms.repay.message.percent.error"), {icon: 2});
                return false;
            }
        }

        // 如果
        var type=$("#type").val();
        var action = "/cms/repay/repaytype/modify";
        if(type=="clone"){
            action="/cms/repay/repaytype/save";
        }
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2})
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshCurrent()
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
        return false;
    });
});

/**显示本金支付频率第二列*/
function showCapitalFrequencyType() {
    var capitalFrequencyType = dicts.get("capitalFrequencyType").val();
    if (bt.empty(capitalFrequencyType)) {
        $("#showCapitalFrequencyNext").html("");
    } else {
        // 按时间周期：accrual.dateType
        if (capitalFrequencyType == "D") {
            $("#showCapitalFrequencyNext").html('<div class="bt-biz-dict" data-width="130px" data-name="capitalFrequencyNext" data-biz-name="capitalFrequencyNextByDate" data-type="select" data-group="accrual.dateType" data-displayData="1,2,3,4,5" data-required="true" data-callback="showCapitalFrequencyNextByDate"></div>');
        } else if (capitalFrequencyType == "C") {
            // 按次数：repay.numberType
            $("#showCapitalFrequencyNext").html('<div class="bt-biz-dict" data-width="130px" data-name="capitalFrequencyNext" data-biz-name="capitalFrequencyNextByNumber" data-type="select" data-group="repay.numberType" data-required="true" data-callback="showCapitalFrequencyNextByNumber"></div>');
        } else {
            $("#showCapitalFrequencyNext").html("");
        }
    }
    //利息按剩余本金计算 随借随还处理
    interestFrequencyTypeByCapital();
    bt.dicts.render();
    $("#capitalFrequencyDetailShow").hide();
    $("#showCapitalFrequency").html("");
    $("#showCapitalFrequencyNumInput").html("");
}

/**
 * 利息与本金相关 随借随还处理
 */
function interestFrequencyTypeByCapital() {
    var capitalAndInterest = $('input[name="capitalAndInterest"]:checked').val()
    var capitalFrequencyType = dicts.get("capitalFrequencyType").val();
    if(capitalAndInterest == "1"){
        if(capitalFrequencyType == "N") {
            // 禁用利息
            dicts.get("interestFrequencyType").attr({"data-displayData": "D,N","data-value": "N", "data-disabled": true});
            dicts.get("interestRepayType").attr({"data-value": "1", "data-disabled": true});
            $("#interestRepayTypeNext").show();
            dicts.get("interestRepayTypeNext").attr({"data-value": "1", "data-disabled": true});
            dicts.get("interestWay").attr({"data-value": "0", "data-disabled": true});
            // 禁用计息日选项
            dicts.get("expiryDateMonth").attr({"data-value":"", "data-disabled": true, "data-required": false});
        }else {
            dicts.get("interestFrequencyType").attr({"data-displayData": "D","data-value": "", "data-disabled": false});
            dicts.get("interestRepayType").attr({"data-value": "0", "data-disabled": false});
            $("#interestRepayTypeNext").hide();
            dicts.get("interestRepayTypeNext").attr({"data-value": "0", "data-disabled": false});
            dicts.get("interestWay").attr({"data-value": "0", "data-disabled": false});
            // 计息日选项
            dicts.get("expiryDateMonth").attr({"data-value":"", "data-disabled": false, "data-required": true});
        }
        bt.form().renderIcon();
        $("#showInterestFrequencyNext").html("");
        $("#showInterestFrequency").html("");
        $("#showInterestFrequencyNumInput").html("");
        $("#showInterestFrequencyThis").html("");

    }
}

/**显示本金支付频率第三列：按时间周期*/
function showCapitalFrequencyNextByDate() {
    var capitalFrequencyNext = dicts.get("capitalFrequencyNextByDate").val();
    if (bt.empty(capitalFrequencyNext)) {
        $("#showCapitalFrequency").html("");
    } else {
        // 天：accrual.dayCycle
        if (capitalFrequencyNext == "1") {
            $("#showCapitalFrequencyThird").html("");
            $("#showCapitalFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="capitalFrequency" data-biz-name="capitalFrequencyDay" data-type="select" data-group="accrual.dayCycle" data-required="true" data-callback="callCapitalFrequencyNumDayByDate"></div>');
        } else if (capitalFrequencyNext == "2") {
            // 周：accrual.weekCycle,
            $("#showCapitalFrequencyThird").html("");
            $("#showCapitalFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="capitalFrequency" data-biz-name="capitalFrequencyWeek" data-type="select" data-group="accrual.weekCycle" data-required="true" data-callback="callCapitalFrequencyNumDayByDate"></div>');
        } else if (capitalFrequencyNext == "3") {
            // 月：accrual.monthCycle
            $("#showCapitalFrequencyThird").html("");
            $("#showCapitalFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="capitalFrequency" data-biz-name="capitalFrequencyMonth" data-type="select" data-group="accrual.monthCycle" data-required="true" data-callback="callCapitalFrequencyNumDayByDate"></div>');
        }else if (capitalFrequencyNext == "4") {
            // 季：accrual.seasonCycle
            $("#showCapitalFrequencyThird").html("");
            $("#showCapitalFrequency").html('<div class="bt-biz-dict" data-width="200px" data-name="capitalFrequency" data-biz-name="capitalFrequencySeason" data-type="select" data-group="accrual.seasonCycle" data-required="true" data-callback="callCapitalFrequencyNumDayByDate"></div>');
        } else if (capitalFrequencyNext == "5") {
            // 年：accrual.yearCycle
            $("#showCapitalFrequencyThird").html('<div class="bt-biz-dict" data-width="130px" data-name="capitalFrequencyThird" data-biz-name="capitalFrequencyThird" data-type="select" data-group="accrual.yearType" data-required="true" data-callback="callCapitalFrequencyThirdByType"></div>');
            $("#showCapitalFrequency").html("");
        } else {
            $("#showCapitalFrequency").html("");
        }
    }
    bt.dicts.render();
}

/**显示本金支付频率第三列：按次数*/
function showCapitalFrequencyNextByNumber() {
    var capitalFrequencyNext = dicts.get("capitalFrequencyNextByNumber").val();
    if (bt.empty(capitalFrequencyNext)) {
        $("#showCapitalFrequency").html("");
        $("#showCapitalFrequencyNumInput").html("");
    } else {
        if (capitalFrequencyNext == "1") {
            // 按比例，输入框带回调
            $("#showCapitalFrequency").html("");
            $("#showCapitalFrequencyNumInput").html('<input type="text" id="capitalFrequencyNumInput" name="capitalFrequencyNum" lay-verify="required|number|numMax|numMin" data-width="130px" autocomplete="off" onchange="callCapitalFrequencyDetailShow()" class="layui-input">');
        } else {
            // 无比例，输入框不带回调
            $("#showCapitalFrequency").html("");
            $("#showCapitalFrequencyNumInput").html('<input type="text" id="capitalFrequencyNumInput" name="capitalFrequencyNum" lay-verify="required|number|numMax|numMin" data-width="130px" autocomplete="off" class="layui-input">');
        }
    }
    $("#capitalFrequencyDetailShow").hide();
}

/**显示本金支付频率第四列：按时间周期后显示输入框(年专用)*/
function callCapitalFrequencyThirdByType() {
    var capitalFrequencyThird = dicts.get("capitalFrequencyThird").val();
    if(capitalFrequencyThird == "12"){
        $("#showCapitalFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="capitalFrequency" data-biz-name="capitalFrequencyYear" data-type="select" data-group="accrual.yearCycle" data-required="true" data-callback="callCapitalFrequencyNumDayByDate"></div>');
    } else {
        $("#showCapitalFrequency").html("");
        $("#showCapitalFrequencyNumInput").html("");
    }
    bt.dicts.render();
}

/**显示本金支付频率第四列：按时间周期后显示输入框*/
function callCapitalFrequencyNumDayByDate() {
    var capitalFrequencyNext = dicts.get("capitalFrequencyNextByDate").val();
    var cycle = "";
    // 天：accrual.dayCycle
    if (capitalFrequencyNext == "1") {
        cycle = "Day"
    } else if (capitalFrequencyNext == "2") {
        // 周：accrual.weekCycle,
        cycle = "Week"
    } else if (capitalFrequencyNext == "3") {
        // 月：accrual.monthCycle
        cycle = "Month"
    } else if (capitalFrequencyNext == "4") {
        // 季：accrual.seasonCycle
        cycle = "Season"
    } else if (capitalFrequencyNext == "5") {
        // 年：accrual.yearCycle
        cycle = "Year"
    }
    var capitalFrequency = dicts.get("capitalFrequency" + cycle).val();
    if (bt.empty(capitalFrequency)) {
        $("#showCapitalFrequencyNumInput").html("");
    } else {
        if (capitalFrequency == "other") {
            $("#showCapitalFrequencyNumInput").html('<input type="text" id="capitalFrequencyNumInput" name="capitalFrequencyNum" lay-verify="required|number|numMax|numMin" data-width="130px" autocomplete="off" class="layui-input">');
        } else {
            $("#showCapitalFrequencyNumInput").html("");
        }
        var capitalAndInterest = $('input[name="capitalAndInterest"]:checked').val()
        if (capitalAndInterest !== "1") {
            //到期一次性支付，可选择利息的算法
            if(capitalFrequency == "num"){
                $("#showInterestWay").show();
            }else {
                $("#showInterestWay").hide();
            }
        } else {
            $("#showInterestWay").show();
        }
    }
    bt.dicts.render();
    $("#capitalFrequencyDetailShow").hide();
}

/**显示本金支付频率第四列：按次数显示输入框*/
function callFrequencyDailyDateByNumber() {
    var frequencyDailyDate = dicts.get("frequencyDailyDate").val();
    var cycle = "";
    // 按比例
    if (frequencyDailyDate == "1") {
        cycle = "Ratio"
    } else if (frequencyDailyDate == "2") {
        // 无比例
        cycle = "Notratio"
    }

    var frequencyDailyDate = dicts.get("frequencyDailyDate" + cycle).val();
    if (bt.empty(frequencyDailyDate)) {
        $("#showCapitalFrequencyNumInput").html("");
    } else {
        $("#showCapitalFrequencyNumInput").html('<input type="text" id="capitalFrequencyNumInput" name="capitalFrequencyNum" lay-verify="required|number|numMax|numMin" data-width="130px" autocomplete="off" onclick="capitalFrequencyDetailShow(this)" class="layui-input">');
    }
    bt.dicts.render();
    $("#capitalFrequencyDetailShow").hide();
}

/**显示本金支付频率第四列：按次数显示输入框回调*/
function callCapitalFrequencyDetailShow() {
    var capitalFrequencyNumInput = $("#capitalFrequencyNumInput").val();
    if (!Number(capitalFrequencyNumInput)) {
        return false;
    }
    if (Number(capitalFrequencyNumInput) > 50) {
        layer.alert(bt.lang("cms.repayType.error1"),{icon: 5});
        return false;
    }
    if (Number(capitalFrequencyNumInput) < 0) {
        layer.alert(bt.lang("cms.repayType.error2"),{icon: 5});
        return false;
    }
    var detailNum = Number(capitalFrequencyNumInput);
    if (detailNum) {
        $("#capitalFrequencyDetailShow").show();
        var html = '';
        for (var rowNum = 1; rowNum <= detailNum; rowNum++) {
            html += '<tr>';
            html += '<td><div class="layui-inline"><input type="text" name="detail[' + rowNum + '].num" id="num' + rowNum + '" lay-verify="required" autocomplete="off" class="layui-input" disabled value="' + rowNum + '" /></div></td>';
            html += '<td><div class="layui-inline"><input type="text" class="layui-input bt-input-inline bt-biz-rateType" name="detail[' + rowNum + '].percent" id="percent' + rowNum + '" data-decimal="2" data-ratetype="percent" lay-verify="required" autocomplete="off" class="layui-input" /></div></td>';
            html += '</tr>';
        }
        $('#dataTableDetailBody').html(html)
    } else {
        $("#capitalFrequencyDetailShow").hide();
    }
    rateType.render();
    bt.form().renderIcon();
}

/**显示利息支付频率第二列*/
function showInterestFrequencyType() {
    var interestFrequencyType = dicts.get("interestFrequencyType").val();
    if (bt.empty(interestFrequencyType)) {
        $("#showInterestFrequencyNext").html("");
    } else {
        // 按时间周期：accrual.dateType
        if (interestFrequencyType == "D") {
            $("#showInterestFrequencyNext").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequencyNext" data-biz-name="interestFrequencyNextByDate" data-type="select" data-group="accrual.dateType" data-displayData="1,2,3,4,5" data-required="true" data-callback="showInterestFrequencyNextByDate"></div>');
        } else if (interestFrequencyType == "C") {
            // 按次数：repay.numberType
            $("#showInterestFrequencyNext").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequencyNext" data-biz-name="interestFrequencyNextByNumber" data-type="select" data-group="repay.numberType" data-required="true" data-callback="showInterestFrequencyNextByNumber"></div>');
        } else {
            $("#showInterestFrequencyNext").html("");
        }
    }
    var expiryDateMonth = "expiryDateMonth";
    if ($("div[data-biz-name = 'expiryDateWeek']").length > 0) {
        expiryDateMonth = "expiryDateWeek";
    }
    if (interestFrequencyType == "D") {
        dicts.get("expiryDateMonth").attr({"data-value":"", "data-disabled": false, "data-required": true});
    } else {
        dicts.get("expiryDateMonth").attr({"data-value":"", "data-disabled": true, "data-required": false});
    }
    $("#showInterestFrequency").html("");
    $("#showInterestFrequencyNumInput").html("");
    bt.dicts.render();
    bt.form().renderIcon()
}

/**显示利息支付频率第三列：按时间周期*/
function showInterestFrequencyNextByDate() {
    var interestFrequencyNext = dicts.get("interestFrequencyNextByDate").val();
    if (bt.empty(interestFrequencyNext)) {
        $("#interestFrequencyNext").html("");
    } else {
        // 天：accrual.dayCycle
        if (interestFrequencyNext == "1") {
            $("#showInterestFrequencyThird").html("");
            $("#showInterestFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequency" data-biz-name="interestFrequencyDay" data-type="select" data-group="accrual.dayCycle" data-required="true" data-callback="callInterestFrequencyNumByDate"></div>');
            $("#showInterestFrequencyThis").html("");
        } else if (interestFrequencyNext == "2") {
            // 周：accrual.weekCycle
            $("#showInterestFrequencyThird").html("");
            $("#showInterestFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequency" data-biz-name="interestFrequencyWeek" data-type="select" data-group="accrual.weekCycle" data-required="true" data-callback="callInterestFrequencyNumByDate"></div>');
            $("#showInterestFrequencyThis").html("");
        } else if (interestFrequencyNext == "3") {
            // 月：accrual.monthCycle
            $("#showInterestFrequencyThird").html("");
            $("#showInterestFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequency" data-biz-name="interestFrequencyMonth" data-type="select" data-group="accrual.monthCycle" data-required="true" data-callback="callInterestFrequencyNumByDate"></div>');
            $("#showInterestFrequencyThis").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequencyThis" data-biz-name="interestFrequencyThisMonth" data-type="select" data-group="accrual.month.thisinterest" data-required="true" data-value="0" data-selectText="false"></div>');
        }else if (interestFrequencyNext == "4") {
            //,季：accrual.seasonCycle
            $("#showInterestFrequencyThird").html("");
            $("#showInterestFrequency").html('<div class="bt-biz-dict" data-width="200px" data-name="interestFrequency" data-biz-name="interestFrequencySeason" data-type="select" data-group="accrual.seasonCycle" data-required="true" data-callback="callInterestFrequencyNumByDate"></div>');
            $("#showInterestFrequencyThis").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequencyThis" data-biz-name="interestFrequencyThisSeason" data-type="select" data-group="accrual.season.thisinterest" data-required="true" data-value="1" data-selectText="false"></div>');
        } else if (interestFrequencyNext == "5") {
            // 年：accrual.yearCycle
            $("#showInterestFrequencyThird").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequencyThird" data-biz-name="interestFrequencyThird" data-type="select" data-group="accrual.yearType" data-required="true" data-callback="callInterestFrequencyThirdByType"></div>');
            $("#showInterestFrequency").html("");
            $("#showInterestFrequencyThis").html("");
        } else {
            $("#showInterestFrequencyThird").html("");
            $("#showInterestFrequency").html("");
            $("#showInterestFrequencyThis").html("");
        }
    }
    var expiryDateMonth = "expiryDateMonth";
    if ($("div[data-biz-name = 'expiryDateWeek']").length > 0) {
        expiryDateMonth = "expiryDateWeek";
    }
    if (interestFrequencyNext == "1") {
        dicts.get("expiryDateMonth").attr({"data-value":"", "data-disabled": true, "data-required": false});
    }else if (interestFrequencyNext == "2") {
        $("#expiryDateTd").html(
            "<div class=\"bt-biz-dict\" data-name=\"interestDay\" data-type=\"select\"" +
            "data-biz-name=\"expiryDateWeek\" data-group=\"accrued.week\"" +
            "data-required=\"true\"></div>"
        );
    } else {
        $("#expiryDateTd").html(
            "<div class=\"bt-biz-dict\" data-name=\"interestDay\" data-type=\"select\"" +
            "data-biz-name=\"expiryDateMonth\" data-group=\"cms.repayType.interestDay\"" +
            "data-required=\"true\"></div>"
        );
    }
    bt.dicts.render();
    bt.form().renderIcon()
}

/**显示利息支付第三列：按次数*/
function showInterestFrequencyNextByNumber() {
    var interestFrequencyNext = dicts.get("interestFrequencyNextByNumber").val();
    if (bt.empty(interestFrequencyNext)) {
        $("#showInterestFrequency").html("");
        $("#showInterestFrequencyNumInput").html("");
    } else {
        $("#showInterestFrequency").html("");
        $("#showInterestFrequencyNumInput").html('<input type="text" id="interestFrequencyNumInput" name="interestFrequencyNum" lay-verify="required|number|numMax|numMin" data-width="130px" autocomplete="off" class="layui-input">');
    }
    bt.dicts.render();
}

/**显示利息支付频率第四列：按时间周期后显示输入框(年专用)*/
function callInterestFrequencyThirdByType() {
    var interestFrequencyThird = dicts.get("interestFrequencyThird").val();
    if(interestFrequencyThird == "12"){
        $("#showInterestFrequency").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequency" data-biz-name="interestFrequencyYear" data-type="select" data-group="accrual.yearCycle" data-required="true" data-callback="callInterestFrequencyNumByDate"></div>');
        $("#showInterestFrequencyThis").html("");
    } else {
        $("#showInterestFrequency").html("");
        $("#showInterestFrequencyNumInput").html("");
        $("#showInterestFrequencyThis").html('<div class="bt-biz-dict" data-width="130px" data-name="interestFrequencyThis" data-biz-name="interestFrequencyThisYear" data-type="select" data-group="accrual.year.thisinterest" data-required="true" data-value="1" data-selectText="false"></div>');
    }
    bt.dicts.render();
}

/**显示利息支付频率第四列：按时间周期后显示输入框*/
function callInterestFrequencyNumByDate() {
    var interestFrequencyNext = dicts.get("interestFrequencyNextByDate").val();
    var cycle = "";
    // 天：accrual.dayCycle
    if (interestFrequencyNext == "1") {
        cycle = "Day"
    } else if (interestFrequencyNext == "2") {
        // 周：accrual.weekCycle,
        cycle = "Week"
    } else if (interestFrequencyNext == "3") {
        // 月：accrual.monthCycle
        cycle = "Month"
    } else if (interestFrequencyNext == "4") {
        // 季：accrual.seasonCycle
        cycle = "Season"
    } else if (interestFrequencyNext == "5") {
        // 年：accrual.yearCycle
        cycle = "Year"
    }
    var interestFrequencyNum = dicts.get("interestFrequency" + cycle).val();
    if (bt.empty(interestFrequencyNum)) {
        $("#showInterestFrequencyNumInput").html("");
    } else {
        if (interestFrequencyNum == "other") {
            $("#showInterestFrequencyNumInput").html('<input type="text" id="interestFrequencyNumInput" name="interestFrequencyNum" lay-verify="required|number|numMax|numMin" style="width: 100px" autocomplete="off" class="layui-input">');
        } else {
            $("#showInterestFrequencyNumInput").html("");
        }
    }
    bt.dicts.render();
}

/**显示利息支付第四列：按次数显示输入框*/
function callFrequencyDailyDateByNumber() {
    var frequencyDailyDate = dicts.get("frequencyDailyDate").val();
    var cycle = "";
    // 按比例
    if (frequencyDailyDate == "1") {
        cycle = "Ratio"
    } else if (frequencyDailyDate == "2") {
        // 无比例
        cycle = "Notratio"
    }
    var frequencyDailyDate = dicts.get("frequencyDailyDate" + cycle).val();
    if (bt.empty(frequencyDailyDate)) {
        $("#showFrequencyDailyDateInput").html("");
    } else {
        $("#showFrequencyDailyDateInput").html('<input type="text" id="interestFrequencyNumInput" name="interestFrequencyNum" lay-verify="required|number|numMax|numMin" data-width="130px" autocomplete="off" class="layui-input">');
    }
    bt.dicts.render();
}

function interestCallBack() {
    var interestRepayType = dicts.get("interestRepayType").val();
    if(interestRepayType=="0"){
        $("#interestRepayTypeNext").hide();
    }else {
        $("#interestRepayTypeNext").show();
    }
}

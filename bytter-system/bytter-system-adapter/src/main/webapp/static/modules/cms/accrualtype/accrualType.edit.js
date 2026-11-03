bt.use(['form', 'layedit', 'selectType', 'dicts', 'selectTree'], function () {
    var $ = bt.jquery, form = bt.form();
    selectTypes = bt.selectType.init();//下拉框
    dicts = bt.dicts.init();//字典

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "") {
            return true;
        }
        return false;
    }

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

    //监听提交
    form.on('submit(submit)', function (data) {
        var type = $("#type").val()
        if (type == "clone") {
            var action = "/cms/accrual/accrualtype/save";
        } else {
            var action = "/cms/accrual/accrualtype/modify";
        }
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
});


/**显示预提日*/
function showFrequency() {
    var frequencyType = dicts.get("frequencyType").val();
    if (bt.empty(frequencyType)) {
        $("#showFrequencyDailyDate").html("");
    } else {
        // 天：accrual.dayCycle
        if (frequencyType == "1") {
            $("#showFrequencyDailyDate").html('<div class="bt-biz-dict" data-filterData="num" data-width="150px" data-name="frequency" data-type="select" data-biz-name="frequencyDay" data-group="accrual.dayCycle" data-required="true" data-callback="callFrequencyDailyDate"></div>');
        } else if (frequencyType == "2") {
            // 周：accrual.weekCycle
            $("#showFrequencyDailyDate").html('<div class="bt-biz-dict" data-width="150px" data-name="frequency" data-biz-name="frequencyWeek" data-type="select" data-group="accrual.weekCycle" data-required="true" data-callback="callFrequencyDailyDate"></div>');
        } else if (frequencyType == "3") {
            // 月：accrual.monthCycle
            $("#showFrequencyDailyDate").html('<div class="bt-biz-dict" data-width="150px" data-name="frequency" data-biz-name="frequencyMonth" data-type="select" data-group="accrual.monthCycle" data-required="true" data-callback="callFrequencyDailyDate"></div>');
        }else if (frequencyType == "4") {
            // 季：accrual.seasonCycle
            $("#showFrequencyDailyDate").html('<div class="bt-biz-dict" data-width="150px" data-name="frequency" data-biz-name="frequencySeason" data-type="select" data-group="accrual.seasonCycle" data-required="true" data-callback="callFrequencyDailyDate"></div>');
        } else if (frequencyType == "5") {
            // 年：accrual.yearCycle
            $("#showFrequencyDailyDate").html('<div class="bt-biz-dict" data-width="130px" data-name="frequency" data-biz-name="frequencyYear" data-type="select" data-group="accrual.yearCycle" data-required="true" data-callback="callFrequencyDailyDate"></div>');
        } else {
            $("#showFrequencyDailyDate").html("");
        }
    }
    $("#showFrequencyDailyDateInput").html("");

    /**
     * 不计提和按天计提则无计提日期
     */
    if (frequencyType === "1" || frequencyType === "0") {
        $("#accruedDailyDateTr").remove();
    } else if (frequencyType === "2") {
        $("#accruedDailyDateTr").remove();
        $("#frequencyTypeTr").after(
            "<tr id=\"accruedDailyDateTr\">" +
            "<td class=\"textRight\">" + bt.lang("accrued.daily.date") + "</td>" +
            "<td>" +
            "   <div class=\"layui-inline\">" +
            "       <div class=\"bt-biz-dict\" data-name=\"accruedDailyDate\" data-type=\"select\"" +
            "       data-biz-name=\"accruedDailyDate1\" data-group=\"accrued.week\"" +
            "       data-required=\"true\"></div>" +
            "   </div>" +
            "</td>" +
            "</tr>"
        );
    } else {
        $("#accruedDailyDateTr").remove();
        $("#frequencyTypeTr").after(
            "<tr id=\"accruedDailyDateTr\">" +
            "<td class=\"textRight\">" + bt.lang("accrued.daily.date") + "</td>" +
            "<td>" +
            "   <div class=\"layui-inline\">" +
            "       <div class=\"bt-biz-dict\" data-name=\"accruedDailyDate\" data-type=\"select\"" +
            "       data-biz-name=\"accruedDailyDate\" data-group=\"bisGatherPolicy.month\"" +
            "       data-value=\"21\" data-required=\"true\"></div>" +
            "   </div>" +
            "</td>" +
            "</tr>"
        );
    }
    bt.dicts.render();
    bt.form().renderIcon();
}

/**显示预提日输入框*/
function callFrequencyDailyDate() {
    var frequencyType = dicts.get("frequencyType").val();
    var cycle = "";
    // 天：accrual.dayCycle
    if (frequencyType == "1") {
        cycle = "Day"
    } else if (frequencyType == "2") {
        // 周：accrual.weekCycle,
        cycle = "Week"
    } else if (frequencyType == "3") {
        // 月：accrual.monthCycle
        cycle = "Month"
    } else if ( frequencyType == "4") {
        // 季：accrual.seasonCycle
        cycle = "Season"
    } else if (frequencyType == "5") {
        cycle
        // 年：accrual.yearCycle
        cycle = "Year"
    }

    var frequencyDailyDate = dicts.get("frequency" + cycle).val();
    if (bt.empty(frequencyDailyDate)) {
        $("#showFrequencyDailyDateInput").html("");
    } else {
        if (frequencyDailyDate == "other") {
            $("#showFrequencyDailyDateInput").html('<input type="text" id="frequencyDailyDateInput" name="frequencyDailyDate" lay-verify="required|number|numMax|numMin" data-width="130px" class="layui-input">');
        } else {
            $("#showFrequencyDailyDateInput").html("");
        }
    }
    bt.dicts.render();
}
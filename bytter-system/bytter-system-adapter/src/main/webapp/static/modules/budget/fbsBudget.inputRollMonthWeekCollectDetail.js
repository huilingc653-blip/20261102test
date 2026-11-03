var moneyFun;
var moneyType;
var up;
bt.use(['form', 'layedit', 'btdate', 'moneyType', 'treeTable', 'btUpload'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.treeTable.init({elem: "#appTable", column: 1});
    up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());
        var isCollect = false;
        $.each($("input[name='statusHid']"),function(index,date){
            if(date.value == 81 || date.value == 100){
                isCollect = true;
            }
        });
        if(!isCollect){
            bt.loading.hide();
            layer.alert(bt.lang('budget.message.collectcorp.num.null'), {icon: 7});
            return false;
        }
        var saveMethod = $("#saveMethod").val();
        var action = "/budget/fbsbudget/";
        if (saveMethod != "inputSave") {
            action = "/budget/fbsbudget/" + saveMethod;
        }
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.getParentDom().location.reload();
                    bt.navtab.closeCurrent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
        return false;
    });
    var bMoneyDiv = $('div[name="bMoneyDiv"]');
    for (var i = 0; i < bMoneyDiv.length; i++) {
        $(bMoneyDiv[i]).text(moneyFun.format($(bMoneyDiv[i]).text()));
    }

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml = function () {
        var url = basePath + "/budget/fbsmessage/list/" + $('#id').val() + "/" + $("#busName").val() + "?timestamp=" + new Date().getTime();
        if ($("#target").val() == "super") {
            url = basePath + "/budget/fbsmessage/list/" + $('#superaddId').val() + "/" + $("#busName").val() + "?timestamp=" + new Date().getTime();
        }
        $.ajax({
            url: url,
            method: 'GET',
            async: false,
            success: function (json) {
                $("#tagMsg").html("");
                $("#tagMsg").html(json);
                form.render();
            }
        });
    }
    $genMsgHtml();
});

///计划科目上报金额计算
function calItemMoney(data, obj) {
    var $ = bt.jquery, form = bt.form();
    var bMoney = obj.data();
    var monthsCount = $("#monthsCount").val();
    calSuperItemMoney(bMoney.itemcode, bMoney.superitemcode, bMoney.key, bMoney.isbeginperiod, bMoney.isendperiod);
    //期初期末科目计算
    calPeriodItemMoney(bMoney.key);
    if(bMoney.key > monthsCount){
        calMonthSuperItemMoney();
    }
    //校验金额是否超过可上报金额
    var canBMoneyLength = $("input[data-biz-name='canBMoney_" + bMoney.itemcode + "']").length;
    if (canBMoneyLength) {
        var canBMoney = moneyFun.toNumber(moneyType.get("canBMoney_" + bMoney.itemcode).val());
        var bMoneyInput = moneyFun.toNumber(moneyType.get("bMoney1_" + bMoney.itemcode).val());
        var sign = $("#sign_" + bMoney.itemcode).val();//资金流向：1流入 -1流出
        if (canBMoney < bMoneyInput && sign == -1) {
            layer.alert(bt.lang('budget.message.bmoney.gt.canbmoney'), {icon: 7});
            moneyType.get("bMoney" + bMoney.key + "_" + bMoney.itemcode).val(0);
            calSuperItemMoney(bMoney.itemcode, bMoney.superitemcode, bMoney.key);
            calMonthSuperItemMoney();
        }
    }
    $('form').find('input[data-period-name="bMoney' + (toWeeks+1) + '"][data-isEndPeriod="1"][data-key="' + (toWeeks+1) + '"][data-superItemCode=""]').each(function () {
        endItemCode = $(this).attr("data-itemCode");
    });
    $('form').find('input[data-period-name="bMoney' + (toWeeks+1) + '"][data-isBeginPeriod="1"][data-key="' + (toWeeks+1) + '"][data-superItemCode=""]').each(function () {
        beginItemCode = $(this).attr("data-itemCode");
    });
    //月期初
    for(var i=toWeeks;i<months.length;i++) {
        if(i==0){
            i = 1;
        }
        //获取期末大科目金额
        var endPeriodMoney = 0;
        //获取当前月大科目期末
        if ($("#bMoney" + i + "_" + endItemCode)) {
            endPeriodMoney = moneyFun.add(endPeriodMoney, moneyFun.toNumber($("#bMoney" + i + "_" + endItemCode).val()));
        }
        //设置下月大科目期初
        var keyNew = i + 1;
        if ($("#bMoney" + keyNew + "_" + beginItemCode)) {
            moneyType.get("bMoney" + keyNew + "_" + beginItemCode).val(endPeriodMoney);
            $("#bMoneyDiv" + keyNew + "_" + beginItemCode).text(moneyFun.format(endPeriodMoney));
        }
        calSuperItemMoney(beginItemCode, "", keyNew, 1, 0);
    }
};

//期初期末科目计算
function calPeriodItemMoney(key) {
    var length = $("#weekNumber"+toWeeks).val() - 1;
    var beginItemCode;
    var endItemCode;
    if(key > months.length) {
        for (; key < length; key++) {
            //获取期末大科目金额
            var endPeriodMoney = 0;
            //获取当前月大科目期末
            $('form').find('input[data-period-name="bMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"][data-superItemCode=""]').each(function () {
                endPeriodMoney = moneyFun.add(endPeriodMoney, moneyFun.toNumber($(this).val()));
            });
            //设置下月大科目期初
            var keyNew = key + 1;
            $('form').find('input[data-period-name="bMoney' + keyNew + '"][data-isBeginPeriod="1"][data-key="' + keyNew + '"][data-superItemCode=""]').each(function () {
                var itemCode = $(this).attr("data-itemCode");
                var superItemcode = $(this).attr("data-superItemCode");
                var isBeginPeriod = $(this).attr("data-isBeginPeriod");
                var isEndPeriod = $(this).attr("data-isEndPeriod");
                moneyType.get("bMoney" + keyNew + "_" + itemCode).val(endPeriodMoney);
                if ($("#bMoneyDiv" + keyNew + "_" + itemCode)) {
                    $("#bMoneyDiv" + keyNew + "_" + itemCode).text(moneyFun.format(endPeriodMoney));
                }
                calSuperItemMoney(itemCode, superItemcode, keyNew, isBeginPeriod, isEndPeriod);
            });
            // calPeriodItemMoney(keyNew);
        }
    }
}

//上级科目汇总计算
function calSuperItemMoney(itemCode, superItemcode, key, isBeginPeriod, isEndPeriod) {
    var $ = bt.jquery, form = bt.form();
    var bMoneyTotal = 0;

    //期末科目不做上级科目汇总
    if (isEndPeriod != "1") {
        //判断是否有上级科目
        if (superItemcode && superItemcode != "") {
            //获取上级科目的流向标志
            var signSuper = $("#sign_" + superItemcode).val();
            $('form').find('input[data-superItemCode="' + superItemcode + '"][data-key="' + key + '"]').each(function () {
                //获取子科目的流向标志，计算上级科目金额（必须增加流向标志，客户存在同一个科目下子科目既有流入又有流出）
                var sign = $("#sign_" + $(this).attr("data-itemCode")).val();
                if(signSuper == sign) {
                    bMoneyTotal = moneyFun.add(bMoneyTotal, moneyFun.toNumber($(this).val()));
                }else{
                    bMoneyTotal = moneyFun.sub(bMoneyTotal, moneyFun.toNumber($(this).val()));
                }
            });
            //上级科目金额
            var bMoneySuper = moneyFun.format(bMoneyTotal);
            moneyType.get("bMoney" + key + "_" + superItemcode).val(bMoneySuper);
            $("#bMoneyDiv" + key + "_" + superItemcode).text(bMoneySuper);
        }
    }

    //判断是否有计算公式 如：k100+k200
    $('form').find('input[name="itemRelation"]').each(function () {
        var itemRelation = $(this).val();
        if (itemRelation && itemRelation != "") {
            if (itemRelation.indexOf(itemCode) != -1 || itemRelation.indexOf(superItemcode) != -1) {
                caluteItemRelation($(this).attr("data-curItemCode"), itemRelation, key);
            }
        }
    });

    //判断上级科目是否存在上级科目
    var superItemcodeNew = $("[data-id='bMoney" + key + "_" + superItemcode + "']").attr("data-superItemCode");
    var keyNew = $("[data-id='bMoney" + key + "_" + superItemcode + "']").attr("data-key");
    var isBeginPeriodNew = $("[data-id='bMoney" + key + "_" + superItemcode + "']").attr("data-isBeginPeriod");
    var isEndPeriodNew = $("[data-id='bMoney" + key + "_" + superItemcode + "']").attr("data-isEndPeriod");
    if (superItemcodeNew && superItemcodeNew != "") {
        calSuperItemMoney(superItemcode, superItemcodeNew, keyNew, isBeginPeriodNew, isEndPeriodNew);
    }
};

function calMonthSuperItemMoney() {
    var $ = bt.jquery, form = bt.form();
    var monthSize = months.length;
    //月计划计算
    $('form').find('input[name="itemCode"]').each(function () {
        var isBeginPeriod = $("#isBeginPeriod_" + $(this).val()).val();
        var isEndPeriod = $("#isEndPeriod_" + $(this).val()).val();
        if (isBeginPeriod != "1" && isEndPeriod != "1") {
            var key = months.length + 1;
            for (var i = 1; i <= toWeeks; i++) {
                var length = $("#weekNumber"+i).val();
                var bMoneyTotal = 0;
                for (; key < length; key++) {
                    var bMoney = 0;
                    if (moneyType.get("bMoney" + key + "_" + $(this).val())) {
                        bMoney = moneyType.get("bMoney" + key + "_" + $(this).val()).val();
                        bMoneyTotal = moneyFun.add(bMoneyTotal, moneyFun.toNumber(bMoney));
                    }
                }
                var bMoneySuper = moneyFun.format(bMoneyTotal);
                if (moneyType.get("bMoney"+i+"_" + $(this).val())) {
                    moneyType.get("bMoney"+i+"_" + $(this).val()).val(bMoneySuper);
                    $("#bMoneyDiv"+i+"_" + $(this).val()).text(bMoneySuper);
                }
            }
        }
    });
    var begin = monthSize+1;
    var endPeriodMoney = 0;
    for (var i = 1; i <= toWeeks; i++) {
        var length = $("#weekNumber"+i).val();
        //月的期初 = 第一周期初
        $('form').find('input[data-name="bMoney'+i+'"][data-isBeginPeriod="1"][data-key="'+i+'"]').each(function () {
            var itemCode = $(this).attr("data-itemCode");
            var superItemcode = $(this).attr("data-superItemCode");
            var beginPeriodMoney = moneyFun.format(moneyType.get("bMoney" +begin + "_" + itemCode).val());
            moneyType.get("bMoney"+i+"_" + itemCode).val(moneyFun.toNumber(beginPeriodMoney));
            $("#bMoneyDiv"+i+"_" + itemCode).text(beginPeriodMoney);
        });
        //月的期末 = 最后周期末
        $('form').find('input[data-name="bMoney'+i+'"][data-isEndPeriod="1"][data-key="'+i+'"]').each(function () {
            var curItemCode = $(this).attr("data-itemCode");
            endPeriodMoney = moneyFun.format(moneyType.get("bMoney" + (length - 1) + "_" + curItemCode).val());
            moneyType.get("bMoney"+i+"_" + curItemCode).val(moneyFun.toNumber(endPeriodMoney));
            $("#bMoneyDiv"+i+"_" + curItemCode).text(endPeriodMoney);
        });
        begin = length;
    }
}

//关系运算符计算
function caluteItemRelation(curItemCode, itemRelation, key) {
    var $ = bt.jquery, form = bt.form();
    //替换运算符（+、-、*、/）获取科目数组
    var itemRelation1 = itemRelation.replace(/\+/g, ";");
    itemRelation1 = itemRelation1.replace(/\-/g, ";");
    itemRelation1 = itemRelation1.replace(/\*/g, ";");
    itemRelation1 = itemRelation1.replace(/\//g, ";");
    var itemCodes = itemRelation1.split(";");//科目
    //替换科目获取运算符数组
    var itemRelation2 = itemRelation;
    for (var i = 0; i < itemCodes.length; i++) {
        itemRelation2 = itemRelation2.replace(itemCodes[i], ";");
    }
    var operators = itemRelation2.split(";");//运算符+、-、*、/
    //金额计算
    var bMoneyTotal = 0;
    for (var i = 0; i < itemCodes.length - 1; i++) {
        var itemCode = itemCodes[i];//获取科目
        var itemCodeNext = itemCodes[i + 1];
        var operator = operators[i + 1];//获取运算符
        var bMoneySuper = 0;
        var bMoneySuperNext = 0;

        if (i != 0) {
            bMoneySuper = bMoneyTotal;
        } else {
            //包含‘k’则为科目
            if (itemCode.indexOf("k") != -1) {
                itemCode = itemCode.replace("k", "");
                //科目不存在，计算时按0计算
                var bMoneyLength = $("input[data-biz-name='bMoney" + key + "_" + itemCode + "']").length;
                if (!bMoneyLength) {
                    bMoneySuperNext = 0;
                } else {
                    bMoneySuper = moneyFun.mul(moneyFun.toNumber(moneyType.get("bMoney" + key + "_" + itemCode).val()), $("#sign_" + itemCode).val());
                }
            } else {
                bMoneySuper = moneyFun.toNumber(itemCode);
            }
        }
        if (itemCodeNext.indexOf("k") != -1) {
            itemCodeNext = itemCodeNext.replace("k", "");
            //科目不存在，计算时按0计算
            var bMoneyLength = $("input[data-biz-name='bMoney" + key + "_" + itemCodeNext + "']").length;
            if (!bMoneyLength) {
                bMoneySuperNext = 0;
            } else {
                bMoneySuperNext = moneyFun.mul(moneyFun.toNumber(moneyType.get("bMoney" + key + "_" + itemCodeNext).val()), $("#sign_" + itemCodeNext).val());
            }
        } else {
            bMoneySuperNext = moneyFun.toNumber(itemCodeNext);
        }
        //根据运算符计算
        var resultTemp = 0;
        if (operator == "+") {
            resultTemp = moneyFun.add(bMoneySuper, bMoneySuperNext);
        } else if (operator == "-") {
            resultTemp = moneyFun.sub(bMoneySuper, bMoneySuperNext);
        }
        bMoneyTotal = resultTemp;
    }
    //计算科目金额
    moneyType.get("bMoney" + key + "_" + curItemCode).val(bMoneyTotal);
    $("#bMoneyDiv" + key + "_" + curItemCode).text(moneyFun.format(bMoneyTotal));
}

$('#detailSubmit1').click(function () {
    bt.loading.show();
    setTimeout("toSubmit(11)", 200);
})
function toSubmit(status) {
    $("#detailSubmit").attr("disabled","disabled");
    $("#status").val(status);
    $("#attachmentId").val(up.getGroupId());
    var saveMethod = $("#saveMethod").val();
    var action = "/budget/fbsbudget/";
    if (saveMethod != "inputSave") {
        action = "/budget/fbsbudget/" + saveMethod;
    }
    var length = $("#weekNumber").val() - 1;
    var key = 1;
    //校验上个月的期末与下个月的期初是否相等
    for (; key < length; key++) {
        if(key ==  months.length){
            key++;
        }
        //获取本月大科目期末金额
        var curEndPeriodMoney = 0;
        $('form').find('input[data-period-name="bMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"][data-superItemCode=""]').each(function () {
            curEndPeriodMoney = moneyFun.add(curEndPeriodMoney, moneyFun.toNumber($(this).val()));
        });
        //获取下个月大科目期初金额
        var keyNew = key + 1;
        var nextBeginPeriodMoney = 0;
        $('form').find('input[data-period-name="bMoney' + keyNew + '"][data-isBeginPeriod="1"][data-key="' + keyNew + '"][data-superItemCode=""]').each(function () {
            nextBeginPeriodMoney = moneyFun.add(nextBeginPeriodMoney, moneyFun.toNumber($(this).val()));
        });
        if (moneyFun.sub(curEndPeriodMoney, nextBeginPeriodMoney) != 0) {
            bt.loading.hide();
            if(key > months.length) {
                layer.alert(bt.lang('budget.message.resolve.month.periodmoney.mismatch', [(key - months.length), (keyNew - months.length)]), {icon: 7});
            }else{
                layer.alert(bt.lang('budget.message.resolve.year.periodmoney.mismatch', [(months[key-1]), (months[keyNew-1])]), {icon: 7});
            }
            return false;
        }
    }
    $.ajax({
        type: "POST",
        url: basePath + action,
        data: $('#inputForm').serialize(),
        dataType: "json",
        async:false,
        success: function(result){
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.getParentDom().location.reload();
                    bt.navtab.closeCurrent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }
    });
}
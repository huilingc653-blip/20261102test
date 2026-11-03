var moneyFun;
var moneyType;
bt.use(['form', 'layedit', 'btdate', 'moneyType', 'toLigerTree', 'btUpload','btImport', 'selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.toLigerTree.init({
        elem: '#appTable',
        newElem: '#maingrid1',
        rownumbers: true,//序号列数，默为无， 1代表左边第一列为序号列
        isDoubleHeader: true,//是否有双表头
        allowHideColumn: false,
        resize:false,
        height: "80%",
        fixedLeftColumns: 2,
    });
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    $("input").each(function() {
        if ($(this).attr("readonly") == "readonly") {
            calItemMoney($(this).val(),$(this));
        };
    });
    //币种回调函数
    bt.curIdCallback = function (data) {
        //获取汇率
        $.ajax({
            url: basePath + "/basic/business/fcexcrateadjust?getExcrateAdjustForBudget",
            method: 'POST',
            data: {corpId: $("#corpId").val(),curId: data.id},
            dataType: 'json',
            success: function (data) {
                if (data.obj) {
                    $("#exchangeRate").val(data.obj);
                }else{
                    $("#exchangeRate").val('');
                }
                form.render();
            }
        });
    };
    var selectType = bt.selectType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());
        var action = "/budget/fbsbudget/inputRollWeekSave";
        if($("#isApprove").val()){
            action = "/budget/fbsbudget/inputRollWeekSaveApprove";
        }

        var exchangeRate = $("#exchangeRate").val();
        if(!exchangeRate){
            bt.loading.hide();
            layer.alert(bt.lang('budget.message.exchangeRate.null'), {icon: 7});
            return false;
        }

        if (!checkInputMoney()) {
            bt.loading.hide();
            return false;
        }
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    if($("#isApprove").val()) {
                        bt.navtab.refreshCurrent();
                    }else {
                        var parentWindow = bt.navtab.getParentDom();
                        if(parentWindow.document.getElementById("budgetBody")) {
                            parentWindow.document.getElementById("budgetBody").className = "layui-hide";
                        }
                        parentWindow.location.reload();
                        bt.navtab.closeCurrent();
                    }
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
        return false;
    });

    //监听提交
    form.on('submit(submitAuto)', function (data) {
        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());
        var action = "/budget/fbsbudget/inputRollWeekSave";
        if($("#isApprove").val()){
            action = "/budget/fbsbudget/inputRollWeekSaveApprove";
        }
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            bt.loading.hide();
            if (result.success) {
                if (bt.navtab) {
                    if($("#isApprove").val()) {
                        bt.navtab.refreshCurrent();
                    }else {
                        var parentWindow = bt.navtab.getParentDom();
                        if(parentWindow.document.getElementById("budgetBody")) {
                            parentWindow.document.getElementById("budgetBody").className = "layui-hide";
                        }
                        parentWindow.location.reload();
                        location.reload();
                    }
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
        return false;
    });

    /**
     * 查看本期明细
     */
    dtlView = function () {
        var timeType = $("#timeType").val();
        var corpId = $("#corpId").val();
        var beginDate = $("#beginDate").val();
        var endDate = $("#endDate").val();
        beginDate = bt.date.format(beginDate, "yyyy-MM-dd");
        endDate = bt.date.format(endDate, "yyyy-MM-dd");
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/budget/fbsbudget/curView/" + corpId + "/" + timeType + "/" + beginDate + "/" + endDate + "/input");
            return;
        }
    };
    /**
     * 查看收支净额
     */
    showNetAmount = function (){
        parent.layer.open({
            type: 2,
            area: ['1020px', '530px'],
            maxmin: true,
            content: basePath + "/budget/fbsbudget/showNetAmount/" + $('#id').val()
        });
    }

    /**
     * 计划上报-明细录入
     * @param id
     * @param detailId
     * @param canBMoney
     */
    inputDtl = function (id, detailId, canBMoney) {
        var url = basePath + "/budget/fbsbudgetdetail/inputDtl?budgetId=" + id + "&detailId=" + detailId + "&canBMoney=" + canBMoney;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.dtl'), url);
            return;
        }
    };
    /**
     * 查看明细
     * @param id
     * @param detailId
     * @param target
     */
    viewDtl = function (id, detailId) {
        var action = "/budget/fbsbudgetdetail/view?budgetId=" + id + "&detailId=" + detailId;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + action);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + action
        });
    };


    /**
     * 导入功能
     */
    importExcel = function () {
        bt.btImport.init({
            code: "50000001",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 2,
            params: {
                timeType: $("#timeType").val(),
                beginDate: $("#beginDate").val(),
                endDate: $("#endDate").val(),
                corpId: $("#corpId").val()
            },
            url: basePath + "/budget/fbsBudgetDownloadAndImport/uploadRoll",
            downUrl: basePath + "/budget/fbsBudgetDownloadAndImport/downloadBudgetRollWeek?id=" + $("#id").val() + "&timeType=" + $("#timeType").val() ,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                var flag = false;
                $.each(data, function (i, item) {
                    // 模板存在单位未分配的计划科目
                    try {
                        if (item.itemCode != "" && item.itemCode != "undefined" && item.itemCode != undefined) {
                            var keys = Object.keys(item);
                            for (var j = 0; j < keys.length; j++) {
                                if ($("input[data-biz-name='" + keys[j] + "_" + item.itemCode + "']").length) {
                                    if (!moneyType.get(keys[j] + "_" + item.itemCode)) {
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }catch (e) {
                        flag = true;
                    }
                })
                if(flag){
                    // 模板不符
                    parent.layer.alert(bt.lang("budget.message.import.mismatch"),{icon: 2});
                } else {
                    $.each(data, function (i, item) {
                        if (item.itemCode != "" && item.itemCode != "undefined" && item.itemCode != undefined) {
                            var keys = Object.keys(item);
                            for (var j = 0; j < keys.length; j++) {
                                if ($("input[data-biz-name='" + keys[j] + "_" + item.itemCode + "']").length) {
                                    if (moneyType.get(keys[j] + "_" + item.itemCode).attr("readonly") == undefined) {
                                        moneyType.get(keys[j] + "_" + item.itemCode).val(item[keys[j]]);
                                        calItemMoney(item[keys[j]], $("input[data-biz-name='" + keys[j] + "_" + item.itemCode + "']"), 0);
                                    }
                                }
                            }
                        }
                    })
                }
            }
        });
    };


    /**
     * 计划上报-明细导入功能
     */
    importDtlExcel = function () {
        bt.loading.show();
        var up = bt.btImport.init({
            code: "50000004",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            params: {
                timeType: $("#timeType").val(),
                flag: $("#target").val(),
                id:$("#id").val(),
                beginDate:$("#beginDate").val(),
                endDate:$("#endDateRoll").val()
            },
            downUrl: basePath + "/budget/fbsBudgetDownloadAndImport/downloadFbsBudgetDetailByInput?id=" + $("#id").val() + "&timeType=" + $("#timeType").val() + "&target=" + $("#target").val(),
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                bt.loading.show();
                $.ajax({
                    type: "POST",
                    url: basePath + "/budget/fbsbudgetdetail/saveRollWeekImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        try {
                            bt.loading.show();
                            bt.each(result.attributes, function (key, v) {
                                moneyType.get("bMoney" + key).val(v);
                                calItemMoney(v, $('input[data-biz-name ="bMoney' + key +'"]'));
                                if (v > 0) {
                                    moneyType.get("bMoney" + key).attr("readonly", "readonly");
                                } else {
                                    moneyType.get("bMoney" + key).removeAttr('readonly');
                                }
                            });
                        }catch (e){}
                        setTimeout(function () {
                            $("#saveBut").trigger('click');
                            bt.loading.hide();
                        }, 5000);
                    }
                });
            }
        });
    };

    /**
     * 生成日志列表
     * @return
     */
    $genMsgHtml= function() {
        var url=basePath + "/budget/fbsmessage/list/"+$('#id').val()+"/"+$("#busName").val();
        if($("#target").val() == "superadd"){
            url=basePath + "/budget/fbsmessage/list/"+$('#superaddId').val()+"/"+$("#busName").val();
        }
        $.ajax({
            url:url,
            method: 'GET',
            async:false,
            success: function (json) {
                $("#tagMsg").html("");
                $("#tagMsg").html(json);
                form.render();
            }
        });
    }
    $genMsgHtml();
});

//计划科目上报金额计算
function calItemMoney(data, obj) {
    var $ = bt.jquery, form = bt.form();
    var bMoney = obj.data();
    var weekCount = $("#weekCount").val();
    calSuperItemMoney(bMoney.itemcode, bMoney.superitemcode, bMoney.key, bMoney.isbeginperiod, bMoney.isendperiod);
    //期初期末科目计算
    calPeriodItemMoney(bMoney.key);
    if(bMoney.key > weekCount){
        calweeksuperItemMoney();
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
            calweeksuperItemMoney();
        }
    }
    var endItemCode = "";
    var beginItemCode = "";
    $('form').find('input[data-period-name="bMoney' + (wRollSubperiod+1) + '"][data-isEndPeriod="1"][data-key="' + (wRollSubperiod+1) + '"][data-superItemCode=""]').each(function () {
        endItemCode = $(this).attr("data-itemCode");
    });
    $('form').find('input[data-period-name="bMoney' + (wRollSubperiod+1) + '"][data-isBeginPeriod="1"][data-key="' + (wRollSubperiod+1) + '"][data-superItemCode=""]').each(function () {
        beginItemCode = $(this).attr("data-itemCode");
    });
    if(beginItemCode != "") {
        var i = wRollSubperiod;
        if (i == 0) {
            i = 1;
        }
        if (bMoney.key <= weeks.length) {
            i = bMoney.key;
        }
        for (; i < weeks.length; i++) {
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
        calSuperItemMoney(beginItemCode, "", 1, 1, 0);
    }
};

//期初期末科目计算
function calPeriodItemMoney(key) {
    var length = $("#dayNumber"+wRollSubperiod).val() - 1;
    var beginItemCode;
    var endItemCode;
    if(key > weeks.length) {
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

function calweeksuperItemMoney() {
    var $ = bt.jquery, form = bt.form();
    var weeksize = weeks.length;
    //周计划计算
    $('form').find('input[name="itemCode"]').each(function () {
        var isBeginPeriod = $("#isBeginPeriod_" + $(this).val()).val();
        var isEndPeriod = $("#isEndPeriod_" + $(this).val()).val();
        if (isBeginPeriod != "1" && isEndPeriod != "1") {
            var key = weeks.length + 1;
            for (var i = 1; i <= wRollSubperiod; i++) {
                var length = $("#dayNumber"+i).val();
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
    var begin = weeksize+1;
    var endPeriodMoney = 0;
    for (var i = 1; i <= wRollSubperiod; i++) {
        var length = $("#dayNumber" + i).val();
        //月的期初 = 第一周期初
        $('form').find('input[data-name="bMoney' + i + '"][data-isBeginPeriod="1"][data-key="' + i + '"]').each(function () {
            var itemCode = $(this).attr("data-itemCode");
            var superItemcode = $(this).attr("data-superItemCode");
            var beginPeriodMoney = moneyFun.format(moneyType.get("bMoney" + begin + "_" + itemCode).val());
            moneyType.get("bMoney" + i + "_" + itemCode).val(moneyFun.toNumber(beginPeriodMoney));
            $("#bMoneyDiv" + i + "_" + itemCode).text(beginPeriodMoney);
        });
        //月的期末 = 最后周期末
        $('form').find('input[data-name="bMoney' + i + '"][data-isEndPeriod="1"][data-key="' + i + '"]').each(function () {
            var curItemCode = $(this).attr("data-itemCode");
            endPeriodMoney = moneyFun.format(moneyType.get("bMoney" + (length - 1) + "_" + curItemCode).val());
            moneyType.get("bMoney" + i + "_" + curItemCode).val(moneyFun.toNumber(endPeriodMoney));
            $("#bMoneyDiv" + i + "_" + curItemCode).text(endPeriodMoney);
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


/**
 * 提交时验证上报金额不可以超过可用金额
 */
function checkInputMoney() {
    var result = true;

    //校验上报/追加金额必须填写
    /*var bMoneyTotal = 0;
    $('form').find('input[data-name="bMoney1"]').each(function () {
        bMoneyTotal = moneyFun.add(bMoneyTotal, Math.abs(moneyFun.toNumber($(this).val())));
    });
    if (bMoneyTotal == 0) {
        bt.loading.hide();
        if ($("#target").val() == "super") {
            layer.alert(bt.lang('budget.message.btempmoney.gt.zero'), {icon: 7});
        } else {
            layer.alert(bt.lang('budget.message.bmoney.gt.zero'), {icon: 7});
        }
        return false;
    }*/

    //校验金额是否超过可上报金额
    if ($("input[data-name='canBMoney']").length) {
        $('form').find('input[data-name="bMoney1"]').each(function () {
            var itemcode = $(this).data("itemcode");
            var superitemcode = $(this).data("superitemcode");
            var key = $(this).data("key");
            var canBMoney = moneyFun.toNumber(moneyType.get("canBMoney_" + itemcode).val());
            var bMoneyInput = moneyFun.toNumber($(this).val());
            var sign = $("#sign_" + itemcode).val();//资金流向：1流入 -1流出
            if (canBMoney < bMoneyInput && sign == -1) {
                bt.loading.hide();
                layer.alert(bt.lang('budget.message.bmoney.gt.canbmoney'), {icon: 7});
                moneyType.get("bMoney" + key + "_" + itemcode).val(0);
                calSuperItemMoney(itemcode, superitemcode, key);
                result = false;
            }
        });
    }

    var length = $("#dayNumber"+wRollSubperiod).val();
    //校验期初子科目与大科目金额是否相等
    for (var key = 2; key <= length; key++) {
        var beginPeriodMoney = 0;
        var beginPeriodTotal = 0;
        var superItemcode = "";
        var itemCode = "";
        //获取期初大科目的金额
        $('form').find('input[data-period-name="bMoney' + key + '"][data-isBeginPeriod="1"][data-key="' + key + '"][data-superItemCode=""]').each(function () {
            itemCode = $(this).attr("data-itemCode");
            beginPeriodMoney = moneyFun.toNumber($(this).val());
        });
        $('form').find('input[data-period-name="bMoney' + key + '"][data-isBeginPeriod="1"][data-key="' + key + '"][data-superItemCode="' + itemCode + '"]').each(function () {
            beginPeriodTotal = moneyFun.add(beginPeriodTotal, moneyFun.toNumber($(this).val()));
        });
        //存在下级科目才校验
        var itemLength = $('input[data-period-name="bMoney' + key + '"][data-isBeginPeriod="1"][data-superItemCode="' + itemCode + '"]').length;
        if (itemLength && itemLength > 0) {
            if (moneyFun.sub(beginPeriodMoney, beginPeriodTotal) != 0) {
                bt.loading.hide();
                if(key > weeks.length) {
                    layer.alert(bt.lang('\tbudget.message.resolve.day.mismatch', [(key - weeks.length), itemCode]), {icon: 7});
                }else{
                    layer.alert(bt.lang('budget.message.resolve.month.mismatch', [(key), itemCode]), {icon: 7});
                }
                result = false;
                return false;
            }
        }
    }

    //校验期末子科目与大科目金额是否相等
    for (var key = 2; key <= length; key++) {
        var endPeriodMoney = 0;
        var endPeriodTotal = 0;
        var superItemcode = "";
        var itemCode = "";
        //获取期末大科目的金额
        $('form').find('input[data-period-name="bMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"][data-superItemCode=""]').each(function () {
            itemCode = $(this).attr("data-itemCode");
            endPeriodMoney = moneyFun.toNumber($(this).val());
        });
        $('form').find('input[data-period-name="bMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"][data-superItemCode="' + itemCode + '"]').each(function () {
            endPeriodTotal = moneyFun.add(endPeriodTotal, moneyFun.toNumber($(this).val()));
        });
        //存在下级科目才校验
        var itemLength = $('input[data-period-name="bMoney' + key + '"][data-isEndPeriod="1"][data-superItemCode="' + itemCode + '"]').length;
        if (itemLength && itemLength > 0) {
            if (moneyFun.sub(endPeriodMoney, endPeriodTotal) != 0) {
                bt.loading.hide();
                if(key > weeks.length) {
                    layer.alert(bt.lang('\tbudget.message.resolve.day.mismatch', [(key - weeks.length), itemCode]), {icon: 7});
                }else{
                    layer.alert(bt.lang('budget.message.resolve.month.mismatch', [(key), itemCode]), {icon: 7});
                }
                result = false;
                return false;
            }
        }
    }
    // //校验上个月的期末与下个月的期初是否相等
    // for (var key = 4; key < length; key++) {
    //     //获取本月大科目期末金额
    //     var curEndPeriodMoney = 0;
    //     $('form').find('input[data-period-name="bMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"][data-superItemCode=""]').each(function () {
    //         curEndPeriodMoney = moneyFun.add(curEndPeriodMoney, moneyFun.toNumber($(this).val()));
    //     });
    //     //获取下个月大科目期初金额
    //     var keyNew = key + 1;
    //     var nextBeginPeriodMoney = 0;
    //     $('form').find('input[data-period-name="bMoney' + keyNew + '"][data-isBeginPeriod="1"][data-key="' + keyNew + '"][data-superItemCode=""]').each(function () {
    //         nextBeginPeriodMoney = moneyFun.add(nextBeginPeriodMoney, moneyFun.toNumber($(this).val()));
    //     });
    //     if (moneyFun.sub(curEndPeriodMoney, nextBeginPeriodMoney) != 0) {
    //         bt.loading.hide();
    //         layer.alert(bt.lang('budget.message.resolve.month.periodmoney.mismatch', [(key - 4), (keyNew - 4)]), {icon: 7});
    //         result = false;
    //         return false;
    //     }
    // }

    return result;
}

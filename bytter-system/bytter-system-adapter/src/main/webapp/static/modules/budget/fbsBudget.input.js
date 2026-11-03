var moneyFun;
var moneyType;
bt.use(['form', 'layedit', 'btdate', 'moneyType', 'toLigerTree', 'btUpload', 'btImport', 'selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.toLigerTree.init({
        elem: '#appTable',
        newElem: '#maingrid1',
        rownumbers: true,//序号列数，默为无， 1代表左边第一列为序号列
        isDoubleHeader: false,//是否有双表头
        allowHideColumn: false,
        resize:false,
        height: "80%",
        fixedLeftColumns: 0,
    });
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();

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

    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());
        //校验上报/追加金额必须填写
        /*var bMoneyTotal = 0;
        var bMoneyName = "bMoney";
        if ($("#target").val() == "super") {
            bMoneyName = "btempMoney";
        }
        $('form').find('input[data-name="' + bMoneyName + '"]').each(function () {
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
        var exchangeRate = $("#exchangeRate").val();
        if(!exchangeRate){
            bt.loading.hide();
            layer.alert(bt.lang('budget.message.exchangeRate.null'), {icon: 7});
            return false;
        }

        var action = "/budget/fbsbudget/inputSave";
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    var parentWindow = bt.navtab.getParentDom();
                    if(parentWindow.document.getElementById("budgetBody")) {
                        parentWindow.document.getElementById("budgetBody").className = "layui-hide";
                    }
                    parentWindow.location.reload();
                    bt.navtab.closeCurrent();
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
        var action = "/budget/fbsbudget/inputSave";
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            if (result.success) {
                if (bt.navtab) {
                    var parentWindow = bt.navtab.getParentDom();
                    if(parentWindow.document.getElementById("budgetBody")) {
                        parentWindow.document.getElementById("budgetBody").className = "layui-hide";
                    }
                    parentWindow.location.reload();
                    location.reload();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
        return false;
    });


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
            downUrl: basePath + "/budget/fbsBudgetDownloadAndImport/downloadBudget?id=" + $("#id").val() + "&timeType=" + $("#timeType").val() + "&target=" + $("#target").val(),
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                var flag = false;
                $.each(data, function (i, item) {
                    if (!item.item) {
                        // 模板存在系统不存在的计划科目
                        flag = true;
                    }
                    // 模板存在单位未分配的计划科目
                    try {
                        if ("super" === $("#target").val()) {
                            if (!moneyType.get("btempMoney_" + item.itemCode)) {
                                flag = true;
                            }
                        } else {
                            if (!moneyType.get("bMoney_" + item.itemCode)) {
                                flag = true;
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
                        if (item.itemCode != "" && item.itemCode != "undefined" && item.itemCode != undefined && item.item.isWrite == "1") {
                            if ("super" === $("#target").val()) {
                                if(moneyType.get("btempMoney_" + item.itemCode).attr("readonly") == undefined) {
                                    moneyType.get("btempMoney_" + item.itemCode).val(item.bsuperMoney);
                                    calItemMoney(item.bsuperMoney, $("input[data-biz-name='btempMoney_" + item.itemCode + "']"));
                                }
                            } else {
                                if(moneyType.get("bMoney_" + item.itemCode).attr("readonly") == undefined) {
                                    moneyType.get("bMoney_" + item.itemCode).val(item.bMoney);
                                    calItemMoney(item.bMoney, $("input[data-biz-name='bMoney_" + item.itemCode + "']"));
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
                endDate:$("#endDate").val()
            },
            downUrl: basePath + "/budget/fbsBudgetDownloadAndImport/downloadFbsBudgetDetailByInput?id=" + $("#id").val() + "&timeType=" + $("#timeType").val() + "&target=" + $("#target").val(),
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/budget/fbsbudgetdetail/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        const map = new Map();
                        $.each(data, function (i, item) {
                            if ("super" === $("#target").val()) {
                                var money = moneyFun.toNumber(item.bsuperMoney);
                                if(map.has(item.itemCode)){
                                    var sumMoney = moneyFun.add(money, moneyFun.toNumber(map.get(item.itemCode)));
                                    map.set(item.itemCode,sumMoney);
                                }else{
                                    map.set(item.itemCode,money);
                                }
                            } else {
                                var money = moneyFun.toNumber(item.bMoney);
                                if(map.has(item.itemCode)){
                                    var sumMoney = moneyFun.add(money, moneyFun.toNumber(map.get(item.itemCode)));
                                    map.set(item.itemCode,sumMoney);
                                }else{
                                    map.set(item.itemCode,money);
                                }
                            }
                        })
                        //循环设置
                        map.forEach(function(value,key){
                            if ("super" === $("#target").val()) {
                                moneyType.get("btempMoney_" + key).val(value);
                                calItemMoney(value, $("input[data-biz-name='btempMoney_" + key + "']"));
                                if (value > 0) {
                                    moneyType.get("btempMoney_" + key).attr("readonly", "readonly");
                                } else {
                                    moneyType.get("btempMoney_" + key).removeAttr('readonly');
                                }
                            } else {
                                moneyType.get("bMoney_" + key).val(value);
                                calItemMoney(value, $("input[data-biz-name='bMoney_" + key + "']"));
                                if (value > 0) {
                                    moneyType.get("bMoney_" + key).attr("readonly", "readonly");
                                } else {
                                    moneyType.get("bMoney_" + key).removeAttr('readonly');
                                }
                            }
                        });
                        bt.loading.show();
                        setTimeout(function () {
                            $("#saveBut").trigger('click');
                        }, 5000);
                    }
                });
            }
        });
    };

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
     * 计划追加-明细录入
     * @param id
     * @param detailId
     * @param canBMoney
     */
    inputDtlSuper = function (id, detailId, canBMoney) {
        var url = basePath + "/budget/fbsbudgetdetail/inputDtl?superaddId=" + id + "&detailId=" + detailId + "&canBMoney=" + canBMoney;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.dtl'), url);
            return;
        }
    };
});

/**
 * 计划科目上报金额计算
 * @param data
 * @param obj
 */
function calItemMoney(data, obj) {
    var $ = bt.jquery, form = bt.form();
    var bMoney = obj.data();
    //流出科目校验金额是否超过可上报金额
    var canBMoneyLength = $("input[data-biz-name='canBMoney_" + bMoney.itemcode + "']").length;
    if (canBMoneyLength) {
        var canBMoney = moneyFun.toNumber(moneyType.get("canBMoney_" + bMoney.itemcode).val());
        var sign = $("#sign_" + bMoney.itemcode).val();//资金流向：1流入 -1流出
        var inputMoney = 0;
        //追加校验
        if (bMoney.name == "btempMoney") {
            inputMoney = moneyFun.toNumber(moneyType.get("btempMoney_" + bMoney.itemcode).val());
            var remainBMoney = moneyFun.toNumber(moneyType.get("remainBMoney_" + bMoney.itemcode).val());
            //追加金额大于0，与可上报金额比较，追加金额小于0，与留存金额比较
            if (inputMoney > 0) {
                if (canBMoney < inputMoney && sign == -1) {
                    layer.alert(bt.lang('budget.message.btempmoney.gt.canbmoney'), {icon: 7});
                    moneyType.get("btempMoney_" + bMoney.itemcode).val(0);
                }
            } else {
                var inputMoneyNew = moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(inputMoney), -1));
                if (remainBMoney < inputMoneyNew) {
                    layer.alert(bt.lang('budget.message.btempmoney.gt.remainbmoney'), {icon: 7});
                    moneyType.get("btempMoney_" + bMoney.itemcode).val(0);
                }
            }
        } else {
            inputMoney = moneyFun.toNumber(moneyType.get("bMoney_" + bMoney.itemcode).val());
            if (canBMoney < inputMoney && sign == -1) {
                layer.alert(bt.lang('budget.message.bmoney.gt.canbmoney'), {icon: 7});
                moneyType.get("bMoney_" + bMoney.itemcode).val(0);
            }
        }
    }
    //追加校验
    if (bMoney.name == "btempMoney") {
        inputMoney = moneyFun.toNumber(moneyType.get("btempMoney_" + bMoney.itemcode).val());
        var remainBMoney = moneyFun.toNumber(moneyType.get("remainBMoney_" + bMoney.itemcode).val());
        //追加金额大于0，与可上报金额比较，追加金额小于0，与留存金额比较
        if (inputMoney < 0) {
            var inputMoneyNew = moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(inputMoney), -1));
            if (remainBMoney < inputMoneyNew) {
                layer.alert(bt.lang('budget.message.btempmoney.gt.remainbmoney'), {icon: 7});
                moneyType.get("btempMoney_" + bMoney.itemcode).val(0);
            }
        }
    }
    calSuperItemMoney(bMoney.itemcode, bMoney.superitemcode, bMoney.name);
};

/**
 * 上级科目汇总计算
 * @param itemCode
 * @param superItemcode
 * @param bMoneyName
 */
function calSuperItemMoney(itemCode, superItemcode, bMoneyName) {
    var $ = bt.jquery, form = bt.form();
    var bMoneyTotal = 0;

    //判断是否有上级科目
    if (superItemcode && superItemcode != "") {
        //获取上级科目的流向标志
        var signSuper = $("#sign_" + superItemcode).val();
        $('form').find('input[data-superItemCode="' + superItemcode + '"][data-name="' + bMoneyName + '"]').each(function () {
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
        moneyType.get(bMoneyName + "_" + superItemcode).val(bMoneySuper);
        $("#" + bMoneyName + "Div_" + superItemcode).text(bMoneySuper);
    }

    //判断是否有计算公式 如：k100+k200
    $('form').find('input[name="itemRelation"]').each(function () {
        var itemRelation = $(this).val();
        if (itemRelation && itemRelation != "") {
            if (itemRelation.indexOf(itemCode) != -1 || itemRelation.indexOf(superItemcode) != -1) {
                caluteItemRelation($(this).attr("data-curItemCode"), itemRelation, bMoneyName);
            }
        }
    });

    //判断上级科目是否存在上级科目
    var superItemcodeNew = $("[data-id='" + bMoneyName + "_" + superItemcode + "']").attr("data-superItemCode");
    var bMoneyNameNew = $("[data-id='" + bMoneyName + "_" + superItemcode + "']").attr("data-name");
    if (superItemcodeNew && superItemcodeNew != "") {
        calSuperItemMoney(superItemcode, superItemcodeNew, bMoneyNameNew);
    }
};

/**
 * 关系运算符计算
 * @param curItemCode
 * @param itemRelation
 * @param bMoneyName
 */
function caluteItemRelation(curItemCode, itemRelation, bMoneyName) {
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
                var bMoneyLength = $("input[data-biz-name='" + bMoneyName + "_" + itemCode + "']").length;
                if (!bMoneyLength) {
                    bMoneySuper = 0;
                } else {
                    bMoneySuper = moneyFun.mul(moneyFun.toNumber(moneyType.get(bMoneyName + "_" + itemCode).val()), $("#sign_" + itemCode).val());
                }
            } else {
                bMoneySuper = moneyFun.toNumber(itemCode);
            }
        }
        if (itemCodeNext.indexOf("k") != -1) {
            itemCodeNext = itemCodeNext.replace("k", "");
            //科目不存在，计算时按0计算
            var bMoneyLength = $("input[data-biz-name='" + bMoneyName + "_" + itemCodeNext + "']").length;
            if (!bMoneyLength) {
                bMoneySuperNext = 0;
            } else {
                bMoneySuperNext = moneyFun.mul(moneyFun.toNumber(moneyType.get(bMoneyName + "_" + itemCodeNext).val()), $("#sign_" + itemCodeNext).val());
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
    moneyType.get(bMoneyName + "_" + curItemCode).val(bMoneyTotal);
    $("#" + bMoneyName + "Div_" + curItemCode).text(moneyFun.format(bMoneyTotal));
}

/**
 * 重置
 */
function clearAll() {
    bt.jquery(".clearDiv").text("");
    var target = $("#target").val();
    if (target == "super") {
        var btempMoneyDiv = $('div[name="btempMoneyDiv"]');//追加金额
        for (var i = 0; i < btempMoneyDiv.length; i++) {
            $(btempMoneyDiv[i]).text(moneyFun.format($(btempMoneyDiv[i]).parent().find("input[name=btempMoney]").val()));
        }
    } else {
        //格式化金额
        var bMoneyDiv = $('div[name="bMoneyDiv"]');//上报金额
        for (var i = 0; i < bMoneyDiv.length; i++) {
            $(bMoneyDiv[i]).text(moneyFun.format($(bMoneyDiv[i]).parent().find("input[name=bMoney]").val()));
        }
    }

}


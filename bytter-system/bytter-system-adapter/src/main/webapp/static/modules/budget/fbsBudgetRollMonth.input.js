var moneyFun;
var moneyType;
bt.use(['form', 'layedit', 'btdate', 'moneyType', 'toLigerTree', 'btUpload', 'btImport', 'selectType'], function () {
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
            calItemMoney($(this).val(),$(this),0);
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

        var exchangeRate = $("#exchangeRate").val();
        if(!exchangeRate){
            bt.loading.hide();
            layer.alert(bt.lang('budget.message.exchangeRate.null'), {icon: 7});
            return false;
        }

        //校验上报金额必须填写
        /*var bMoneyTotal = 0;
        $('form').find('input[data-name="bMoney1"]').each(function () {
            bMoneyTotal = moneyFun.add(bMoneyTotal, Math.abs(moneyFun.toNumber($(this).val())));
        });
        if (bMoneyTotal == 0) {
            bt.loading.hide();
            layer.alert(bt.lang('budget.message.bmoney.gt.zero'), {icon: 7});
            return false;
        }*/

        var action = "/budget/fbsbudget/inputRollMonthSave";
        if($("#isApprove").val()){
            action = "/budget/fbsbudget/inputRollMonthSaveApprove";
        }
        if (!checkInputMoney()) {
            bt.loading.hide();
            return false;
        }
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    if($("#isApprove").val()) {
                        bt.navtab.refreshCurrent();
                    }else {
                        var parentWindow = bt.navtab.getParentDom();
                        parentWindow.document.getElementById("budgetBody").className ="layui-hide";
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
        var action = "/budget/fbsbudget/inputRollMonthSave";
        if($("#isApprove").val()){
            action = "/budget/fbsbudget/inputRollMonthSaveApprove";
        }
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            if (result.success) {
                if (bt.navtab) {
                    if($("#isApprove").val()) {
                        bt.navtab.refreshCurrent();
                    }else {
                        var parentWindow = bt.navtab.getParentDom();
                        parentWindow.document.getElementById("budgetBody").className ="layui-hide";
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
     * 导入功能
     */
    importExcel = function () {
        bt.btImport.init({
            code: "50000001",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 2,
            downUrl: basePath + "/budget/fbsBudgetDownloadAndImport/downloadBudgetRollMonth?id=" + $("#id").val() + "&timeType=" + $("#timeType").val() + "&target=" + $("#target").val() + "&beginDate=" + $("#beginDate").val(),
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                var flag = false;
                $.each(data, function (i, item) {
                    if (!item.item) {
                        // 模板存在系统不存在的计划科目
                        flag = true;
                    }
                    // 模板存在单位未分配的计划科目
                    try {
                        $.each(months, function (index, month) {
                            if (item.itemCode != "" && item.itemCode != "undefined" && item.itemCode != undefined && item.item.isWrite == "1") {
                                if (!moneyType.get("bMoney" + month + "_" + item.itemCode)) {
                                    flag = true;
                                }
                            }
                        })
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
                            var monthMoney = [];
                            $.each(months, function (index, month) {
                                if (item.bMoney1 && 1 == month) {
                                    monthMoney.push(item.bMoney1);
                                }
                                if (item.bMoney2 && 2 == month) {
                                    monthMoney.push(item.bMoney2);
                                }
                                if (item.bMoney3 && 3 == month) {
                                    monthMoney.push(item.bMoney3);
                                }
                                if (item.bMoney4 && 4 == month) {
                                    monthMoney.push(item.bMoney4);
                                }
                                if (item.bMoney5 && 5 == month) {
                                    monthMoney.push(item.bMoney5);
                                }
                                if (item.bMoney6 && 6 == month) {
                                    monthMoney.push(item.bMoney6);
                                }
                                if (item.bMoney7 && 7 == month) {
                                    monthMoney.push(item.bMoney7);
                                }
                                if (item.bMoney8 && 8 == month) {
                                    monthMoney.push(item.bMoney8);
                                }
                                if (item.bMoney9 && 9 == month) {
                                    monthMoney.push(item.bMoney9);
                                }
                                if (item.bMoney10 && 10 == month) {
                                    monthMoney.push(item.bMoney10);
                                }
                                if (item.bMoney11 && 11 == month) {
                                    monthMoney.push(item.bMoney11);
                                }
                                if (item.bMoney12 && 12 == month) {
                                    monthMoney.push(item.bMoney12);
                                }
                            });
                            if (monthMoney && monthMoney.length != 0) {
                                $.each(monthMoney, function (index, money) {
                                    if (moneyType.get("bMoney" + (index + 1) + "_" + item.itemCode).attr("readonly") == undefined) {
                                        moneyType.get("bMoney" + (index + 1) + "_" + item.itemCode).val(money);
                                        calItemMoney(money, $("input[data-biz-name='bMoney" + (index + 1) + "_" + item.itemCode + "']"), 0);
                                    }
                                });
                            }
                        }
                    });
                }
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
});

//计划科目上报金额计算
function calItemMoney(data, obj, preValue) {
    var $ = bt.jquery, form = bt.form();
    var bMoney = obj.data();
    var bMoneyOld = moneyFun.format(preValue);//输入前的值
    var bMoneyNew = moneyFun.format(moneyType.get("bMoney" + bMoney.key + "_" + bMoney.itemcode).val());//输入后的值
    //如果金额未发生改变，无需计算
    if (bMoneyOld == bMoneyNew) {
        return;
    }
    calSuperItemMoney(bMoney.itemcode, bMoney.superitemcode, bMoney.key);
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
        }
    }
};

//上级科目汇总计算
function calSuperItemMoney(itemCode, superItemcode, key) {
    var $ = bt.jquery, form = bt.form();
    var bMoneyTotal = 0;

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
    if (superItemcodeNew && superItemcodeNew != "") {
        calSuperItemMoney(superItemcode, superItemcodeNew, keyNew);
    }
};

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
    $('form').find('input[data-name="bMoney1"]').each(function () {
        var itemcode = $(this).data("itemcode");
        var superitemcode = $(this).data("superitemcode");
        var key = $(this).data("key");
        var canBMoneyLength = $("input[data-biz-name='canBMoney_" + itemcode + "']").length;
        if (canBMoneyLength) {
            var canBMoney = moneyFun.toNumber(moneyType.get("canBMoney_" + itemcode).val());
            var bMoneyInput = moneyFun.toNumber($(this).val());
            var sign = $("#sign_" + itemcode).val();//资金流向：1流入 -1流出
            if (Math.abs(canBMoney) < Math.abs(bMoneyInput) && sign == -1) {
                bt.loading.hide();
                layer.alert(bt.lang('budget.message.bmoney.gt.canbmoney'), {icon: 7});
                moneyType.get("bMoney" + key + "_" + itemcode).val(0);
                calSuperItemMoney(itemcode, superitemcode, key);
                result = false;
            }
        }
    });
    return result;
}

function clearAll() {
    bt.jquery(".clearDiv").text("");
    //格式化金额
    var bMoneyDiv = $('div[name="bMoneyDiv"]');//上报金额
    for (var i = 0; i < bMoneyDiv.length; i++) {
        $(bMoneyDiv[i]).text(moneyFun.format($(bMoneyDiv[i]).parent().find("input[name^=bMoney]").val()));
    }
}
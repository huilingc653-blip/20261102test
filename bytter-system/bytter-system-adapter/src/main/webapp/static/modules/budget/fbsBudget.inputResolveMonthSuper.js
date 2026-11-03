var moneyFun;
var moneyType;
bt.use(['form', 'layedit', 'btdate', 'moneyType', 'toLigerTree', 'btUpload', 'btImport'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.toLigerTree.init({
        elem: '#appTable',
        newElem: '#maingrid1',
        rownumbers: true,//序号列数，默为无， 1代表左边第一列为序号列
        isDoubleHeader: true,//是否有双表头
        allowHideColumn: false,
        resize:false,
        height: "80%",
        fixedLeftColumns: 3,
    });
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    var weekNum = $("#weekNum").val();
    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        $("#status").val($(this).attr("value"));
        $("#attachmentId").val(up.getGroupId());

        //校验上报金额必须填写
        /*var bMoneyTotal = 0;
        $('form').find('input[data-name="btempMoney"]').each(function () {
            bMoneyTotal = moneyFun.add(bMoneyTotal, Math.abs(moneyFun.toNumber($(this).val())));
        });
        if (bMoneyTotal == 0) {
            bt.loading.hide();
            layer.alert(bt.lang('budget.message.bmoney.gt.zero'), {icon: 7});
            return false;
        }*/

        //校验期初子科目与大科目金额是否相等
        for (var key = 1; key <= weekNum; key++) {
            var beginPeriodMoney = 0;
            var beginPeriodTotal = 0;
            var superItemcode = "";
            var itemCode = "";
            $('form').find('input[data-name="btempMoney' + key + '"][data-isBeginPeriod="1"][data-key="' + key + '"]').each(function () {
                superItemcode = $(this).attr("data-superItemCode");
                if (superItemcode == "") {
                    itemCode = $(this).attr("data-itemCode");
                    beginPeriodMoney = moneyFun.toNumber($(this).val());
                } else {
                    beginPeriodTotal = moneyFun.add(beginPeriodTotal, moneyFun.toNumber($(this).val()));
                }
            });
            //存在下级科目才校验
            var itemLength = $('input[data-name="btempMoney' + key + '"][data-isBeginPeriod="1"][data-superItemCode="' + itemCode + '"]').length;
            if (itemLength && itemLength > 0) {
                if (moneyFun.sub(beginPeriodMoney, beginPeriodTotal) != 0) {
                    bt.loading.hide();
                    layer.alert(bt.lang('budget.message.resolve.month.mismatch', [key, superItemcode]), {icon: 7});
                    return false;
                }
            }
        }

        //校验期末子科目与大科目金额是否相等
        for (var key = 1; key <= weekNum; key++) {
            var endPeriodMoney = 0;
            var endPeriodTotal = 0;
            var superItemcode = "";
            var itemCode = "";
            $('form').find('input[data-name="btempMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"]').each(function () {
                superItemcode = $(this).attr("data-superItemCode");
                if (superItemcode == "") {
                    itemCode = $(this).attr("data-itemCode");
                    endPeriodMoney = moneyFun.toNumber($(this).val());
                } else {
                    endPeriodTotal = moneyFun.add(endPeriodTotal, moneyFun.toNumber($(this).val()));
                }
            });
            //存在下级科目才校验
            var itemLength = $('input[data-name="btempMoney' + key + '"][data-isEndPeriod="1"][data-superItemCode="' + itemCode + '"]').length;
            if (itemLength && itemLength > 0) {
                if (moneyFun.sub(endPeriodMoney, endPeriodTotal) != 0) {
                    bt.loading.hide();
                    layer.alert(bt.lang('budget.message.resolve.month.mismatch', [key, superItemcode]), {icon: 7});
                    return false;
                }
            }
        }
        /*
              //校验上周的期末与下周的期初是否相等
              for (var key = 1; key < weekNum; key++) {
                  //获取本周大科目期末金额
                  var curEndPeriodMoney = 0;
                  $('form').find('input[data-name="bMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"][data-superItemCode=""]').each(function () {
                      curEndPeriodMoney = moneyFun.add(curEndPeriodMoney, moneyFun.toNumber($(this).val()));
                  });
                  //获取下周大科目期初金额
                  var keyNew = key + 1;
                  var nextBeginPeriodMoney = 0;
                  $('form').find('input[data-name="bMoney' + keyNew + '"][data-isBeginPeriod="1"][data-key="' + keyNew + '"][data-superItemCode=""]').each(function () {
                      nextBeginPeriodMoney = moneyFun.add(nextBeginPeriodMoney, moneyFun.toNumber($(this).val()));
                  });
                  if (moneyFun.sub(curEndPeriodMoney, nextBeginPeriodMoney) != 0) {
                      bt.loading.hide();
                      layer.alert(bt.lang('budget.message.resolve.month.periodmoney.mismatch', [key, keyNew]), {icon: 7});
                      return false;
                  }
              }*/

        var action = "/budget/fbsbudget/inputResolveMonthSave";
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
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        }, 'JSON');
        return false;
    });

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
            downUrl: basePath + "/budget/fbsBudgetDownloadAndImport/downloadBudgetResolve?id=" + $("#id").val() + "&target=super&timeType=" + $("#timeType").val() + "&beginDate=" + $("#beginDate").val(),
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                var flag = false;
                $.each(data, function (i, item) {
                    if (!item.item) {
                        // 模板存在系统不存在的计划科目
                        flag = true;
                    }
                    // 模板存在单位未分配的计划科目
                    try {
                        if (!moneyType.get("btempMoney1_" + item.itemCode) || !moneyType.get("btempMoney2_" + item.itemCode)
                            || !moneyType.get("btempMoney3_" + item.itemCode)|| !moneyType.get("btempMoney4_" + item.itemCode)) {
                            flag = true;
                        }
                        if ($("input[data-biz-name='btempMoney5_" + item.itemCode + "']").length) {
                            if (!moneyType.get("btempMoney5_" + item.itemCode)) {
                                flag = true;
                            }
                        }
                        if ($("input[data-biz-name='btempMoney6_" + item.itemCode + "']").length) {
                            if (!moneyType.get("btempMoney6_" + item.itemCode)) {
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
                            moneyType.get("btempMoney1_" + item.itemCode).val(item.bWeekMoney1);
                            calItemMoney(item.bWeekMoney1, $("input[data-biz-name='btempMoney1_" + item.itemCode + "']"), 0);
                            moneyType.get("btempMoney2_" + item.itemCode).val(item.bWeekMoney2);
                            calItemMoney(item.bWeekMoney2, $("input[data-biz-name='btempMoney2_" + item.itemCode + "']"), 0);
                            moneyType.get("btempMoney3_" + item.itemCode).val(item.bWeekMoney3);
                            calItemMoney(item.bWeekMoney3, $("input[data-biz-name='btempMoney3_" + item.itemCode + "']"), 0);
                            moneyType.get("btempMoney4_" + item.itemCode).val(item.bWeekMoney4);
                            calItemMoney(item.bWeekMoney4, $("input[data-biz-name='btempMoney4_" + item.itemCode + "']"), 0);
                            if ($("input[data-biz-name='btempMoney5_" + item.itemCode + "']").length) {
                                moneyType.get("btempMoney5_" + item.itemCode).val(item.bWeekMoney5);
                                calItemMoney(item.bWeekMoney5, $("input[data-biz-name='btempMoney5_" + item.itemCode + "']"), 0);
                            }

                            if ($("input[data-biz-name='btempMoney6_" + item.itemCode + "']").length) {
                                moneyType.get("btempMoney6_" + item.itemCode).val(item.bWeekMoney6);
                                calItemMoney(item.bWeekMoney6, $("input[data-biz-name='btempMoney6_" + item.itemCode + "']"), 0);
                            }
                        }
                    })
                }
            }
        });
    };
});

//计划科目上报金额计算
function calItemMoney(data, obj, preValue) {
    if(!preValue){
        preValue = 0;
    }
    var $ = bt.jquery, form = bt.form();
    var bMoney = obj.data();
    var bMoneyOld = moneyFun.format(preValue);//输入前的值
    var bMoneyNew = moneyFun.format(moneyType.get("btempMoney" + bMoney.key + "_" + bMoney.itemcode).val());//输入后的值
    //如果金额未发生改变，无需计算
    if (bMoneyOld == bMoneyNew) {
        return;
    }
    calSuperItemMoney(bMoney.itemcode, bMoney.superitemcode, bMoney.key, bMoney.isbeginperiod, bMoney.isendperiod);
    //期初期末科目计算
    calPeriodItemMoney(bMoney.key);
    calMonthSuperItemMoney();
    //校验金额是否超过可上报金额
    var canBMoneyLength = $("input[data-biz-name='canBMoney_" + bMoney.itemcode + "']").length;
    if (canBMoneyLength) {
        var canBMoney = moneyFun.toNumber(moneyType.get("canBMoney_" + bMoney.itemcode).val());
        var bMoneyInput = moneyFun.toNumber(moneyType.get("btempMoney_" + bMoney.itemcode).val());
        var sign = $("#sign_" + bMoney.itemcode).val();//资金流向：1流入 -1流出
        if (canBMoney < bMoneyInput && sign == -1) {
            layer.alert(bt.lang('budget.message.bmoney.gt.canbmoney'), {icon: 7});
            moneyType.get("btempMoney" + bMoney.key + "_" + bMoney.itemcode).val(0);
            calSuperItemMoney(bMoney.itemcode, bMoney.superitemcode, bMoney.key);
            //期初期末科目计算
            calPeriodItemMoney(bMoney.key);
            calMonthSuperItemMoney();
        }
    }
};

//上级科目汇总计算
function calSuperItemMoney(itemCode, superItemcode, key, isBeginPeriod, isEndPeriod) {
    var $ = bt.jquery, form = bt.form();
    var bMoneyTotal = 0;
    //期末科目不做上级科目汇总
    if (isEndPeriod != "1") {
        //判断是否有上级科目
        if (superItemcode && superItemcode != "") {
            $('form').find('input[data-superItemCode="' + superItemcode + '"][data-name="btempMoney' + key + '"]').each(function () {
                bMoneyTotal = moneyFun.add(bMoneyTotal, moneyFun.toNumber($(this).val()));
            });
            //上级科目金额
            var bMoneySuper = moneyFun.format(bMoneyTotal);
            moneyType.get("btempMoney" + key + "_" + superItemcode).val(bMoneySuper);
            $("#btempMoneyDiv" + key + "_" + superItemcode).text(bMoneySuper);
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
    var superItemcodeNew = $("[data-id='btempMoney" + key + "_" + superItemcode + "']").attr("data-superItemCode");
    var keyNew = $("[data-id='btempMoney" + key + "_" + superItemcode + "']").attr("data-key");
    var isBeginPeriodNew = $("[data-id='btempMoney" + key + "_" + superItemcode + "']").attr("data-isBeginPeriod");
    var isEndPeriodNew = $("[data-id='btempMoney" + key + "_" + superItemcode + "']").attr("data-isEndPeriod");
    if (superItemcodeNew && superItemcodeNew != "") {
        calSuperItemMoney(superItemcode, superItemcodeNew, keyNew, isBeginPeriodNew, isEndPeriodNew);
    }
};

//期初期末科目计算
function calPeriodItemMoney(key) {
    var weekNum = $("#weekNum").val();
    if (key < weekNum) {
        //获取期末大科目金额
        var endPeriodMoney = 0;
        //获取当前月大科目期末
        $('form').find('input[data-name="btempMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"][data-superItemCode=""]').each(function () {
            endPeriodMoney = moneyFun.add(endPeriodMoney, moneyFun.toNumber($(this).val()));
        });
        //设置下月大科目期初
        var keyNew = key + 1;
        $('form').find('input[data-name="btempMoney' + keyNew + '"][data-isBeginPeriod="1"][data-key="' + keyNew + '"][data-superItemCode=""]').each(function () {
            var itemCode = $(this).attr("data-itemCode");
            var superItemcode = $(this).attr("data-superItemCode");
            var isBeginPeriod = $(this).attr("data-isBeginPeriod");
            var isEndPeriod = $(this).attr("data-isEndPeriod");
            moneyType.get("btempMoney" + keyNew + "_" + itemCode).val(endPeriodMoney);
            if ($("#btempMoneyDiv" + keyNew + "_" + itemCode)) {
                $("#btempMoneyDiv" + keyNew + "_" + itemCode).text(moneyFun.format(endPeriodMoney));
            }
            calSuperItemMoney(itemCode, superItemcode, keyNew, isBeginPeriod, isEndPeriod);
        });
        calPeriodItemMoney(keyNew);
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
                var bMoneyLength = $("input[data-biz-name='btempMoney" + key + "_" + itemCode + "']").length;
                if (!bMoneyLength) {
                    bMoneySuper = 0;
                } else {
                    bMoneySuper = moneyFun.mul(moneyFun.toNumber(moneyType.get("btempMoney" + key + "_" + itemCode).val()), $("#sign_" + itemCode).val());
                }
            } else {
                bMoneySuper = moneyFun.toNumber(itemCode);
            }
        }
        if (itemCodeNext.indexOf("k") != -1) {
            itemCodeNext = itemCodeNext.replace("k", "");
            //科目不存在，计算时按0计算
            var bMoneyLength = $("input[data-biz-name='btempMoney" + key + "_" + itemCodeNext + "']").length;
            if (!bMoneyLength) {
                bMoneySuperNext = 0;
            } else {
                bMoneySuperNext = moneyFun.mul(moneyFun.toNumber(moneyType.get("btempMoney" + key + "_" + itemCodeNext).val()), $("#sign_" + itemCodeNext).val());
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
    moneyType.get("btempMoney" + key + "_" + curItemCode).val(bMoneyTotal);
    $("#btempMoneyDiv" + key + "_" + curItemCode).text(moneyFun.format(bMoneyTotal));
}

//计算月计划上报金额
function calMonthSuperItemMoney() {
    var $ = bt.jquery, form = bt.form();
    var weekNum = $("#weekNum").val();
    var itemCode = "";
    var superItemcode = "";
    //月的期初 = 第1周期初
    $('form').find('input[data-name="btempMoney"][data-isBeginPeriod="1"][data-key=""]').each(function () {
        itemCode = $(this).attr("data-itemCode");
        superItemcode = $(this).attr("data-superItemCode");
        var beginPeriodMoney = moneyFun.format(moneyType.get("btempMoney1_" + itemCode).val());
        moneyType.get("btempMoney_" + itemCode).val(beginPeriodMoney);
        $("#btempMoneyDiv_" + itemCode).text(beginPeriodMoney);
    });
    //月的期末 = 12月期末
    $('form').find('input[data-name="btempMoney"][data-isEndPeriod="1"][data-key=""]').each(function () {
        var curItemCode = $(this).attr("data-itemCode");
        var endPeriodMoney = moneyFun.format(moneyType.get("btempMoney" + weekNum + "_" + curItemCode).val());
        moneyType.get("btempMoney_" + curItemCode).val(endPeriodMoney);
        $("#btempMoneyDiv_" + curItemCode).text(endPeriodMoney);
    });
    // 年计划新追加金额计算
    $('form').find('input[name="itemCode"]').each(function () {
        var isBeginPeriod = $("#isBeginPeriod_" + $(this).val()).val();
        var isEndPeriod = $("#isEndPeriod_" + $(this).val()).val();
        if (isBeginPeriod != "1" && isEndPeriod != "1") {
            var bMoneyTotal = 0;
            for (var key = $("#minWeek").val(); key <= weekNum; key++) {
                var bMoney = moneyType.get("btempMoney" + key + "_" + $(this).val()).val();
                bMoneyTotal = moneyFun.add(bMoneyTotal, moneyFun.toNumber(bMoney));
            }
            moneyType.get("btempMoney_" + $(this).val()).val(bMoneyTotal);
            var bMoneySuper = moneyFun.format(bMoneyTotal);
            $("#btempMoneyDiv_" + $(this).val()).text(bMoneySuper);
        }
    });
    calSuperItemMoney(itemCode, superItemcode, "");
}

function clearAll() {
    bt.jquery(".clearDiv").text("");
    //格式化金额
    var btempMoneyDiv = $('div[name="btempMoneyDiv"]');//上报金额
    for (var i = 0; i < btempMoneyDiv.length; i++) {
        $(btempMoneyDiv[i]).text(moneyFun.format($(btempMoneyDiv[i]).parent().find("input[name^=btempMoney]").val()));
    }
}
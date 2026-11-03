var moneyFun;
var moneyType;
bt.use(['form', 'layedit', 'btdate', 'moneyType', 'btUpload', 'btImport','toLigerTree', 'selectType','date'], function () {
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

        data.field['createDate'] = data.field['createDate']?bt.date.format(data.field['createDate'],'yyyy-MM-dd hh:mm:ss'):data.field['createDate']

        //校验上报金额必须填写
        /*var bMoneyTotal = 0;
        $('form').find('input[data-name="bMoney"]').each(function () {
            bMoneyTotal = moneyFun.add(bMoneyTotal, Math.abs(moneyFun.toNumber($(this).val())));
        });
        if (bMoneyTotal == 0) {
            bt.loading.hide();
            layer.alert(bt.lang('budget.message.bmoney.gt.zero'), {icon: 7});
            return false;
        }*/

        //校验期初子科目与大科目金额是否相等
        for (var key = 1; key <= 12; key++) {
            var beginPeriodMoney = 0;
            var beginPeriodTotal = 0;
            var superItemcode = "";
            var itemCode = "";
            var length = 0;
            $('form').find('input[data-name="bMoney' + key + '"][data-isBeginPeriod="1"][data-key="' + key + '"]').each(function () {
                superItemcode = $(this).attr("data-superItemCode");
                if(superItemcode.length > length){
                    length = superItemcode.length;
                }
                if (superItemcode == "") {
                    itemCode = $(this).attr("data-itemCode");
                    beginPeriodMoney = moneyFun.toNumber($(this).val());
                } else {
                    beginPeriodTotal = moneyFun.add(beginPeriodTotal, moneyFun.toNumber($(this).val()));
                }
            });
            //存在下级科目才校验
            var itemLength = $('input[data-name="bMoney' + key + '"][data-isBeginPeriod="1"][data-superItemCode="' + itemCode + '"]').length;
            if (itemLength && itemLength > 0) {
                beginPeriodTotal = moneyFun.div(beginPeriodTotal,length/3);
                if (moneyFun.sub(beginPeriodMoney, beginPeriodTotal) != 0) {
                    bt.loading.hide();
                    layer.alert(bt.lang('budget.message.resolve.year.mismatch', [key, superItemcode]), {icon: 7});
                    return false;
                }
            }
        }

        //校验期末子科目与大科目金额是否相等
        for (var key = 1; key <= 12; key++) {
            var endPeriodMoney = 0;
            var endPeriodTotal = 0;
            var superItemcode = "";
            var itemCode = "";
            $('form').find('input[data-name="bMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"]').each(function () {
                superItemcode = $(this).attr("data-superItemCode");
                if (superItemcode == "") {
                    itemCode = $(this).attr("data-itemCode");
                    endPeriodMoney = moneyFun.toNumber($(this).val());
                } else {
                    endPeriodTotal = moneyFun.add(endPeriodTotal, moneyFun.toNumber($(this).val()));
                }
            });
            //存在下级科目才校验
            var itemLength = $('input[data-name="bMoney' + key + '"][data-isEndPeriod="1"][data-superItemCode="' + itemCode + '"]').length;
            if (itemLength && itemLength > 0) {
                if (moneyFun.sub(endPeriodMoney, endPeriodTotal) != 0) {
                    bt.loading.hide();
                    layer.alert(bt.lang('budget.message.resolve.year.mismatch', [key, superItemcode]), {icon: 7});
                    return false;
                }
            }
        }

        //校验上个月的期末与下个月的期初是否相等
        for (var key = 1; key < 12; key++) {
            //获取本月大科目期末金额
            var curEndPeriodMoney = 0;
            $('form').find('input[data-name="bMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"][data-superItemCode=""]').each(function () {
                curEndPeriodMoney = moneyFun.add(curEndPeriodMoney, moneyFun.toNumber($(this).val()));
            });
            //获取下个月大科目期初金额
            var keyNew = key + 1;
            var nextBeginPeriodMoney = 0;
            $('form').find('input[data-name="bMoney' + keyNew + '"][data-isBeginPeriod="1"][data-key="' + keyNew + '"][data-superItemCode=""]').each(function () {
                nextBeginPeriodMoney = moneyFun.add(nextBeginPeriodMoney, moneyFun.toNumber($(this).val()));
            });
            if (moneyFun.sub(curEndPeriodMoney, nextBeginPeriodMoney) != 0) {
                bt.loading.hide();
                layer.alert(bt.lang('budget.message.resolve.year.periodmoney.mismatch', [key, keyNew]), {icon: 7});
                return false;
            }
        }

        var action = "/budget/fbsbudget/inputResolveYearSave";
        if($("#viewType").val() == "approve"){
            var action = "/budget/fbsbudget/inputResolveYearSaveApprove";
        }
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    if($("#viewType").val() == "approve") {
                        bt.navtab.refreshCurrent();
                    }else {
                        var parentWindow = bt.navtab.getParentDom();
                        if (parentWindow.document.getElementById("budgetBody")) {
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
        var action = "/budget/fbsbudget/inputResolveYearSave";
        $.post(basePath + action, $('#inputForm').serialize(), function (result) {
            if (result.success) {
                if (bt.navtab) {
                    var parentWindow = bt.navtab.getParentDom();
                    if(parentWindow.document.getElementById("budgetBody")){
                        parentWindow.document.getElementById("budgetBody").className ="layui-hide";
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
            downUrl: basePath + "/budget/fbsBudgetDownloadAndImport/downloadBudgetResolve?id=" + $("#id").val() + "&timeType=" + $("#timeType").val(),
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                var flag = false;
                $.each(data, function (i, item) {
                    if (!item.item) {
                        // 模板存在系统不存在的计划科目
                        flag = true;
                    }
                    // 模板存在单位未分配的计划科目
                    try {
                        if (!moneyType.get("bMoney1_" + item.itemCode) || !moneyType.get("bMoney2_" + item.itemCode)
                            || !moneyType.get("bMoney3_" + item.itemCode)|| !moneyType.get("bMoney4_" + item.itemCode)
                            || !moneyType.get("bMoney5_" + item.itemCode)|| !moneyType.get("bMoney6_" + item.itemCode)
                            || !moneyType.get("bMoney7_" + item.itemCode)|| !moneyType.get("bMoney8_" + item.itemCode)
                            || !moneyType.get("bMoney9_" + item.itemCode)|| !moneyType.get("bMoney10_" + item.itemCode)
                            || !moneyType.get("bMoney11_" + item.itemCode)|| !moneyType.get("bMoney12_" + item.itemCode)) {
                            flag = true;
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
                            moneyType.get("bMoney1_" + item.itemCode).val(item.bMoney1);
                            calItemMoney(item.bMoney1, $("input[data-biz-name='bMoney1_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney2_" + item.itemCode).val(item.bMoney2);
                            calItemMoney(item.bMoney2, $("input[data-biz-name='bMoney2_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney3_" + item.itemCode).val(item.bMoney3);
                            calItemMoney(item.bMoney3, $("input[data-biz-name='bMoney3_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney4_" + item.itemCode).val(item.bMoney4);
                            calItemMoney(item.bMoney4, $("input[data-biz-name='bMoney4_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney5_" + item.itemCode).val(item.bMoney5);
                            calItemMoney(item.bMoney5, $("input[data-biz-name='bMoney5_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney6_" + item.itemCode).val(item.bMoney6);
                            calItemMoney(item.bMoney6, $("input[data-biz-name='bMoney6_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney7_" + item.itemCode).val(item.bMoney7);
                            calItemMoney(item.bMoney7, $("input[data-biz-name='bMoney7_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney8_" + item.itemCode).val(item.bMoney8);
                            calItemMoney(item.bMoney8, $("input[data-biz-name='bMoney8_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney9_" + item.itemCode).val(item.bMoney9);
                            calItemMoney(item.bMoney9, $("input[data-biz-name='bMoney9_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney10_" + item.itemCode).val(item.bMoney10);
                            calItemMoney(item.bMoney10, $("input[data-biz-name='bMoney10_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney11_" + item.itemCode).val(item.bMoney11);
                            calItemMoney(item.bMoney11, $("input[data-biz-name='bMoney11_" + item.itemCode + "']"), 0);
                            moneyType.get("bMoney12_" + item.itemCode).val(item.bMoney12);
                            calItemMoney(item.bMoney12, $("input[data-biz-name='bMoney12_" + item.itemCode + "']"), 0);
                        }
                    })
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

    /**
     * 查看明细
     * @param id
     * @param detailId
     */
    function view (id, detailId) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/budget/fbsbudgetdetail/view?budgetId=" + id + "&detailId=" + detailId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/budget/fbsbudgetdetail/view?budgetId=" + id + "&detailId=" + detailId
        });
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
    calSuperItemMoney(bMoney.itemcode, bMoney.superitemcode, bMoney.key, bMoney.isbeginperiod, bMoney.isendperiod);
    // //期初期末科目计算
    calPeriodItemMoney(bMoney.key);
    calYearSuperItemMoney();
};

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

//期初期末科目计算
function calPeriodItemMoney(key) {
    if (key < 12) {
        //获取期末大科目金额
        var endPeriodMoney = 0;
        //获取当前月大科目期末
        $('form').find('input[data-name="bMoney' + key + '"][data-isEndPeriod="1"][data-key="' + key + '"][data-superItemCode=""]').each(function () {
            endPeriodMoney = moneyFun.add(endPeriodMoney, moneyFun.toNumber($(this).val()));
        });
        //设置下月大科目期初
        var keyNew = key + 1;
        $('form').find('input[data-name="bMoney' + keyNew + '"][data-isBeginPeriod="1"][data-key="' + keyNew + '"][data-superItemCode=""]').each(function () {
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
                var bMoneyLength = $("input[data-biz-name='bMoney" + key + "_" + itemCode + "']").length;
                if (!bMoneyLength) {
                    bMoneySuper = 0;
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

//计算年计划上报金额
function calYearSuperItemMoney() {
    var $ = bt.jquery, form = bt.form();
    var itemCode = "";
    var superItemcode = "";
    //年的期初 = 1月期初
    $('form').find('input[data-name="bMoney"][data-isBeginPeriod="1"][data-key=""]').each(function () {
        itemCode = $(this).attr("data-itemCode");
        superItemcode = $(this).attr("data-superItemCode");
        var beginPeriodMoney = moneyFun.format(moneyType.get("bMoney1_" + itemCode).val());
        moneyType.get("bMoney_" + itemCode).val(moneyFun.toNumber(beginPeriodMoney));
        $("#bMoneyDiv_" + itemCode).text(beginPeriodMoney);
    });
    //年的期末 = 12月期末
    $('form').find('input[data-name="bMoney"][data-isEndPeriod="1"][data-key=""]').each(function () {
        var curItemCode = $(this).attr("data-itemCode");
        var endPeriodMoney = moneyFun.format(moneyType.get("bMoney12_" + curItemCode).val());
        moneyType.get("bMoney_" + curItemCode).val(moneyFun.toNumber(endPeriodMoney));
        $("#bMoneyDiv_" + curItemCode).text(endPeriodMoney);
    });
    //年计划计算
    $('form').find('input[name="itemCode"]').each(function () {
        var isBeginPeriod = $("#isBeginPeriod_" + $(this).val()).val();
        var isEndPeriod = $("#isEndPeriod_" + $(this).val()).val();
        if (isBeginPeriod != "1" && isEndPeriod != "1") {
            var bMoneyTotal = 0;
            for (var key = 1; key <= 12; key++) {
                var bMoney = moneyType.get("bMoney" + key + "_" + $(this).val()).val();
                bMoneyTotal = moneyFun.add(bMoneyTotal, moneyFun.toNumber(moneyType.get("bMoney" + key + "_" + $(this).val()).val()));
            }
            var bMoneySuper = moneyFun.format(bMoneyTotal);
            moneyType.get("bMoney_" + $(this).val()).val(bMoneySuper);
            $("#bMoneyDiv_" + $(this).val()).text(bMoneySuper);
        }
    });
    calSuperItemMoney(itemCode, superItemcode, "");
}

function clearAll() {
    bt.jquery(".clearDiv").text("");
    //格式化金额
    var bMoneyDiv = $('div[name="bMoneyDiv"]');//上报金额
    for (var i = 0; i < bMoneyDiv.length; i++) {
        $(bMoneyDiv[i]).text(moneyFun.format($(bMoneyDiv[i]).parent().find("input[name^=bMoney]").val()));
    }
}
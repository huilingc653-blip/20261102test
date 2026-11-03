var moneyFun;
var moneyType;
bt.use(['form', 'layedit', 'btdate', 'moneyType', 'treeTable', 'btUpload'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.treeTable.init({elem: "#appTable", column: 1});
    var up = bt.btUpload.init({
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

var moneyFun;
var moneyType;
bt.use(['form', 'layedit', 'btdate', 'moneyType', 'treeTable', 'btUpload'], function () {
    var $ = bt.jquery, form = bt.form();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    bt.treeTable.init({elem: "#appTable", column: 1});
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
        //校验上报/追加金额必须填写 修改
        // var bMoneyTotal = 0;
        // var bMoneyName = "bMoney";
        // if ($("#target").val() == "super") {
        //     bMoneyName = "btempMoney";
        // }
        // if ($('form').find('input[data-name="' + bMoneyName + '"]').length) {
        //     $('form').find('input[data-name="' + bMoneyName + '"]').each(function () {
        //         bMoneyTotal = moneyFun.add(bMoneyTotal, Math.abs(moneyFun.toNumber($(this).val())));
        //     });
        // } else {
        //     //月滚动
        //     $('form').find('input[data-name="bMoney1"]').each(function () {
        //         bMoneyTotal = moneyFun.add(bMoneyTotal, Math.abs(moneyFun.toNumber($(this).val())));
        //     });
        // }
        // if (bMoneyTotal == 0) {
        //     bt.loading.hide();
        //     layer.alert(bt.lang('budget.message.collectcorp.num.null'), {icon: 7});
        //     return false;
        // }
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
        var action = "/budget/fbsbudget/inputSave";
        if (saveMethod != "") {
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

/**
 * 计划科目上报金额计算
 * @param data
 * @param obj
 */
function calItemMoney(data, obj) {
    var $ = bt.jquery, form = bt.form();
    var bMoney = obj.data();
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

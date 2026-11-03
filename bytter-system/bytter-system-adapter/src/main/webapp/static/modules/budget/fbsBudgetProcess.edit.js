bt.use(['form', 'layedit', 'dateType', 'moneyType', 'dicts', 'externalCorpBankAcc', 'selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    var dt = bt.dateType.init();
    var mt = bt.moneyType.init();
    var dicts = bt.dicts.init();
    bt.externalCorpBankAcc.init();
    var select = bt.selectType.init();

    $("#close").on("click", function () {
        if (bt.navtab) {
            bt.navtab.refreshParent();
            bt.navtab.closeCurrent();
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var status = $(this).attr("value");
        data.field.status = status;
        var innerSign = dicts.get("innerSign").val();
        if (innerSign == 1) {
            data.field.payeeAccName = select.get("payeeAccName").getData().text;
        } else {
            data.field.payeeAccName = $("#payeeAccName").val();
        }
        var action = "/budget/fbsBudgetProcess/modify";
        $.post(basePath + action, data.field, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (!bt.navtab && result.success) {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            } else {
                bt.navtab.refreshParent();
                bt.navtab.closeCurrent();
            }
        }, 'JSON');
        return false;
    });
    //客商选择
    bt.payeeAccCallback = function (data, dom) {
        $("#payeeAccName").val(data.name);
    };

    $changSign = function (sign) {
        if (sign == 1) {
            $("#corp1").attr("style", "display:none");
            $("#corp2").attr("style", "display:block");
        } else {
            $("#corp1").attr("style", "display:block");
            $("#corp2").attr("style", "display:none");
        }
        form.render();
        form.renderIcon();
    }

    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var url = basePath + "/budget/fbsBudgetProcess/getEditItemDetail";
        var param = {
            id: $("#id").val(),
            corpId: $("#corpId").val(),
            itemCode: "",
            billNo: $("#billNo").val(),
            curId: $("#curId").val(),
            defItemCode: $("#defItemCode").val(),
            processDate: $("#processDate").val(),
            type: 'select',
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    };
    $genOtherHtml();
});

/**
 * 预算执行输入金额汇总后的回调
 * @param data  汇总金额
 */
function sumMoneyCallBack(data) {
    bt.moneyType.get("processMoney").val(data);
}

//是否内转回调
bt.innerSign = function (data) {
    $changSign(data.value);
}

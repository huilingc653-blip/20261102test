bt.use(['form', 'layedit', 'btdate', 'moneyType', 'dateType', 'bank'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.bank.init({elem: ".bank"});
    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            placeholder: $(item).attr("defaultVal"),
            defaultVal: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            placeholder: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    form.on('radio(cdSign)', function (data) {
        bankDownBal();
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        //交易金额必须大于0
        if(Number(data.field.amt)==Number(0)){
            bt.loading.hide();
            layer.msg(bt.lang('nis.netbank.message.amt.mismatch'));
            return false;
        }

        var action = "/bankacc/btbankaccdtl/modify";
        data.field.status = $(this).attr("value");
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        if (bt.navtab.getCurrentDom().$("#addBtBankAccDtl").length > 0) {
                            parent.layer.closeAll();
                        } else {
                            bt.navtab.closeCurrent();
                        }
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                        bt.navtab.refreshCurrent();
                    }
                }
            });
        }, 'JSON');
        return false;
    });


    /**
     * 查询最近一天的银行行账号余额
     * @param transTime
     */
    function bankDownBal(amtVal) {
        var amt = amtVal ? amtVal : moneyType.get("amt").val();
        var data = {
            bankAcc: $("#bankAcc").val(),
            curId: $("#curId").val(),
            transTime: bt.dateType.get("transTime").val(),
            cdSign: $('input:radio[name="cdSign"]:checked').val(),
            rbSign: $("#rbSign").val(),
            amt: amt,
            orderNo: $("#orderNo").val()
        };
        $.ajax({
            url: basePath + "/bankacc/btbankaccdtl?returnDownBankBal",
            method: 'POST',
            data: data,
            dataType: 'json',
            success: function (result) {
                $("#soucebankBal").val(result - amt);
                bt.moneyType.get("bal").val(result);
            }
        });
    }

    /**
     * 金额回调
     * @param data
     */
    amtCallback = function (data) {
        bankDownBal(data);
    }

    var moneyType = bt.moneyType.init();
});




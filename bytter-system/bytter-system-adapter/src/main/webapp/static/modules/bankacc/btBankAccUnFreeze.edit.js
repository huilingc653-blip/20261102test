bt.use(['form', 'layedit', 'btdate', 'moneyType'], function () {
    var $ = bt.jquery, form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
//    moneyType.get("freezeMoney").val($("#availBal").val());
    //格式化金额
    var bMoneyDiv = $('div[name="money"]');//上报金额
    for (var i = 0; i < bMoneyDiv.length; i++) {
        $(bMoneyDiv[i]).text(moneyFun.format($(bMoneyDiv[i]).text()));
    }
    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    //监听暂存
    form.on('submit(temp)', function (data) {
        bt.loading.show();
        var action = "/bankacc/btbankaccfreeze/modify";
        var freezeMoney = $("#freezeMoney").val();
        var freezeMoneyNum = bt.money.toNumber(freezeMoney);
        var ufavalBal = $("#ufavalBal").val();
        if (freezeMoneyNum <= 0 || freezeMoneyNum > ufavalBal) {
            bt.loading.hide();
            layer.alert(bt.lang("common.message.unfreezeMoney"));
            return false;
        }
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            });
        }, 'JSON');
        return false;
    });
});



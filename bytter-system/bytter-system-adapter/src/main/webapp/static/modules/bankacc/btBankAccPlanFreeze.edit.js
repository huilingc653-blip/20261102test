var $, form, dateType, moneyType, moneyFun;
bt.use(['form', 'layedit', 'btdate', 'moneyType'], function () {
    $ = bt.jquery;
    form = bt.form();
    dateType = bt.dateType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();

    //格式化金额
    var bMoneyDiv = $('div[name="money"]');//上报金额
    for (var i = 0; i < bMoneyDiv.length; i++) {
        $(bMoneyDiv[i]).text(moneyFun.format($(bMoneyDiv[i]).text()));
    }

    //监听暂存
    form.on('submit(temp)', function (data) {
        bt.loading.show();
        var action = "/bankacc/btbankaccplanfreeze/planFreezeModify";
        // 冻结金额不能小于0
        var applyFreezeMoney = bt.money.toNumber(moneyType.get("applyFreezeMoney").val());
        if (applyFreezeMoney <= 0) {
            bt.loading.hide();
            layer.alert(bt.lang("bt.bankaccfreeze.message.money.gtzero"), {icon: 2});
            return false;
        }
        // 冻结金额必须小于等于账户的可用余额！
        var availBal = $("#availBal").val();
        if (applyFreezeMoney > availBal) {
            bt.loading.hide();
            layer.alert(bt.lang("bt.bankaccfreeze.message.money.geavailBal"), {icon: 2});
            return false;
        }
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function (index) {
                parent.layer.close(index);
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshCurrent();
                        bt.navtab.refreshParent();
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            });
        }, 'JSON');
        return false;
    });
});

// 重置
function planFreezeReset() {
    moneyType.get("applyFreezeMoney").val($("#_applyFreezeMoney").val());//冻结金额
    dateType.get("freezeStartDate").val($("#_freezeStartDate").val());//冻结开始时间
    dateType.get("freezeEndDate").val($("#_freezeEndDate").val());//冻结结束时间
    $("#freezeRession").val($("#_freezeRession").val());//冻结原因
    form.render();
}


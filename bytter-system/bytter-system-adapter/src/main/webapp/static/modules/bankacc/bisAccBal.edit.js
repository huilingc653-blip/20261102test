bt.use(['form', 'layedit', 'dateType', 'moneyType', 'bank'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    var moneyType = bt.moneyType.init();
    bt.bank.init({
        elem: '.bank'
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var balVal = bt.money.toNumber(moneyType.get('bal').val());
        var availBalVal = bt.money.toNumber(moneyType.get('availBal').val());
        if (balVal < availBalVal) {
            bt.loading.hide();
            layer.alert(bt.lang("common.message.bal.ltavailBal"), {icon: 2});
            return false;
        }
        data.field.status = $(this).attr("value");
        var action = "/bankacc/bisaccbal/modify";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
});
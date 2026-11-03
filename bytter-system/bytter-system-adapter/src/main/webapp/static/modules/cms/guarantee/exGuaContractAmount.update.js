bt.use(['form', 'layedit', 'corp','selectType', 'dicts','moneyType','rateType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.rateType.init();
    bt.moneyType.init();

    //监听提交
    form.on('submit(submit)', function (data) {

        var action = "/cms/guarantee/guaExGuaranteeAmount/modify";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent();
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

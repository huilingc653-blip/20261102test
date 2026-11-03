bt.use(['form', 'layedit', 'dicts', 'selectType', 'selectTable', 'btUpload', 'moneyType','dateType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.moneyType.init();
    bt.moneyType.getFun();
    bt.selectType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/foreign/fexcashflow/mergeSave";
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

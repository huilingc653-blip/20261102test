bt.use(['form', 'datatable', 'dicts', 'corp','mulselect','dateType'], function () {
    $ = bt.jquery, form = bt.form();

    bt.dicts.init();
    bt.mulselect.init();
    bt.corp.init();
    bt.dateType.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/bankacc/biscorppooldtl/genBalSave";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshCurrentTable();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
});

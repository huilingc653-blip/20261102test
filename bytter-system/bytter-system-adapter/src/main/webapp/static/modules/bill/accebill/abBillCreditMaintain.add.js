var $, form, datatable;
bt.use(['form', 'datatable', 'corp', 'selectType', 'mulselect', 'btImport', 'dicts', 'rateType'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.mulselect.init();
    bt.dicts.init();
    bt.rateType.init();

    // 监听提交与暂存
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/bill/accebill/creditMaintain/save";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            if (result.success) {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
        return false;
    });

});
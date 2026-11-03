var $, form, datatable;
bt.use(['form', 'datatable', 'dicts', 'rateType'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.rateType.init();
    dicts = bt.dicts.init();//字典

    // 监听提交与暂存
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/wage/wageFloatRatio/modify";
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
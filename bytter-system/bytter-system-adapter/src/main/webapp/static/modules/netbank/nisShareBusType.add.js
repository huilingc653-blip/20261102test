var $, form, selectType;
bt.use(['form', 'selectType', 'dicts'], function () {
    $ = bt.jquery;
    form = bt.form();
    selectType = bt.selectType.init();
    dicts = bt.dicts.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/netbank/nissharebustype/save";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function (index) {
                if (result.success) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    parent.layer ? parent.layer.close(index) : layer.close(index);
                }
            })
        }, 'JSON');
        return false;
    });

    form.renderIcon();//渲染必填
});
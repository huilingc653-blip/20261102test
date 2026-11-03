bt.use(['form', 'select', 'dicts', 'bankInfo'], function () {
    var $ = bt.jquery
    var form = bt.form();
    bt.dicts.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/cms/rat/creditCooperationBank/modify";
        $.post(basePath + action, data.field, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                }
            }
        }, 'JSON');
        return false;
    });
});
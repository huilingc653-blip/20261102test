bt.use(['form', 'layedit', 'selectType'], function () {
    var $ = bt.jquery, form = bt.form();

    /**
     * 系统结算方式回调函数
     * @param data
     */

    bt.selectType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/receipt/bisbankreceiptdtldispose/update";
        $.post(basePath + action, data.field, function (result) {
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
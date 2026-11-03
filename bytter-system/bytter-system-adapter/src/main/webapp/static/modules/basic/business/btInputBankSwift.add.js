bt.use(['form', 'layedit','bankInfo'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.bankInfo.init();//联行号

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/basic/business/btinputbankswift/save";
        bt.loading.show();
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if(!bt.navtab){
                        parent.layer?parent.layer.closeAll():layer.closeAll();
                    } else {
                        bt.navtab.refreshParent();
                        bt.navtab.closeCurrent();
                    }
                }
            });
        }, 'JSON');
        return false;
    });

});

/**
 * 开户行行号选择
 * @param data
 * @param dom
 */
function bankinfoCallback(data, dom) {
    $("#bankName").val(data.bankName);
    $("#bankCode").val(data.bankCode);
}

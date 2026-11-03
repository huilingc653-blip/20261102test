bt.use(['form', 'select', 'dicts', 'bankInfo'], function () {
    var $ = bt.jquery
    var form = bt.form();
    bt.dicts.init();

    var bankInfo = bt.bankInfo.init();//联行号
    bt.bankInfoBeforeClick = function () {
        bankInfo.set({
            "joinBankType": true,
        });
        return true;
    }

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/cms/rat/creditCooperationBank/save";
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

//银行账号选择
function bankinfoCallback(data) {
    $("#showBankName").val(data.bankName);
    $("#bankName").val(data.bankName);//联行号名称
    $("#bankCode").val(data.bankCode);//联行号
}
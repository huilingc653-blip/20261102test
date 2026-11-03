var dicts;
var form;
bt.use(['form', 'layedit', 'selectTree','selectType','dateType'], function () {
    var $ = bt.jquery;
    bt.selectType.init();
    bt.dateType.init();
    form = bt.form();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/bill/accebill/bankInsufficientFundsTimeConfig/saveOrUpdate";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });
});

//银行账号选择
function bankinfoCallback(data, dom) {
    $("#bankTypeName").val(data.typename);
}
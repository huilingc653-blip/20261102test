bt.use(['form', 'layedit', 'selectType','dateType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/bill/accebill/bankInsufficientFundsTimeConfig/saveOrUpdate";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                    bt.navtab.closeCurrent();//关闭当前选项卡回到父级选项卡
                }
            })
        }, 'JSON');
        return false;
    });
});
var corpId = "";
var ratio,billPool;
bt.use(['form', 'layedit','moneyType','dicts'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.moneyType.init();
    form.verify({

    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var ids = $("#ids").val();//票据池交易明细ids
        data.field.id=ids;
        bt.loading.show();
        var action = "/billPool/discount/save";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            if (result.success) {
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            } else {
                parent.layer ? parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }
        }, 'JSON');
        return false;
    });
});




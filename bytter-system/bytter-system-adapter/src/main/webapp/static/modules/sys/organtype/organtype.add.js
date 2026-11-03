var corpId = "";
bt.use(['form', 'layedit', 'btdate', 'dicts'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    form.verify({
        //验证组织架构名称
        nameCheck: function (value) {
            var success = false;
            $.ajax({
                type: 'POST',
                url: basePath + "/sys/organtype/organtype/checkOrgtName",
                data: {'orgtName': value},
                async: false,
                dataType: 'json',
                success: function (result) {
                    if (!result.success) {
                        success = true;
                    }
                }
            });
            if (success) {
                return bt.lang('sys.organtype.organtypeName.repeat');
            }
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/sys/organtype/organtype/save";
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



bt.use(['form', 'layedit', 'btUpload'], function () {
    var $ = bt.jquery, form = bt.form();
    var up = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: $("#attachmentId").val()
    });
    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/bankacc/btbankaccdel/modify";
        data.field.flag = "1";
        data.field.attachmentId = up.getGroupId();
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            });
        }, 'JSON');
        return false;
    });
    //监听暂存
    form.on('submit(temp)', function (data) {
        bt.loading.show();
        var action = "/bankacc/btbankaccdel/modify";
        data.field.flag = "0";
        data.field.attachmentId = up.getGroupId();
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
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

bt.use(['form','layedit','dicts','selectType', 'selectTable', 'bttable'], function(){
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/sys/equityMaintenance/modify";
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

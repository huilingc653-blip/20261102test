bt.use(['form', 'corp', 'dateType', 'moneyType','selectType'], function () {
    $ = bt.jquery, form = bt.form();

    //单位回调函数
    bt.corpCallback = function (id) {
        if(id) {
            //获取单位信息
            $.ajax({
                url: basePath + "/sys/corp/getSysCorp",
                method: 'POST',
                data: {id: id},
                dataType: 'json',
                success: function (data) {
                    if (data.obj) {
                        bt.selectType.get('curId').val(data.obj.curId);
                    }
                    form.render();
                }
            });
        }
    }

    bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();
    bt.selectType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/bankacc/biscorppooloverdraft/save";
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

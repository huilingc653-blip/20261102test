bt.use(['form', 'layedit', 'corp', 'dicts','mulselect'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.mulselect.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var busType;
        // 复选框选中
        $.each($("input[type='checkbox']:checked"), function (i, item) {
            if ($(item).attr("value") != "all") {
                if (null == busType) {
                    busType = $(item).attr("value");
                } else {
                    busType += "," + $(item).attr("value");
                }
            }
        });
        $("#sysBusType").val(busType);
        data.field.sysBusType = busType;
        var action = "/basic/business/btpurpose/modify";
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

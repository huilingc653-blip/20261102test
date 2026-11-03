bt.use(['form', 'layedit', 'selectType', 'dicts', 'verify'], function () {
    var $ = bt.jquery, form = bt.form();
/**
     * 选择接口银行回调
     * @param data
     */
    bifCodeCallback = function (data) {
        $("#bifName").val(data.name);
        $("#bifCodeText").val(data.bifcode);
    }
    bt.dicts.init();

    bt.selectType.init();
    bt.verify.init({elem: ".inputVerify-form"});




    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/netbank/bisbifrule/save";
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


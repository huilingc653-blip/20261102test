bt.use(['form', 'selectType', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    /**
     * 选择收支属性回调
     * @param data
     */
    cdSignCallback = function (data) {
        var url = basePath + "/netbank/nisproject/listNisProjectByCdSign?cdSign=" + data.value;
        selectType.get("projectCode").attr("data-url", url);
    }
    var dicts = bt.dicts.init();
    var selectType = bt.selectType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/netbank/bisaccdtlclaim/batchClaim";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            })
        }, 'JSON');
        return false;
    });

});

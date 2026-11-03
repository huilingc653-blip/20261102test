bt.use(['form', 'layedit', 'selectType', 'corp', 'dateType','ratioType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dateType.init();
    bt.ratioType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/bill/accebill/billTargetProportion/saveOrUpdate";
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

//单位回调函数
function corpCallback(data){
    if(data&&data.length==0)return;
    $("#corpName").val(data.corpName);
    $("#corpCode").val(data.code);
}
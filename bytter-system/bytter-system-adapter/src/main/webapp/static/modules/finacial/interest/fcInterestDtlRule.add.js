var dicts;
var form;
bt.use(['form', 'layedit', 'dicts', 'dateType','selectType'], function () {
    var selectType=bt.selectType.init();
    var $ = bt.jquery;
    dicts = bt.dicts.init();
    bt.dateType.init();
    form = bt.form();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/finacial/interest/fcinterestdtlrule/save";
        $.post(basePath + action, $('#fcInterestDtlRuleForm').serialize(), function (result) {
            // 国际化异常信息
            var errorCode =result.messageEnum? result.messageEnum.toLowerCase().replaceAll("_","."):result.msg;
            parent.layer.alert(bt.lang(errorCode), {icon: result.success ? 1 : 2}, function () {
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

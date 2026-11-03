var $, form, moneyType, moneyFun, laydate;
bt.use(['form', 'select', 'dicts', 'corp', 'moneyType', 'dateType', 'laydate'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    laydate = layui.laydate;

    // 日期控件
    laydate.render({
        elem: '#years', //指定元素
        type: 'year' //只提供年列表选择
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/cms/rat/yearFinPlanRegister/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                }
            }
        }, 'JSON');
        return false;
    });
});

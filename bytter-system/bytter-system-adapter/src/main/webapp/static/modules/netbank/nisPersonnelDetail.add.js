bt.use(['form', 'layedit', 'corp', 'bank', 'bankInfo', 'dicts', 'region', 'selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    // 初始化下拉树
    bt.corp.init();
    // 初始化银行账户效果
    bt.bank.init({
        elem: ".bank"
    });
    bt.selectType.init();
    bt.bankInfo.init();
    bt.dicts.init();
    var region = bt.region.init();

    //开户行选择
    bt.bankinfoCallback = function (data, dom) {
        $("#bankCode").val(data.bankCode);
        $("#bankName").val(data.bankName);
        //设置省市组的值
        region.get('payeeAddr').set([{
            name: 'prov',
            defaultValue: data.prov
        }, {
            name: 'city',
            defaultValue: data.city
        }]);
    };

    //监听提交 
    form.on('submit(submit)', function (data) {
        var action = "/netbank/nispersonneldetail/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParentTable();
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

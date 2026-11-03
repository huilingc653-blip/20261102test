bt.use(['form', 'layedit','dicts', 'selectTree','rateType','selectType','moneyType','dateType','selectTableType'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    select = bt.selectType.init();
    moneyType = bt.moneyType.init();
    moneyFun=bt.moneyType.getFun();
    rateTypes = bt.rateType.init();//利率
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法
    //监听保存
    form.on('submit', function(data){
        bt.loading.show();

        var nm = $(this).attr("id");
        if(nm == "submit"){
            data.field.saveType = "1";
        }else {
            data.field.saveType = "0";
        }
        var action="/cms/equity/payThe/savePay";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    location.reload();
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshCurrent();
                }
            }else {
                bt.loading.hide();
            }

        }, 'JSON');
        return false;
    });

})




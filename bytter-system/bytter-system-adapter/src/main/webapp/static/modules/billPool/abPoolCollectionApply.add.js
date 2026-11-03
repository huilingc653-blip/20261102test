var form,$,billPool,billPoolCorp,datatable,moneyType;
bt.use(['form','billPool','billPoolCorp','dateType','mulselect','dicts','moneyType'], function () {
    $ = bt.jquery; form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();


    //监听新增保存
    form.on('submit(submit)', function(data){
        bt.loading.show();
        $.post(basePath + "/billpool/collection/submit",data.field,function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if (bt.navtab) {
                    parent.layer.closeAll();//重载当前页面
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });

});


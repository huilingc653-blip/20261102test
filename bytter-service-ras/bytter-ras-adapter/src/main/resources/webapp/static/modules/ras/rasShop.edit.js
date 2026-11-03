bt.use(['form','dateType','moneyType','dicts','corp'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.corp.init();
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var action = "/ras/shop/modify";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            if(result.success){
                parent.layer.alert(result.msg, {icon:1});
                if(bt.navtab) {
                    bt.navtab.closeCurrent();//关闭当前tab，刷新父tab页
                }else{
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }else{
                parent.layer.alert(result.msg, {icon: 2});
            }
        },'JSON');
        return false;
    });
});

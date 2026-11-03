bt.use(['form','layedit','dicts','moneyType','checkboxs', 'selectTable'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.moneyType.init();
    form.renderIcon();
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/sys/systemProvince/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.refreshParent();
                        bt.navtab.refreshCurrent()
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
});

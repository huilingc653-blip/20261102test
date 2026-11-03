bt.use(['form','layedit','dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    //监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();
        var action = "/dataexchange2/basic/erpdatatype/save";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab) {
                    bt.navtab.refreshCurrent();
                    bt.navtab.refreshParent();
                }else{
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });
});

bt.use(['form','layedit','region'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.region.init();
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/basic/business/btinputbankinfo/save";
        $.post(basePath + action,data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                parent.layer.closeAll();
                if(result.success){
                    if(bt.navtab) {
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshParent();
                    }else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        },'JSON');
        return false;
    });
});

bt.use(['form','layedit','dicts'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/dataexchange2/basic/erpdatatype/modify";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.closeCurrent();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });
});

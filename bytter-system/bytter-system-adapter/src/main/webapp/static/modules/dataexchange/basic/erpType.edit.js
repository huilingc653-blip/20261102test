bt.use(['form','layedit'], function(){
    var $ = bt.jquery,form = bt.form();
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/dataexchange2/basic/erptype/modify";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(!bt.navtab && result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            if (bt.navtab) {
                bt.navtab.refreshParent();
                bt.navtab.closeCurrent();
            }
        },'JSON');
        return false;
    });
});

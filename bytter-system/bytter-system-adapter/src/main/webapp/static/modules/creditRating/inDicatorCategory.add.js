bt.use(['form'], function() {
    var $ = bt.jquery, form = bt.form();

    //监听提交
    form.on('submit', function(data){
        var action = "/creditRating/indicatorCategory/save"
        $.post(basePath + action,data.field,function(result){
            parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success) {
                if(bt.navtab) {
                    bt.navtab.refreshParent();
                    bt.navtab.refreshCurrent()
                }else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });
});
var selectType,dict;
bt.use(['form','selectType','dicts'], function() {
    var $ = bt.jquery, form = bt.form();
    selectType = bt.selectType.init();
    dict = bt.dicts.init();
    //监听提交
    form.on('submit', function(data){
        var action = "/creditRating/crScoringIndicator/save"
        $.post(basePath + action,data.field,function(result){
            parent.layer.msg(result.msg, {icon: result.success?1:2});
            if(result.success) {
                if(bt.navtab) {
                    bt.navtab.getParentDom().$('[type="submit"]').trigger('click');
                    bt.navtab.closeCurrent();
                }else {
                    bt.navtab.getCurrentDom().$('[type="submit"]').trigger('click');
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');
        return false;
    });
});
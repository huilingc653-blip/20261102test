var form;
var selectTypes;
bt.use(['form','dicts','selectType'], function(){
    form = bt.form(),$  = bt.jquery;
    var dict = bt.dicts.init();
    selectTypes = bt.selectType.init();//下拉框
    //监听提交
    form.on('submit(configSubmit)', function(data){
            var msg = bt.lang('sys.organtype.budgetRelated');
            parent.layer.confirm(msg, function () {
            bt.loading.show();
            $.post(basePath + "/sys/organtype/orgtmodule/save",data.field,function(result){
                bt.navtab.refreshCurrent();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
                bt.loading.hide();
            },"JSON");
        });
        return false;
    });
});

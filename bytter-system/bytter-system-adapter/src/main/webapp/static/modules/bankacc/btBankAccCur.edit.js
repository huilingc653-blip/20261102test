bt.use(['form','layedit'], function(){
    var $ = bt.jquery,form = bt.form();
    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/bankacc/btbankacccur/modify";
        $.post(basePath + action,data.field,function(result){
            if(result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
            }
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });
});

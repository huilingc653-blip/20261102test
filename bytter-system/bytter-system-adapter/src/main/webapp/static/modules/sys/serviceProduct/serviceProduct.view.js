
bt.use(['form', 'jquery'], function(){
    var form = bt.form(),layer = bt.layer,$  = bt.jquery;


    //监听提交
    form.on('submit(serviceSubmit)', function(data){
        var action = $('input[name="id"]').val()?"update":"save";
        $.post(basePath + "/sys/serviceProduct/" + action,data.field,function(result){
            if(result.success){
                parent.layer.closeAll();
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });
});

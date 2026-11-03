
bt.use(['form', 'jquery'], function(){
    var form = bt.form(),layer = bt.layer,$  = bt.jquery;

     //监听提交
    form.on('submit(menuSubmit)', function(data){
        if(typeof(data.field.status) == "undefined"){
            data.field.status = 0;
        }else{
            data.field.status = 1;
        }
        var action = $('input[name="id"]').val()?"update":"save";
        $.post(basePath + "/sys/dict/" + action,data.field,function(result){
            if(result.success){
                parent.layer.closeAll("",true);
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });
});

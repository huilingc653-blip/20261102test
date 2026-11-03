
bt.use(['form', 'jquery'], function(){
    var form = bt.form(),layer = bt.layer,$  = bt.jquery;

    //自定义验证规则
    form.verify({
        langKey:function(value,item){
            if(/[\u4e00-\u9fa5]/.test(value)){
                return 'key不能使用中文';
            }
        }
    });

    //监听提交
    form.on('submit(multiLangSubmit)', function(data){
        var action = $('input[name="id"]').val()?"update":"save";
        $.post(basePath + "/sys/lang/" + action,data.field,function(result){
            if(result.success){
                parent.layer.closeAll("",true);
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });
});

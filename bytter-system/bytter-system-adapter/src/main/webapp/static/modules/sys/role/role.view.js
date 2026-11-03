
bt.use(['form','layedit','dicts'], function(){
   var $ = bt.jquery,form = bt.form();
   dicts = bt.dicts.init();

    //自定义验证规则
//        form.verify({
//            name: function(value){
//                if(value.length < 2){
//                    return '角色名称至少得2个字符！';
//                }
//                if(value.length>20){
//                    return '角色名称不能大于20个字符！';
//                }
//            },
//            code:function(value){
//                if(value && value.length > 30){
//                    return '代码编号不能大于30个字符！';
//                }
//            },
//            description:function(value){
//                if(value && value.length > 60){
//                    return '角色描述不能大于60个字符！';
//                }
//            }
//        });

    //监听提交
    form.on('submit(roleSubmit)', function(data){
        if(typeof(data.field.useable) == "undefined"){
            data.field.useable = 0;
        }else{
            data.field.useable = 1;
        }
        var action = $('input[name="id"]').val()?"update":"save";
        $.post(basePath + "/sys/role/" + action,data.field,function(result){
            if(result.success){
                parent.layer.closeAll("",true);
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });


});

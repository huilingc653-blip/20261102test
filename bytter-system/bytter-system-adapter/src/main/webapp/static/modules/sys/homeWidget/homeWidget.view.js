
    bt.use(['form','jquery','selectTree','dicts'], function(){
        var form = bt.form()
                ,layer = bt.layer
                ,laydate = bt.laydate
                ,$  = bt.jquery;
        var dict = bt.dicts.init();
        //自定义验证规则
        form.verify({
            sortVerify:function(value){
                var pattern = /^-?\d{0,5}$/;
                if(value && !pattern.test(value)){
                    // 排序值为正整数，并且不能大于5位数！
                    return bt.lang('sys.sysCorp.sortVerify.maxLength');
                }
            }
        });

        //监听提交
        form.on('submit(userSubmit)', function(data){

//            if(typeof(data.field.isAdmin) == "undefined"){
//                data.field.isAdmin = 0;
//            }else{
//                data.field.isAdmin = 1;
//            }
//            if(typeof(data.field.status) == "undefined"){
//                data.field.status = 0;
//            }else{
//                data.field.status = 1;
//            }

            var action = $('input[name="id"]').val()?"update":"save";
            $.post(basePath + "/sys/homeWidget/" + action, data.field,function(result){
                if(result.success){
                    parent.layer.closeAll("",true);
                }
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            },'JSON');
            return false;
        });


    });

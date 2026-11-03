
bt.use(['form','jquery'], function(){
    var form = bt.form()
            ,layer = bt.layer
            ,$  = bt.jquery;


    //监听提交
    form.on('submit(userSubmit)', function(data){
        bt.loading.show();
        var temp = {};
        var roleId = [];
        $('input[type="checkbox"]:checked').each(function(){
            roleId.push($(this).val());
        });
        var userId = $('input[name="id"]').val();
        $.post(basePath + "/sys/user/assignRoles",{ids:roleId,userId:userId},function(result){
            bt.loading.hide();
            if(result.success){
                parent.layer.closeAll();
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
        return false;
    });


});

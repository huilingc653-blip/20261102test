  bt.use(['form','billPool','select','jquery','selectType','selectTable','bank','bankInfo','btdate','dateType','dicts'], function(){
        var $ = bt.jquery,
        form = bt.form();
        bt.selectType.init();
        bt.billPool.init();
        bt.bankInfo.init();
        bt.dicts.init();
        bt.bank.init({
        	elem:'.bank'
    	})
        //提交申请信息
        form.on('submit(retreat)', function(data){
            bt.loading.show();
            submitForm(data);
        });
        //返回
        $("[name='revert']").click(function(){
            parent.layer.closeAll();
        });
    });

    function submitForm(data){
        $.post(basePath + '/billPool/pdtst/saveApplyDtl', data.field, function (result) {
            bt.loading.hide();
            if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                parent.layer.closeAll();
            }else{
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
            }
            return false;
        },'JSON');
    }
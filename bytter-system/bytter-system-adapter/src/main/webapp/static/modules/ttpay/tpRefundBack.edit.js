var selectTree,$,form,dicts,corp,moneyType;
bt.use(['form','moneyType'], function(){
    $ = bt.jquery,form = bt.form();

    moneyType = bt.moneyType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/ttpay/refundOrderQuery/update";
        if(data.field.type === 'check'){
            action = "/ttpay/refundOrderCheck/update";
        }
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            if (result.success) {
                if (bt.navtab) {
                    closeForms();
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
            parent.layer ? parent.layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            }) : layer.alert(result.msg, {
                icon: result.success ? 1 : 2
            });
        }, 'JSON');
        return false;
    });

    // 自定义规则
    form.verify({
        msg:function (value) {
            if(value.length>20){
                return bt.lang('common.message.config.tooLength');
            }
        }
    });

});






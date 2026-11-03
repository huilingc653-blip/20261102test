var corpId = "";
bt.use(['form', 'layedit'], function () {
    var $ = bt.jquery, form = bt.form();
    form.verify({

    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var ids = $("#ids").val();//额度分配核准ids
        var checkMsg = $("#checkMsg").val();//核准意见
        //当不通过时需要填写核准意见
        var checkType=$(this).attr("value");
        if(checkType =='0' && checkMsg.trim()==''){
            layer.alert(bt.lang('billPool.Limit.approvalOpinionNotNull'), {icon: 7});
            return false;
        }
        data.field.checkType=checkType;
        bt.loading.show();
        var action = "/billpool/limitAllotCheck/save";
        $.post(basePath + action, data.field, function (result) {
            bt.loading.hide();
            if (result.success) {
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            } else {
                parent.layer ? parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }
        }, 'JSON');
        return false;
    });
});



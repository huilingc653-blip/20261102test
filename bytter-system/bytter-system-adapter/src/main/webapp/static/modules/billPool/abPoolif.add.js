var corpId = "";
bt.use(['form', 'layedit', 'btdate', 'dicts','selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    form.verify({
        codeCheck: function (value) {
            var success = false;
            $.ajax({
                type: 'POST',
                url: basePath + "/billpool/poolif/checkPifCodeOrPifName",
                data: {'pifCode': value},
                async: false,
                dataType: 'json',
                success: function (result) {
                    if (!result.success) {
                        success = true;
                    }
                }
            });
            if (success) {
                return bt.lang('billPool.abPoolif.pifCode.repeat');
            }
        },
        nameCheck: function (value) {
            var success = false;
            $.ajax({
                type: 'POST',
                url: basePath + "/billpool/poolif/checkPifCodeOrPifName",
                data: {'pifName': value},
                async: false,
                dataType: 'json',
                success: function (result) {
                    if (!result.success) {
                        success = true;
                    }
                }
            });
            if (success) {
                return bt.lang('billPool.abPoolif.pifName.repeat');
            }
        }
    });



    //监听提交
    form.on('submit(submit)', function (data) {
        var bifCodeDate=data;
        bifCodeDate.bifcode=data.field.bifCode
        if(checkBifCode(bifCodeDate)){
            bt.loading.show();
            var action = "/billpool/poolif/save";
            $.post(basePath + action, data.field, function (result) {
                bt.loading.hide();
                if (result.success) {
                    parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    parent.layer ? parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }
            }, 'JSON');
        }
        return false;
    });
});

//检查银行接口代码是否存在，同一个银行接口只能建一个池接口
function checkBifCode(data) {
    if(data.bifcode==null || data.bifcode==''){
        return false;
    }
    var success = false;
    $.ajax({
        type: 'POST',
        url: basePath + "/billpool/poolif/checkPifCodeOrPifName",
        data: {'bifCode': data.bifcode},
        async: false,
        dataType: 'json',
        success: function (result) {
            if (!result.success) {
                success = true;
            }
        }
    });
    if (success) {
        layer.alert(bt.lang("billPool.abPoolif.bifCode.repeat"), {icon: 3});
        return false;
    }else{
        return true;
    }
}


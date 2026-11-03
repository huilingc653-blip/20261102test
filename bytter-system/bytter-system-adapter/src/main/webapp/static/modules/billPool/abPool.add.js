var corpId = "";
bt.use(['form', 'layedit', 'btdate', 'dicts','billPoolif'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.billPoolif.init();
    form.verify({
        codeCheck: function (value) {
            var success = false;
            $.ajax({
                type: 'POST',
                url: basePath + "/billpool/pool/checkBpCodeOrBpName",
                data: {'bpCode': value},
                async: false,
                dataType: 'json',
                success: function (result) {
                    if (!result.success) {
                        success = true;
                    }
                }
            });
            if (success) {
                return bt.lang('billPool.abPool.bpCode.repeat');
            }
        },
        nameCheck: function (value) {
            var success = false;
            $.ajax({
                type: 'POST',
                url: basePath + "/billpool/pool/checkBpCodeOrBpName",
                data: {'bpName': value},
                async: false,
                dataType: 'json',
                success: function (result) {
                    if (!result.success) {
                        success = true;
                    }
                }
            });
            if (success) {
                return bt.lang('billPool.abPool.bpName.repeat');
            }
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var bifCodeDate=data;
        bifCodeDate.value=data.field.pifId
        var bpCode = data.field.bpCode
        var bpMode = data.field.bpMode
        if(pifIdCheck(bifCodeDate)){
            if (bpCode === '9015' && bpMode !== '1') { // 兴业银行
                layer.alert('兴业银行的池模式只能为集团票据池（统收统支）', {icon: 3});
                return false;
            } else if (bpCode === '9018' && bpMode !== '1') { // 中国建设银行
                layer.alert('建设银行的池模式只能为集团票据池（统收统支）', {icon: 3});
                return false;
            } else if (bpCode === '9009' && bpMode === '2') { // 中国农业银行
                layer.alert('农业银行的池模式只能为集团票据池（统收统支）和单一票据池', {icon: 3});
                return false;
            } else if (bpCode === '9022' && bpMode === '3') { // 浙商银行
                layer.alert('浙商银行的池模式只能为集团票据池（统收统支）和集团票据池（自收自用）', {icon: 3});
                return false;
            } else if (bpCode === '9001' && bpMode !== '1') { // 中信银行
                layer.alert('中信银行的池模式只能为集团票据池（统收统支）', {icon: 3});
                return false;
            } else if (bpCode === '9021' && bpMode === '2') { // 中国银行
                layer.alert('中国银行的池模式只能为集团票据池（统收统支）和单一票据池', {icon: 3});
                return false;
            }
            bt.loading.show();
            var action = "/billpool/pool/save";
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

//检查池接口是否存在，同一个池接口只能建一个票据池
function pifIdCheck(data) {
    if(data.value==null || data.value==''){
        return false;
    }
    var success = false;
    $.ajax({
        type: 'POST',
        url: basePath + "/billpool/pool/checkBpCodeOrBpName",
        data: {'pifId': data.value},
        async: false,
        dataType: 'json',
        success: function (result) {
            if (!result.success) {
                success = true;
            }
        }
    });
    if (success) {
        layer.alert(bt.lang("billPool.abPool.pifId.repeat"), {icon: 3});
        return false;
    }else{
        return true;
    }
}


bt.use(['form', 'layedit', 'dicts', 'selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    var select;
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/foreign/fextrangroup/modify";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.closeCurrent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    $('#appTable').on('click','.operate-button',function () {
        var type = $(this).data('type');
        var tranId = $(this).closest('tr').find('.tranid').val();
        if (type =='delete'){
            parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
                $.post(basePath + "/foreign/fextranreq/updateTranGroup/" + tranId, function (result) {
                    location.reload(true);
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            });
        }else if (type=='view'){
            if (bt.navtab) {
                bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/view/" + tranId);
                return;
            }
            parent.layer.open({
                type: 2,
                area: ['950px', '530px'],
                maxmin: true,
                content: basePath + "/foreign/fextranreq/view/" + tranId
            });
        }
    })

    bt.changeProId = function () {
        var proId = select.get("proId").val();
        if (!proId) {
            return;
        }
        var url = basePath + "/foreign/fexproduct/listFexProduct/" + proId;
        $.ajax({
            type: "GET",
            url: url,
            dataType: "json",
            success: function(msg){
                var obj = typeof msg === 'string' ? JSON.parse(msg) : msg;
                if (!obj) {
                    return;
                }
                var ruleAttr = {
                    "data-defaultvalue": obj.trangroupRuleId || "",
                    "data-value": obj.trangroupRuleId || ""
                };
                if (obj.isAllowedChange === '1') {
                    ruleAttr["data-disabled"] = false;
                } else {
                    ruleAttr["data-disabled"] = true;
                }
                select.get('ruleId').attr(ruleAttr);
            }
        });
    }

    bt.dicts.init();
    select= bt.selectType.init();
});

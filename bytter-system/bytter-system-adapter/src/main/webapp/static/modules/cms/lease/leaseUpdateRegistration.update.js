bt.use(['form', 'layedit', 'dateType', 'dicts', 'rateType', 'moneyType', "selectType"], function () {
    var $ = bt.jquery, form = bt.form(), moneyFun = bt.moneyType.getFun();
    //初始化日期
    var dateType = bt.dateType.init();
    bt.moneyType.init();
    var selectTypes = bt.selectType.init();//下拉框
    //初始化字典
    var dict = bt.dicts.init();
    //初始化利率
    bt.rateType.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var status = $(this).attr("value");
        var action = "/cms/lease/UpdateObjectInfo/modify";
        var leaseItemCode=$("#leaseItemCode").val();
        var id=$("#id").val();
        data.field.status = status;//提交状态
        data.field.curName = selectTypes.get("curId").currentObject.elem.find("option:selected").data().curname;//获取币别选择的全部数据
        bt.jquery.ajax({
            url: basePath + "/cms/lease/UpdateObjectInfo/checkLeaseItemCode/" + leaseItemCode + "/" +id,
            method: 'POST',
            dataType: 'json',
            success: function (t) {
                if (t == "false") {
                    layer.alert(bt.lang('租赁物编号不能重复'), {icon: 7});//租赁物编号不能重复
                } else {
                    $.post(basePath + action, data.field, function (result) {
                        parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        if (result.success) {
                            if (!bt.navtab) {
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            } else {
                                bt.navtab.closeCurrent();
                                bt.navtab.refreshCurrent();
                            }
                        }
                    }, 'JSON');
                }
            },
        });
        return false;
    });

    //关闭窗口
    doClose = function () {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    }
});

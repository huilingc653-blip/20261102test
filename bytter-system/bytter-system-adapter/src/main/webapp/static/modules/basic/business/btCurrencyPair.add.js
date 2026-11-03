bt.use(['form', 'layedit', 'dicts', 'moneyType', 'selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.moneyType.init();
    var select = bt.selectType.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/basic/business/btcurrencypair/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                parent.layer.closeAll();
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshParentTable();
                        location.reload();//重刷页面
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            })
        }, 'JSON');
        return false;
    });

    bt.cur1 = function () {
        if (select.get("curId1").val() === select.get("curId2").val()) {
            select.get("curId2").val("")
        }
        if(select.get("curId1").val()!=null &&select.get("curId2").val()!=null){
            var data = select.get("curId1").val()+','+select.get("curId2").val();
            select.get("baseCurId").attr({"data-disabled":"","data-displayData":data})
        }
    };

    bt.cur2 = function () {
        if (select.get("curId1").val() === select.get("curId2").val()) {
            select.get("curId1").val("")
        }
        if(select.get("curId1").val()!=null &&select.get("curId2").val()!=null){
            var data = select.get("curId1").val()+','+select.get("curId2").val();
            select.get("baseCurId").attr({"data-disabled":"","data-displayData":data})
        }
    };

});

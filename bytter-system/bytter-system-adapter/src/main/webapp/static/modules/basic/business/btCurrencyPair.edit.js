bt.use(['form', 'layedit', 'dicts', 'moneyType', 'selectType'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.moneyType.init();
    let select=bt.selectType.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/basic/business/btcurrencypair/modify";
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

    bt.cur1 = function () {
        if (select.get("curId1").val() === select.get("curId2").val()) {
            select.get("curId2").val("");
            select.get("baseCurId").val("");
        }
        let data = select.get("curId1").val()+','+select.get("curId2").val();
        select.get("baseCurId").attr({"data-disabled":"","data-displayData":data})
        select.render();
    };

    bt.cur2 = function () {
        if (select.get("curId1").val() === select.get("curId2").val()) {
            select.get("curId1").val("");
            select.get("baseCurId").val("");
        }
        let data = select.get("curId1").val()+','+select.get("curId2").val();
        select.get("baseCurId").attr({"data-disabled":"","data-displayData":data})
        select.render();
    };

    /**
     * 重置按钮回调
     */
    resetCallBack = function () {
        location.reload();
    }
    // bt.cur3= function () {
    //     let data = select.get("curId1").val()+','+select.get("curId2").val();
    //     select.render()
    //     console.log(data)
    //
    // };

    // select.get("baseCurId").attr("data-displayData",data);
});

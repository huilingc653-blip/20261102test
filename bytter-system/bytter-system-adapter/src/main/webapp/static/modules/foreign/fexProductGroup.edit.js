bt.use(['form', 'layedit', 'dicts', 'selectType', 'fexProductInfo','fexProductView'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    var selectType = bt.selectType.init();
    var fexProductInfo = bt.fexProductInfo.init();
    var fexProductView = bt.fexProductView.init();
    bt.fexProductInfoParams = function () {
        fexProductInfo.set({
            "tranType": $("#tranType").val(),
            "id": $("#id").val(),
            "proId": selectType.get("proId").val()
        });
        return true;
    }
    bt.fexProductViewParams = function () {
        fexProductView.set({"id": $("#billId").val()});
        return true;
    }
    bt.fexProductInfoCallback = function (datas, rom) {
        var billIds = "";
        $.each(datas.data, function (index, data) {
            if (billIds == "") {
                billIds += data.id;
            } else {
                billIds += "," + data.id;
            }
        });
        $("#billId").val(billIds);
    }
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/foreign/fexproduct/modify";
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
});

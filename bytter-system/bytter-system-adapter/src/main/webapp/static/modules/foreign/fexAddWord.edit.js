bt.use(['form', 'layedit', 'selectTable', 'dicts'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.dicts.init();

    //交易编码
    var tranCodeInit = bt.selectTable.init({
        elem: "#tranCodeTable",
        url: basePath + "/foreign/fextraninfo/list",
        searchInput: "#tranCode",
        scrollX: true,
        scrollY: "300px",
        columns: [
            {data: "tranCode",},
            {data: "tranName",}
        ],
        clickCallback: function (data) {
            if (data) {
                $('#tranName').val(data.tranName);
                $('#tranCode').val(data.tranCode);
            } else {
                $('input.text').val('');
            }
        }
    });

    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/foreign/fexAddWord/modify";
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

bt.use(['form', 'tableGrid', 'jquery', 'dicts'], function () {
    var $ = bt.jquery;
    bt.dicts.init();

    var addFunc = function () {
        parent.layer.open({
            type: 2,
            area: ['830px', '530px'],
            content: basePath + "/sys/lang/view/",
            end: function (flag) {
                if(flag){
                    table.trigger("reloadGrid");
                }
            }
        });
    }

    var editFunc = function (rowData) {
        var id = rowData.id ? rowData.id : '';
        parent.layer.open({
            type: 2,
            area: ['830px', '530px'],
            content: basePath + "/sys/lang/view/?id=" + rowData.id,
            end: function (flag) {
                if(flag){
                    table.trigger("reloadGrid");
                }
            }
        });
    }

    var delFunc = function () {
        var rowData = table.getSelectRowsData();
        var ids = [];
        for (var i = 0; i < rowData.length; i++) {
            ids.push(rowData[i].id);
        }
        parent.layer.confirm(bt.lang("sys.notification.deleteShow"), function () {
            $.post(basePath + "/sys/lang/delete/", {langIds: ids}, function (result) {
                table.trigger("reloadGrid");
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }

    var table = bt.tableGrid.init({
        elem: "#appTable",
        url: basePath + "/sys/lang/tableGrid.json",
        colModel: [
            {
                name: "langCode",
                width: 50,
                label: bt.lang('sys.lang.language'),
                formatter: function (cellvalue, options, rowObject) {
                    return bt.dict('sys_lang', cellvalue);
                }
            },
            // 代码
            {name: "langKey", label: bt.lang('sys.lang.code')},
            // 代码值
            {name: "langContext", label: bt.lang('sys.lang.codeValue')}
        ],
        addFunc: addFunc,
        editFunc: editFunc,
        delFunc: delFunc
    });
})

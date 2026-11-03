bt.use(['form', 'tableGrid', 'jquery', 'selectType'], function () {
    var $ = bt.jquery;
    bt.selectType.init();
    /**
     * 新增
     */
    var addFunc = function () {
        var data = table.getSelectRowsData();
        var id = "";
        if (data && data.length == 1) {
            console.log(data);
            id = data[0].id;
        }
        parent.layer.open({
            type: 2,
            area: ['830px', '530px'],
            content: basePath + "/sys/dict/view?type=add&id=" + id,
            end: function (flag) {
                if(flag){
                    table.trigger("reloadGrid");
                }
            }
        });
    }
    /**
     * 编辑
     * @param rowData
     */
    var editFunc = function (rowData) {
        var id = rowData.id ? rowData.id : '';
        parent.layer.open({
            type: 2,
            area: ['830px', '530px'],
            content: basePath + "/sys/dict/view/?id=" + rowData.id,
            end: function (flag) {
                if(flag){
                    table.trigger("reloadGrid");
                }
            }
        });
    }
    /**
     * 删除
     */
    var delFunc = function () {
        var rowData = table.getSelectRowsData();
        var ids = [];
        var names = "";
        for (var i = 0; i < rowData.length; i++) {
            ids.push(rowData[i].id);
            names += rowData[i].name;
            if (i != rowData.length - 1) {
                names += ",";
            }
        }
        parent.layer.confirm(bt.lang("sys.notification.deleteShow"), function () {
            $.post(basePath + "/sys/dict/delete", {dictIds: ids}, function (result) {
                table.trigger("reloadGrid");
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }

    var table = bt.tableGrid.init({
        elem: "#appTable",
        url: basePath + "/sys/dict/tableGrid.json",
        colModel: [
            // 字典组
            {name: "groupName", width: 100, label: bt.lang('sys.dict.dictGroup')},
            // 所属字典组代码
            {name: "groupCode", label: bt.lang('sys.dict.groupCodeBelong')},
            // 排序
            {name: "sort", label: bt.lang('common.message.sort'), width: 30},
            // 字典名
            {name: "name", label: bt.lang('sys.dict.name')},
            // 代码值
            {name: "code", label: bt.lang('sys.dict.code')}
        ],
        addFunc: addFunc,
        editFunc: editFunc,
        delFunc: delFunc
    });

})

bt.use(['form', 'layedit', 'selectType', 'datatable'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/allocation/bisaccgatherpolicy/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            },
            {
                text: bt.lang('common.button.submit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if (len < 1) {
                        layer.msg('请至少选择1条数据');
                        return;
                    }
                    var ids = "";
                    var rowData = data.splice(0, len);
                    for (var i = 0; i < rowData.length; i++) {
                        if (ids == "") {
                            ids += rowData[i].id;
                        } else {
                            ids += "," + rowData[i].id;
                        }
                    }
                    var gatherPolicyId = $("#gatherPolicyId").val();//策略ID
                    var prentAccId = $("#prentAccId").val();//父账号ID
                    var curId = $("#curId").val();//币别Id

                    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
                        $.post(basePath + "/allocation/bisaccgatherpolicy/save", {
                            ids: ids,
                            gatherPolicyId: gatherPolicyId,
                            prentAccId: prentAccId,
                            curId: curId
                        }, function (result) {
                            if (result.success) {
                                location.reload();
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            }
                            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                        return false;
                    });
                }
            }
        ]
        , columns: [
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "attributeName", bSortable: true},
            {data: "bifName", bSortable: true},
            {data: "natureName", bSortable: true}
        ]
    });
});

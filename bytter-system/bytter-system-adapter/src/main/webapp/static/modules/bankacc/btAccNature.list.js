bt.use(['form', 'datatable'], function () {
    $ = bt.jquery, form = bt.form();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        query: "search",
        checkbox: true,
        order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/btaccnature/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    batchDelete(e, dt, node, config);
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "natureCode", bSortable: true, className: "center"},
            {data: "natureName", bSortable: true, className: "center"},
            {data: "rmk", bSortable: true, className: "center"},
            {
                data: "validSign", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("valid_sign", full.validSign);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    html += '<span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>'
                    html += '<span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });

    function add() {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/bankacc/btaccnature/add");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/btaccnature/add",
            end: function () {
                location.reload();
            }
        });
    }

    view = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/btaccnature/view/" + data.id
        });
    }

    update = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            if (bt.navtab) {
                bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btaccnature/update?id=" + data.id);
                return;
            }
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/bankacc/btaccnature/update?id=" + data.id,
                end: function () {
                    location.reload();
                }
            });
        }
    }

    del = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/bankacc/btaccnature/delete/" + data.id, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    if (result.success) {
                        bt.datatable.refreshCurrentPage();
                    }
                });
            }, 'JSON');
        });
    }

    /**
     * 批量删除
     * @param e
     * @param dt
     * @param node
     * @param config
     */
    function batchDelete(e, dt, node, config) {
        var data = dt.rows('.selected').data();
        if (data.length < 1) {
            layer.msg(bt.lang('common.message.confirm.selectData'));
            return;
        }
        var ids = "";
        var rowData = data.splice(0, data.length);
        for (var i = 0; i < rowData.length; i++) {
            if (ids == "") {
                ids += rowData[i].id;
            } else {
                ids += "," + rowData[i].id;
            }
        }
        parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
            $.post(basePath + "/bankacc/btaccnature/batchDelete/", {ids: ids}, function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    if (result.success) {
                        bt.datatable.refreshCurrentPage();
                    }
                });
            }, 'JSON');
        });
    }
});

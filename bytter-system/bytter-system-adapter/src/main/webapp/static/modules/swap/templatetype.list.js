bt.use(['form', 'datatable', 'select', 'date'], function () {
    $ = bt.jquery, form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [1, 'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/erp/swap/templatetype/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('erp:swap:templatetype:add'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('erp:swap:templatetype:update'),
                text: '<i class="layui-icon">&#xe642;</i> ' + bt.lang('common.button.edit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    var rowData = data.splice(0, len);
                    if (rowData && rowData.length > 0) {
                        update(rowData[0]);
                    } else {
                        layer.msg(bt.lang("common.button.selectnum"));
                    }
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "typeCode", bSortable: true},
            {data: "typeName", bSortable: true},
            {
                data: "opDate", width: "150px", className: 'center', bSortable: true, render: function (data) {
                    return bt.date.format(data, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {data: "status", bSortable: true},
            {data: "rmk", bSortable: false},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('erp:swap:templatetype:view')) {
                        html += '<a class="" href="javascript:;" data-row="' + meta.row + '" onclick="view(this)">'
                            + bt.lang("common.button.view") + '</a>';
                    }
                    if (bt.isResource('erp:swap:templatetype:update')) {
                        if (html) {
                            html += '<span class="text-explode">|</span>';
                        }
                        html += '<a class="" href="javascript:;" data-row="' + meta.row
                            + '" onclick="update(layui.jquery(\'#appTable\').DataTable().row(layui.jquery(this).data(\'row\')).data())">'
                            + bt.lang("common.button.edit") + '</a>';
                    }
                    if (bt.isResource('erp:swap:template:view')) {
                        if (html) {
                            html += '<span class="text-explode">|</span>';
                        }
                        html += '<a class="" href="javascript:;" data-row="' + meta.row
                            + '" onclick="manage(layui.jquery(\'#appTable\').DataTable().row(layui.jquery(this).data(\'row\')).data())">'
                            + bt.lang("table.template.maintenance") + '</a>';
                    }
                    if (bt.isResource('erp:swap:templatetype:delete')) {
                        if (html) {
                            html += '<span class="text-explode">|</span>';
                        }
                        html += '<a class="" href="javascript:;" data-row="' + meta.row + '" onclick="deleteColumn(this)">'
                            + bt.lang("common.button.delete") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/erp/swap/templatetype/add");
        return;
    }
    layer.open({
        type: 2,
        title: bt.lang('common.add.data'),
        area: ['850px', '650px'],
        maxmin: true,
        content: basePath + "/erp/swap/templatetype/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var perContent = layer.open({
        type: 2,
        area: ['800px', '600px'],
        maxmin: true,
        content: basePath + "/erp/swap/templatetype/view/" + data.id
    });
    layer.full(perContent);
}

function update(data) {
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/erp/swap/templatetype/update/" + data.id);
            return;
        }
        layer.open({
            type: 2,
            area: ['850px', '650px'],
            maxmin: true,
            content: basePath + "/erp/swap/templatetype/update/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function manage(data) {
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('模版维护--' + data.typeName), basePath + "/erp/swap/template/list/" + data.id + "?name=" + data.typeName);
            return;
        }
        layer.open({
            type: 2,
            title: bt.lang('模版维护--' + data.typeName),
            area: ['850px', '650px'],
            maxmin: true,
            content: basePath + "/erp/swap/template/list/" + data.id + "?name=" + data.typeName,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
            $.post(basePath + "/erp/swap/templatetype/delete/" + data.id, function (result) {
                datatable && datatable.draw();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
}
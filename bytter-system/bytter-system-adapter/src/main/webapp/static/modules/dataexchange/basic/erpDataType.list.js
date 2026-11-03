bt.use(['form', 'datatable'], function () {
    $ = bt.jquery, form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,


        ajax: {
            'url': basePath + "/dataexchange2/basic/erpdatatype/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('dataexchange:basic:erpdatatype:add'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "code", bSortable: true},
            {data: "name", bSortable: true},
            {data: "tableName", bSortable: true},
            {data: "keyField", bSortable: true},
            {data: "valueField", bSortable: true},
            {data: "queryCondition", bSortable: false},
            {
                data: "validSign", bSortable: true, render: function (data, type, full, meta) {
                    return full.validSignName;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('dataexchange:basic:erpdatatype:view')) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">'
                            + bt.lang("common.button.view") + '</a>';
                    }
                    if (bt.isResource('dataexchange:basic:erpdatatype:update')) {
                        if (html) {
                            html += '<span class="text-explode">|</span>';
                        }
                        html += '<a className="" href="#" data-row="' + meta.row + '" onClick="update(this)">'
                            + bt.lang("common.button.edit") + '</a>';
                    }
                    if (bt.isResource('dataexchange:basic:erpdatatype:delete')) {
                        if (html) {
                            html += '<span class="text-explode">|</span>';
                        }
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">'
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
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/dataexchange2/basic/erpdatatype/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/dataexchange2/basic/erpdatatype/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/dataexchange2/basic/erpdatatype/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/dataexchange2/basic/erpdatatype/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/dataexchange2/basic/erpdatatype/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/dataexchange2/basic/erpdatatype/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
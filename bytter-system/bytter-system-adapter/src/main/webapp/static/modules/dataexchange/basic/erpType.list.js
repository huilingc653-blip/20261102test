bt.use(['form', 'datatable'], function () {
    $ = bt.jquery, form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,


        ajax: {
            'url': basePath + "/dataexchange2/basic/erptype/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('dataexchange:basic:erptype:add'),
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
            {data: "typeCode", bSortable: true},
            {data: "typeName", bSortable: true},
            {data: "rmk", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('erp:swap:templatetype:view')) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">'
                            + bt.lang("common.button.view") + '</a>';
                    }
                    if (bt.isResource('dataexchange:basic:erptype:update')) {
                        if (html) {
                            html += '<span class="text-explode">|</span>';
                        }
                        html += '<a className="" href="#" data-row="' + meta.row + '" onClick="update(this)">'
                            + bt.lang("common.button.edit") + '</a>';
                    }
                    if (bt.isResource('dataexchange:basic:erptype:delete')) {
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
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/dataexchange2/basic/erptype/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/dataexchange2/basic/erptype/add",
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
        content: basePath + "/dataexchange2/basic/erptype/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/dataexchange2/basic/erptype/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/dataexchange2/basic/erptype/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/dataexchange2/basic/erptype/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
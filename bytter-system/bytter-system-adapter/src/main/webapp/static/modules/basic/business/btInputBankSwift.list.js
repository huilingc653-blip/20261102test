bt.use(['form', 'datatable'], function () {
    $ = bt.jquery, form = bt.form();

    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[1, 'desc']],
        colReorder: true,
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/basic/business/btinputbankswift/datatable"
        },
        selectCss: false,
        buttons: [
            {
                hide:bt.isnotResource('bt:input:bank:swift:add'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "bankCode", bSortable: true},
            {data: "bankName", bSortable: false},
            {data: "swiftCode", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    if (bt.isResource('bt:input:bank:swift:edit')){
                        html += '<span class="text-explode">|</span>';
                        html += '<a href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                    }

                    if (bt.isResource('bt:input:bank:swift:delete')){
                        html += '<span class="text-explode">|</span>';
                        html += '<a href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
});


/**
 * 新增
 */
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/basic/business/btinputbankswift/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/btinputbankswift/add",
        end: function () {
            table && table.draw();
        }
    });
}

/**
 * 查看
 */
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/btinputbankswift/view/" + data.id
    });
}

/**
 * 修改
 */
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/basic/business/btinputbankswift/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/btinputbankswift/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

/**
 * 删除
 */
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/basic/business/btinputbankswift/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
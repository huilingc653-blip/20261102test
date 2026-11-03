bt.use(['form', 'datatable', 'btImport'], function () {
    $ = bt.jquery, form = bt.form();


    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/basic/business/btinputbankinfo/datatablePrefix"
        },
        selectCss: false,
        buttons: [
            // {
            //     hide:bt.isnotResource('sys:datamanage:bank:btinputbankinfo:operate'),
            //     text: bt.lang('common.button.add'),
            //     action: function (e, dt, node, config) {
            //         add();
            //     }
            // }, {
            //     hide:bt.isnotResource('sys:datamanage:bank:btinputbankinfo:operate'),
            //     text: bt.lang('common.button.imports'),
            //     action: function (e, dt, node, config) {
            //         importExcel();
            //     }
            // },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "bankCode", bSortable: true},
            {data: "bankName", bSortable: false},
            {data: "provName", bSortable: true},
            {data: "cityName", bSortable: true},
            {data: "updateDate", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // if (bt.isResource('sys:datamanage:bank:btinputbankinfo:operate')){
                    //     html += '<span class="text-explode">|</span>';
                    //     html += '<a href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    //     html += '<a href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    // }
                    return html;
                }
            }
        ],
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/basic/business/btinputbankinfo/exportAll";
            openExportExcel(exportUrl, data, '40000001');
            processing(false);
        }
    });
});
/**
 * 导入Excel
 */
importExcel = function () {
    up = bt.btImport.init({
        code: "40000001",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select, cacheCode) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/basic/business/btinputbankinfo/saveImport",
                dataType: "json",
                data: {"cacheCode": cacheCode},
                success: function (result) {
                    bt.datatable.refreshCurrentPage();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }
            });
        }
    });
}

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/basic/business/btinputbankinfo/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/btinputbankinfo/add",
        end: function () {
            table && table.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/btinputbankinfo/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/basic/business/btinputbankinfo/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/btinputbankinfo/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/basic/business/btinputbankinfo/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
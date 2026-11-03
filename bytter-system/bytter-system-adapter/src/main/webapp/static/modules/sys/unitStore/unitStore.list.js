bt.use(['form', 'datatable', 'moneyType','corp','selectType','dicts','btImport'], function () {
    $ = bt.jquery, form = bt.form();
    var moneyFun = bt.moneyType.getFun();
    bt.corp.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[0, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/sys/unitStore/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            // {
            //     extend: "colvis",
            //     text: bt.lang('common.button.theadHide'),
            //     columns: ':not(:first):not(:last)'
            // },
            {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    //layer.alert(bt.lang('common.message.indevelopment'));
                    importExcel();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "corpName", bSortable: true},
            {data: "storeCode", bSortable: true},
            {data: "storeName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>\
	               <a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });
    //导入初始化
    importExcel = function () {
        up = bt.btImport.init({
            code: "40000006",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            success: function (head, data, select) {
                $.ajax({
                    type: "POST",
                    url: basePath + "/sys/unitStore/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(select),
                    //重写success方法
                    success: function (result) {
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        bt.datatable.refreshCurrentPage();
                    }
                });
            }
        });
    }
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/sys/unitStore/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/sys/unitStore/add",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

function view(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/sys/unitStore/view/" + data.id
    });
}

function update(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/sys/unitStore/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/sys/unitStore/update?id=" + data.id,
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}

function deleteColumn(el) {
    var data = $("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/sys/unitStore/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
} 
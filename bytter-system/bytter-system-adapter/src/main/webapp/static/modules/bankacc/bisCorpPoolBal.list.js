var dataTable;
bt.use(['form', 'datatable', 'corp', 'btImport', 'dateType', 'money', 'dicts', 'selectType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.corp.init();
    bt.selectType.init();

    /**
     * 导入功能
     */
    importExcel = function () {
        up = bt.btImport.init({
            code: "10000090",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/bankacc/biscorppoolbal/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    };

    /**
     * 生成列表
     */
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[2, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/biscorppoolbal/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('bankacc:biscorppoolbal:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('bankacc:biscorppoolbal:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            },{
                hide: bt.isnotResource('bankacc:biscorppoolbal:apply'),
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "corpName", bSortable: true},
            {data: "balDate", bSortable: true, className: 'center'},
            {
                data: "bal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bal);
                }
            },
            {
                data: "overdraftAmt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.overdraftAmt);
                }
            },
            {
                data: "availBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.availBal);
                }
            },
            {data: "curName", bSortable: true, className: 'center'},
            {data: "getDate", bSortable: true, className: 'center'},
            {data: "initSignName", bSortable: true, className: 'center'},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                if (full.initSign == "1") {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("netbank.button.change") + '</a><span class="text-explode">|</span>';
                }
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                return html;
            }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/bankacc/biscorppoolbal/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/biscorppoolbal/add",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/biscorppoolbal/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/biscorppoolbal/update?id=" + data.id,
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}

//批量删除
function deleteBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
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
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/bankacc/biscorppoolbal/batchDelete", {
            ids: ids
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//删除
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bankacc/biscorppoolbal/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
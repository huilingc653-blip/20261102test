var billTable = {};
bt.use(['form', 'datatable', 'btdate', 'select', 'dateType', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/billpool/pool/datatable"
        },
        buttons: [
            //新增、导出
            {
                text: bt.lang('common.button.add'),
                hide: bt.isnotResource('abpool:pool:resource:op'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                text: bt.lang('common.button.delete'),
                hide: bt.isnotResource('abpool:pool:resource:op'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "bpCode", bSortable: true},
            {data: "bpName", bSortable: true},
            {data: "bpOrg", bSortable: true},
            {data: "bpModeName", bSortable: true},
            {data: "bpTypeName", bSortable: true},
            {data: "pifName", bSortable: true},
            {data: "rmk", bSortable: true},
            {data: "validSignName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                if (data.validSign == 0) {//禁用状态可以修改
                    html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                    html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="updateStatus(this,1)">' + bt.lang("common.button.enable") + '</a>';
                } else if (data.validSign == 1) {//启用状态不可修改，只能禁用
                    html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="updateStatus(this,0)">' + bt.lang("common.button.taboo") + '</a>';
                }
                html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                return html;
            }
            }
        ],
        fixedColumns: {
            rightColumns: 2
        }
    });
});


//新增跳转
function add() {
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/pool/add",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//修改
function update(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/pool/edit/" + id,
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//查看
function view(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/pool/view/" + id,
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

//修改状态
function updateStatus(el, status) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    var msg = bt.lang('common.message.confirm.enable');
    if (status == 0) {
        msg = bt.lang('common.message.confirm.taboo');
    }
    parent.layer.confirm(msg, function () {
        $.post(basePath + "/billpool/pool/updateStatus", {"id": id, 'status': status}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

//删除数据
function deleteBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var ids;
    for (i = 0; i < len; i++) {
        if (ids) {
            ids = ids + "," + rowData[i].id;
        } else {
            ids = rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/billpool/pool/batchDelete", {"ids": ids}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

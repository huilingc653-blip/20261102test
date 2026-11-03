bt.use(['form', 'datatable', 'selectType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/bisbifrule/datatable"
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
                        $.post(basePath + "/netbank/bisbifrule/batchDelete/", {ids: ids}, function (result) {
                            bt.datatable.refreshCurrentPage();
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
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
            {data: "bifCode", bSortable: true, className: "center"},
            {data: "bifName", bSortable: true, className: "center"},
            {data: "payerName", bSortable: true, className: "textRight"},
            {data: "payerAcc", bSortable: true, className: "textRight"},
            {data: "payerBank", bSortable: true, className: "textRight"},
            {data: "payeeName", bSortable: true, className: "textRight"},
            {data: "payeeAcc", bSortable: true, className: "textRight"},
            {data: "payeeBank", bSortable: true, className: "textRight"},
            {data: "purpose", bSortable: true, className: "textRight"},
            {data: "addWord", bSortable: true, className: "textRight"},
            {
                data: "isPurpose", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isPurpose);
                }
            },
            {
                data: "isBankOnline", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isBankOnline);
                }
            },
            {data: "sendNum", bSortable: true, className: "textRight"},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]

    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/netbank/bisbifrule/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/bisbifrule/add",
        end: function () {
            location.reload();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/bisbifrule/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/netbank/bisbifrule/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/netbank/bisbifrule/update?id=" + data.id,
            end: function () {
                location.reload();
            }
        });
    }
}

function del(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/netbank/bisbifrule/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
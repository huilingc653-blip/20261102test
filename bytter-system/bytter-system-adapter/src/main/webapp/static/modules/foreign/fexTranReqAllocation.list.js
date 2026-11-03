var datatable;
bt.use(['form', 'datatable', 'select', 'dicts', 'selectType','element', 'dateType', 'corp'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fextranreq/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('foreign:fextranreq:allocation:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:allocation:apply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            }, {
                hide: bt.isnotResource('foreign:fextranreq:allocation:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
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
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "proName", bSortable: true, className: 'center'},
            {
                data: "corpId", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return full.corpName;
                }
            },
            {
                data: "targetCorp", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return full.targetCorpName;
                }
            },
            {
                data: "targetAccCurId", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return full.targetAccEnglishCode;
                }
            },
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data: "tranDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {
                data: "isPayed", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("sf_yn", full.isPayed);
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("choBankFinance.status", full.status);
                }
            },
            {
                data: "billFrom", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return full.billFromName;
                }
            },
            {
                data: "relBillNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.relBillNo!="" && full.relBillNo!=null) {
                        var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewRelBill(this)">' + full.relBillNo + '</a>';
                        return html;
                    }else{
                        return '';
                    }
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (full.status == "0" || full.status == "-1") {
                        if (bt.isResource('foreign:fextranreq:allocation:apply', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                        }
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="clone(this)">' + bt.lang("common.button.clone") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fextranreq/add/"+$("#tranType").val());
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/add/"+$("#tranType").val(),
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function clone(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fextranreq/clone/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/clone/" + data.id
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fextranreq/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fextranreq/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/foreign/fextranreq/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量删除
function deleteBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
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
        $.post(basePath + "/foreign/fextranreq/batchDelete", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量提交
function submitBatch(data) {
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
    var encrypt = $("#encrypt").val(); // cfca证书
    if(encrypt == 2 || encrypt == 1){ // 1 天威证书
        signData("外汇交易提交", 11,function () {
            parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
                bt.loading.show();
                $.post(basePath + "/foreign/fextranreq/batchSubmit", {ids: ids}, function (result) {
                    bt.loading.hide();
                    var icon = result.success ? (result.attributes ? result.attributes.succIcon : 1) : 2;
                    datatable.draw();
                    parent.layer.alert(result.msg, {icon: icon});
                }, 'JSON');
            });
        })
    }else{
        parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
            bt.loading.show();
            $.post(basePath + "/foreign/fextranreq/batchSubmit", {ids: ids}, function (result) {
                bt.loading.hide();
                var icon = result.success ? (result.attributes ? result.attributes.succIcon : 1) : 2;
                datatable.draw();
                parent.layer.alert(result.msg, {icon: icon});
            }, 'JSON');
        });
    }
    return false;
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
        $.post(basePath + "/foreign/fextranreq/batchDelete", {
            ids: ids
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

function viewRelBill(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fextranreq/viewBillNo/" + data.relBillNo);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fextranreq/viewBillNo/" + data.relBillNo
    });
}

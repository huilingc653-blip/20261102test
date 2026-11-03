var datatable;
bt.use(['form', 'datatable', 'dateType', 'selectType', 'corp', 'moneyType', 'bank', 'btImport'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.bank.init({
        elem: '.bank',
    });

    //业务类型选择
    busTypeCallBack = function (data) {
        selectType.get("opType").attr("data-url", basePath + "/expense/nisbusinesstype/getOpType?busType=" + data.bustype);
    }

    opTypeCallBack = function (data) {
               var busTypeValue =selectType.get("busType").val();
               var opTypeValue =selectType.get("opType").val();
               if (opTypeValue!=""&&busTypeValue=="") {
                 parent.layer.alert(bt.lang('common.message.busTypeChose'));
                 $('select[name="eqs_opType"]').val("");
                  return false;
                  }
    }

    var selectType = bt.selectType.init();

    //根据是否挂 计划 选择不同的模板
    var importCode = "10000004";
    if ($("#budgetSel").val() === "1" && $("#expFbsin").val() === "0") {
        importCode = "100000041";
    }
    importExcel = function () {
        up = bt.btImport.init({
            code: importCode,
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/expense/nisexpensedetail/saveImport",
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
    }

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/expense/nisexpensedetail/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:expense:nisExpenseDetail:touchApply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('netbank:expense:nisExpenseDetail:touchApply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            }, {
                hide: bt.isnotResource('netbank:expense:nisExpenseDetail:touchApply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            }, {
                hide: bt.isnotResource('netbank:expense:nisExpenseDetail:touchApply'),
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
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
            {data: "expenseId", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.status == -1){
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data: "corpName", bSortable: true},
            {data: "expenseDate", bSortable: true, className: 'center'},
            {
                data: "expenseMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.expenseMoney);
                }
            },
            {data: "accName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "bankCode", bSortable: true, className: 'center'},
            {data: "busTypeName", bSortable: true, className: 'center'},
            {data: "opTypeName", bSortable: true, className: 'center'},
            {data: "purpose", bSortable: true},
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisExpenseDetail.status", full.status);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:expense:nisExpenseDetail:touchApply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ]
    });

    /**
     * 重置
     */
    resetSelect = function () {
        selectType.get("opType").attr("data-url", basePath + "/expense/nisbusinesstype/getOpType?busType=02");
    }
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/expense/nisexpensedetail/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/expense/nisexpensedetail/add",
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
        content: basePath + "/expense/nisexpensedetail/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/expense/nisexpensedetail/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/expense/nisexpensedetail/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/expense/nisexpensedetail/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
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
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/expense/nisexpensedetail/batchDelete", {ids: ids}, function (result) {
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
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        $.post(basePath + "/expense/nisexpensedetail/batchSubmit", {ids: ids}, function (result) {
            datatable.draw();
            showBatchSubmitResult(result);
        }, 'JSON');
    });
}

function showBatchSubmitResult(result) {
    if (paymentLimitAlert.isBatchResult(result)) {
        parent.layer.alert(paymentLimitAlert.batchContent(result), paymentLimitAlert.batchAlertOptions());
        return;
    }
    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
}

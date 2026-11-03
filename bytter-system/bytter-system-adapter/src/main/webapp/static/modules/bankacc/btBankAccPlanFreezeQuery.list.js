var $, form, moneyType, selectType, datatable;
bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'selectType', 'accountTable', 'bank', 'moneyType', 'mulselect'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    moneyType = bt.moneyType.init();
    selectType = bt.selectType.init();

    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
    }
    var accountTable = bt.accountTable.init();

    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_bankAcc").setCorp(id);
    }

    bt.corp.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
        },
        checkbox: true,
        ajax: {
            'url': basePath + "/bankacc/btbankaccplanfreeze/queryDataTable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bankAcc:btBankAccPlanFreezeQuery:apply'),//应用权限
                text: '删除',
                action: function (e, dt, node, config) {
                    manualPlanDelete(e, dt, node, config);
                }
            },
            {
                hide: bt.isnotResource('netbank:bankAcc:btBankAccPlanFreezeQuery:apply'),//应用权限
                text: '解冻',
                action: function (e, dt, node, config) {
                    manualPlanUnFreeze(e, dt, node, config);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        insertInformation: function (settings) { //底部插入信息回调方法，返回html字符串
            var showHTML = '已选笔数小计: ' + bt.datatable.getSelectData('#appTable').length || 0;
            return '<div style="color: red" <span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span></div>';
        },
        btClickRowCallback: function () {
            $("#sumMoneyBillSpan").text('已选笔数小计: ' + bt.datatable.getSelectData('#appTable').length || 0)
        },
        columns: [
            {data: "planFreezeNo", className: 'center', bSortable: true},
            {data: "corpName", className: 'center', bSortable: false},
            {data: "bankAcc", className: 'center', bSortable: false},
            {data: "bankAccName", className: 'center', bSortable: false},
            {data: "bankName", className: 'center', bSortable: false},
            {
                data: "isOnline", className: 'center', bSortable: false,
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isOnline)
                }
            },
            {data: "freezeStartDate", className: 'center', bSortable: true},
            {data: "freezeEndDate", className: 'center', bSortable: true},
            {
                data: "applyFreezeMoney", className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.applyFreezeMoney);
                }
            },
            {data: "freezeRession", className: 'center', bSortable: false},
            {
                data: "realFreezeMoney", className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.money.format(full.realFreezeMoney);
                }
            },
            {data: "statusName", className: 'center', bSortable: false},
            {data: "freezeStatusName", className: 'center', bSortable: false},
            {
                data: "isFreezeComplete",
                className: 'center',
                bSortable: false,
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isFreezeComplete)
                }
            },
            {
                data: "isUnfreeze", className: 'center', bSortable: false, render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isUnfreeze)
                }
            },
            {data: "loginName", className: 'center', bSortable: false},
            {data: "createDate", className: 'center', bSortable: true},
        ],
        summary: {
            pageSum: true,//小计功能关闭
            selectSum: true,//已选汇总功能开启
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',
                    dataName: 'applyFreezeMoney'
                },
                {
                    colNum: '9',
                    dataName: 'realFreezeMoney'
                }
            ]
        },
    });
});

// 解冻
function manualPlanUnFreeze(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    var len = data.length;
    if (len < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        const data = rowData[i];
        if (data.status != 95 || data.isUnfreeze != 0 || (data.freezeStatus != 1 && data.freezeStatus != 2)) {
            layer.msg("计划冻结单号: " + data.planFreezeNo + " 不满足解冻条件! 请重新选择");
            return;
        }
        if (ids === "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm('确定要批量解冻吗？', function () {
        bt.loading.show();
        $.post(basePath + "/bankacc/btbankaccplanfreeze/batchManualPlanUnFreeze", {ids: ids}, function (result) {
            bt.loading.hide();
            if (result.success) {
                datatable && datatable.draw();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
    return false;
}

// 删除
function manualPlanDelete(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    var len = data.length;
    if (len < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        const data = rowData[i];
        if (data.status != 95 || data.freezeStatus != 0) {
            layer.msg("计划冻结单号: " + data.planFreezeNo + " 不满足删除条件! 请重新选择");
            return;
        }
        if (ids === "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm('确定要批量删除吗？', function () {
        bt.loading.show();
        $.post(basePath + "/bankacc/btbankaccplanfreeze/batchManualPlanDelete", {ids: ids}, function (result) {
            bt.loading.hide();
            if (result.success) {
                datatable && datatable.draw();
            }
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
    return false;
}
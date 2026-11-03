var datatable;
bt.use(['form', 'datatable', 'dateType', 'selectType', 'corp', 'moneyType', 'bank'], function () {
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
                hide: bt.isnotResource('netbank:expense:nisExpenseDetail:collectApply'),
                text: bt.lang('common.button.collect'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if (len < 1) {
                        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
                        return;
                    }
                    var ids = "";
                    var busType = "";
                    var corpId = "";
                    var rowData = data.splice(0, len);
                    for (var i = 0; i < rowData.length; i++) {
                        if (ids == "") {
                            ids += rowData[i].id;
                            busType = rowData[i].busType;
                            corpId = rowData[i].corpId;
                        } else {
                            ids += "," + rowData[i].id;
                            if (busType != rowData[i].busType || corpId != rowData[i].corpId) {
                                layer.alert(bt.lang('netbank.message.busTypeandcorpid.same'), {icon: 7});
                                return;
                            }
                        }
                    }
                    collect(ids);
                }
            }, {
                hide: bt.isnotResource('netbank:expense:nisExpenseDetail:collectApply'),
                text: bt.lang('common.button.batchBack'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if (len < 1) {
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
                    back(ids);
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
            {data: "expenseId", bSortable: true, className: 'center'},
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
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }
            }
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '4',
                    dataName: 'expenseMoney'
                }
            ]
        }
    });

    /**
     * 重置
     */
    resetSelect = function () {
        selectType.get("opType").attr("data-url", basePath + "/expense/nisbusinesstype/getOpType?busType=02");
    }
});

function collect(ids) {
    if (ids) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/expense/nisexpensedetail/collectTests");
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/expense/nisexpensedetail/collectTests",
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function back(ids) {
    parent.layer.confirm(bt.lang('common.message.confirm.batchBack'), function () {
        $.post(basePath + "/expense/nisexpensedetail/back", {ids: ids}, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
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

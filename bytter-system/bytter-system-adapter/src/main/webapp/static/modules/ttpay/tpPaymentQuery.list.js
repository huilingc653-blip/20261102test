var datatable;
var dateFun;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType', 'element'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    dateFun = bt.dateType.getFun();
    bt.dicts.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'asc'], [13, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/ttpay/tpPaymentQuery/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('ttpay:tpPaymentQuery:recall'),
                text: bt.lang('common.button.return'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    recallBatch(data);
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
            {
                data: "billNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.status == -1) {
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data: "busTitle", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "payerAcc", bSortable: true},
            {data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }},
            {data: "payeeAccName", bSortable: true,},
            {data: "payeeAcc", bSortable: true},
            {data: "wishPayday", bSortable: true, className: 'center'},
            {data: "payTypeName", bSortable: true, className: 'center'},
            {data: "isForindividualName", bSortable: true, className: 'center'},
            {data: "statusName", bSortable: true, className: 'center'},
            {data: "voucherStatName", bSortable: true, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center'},
            {data: "createUserName", bSortable: true, className: 'center'},
            {data: "busTypeName", bSortable: true, className: 'center'},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: true,
            groupColumn:'',
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',
                    dataName: 'payMoney'
                }
            ]
        },
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/ttpay/tpPaymentQuery/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    });
    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/ttpay/tpPaymentQuery/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/tpPaymentQuery/view/" + data.id
    });
}


//批量退回
function recallBatch(data) {
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
    parent.layer.confirm(bt.lang('ttpay.message.recall.yesOrNo'), function () {
        $.post(basePath + "/ttpay/tpPaymentQuery/batchRecall", {
            ids: ids
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}
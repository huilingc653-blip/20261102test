var datatable;
bt.use(['form', 'treeTable', 'printType','datatable'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.printType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [[0, 'asc']],
        searchForm: {
            formElem: "#inputForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/budget/fbsBudgetProcessReport/dataTablesDetailProcessInfo"
        },
        buttons: [
            {
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
            {data: "itemCode", bSortable: true},
            {data: "itemName", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {
                data: "bMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bMoney);
                }
            },
            {
                data: "bsuperMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bsuperMoney);
                }
            },
            {
                data: "btempMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.btempMoney);
                }
            },
            {
                data: "bMoneySum", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bMoneySum);
                }
            },
            {
                data: "inProcessMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    //return bt.money.format(full.inProcessMoney);
                    return '<a class="" href="#" data-row="' + meta.row + '" onclick="openProcess(this,95,1)">' + bt.money.format(full.inProcessMoney) + '</a>';
                }
            },
            {
                data: "inTempProcessMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    //return bt.money.format(full.inTempProcessMoney);
                    return '<a class="" href="#" data-row="' + meta.row + '" onclick="openProcess(this,0,1)">' + bt.money.format(full.inTempProcessMoney) + '</a>';
                }
            },
            {
                data: "outProcessMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    //return bt.money.format(full.outProcessMoney);
                    return '<a class="" href="#" data-row="' + meta.row + '" onclick="openProcess(this,95,0)">' + bt.money.format(full.outProcessMoney) + '</a>';
                }
            },
            {
                data: "outTempProcessMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    //return bt.money.format(full.outTempProcessMoney);
                    return '<a class="" href="#" data-row="' + meta.row + '" onclick="openProcess(this,0,0)">' + bt.money.format(full.outTempProcessMoney) + '</a>';
                }
            },
            {
                data: "canBMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.canBMoney);
                }
            }
        ]
    });
});

function openProcess(el, status, innerSign) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var statusFlag = "95" == status;
    var innerSignFlag = "1" == innerSign;
    var curId = $("#curId").val();
    var url = basePath + "/budget/fbsBudgetProcessReport/openProcessInfo";
    url += "?itemCode=" + data.itemCode + "&corpId=" + data.corpId;
    url += "&statusFlag=" + statusFlag + "&innerSignFlag=" + innerSignFlag;
    url += "&beginDate=" + data.beginDate + "&endDate=" + data.endDate +"&curId="+data.curId;
    url += "&payeeAccName=" + data.payeeAccName;
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), url);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: url
    });
}

bt.use(['form', 'datatable', 'moneyType'], function () {
    $ = bt.jquery, form = bt.form();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        checkbox: false,
        order: [[4, 'desc']],
        ajax: {
            'url': basePath + "/budget/fbsBudgetProcessReport/dataTablesProcessInfo"
        },
        columns: [
            {data: "corpName", bSortable: true},
            {data: "itemCode", bSortable: true},
            {data: "itemName", bSortable: true},
            {
                data: "businessName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fbsBudgetProcess.businessName", full.businessName);
                }
            },
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "processDate", bSortable: true, className: 'center'},
            {
                data: "processMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.processMoney);
                }
            },
            {data: "curName", bSortable: true, className: 'center'},
            {
                data: "payeeAccName", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.payeeAccName;
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.status == 95) {
                        return bt.lang('table.fbsBudget.process.execute');
                    } else {
                        return bt.lang('table.fbsBudgetProcessReport.bsuperMoney.temp');
                    }
                }
            }
        ]
    });
});
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
        checkbox: true,
        order: [[5, 'desc']],
        ajax: {
            'url': basePath + "/budget/fbsBudgetProcessReport/dataTablesSuperaddInfo"
        },
        columns: [
            {data: "corpName", bSortable: true},
            {data: "itemCode", bSortable: true},
            {data: "itemName", bSortable: true},
            {
                data: "bsuperMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bsuperMoney);
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
var datatable;
bt.use(['form', 'datatable', 'dateType', 'selectType', 'moneyType', 'dicts', 'bank', 'corp'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/report/allocation/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "corpName", bSortable: true},
            {data: "voucherFrom", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("nisAllocationReport.voucherFrom", full.voucherFrom);
                }
            },
            {data: "reqDate", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data: "purpose", bSortable: true},
            {data: "payerAcc", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "payerBank", bSortable: true},
            {data: "payeeAcc", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBank", bSortable: true}
        ],
        summary: {
            pageSum: false,
            allSum:
                true,
            selectSum:
                true,
            format:
                {
                    decimal: 2
                }
            ,
            sumList: [
                {
                    colNum: '5',
                    dataName: 'payMoney'
                }
            ]
        }
    });
});
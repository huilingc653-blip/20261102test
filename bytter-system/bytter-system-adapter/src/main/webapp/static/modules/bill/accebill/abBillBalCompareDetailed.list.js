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
        checkbox: false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/balCompare/detailedDatatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "origNo", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "bankType", bSortable: true},
            {data: "billTypeName", bSortable: true},
            {data: "billFlagName", bSortable: true},
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "payer", bSortable: true},
            {data: "payee", bSortable: true},
            {data: "acceptor", bSortable: true}
        ],
        summary: {
            pageSum: true,
            allSum:false,
            selectSum:false,
            sumList: [
                {
                    colNum: '8',
                    dataName: 'billMoney',
                    money:true//货币类型，需要千分格式化
                }
            ]
        }
    });
});
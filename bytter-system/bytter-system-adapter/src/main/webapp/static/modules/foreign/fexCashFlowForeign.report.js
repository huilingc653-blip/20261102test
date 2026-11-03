var datatable;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'selectType', 'element','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.dateType.init();
    bt.selectType.init();
    bt.mulselect.init();
    var dateFun = bt.dateType.getFun();
    bt.dicts.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fexcashflow/dataTableForeignReport"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "corpName", bSortable: true,className: 'center'},
            {data: "proName", bSortable: true,className: 'center'},
            {data: "tranBillNo", bSortable: true, className: 'center'},
            {data: "tranGroupBillNo", bSortable: true, className: 'center'},
            {data: "cotpName", bSortable: true,className: 'center'},
            {
                data: "cashFlowType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexCorp.cashFlowType", full.cashFlowType);
                }
            },
            {data: "englishCode", bSortable: true, className: 'center'},
            {data: "tranDate", bSortable: true, className: 'center'},
            {data: "settleDate", bSortable: true, className: 'center'},
            {data: "corpBankName", bSortable: true,className: 'center'},
            {data: "corpBankAcc", bSortable: true,className: 'center'},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {
                data: "contractRate", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return moneyFun.formatConvert(full.contractRate);
                }
            }
        ]
    });
});
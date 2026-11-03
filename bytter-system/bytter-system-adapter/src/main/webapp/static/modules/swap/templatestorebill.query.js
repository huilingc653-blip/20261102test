var form,configType,datatable;
bt.use(['form','element','datatable','selectType','corp','dicts','moneyType','dateType'], function(){
    form = bt.form(),$  = bt.jquery,element = bt.element();
    configType = $("input[name='dealType']").val();
    var status = 0;
    bt.dicts.init();
    bt.moneyType.init();
    bt.selectType.init();
    bt.corp.init();
    dateType=bt.dateType.init();
    loaddatatable();
    function loaddatatable() {
        datatable = bt.datatable.init({
            elem: "#appTable",
            colReorder: true,
            checkbox: true,
            order: [[1, 'desc']],
            paging: true,
            fixedColumns: {
                leftColumns:1,
                rightColumns:2
            },
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath + "/erp/swap/templatestore/bill/datatable/query"
            },
            buttons: [
                {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ]
            , columns: [
                {data: "code", bSortable: true},
                {
                    data: "status", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("templatestore.status", full.status);
                    }
                }
                ,{data: "billType", bSortable: true,render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_TYPE",full.billType);
                }},{data: "billFlag", bSortable: true,render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_FLAG",full.billFlag);
                }},
                {data: "tradeType", bSortable: true,render: function (data, type, full, meta) {
                    return bt.dict("bill_operation_type",full.tradeType);
                }},
                {data: "corpName", bSortable: false},
                {data: "payer", bSortable: true},
                {data: "acceptor", bSortable: true},
                {data: "money", bSortable: true, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.money);
                    }
                },
                {data: "businessDate", bSortable: true},
                {data: "transTime", bSortable: true},
                {data: "maturityDate", bSortable: true},
                {data: "receiver", bSortable: true},
                {data: "sendBackNo", bSortable: true},
                {data: "sendReason", bSortable: false},
                {data: "opName", bSortable: false},
                {data: "templateName", bSortable: false},
                {data: "typeName", bSortable: false}
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
                        colNum: '9',
                        dataName: 'money'
                    }
                ]
            }
        });
    }
});
bt.use(['form', 'datatable', 'btdate', 'corp', 'selectType', 'moneyType','dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.dicts.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        //dom: 'Brtlip',
        checkbox:true,
        order: [[1, 'asc'],[5, 'asc'],[6, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcaccdetailquery/datatable"
        },
        buttonsLayout: true,
        columns: [
            {data: "account", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "interestStartDate", bSortable: true},
            {data: "tallyDate", bSortable: true},
            {data: "voucherNo", bSortable: true},
            {data: "summaryContent", bSortable: true},
            {
                data: "debitMoney", bSortable: true, className: "textRight",
                render: function (data, type, row, meta) {
                    return moneyFun.format(data);
                }
            },
            {
                data: "creditMoney", bSortable: true, className: "textRight",
                render: function (data, type, row, meta) {
                    return moneyFun.format(data);
                }
            },
            {data: "voucherName", bSortable: true},
            {
                data: "balance", bSortable: true, className: "textRight",
                render: function (data, type, row, meta) {
                    return moneyFun.format(data);
                }
            },
            {data: "externalName", bSortable: true},
            {data: "externalAcc", bSortable: true},
            {data: "origVoucherNo", bSortable: true},
        ],
        summary: {
            pageSum: true,
            // allSum: true,
            selectSum: true,
            groupColumn:"curName",
            sumList: [
                {
                    colNum: '9',
                    dataName: 'debitMoney',
                    money:true
                },
                {
                    colNum: '10',
                    dataName: 'creditMoney',
                    money:true
                },
            ]
        },
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/inneraccount/fcaccdetailquery/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    });

    //时间
    initDate = function () {
        $.each($("div[type='date']"), function (i, item) {
            bt.btdate.init({
                elem: ".tallyDate",
                format: "YYYY-MM-DD",
                istime: false,
                startname: 'ged_tallyDate',
                endname: 'led_tallyDate'
            });
        });
    }
    initDate();

});

//重置按钮回调
resetDate = function () {
    initDate();
}
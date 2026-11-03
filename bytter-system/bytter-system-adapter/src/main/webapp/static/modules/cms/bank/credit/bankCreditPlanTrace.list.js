bt.use(['form', 'datatable', 'selectType', 'dateType', 'dicts', 'mulselect', 'rateType', 'moneyType'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dicts.init();
    bt.dateType.init(); //日期
    bt.selectType.init();
    bt.mulselect.init();
    bt.rateType.init();//利率
    bt.moneyType.init();//金额
    fun = bt.moneyType.getFun()
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        //order: [4, 'desc'],
        ajax: {
            'url': basePath + "/cms/bank/credit/plan/trace/datatable"
        },
        searchForm:{
            formFilter:"search",
            formElem:"#searchForm"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        // fixedColumns: {
        //     rightColumns: 1//代表冻结右边一列
        // },
        columns: [
            { data: "areaName", bSortable: false, className: 'center' },
            { data: "corpName", bSortable: false, className: 'center' },

            /*================= 年度融资规划（万元） =================*/
            {
                data: "yearPurchase", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.yearPurchase;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },
            {
                data: "mergeAcquisitionInvest", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.mergeAcquisitionInvest;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },
            {
                data: "creditRequire", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.creditRequire;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },

            /*================= 计划授信（万元） =================*/
            {
                data: "cooperationType", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("rat.cooperation.type", data);
                }
            },
            { data: "planCreditBankName", bSortable: false, className: 'center' },
            {
                data: "planCreditLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.planCreditLimit;
                    return money == null ? "-" : bt.money .format(money / 10000);
                }
            },
            {
                data: "creditChange", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.creditChange;
                    return money == null ? "-" : bt.money .format(money / 10000);
                }
            },

            /*================= 授信情况 =================*/
            {
                data: "bankLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.bankLimit;
                    return money == null ? "-" : bt.money .format(money / 10000);
                }
            },

            { data: "creditBankName", bSortable: false, className: 'center' },

            { data: "contractStartEndDate", bSortable: false, className: 'center' },

            {
                data: "comprehensiveLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.comprehensiveLimit;
                    return money == null ? "-" : bt.money .format(money / 10000);
                }
            },
            {
                data: "flowLoanLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.flowLoanLimit;
                    return money == null ? "-" : bt.money .format(money / 10000);
                }
            },
            {
                data: "bankAcceptanceBillLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.bankAcceptanceBillLimit;
                    return money == null ? "-" : bt.money .format(money / 10000);
                }
            },
            { data: "otherLimit", bSortable: false, className: 'center' },
            { data: "allocatedSubsidiaryLimit", bSortable: false, className: 'center' },

            {
                data: "marginRatio", sortable: false, className: "textRight",
                render: function (data, type, row, meta) {
                    const rate = row.marginRatio;
                    return rate == null ? "-" : fun.format(rate * 100, {decimal: 2}) + "%";
                }
            },
            {
                data: "invoiceFee", sortable: false, className: "textRight",
                render: function (data, type, row, meta) {
                    const rate = row.invoiceFee;
                    return rate == null ? "-" : fun.format(rate * 100, {decimal: 2}) + "%";
                }
            },
            {
                data: "marginRate", sortable: false, className: "textRight",
                render: function (data, type, row, meta) {
                    const rate = row.marginRate;
                    return rate == null ? "-" : fun.format(rate * 100, {decimal: 2}) + "%";
                }
            },
            {
                data: "isUsed", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", data);
                }
            },

            /*================= 额度使用情况（万元） =================*/
            {
                data: "currentPeriodUsedLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.currentPeriodUsedLimit;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },
            {
                data: "currentPeriodAvailableLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.currentPeriodAvailableLimit;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },
            {
                data: "currentPeriodLimitGap", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.currentPeriodLimitGap;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },

            {
                data: "lastPeriodUsedLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.lastPeriodUsedLimit;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },

            {
                data: "currentYearUsedLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.currentYearUsedLimit;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },
            {
                data: "currentYearAvailableLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.currentYearAvailableLimit;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },
            {
                data: "currentYearLimitGap", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.currentYearLimitGap;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },
            {
                data: "lastYearUsedLimit", sortable: false, className: "textRight",
                render: function(data, type, full, meta) {
                    const money = full.lastYearUsedLimit;
                    return money == null ? "" : bt.money .format(money / 10000);
                }
            },


            {
                data: "creditCompleteRate", sortable: false, className: "textRight",
                render: function (data, type, row, meta) {
                    const rate = row.creditCompleteRate;
                    return rate == null ? "-" : fun.format(rate * 100, {decimal: 2}) + "%";
                }
            },

            { data: "existDifficulty", bSortable: false, className: 'center' },

            { data: "solveMeasure", bSortable: false, className: 'center' },

        ]
    });
});
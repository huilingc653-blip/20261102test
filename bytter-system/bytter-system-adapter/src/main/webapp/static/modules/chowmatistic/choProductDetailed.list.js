bt.use(['form', 'selectType', 'dicts', 'rateType', 'corp', 'dateType','mulselect', 'datatable'], function () {
    var selectType = bt.selectType.init();
    var dict = bt.dicts.init();
    var corp = bt.corp.init();
    var mulselect = bt.mulselect.init();
    var form = bt.form();
    bt.dateType.init();
    var rateType = bt.rateType.getFun();
    var columns = [
        {data: "corpName", "width": "130px", className: 'center'},
        {data: "bankTypeName", "width": "130px", className: 'center'},
        {data: "bankName", "width": "130px", className: 'center'},
        {
            data: "productType", "width": "130px", className: 'center', render: function (data, type, full, meta) {
                return bt.dict("cho.productType", full.productType);
            }
        },
        {data: "productCode", "width": "130px", className: 'center'},
        {data: "productName", "width": "130px", className: 'center'},
        {data: "bankAcc", "width": "130px", className: 'center'},
        {
            data: "buyMoney", "width": "130px", className: 'textRight', render: function (data, type, full, meta) {
                return bt.money.format(full.buyMoney);
            }
        },
        {data: "curName", "width": "130px", className: 'center'},
        {
            data: "incomeClause", "width": "130px", className: 'center', render: function (data, type, full, meta) {
                return bt.dict("choProduct.incomeClause", full.incomeClause);
            }
        },
        {
            data: "referIncomeRate",
            "width": "130px",
            render: function (data, type, full, meta) {
                return bt.rateType.getFun().format(full.referIncomeRate,undefined,undefined,undefined,'');
            }
        },
        {
            data: "referIncomeMoney",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.referIncomeMoney);
            }
        },
        {data: "dayLimit", "width": "130px", className: 'center'},
        {data: "buyDate", "width": "130px", className: 'center'},
        {data: "interestStartDate", "width": "130px", className: 'center'},
        {data: "deadlineDate", "width": "130px", className: 'center'},
        {
            data: "redeemedMoney", "width": "130px", className: 'textRight', render: function (data, type, full, meta) {
                return bt.money.format(full.redeemedMoney);
            }
        },
        {data: "redeemedDate", "width": "130px", className: 'center'},
        {data: "redeemedDay", "width": "130px", className: 'center'},
        {
            data: "redeemedWeight",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.redeemedWeight);
            }
        },
        {
            data: "redeemedReferIncomeMoney",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.redeemedReferIncomeMoney);
            }
        },
        {
            data: "redeemedIncomeMoney",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.redeemedIncomeMoney);
            }
        },
        {
            data: "redeemedWeightAverageInvest",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.redeemedWeightAverageInvest);
            }
        },
        {
            data: "redeemedAnnualizedReturn",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.rateType.getFun().format(full.redeemedAnnualizedReturn);
            }
        },
        {
            data: "redeemedsMoney",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.redeemedsMoney);
            }
        },
        {
            data: "waitRedeemMoney",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.waitRedeemMoney);
            }
        },
        {data: "lastRedeemedDate", "width": "130px", className: 'center'},
        {
            data: "redeemedsIncomeMoney",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.redeemedsIncomeMoney);
            }
        },
        {
            data: "redeemedsWeightAverageInvest",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.redeemedsWeightAverageInvest);
            }
        },
        {
            data: "redeemedsAnnualizedReturn",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.rateType.getFun().format(full.redeemedsAnnualizedReturn);
            }
        },
        {data: "currentStartDate", "width": "130px", className: 'center'},
        {data: "currentEndDate", "width": "130px", className: 'center'},
        {data: "currentDay", "width": "130px", className: 'center'},
        {
            data: "currentWeight", "width": "130px", className: 'textRight', render: function (data, type, full, meta) {
                return bt.money.format(full.currentWeight);
            }
        },
        {
            data: "currentIncomeMoney",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.currentIncomeMoney);
            }
        },
        {
            data: "currentRedeemedWeightAverageInvest",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.currentRedeemedWeightAverageInvest);
            }
        },
        {
            data: "currentRedeensAnnualizedReturn",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.rateType.getFun().format(full.currentRedeensAnnualizedReturn);
            }
        },
        {data: "currentWaitRedeemDay", "width": "130px", className: 'center'},
        {
            data: "currentWaitRedeemWeight",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.currentWaitRedeemWeight);
            }
        },
        {
            data: "currentWaitRedeemIncomeMoney",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.currentWaitRedeemIncomeMoney);
            }
        },
        {
            data: "currentWaitRedeemWeightAverageInvest",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.currentWaitRedeemWeightAverageInvest);
            }
        },
        {
            data: "currentWaitRedeemAnnualizedReturn",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.rateType.getFun().format(full.currentWaitRedeemAnnualizedReturn);
            }
        },
        {
            data: "currentReferIncomeRate",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.currentReferIncomeRate);
            }
        },
        {
            data: "currentWeightAverageInvest",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.currentWeightAverageInvest);
            }
        },
        {
            data: "currentAnnualizedReturn",
            "width": "130px",
            className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.rateType.getFun().format(full.currentAnnualizedReturn);
            }
        }
    ];

    var htmlx =
         '<tr>'+
            '<th colspan="16">'+bt.lang("cho.productDetailed.baseInfo")+'</th>'+
            '<th colspan="14">'+bt.lang("cho.productDetailed.incomeInfo")+'</th>'+
            '<th colspan="2">'+bt.lang("cho.productDetailed.currentDefine")+'</th>'+
            '<th colspan="5">'+bt.lang("cho.productDetailed.currentRedeemedIncomeInfo")+'</th>'+
            '<th colspan="5">'+bt.lang("cho.productDetailed.currentWaitRedeemIncomeInfo")+'</th>'+
            '<th colspan="3">'+bt.lang("cho.productDetailed.currentProductIncomeInfo")+'</th>'+
        '</tr>'+
        '<tr>'+
            '<th>'+bt.lang("cho.productDetailed.corpName")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.bankTypeName")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.bankName")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.productType")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.productCode")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.productName")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.bankAcc")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.buyMoney")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.curName")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.incomeClause")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.referIncomeRate")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.referIncomeMoney")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.dayLimit")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.buyDate")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.interestStartDate")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.deadlineDate")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedMoney")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedDate")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedDay")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedWeight")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedReferIncomeMoney")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedIncomeMoney")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedWeightAverageInvest")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedAnnualizedReturn")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedsMoney")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.waitRedeemMoney")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.lastRedeemedDate")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedsIncomeMoney")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedsWeightAverageInvest")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.redeemedsAnnualizedReturn")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentStartDate")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentEndDate")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentDay")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentWeight")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentIncomeMoney")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentRedeemedWeightAverageInvest")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentRedeensAnnualizedReturn")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentWaitRedeemDay")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentWaitRedeemWeight")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentWaitRedeemIncomeMoney")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentWaitRedeemWeightAverageInvest")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentWaitRedeemAnnualizedReturn")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentReferIncomeRate")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentWeightAverageInvest")+'</th>'+
            '<th>'+bt.lang("cho.productDetailed.currentAnnualizedReturn")+'</th>'+
        '</tr>';
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        resize:true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/report/choProductDetailed/datatable"
        },
        exportFile: {
            customPrint: {
                header: function () {
                    var printHtml = htmlx;
                    return printHtml;
                },
            },
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: columns,
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: false,
            groupColumn:'curName',
            sumList: [
                {
                    colNum: '7',//汇总列序号
                    dataName: 'buyMoney',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '16',//汇总列序号
                    dataName: 'redeemedMoney',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '19',//汇总列序号
                    dataName: 'redeemedWeight',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '20',//汇总列序号
                    dataName: 'redeemedReferIncomeMoney',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '21',//汇总列序号
                    dataName: 'redeemedIncomeMoney',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '22',//汇总列序号
                    dataName: 'redeemedWeightAverageInvest',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '24',//汇总列序号
                    dataName: 'redeemedsMoney',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '25',//汇总列序号
                    dataName: 'waitRedeemMoney',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '27',//汇总列序号
                    dataName: 'redeemedsIncomeMoney',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '28',//汇总列序号
                    dataName: 'redeemedsWeightAverageInvest',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '33',//汇总列序号
                    dataName: 'currentWeight',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '34',//汇总列序号
                    dataName: 'currentIncomeMoney',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '35',//汇总列序号
                    dataName: 'currentRedeemedWeightAverageInvest',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '38',//汇总列序号
                    dataName: 'currentWaitRedeemWeight',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '39',//汇总列序号
                    dataName: 'currentWaitRedeemIncomeMoney',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '40',//汇总列序号
                    dataName: 'currentWaitRedeemWeightAverageInvest',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '42',//汇总列序号
                    dataName: 'currentReferIncomeRate',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
                {
                    colNum: '43',//汇总列序号
                    dataName: 'currentWeightAverageInvest',//汇总列数据
                    money: true,//非货币类型，不需要千分格式化

                },
            ]
        }
    });
});

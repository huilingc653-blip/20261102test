var corp;
var selectType;
bt.use(['form', 'datatable', 'dateType', 'selectType', 'dicts', 'moneyType', 'rateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.rateType.init();
    selectType = bt.selectType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = moneyType.getFun();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        isInitData: false,
        //buttonsLayout: false,
        //customDom: "rtlip",
        ignoreButtons: ['colvis', 'print', 'state'],// 忽略的表单默认按钮 state取消状态保存按钮
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            callback: function (data) {
                //点击查询按钮的回调方法,返回data对象，包含：elem:当前按钮jq对象；field:查询表单内容；form:表单jq对象
                // bt.loading.show();
            }
        },
        ajax: {
            'url': basePath + "/bi/cms/report/financingRiskExposureBalance/datatable"
        },
        exportFile: {
            extendExcelSingleRowInfoCallback: function () {
                var queryDate = $("#queryDate").val();
                var unitShowInfo = bt.lang('table.nisAllocationReport.curType');
                var curIdElement = selectType.get("curId");
                var curName = '';
                if(curIdElement){
                    var selectedCur = selectType.get("curId").getData();
                    if(selectedCur){
                        curName = selectedCur.text;
                    }
                }
                return [bt.lang('cms.report.date') + '：' + queryDate, '', '', '', '', '', '', '', bt.lang('table.nisBillhead.curCode') + '：' +curName, unitShowInfo];
            },
        },
        columns: [
            {data: "corpName", bSortable: false, className: "center"},
            {
                data: "flowLoanRiskExpBal", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                data: "billRiskExpBal", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                data: "bondRiskExpBal", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                data: "riskExpSum", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                data: "netInterestExpenseSum", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                // 环比增幅（数值型）
                data: "chainIncrease", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                // 较去年底增幅（数值型）
                data: "increaseComparedLastYearEnd", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                // 环比增长比例（百分比）
                data: "chainIncreaseRatioComparedLastMonth", bSortable: false, className: "center",
                render: function (data, type, full, meta) {
                    if (data == null || data == undefined) {
                        return "---";
                    }
                    if (data == 0) {
                        return "0.00%";
                    }
                    return bt.rateType.getFun().format(data, 'percent', 2, ',');
                }
            },
            {
                // 较去年底增长比例（百分比）
                data: "increaseRatioComparedLastYearEnd", bSortable: false, className: "center",
                render: function (data, type, full, meta) {
                    if (data == null || data == undefined) {
                        return "---";
                    }
                    if (data == 0) {
                        return "0.00%";
                    }
                    return bt.rateType.getFun().format(data, 'percent', 2, ',');
                }
            },
        ],
        btDrawCallback: function (data) {
            // bt.loading.hide();
        },
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    money: true,
                    colNum: '1',
                    dataName: 'flowLoanRiskExpBal',
                },
                {
                    money: true,
                    colNum: '2',
                    dataName: 'billRiskExpBal'
                },
                {
                    money: true,
                    colNum: '3',
                    dataName: 'bondRiskExpBal'
                },
                {
                    money: true,
                    colNum: '4',
                    dataName: 'riskExpSum'
                },
                {
                    money: true,
                    colNum: '5',
                    dataName: 'netInterestExpenseSum'
                },
                {
                    money: true,
                    colNum: '6',
                    dataName: 'chainIncrease'
                },
                {
                    money: true,
                    colNum: '7',
                    dataName: 'increaseComparedLastYearEnd'
                },
                {
                    money: false,
                    colNum: '8',
                    dataName: 'chainIncreaseRatioComparedLastMonth', // 环比增长比例
                    expression: function (obj) {  // 自定义汇总内容 obj为各汇总列数据
                        // var selectedRows = datatable.rows('.selected').data();
                        var pageDataListObj = datatable.rows('').data();
                        var pageDataList = pageDataListObj.splice(0, pageDataListObj.length);
                        // 今年融资合计数（敞口）
                        var thisYearFinancingOpenMoneySum = getDataThisYearFinancingOpenMoneySum(pageDataList);
                        //  上个月 融资合计数（敞口）
                        var lastMonthDateFinancingOpenMoneySum = getDataLastMonthDateFinancingOpenMoneySum(pageDataList);
                        // 环比增长比例 = 环比增幅 / 去年同期总额
                        var chainIncrease = moneyFun.sub(thisYearFinancingOpenMoneySum, lastMonthDateFinancingOpenMoneySum);
                        // console.log("今年融资合计数（敞口） 合计：", thisYearFinancingOpenMoneySum);
                        // console.log("上个月 融资合计数（敞口） 合计：", lastYearSameDateFinancingOpenMoneySum);
                        if (lastMonthDateFinancingOpenMoneySum == 0) {
                            return "---";
                        }
                        if (chainIncrease == 0) {
                            return "0.00%";
                        }
                        var chainIncreaseRatio = moneyFun.div(chainIncrease, Math.abs(lastMonthDateFinancingOpenMoneySum),4);
                        return bt.rateType.getFun().format(chainIncreaseRatio, 'percent', 2, ',');
                    },
                    selectSumCallback: function (currentRowData, selectedData) {
                        if (selectedData && selectedData.length == 0) {
                            return "";
                        }
                        // 今年融资合计数
                        var thisYearFinancingOpenMoneySum = getDataThisYearFinancingOpenMoneySum(selectedData);
                        // 上个月 融资合计数
                        var lastMonthDateFinancingOpenMoneySum = getDataLastMonthDateFinancingOpenMoneySum(selectedData);
                        // 环比增长比例 = 环比增幅 / 去年同期总额
                        var chainIncrease = moneyFun.sub(thisYearFinancingOpenMoneySum, lastMonthDateFinancingOpenMoneySum);
                        if (lastMonthDateFinancingOpenMoneySum == 0) {
                            return "---";
                        }
                        if (chainIncrease == 0) {
                            return "0.00%";
                        }
                        var chainIncreaseRatio = moneyFun.div(chainIncrease, Math.abs(lastMonthDateFinancingOpenMoneySum),4);
                        return bt.rateType.getFun().format(chainIncreaseRatio, 'percent', 2, ',');
                    }
                },
                {
                    money: false,
                    colNum: '9',
                    dataName: 'increaseRatioComparedLastYearEnd', // 较去年底增长比例
                    expression: function (obj) {  // 自定义汇总内容 obj为各汇总列数据
                        // var selectedRows = datatable.rows('.selected').data();
                        var pageDataListObj = datatable.rows('').data();
                        var pageDataList = pageDataListObj.splice(0, pageDataListObj.length);
                        // 今年融资合计数（敞口）
                        var thisYearFinancingOpenMoneySum = getDataThisYearFinancingOpenMoneySum(pageDataList);
                        // 去年年底 融资合计数（敞口）
                        var lastYearEndFinancingOpenMoneySum = getDataLastYearEndFinancingOpenMoneySum(pageDataList);
                        // 较去年底增长比例 = 较去年底增幅 / 去年年底总额
                        var chainIncrease = moneyFun.sub(thisYearFinancingOpenMoneySum, lastYearEndFinancingOpenMoneySum);
                        // console.log("今年融资合计数（敞口） 合计：", thisYearFinancingOpenMoneySum);
                        // console.log("去年年底 融资合计数（敞口） 合计：", lastYearEndFinancingOpenMoneySum);
                        if (lastYearEndFinancingOpenMoneySum == 0) {
                            return "---";
                        }
                        var chainIncreaseRatio = moneyFun.div(chainIncrease, Math.abs(lastYearEndFinancingOpenMoneySum),4);
                        return bt.rateType.getFun().format(chainIncreaseRatio, 'percent', 2, ',');
                    },
                    selectSumCallback: function (currentRowData, selectedData) {
                        if (selectedData && selectedData.length == 0) {
                            return "";
                        }
                        // 今年融资合计数（敞口）
                        var thisYearFinancingOpenMoneySum = getDataThisYearFinancingOpenMoneySum(selectedData);
                        // 去年年底 融资合计数（敞口）
                        var lastYearEndFinancingOpenMoneySum = getDataLastYearEndFinancingOpenMoneySum(selectedData);
                        //  较去年底增长比例 = 较去年底增幅 / 去年年底总额
                        var chainIncrease = moneyFun.sub(thisYearFinancingOpenMoneySum, lastYearEndFinancingOpenMoneySum);
                        if (lastYearEndFinancingOpenMoneySum == 0) {
                            return "---";
                        }
                        var chainIncreaseRatio = moneyFun.div(chainIncrease, Math.abs(lastYearEndFinancingOpenMoneySum),4);
                        return bt.rateType.getFun().format(chainIncreaseRatio, 'percent', 2, ',');
                    }
                },
            ]
        }
    });

    /**
     * 获取 今年 融资合计数（敞口）
     * @param dataList
     */
    function getDataThisYearFinancingOpenMoneySum(dataList) {
        var thisYearFinancingOpenMoneySum = 0;
        if (dataList) {
            dataList.forEach(function (item, index, arr) {
                thisYearFinancingOpenMoneySum = moneyFun.add(thisYearFinancingOpenMoneySum, item.thisYearFinancingOpenMoneySum);
            });
        }
        return thisYearFinancingOpenMoneySum;
    }

    /**
     * 获取  上个月 融资合计数（敞口）
     * @param dataList
     */
    function getDataLastMonthDateFinancingOpenMoneySum(dataList) {
        var lastMonthDateFinancingOpenMoneySum = 0;
        if (dataList) {
            dataList.forEach(function (item, index, arr) {
                lastMonthDateFinancingOpenMoneySum = moneyFun.add(lastMonthDateFinancingOpenMoneySum, item.lastMonthDateFinancingOpenMoneySum);
            });
        }
        return lastMonthDateFinancingOpenMoneySum;
    }

    /**
     * 获取 去年年底 融资合计数（敞口）
     * @param dataList
     */
    function getDataLastYearEndFinancingOpenMoneySum(dataList) {
        var lastYearEndFinancingOpenMoneySum = 0;
        if (dataList) {
            dataList.forEach(function (item, index, arr) {
                lastYearEndFinancingOpenMoneySum = moneyFun.add(lastYearEndFinancingOpenMoneySum, item.lastYearEndFinancingOpenMoneySum);
            });
        }
        return lastYearEndFinancingOpenMoneySum;
    }


});

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
        ignoreButtons: ['colvis', 'print', 'state'],// 忽略的表单默认按钮 state取消状态保存按钮
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bi/cms/financingRiskBalance/datatable"
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
            {data: "corpName", bSortable: false, className: "center",width: 60},
            {
                data: "flowLoanRiskBal", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                data: "billRiskBal", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                data: "bondRiskBal", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                data: "financingSum", bSortable: false, className: "textRight",
                render: function (data, type, full, meta) {
                    return moneyFun.format(data, {separator: ",", decimal: 2});
                }
            },
            {
                data: "netInterestSum", bSortable: false, className: "textRight",
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
        fixedColumns:{
            leftColumns:1
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
                    dataName: 'flowLoanRiskBal',
                },
                {
                    money: true,
                    colNum: '2',
                    dataName: 'billRiskBal'
                },
                {
                    money: true,
                    colNum: '3',
                    dataName: 'bondRiskBal'
                },
                {
                    money: true,
                    colNum: '4',
                    dataName: 'financingSum'
                },
                {
                    money: true,
                    colNum: '5',
                    dataName: 'netInterestSum'
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
                        // 融资合计数
                        var thisYearFinancingMoneySum = getDataThisYearFinancingMoneySum(pageDataList);
                        // 上个月同期融资合计数
                        var lastMonthSameDateFinancingMoneySum = getDataLastMonthSameDateFinancingMoneySum(pageDataList);
                        // 环比增长比例 = 环比增幅 / 去年同期总额
                        var chainIncrease = moneyFun.sub(thisYearFinancingMoneySum, lastMonthSameDateFinancingMoneySum);
                        // console.log("今年融资合计数 合计：", thisYearFinancingMoneySum);
                        // console.log("去年同期融资合计数 合计：", lastMonthSameDateFinancingMoneySum);
                        if (lastMonthSameDateFinancingMoneySum == 0) {
                            return "---";
                        }
                        if (chainIncrease == 0) {
                            return "0.00%";
                        }
                        var chainIncreaseRatio = moneyFun.div(chainIncrease, lastMonthSameDateFinancingMoneySum,4);
                        return bt.rateType.getFun().format(chainIncreaseRatio, 'percent', 2, ',');
                    },
                    selectSumCallback:function(currentRowData,selectedData){
                        if (selectedData && selectedData.length == 0) {
                            return "";
                        }
                        // 融资合计数
                        var thisYearFinancingMoneySum = getDataThisYearFinancingMoneySum(selectedData);
                        // 上月同期融资合计数
                        var lastMonthSameDateFinancingMoneySum = getDataLastMonthSameDateFinancingMoneySum(selectedData);
                        // 环比增长比例 = 环比增幅 / 去年同期总额
                        var chainIncrease = moneyFun.sub(thisYearFinancingMoneySum, lastMonthSameDateFinancingMoneySum);
                        if (lastMonthSameDateFinancingMoneySum == 0) {
                            return "---";
                        }
                        if (chainIncrease == 0) {
                            return "0.00%";
                        }
                        var chainIncreaseRatio = moneyFun.div(chainIncrease, lastMonthSameDateFinancingMoneySum,4);
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
                        // 今年融资合计数
                        var thisYearFinancingMoneySum = getDataThisYearFinancingMoneySum(pageDataList);
                        // 去年年底 融资合计数
                        var lastYearEndFinancingMoneySum = getDataLastYearEndFinancingMoneySum(pageDataList);
                        //  较去年底增长比例 = 较去年底增幅 / 去年年底总额
                        var chainIncrease = moneyFun.sub(thisYearFinancingMoneySum, lastYearEndFinancingMoneySum);
                        // console.log("今年融资合计数 合计：", thisYearFinancingMoneySum);
                        // console.log("去年年底 融资合计数 合计：", lastYearEndFinancingMoneySum);
                        if (lastYearEndFinancingMoneySum == 0) {
                            return "---";
                        }
                        var chainIncreaseRatio = moneyFun.div(chainIncrease, lastYearEndFinancingMoneySum,4);
                        return bt.rateType.getFun().format(chainIncreaseRatio, 'percent', 2, ',');
                    },
                    selectSumCallback:function(currentRowData,selectedData){
                        if (selectedData && selectedData.length == 0) {
                            return "";
                        }
                        // 今年融资合计数
                        var thisYearFinancingMoneySum = getDataThisYearFinancingMoneySum(selectedData);
                        // 去年年底 融资合计数
                        var lastYearEndFinancingMoneySum = getDataLastYearEndFinancingMoneySum(selectedData);
                        //  较去年底增长比例 = 较去年底增幅 / 去年年底总额
                        var chainIncrease = moneyFun.sub(thisYearFinancingMoneySum, lastYearEndFinancingMoneySum);
                        if (lastYearEndFinancingMoneySum == 0) {
                            return "---";
                        }
                        var chainIncreaseRatio = moneyFun.div(chainIncrease, lastYearEndFinancingMoneySum,4);
                        return bt.rateType.getFun().format(chainIncreaseRatio, 'percent', 2, ',');
                    }
                },
            ]
        }
    });

    /**
     * 获取 今年 融资合计数
     * @param dataList
     */
    function getDataThisYearFinancingMoneySum(dataList) {
        var thisYearFinancingMoneySum = 0;
        if (dataList) {
            dataList.forEach(function (item, index, arr) {
                thisYearFinancingMoneySum = moneyFun.add(thisYearFinancingMoneySum, item.thisYearFinancingMoneySum);
            });
        }
        return thisYearFinancingMoneySum;
    }

    /**
     * 获取 去年同期融资合计数
     * @param dataList
     */
    function getDataLastMonthSameDateFinancingMoneySum(dataList) {
        var lastMonthSameDateFinancingMoneySum = 0;
        if (dataList) {
            dataList.forEach(function (item, index, arr) {
                lastMonthSameDateFinancingMoneySum = moneyFun.add(lastMonthSameDateFinancingMoneySum, item.lastMonthSameDateFinancingMoneySum);
            });
        }
        return lastMonthSameDateFinancingMoneySum;
    }

    /**
     * 获取 去年年底 融资合计数
     * @param dataList
     */
    function getDataLastYearEndFinancingMoneySum(dataList) {
        var lastYearEndFinancingMoneySum = 0;
        if (dataList) {
            dataList.forEach(function (item, index, arr) {
                lastYearEndFinancingMoneySum = moneyFun.add(lastYearEndFinancingMoneySum, item.lastYearEndFinancingMoneySum);
            });
        }
        return lastYearEndFinancingMoneySum;
    }

});

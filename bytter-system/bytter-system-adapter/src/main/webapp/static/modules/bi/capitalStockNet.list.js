var selectType,form,dateType;
bt.use(['form', 'datatable', 'money', 'dicts', 'selectType','date','corp','dateType'], function () {
    $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    dateType = bt.dateType.init();
    var reportType = $("#reportType").val();
    var corp = bt.corp.init();
    var openData;
    /**
     * 币别回调，控制本位币小计显示
     * @param data
     */
    var natualCurrency = [];
    curCallBack = function (data) {
        selectType.get("reducedCurId").val("");
        natualCurrency = [];
        if (data.curname != bt.lang('table.curName.rmb')) {
            //通过Ajax获取数据
            var url = basePath + "/basic/business/fcexcrateadjust/getExchangeRate";
            $.ajax({
                type: "post",
                url: url,
                data: {curId: data.id},
                success: function (result) {
                    natualCurrency.push({currency: bt.lang('table.capitalStock.moeny'), rate: result});
                }
            });
        }
    };
    selectType = bt.selectType.init();
    /**
     * 获取第一列网点/单位/银行
     * @returns {*}
     */
    getName = function () {
        if (reportType === "net") {
            return 'netName';
        } else if (reportType === "corp") {
            return 'corpName';
        } else if (reportType === "bankType") {
            return 'bankTypeName';
        } else {
            return '';
        }
    };
    //生成列表
    var datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: false,
        checkbox: false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bi/capitalStock/datatable"
        },
        buttons: [],
        rowsGroup: [0],
        columns: [
            {
                data: getName(),bSortable: false,
                render: function (data, type, full, meta) {
                    if (reportType === "net") {
                        return '<a href="#"  onclick="openBalCorp(\'' + full.netId + '\')">' + full.netName + '</a>';
                    } else if (reportType === "corp") {
                        return '<a  href="#" onclick="openBalBankAcc(\'' + full.corpId + '\')">' + full.corpName + '</a>';
                    } else if (reportType === "bankType") {
                        return full.bankTypeName;
                    } else {
                        return "";
                    }
                }
            },
            {
                data: "stockTypeName",bSortable: false,
                render: function (data, type, full, meta) {
                    if (reportType == "net") {
                        return '<a href="#"  onclick="openBalCorp(\'' + full.netId + '\',\'' + full.stockType + '\')">' + full.stockTypeName + '</a>';
                    } else if (reportType == "corp") {
                        return '<a  href="#" onclick="openBalBankAcc(\'' + full.corpId + '\',\'' + full.stockType + '\')">' + full.stockTypeName + '</a>';
                    } else if (reportType == "bankType") {
                        var bankTypeId = full.bankTypeId;
                        if (full.stockType == 1) {
                            return '<a  href="#" onclick="openBalDtl(\'' + bankTypeId + '\',\'' + full.stockType + '\')">' + full.stockTypeName + '</a>';
                        } else {
                            return '<a  href="#" onclick="openAbBillDtl(\'' + bankTypeId + '\',\'' + full.stockType + '\')">' + full.stockTypeName + '</a>';
                        }
                    } else {
                        return full.stockTypeName;
                    }
                }
            },
            {
                data: "beginBal", className: 'textRight',bSortable: false,
                render: function (data, type, full, meta) {
                    if (dicts.get("moneyUnit").val() === "million") {
                        return bt.money.format(bt.money.toMillion(full.beginBal));
                    } else {
                        return bt.money.format(full.beginBal);
                    }
                }
            },
            {
                data: "inAmt", className: 'textRight',bSortable: false,
                render: function (data, type, full, meta) {
                    if (dicts.get("moneyUnit").val() === "million") {
                        return bt.money.format(bt.money.toMillion(full.inAmt));
                    } else {
                        return bt.money.format(full.inAmt);
                    }
                }
            },
            {
                data: "outAmt", className: 'textRight',bSortable: false,
                render: function (data, type, full, meta) {
                    if (dicts.get("moneyUnit").val() === "million") {
                        return bt.money.format(bt.money.toMillion(full.outAmt));
                    } else {
                        return bt.money.format(full.outAmt);
                    }
                }
            },
            {
                data: "endBal", className: 'textRight',bSortable: false,
                render: function (data, type, full, meta) {
                    if (dicts.get("moneyUnit").val() === "million") {
                        return bt.money.format(bt.money.toMillion(full.endBal));
                    } else {
                        return bt.money.format(full.endBal);
                    }
                }
            },
            {
                data: "freezeMoney", className: 'textRight',bSortable: false,
                render: function (data, type, full, meta) {
                    var freezeMoney;
                    if (dicts.get("moneyUnit").val() === "million") {
                        freezeMoney = bt.money.format(bt.money.toMillion(full.freezeMoney));
                    } else {
                        freezeMoney = bt.money.format(full.freezeMoney);
                    }
                    if (reportType == "bankType") {
                        var bankTypeId = full.bankTypeId;
                        if (full.stockType == 1) {
                            return '<a  href="#" onclick="openBalFreeze(\'' + bankTypeId + '\',\'' + full.stockType + '\')">' + freezeMoney + '</a>';
                        } else {
                            return '<a  href="#" onclick="openAbBillFreeze(\'' + bankTypeId + '\',\'' + full.stockType + '\')">' + freezeMoney + '</a>';
                        }
                    } else {
                        return freezeMoney;
                    }
                }
            },
            {
                data: "availBal", className: 'textRight',bSortable: false,
                render: function (data, type, full, meta) {
                    if (dicts.get("moneyUnit").val() === "million") {
                        return bt.money.format(bt.money.toMillion(full.availBal));
                    } else {
                        return bt.money.format(full.availBal);
                    }
                }
            }
        ],
        btInitComplete: function (setting, json) {
            openData = {
                "reportType": reportType,
                "moneyUnit": dicts.get("moneyUnit").val() === " " ? "" : dicts.get("moneyUnit").val(),
                "stockType": dicts.get("stockType").val() === " " ? "" : dicts.get("stockType").val(),
                "curId": selectType.get("curId").val() === " " ? "" : selectType.get("curId").val(),
                "reducedCurId": selectType.get("reducedCurId").val() === " " ? "" : selectType.get("reducedCurId").val(),
                "dateType": $("#dateType").val(),
            };
            if (openData.dateType == undefined) {
                openData.dateType = dicts.get("dateType").val();
            }

            // 设置第一行
            var moneyUnitName = $('[data-biz-name="moneyUnit"]').find("option:selected").text();
            $("#moneyUnitText").text(moneyUnitName);
            var curName = openData.curId === "" ? "" : $('[data-biz-name="curId"]').find("option:selected").text();
            $("#curNameText").text(curName);
            if($("#reportType").val() == "net"){
                $("#dateText").text(dateType.get("beginDate").val() + "~" + dateType.get("endDate").val());
            }else {
                $("#dateText").text($("#beginDate").val()+"~"+$("#endDate").val());
            }

            if (reportType === "net") {
                // 加载可用资金柱状图
                // initBar();
            }
        },
        btDrawCallback: function (setting) {
            openData = {
                "reportType": reportType,
                "moneyUnit": dicts.get("moneyUnit").val() === " " ? "" : dicts.get("moneyUnit").val(),
                "stockType": dicts.get("stockType").val() === " " ? "" : dicts.get("stockType").val(),
                "curId": selectType.get("curId").val() === " " ? "" : selectType.get("curId").val(),
                "reducedCurId": selectType.get("reducedCurId").val() === " " ? "" : selectType.get("reducedCurId").val(),
                "dateType": $("#dateType").val(),
            };
            if (openData.dateType == undefined) {
                openData.dateType = dicts.get("dateType").val();
            }

            // 设置第一行
            var moneyUnitName = $('[data-biz-name="moneyUnit"]').find("option:selected").text();
            $("#moneyUnitText").text(moneyUnitName);
            var curName = openData.curId === "" ? "" : $('[data-biz-name="curId"]').find("option:selected").text();
            $("#curNameText").text(curName);
            if($("#reportType").val() == "net"){
                $("#dateText").text(dateType.get("beginDate").val() + "~" + dateType.get("endDate").val());
            }else {
                $("#dateText").text($("#beginDate").val()+"~"+$("#endDate").val());
            }
            if (reportType === "net") {
                // 加载可用资金柱状图
                // initBar();
            }
        },
        summary: {
            pageSum: true,
            groupColumn: 'stockTypeName',//分类汇总列参数
            convertMoney: function () {
                return "yuan" === dicts.get("moneyUnit").val() ? "" : dicts.get("moneyUnit").val();
            },
            natualCurrency: function () {
                if (natualCurrency.length != 0) {
                    return natualCurrency;
                } else {
                    if (reportType !== "net" && $("#rate").val() !== "") {
                        natualCurrency.push({currency: bt.lang('table.capitalStock.moeny'), rate: $("#rate").val()});
                        return natualCurrency;
                    }else {
                        return false;
                    }
                }
            },
            format:
                {
                    decimal: 2
                }
            ,
            sumList: [
                {
                    colNum: '2',
                    dataName: 'beginBal'
                },
                {
                    colNum: '3',
                    dataName: 'inAmt'
                },
                {
                    colNum: '4',
                    dataName: 'outAmt'
                },
                {
                    colNum: '5',
                    dataName: 'endBal'
                },
                {
                    colNum: '6',
                    dataName: 'freezeMoney'
                },
                {
                    colNum: '7',
                    dataName: 'availBal'
                }
            ]
        }
    });

    /**
     * 跳转现金单位查询列表
     * @param _this
     */
    openBalCorp = function (netId, stockType) {
        openData.netId = netId;
        openData.reducedCurId = selectType.get("reducedCurId").val();
        openData.beginDate = dateType.get("beginDate").val();
        openData.endDate = dateType.get("endDate").val();
        if (stockType != undefined) {
            openData.stockType = stockType;
        }
        var openUrl = basePath + "/bi/capitalStock/listCorp?" + parseParam(openData);
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.capitalStock.reportCorp'), openUrl);
            return;
        }
    }

    /**
     * 跳转现金银行查询列表
     * @param _this
     */
    openBalBankAcc = function (corpId, stockType) {
        openData.corpId = corpId;
        openData.reducedCurId = selectType.get("reducedCurId").val();
        openData.beginDate = $("#beginDate").val();
        openData.endDate = $("#endDate").val();
        if (stockType != undefined) {
            openData.stockType = stockType;
        }
        var openUrl = basePath + "/bi/capitalStock/listBankType?" + parseParam(openData);
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.capitalStock.reportBankAcc'), openUrl);
            return;
        }
    }
    /**
     * 跳转现金流水查询列表
     * @param _this
     */
    openBalDtl = function (bankTypeId, stockType) {
        openData.bankTypeId = bankTypeId;
        openData.corpId = $("#corpId").val();
        openData.reducedCurId = selectType.get("reducedCurId").val();
        openData.beginDate = $("#beginDate").val();
        openData.endDate = $("#endDate").val();
        if (stockType != undefined) {
            openData.stockType = stockType;
        }
        var openUrl = basePath + "/bi/capitalStock/listDtl?" + parseParam(openData);
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.capitalStock.reportDtl'), openUrl);
            return;
        }
    }
    /**
     * 跳转应收票据明细查询列表
     * @param _this
     */
    openAbBillDtl = function (bankTypeId, stockType) {
        openData.bankTypeId = bankTypeId;
        openData.corpId = $("#corpId").val();
        openData.reducedCurId = selectType.get("reducedCurId").val();
        openData.beginDate = $("#beginDate").val();
        openData.endDate = $("#endDate").val();
        if (stockType != undefined) {
            openData.stockType = stockType;
        }
        var openUrl = basePath + "/bi/capitalStock/listAbBillDtl?" + parseParam(openData);
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.capitalStock.reportAbBillDtl'), openUrl);
            return;
        }
    }
    /**
     * 跳转现金冻结查询列表
     * @param _this
     */
    openBalFreeze = function (bankTypeId, stockType) {
        openData.bankTypeId = bankTypeId;
        openData.corpId = $("#corpId").val();
        openData.reducedCurId = selectType.get("reducedCurId").val();
        openData.beginDate = $("#beginDate").val();
        openData.endDate = $("#endDate").val();
        if (stockType != undefined) {
            openData.stockType = stockType;
        }
        var openUrl = basePath + "/bi/capitalStock/listFreeze?" + parseParam(openData);
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.capitalStock.reportFreeze'), openUrl);
            return;
        }
    }

    /**
     * 跳转票据占用明细查询列表
     * @param _this
     */
    openAbBillFreeze = function (bankTypeId, stockType) {
        openData.bankTypeId = bankTypeId;
        openData.corpId = $("#corpId").val();
        openData.reducedCurId = selectType.get("reducedCurId").val();
        openData.beginDate = $("#beginDate").val();
        openData.endDate = $("#endDate").val();
        if (stockType != undefined) {
            openData.stockType = stockType;
        }
        var openUrl = basePath + "/bi/capitalStock/listAbBillFreeze?" + parseParam(openData);
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.capitalStock.reportAbBillFreeze'), openUrl);
            return;
        }
    }

    /**
     * 重新组装参数
     * @param param
     * @param key
     * @returns {string}
     */
    var parseParam = function (param, key) {
        var paramStr = "";
        if (param instanceof String || param instanceof Number || param instanceof Boolean) {
            paramStr += "&" + key + "=" + encodeURIComponent(param);
        } else {
            $.each(param, function (i) {
                var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
                paramStr += '&' + parseParam(this, k);
            });
        }
        return paramStr.substr(1);
    };


    // if (reportType === "net") {
    //     // 初始化图表标签
    //     var myChartSum = echarts.init(document.getElementById('chart'));
    //
    //     initBar = function () {
    //         //通过Ajax获取数据
    //         var url = basePath + "/bi/capitalStock/createNetBar";
    //         $.ajax({
    //             type: "post",
    //             async: false, //同步执行
    //             url: url,
    //             data: openData,
    //             dataType: "json", //返回数据形式为json
    //             success: function (result) {
    //                 var option = "";
    //                 if (result) {
    //                     option = initChart(result.legend, result.xAxisData, result.series);
    //                 }
    //                 myChartSum.hideLoading();
    //                 myChartSum.setOption(option, true);
    //             }
    //         });
    //     };
    // }

    /**
     * 初始化资金汇总表
     * @param legend
     * @param xAxisData
     * @param series
     */
    function initChart(legend, xAxisData, series) {
        var option = {
            title: {
                text: bt.lang("nis.report.allocation.available")
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: legend
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: xAxisData
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: JSON.parse(series)
        };
        return option;
    }
});


/**
 * 币种转换校验
 */
function checkConvertCurCallBack() {
    var origCur = selectType.get("curId").val();
    var reducedCurId = selectType.get("reducedCurId").val();
    if (isNotEmpty(origCur) && isNotEmpty(reducedCurId)) {
        $.ajax({
            url: basePath + "/report/deposit/balance/checkConvertCurId",
            type:"post",
            data:{
                "origCurId": origCur,
                "convertCurId": reducedCurId,
                "date": bt.date.format(new Date(), 'yyyy-mm-dd')
            },
            dataType: "json",
            success:function (data) {
                if (!data.success) {
                    parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
                    selectType.get("reducedCurId").val("");
                }
            }
        });
    }
}

/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}
/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
    return !isEmpty(stringData)
}
checkDate = function (data) {
    var url = basePath + "/bi/capitalStock/getDate";
    $.ajax({
        type: "post",
        url: url,
        dataType:"json",
        data: {dateType: data.value},
        success: function (result) {
            dateType.get("beginDate").val(result.beginDate);
            dateType.get("endDate").val(result.endDate);
        }
    });
}
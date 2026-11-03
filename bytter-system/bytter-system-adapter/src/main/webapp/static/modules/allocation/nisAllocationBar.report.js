bt.use(['form', 'datatable', 'dateType', 'mulselect', 'corp', 'money'], function () {
    $ = bt.jquery, form = bt.form();
    var corp = bt.corp.init();
    var dateType = bt.dateType.init();

    var chartIconColor = '#c2c2c2';
    var chartTitleColor = '#303343'
    if($('body').hasClass('dark-skin')){
        chartIconColor = '#D3D3D3'
        chartTitleColor ='#d3d3d3'
    }

    /**
     * 币别回调
     * @param data
     */
    var dataCurId = "";
    var natualCurrency = [];
    curCallBack = function (data) {
        natualCurrency = [];
        $.each(data, function (index, cur) {
            if (dataCurId === "") {
                dataCurId += cur.id;
            } else {
                dataCurId += "," + cur.id;
            }
            if (cur.curName !== bt.lang('table.curName.rmb')) {
                //通过Ajax获取数据
                var url = basePath + "/basic/business/fcexcrateadjust/getExchangeRate";
                $.ajax({
                    type: "post",
                    url: url,
                    data: {curId: cur.id},
                    success: function (result) {
                        natualCurrency.push({currency: cur.curName, rate: result});
                    }
                });
            }
        });
    }
    bt.mulselect.init();
    var datatable = bt.datatable.init({
        elem: "#appTable",
        ajax: {
            'url': basePath + "/report/allocation/dataTableSum",
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [],
        rowsGroup: [0],
        buttonsLayout: false,
        columns: [
            {
                data: "corpName", bSortable: false,
                render: function (data, type, full, meta) {
                    return '<a href="#"  onclick="openInfo(\'' + full.corpId + '\')">' + full.corpName + '</a>';
                }
            },
            {data: "curName", bSortable: false},
            {
                data: "upPayMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!data){
                        return bt.money.format(data);
                    }
                    return '<a  href="#" onclick="openInfo(\'' + full.corpId + '\',\'5,8\',\'' + full.curId + '\')">' + bt.money.format(full.upPayMoney) + '</a>';
                }
            },
            {
                data: "downPayMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!data){
                        return bt.money.format(data);
                    }
                    return '<a  href="#" onclick="openInfo(\'' + full.corpId + '\',\'2,7\',\'' + full.curId + '\')">' + bt.money.format(full.downPayMoney) + '</a>';
                }
            },
            {
                data: "askPayMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!data){
                        return bt.money.format(data);
                    }
                    return '<a  href="#" onclick="openInfo(\'' + full.corpId + '\',\'3\',\'' + full.curId + '\')">' + bt.money.format(full.askPayMoney) + '</a>';
                }
            },
            {
                data: "turnPayMoney", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    if(!data){
                        return bt.money.format(data);
                    }
                    return '<a  href="#" onclick="openInfo(\'' + full.corpId + '\',\'6\',\'' + full.curId + '\')">' + bt.money.format(full.turnPayMoney) + '</a>';
                }
            },
        ],
        btInitComplete: function (setting, json) {
            initBar();
        },
        btDrawCallback: function (setting) {
            initBar();
        },
        summary: {
            pageSum: true,
            groupColumn: 'curName',//分类汇总列参数
            natualCurrency: function () {
                if (natualCurrency.length != 0) {
                    return natualCurrency;
                } else {
                    if ($("#rate").val() !== "") {
                        natualCurrency.push({currency: bt.lang('table.capitalStock.moeny'), rate: $("#rate").val()});
                        return natualCurrency;
                    } else {
                        return false;
                    }
                }
            },
            position: "top",
            format:
                {
                    decimal: 2
                }
            ,
            sumList: [
                {
                    colNum: '2',
                    dataName: 'upPayMoney'
                },
                {
                    colNum: '3',
                    dataName: 'downPayMoney'
                },
                {
                    colNum: '4',
                    dataName: 'askPayMoney'
                },
                {
                    colNum: '5',
                    dataName: 'turnPayMoney'
                }
            ]
        }
    });

    /**
     * 跳转明细页面
     * @param corpId
     * @param voucherFrom
     * @param curId
     */
    openInfo = function (corpId, voucherFrom, curId) {
        var openUrl = basePath + "/report/allocation/open?corpId=" + corpId;
        if (voucherFrom != undefined) {
            openUrl += "&voucherFrom=" + voucherFrom;
        }
        if (curId != undefined) {
            openUrl += "&curId=" + curId;
        }
        openUrl += "&beginReqDate=" + bt.dateType.get("ged_reqDate").val() + "&endReqDate=" + bt.dateType.get("led_reqDate").val();
        console.log(openUrl);
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: openUrl
        });
    }

    // 初始化图表标签
    var myChartSum = echarts.init(document.getElementById('chartSum'));
    var xData;
    var openParams;

    /**
     * 报表点击事件
     * @param param4
     */
    function openClick(param) {
        var corpIndex = Math.ceil((param.dataIndex + 1) / 4);
        var keyParam = xData[corpIndex - 1] + param.name + param.seriesName;
        var openParam = openParams[keyParam].split("!-!");
        openInfo(openParam[0], openParam[1], openParam[2]);
    }

    myChartSum.on('click', openClick);

    initBar = function () {
        var data = {
            netId: corp.get("ins_netId").val(),
            corpId: corp.get("ins_corpId").val(),
            curId: dataCurId,
            beginReqDate: dateType.get("ged_reqDate").val(),
            endReqDate: dateType.get("led_reqDate").val(),
        };
        $("#reqDateText").text(data.beginReqDate + " ~ " + data.endReqDate);
        //通过Ajax获取数据
        var url = basePath + "/report/allocation/createNisAllocationSumBar";
        $.ajax({
            type: "post",
            async: false, //同步执行
            url: url,
            data: data,
            dataType: "json", //返回数据形式为json
            success: function (result) {
                var option = initChartSum(result.legend, result.xAxisData1, result.xAxisData2, result.series);
                myChartSum.hideLoading();
                myChartSum.setOption(option, true);
                window.onresize = myChartSum.resize;
                xData = result.xAxisData2;
                openParams = result.openParams;
            }
        });
    };

    /**
     * 初始化资金汇总表
     * @param legend
     * @param xAxisData1
     * @param series
     */
    function initChartSum(legend, xAxisData1, xAxisData2, series) {
        var option = {
            title: [{
                show: true,
                text: bt.lang("nis.report.allocation.summary"),
                textStyle:{
                    color:chartTitleColor
                }
            }],
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: legend,
                top: 30,
            },
            grid: {
                left: '3%',
                right: "4%",
                bottom: "10%",
                containLabel: true
            },
            "toolbox": {
                "show": true,
                "feature": {
                    "mark": {
                        "show": true,
                        "title": {
                            "markUndo": "删除辅助线",
                            "markClear": "清空辅助线",
                            "mark": "辅助线开关"
                        },
                        "lineStyle": {
                            "color": "#1e90ff",
                            "type": "dashed",
                            "width": 2
                        }
                    },
                    "dataZoom": {
                        "show": false,
                        "title": {
                            "dataZoom": "区域缩放",
                            "dataZoomReset": "区域缩放后退"
                        }
                    },
                    "magicType": {
                        "show": true,
                        "title": {
                            "bar": "柱形图切换",
                            "stack": "堆积",
                            "tiled": "平铺",
                            "line": "折线图切换"
                        },
                        "type": [
                            "line",
                            "bar"
                        ]
                    },
                    "restore": {
                        "show": true,
                        "title": "还原"
                    },
                    "saveAsImage": {
                        "show": true,
                        "title": "保存为图片",
                        "type": "png",
                        "lang": [
                            "点击保存"
                        ]
                    }
                }
            },
            dataZoom: [
                {
                    type: 'inside',
                    start: 50,
                    end: 100,
                },
                {
                    show: true,
                    start: 30,
                    end: 100,
                    filterMode: 'filter',
                    xAxisIndex: [0, 1]
                }
            ],
            xAxis: [
                {
                    type: 'category',
                    data: xAxisData1,
                    position: "bottom",
                    offset: 0,
                    axisTick: {
                        show: true,
                        interval: function (index, value) {
                            return index % 4 === 0;
                        },
                        length: 30,
                        lineStyle: {
                            // 使用深浅的间隔色
                            color: ['#aaa', '#ddd']
                        }
                    }
                },
                {
                    type: 'category',
                    length: 0,
                    data: xAxisData2,
                    position: 'bottom',
                    offset: 30,
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    axisLabel: {interval: 0}
                }
            ],
            series: JSON.parse(series)
        };
        return option;
    }

    initBar();

});

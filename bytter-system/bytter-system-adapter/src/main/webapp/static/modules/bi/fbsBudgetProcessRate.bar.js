bt.use(['form', 'dateType', 'mulselect', 'corp', 'money'], function () {
    $ = bt.jquery, form = bt.form();
    bt.mulselect.init();

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
            id: $("#id").val(),
            timeType: $("#timeType").val(),
            beginDate: $("#beginDate").val(),
            endDate: $("#endDate").val(),
        };
        //通过Ajax获取数据
        var url = basePath + "/bi/budget/createFbsBudgetProcessRateBar";
        $.ajax({
            type: "post",
            async: false, //同步执行
            url: url,
            data: data,
            dataType: "json", //返回数据形式为json
            success: function (result) {
                var option = initChartSum(result.legend, result.bMoneyData, result.processMoneyData);
                myChartSum.hideLoading();
                myChartSum.setOption(option, true);
                window.onresize = myChartSum.resize;
                //xData = result.xAxisDataItem;
                //openParams = result.openParams;
            }
        });
    };

    /**
     * 初始化
     * @param legend
     * @param xAxisData1
     * @param series
     */
    function initChartSum(legend, bMoneyData, processMoneyData) {
        var option = {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data:[bt.lang('table.fbsBudgetYear.bMoney'),bt.lang('table.fbsBudgetProcess.processMoney')]
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    data : [bt.lang('table.fbsBudget.money.in'),bt.lang('table.fbsBudget.money.out')]
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:bt.lang('table.fbsBudgetYear.bMoney'),
                    type:'bar',
                    data:bMoneyData
                },
                {
                    name:bt.lang('table.fbsBudgetProcess.processMoney'),
                    type:'bar',
                    data:processMoneyData
                },
            ]
        };
        return option;
    }

    initBar();

});
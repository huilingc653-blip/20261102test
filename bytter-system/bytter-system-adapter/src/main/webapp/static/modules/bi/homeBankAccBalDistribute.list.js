var corp;
var selectType;
var myChartSum;
var search;
bt.use(['form', 'dateType', 'selectType', 'moneyType', 'corp', 'datatablecore','customTable','jquery','form', 'dicts','money'], function () {
    var $ = bt.jquery, form = bt.form();
    var dateType = bt.dateType.init();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    selectType = bt.selectType.init();
    var uiStyle = $('#uiStyle').val();
    if(uiStyle != '1') {
        $('body').addClass('old-skin');
    }
    var webSkin = store.get('web-skin')
    if(webSkin === 'dark-skin'){
        $('body').addClass('dark-skin')
    }else {
        $('body').removeClass('dark-skin')
    }
    //刷新
    $("#refreshMap").on("click",function () {
        window.location.reload();
    })

    window.onresize = function () {
        myChartDistribute.resize()
        myChartBankType.resize()
        myChartAttribute.resize()
    }
    // 解决加载慢时，图标样式不对的问题
    setTimeout(function(){
        if (document.createEvent) {
            const event = document.createEvent('HTMLEvents')
            event.initEvent('resize', true, true)
            window.dispatchEvent(event)
        } else if (document.createEventObject) {
            window.fireEvent('onresize')
        }
    })

    //设置定时任务刷新：5分钟刷新一次（300000）
    /*window.setInterval(function(){
        reloadBar();
        initBankAccBal();
    }, 300000);*/

    //单位
    bt.corpCallback = function(data){
       reloadBar();
      //  initBankAccBal();
    }
    corp = bt.corp.init();

    //金额单位
    bt.moneyTypeCallback = function(data){
        reloadBar();
       // initBankAccBal();
    }

    function reloadBar() {
        initDistributeBar();
        initBankTypeBar();
        initAttributeBar("attribute");
        initBankAccBal();
    }

    //周期类型
    bt.timeTypeCallback = function(data){
        var timeType = bt.dicts.get("timeType").val();
        if(timeType == "Y"){
            $("#monthsDiv").hide();
        } else {
            $("#monthsDiv").show();
        }
        initDistributeBar();
    }

    //年份
    form.on('select(year)', function (data) {
        initDistributeBar();
    });

    //月份
    bt.monthCallback = function(data){
        initDistributeBar();
    };
    bt.dicts.init();

    var initBankAccBal = function () {
        var data = {
            corpId: bt.corp.get("corpId").val(),
            moneyType: bt.dicts.get("moneyType").val(),
            timeType: bt.dicts.get("timeType").val()
        };
        $.ajax({
            type: "post",
            url: basePath + "/bi/homeBankAccBalDistribute/initBankAccBal",
            data: data,
            async: true, //同步执行
            dataType: 'json',
            success: function (dataList) {
                renderBalData(dataList['curBal']);
                renderTopBalData(dataList['listBal'], "#topBalContainer");
                renderCurBalData(dataList['listBalCur'], "#curBalContainer");
            }
        });
    };

    function renderBalData(curBal){
        if(curBal){
            $("#balContainer").html(moneyFun.format(curBal.bal));
            $("#availBalContainer").html(moneyFun.format(curBal.availBal));
            $("#frzBalContainer").html(moneyFun.format(curBal.frzBal));
            $("#standardBalContainer").html(moneyFun.format(curBal.standardBal));
            $("#foreignBalContainer").html(moneyFun.format(curBal.foreignBal));
        }
    }

    function renderTopBalData(list,containerId){
        var html = '<table id="topBalTable" class="report-table"><tbody>';
        html += '<tr class="report-table-header">';
        html += '<td style="width: 60px;"></td>';
        html += '<td>'+bt.lang('table.nisBillhead.accId')+'</td>';
        html += '<td>'+bt.lang('table.btBankAcc.bankTypeId')+'</td>';
        html += '<td>'+bt.lang('table.fcBond.corpName')+'</td>';
        html += '<td>'+bt.lang('table.btBankAccDtl.bal')+'</td></tr>';
        html += '';
        if(list){
            for (var i = 0; i < list.length; i++) {
                var data = list[i]
                html += '<tr>'
                html += '<td align="center">'+ (i+1) +'</td>'
                html += '<td>'+ data.bankAcc +'</td>'
                html += '<td>'+ data.bankTypeName +'</td>'
                html += '<td>'+ data.corpName +'</td>'
                html += '<td>'+ moneyFun.format(data.bal) +'</td>'
                html += '</tr>'
            }
        }
        $(containerId).html(html)
    }

    function renderCurBalData(list,containerId){
        var html = '<table id="curBalTable" class="report-table"><tbody>';
        html += '<tr class="report-table-header">';
        html += '<td>'+bt.lang('table.bisAccDtl.curName')+'</td>';
        html += '<td>'+bt.lang('table.btBankAccDtl.bal')+'</td>';
        html += '<td>'+bt.lang('table.accbook.rate')+'</td>';
        html += '';
        if(list){
            for (var i = 0; i < list.length; i++) {
                var data = list[i]
                html += '<tr>'
                html += '<td>'+ data.curName +'</td>'
                html += '<td>'+ moneyFun.format(data.bal) +'</td>'
                html += '<td>'+ moneyFun.format(data.balRatio) +'%</td>'
                html += '</tr>'
            }
        }
        $(containerId).html(html)
    }

    //银行账户余额趋势图
    myChartDistribute = echarts.init(document.getElementById('chartBalDistribute'));
    initDistributeBar = function () {
        var data = {
            corpId: bt.corp.get("corpId").val(),
            moneyType: bt.dicts.get("moneyType").val(),
            timeType: bt.dicts.get("timeType").val(),
            year: $("#year").val(),
            month: bt.dicts.get("month").val()
        };
        //通过Ajax获取数据
        var url = basePath + "/bi/homeBankAccBalDistribute/createBankAccBalDistributeBar";
        $.ajax({
            type: "post",
            async: true, //同步执行
            url: url,
            data: data,
            dataType: "json", //返回数据形式为json
            success: function (result) {
                var option = initDistributeChart(result);
                myChartDistribute.setOption(option, true);
                myChartDistribute.resize();
            }
        });
    };
    initDistributeBar();
    /**
     * 初始化
     * @param legend
     * @param xAxisData1
     * @param series
     */
    function initDistributeChart(result) {
        var option = {
            color: ['#4e6dec'],
            tooltip: {
                trigger: 'item'
            },
            grid:{
                bottom:'50',
                left:10,
                right:15,
                containLabel:true
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                axisLabel: {
                    interval:0
                },
                axisLine: {
                    lineStyle:{
                        color: $('#textColor').val(),
                    }
                },
                data: $.parseJSON(result.xData)
            },
            yAxis: {
                name: bt.lang('table.bankAccBalTrend.corpId')+"："+bt.dict("common.MoneyType", bt.dicts.get("moneyType").val()),
                type: 'value',
                axisLine: {
                    lineStyle:{
                        color: $('#textColor').val(),
                    }
                },
                splitLine: {
                    show: false
                }
            },
            series: [
                {
                    data: $.parseJSON(result.yData),
                    type: 'line'
                }
            ]
        };
        return option;
    }

    //银行账户余额类别统计占比
    myChartBankType = echarts.init(document.getElementById('chartBankType'));
    initBankTypeBar = function () {
        var data = {
            corpId: bt.corp.get("corpId").val(),
            moneyType: bt.dicts.get("moneyType").val(),
            groupBy: "bankType"
        };
        //通过Ajax获取数据
        var url = basePath + "/bi/homeBankAccBalDistribute/createBankAccBalBar";
        $.ajax({
            type: "post",
            async: true, //同步执行
            url: url,
            data: data,
            dataType: "json", //返回数据形式为json
            success: function (result) {
                var option = initBankTypeChart(result);
                myChartBankType.setOption(option, true);
                myChartBankType.resize();
            }
        });
    };
    initBankTypeBar();

    /**
     * 初始化
     * @param legend
     * @param xAxisData1
     * @param series
     */
    function initBankTypeChart(result) {
        var industryData = $.parseJSON(result.bals);
        var option = {
            color: ['#4e6dec','#d2585d','#ed955a','#ecda6c','#ec8b92','#93c8ef','#b0ede6'],
            tooltip: {
                trigger: 'item'
            },
            legend: {
                type: 'scroll',
                orient: 'vertical',
                left: 'left',
                top: 'middle',
                data: $.parseJSON(result.names),
                formatter:function(name){  //该函数用于设置图例显示后的百分比
                    var total = 0;
                    var value;
                    industryData.forEach(function (item) {
                        total += Number(item.value);
                        if (item.name == name) {
                            value = item.value || 0;
                        }
                    })
                    var percent = (value / total) * 100
                    var p = bt.money.format(percent,{separator:''}); //求出百分比
                    return name+" "+p+"%";  //返回出图例所显示的内容是名称+百分比
                },
                textStyle:{
                    color: $('#textColor').val(),
                }
            },
            series: [
                {
                    name: '',
                    type: 'pie',
                    radius: ['40%', '60%'],  // 设置饼状图大小，100%时，最大直径=整个图形的min(宽，高)
                    //radius: ['30%', '60%'],  // 设置环形饼状图， 第一个百分数设置内圈大小，第二个百分数设置外圈大小
                    center: ['65%', '50%'],  // 设置饼状图位置，第一个百分数调水平位置，第二个百分数调垂直位置
                    label: {
                        normal: {
                            //formatter: '{b}:{c}' + '\n\r' + '({d}%)',
                            show: false,
                            position: 'center'
                        }
                    },
                    data: $.parseJSON(result.bals),
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ],
        };
        return option;
    }

    //属性
    $("#attributeBut").on("click",function () {
        $("#attributeBut").addClass("active");
        $("#natureBut").removeClass("active");
        initAttributeBar("attribute");
    })
    //性质
    $("#natureBut").on("click",function () {
        $("#natureBut").addClass("active");
        $("#attributeBut").removeClass("active");
        initAttributeBar("nature");
    })

    //银行账户余额属性分布
    myChartAttribute = echarts.init(document.getElementById('chartAttribute'));
    initAttributeBar = function (groupBy) {
        var data = {
            corpId: bt.corp.get("corpId").val(),
            moneyType: bt.dicts.get("moneyType").val(),
            groupBy: groupBy
        };
        //通过Ajax获取数据
        var url = basePath + "/bi/homeBankAccBalDistribute/createBankAccBalBar";
        $.ajax({
            type: "post",
            async: true, //同步执行
            url: url,
            data: data,
            dataType: "json", //返回数据形式为json
            success: function (result) {
                var option = initAttributeChart(result);
                myChartAttribute.setOption(option, true);
                myChartAttribute.resize();
            }
        });
    };
   initAttributeBar("attribute");

    /**
     * 初始化
     * @param legend
     * @param xAxisData1
     * @param series
     */
    function initAttributeChart(result) {
        var industryData = $.parseJSON(result.bals);
        var option = {
            color: ['#4e6dec','#d2585d','#ed955a','#ecda6c','#ec8b92','#93c8ef','#b0ede6'],
            tooltip: {
                trigger: 'item'
            },
            legend: {
                type: 'scroll',
                orient: 'vertical',
                left: 'left',
                top: 'middle',
                data: $.parseJSON(result.names),
                formatter:function(name){  //该函数用于设置图例显示后的百分比
                    var total = 0;
                    var value;
                    industryData.forEach(function (item) {
                        total += Number(item.value);
                        if (item.name == name) {
                            value = item.value;
                        }
                    })
                    var p = Math.round(((value / total) * 100)); //求出百分比
                    return name+" "+p+"%";  //返回出图例所显示的内容是名称+百分比
                },
                textStyle:{
                    color: $('#textColor').val(),
                }
            },
            series: [
                {
                    name: '',
                    type: 'pie',
                    //radius: '50%',  // 设置饼状图大小，100%时，最大直径=整个图形的min(宽，高)
                    radius: '60%',  // 设置环形饼状图， 第一个百分数设置内圈大小，第二个百分数设置外圈大小
                    center: ['60%', '55%'],  // 设置饼状图位置，第一个百分数调水平位置，第二个百分数调垂直位置
                    label: {
                        normal: {
                            //formatter: '{b}:{c}' + '\n\r' + '({d}%)',
                            show: false,
                            position: 'center'
                        }
                    },
                    data: $.parseJSON(result.bals),
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ],
        };
        return option;
    }
});

var corp;
var selectType;
var myChartSum;
var search;
bt.use(['form', 'dateType', 'selectType', 'moneyType', 'corp', 'datatablecore','customTable','jquery','form', 'dicts'], function () {
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

    //设置定时任务刷新：5分钟刷新一次（300000）
    /*window.setInterval(function(){
        initDistributeBar();
        initBankAccDtl();
    }, 300000);*/

    //单位
    bt.corpCallback = function(data){
        initDistributeBar();
        initBankAccDtl();
    }
    corp = bt.corp.init();

    //金额单位
    bt.moneyTypeCallback = function(data){
        initDistributeBar();
        initBankAccDtl();
    }
    bt.dicts.init();

    var initBankAccDtl = function () {
        var data = {
            corpId: bt.corp.get("corpId").val(),
            moneyType: bt.dicts.get("moneyType").val(),
            dateType: $("#dateType").val()
        };
        $.ajax({
            type: "post",
            url: basePath + "/bi/homeBankAccDtlDistribute/initBankAccDtl",
            data: data,
            async: true, //同步执行
            dataType: 'json',
            success: function (dataList) {
                renderDtlData(dataList['curData']);
                renderListDtlData(dataList['listDtl'], "#bankAccDtlTable");
                renderBisExcData(dataList['waitFillExc'], dataList['successExc'], dataList['failedExc']);
            }
        });
    };

    function renderDtlData(curData){
        if(curData){
            $("#startMoneyContainer").html(moneyFun.format(curData.startMoney));
            $("#getMoneyContainer").html(moneyFun.format(curData.getMoney));
            $("#borrowMoneyContainer").html(moneyFun.format(curData.borrowMoney));
            $("#getNumbersContainer").html(curData.getNumbers);
            $("#borrowNumbersContainer").html(curData.borrowNumbers);
            $("#endMoneyContainer").html(moneyFun.format(curData.endMoney));
        }
    }

    function renderListDtlData(list,containerId){
        var html = '<table id="topBalTable" class="report-table"><tbody>';
        if(list){
            for (var i = 0; i < list.length; i++) {
                var dtl = list[i]
                if(i<3) {
                    html += '<div class="deal-list">'
                    html += '<div class="list-header">';
                    html += '<div class="type">'+bt.dict("btBankAccDtl.cdSign", dtl.cdSign)+'</div>';
                    html += '<div class="money">' + moneyFun.format(dtl.amt) + '</div>';
                    html += '</div>';
                    html += '<div class="list-detail">';
                    html += '<div>';
                    if(dtl.oppAccNo == null){
                        dtl.oppAccNo = '';
                    }
                    if(dtl.oppAccName == null){
                        dtl.oppAccName = '';
                    }
                    html += '<div>' + bt.lang("table.btBankAccDtl.oppAccNo")+ '：' + dtl.oppAccNo + '</div>';
                    html += '<div class="text-color1">' + bt.lang("table.btBankAccDtl.oppAccName")+ '：' + dtl.oppAccName + '</div>';
                    html += '</div>';
                    html += '<div>';
                    html += '<div>' + bt.lang("table.btBankAccDtl.bankAcc") + '：' + dtl.bankAcc + '</div>';
                    html += '<div class="text-color1">' + bt.lang("table.btBankAccDtl.accName") + '：' + dtl.accName + '</div>';
                    html += '</div>';
                    html += '<div>';
                    html += '<div class="deal-time">' + dtl.transTime + '</div>';
                    html += '</div>';
                    html += '</div>';
                    html += '</div>'
                }
            }
        }
        $(containerId).html(html)
    }

    function renderBisExcData(waitFillExc,successExc,failedExc){
        if(waitFillExc){
            $("#waitTotalMoney").html(moneyFun.format(waitFillExc.totalMoney));
            $("#waitTotalNum").html(waitFillExc.totalNum);
        }
        if(successExc){
            $("#successTotalMoney").html(moneyFun.format(successExc.totalMoney));
            $("#successTotalNum").html(successExc.totalNum);
        }
        if(failedExc){
            $("#failTotalMoney").html(moneyFun.format(failedExc.totalMoney));
            $("#failTotalNum").html(failedExc.totalNum);
        }
    }

    var activeClass ='active'
    $('#myChartDistributeBtn').on('click','button',function () {
        var key = $(this).data('id');
        $('#myChartDistributeBtn button').removeClass(activeClass)
        $(this).addClass(activeClass)
        var dateTypeValue = 7
        if(key === 'oneWeekBut'){ //近7天
            dateTypeValue = 7
        }else if(key === 'oneMonthBut'){ //1个月
            dateTypeValue = 30
        }else if(key === 'threeMonthBut'){//3个月
            dateTypeValue = 90
        }
        $("#dateType").val(dateTypeValue);
        initDistributeBar();
    })

    myChartDistribute = echarts.init(document.getElementById('myChartDistribute'));
    initDistributeBar = function () {
        var dateType = $("#dateType").val();
        if(!dateType || dateType == ""){
            //默认近7天
            dateType = "7";
        }
        var data = {
            corpId: bt.corp.get("corpId").val(),
            moneyType: bt.dicts.get("moneyType").val(),
            dateType: dateType
        };
        //通过Ajax获取数据
        var url = basePath + "/bi/homeBankAccDtlDistribute/createBankAccDtlDistributeBar";
        $.ajax({
            type: "post",
            async: true, //同步执行
            url: url,
            data: data,
            dataType: "json", //返回数据形式为json
            success: function (result) {
                //重新刷指标数据
                renderDtlData(result['curData']);

                var option = initDistributeChart(result);
                myChartDistribute.setOption(option, true);
                myChartDistribute.resize();
            }
        });
    };
    initDistributeBar();

    window.onresize = function () {
        myChartDistribute.resize()
    }

    // 解决加载慢时，图标样式不对的问题
    setTimeout(function() {
        if (document.createEvent) {
            const event = document.createEvent('HTMLEvents')
            event.initEvent('resize', true, true)
            window.dispatchEvent(event)
        } else if (document.createEventObject) {
            window.fireEvent('onresize')
        }
    })

    /**
     * 初始化
     * @param legend
     * @param xAxisData1
     * @param series
     */
    function initDistributeChart(result) {
        var option = {
            color: ['#4e6dec','#d2585d','#6687ee','#f19aa0'],
            tooltip: {
                trigger: 'axis',
                right:'263px',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            grid:{
                left:'100',
                right:'100',
                bottom:'50'
            },
            legend: {
                data: [bt.lang('repot.bankAccBal.getMoney'), bt.lang('repot.bankAccBal.borrowMoney'), bt.lang('repot.bankAccBal.getNumbers'), bt.lang('repot.bankAccBal.borrowNumbers')],
                textStyle:{
                    color: $('#barColor').val(),
                }
            },
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: true,
                    data: $.parseJSON(result.xData),
                    axisPointer: {
                        type: 'shadow'
                    },
                    axisLine: {
                        lineStyle:{
                            color: $('#barColor').val(),
                        }
                    }
                }
            ],
            yAxis : [
                {
                    name: bt.lang('table.bankAccBalTrend.corpId')+"："+bt.dict("common.MoneyType", bt.dicts.get("moneyType").val()),
                    type : 'value',
                    axisLine: {
                        lineStyle:{
                            color: $('#barColor').val(),
                        }
                    },
                    splitLine: {
                        show: false
                    }
                },
                {
                    name: bt.lang('table.fcAccBalance.cnt'),
                    type : 'value',
                    axisLine: {
                        lineStyle:{
                            color: $('#barColor').val(),
                        }
                    },
                    minInterval : 1,
                    splitLine: {
                        show: false
                    }
                },
            ],
            series: [
                {
                    name: bt.lang('repot.bankAccBal.getMoney'),
                    type: 'bar',
                    barWidth:6,
                    yAxisIndex:0,
                    tooltip: {
                        valueFormatter: function (value) {
                            return value;
                        }
                    },
                    data: $.parseJSON(result.getMoney)
                },
                {
                    name: bt.lang('repot.bankAccBal.borrowMoney'),
                    type: 'bar',
                    barWidth:6,
                    yAxisIndex:0,
                    tooltip: {
                        valueFormatter: function (value) {
                            return value;
                        }
                    },
                    data: $.parseJSON(result.borrowMoney)
                },
                {
                    name: bt.lang('repot.bankAccBal.getNumbers'),
                    type: 'line',
                    yAxisIndex:1,
                    tooltip: {
                        valueFormatter: function (value) {
                            return value;
                        }
                    },
                    data: $.parseJSON(result.getNumbers)
                },
                {
                    name: bt.lang('repot.bankAccBal.borrowNumbers'),
                    type: 'line',
                    yAxisIndex:1,
                    tooltip: {
                        valueFormatter: function (value) {
                            return value;
                        }
                    },
                    data: $.parseJSON(result.borrowNumbers)
                }
            ]
        };
        return option;
    }
});

function viewBisExc() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bdlink/bisexc/list");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisexc/list"
    });
}

function viewSettlement() {
    //获取日期参数
   var dateType=$("#dateType").val();

    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/report/allocationsettlement/corplist?dateType="+dateType);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/report/allocationsettlement/corplist?dateType="+dateType
    });
}


function viewBisAccDtl() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bankacc/btbankaccdtl/query");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccdtl/query"
    });
}

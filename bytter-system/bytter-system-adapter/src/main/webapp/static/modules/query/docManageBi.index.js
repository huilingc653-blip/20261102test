var corp;
var selectType;
var search;
bt.use(['moneyType', 'jquery','money','laydate','form'], function () {
    var $ = bt.jquery;
   bt.form.render()
    var moneyFun = bt.money;
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

    //应收款周转分析日期
    var turnoverAnsDate = bt.laydate.render({
        elem:"#turnoverAnsDate",
        type:'month',
        range:'~',
        format:'yyyy年M月',
        btns:['confirm'],
        value: getRecent3Months(),
        max: getMaxMonth(),
        done:function(value,date,endDate){
            $("#turnoverAnsDate").val(value);
            initArAnalysis()
        }
    })

    //获取最近３个月，不包括当月
    function getRecent3Months(){

        var endDate = new Date();
        endDate.setMonth(endDate.getMonth() - 1); //上月

        var startDate = new Date();
        startDate.setMonth(startDate.getMonth() - 3);

        function formatArTurnoverDate(date){
            return `${date.getFullYear()}年${date.getMonth() + 1}月`;
        }
        return formatArTurnoverDate(startDate) + ' ~ ' + formatArTurnoverDate(endDate);
    }

    function formatDate(item){
        if(item !== undefined){
            var [year, month] = item.match(/\d+/g);
            return `${year}-${month.padStart(2, '0')}`;
        }
    };

    function getParentQueryFields(){
        return parent.bt.getQueryFields();
    }
    //刷新
   bt.refreshReport = function(data){
       reloadBar();
   }
   //应收趋势分析_chart
    var myChartArTrendAnalysis = echarts.init(document.getElementById('chartARTrendAnalysis'));
    var myChartARTrendTooltip = echarts.init(document.getElementById('chartARTrendTooltip'));
    //应收款占比分析_chart
    var myChartArRatioDom = document.getElementById('chartArRatio');
    var myChartArRatio = echarts.init(myChartArRatioDom);
    var customerNameTooltip = document.getElementById('customerNameTooltip');
    //应收款单据类型分布_chart
    var myChartArDocTypeDist = echarts.init(document.getElementById('chartArDocTypeDist'));
    var myChartArDocTooltip = echarts.init(document.getElementById('chartArDocTooltip'));
    //账龄分布图_chart
   var myChartAgingDom = document.getElementById('chartAgingDistribution');
   var myChartAgingDist = echarts.init(myChartAgingDom);
   var myAgingCustomTooltip = document.getElementById('agingCustomTooltip');

    $("#refreshArTrend").on("click",function () {
       initArTrendAnalysisBar();
    })
    window.onresize = function () {
        initArAnalysis();
        myChartArTrendAnalysis.resize();
        myChartArRatio.resize();
        myChartArDocTypeDist.resize();
        myChartAgingDist.resize();
    }

    function reloadBar() {
        parent.bt.loading.show();
        initArAnalysis();
        initArTrendAnalysisBar();
        var curType = $("button.active").attr("id");
        if('byOverDueAmountBut' == curType){
            initArRatioBar("byOverDueAmount");
        }else{
            initArRatioBar('byWriteOffBal');
        }
        initArDocTypeDistBar();
        initAgingDistChartBar();
         setTimeout(function(){
           parent.bt.loading.hide();
      },500)
    }
    reloadBar();

    //趋势分析日期
    $("#receiptDate").val($("#ged_receiptDate").val()+' ~ '+$("#led_receiptDate").val())
    var dateRange = bt.laydate.render({
        elem:"#receiptDate",
        type:'month',
        range:'~',
        format:'yyyy年M月',
        btns:['confirm'],
        value:$("#ged_receiptDate").val()+' ~ '+$("#led_receiptDate").val(),
        max: getMaxMonth(),
        done:function(value,date,endDate){
            var monthArr = value && value.split('~') || ''
            if(monthArr.length){
                $("#ged_receiptDate").val(monthArr[0])
                $("#led_receiptDate").val(monthArr[1]);
                initArTrendAnalysisBar();
            }
        }
    })

    //最大上月
    function getMaxMonth(){
        var today = new Date();
        today.setDate(0); //上月最后一天
        return today.getFullYear()
            + '-' + String(today.getMonth() + 1).padStart(2, '0')
            + '-' + today.getDate();
    }

    function initArAnalysis() {
        var data = getParentQueryFields();
        var dateStr = $("#turnoverAnsDate").val().split('~');
        if(dateStr!=""){
            data['startDate'] =formatDate(dateStr[0]);
            data['endDate'] =formatDate(dateStr[1]);
            //data['type']='cycle'
        }

        //应收账款分析
        var url = basePath + "/bi/receivables/queryArAnalysis";
        ajaxRequest(url, 'POST', data).success(result => {
            $("#writeOffBal").html(result.writeOffBal===0?'0':(result.writeOffBal.toFixed(2) || '-'));
            $("#overdueAmount").html(result.overdueAmount===0?'0':(result.overdueAmount.toFixed(2) || '-'));
            $("#contractMoney").html(result.contractMoney===0?'0':(result.contractMoney.toFixed(2) || '-'));
        });

        //应收账款周转分析
       var turnoverUrl = basePath + "/bi/receivables/queryArTurnoverRate";
       ajaxRequest(turnoverUrl, 'POST', data).success(result => {
           $("#arTurnoverRatio").html(result.arTurnoverRatio?result.arTurnoverRatio:'-');
           $("#averageArBalance").html(result.averageArBalance===0?'0':(result.averageArBalance.toFixed(2) || '-'));
       });

    }
    $('#cycle-desc').mouseenter(function () {
        tip_index1 = layer.tips(bt.lang('基于当前数据估算，实际以财务报表为准'), '#cycle-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index1);
    });
    $('#rule-desc').mouseenter(function () {
        tip_index2 = layer.tips(bt.lang('<span style="font-weight:bold">周转率更新规则</span><br/>每月月末自动计算并更新，基于截至当前月份所有应收单初始金额和月度余额'), '#rule-desc', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index2);
    });
    function ajaxRequest(url, method='GET', data = null){
        return $.ajax({
           type: method,
           async: true, //同步执行
           url: url,
           data: data,
           dataType: "json", //返回数据形式为json
           cache: false,
           //timeout: 5000
       });
    }

    //应收趋势分析_chart
    //var myChartARTrendCategory = ['25年2月','25年3月','25年4月','25年5月','25年6月','25年7月','25年8月'];

    function initArTrendAnalysisBar(){
        var data = getParentQueryFields();
        data.startDate = formatDate($.trim($('[name="ged_receiptDate"]').val()));
        data.endDate = formatDate($.trim($('[name="led_receiptDate"]').val()));
        var url = basePath + "/bi/receivables/queryArTrendAnalysis";
        ajaxRequest(url, 'POST', data)
            .success(result => {
            if(result.length > 0){
                if(myChartArTrendAnalysis.isDisposed){ //检查是否已被销毁
                    myChartArTrendAnalysis = echarts.init(document.getElementById('chartARTrendAnalysis')); //再初始化
                }
                var option = initArTrendAnalysisChart(result, data.moneyType);
                myChartArTrendAnalysis.setOption(option, true);
                myChartArTrendAnalysis.resize();
            }else{
                myChartArTrendAnalysis.dispose(); //先销毁
                var html = '<div class="article-empty" style="text-align: center;" ><img src="'+ basePath +'/static/v3.2/images/pic_blank.png" class="pic-bank"><div class="empty-text">'+bt.lang("common.msg.temporarily_no_data")+'</div></div>'
                $('#chartARTrendAnalysis').html(html);
            }
        });
    };
    // initArTrendAnalysisBar();
    var categoryData; //放这里时鼠标移入移除时能取到
    function initArTrendAnalysisChart(result, moneyType){
        var legendData = ['应收余额','合同金额','本期新增应收', '本期核销金额'];
        var writeOffBalData;
        var contractMoney;
        var receivableMoney;
        var writeOffAmountData;
        if(result.length > 0){
            categoryData = result.map(obj => obj["month"]);
            writeOffBalData = result.map(obj => obj["writeOffBal"].toFixed(2));
            contractMoney = result.map(obj => obj["contractMoney"].toFixed(2));
            receivableMoney = result.map(obj => obj["receivableMoney"].toFixed(2));
            writeOffAmountData = result.map(obj => obj["writeOffAmount"].toFixed(2));
        }
        var option = {
            color: ['#C70D0D', '#FF7F03', '#3558E1', '#1EA05A'],
            tooltip:{
                trigger: 'axis',
                backgroundColor: '#ffffff',
                borderColor: '#ddd',
                borderWidth: 1,
                confine: true, //限制在图表区域内
                padding: 10,
                textStyle: {
                    color: '#000000',
                    fontSize: 12,
                    fontWeight: 'normal'
                },
                axisPointer: {
                    type: 'shadow'
                }
            },
            legend:{
                data: legendData,
                top: 20,
                left: '0%',
                itemWidth:10,
                itemHeight:10
            },
            grid:{
                left: moneyType == '10000' ? '50px' : '90px', //6%
                right: '4%',
                //bottom: '10%',
                bottom: categoryData.length > 12 ? 40 : 40, //根据数据量调整底部间距
                containaLabel: true
            },
            xAxis: {
                type: 'category',
                data: categoryData,
                axisLabel: {
                    interval: 0, //强制显示所有标签
                    rotate: categoryData.length > 12 ? 45 : 0 //如果标签重叠可以旋转
                },
                axisLine: {
                    lineStyle:{
                       color: '#999',
                    }
                }
            },
            yAxis: {
                //name: bt.dict("common.MoneyType", bt.dicts.get("moneyType").val()),
                type: 'value',
                splitNumber: 5,
                axisLabel: {
                    formatter: '{value}'

                },
                axisLine: {
                    lineStyle:{
                       color: '#999',
                    }
                },
                /*max: yMaxValue,
                interval: yInterval,*/
                splitLine: {
                    show: true,
                    lineStyle: {
                        type: 'dashed'
                    }
                }
            },
            series: [
                {
                    name: legendData[0],
                    type: 'bar',
                    barWidth: '10%',
                    data: writeOffBalData
                },
                {
                    name: legendData[1],
                    type: 'bar',
                    barWidth: '10%',
                    data: contractMoney
                },
                {
                    name: legendData[2],
                    type: 'line',
                    yAxisIndex: 0,
                    data: receivableMoney,
                    lineStyle: {
                        width: 3,
                        //color: '#3558E1'
                    },
                    symbol: 'circle',
                    symbolSize: 8
                },
                {
                    name: legendData[3],
                    type: 'line',
                    yAxisIndex: 0,
                    data: writeOffAmountData,
                    lineStyle: {
                        width: 3,
                        //color: '#7D5BFD'
                    },
                    symbol: 'diamond',
                    symbolSize: 8
                },
            ]
            };

        if(categoryData.length > 12){
            option.dataZoom = [{
                type: 'slider',
                xAxisIndex: 0,
                start: 0,
                end: Math.round((12 / categoryData.length) * 100), //初始显示12个月
                height: 12,
                bottom: 0,
                show: false,//初始隐藏
                handleSize: 0, //隐藏手柄
                handleStyle: {
                    color: 'transparent'
                },
                backgroundColor: 'rgba(0,0,0,0)',
                dataBackground:{
                    lineStyle: {
                        opacity: 0
                    },
                    areaStyle: {
                        opacity: 0
                    }
                },
                fillerColor: 'rgba(200,200,200,0.2)',
                borderColor: 'transparent',
                brushSelect: false,
                zoomLock: true //禁止缩放
            }]
            //调整网格底部间距
            option.grid.bottom = 40;
        }
        return option;
    }

//鼠标移入显示滚动条
myChartArTrendAnalysis.getDom().addEventListener('mouseover', function(){
    if(categoryData && categoryData.length > 12){
        var option = myChartArTrendAnalysis.getOption();
        option.dataZoom[0].show = true;
        option.dataZoom[0].handleSize = 8; //显示手柄
        myChartArTrendAnalysis.setOption(option);
    }
});

//鼠标移除隐藏滚动条
myChartArTrendAnalysis.getDom().addEventListener('mouseout', function(){
    if(categoryData && categoryData.length > 12){
        var option = myChartArTrendAnalysis.getOption();
        option.dataZoom[0].show = false;
        option.dataZoom[0].handleSize = 0; //隐藏手柄
        myChartArTrendAnalysis.setOption(option);
    }
});

    //应收款占比分析_chart
    function initArRatioBar(category){
        var data = getParentQueryFields();
        data.category = category;
        var url = basePath + "/bi/receivables/queryArRatio";
        ajaxRequest(url, 'POST', data)
            .success(result => {
            if(result.length > 0){
                if(myChartArRatio.isDisposed){ //检查是否已被销毁
                    myChartArRatio = echarts.init(document.getElementById('chartArRatio')); //再初始化
                }
                var option = initArRatioChart(result, category);
                myChartArRatio.setOption(option, true);
                myChartArRatio.resize();
            }else{
                myChartArRatio.dispose(); //先销毁
                var html = '<div class="article-empty" style="text-align: center;" ><img src="'+ basePath +'/static/v3.2/images/pic_blank.png" class="pic-bank"><div class="empty-text">'+bt.lang("common.msg.temporarily_no_data")+'</div></div>'
                $('#chartArRatio').html(html);
            }
        }); 
    };
    // initArRatioBar("byWriteOffBal");
    function initArRatioChart(result, category){
        var companyNames = result.map(obj => obj["externalName"]);
        var legendName = category === 'byWriteOffBal' ? ['合同金额','应收款余额','应收账款比例 '] : ['合同金额','逾期金额','逾期账款比例 '];
        var wrappedNames = companyNames.map(name => {
            if(name.length > 6){
                return name.substring(0,6) + '...';
            }
            return name;
        });
        //逾期维度下为逾期金额, 应收余额维度下为应收余额
        var writeOffBal = category === 'byWriteOffBal' ? result.map(obj => obj["writeOffBal"].toFixed(2)) : result.map(obj => obj["overDueAmount"].toFixed(2));
        var contractMoney = result.map(obj => obj["contractMoney"].toFixed(2));
        //比例转数字，用于显示
        var rateNum = result.map(obj => Number(obj["rate"].replace('%','')) / 100);
        var option = {
            color: ['#C70D0D','#FF7F03','#8A6BFE'],
            tooltip:{
                trigger: 'axis',
                backgroundColor: '#ffffff',
                borderColor: '#ddd',
                borderWidth: 1,
                confine: true, //关键配置：将tooltip限制在图表区域内
                padding: 10,
                textStyle: {
                    color: '#000000',
                    fontSize: 12,
                    fontWeight: 'normal'
                },
                //extraCssText: 'box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);', //添加阴影效果
                axisPointer: {
                    type: 'shadow'
                },
                formatter: function(params){
                    var index = params[0].dataIndex;
                    var item = result[index];
                    if(category === 'byWriteOffBal'){
                        return getWriteOffBalTooltipHtml(item);
                    }else{
                        return getOverDueAmountTooltipHtml(item);
                    }
                }
            },
            legend: {
                data: legendName,
                left: 0,
                top: 20,
                 itemWidth:10,
                 itemHeight:10
            },
            grid: {
                left: '90px',
                right: '20px',
                bottom: '20px',
                containaLabel: true
            },
            xAxis:{
                type: 'value',
                axisLine:{
                    show: true
                },
                axisTick:{
                    show: false
                },
                splitLine:{
                    lineStyle:{
                        type: 'dashed'
                    }
                },
                axisLine: {
                    lineStyle:{
                       color: '#999',
                    }
                }
            },
            yAxis:{
                type: 'category',
                data: companyNames,
                axisLabel:{
                    interval: 0,
                    formatter: function(value){
                        if(value.length > 6){
                            return value.substring(0,6) + '...';
                        }
                        return value;
                    }
                },
                axisLine:{
                    show: false
                },
                axisTick: {
                    show: false
                }
            },
            series: [
                {
                    name: legendName[0],
                    type: 'bar',
                    data: contractMoney
                },
                {
                    name: legendName[1],
                    type: 'bar',
                    data: writeOffBal
                },
                {
                   name: legendName[2],
                   type: 'line',
                   data: rateNum
                }
            ],
            dataZoom:[
                {
                    type: 'slider',
                    zoomLock: true, //禁止缩放
                    yAxisIndex: 0,
                    start: 30,
                    end: 100,
                    width: 15, //滑块宽度
                    right: 5, //距离右侧距离
                    top: '10%', //顶部位置
                    bottom: '10%', //底部位置
                    showDetail: false,
                    handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4c19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
                    handleSize: '80%',
                    handleStyle:{
                        color: '#fff',
                        shadowBlur: 3,
                        shadowColor: 'rgba(0,0,0,0.6)',
                        shadowOffsetX: 2,
                        shadowOffsetY: 2
                    },
                    fillerColor: 'rgba(0,0,0,0.2)',
                    backgroundColor: '#eee',
                    dataBackground:{
                        lineStyle:{
                            color: '#bbb'
                        },
                        areaStyle:{
                            color: '#eee'
                        }
                    },
                    brushSelect: false
                },
                {
                    type: 'inside',
                    yAxisIndex: 0,
                    zoomOnMouseWheel: true,
                    moveOnMouseMove: true
                }
            ]
        };
        return option;
    };

    function getWriteOffBalTooltipHtml(item){
        var creditRating = item.creditRating == null ? '' : item.creditRating;
        return `
            <table style="width: 140px; border-collapse: collapse; font-size: 12px;">
                <tr>
                    <td style="padding: 2px 5px;"><strong>${item.externalName}</strong></td>
                </tr>
                <tr>
                    <td style="padding: 2px 5px;">合同金额</td>
                    <td style="padding: 2px 5px;">${(item.contractMoney).toFixed(2)}</td>
                </tr>
                <tr>
                    <td style="padding: 2px 5px;">应收款余额</td>
                    <td style="padding: 2px 5px;">${(item.writeOffBal).toFixed(2)}</td>
                </tr>
                <tr>
                    <td style="padding: 2px 5px;">应收账款比例</td>
                    <td style="padding: 2px 5px;">${item.rate}</td>
                </tr>
                <tr>
                    <td style="padding: 2px 5px;">客商信用等级</td>
                    <td style="padding: 2px 5px;">${creditRating}</td>
                </tr>
            </table>
        `;
    }
    function getOverDueAmountTooltipHtml(item){
        var creditRating = item.creditRating == null ? '' : item.creditRating;
        return `
            <table style="width: 140px; border-collapse: collapse; font-size: 12px;">
                <tr>
                    <td style="padding: 2px 5px;"><strong>${item.externalName}</strong></td>
                </tr>
                <tr>
                    <td style="padding: 2px 5px;">合同金额</td>
                    <td style="padding: 2px 5px;">${(item.contractMoney).toFixed(2)}</td>
                </tr>
                <tr>
                    <td style="padding: 2px 5px;">逾期金额</td>
                    <td style="padding: 2px 5px;">${(item.overDueAmount).toFixed(2)}</td>
                </tr>
                <tr>
                    <td style="padding: 2px 5px;">逾期账款比例</td>
                    <td style="padding: 2px 5px;">${item.rate}</td>
                </tr>
                <tr>
                    <td style="padding: 2px 5px;">客商信用等级</td>
                    <td style="padding: 2px 5px;">${creditRating}</td>
                </tr>
            </table>
        `;
    }

    //应收款占比分析－按应收余额统计
    $("#byWriteOffBalBut").on("click",function () {
        $("#byWriteOffBalBut").addClass("active");
        $("#byOverDueAmountBut").removeClass("active");
        initArRatioBar("byWriteOffBal");
    });
    //应收款占比分析－按逾期余额统计
    $("#byOverDueAmountBut").on("click",function () {
        $("#byOverDueAmountBut").addClass("active");
        $("#byWriteOffBalBut").removeClass("active");
        initArRatioBar("byOverDueAmount");
    });

    myChartArRatioDom.addEventListener('mousemove', function(e){
        //获取鼠标位置
        var rect = myChartArRatioDom.getBoundingClientRect();
        var mouseX = e.clientX - rect.left;
        var mouseY = e.clientY -rect.top;
        //判断是否在Ｙ轴标签区域
        var isInYAxisLabelArea = mouseX < 100;
        if(isInYAxisLabelArea){
            //获取Ｙ轴模型
            var yAxisModel = myChartArRatio.getModel().getComponent('yAxis');
            var yAxis = yAxisModel.axis;
            //获取Ｙ轴范围
            var axisExtent = yAxis.getExtent();
            var axisLength = Math.abs(axisExtent[1] - axisExtent[0]);
            //获取所有刻度
            var ticks = yAxis.scale.getTicks();
            var labelInterval = axisLength / (ticks.length - 1);
            //计算鼠标位置对应的刻度索引
            var pointInGrid = myChartArRatio.convertFromPixel('grid', [mouseX, mouseY]);
            if(!pointInGrid) return;

            var currentLabel = yAxis.scale._data[pointInGrid[1]];
            if(currentLabel != null){
                customerNameTooltip.innerHTML='<span>'+currentLabel+"</span>";
                customerNameTooltip.style.left = (mouseX + 50) + 'px';
                customerNameTooltip.style.top = (mouseY + 20) + 'px'
                customerNameTooltip.style.display = 'block';
            }
        }else{
            customerNameTooltip.style.display = 'none';
        }
    });

    customerNameTooltip.addEventListener('mouseleave', function(){
        customerNameTooltip.style.display = 'none';
    });


    //应收款业务类型分布_chart
   function initArDocTypeDistBar (){
       var data = getParentQueryFields();
       var url = basePath + "/bi/receivables/queryArDocTypeDist";
       ajaxRequest(url, 'POST', data)
           .success(result => {
           if(result.length > 0){
               if(myChartArDocTypeDist.isDisposed){ //检查是否已被销毁
                   myChartArDocTypeDist = echarts.init(document.getElementById('chartArDocTypeDist')); //再初始化
               }
               var option = initArDocTypeDistChart(result);
               myChartArDocTypeDist.setOption(option, true);
               myChartArDocTypeDist.resize();
           }else{
               myChartArDocTypeDist.dispose(); //先销毁
               var html = '<div class="article-empty" style="text-align: center;" ><img src="'+ basePath +'/static/v3.2/images/pic_blank.png" class="pic-bank"><div class="empty-text">'+bt.lang("common.msg.temporarily_no_data")+'</div></div>'
               $('#chartArDocTypeDist').html(html);
           }
       });
   };
   function initArDocTypeDistChart(result){
       /*var arDocTypeData = [
            { value: 48.20, name: '预付款项'},
            { value: 23.02, name: '展示销售发票'},
            { value: 19.61, name: '费用报销应收单'},
            { value: 6.61, name: '应收账款调整单'},
            { value: 5.28, name: '其他应收单'}
       ];*/
       legendName = result.map(obj => obj["name"]);
       //创建颜色映射函数
       var getLineColor = function(dataIndex){
            return result[dataIndex].itemStyle.normal.color;
       };
       var option = {
            color: ['#C70D0D','#3558E1','#FF860C','#7D5BFD','#1EA05A'],
            tooltip: {
                trigger: 'item',
//                trigger: 'axis',
                backgroundColor: '#ffffff',
                borderColor: '#ddd',
                borderWidth: 1,
                //padding: 10,
                textStyle: {
                    color: '#000000',
                    fontSize: 12,
                    fontWeight: 'normal'
                },
                //extraCssText: 'box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);', //添加阴影效果
                axisPointer: {
                    type: 'shadow'
                },
                formatter: '{b}: {d}%'
            },
            grid:{
                left: '5%',
                right: '5%',
                bottom: '5%'
            },
            legend: {
                orient: 'vertical',
                right: '5%',
                itemGap: 30,
                top: 'center',
                textStyle: {
                    rich: {
                        name_css: {width: 100},
                        value_css: {align: 'right',width: 60}
                    }
                },
                data: legendName,
                formatter: function(name){
                    var data = option.series[0].data;
                    var item = data.find(item => item.name === name);
                    //var value = item.value.toFixed(2);
                    return `{name_css|${name}}` + `{value_css|${item.value}` +'%'+`}`;
                },
                itemWidth:10,
                itemHeight:10
            },
            series: [
                {
                    name: '应收款单据类型',
                    type: 'pie',
                    center: ['30%','50%'],
                    radius: '70%',
                    avoidLabelOverlap: false,
                    label: {
                        normal: {
                            show: true,
                            position: 'outside',
                            formatter: '{d}%'
                        },
                        emphasis: {
                            show: true,
                            fontSize: 16,
                            fontWeight: 'bold'
                        }
                    },
                    labelLine: {
                        normal: {
                            show: true,
                            length: 15,
                            length2: 25,
                            smooth: 0.2,
                            lineStyle: {
                                width: 1,
                                color: function(params){
                                    return getLineColor(params.dataIndex);
                                }
                            }
                        },
                        emphasis: {
                            show: true
                        }
                    },
                    data: result
                }
            ]
       };
       return option;
   }

   //账龄分布图_chart
    function initAgingDistChartBar(){
        var data = getParentQueryFields();
       var url = basePath + "/bi/receivables/queryArAgingDist";
       ajaxRequest(url, 'POST', data)
           .success(result => {
           if(result.length > 0){
               if(myChartAgingDist.isDisposed){ //检查是否已被销毁
                   myChartAgingDist = echarts.init(document.getElementById('chartAgingDistribution')); //再初始化
               }
               var option = initAgingDistChart(result);
               myChartAgingDist.setOption(option, true);
               myChartAgingDist.resize();
           }else{
               myChartAgingDist.dispose(); //先销毁
               var html = '<div class="article-empty" style="text-align: center;" ><img src="'+ basePath +'/static/v3.2/images/pic_blank.png" class="pic-bank"><div class="empty-text">'+bt.lang("common.msg.temporarily_no_data")+'</div></div>'
               $('#chartAgingDistribution').html(html);
           }
       });
   }
   // initAgingDistChartBar();
   function initAgingDistChart(result){

       var currentParams = null;
       /*var detailData = {
            '0~30天':  {receivableMoney: 154998.71, writeOffBal: 34512.33, notDueAmount: 222496.27},
            '31~60天': {receivableMoney: 87654.32, writeOffBal: 12345.67, notDueAmount: 98765.43},
            '61~90天': {receivableMoney: 54321.12, writeOffBal: 9876.54, notDueAmount: 65432.10},
            '90天以上': {receivableMoney: 31209.45, writeOffBal: 7654.32, notDueAmount: 43210.98}
       };*/

       var agingData = {};
       var defaultNullValue = { writeOffBal: 0};
       agingData['0~30天'] = result.find(item => item.agingDay === 'thirtyDay') || defaultNullValue;
       agingData['31~60天'] = result.find(item => item.agingDay === 'sixtyDay') || defaultNullValue;
       agingData['61~90天'] = result.find(item => item.agingDay === 'ninetyDay') || defaultNullValue;
       agingData['90天以上'] = result.find(item => item.agingDay === 'overNinetyDay') || defaultNullValue;

       var chartData = [];
       Object.entries(agingData).forEach(([key,value]) => {
         var writeOffBal = Number(`${value.writeOffBal}`).toFixed(2);
         var obj = {name: `${key}`, value: writeOffBal};
         chartData.push(obj);
       });
       var legendName = ['0~30天','31~60天','61~90天','90天以上'];
       var legendColor = ['#C70D0D','#2245CC','#FF7F03','#7D5BFD']
       var option = {
        color: legendColor,
        tooltip: {
        show: false,
        trigger: 'item'
        },
        grid:{
            left: '5%',
            right: '5%',
            bottom: '5%'
        },
        legend: {
            orient: 'vertical',
            right: '10%',
            itemGap: 30,
            itemWidth:10,
            itemHeight:10,
            top: 'center',
            data: legendName
        },
        series: [
        {
            name: '账龄分布',
            type: 'pie',
            center: ['30%','50%'],
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
                normal: {
                    show: false,
                    position: 'center'
                }
            },
            emphasis: {
                normal: {
                    label: {
                        show: false,
                        fontSize: '18',
                        fontWeight: 'bold'
                    }
                }
            },
            labelLine: {
                normal: {
                    show: false
                }
            },
            data: chartData
            }
        ]
       };

       otherEvent(option, agingData);
       return option;
   }

   function formatTooltip(params, agingData){
        currentParams = params;
        var data = agingData[params.name];
        function formatMoney(num){
            return Number(num).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
        }

        return [
'<div style="display:flex;align-items:center;margin-bottom:5px;>',
    '<div class="layui-row layui-col-space10">',
        '<div class="layui-col-xs4">',
            '<div style="width:12px;height:12px;background-color:' + params.color + ';margin-right:8px;border-radius:2px;"></div>',
        '</div>',
        '<div class="layui-col-xs8">',
            '<span style="font-size:14px">'+ params.name + '</span>',
        '</div>',
    '</div>',
    '<div class="layui-row layui-col-space5">',
        '<div class="layui-col-xs6">',
            '<span style="font-size:14px">应收余额</span>',
        '</div>',
        '<div class="layui-col-xs6">',
            '<span style="font-weight:bold;">' + formatMoney(data.writeOffBal) + '</span>',
        '</div>',
    '</div>',
    /*'<div class="layui-row layui-col-space5">',
        '<div class="layui-col-xs6">',
            '<span style="font-size:14px">已核销金额</span>',
        '</div>',
        '<div class="layui-col-xs6">',
            '<span style="font-weight:bold;">' + formatMoney(data.writeOffBal) + '</span>',
        '</div>',
    '</div>',
    '<div class="layui-row layui-col-space5">',
        '<div class="layui-col-xs6">',
            '<span style="font-size:14px">未到期金额</span>',
        '</div>',
        '<div class="layui-col-xs6"></div>',
            '<span style="font-weight:bold;">' + formatMoney(data.notDueAmount) + '</span>',
        '</div>',
    '</div>',*/
    '<div class="layui-row layui-col-space5">',
        '<div class="layui-col-xs12">',
            '<div id="tooltipLink">',
                '<span style="font-weight:bold;">客商单据详情>></span>',
            '</div>',
        '</div>',
    '</div>',
'</div>'
        ].join('');
   }

   function otherEvent(option, agingData){
        //　改进的鼠标事件处理
        var isMouseOnChart = false;
        var isMouseOnTooltip = false;

        myChartAgingDom.addEventListener('mouseenter', function() {
          isMouseOnChart = true;
        });

        myChartAgingDom.addEventListener('mouseleave', function() {
          isMouseOnChart = false;
        });

        myChartAgingDist.getZr().on('mouseover', function(params){
          if(params.topTarget && params.topTarget.type === 'sector'){
              var seriesIndex = params.topTarget.seriesIndex;
              var dataIndex = params.topTarget.dataIndex;
              var series = myChartAgingDist.getModel().getSeries()[seriesIndex];
              var data = series.getData();
              var rawData = data.getRawDataItem(dataIndex);
              var colors = option.color;
              var toolTipHtml = formatTooltip({
                  seriesType: 'pie',
                  name: rawData.name,
                  value: rawData.value,
                  dataIndex: dataIndex,
                  seriesIndex: seriesIndex,
                  color: colors[dataIndex]
              }, agingData);
              myAgingCustomTooltip.innerHTML = toolTipHtml;

              myAgingCustomTooltip.style.left = (params.offsetX + 10) + 'px';
              myAgingCustomTooltip.style.top = (params.offsetY + 10) + 'px';
              myAgingCustomTooltip.style.display = 'block';
          }
        });

        myAgingCustomTooltip.addEventListener('mouseenter', function(){
          isMouseOnTooltip = true;
        });

        myAgingCustomTooltip.addEventListener('mouseleave', function(){
         isMouseOnTooltip = false;
         if(!isMouseOnChart){
              myAgingCustomTooltip.style.display = 'none';
         }
        });

        myAgingCustomTooltip.addEventListener('click', function(e){
          if(e.target.parentNode.id === 'tooltipLink' && currentParams){
              var src = basePath + '/accountReceivable/agingAnalysis/list';
              bt.navtab.addNavtab(bt.lang('应收账龄分析表'), src)

          }
        });
    }

//跳转
$('.clickable').on('click', function() {
    var action = $(this).data('action');
    switch(action){
        case 'writeOffBal':
            bt.navtab.addNavtab(bt.lang('应收查询'), basePath + "/accountReceivable/query/docManage/list" );
            break;
        case 'overdueAmount':
            bt.navtab.addNavtab(bt.lang('逾期催收登记表'), basePath + "/accountReceivable/query/docOverdueNotice/list" );
            break;
        case 'contractMoney':
            bt.navtab.addNavtab(bt.lang('应收账龄分析表'), basePath + "/accountReceivable/agingAnalysis/list?type=byContract" );
            break;
        default:
            console.log('error action', action);
    }
})

});
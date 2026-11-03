bt.use(['form', 'datatable', 'dateType', 'selectType', 'corp' ,'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.corp.init();
    selectType = bt.selectType.init();
    dateTypes = bt.dateType.init();
    dicts = bt.dicts.init();//字典

    form.on('submit(excel)', function(data) {
    	var url = basePath+"/cms/report/cmsbalancegather/excel";
	   	downLoadFile({url:url,data:data.field,method:'get'});
		return false;
    });
    
    var downLoadFile = function (options) {
	    var config = $.extend(true, { method: 'get' }, options);
	    var $iframe = $('<iframe id="down-file-iframe" />');
	    var $form = $('<form target="_self" method="' + config.method + '" />');
	    $form.attr("action",config.url);
	    for (var key in config.data) {
            $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
        }
	    $iframe.append($form);
	    $(document.body).append($iframe);
	    $form[0].submit();
	    $iframe.remove();
	}
});

function submitQuery(){
   	$("#searchForm").attr("action",basePath+"/cms/report/cmsbalancegather/gatherList");
   	$("#searchForm").submit();
}

function initPie(corpId,curId) {
	var myChartPie = echarts.init(document.getElementById('chartPie'));
    myChartPie.showLoading();
    //通过Ajax获取数据
    var url = basePath + "/cms/report/cmsbalancegather/gatherPieList";
    $.ajax({
        type: "post",
        async: false, //同步执行
        url: url,
        data: {"corpId":corpId,"curId":curId},
        dataType: "json", //返回数据形式为json
        success: function (result) {
            var option = initChartPie(result.legend,  result.series ,result.title);
            myChartPie.hideLoading();
            myChartPie.setOption(option, true);

           /*  function open(param) {
                // 获取跳转的参数
                var corpIndex = Math.ceil((param.dataIndex + 1) / 4);
                var keyParam = result.xAxisData2[corpIndex - 1] + param.name + param.seriesName;
                var openParam = (result.openParams)[keyParam].split(",");
                var openUrl = basePath + "/report/allocation/open?corpId=" + openParam[0] + "&voucherFrom=" + openParam[1] + "&curId=" + openParam[2];
                if (bt.navtab) {
                    bt.navtab.addNavtab(bt.lang('table.nisAllocationReport.list'), openUrl);
                    return;
                }
            } 
            myChartPie.on('click', open);*/
        }
    });
}

function loadCorpCurData(corpId,curId){
	//initPie(corpId,curId);
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/report/cmsbalancegather/gatherViewPir?corpId=" + corpId + "&curId=" + curId);
        return;
    }
}

function loadDetail(corpId,curId,loanSign){
	var start = dateTypes.get("startDate").val();
	var end = dateTypes.get("endDate").val();
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/report/cmsbalancegather/detailList?corpId=" + corpId + "&loanSign=" + loanSign + "&curId=" + curId+"&startDate=" + start + "&endDate="+ end);
        return;
    }
}

function initChartPie(legend,  series ,title) {
	option = {
		 	title: title,
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)",
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                data: legend,
                bottom: 10,
                left: 'center',
            },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
	    	calculable : false,
	    	series: JSON.parse(series)
	   	};
	return option;
}

function initChartLine(legend,  series) {
	option = {
		 	title: [{
                show: true,
                text: "line",
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
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
	    	calculable : false,
	    	series: JSON.parse(series)
	   	};
	return option;
}

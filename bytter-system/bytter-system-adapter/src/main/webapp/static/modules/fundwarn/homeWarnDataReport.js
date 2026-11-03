var corp,dicts,dateType,warnDataTable,selectType;
bt.use(['form','datatable','corp','select','dicts','dateType','selectType'], function(){
    $ = bt.jquery,form = bt.form();
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

    initDimensionCode = function (data) {
    	dicts.get('dimensionCode').attr({'data-displayData':typecodeDimensions[data.value]});
    };
    initWarnDataTable = function (data) {
    	var typeCode=dicts.get('typeCode').val();
    	var dimensionCode=dicts.get('dimensionCode').val()
    	if(typeCode && dimensionCode
    		 && $.trim(typeCode) && $.trim(dimensionCode)){
			initDataTable();
    	}
    };
    corp=bt.corp.init();
    dicts=bt.dicts.init();
    dateType=bt.dateType.init();
    selectType = bt.selectType.init();

	//设置定时任务刷新：5分钟刷新一次（300000）
	/*window.setInterval(function(){
		initWarnDataBar();
	}, 300000);*/

	//生成列表
	warnDataTable = bt.datatable.init({
		elem: "#appTable",
		dom: 'Brtlip',
		colReorder: false,
		query: "search",
		ordering:false,
		checkbox: false,
		scrollY:	'240px',
		ignoreButtons: ['state'],
		lengthMenu: [5, 10, 50, 100, 500],//每页显示多少条
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search",
			callback:function(data) {
				initWarnDataBar();
			}
		},
		exportFile:{
			outputTitle:bt.lang('repot.bankAccBal.warnDataTitle'),
			outputFilename: bt.lang('repot.bankAccBal.warnDataTitle'),
		},
		ajax: {
			'url': basePath + "/fundwarn/btwarndata/datatable"
		},
		columns: [
			{data: "corpName", bSortable: false},
			{data: "typeName", bSortable: false},
			{data: "dimensionName", bSortable: false},
			{
				data: "warnLevel", bSortable: true, className: "center",
				render: function (data, type, full, meta) {
					return bt.dict("btwarnstrategy.level", full.warnLevel);
				}
			},
			{data: "warnDate", bSortable: true, className: "center"},
			{data: "createDate", bSortable: true, className: "center"}
		]
	});

	myChartWarnData = echarts.init(document.getElementById('chartWarnData'));
	initWarnDataBar = function () {
		var data = {
			corpId: bt.corp.get("corpId").val(),
			typeCode: bt.dicts.get("typeCode").val(),
			dimensionCode: bt.dicts.get("dimensionCode").val(),
			beginDate: bt.dateType.get("ged_warnDate").val(),
			endDate: bt.dateType.get("led_warnDate").val()
		};
		//通过Ajax获取数据
		var url = basePath + "/fundwarn/btwarndata/createWarnDataBar";
		$.ajax({
			type: "post",
			async: true, //同步执行
			url: url,
			data: data,
			dataType: "json", //返回数据形式为json
			success: function (result) {
				var option = initWarnDataChart(result);
				myChartWarnData.setOption(option, true);
				myChartWarnData.resize();
			}
		});
	};
	initWarnDataBar();

	window.onresize = function () {
		myChartWarnData.resize()
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
	function initWarnDataChart(result) {
		var option = {
			color: ['#d2585d','#ed955a', '#4e6dec'],
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
				left:'80',
				right:'80',
				bottom:'30'
			},
			legend: {
				data: [bt.dict("btwarnstrategy.level", 2), bt.dict("btwarnstrategy.level", 1), bt.dict("btwarnstrategy.level", 0)],
				textStyle:{
					color: $('#barColor').val(),
				}
			},
			xAxis: [
				{
					type: 'category',
					axisLine: {
						lineStyle:{
							color: $('#barColor').val(),
						}
					},
					boundaryGap: false,
					data: $.parseJSON(result.xData),
					axisPointer: {
						type: 'shadow'
					},
					splitLine:{
						show: false
					}
				}
			],
			yAxis : [
				{
					name: bt.lang('table.bankAccBalTrend.corpId')+"："+bt.lang('cms.load.count'),
					type : 'value',
					axisLine: {
						lineStyle:{
							color: $('#barColor').val(),
						}
					},
					minInterval : 1,
					splitLine:{
						show: false
					}
				}
			],
			series: [
				{
					name: bt.dict("btwarnstrategy.level", 2),
					type: 'line',
					tooltip: {
						formatter: function (value) {
							return value + bt.lang('cms.load.count');
						}
					},
					data: $.parseJSON(result.warnLevel2)
				},
				{
					name: bt.dict("btwarnstrategy.level", 1),
					type: 'line',
					tooltip: {
						formatter: function (value) {
							return value + bt.lang('cms.load.count');
						}
					},
					data: $.parseJSON(result.warnLevel1)
				},
				{
					name: bt.dict("btwarnstrategy.level", 0),
					type: 'line',
					tooltip: {
						formatter: function (value) {
							return value + bt.lang('cms.load.count');
						}
					},
					data: $.parseJSON(result.warnLevel0)
				}
			]
		};
		return option;
	}
});

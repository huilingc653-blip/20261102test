bt.use(['form','datatable','dicts','mulselect','dateType','corp'], function(){
    $ = bt.jquery,form = bt.form();
	bt.dateType.init();
	bt.dicts.init();
	bt.mulselect.init();
	bt.corp.init();

	//生成列表
	bt.datatable.init({
		elem: "#appTable",
		colReorder: true,
		query: "search",
		checkbox:false,
		order: [[5,'desc']],
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath+"/credit/evaluationAnalysis/datatable"
		},
		buttons: [
			{
				extend: "colvis",
				text: bt.lang('common.button.theadHide'),
				columns: ':not(:first):not(:last)'
			}
		]
		,columns: [
			{data:"taskNo",bSortable:false,className:"center"},
			{data:"taskName",bSortable:false,className:"center"},
			{data: "corpName", bSortable: false, className: "center",render:function (data, type, full, meta) {
					return '<div class="table-ellipsis" title="'+data+'">'+data+'</div>'
				}},
			{data:"orgType",bSortable:false,className:"center"},
			{data: "orgName", bSortable: false, className: "center",render:function (data, type, full, meta){
					return '<div class="table-ellipsis" title="'+data+'">'+data+'</div>'
				}},
			{data:"evaluateStartDate",bSortable:true,className:"center",
				render: function (data, type, full, meta) {
					if(full.evaluateStartDate){
						return bt.date.format(full.evaluateStartDate, 'yyyy-MM-dd');
					}
					return "";
				}
			},
			{data:"evaluateEndDate",bSortable:true,className:"center",
				render: function (data, type, full, meta) {
					if(full.evaluateEndDate){
						return bt.date.format(full.evaluateEndDate, 'yyyy-MM-dd');
					}
					return "";
				}
			},
			{data:"taskCycle",bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					if(full.taskCycle){
						return bt.dict("credit.crTaskInfo.cycle",full.taskCycle);
					}
					return "";
				}
			},
			{data:"evaluateBy",bSortable:false,className:"center"},
			{data:"status",bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					return bt.dict("table.crTaskCorpInfo.status",full.status);
				}
			},
			{data:null,bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					var html = '';
					if(full.status  == 0 || full.status == -1){
						html = '<a class="" href="#" data-row="' + meta.row + '" onclick="evaluation(this)">' + bt.lang("evaluationAnalysis.button.evaluate") + '</a> ';
					}
					html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a> ';
					html += '<a class="" href="#" data-row="' + meta.row + '" onclick="queryLog(this)">' + bt.lang("common.button.logInfo") + '</a>';
					return html;
				}
			}
		]
	});

	//查看
	view = function (el) {
		var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();

		if(data){
			if(bt.navtab){
				bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/credit/evaluationAnalysis/view/" + data.id + "/" + data.taskNo + "/" + data.corpId);

			}
		}
	}
});

//评价
function evaluation(el) {
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
		if(bt.navtab){
			bt.navtab.addNavtab(bt.lang('evaluationAnalysis.button.evaluate'),basePath + "/credit/evaluationAnalysis/evaluate/" + data.taskNo + "/" + data.corpId+ "/" + data.id);

		}
	}
}

//查看日志
function queryLog(el) {
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
		if(bt.navtab){
			bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/credit/evaluationAnalysis/viewLog/" + data.id);

		}
	}
}




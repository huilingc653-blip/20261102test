bt.use(['form','datatable','dateType','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.mulselect.init();

	//生成列表
	bt.datatable.init({
		elem: "#appTable",
		colReorder: true,
		query: "search",
		checkbox:false,
		order: [[8,'desc']],
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath+"/credit/evaluationReport/datatable"
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
			{data:"orgCode",bSortable:false,className:"center"},
			{data:"corpName",bSortable:false,className:"center"},
			{data:"orgType",bSortable:false,className:"center"},
			{data:"orgName",bSortable:false,className:"center"},
			{data:"orgAvg",bSortable:false,className:"center"},
			{data:"orgEvaluateLevel",bSortable:false,className:"center"},
			{data:"evaluateStartDate",bSortable:true,className:"center",
				render: function (data, type, full, meta) {
					if(full.evaluateStartDate){
						return bt.date.format(full.evaluateStartDate, "yyyy-MM-dd");
					}
					return "";
				}
			},
			{data:"evaluateEndDate",bSortable:true,className:"center",
				render: function (data, type, full, meta) {
					if(full.evaluateEndDate){
						return bt.date.format(full.evaluateEndDate, "yyyy-MM-dd");
					}
					return "";
				}
			},
			{data:null,bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
					return html;
				}
			}
		]
	});
});

//查看
function view(el) {
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
		if(bt.navtab){
			bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/credit/evaluationReport/view/" + data.id +"/" + data.orgId);
			return;
		}
	}

}




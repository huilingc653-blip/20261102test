var appTable = {};
bt.use(['form','datatable','dicts','dateType'], function(){
	var $ = bt.jquery,form = bt.form();
	var dicts = bt.dicts.init();
	bt.dateType.init();
	//生成列表
	appTable=bt.datatable.init({
		elem: "#appTable",
		colReorder: true,
		query: "search",
		checkbox:false,
		order: [[0,'asc']],
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath+"/fundwarn/exceptionData/datatable"
		},
		buttons: [
		]
		,columns: [
			{data:"sendDate",bSortable:true},
			{data:"sendStatusName",bSortable:true},
			{data:"channelName",bSortable:true},
			{data:"warnTypeName",bSortable:true},
			{data:"warnContent",bSortable:false},
			{data:"reqMsg",bSortable:false},
			{data:"respMsg",bSortable:false},
			{
				data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
					var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
					return html;
				}
			}
		]
	});
	//时间
	$.each($("div[type='date']"), function (i, item) {
		bt.btdate.init({
			elem: "[field='" + $(item).attr("field") + "']",
			name: $(item).attr("field"),
			start: $(item).attr("defaultVal"),
			format: "YYYY-MM-DD",
			istime: false
		});
	});
	$.each($("div[type='datetime']"), function (i, item) {
		bt.btdate.init({
			elem: "[field='" + $(item).attr("field") + "']",
			name: $(item).attr("field"),
			start: $(item).attr("defaultVal"),
			format: "YYYY-MM-DD hh:mm:ss",
			istime: true
		});
	});
});

function view(el) {
	var data = appTable.row(layui.jquery(el).data("row")).data();
	var id = data.id;

	// if (bt.navtab) {
	// 	bt.navtab.addNavtab(bt.lang('common.button.view'),
	// 		basePath + "/fundwarn/exceptionData/view/" + id);
	// 	return;
	// }

	parent.layer.open({
		type: 2,
		area: ['850px', '720px'],
		maxmin: true,
		content: basePath + "/fundwarn/exceptionData/view/" + id
	});
}
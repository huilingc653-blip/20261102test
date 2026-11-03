bt.use(['form','datatable','dicts','mulselect','dateType'], function(){
    $ = bt.jquery,form = bt.form();

	bt.dicts.init();
	bt.dateType.init();
	bt.mulselect.init();

	//生成列表
	bt.datatable.init({
		elem: "#appTable",
		colReorder: true,
		query: "search",
		checkbox:false,
		order: [[0,'desc']],
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath+"/credit/evaluationLog/datatable"
		},
		buttons: [
			{
				extend: "colvis",
				text: bt.lang('common.button.theadHide'),
				columns: ':not(:first):not(:last)'
			}
		]
		,columns: [
			{data:"createDate",bSortable:true,className:"center",
				render: function (data, type, full, meta) {
					if(full.createDate){
						return bt.date.format(full.createDate, 'yyyy-MM-dd hh:mm:ss');
					}
					return "";
				}
			},
			{data:"createBy",bSortable:false,className:"center"},
			{data:"createName",bSortable:false,className:"center"},
			{data:"logType",bSortable:false,className:"center" ,render: function ( data, type, full, meta ){
					return bt.dict("creditRating.logType", full.logType);
				}},
			{data:"msg",bSortable:false,className:"center"}
		]
	});

});

//关闭窗口
function doClose() {
	if (bt.navtab) {
		bt.navtab.closeCurrent();
	}
}




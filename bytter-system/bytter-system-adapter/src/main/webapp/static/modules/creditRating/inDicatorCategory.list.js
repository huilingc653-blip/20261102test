var selectType;
bt.use(['form','datatable','selectType','date'], function(){
	$ = bt.jquery,form = bt.form();
	selectType = bt.selectType.init();
	//生成列表
	bt.datatable.init({
		elem: "#appTable",
		dom: 'Brtlip',
		colReorder: true,
		order: [[0, 'asc']],
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath + "/creditRating/indicatorCategory/dataTable"
		},
		buttons: [
			{
				text:bt.lang('common.button.add'),
				action:function (e, dt, node, config) {
					add();
				}
			},
			{
				extend: "colvis",
				text: bt.lang('common.button.theadHide'),
				columns: ':not(:first):not(:last)'
			}
		],
		fixedColumns: {
			rightColumns: 1//代表冻结右边一列
		},
		columns:[
			{data:"categoryName",bSortable:true,className:"center"},
			{data:"categoryDescription",bSortable:true,className:"center"},
			{data:"createDate",bSortable:true,className:"center",	render: function (data, type, full, meta) {
					return bt.date.format(data, "yyyy-MM-dd");
				}
			},
			{data:null,bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					var html = "<a href=\"#\" onclick=\"edit('" + full.id + "')\">" + bt.lang("common.button.edit") + "</a>"+'<span class="text-explode">|</span>';
					html += "<a href=\"#\" onclick=\"view('" + full.id + "')\">" + bt.lang("common.button.view") + "</a>"+'<span class="text-explode">|</span>';
					html += "<a href=\"#\" onclick=\"del('" + full.id + "')\">" + bt.lang("common.button.delete") + "</a>"+'<span class="text-explode">|</span>';
					return html;
				}
			}
		]
	});
});
function add() {
	if (bt.navtab){
		bt.navtab.addNavtab(bt.lang('common.add.data'),basePath+'/creditRating/indicatorCategory/add');
		return;
	}
	parent.layer.open({
		type:2,
		area:['850px','530px'],
		maxmin:true,
		content:basePath+'/creditRating/indicatorCategory/add',
		end:function () {
			table && table.draw();
		}
	});
}
function view(id) {
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/creditRating/indicatorCategory/view/" + id
	});
}
function edit(id) {
	if (bt.navtab){
		bt.navtab.addNavtab(bt.lang('common.add.data'),basePath+'/creditRating/indicatorCategory/edit?id='+id);
		return;
	}
	parent.layer.open({
		type:2,
		area:['850px','530px'],
		maxmin:true,
		content:basePath+'/creditRating/indicatorCategory/edit?id='+id,
		end:function () {
			table && table.draw();
		}
	});
}
function del(id) {
	parent.layer.confirm(bt.lang('common.message.confirm.delete'),function () {
		$.post(basePath + "/creditRating/indicatorCategory/delete/" + id,function (result) {
			bt.datatable.refreshCurrentPage();
			parent.layer.alert(result.msg,{icon: result.success?1:2});
		},'JSON');
	})
}

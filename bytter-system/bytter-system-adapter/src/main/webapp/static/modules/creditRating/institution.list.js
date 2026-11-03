bt.use(['form','datatable','dateType','mulselect', 'moneyType', 'btdate', 'corp', 'select','btImport', 'dicts','selectTree'], function(){
    $ = bt.jquery,form = bt.form();
	bt.corp.init();
	bt.dicts.init();
	bt.moneyType.init();
	bt.dateType.init();
	bt.mulselect.init();
	// bt.selectTree.init();

	//生成列表
	bt.datatable.init({
		elem: "#appTable",
		colReorder: true,
		resize:true,
		query: "search",
		checkbox:false,
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath+"/credit/rawData/institutionDatatable"
		},
		ignoreButtons: ['colvis', 'print', 'state','output'],
		buttons: [
			{
				text: bt.lang('table.institution.access'),
				hide: bt.isnotResource('fundwarn:creditRating:institution:access'),
				action: function (e, dt, node, config) {
					add();
				}
			}
		]
		,columns: [
			{data:"institutionCode",bSortable:false,className:"center"},
			{data:"institutionTypeName",bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					return bt.dict("INSTITUTIONA_TYPE", full.institutionType);
				}
			},
			{data:"institutionName",bSortable:false,className:"center",render: function (data, type, full, meta) {
					return '<div style="max-width: 400px;width: 100%;" class="table-ellipsis">'+data+'</div>'
				}},
			{data:"statusName",bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					return bt.dict("institution_status", full.status);
				}
			},
			{data:"creator",bSortable:false,className:"center"},
			{data:"createDate",bSortable:true,className:"center",
				render: function (data, type, full, meta) {
					if(full.createDate){
						return bt.date.format(full.createDate, "yyyy-MM-dd");
					}
					return "";
				}
			},
			{data:null,bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					var html = "";
					//0000-未准入;0001-准入审批中;0095-已准入, -1 审批打回 ;
					if (data.status == "000" || data.status == "-1"){
						//编辑 删除 查看 日志
						html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
						html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
					}
					if (data.status == "001"){
					//查看 日志
					}
					if (data.status == "095"){
						// 编辑
						html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
					}
					// 查看 日志
					html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
					html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="viewLog(this)">' + bt.lang("common.button.logInfo") + '</a>';
					return html;
				}
			}
		]
	});

});

/**
 * 新增
 */
function add() {
	if (bt.navtab) {
		bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/credit/rawData/addInstitution");
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/credit/rawData/addInstitution",
		end: function () {
			table && table.draw();
		}
	});
}
//编辑
function update(el) {
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if (bt.navtab) {
		bt.navtab.addNavtab(bt.lang('common.button.edit'), basePath + "/credit/rawData/modifyInstitution/" + data.id);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/credit/rawData/modifyInstitution/" + data.id
	});
}
//删除

function del(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
		bt.loading.show();
		$.post(basePath + "/credit/rawData/delInstitution/" + data.id, function(result){
			bt.loading.hide();
			bt.navtab.refreshCurrentTable();
			parent.layer.msg(result.msg, {icon: result.success?1:2});
		},'JSON');
	});
}
//查看
function view(el) {
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if (bt.navtab) {
		bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/credit/rawData/viewInstitution?id=" + data.id);
		return;
	}

	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/credit/rawData/viewInstitution?id=" + data.id
	});
}
//日志
function viewLog(el) {
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if (bt.navtab) {
		bt.navtab.addNavtab(bt.lang('common.button.logInfo'), basePath + "/credit/evaluationLog/viewLog/" + data.id);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/credit/evaluationLog/viewLog/" + data.id
	});
}



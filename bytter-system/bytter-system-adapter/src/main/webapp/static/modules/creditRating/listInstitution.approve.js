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
		query: "search",
		checkbox:true,
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath+"/credit/rawData/institutionDatatableToApprove"
		},
		ignoreButtons: ['colvis', 'print', 'state','output'],
		buttons: [
			// {
			// 	text: bt.lang('table.institution.access'),
			// 	hide: bt.isnotResource('fundwarn:creditRating:institution:access'),
			// 	action: function (e, dt, node, config) {
			// 		add();
			// 	}
			// }
		],
		fixedColumns: {
			rightColumns: 1//代表冻结右边一列
		},columns: [
			{data:"institutionCode",bSortable:false,className:"center"},
			{data:"institutionTypeName",bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					return bt.dict("INSTITUTIONA_TYPE", full.institutionType);
				}
			},
			{data:"bankTypeName",bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					return bt.dict("sys.bankType", full.bankType);
				}
			},
			{data:"institutionName",bSortable:false,className:"center"},
			{data:"statusName",bSortable:false,className:"center",
				render: function (data, type, full, meta) {
					return bt.dict("institution_status", full.status);
				}
			},
			{data:"creator",bSortable:false,className:"center"},
			{data:"createDate",bSortable:true,className:"center",
				render: function (data, type, full, meta) {
					if(full.createDate){
						return bt.date.format(full.createDate, "yyyy-MM-dd hh:mm:ss");
					}
					return "";
				}
			},
			// {data:null,bSortable:false,className:"center",
			// 	render: function (data, type, full, meta) {
			// 		var html = "";
			// 		//0000-未准入;0001-准入审批中;0095-已准入;
			// 		if (data.status == "000"){
			// 			//编辑 删除 查看 日志
			// 			html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
			// 			html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
			// 		}
			// 		if (data.status == "001"){
			// 		//查看 日志
			// 		}
			// 		if (data.status == "095"){
			// 			// 编辑
			// 			html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
			// 		}
			// 		// 查看 日志
			// 		html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
			// 		html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="viewLog(this)">' + bt.lang("common.button.logInfo") + '</a>';
			// 		return html;
			// 	}
			// }
			{data:null,bSortable:false,className:'center',
				render: function (data, type, full, meta) {
					var html='';
					if($("#flowStatus").val()=='todo'){
						html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
					}
					if($("#flowStatus").val() == 'pass'){
						html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode">|</span>';
						html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
					}else {
						html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
					}
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

function approve(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/credit/rawData/approve?id="+data.id + "&taskId=" + data.taskId);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/credit/rawData/approve?id="+data.id + "&taskId=" + data.taskId
	});
}

//日志
// function viewLog(el) {
// 	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
// 	if (bt.navtab) {
// 		bt.navtab.addNavtab(bt.lang('common.button.logInfo'), basePath + "/credit/rawData/viewLogInfo/" + data.id);
// 		return;
// 	}
// 	parent.layer.open({
// 		type: 2,
// 		area: ['850px', '530px'],
// 		maxmin: true,
// 		content: basePath + "/credit/rawData/viewLogInfo/" + data.id
// 	});
// }



bt.use(['form', 'corp', 'selectType','datatable', 'dicts', 'dateType', 'moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    var corp = bt.corp.init();
    var dicts = bt.dicts.init();
    var selectType = bt.selectType.init();
	var dateType = bt.dateType.init();
	var moneyType = bt.moneyType.getFun();

	bt.datatable.init({
		elem: "#appTable",
		dom: 'Brtlip',
		checkbox: true,
		order: [[1, 'desc']],
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath + "/finacial/interest/single/nod/datatable"
		},
		buttons: [
			{
				hide: bt.isnotResource('finacial:interest:nod:hand'),
				text: bt.lang('common.button.add'),
				action: function (e, dt, node, config) {
					add();
				}
			}, {
				extend: "colvis",
				text: bt.lang('common.button.theadHide'),
				columns: ':not(:first):not(:last)'
			}
		],
		columns: [
			{data: "corpName", bSortable: true},
			{data: "account", bSortable: true},
			{data: "accName", bSortable: true},
			{data: "curName", bSortable: true},
			{data: "startDate", bSortable: true},
			{data: "endDate", bSortable: true},
			{data: "status", bSortable: true,className: 'center',render:function (data, type, full, meta) {
					return bt.dict("interest.busStatus", data);
				}},
			{data: null, bSortable: false, className: 'center',
				render: function (data, type, full, meta) {
					var html = '';
					if (bt.isResource('finacial:interest:nod:hand', full.corpId)) {
						html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
						html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
					}
					html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
					return html;

				}
			}
		]
	});
});

function add() {
	if (bt.navtab) {
		bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/finacial/interest/single/nod/add");
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/finacial/interest/single/nod/add",
		end: function () {
			location.reload();
		}
	});
}
function view(el) {
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if (bt.navtab) {
		bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/finacial/interest/single/nod/view/" + data.id);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/finacial/interest/single/nod/view/" + data.id
	});
}

function update(el) {
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if (data) {
		if (bt.navtab) {
			bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/finacial/interest/single/nod/update?id=" + data.id);
			return;
		}
		parent.layer.open({
			type: 2,
			area: ['850px', '530px'],
			maxmin: true,
			content: basePath + "/finacial/interest/single/nod/update?id=" + data.id,
			end: function () {
				location.reload();
			}
		});
	}
}

function deleteColumn(el) {
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
		$.post(basePath + "/finacial/interest/single/nod/delete", {
			"id":data.id,
			"account":data.account,
			"netCode" : data.netId,
			"accTypeCode" : data.accType
		}, function (result) {
			bt.datatable.refreshCurrentPage();
			parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
		}, 'JSON');
	});
}

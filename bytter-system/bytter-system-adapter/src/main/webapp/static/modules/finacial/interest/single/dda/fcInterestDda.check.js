var moneyType;
var rateType;
var selectType, dicts, corp, dateType;
bt.use(['form', 'interestCorp', 'element', 'btdate', 'select','selectType','datatable', 'dicts', 'dateType', 'moneyType', 'rateType'], function () {
    $ = bt.jquery, form = bt.form();
    corp = bt.interestCorp.init();
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
	dateType = bt.dateType.init();
	moneyType = bt.moneyType.getFun();
    rateType = bt.rateType.getFun();
	dataTableInit();
});
//表格初始化
function dataTableInit() {
	//生成列表
	fcAccTable = bt.datatable.init({
		elem: "#appTable",
		dom: 'Brtlip',
		checkbox: true,
		order: [[1, 'desc']],
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath + "/finacial/interest/single/dda/datatable"
		},
		buttons: [
			{
				hide: bt.isnotResource('finacial:interest:dda:check'),
				text: bt.lang('common.button.batchCheck'),
				action: function (e, dt, node, config) {
					batchCheck();
				}
			},{
				hide: bt.isnotResource('finacial:interest:dda:check'),
				text: bt.lang('common.button.batchBack'),
				action: function (e, dt, node, config) {
					batchBack();
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
					//if (bt.isResource('finacial:interest:dda:check', full.corpId)) {
						// html += '<a class="" href="#" data-row="' + meta.row + '" onclick="check(this)">' + bt.lang("common.button.check") + '</a><span class="text-explode">|</span>';
					//}
					html += '<a class="" href="#" data-row="' + meta.row + '" onclick="check(this)">' + bt.lang("common.button.check") + '</a><span class="text-explode"></span>';
					return html;

				}
			}
		]
	});

}
//批量打回
function batchBack() {
	checkInterest("back",bt.lang("common.button.back"));//打回
}
//批量复核
function batchCheck() {
	checkInterest("check",bt.lang("common.button.check"));//复核
}
//复核/打回
function checkInterest(opType, opTips){
	var table = $('#appTable').DataTable();
	var selectRows = table.rows('.selected').data();//获取所有选中行
	var len = selectRows.length;
	if(len<1){
		layer.msg(bt.lang('common.message.confirm.selectData'));
		return;
	}
	var saveDataAry=[];
	for (var j = 0; j < selectRows.length; j++) {
		var data = {};
		data.id=selectRows[j].id;
		data.opType=opType;
		data.netId=selectRows[j].netId;
		data.accType=selectRows[j].accType;
		data.tallyDate=selectRows[j].tallyDate;
		data.endDate=selectRows[j].endDate;
		data.poolSign=selectRows[j].poolSign;
		data.account=selectRows[j].account;
		saveDataAry.push(data);
	}
	layer.confirm(bt.lang("finacial.interest.OpInterest",new Array(opTips)), {
		icon: 3,
		btn: [bt.lang("common.button.confirm"),bt.lang("common.button.cancel")]
	}, function(index, layero){
		$.ajax({
			type: "POST",
			url: basePath+"/finacial/interest/single/dda?doProcessInterestCheck",
			data: JSON.stringify(saveDataAry),
			dataType : "json",
			contentType:"application/json", // 指定这个协议很重要
			success: function(result){
				bt.datatable.refreshCurrentPage();
				parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
			}
		});
	}, function(){
	});

}
function check(el) {
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if (bt.navtab) {
		bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/finacial/interest/single/dda/check/" + data.id);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/finacial/interest/single/dda/view/" + data.id
	});
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/finacial/interest/single/dda/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/interest/single/dda/view/" + data.id
    });
}


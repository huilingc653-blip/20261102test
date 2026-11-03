var moneyType;
var rateType;
var selectType, dicts, corp, dateType;
bt.use(['form', 'element', 'btdate', 'select','selectType','datatable', 'dicts', 'dateType', 'moneyType', 'rateType','interestCorp'], function () {
    $ = bt.jquery, form = bt.form();
	corp = bt.interestCorp.init();
    dicts = bt.dicts.init();
    selectType = bt.selectType.init();
	dateType = bt.dateType.init();
	moneyType = bt.moneyType.init();
    rateType = bt.rateType.getFun();
	dataTableInit();
});

function dataTableInit() {
	//生成列表
	bt.datatable.init({
		elem: "#appTable",
		colReorder: true,
		ordering:true,
		buttonsLayout:true,
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath+"/finacial/interest/single/tda/datatableAll?isPool="+$("#isPool").val()
		}
		,columns: [
			{data:"account",bSortable:true,className:"center"},
			{data:"accName",bSortable:true,className:"center"},
			{data:"curName",bSortable:true,className:"center"},
			{data:"interestTime",bSortable:true,className:"center"},
			{data:"interestStartDate",bSortable:true,className:"center"},
			{data:"blnInstDate",bSortable:true,className:"center"},
			{data:"days",bSortable:true,className:"center"},
			{data:"cumulation",bSortable:true,className:"textRight",
				render: function(data,type,full,metd){
					var cumulation = bt.money.format(full.cumulation);
					return cumulation;
				}},
			{data:"interestRate",bSortable:true,className:"textRight",
				render : function(data,type,full,metd){
					var interestRate = bt.rateType.getFun().format(full.interestRate,'percent',4);
					return interestRate;
				}},
			{data:"interest",bSortable:true,className:"textRight",
				render : function(data,type,full,meta){
					var interest = bt.money.format(full.interest);
					return interest;
				}},
			{data:"interestSignName",bSortable:true,className:"center"},
		],
	});
}


var selectType;
var dicts;
var moneyType;
var moneyFun;
var form;
var dateType;
bt.use(['form','corp','dicts','selectType','selectTable','selectTableType','date','datatable','moneyType', 'dateType'], function(){
	$ = bt.jquery, form = bt.form();
	bt.corp.init();
	selectType = bt.selectType.init();
	dicts = bt.dicts.init();
	moneyType = bt.moneyType.init();
	moneyFun = bt.moneyType.getFun();
	dateType = bt.dateType.init();
	//生成列表
	var datatable = bt.datatable.init({
		elem: "#appTable",
		dom: 'Brtlip',
		colReorder: true,
		checkbox: false,
		isInitData:false,
		searchForm: {
			formElem: "#searchForm",
			formFilter: "search"
		},
		ajax: {
			'url': basePath + "/bi/settlementBusiness/datatable"
		},
		buttons: [
			{
				extend: "colvis",
				text: bt.lang('common.button.theadHide'),
				columns: ':not(:first):not(:last)'
			},
		],
		columns: [
			{data: "voucherName", bSortable: false, className: 'center'},
			{data: "curName", bSortable: false, className: 'center'},
			{data:"tallyNumbers",bSortable:false, className: 'center'},
			{
				data: "tallyMoney", bSortable: false, className: 'textRight',
				render: function (data, type, full, meta) {
					return bt.money.format(full.tallyMoney);
				}
			}
		],
		summary: {
			pageSum: true,
			allSum: false,
			selectSum: false,
			groupColumn:'curName',
			format: {
				decimal: 2
			},
			customSum:{
				text: function(){
					var changeCur = dicts.get("changeCur").val();
					if(changeCur=="1") {
						return bt.lang('table.fexForeign.report.convert.currency.total');
					}
					return '';
				},
				sumCallback:function(currentPageData,colName){ //汇总回调，必须要有返回值
					var result=0;
					if(currentPageData) {
						bt.each(currentPageData, function (index, item) {
							if (colName == "tallyMoney"){
								result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.tallyMoney),moneyFun.toNumber(item.curExrate)));
							} else if (colName == "tallyNumbers"){
								result = result + moneyFun.toNumber(item.tallyNumbers);
							}
						});
						if (colName == "tallyMoney") {
							return moneyFun.format(result);
						}else{
							return '';
						}
					}
				}
			},
			sumList: [
				{
					colNum: '2',
					dataName: 'tallyNumbers',
					format: 0,
					money:false
				},
				{
					colNum: '3',
					dataName: 'tallyMoney'
				}
			]
		}
	});

	//全选
	form.on('checkbox(allChoose)', function(data){
		var child = $('input[name="voucherTypeIdCheck"]');
		child.each(function(index, item){
			item.checked = data.elem.checked;
		});
		form.render('checkbox');
		setVoucherTypeId();
	});

	function setVoucherTypeId(){
		var voucherTypeIds = "";
		$('form').find('input[name="voucherTypeIdCheck"]:checked').each(function(){
			if ($(this).val()) {
				if ("" == voucherTypeIds) {
					voucherTypeIds = $(this).val();
				} else {
					voucherTypeIds += "," + $(this).val();
				}
			}
		});
		$('#voucherTypeId').val(voucherTypeIds);
	}

	//全选
	form.on('checkbox(voucherTypeIdCheck)', function(data){
		setVoucherTypeId();
	});
})


function callToCurr(data){
	if(data.value =="1"){
		selectType.get("reducedCurId").attr({'data-disabled':false,'data-required':true,'data-value':$("#rmbCur").val()});
		selectType.get("rateType").attr({'data-disabled':false,'data-required':true});
		dateType.get("rateDate").attr({'readonly':false,'data-required':true});
	}else {
		selectType.get("reducedCurId").attr({'data-disabled':true,'data-required':false});
		selectType.get("rateType").attr({'data-disabled':true,'data-required':false});
		dateType.get("rateDate").attr({'readonly':true,'data-required':false});
	}
	form.render();
	form.renderIcon();
}

/**
 * 币种转换校验
 */
function checkConvertCurCallBack() {
	var origCur = selectType.get("curId").val();
	var reducedCurId = selectType.get("reducedCurId").val();
	if (isNotEmpty(origCur) && isNotEmpty(reducedCurId)) {
		$.ajax({
			url: basePath + "/report/deposit/balance/checkConvertCurId",
			type:"post",
			data:{
				"origCurId": origCur,
				"convertCurId": reducedCurId,
				"date": bt.date.format(new Date(), 'yyyy-mm-dd')
			},
			dataType: "json",
			success:function (data) {
				if (!data.success) {
					parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
					selectType.get("reducedCurId").val("");
				}
			}
		});
	}
}

/**
 * 为空判断
 * @param stringData
 * @returns {boolean}
 */
function isEmpty(stringData) {
	if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
		return true;
	} else {
		return false;
	}
}
/**
 * 非空判断
 * @param stringData
 * @returns {boolean}
 */
function isNotEmpty(stringData) {
	return !isEmpty(stringData)
}
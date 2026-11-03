var datatable;
var selectType,mulselectType,form;
bt.use(['form','datatable','dateType','selectType','dicts','corp','page','moneyType','mulselect'],function(){
	$=bt.jquery,form=bt.form();
	bt.corp.init();
	selectType=bt.selectType.init();
	dicts=bt.dicts.init();
	mulselectType=bt.mulselect.init();
	dateType=bt.dateType.init();
	moneyType=bt.moneyType.init();
	moneyFun = bt.moneyType.getFun();
	datatable=bt.datatable.init({
		elem:"#appTable",
		colReorder: true,
	    checkbox: false,
	    isInitData : true,
	    searchForm: {
	            formElem: "#searchForm",
	            formFilter: "search"
	        },
	     ajax: {
	            'url': basePath + "/report/merchantreceipt/datatable"
	        },
	      buttons: [
	            {
	                extend: "colvis",
	                text: bt.lang('common.button.theadHide'),
	                columns: ':not(:first):not(:last)'
	            }
	        ],
	    columns:[
	         {data:"corpName",bSortable: true,className:"center"},
	         {data:"code", bSortable: true,className:"center"},
	         {data:"merchantName",bSortable: true,className:"center"},
		     {data:"abbreviate",bSortable: true,className:"center"},
		     {data:"curName",bSortable: true,className:"center"},
             {data:"payMoney",bSortable: true,className:"textRight",
		        	 render:function(data,type,full,meta){
		        		 var html = "";
		        		 if(isNotNull(full.payMoney)){
		        			 return "<a href='javaScript:void(0)' onclick='view(\""+full.corpId+"\",\""+full.id+"\",\""+full.curId+"\")'>"+moneyFun.format(full.payMoney)+'</a>';
		        		 }else{
		        			 return moneyFun.format(0);
		        		 }
		        	 }
		         },
		     {data:"payNumber",bSortable: true,className:"center"}
	      ],
		summary: {
			groupColumn: 'curName',
			pageSum: 'pageSum',
			sumTextCol: 1,  //统计名称
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
					//currentPageData　当前页面数据 colName　当前列名称
					var result=0;
					if(currentPageData) {
						bt.each(currentPageData, function (index, item) {
							if (colName == "payMoney") {
								result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.payMoney),moneyFun.toNumber(item.curExrate)));
							}
						});
					}
					return moneyFun.format(result);
				}
			},
			sumList: [
				{
					colNum: '5',
					dataName: 'payMoney'
				}
			]
		}
	    });

	$("#reset").on('click', function () {
		selectType.get("reducedCurId").attr({'data-disabled':true,'data-required':false});
		selectType.get("rateType").attr({'data-disabled':true,'data-required':false});
		dateType.get("rateDate").attr({'readonly':true,'data-required':false});
		form.render();
		form.renderIcon();
	});
	
	});


	
function view(corpId,id,curId){
	var param="";
	param+="corpId="+corpId+"&id="+id+"&curId="+curId+"&startDate="+dateType.get('startDate').val()+"&endDate="+dateType.get('endDate').val();
	 parent.layer.open({
	        type: 2,
	        area: ['1120px', '700px'],
	        maxmin: true,
	        content: basePath + "/report/merchantreceipt/view?"+param
	    });
}
	/**
	 * 判断不为空
	 * @param data
	 * @returns {*}
	 */
	var isNotNull = function (data) {
	    if(data == null || data == "null" || data == "undefined" || data.length < 1){
	        return false;
	    }else{
	        return true;
	    }
	}

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
	var curData = mulselectType.get("ins_curId").val();
	var curId="";
	if(curData) {
		bt.each(curData, function (index, item) {
			if(item.id) {
				curId = curId + item.id + ",";
			}
		});
	}
	if(!curId){
		return;
	}
	var reducedCurId = selectType.get("reducedCurId").val();
	var rateType = selectType.get("rateType").val();
	var rateDate = dateType.get('rateDate').val();
	if (isNotEmpty(reducedCurId) && isNotEmpty(rateType) && isNotEmpty(rateDate)) {
		$.ajax({
			url: basePath + "/report/allocationsettlement/checkConvertCurId",
			type:"post",
			data:{
				"origCurId":curId,
				"convertCurId": reducedCurId,
				"rateType": rateType,
				"rateDate": rateDate,
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
var datatable;
var dateType;
var selectType,mulselectType,form;
bt.use(['form','datatable','dateType','selectType','dicts','corp','page','moneyType','mulselect'],function(){
	$=bt.jquery,form=bt.form();
	bt.corp.init();
	selectType=bt.selectType.init();
	dicts=bt.dicts.init();
    dateType = bt.dateType.init();
	mulselectType=bt.mulselect.init();
	moneyType=bt.moneyType.init();
	moneyFun = bt.moneyType.getFun();
	datatable=bt.datatable.init({
		elem:"#appTable",
		colReorder: true,
	    checkbox: false,
	    isInitData : true,
	    colReorder:true,
	    searchForm: {
	            formElem: "#searchForm",
	            formFilter: "search"
	        },
	     ajax: {
	            'url': basePath + "/report/allocationsettlement/getData"
	        },
	      buttons: [
	            {
	                extend: "colvis",
	                text: bt.lang('common.button.theadHide'),
	                columns: ':not(:first):not(:last)'
	            }
	        ],
	    columns:[
	         {data:"bankType",bSortable: true,className:"left"},
	         {data:"curName", bSortable: true,className:"center"},
	         {data:"startMoneyStr",bSortable: true,className:"textRight",
		        	render:function(data,type,full,meta){
		        		 if(full.startMoneyStr == "--"){
	        		    	 return full.startMoneyStr;
	        		     }else{
	        		    	 return moneyFun.format(full.startMoneyStr);
	        		     }
		        	} 
		         },
		         {data:"getNumbers",bSortable: true,className:"center",
		        	 render:function(data,type,full,meta){
		        		 if(isNotNull(full.getNumbers)){
                             return '<a href="javaScript:void(0)" class="table-review" data-source="'+full.curId+',0,'+full.bankType+'">' + full.getNumbers + '</a>';
		        		 }else{
		        			 return 0;
		        		 }
		        	 }
		        	 },
		         {data:"borrowNumbers",bSortable: true,className:"center",
		        		 render:function(data,type,full,meta){
			        		 if(isNotNull(full.borrowNumbers)){
                                 return '<a href="javaScript:void(0)" class="table-review" data-source="'+full.curId+',1,'+full.bankType+'">' + full.borrowNumbers + '</a>';
			        		 }else{
			        			 return 0;
			        		 }
			        	 }
		        		 },
		         {data:"getMoney",bSortable: true,className:"textRight",
		        	 render:function(data,type,full,meta){
		        		 if(isNotNull(full.getMoney)){
		        			 return moneyFun.format(full.getMoney);
		        		 }else{
		        			 return moneyFun.format(0);
		        		 }
		        	 }
		         },
		         {data:"borrowMoney",bSortable: true,className:"textRight",
		        	 render:function(data,type,full,meta){
		        		 if(isNotNull(full.borrowMoney)){
		        			 return moneyFun.format(full.borrowMoney);
		        		 }else{
		        			 return moneyFun.format(0);
		        		 }
		        	 }
		         },
		         {data:"endMoneyStr",bSortable: true,className:"textRight",
		        	 render:function(data,type,full,meta){
		        		     if(full.endMoneyStr == "--"){
		        		    	 return full.endMoneyStr;
		        		     }else{
		        		    	 return moneyFun.format(full.endMoneyStr);
		        		     }
		        	 }
		         },
		         {data:"rate",bSortable: true,className:"textRight",
		        	 render:function(data,type,full,meta){
		        		 return rate=moneyFun.format(full.rate)+"%";
		        	 }
		         }
	         
	      ],
        btInitComplete:function(setting,json){ //初始化完成的回调
            $("#appTable").on('click',".table-review",function () {
                var source = $(this).data("source");
                var arr = source.split(",");
                var beginDate = dateType.get("beginDate").val();
                var endDate = dateType.get("endDate").val();
                var action = "/report/allocationsettlement/view?cdSign=" + arr[1] +"&beginDate=" + beginDate +"&endDate="+endDate +"&curId="+arr[0]+"&typeName="+arr[2];
                parent.layer.open({
                    type: 2,
                    area: ['90%','80%'],
                    maxmin: true,
                    content: basePath + action
                });
            })
        },
	      summary: {
	    	  groupColumn:'curName',
	    	  pageSum:'pageSum',
	    	  sumTextCol:1,  //统计名称
	    	  format:{
	    		  decimal:2
	    	  },
			  customSum:{
				  text:function(){
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
							  if (colName == "startMoneyStr") {
								  result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.startMoneyStr),moneyFun.toNumber(item.curExrate)));
							  }
							  if (colName == "getMoney") {
								  result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.getMoney),moneyFun.toNumber(item.curExrate)));
							  }
							  if (colName == "borrowMoney") {
								  result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.borrowMoney),moneyFun.toNumber(item.curExrate)));
							  }
							  if (colName == "endMoneyStr") {
								  result = result + moneyFun.toNumber(moneyFun.mul(moneyFun.toNumber(item.endMoneyStr),moneyFun.toNumber(item.curExrate)));
							  }
						  });
					  }
					  if (colName == "startMoneyStr" || colName == "getMoney" || colName == "borrowMoney" || colName == "endMoneyStr") {
						  return moneyFun.format(result);
					  }else{
						  return '';
					  }
				  }
			  },
	    	  sumList: [
	                    {
	                        colNum: '2',
	                        dataName: 'startMoneyStr'
	                    },
	                    {
	                        colNum: '3',
	                        dataName: 'getNumbers',
	                        format:0,
	                        money:false
	                    },
	                    {
	                        colNum: '4',
	                        dataName: 'borrowNumbers',
	                        format:0,
	                        money:false
	                    },
	                    {
	                        colNum: '5',
	                        dataName: 'getMoney'
	                    },
	                    {
	                        colNum: '6',
	                        dataName: 'borrowMoney'
	                    },
	                    {
	                        colNum: '7',
	                        dataName: 'endMoneyStr'
	                    },
	                    {
	                    	colNum: '8',
	                        dataName: 'rate',
	                        suffix:'%'
	                    }
	                ]
	    	  
	      }
	   
	      
	    });
	 
	
	
	});

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
		var curData = mulselectType.get("curId").val();
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
	
	function resetData(){
		 var inputs=$('.rCode').find('input');
		  inputs.each(function(){
		     $(this).val("");
		  })
		  bt.dateType.init().get("beginDate").val($("#beginDates").val());
		  form.render('select');
	}
	

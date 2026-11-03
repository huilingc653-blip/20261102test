var revenueDataTable;
var receivedDataTable;
var dicts={};
var rasShop={};
var corp={};
bt.use(['form','datatable','moneyType','corp','dicts','rasShop','element','dateType'], function(){
    $ = bt.jquery;
    bt.form();
    bt.dateType.init();
    bt.moneyType.init();
    corp=bt.corp.init();
    dicts=bt.dicts.init();
    rasShop=bt.rasShop.init();
    revenueDataTable = bt.datatable.initMultiTables({
        elem:"#revenueTb",
        ajax: {
            'url': basePath+"/ras/audit/manual/datatableRevenue"
        },
        searchForm: {
            formElem: "#searchForm",
        }, 
        colReorder: true,
        checkbox:true,
        buttons: [{
            extend: "colvis",
            text: bt.lang('common.button.theadHide'),
            columns: ':not(:first):not(:last)'
        }],
        columns:[
            {data:"serialNo",className:'center'},
            {data:"orderNo",className:'center'},
            {data:"corpName",className:'center'},
            {data:"shopName",className:'center'},
            {data:"payTypeName",className:'center'},
            {data:"money",className:'textRight',
            	render:function(data, type, full, meta){
                    return bt.money.format(full.money);
                }
            },
            {data:"transDate",className:'center'}
        ],
        btClickRowCallback:function(data){ //更新勾选信息
			calCheckTotalData("revenue",data);
        },
		exportFile: { //输出文档设置
			outputTitle: bt.lang('ras.revenue.dataTable'),		//设置导出标题　默认为父级选项卡内容
			outputFilename: bt.lang('ras.revenue.dataTable')		//设置导出文件名称　默认为父级选项卡内容或标题名称
		}
    });

    receivedDataTable = bt.datatable.initMultiTables({
        elem:"#receivedTb",
        ajax: {
            'url': basePath+"/ras/audit/manual/datatableReceived"
        },
        searchForm: {
            formElem: "#searchForm",
        }, 
        colReorder: true,
        checkbox:true,
        buttons: [{
            extend: "colvis",
            text: bt.lang('common.button.theadHide'),
            columns: ':not(:first):not(:last)'
        }],
        columns:[
            {data:"serialNo",className:'center'},
            {data:"orderNo",className:'center'},
            {data:"corpName",className:'center'},
            {data:"shopName",className:'center'},
            {data:"payTypeName",className:'center'},
            {data:"money",className:'textRight',
                render:function(data, type, full, meta){
                    return bt.money.format(full.money);
                }
            },
            {data:"transMoney",className:'textRight',
                render:function(data, type, full, meta){
                    return bt.money.format(full.transMoney);
                }
            },
            {data:"transDate",className:'center'},
            {data:"payeeAcc",className:'center'},
            {data:"payeeAccName",className:'center'},
            {data:"payeeBankName",className:'center'},
            {data:"dateSourceName",className:'center'}
        ],
        btClickRowCallback:function(data){ //更新勾选信息
			calCheckTotalData("received",data);
        },
		exportFile: { //输出文档设置
			outputTitle: bt.lang('ras.received.dataTable'),		// 设置导出标题　默认为父级选项卡内容
			outputFilename: bt.lang('ras.received.dataTable') 		// 设置导出文件名称　默认为父级选项卡内容或标题名称
		}
    });
});
//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}

function searchData(){
	initData("revenue");
	initData("received");
	revenueDataTable.draw();
	receivedDataTable.draw();
}

//反稽核提交按钮
function submit(data){
}

function calCheckTotalData(type,newData){
	var data = bt.datatable.getSelectData("#"+type+"Tb");
	var indexes = bt.datatable.getSelectRowIndexes("#"+type+"Tb"); //获取选中行的索引数组
    var ids="";
    var money=0;
    var shopId="";
    var payType="";
    var transDate="";
    var isAllSelectType="";
    var size=0;
    var updateCheckboxArray = [];//更新复选框对象数组
    bt.each(data,function(i,v){
    	var isOk=true;
    	var newTransDate=v.transDate;
//        if(newTransDate&&newTransDate.length>10){
//        	newTransDate=newTransDate.substr(0,10);
//    	}
        if(shopId==""||payType==""){
        	shopId=v.shopId;
        	payType=v.payType;
        	transDate=newTransDate;
        }else{
        	if(shopId!=v.shopId){
            	if(newData.length==1){
            		updateCheckboxArray.push({key:"id",value:newData[0].id,checked:false,rowIndex:indexes[i]})
            		// bt.datatable.onCheckRow("#"+type+"Tb",{key:"id",value:newData[0].id,checked:false,rowIndex:0});
	        	    layer.alert(bt.lang('ras.audit.msg.shop.neeq'), {icon: 7});
	        		return;
            	}else{
            		// bt.datatable.onCheckRow("#"+type+"Tb",{key:"id",value:v.id,checked:false,rowIndex:indexes[i]});
					updateCheckboxArray.push({key:"id",value:v.id,checked:false,rowIndex:indexes[i]})
        			isAllSelectType="shopId";
        		}
    			isOk=false;
        	}else if(payType!=v.payType){
        		if(newData.length==1){
            		// bt.datatable.onCheckRow("#"+type+"Tb",{key:"id",value:newData[0].id,checked:false,rowIndex:0});
					updateCheckboxArray.push({key:"id",value:newData[0].id,checked:false,rowIndex:indexes[i]})
	        	    layer.alert(bt.lang('ras.audit.msg.paytype.neeq'), {icon: 7});
	        		return;
        		}else{
            		// bt.datatable.onCheckRow("#"+type+"Tb",{key:"id",value:v.id,checked:false,rowIndex:indexes[i]});
					updateCheckboxArray.push({key:"id",value:v.id,checked:false,rowIndex:indexes[i]})
        			isAllSelectType="payType";
        		}
    			isOk=false;
        	}else if((payType=='001'||payType=='006')&&transDate!=newTransDate){
        		if(newData.length==1){
            		// bt.datatable.onCheckRow("#"+type+"Tb",{key:"id",value:newData[0].id,checked:false,rowIndex:0});
					updateCheckboxArray.push({key:"id",value:newData[0].id,checked:false,rowIndex:indexes[i]})
	        	    layer.alert(bt.lang('ras.audit.msg.paytype.neTransDate'), {icon: 7});
	        		return;
        		}else{
            		// bt.datatable.onCheckRow("#"+type+"Tb",{key:"id",value:v.id,checked:false,rowIndex:indexes[i]});
            		updateCheckboxArray.push({key:"id",value:v.id,checked:false,rowIndex:indexes[i]})
        			isAllSelectType="transDate";
        		}
    			isOk=false;
        	}
        }


        if(isOk){
	        if('received'!=type){
		        money=bt.money.add(money, v.money);
	        }else{
		        money=bt.money.add(money, v.transMoney);
	        }
	        if(ids!=""){
	        	ids+=",";
	        }
	        ids+=v.id;
	        size++;
        }
    });

    //统一处理复选行更新
	if ( updateCheckboxArray.length ){
        bt.datatable.onCheckRow("#"+type+"Tb",updateCheckboxArray);
	}

    if(isAllSelectType=="shopId"){
	    layer.alert(bt.lang('ras.audit.msg.shop.neeq'), {icon: 7});
    }
    if(isAllSelectType=="payType"){
	    layer.alert(bt.lang('ras.audit.msg.paytype.neeq'), {icon: 7});
    }
    if(isAllSelectType=="transDate"){
	    layer.alert(bt.lang('ras.audit.msg.paytype.neTransDate'), {icon: 7});
    }
    $("#"+type+"TransDate").val(transDate);
    $("#"+type+"ShopId").val(shopId);
    $("#"+type+"PayType").val(payType);
    $("#"+type+"SizeSpan").html(size);
	$("#"+type+"Size").val(size);
    $("#"+type+"MoneySpan").html(bt.money.format(money, {scale: 2}));
    $("#"+type+"Money").val(money);
	$("#"+type+"Id").val(ids);
	var lessMoney=bt.money.sub(bt.money.toNumber($("#revenueMoney").val()),bt.money.toNumber($("#receivedMoney").val()));
    $("#lessMoneySpan").html(bt.money.format(lessMoney, {scale: 2}));
	$("#lessMoney").val(lessMoney);
}

function initData(type){
	$("#"+type+"TransDate").val("");
    $("#"+type+"ShopId").val("");
    $("#"+type+"PayType").val("");
	$("#"+type+"Id").val("");
	
    $("#"+type+"SizeSpan").html(0);
	$("#"+type+"Size").val(0);
    $("#"+type+"MoneySpan").html(0);
    $("#"+type+"Money").val(0);
    $("#lessMoneySpan").html(0);
	$("#lessMoney").val(0);
}

/**
 * 手动稽核操作方法
 * @param type
 */
function auditFun(type){
	if(auditCheckFun(type)){
		auditFunOp(type);
	}
}

/**
 * 进行稽核操作
 * @param type
 */
function auditFunOp(type){
	var auditData=initAuditData(type);
	bt.loading.show();
    $.ajax({
        type:"POST",
        url: basePath+ "/ras/audit/manual/manualAudit",
        dataType:"json",
        contentType:"application/json",
        data:JSON.stringify(auditData),
        success:function(result){
	        bt.loading.hide();
        	if(result.success){
	            parent.layer.alert(result.msg, {icon: result.success?1:2});
	            searchData();
	        }else{ 
	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
	        }
        }
    });
}

/**
 * 设置稽核数据信息
 * @param type
 * @returns {___anonymous6589_6598}
 */
function initAuditData(opType){
	var shopId=$("#shopId").val();
	var payType=$("#payType").val();
	if(opType=='auto'){
		shopId=rasShop.get("shopId").val();
		if(shopId==' '){
			shopId="";
		}
		payType=dicts.get("payType").val();
	}
	var dataSource=dicts.get("dataSource").val();
	if(dataSource instanceof Array){
		dataSource=dataSource.join();
	}
	if(payType instanceof Array){
		payType=payType.join();
	}
	var transDate=$("#transDate").val();
//	if(transDate&&transDate.length>10){
//		transDate=transDate.substr(0,10);
//	}
	var auditData={
			'shopId':shopId,
			'payType':payType,
			'corpId':corp.get("corpId").val(),
			'revenueTransDateStart':$("#revenueTransDateStart").val(),
			'revenueTransDateEnd':$("#revenueTransDateEnd").val(),
			'receivedTransDateStart':$("#receivedTransDateStart").val(),
			'receivedTransDateEnd':$("#receivedTransDateEnd").val(),
			'dataSource':dataSource,
			'transDate':transDate,
			'revenueId':$("#revenueId").val(),
			'revenueSize':$("#revenueSize").val(),
			'revenueMoney':$("#revenueMoney").val(),
			'receivedId':$("#receivedId").val(),
			'receivedSize':$("#receivedSize").val(),
			'receivedMoney':$("#receivedMoney").val(),
			'lessMoney':$("#lessMoney").val(),
			'rmk':$("#rmk").val(),
			'opType':opType
	};
	return auditData;
}

/**
 * 数据检查
 * @param type 操作类型
 * @returns {Boolean}
 */
function auditCheckFun(type){
	if(type!='auto'){
		if($("#revenueShopId").val()&&$("#receivedShopId").val()&&$("#revenueShopId").val()!=$("#receivedShopId").val()){
			layer.alert(bt.lang('ras.audit.msg.shop.neeq'), {icon: 7});
			return false;
		}
		if($("#revenuePayType").val()&&$("#receivedPayType").val()&&$("#revenuePayType").val()!=$("#receivedPayType").val()){
			layer.alert(bt.lang('ras.audit.msg.paytype.neeq'), {icon: 7});
			return false;
		}
		if(($("#revenuePayType").val()=='001'||$("#revenuePayType").val()=='006')
				&&$("#revenueTransDate").val()
				&&$("#receivedTransDate").val()
				&&$("#revenueTransDate").val()!=$("#receivedTransDate").val()){
		    layer.alert(bt.lang('ras.audit.msg.paytype.neTransDate'), {icon: 7});
			return false;
		}
		if($("#revenueSize").val()==0&&$("#receivedSize").val()==0){
			layer.alert(bt.lang('ras.audit.msg.not.null'), {icon: 7});
			return false;
		}else if($("#revenueSize").val()>1&&$("#receivedSize").val()>1){
			layer.alert(bt.lang('ras.audit.msg.not.manytomany'), {icon: 7});
			return false;
		}
		if(type=='manual'){
			if($("#lessMoney").val()!=0){
				layer.alert(bt.lang('ras.audit.msg.manual.notlessMoney'), {icon: 7});
				return false;
			}
		}else if(type=='less'){
			if($("#lessMoney").val()==0){
				layer.alert(bt.lang('ras.audit.msg.less.lessMoney'), {icon: 7});
				return false;
			}
			if(!$("#rmk").val()){
				layer.alert(bt.lang('ras.audit.msg.less.rmkIsNotNull'), {icon: 7});
				return false;
			}
		}
		if($("#revenueSize").val()>0){
			initSetData("revenue");
		}else{
			initSetData("received");
		}
		var payType=$("#payType").val();
		if(payType=='001'||payType=='006'){
			var transDate=$("#transDate").val();
			var shopId=$("#shopId").val();
			var isnotClaimNum=isNotClaimNumFun(transDate,shopId);
			var reInfo=false;
			if(isnotClaimNum){
			    parent.layer.confirm(bt.lang('ras.audit.msg.asClaimNum.isAudit'),function(){
			    	auditFunOp(type);
			    });
			}else{
				reInfo= true;
			}
			return reInfo;
		}else{
			return true;
		}
	}else{
		var revenueTransDateEnd=$("#revenueTransDateEnd").val();
		if(!revenueTransDateEnd){
//			自动稽核,营收交易日期结束时间必填！
			layer.alert(bt.lang('ras.audit.msg.revenueTransDateEnd.notnull'), {icon: 7});
			return false;
		}
		var receivedTransDateEnd=$("#receivedTransDateEnd").val();
		if(!receivedTransDateEnd){
//			自动稽核,实收交易日期结束时间必填！
			layer.alert(bt.lang('ras.audit.msg.receivedTransDateEnd.notnull'), {icon: 7});
			return false;
		}
		return true;
	}
}

function initSetData(type){
	$("#transDate").val($("#"+type+"TransDate").val());
	$("#payType").val($("#"+type+"PayType").val());
	$("#shopId").val($("#"+type+"ShopId").val());
}

/**
 * 判断此交易日期是否有未认领的实收数据
 * @param transDate
 * @returns {Boolean}
 */
function isNotClaimNumFun(transDate,shopId){
	var success=false;
	$.ajax({ 
		type: 'POST',
		url: basePath+"/ras/received/isNotClaimNum", 
		data:{'transDate':transDate,'shopId':shopId},	
		async: false, 
		dataType:'json',
		success: function(result){
        	if(!result.success){
        		success=true;
        	}
      }});
	return success;
}

var datatable;
bt.use(['form','datatable','corp','dicts','dateType','moneyType','rasShop','date','btImport'], function(){
    $ = bt.jquery;
    bt.form();
    bt.corp.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.rasShop.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ras/received/importDatatable"
        }
        , 
        buttons: [
            {
	            text:bt.lang('common.button.tovoid'),
	            hide:bt.isnotResource('ras:receivedImport:op'), 
	            action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
	            	tovoid(data);   
	            }
            },{
	        	 text: bt.lang('common.button.imports'),
		         hide:bt.isnotResource('ras:receivedImport:op'), 
	             action: function ( e, dt, node, config ) {
	                 importExcel();
	             }
            }
        ]
        ,columns: [
            {data:"serialNo",className:'center',bSortable:true},
            {data:"orderNo",className:'center',bSortable:true},
            {data:"shopName",className:'center',bSortable:true},
            {data:"corpName",className:'center',bSortable:true},
            {data:"payeeAcc",className:'center',bSortable:true},
            {data:"payeeAccName",className:'textRight',bSortable:true},
            {data:"payeeBankName",className:'center',bSortable:true},
            {data:"payTypeName",className:'center',bSortable:true},
            {data:"transDate",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.date.format(full.transDate, "yyyy-MM-dd hh:mm:ss");
                }
            },
            {data:"money",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.money);
                }
            },
            {data:"transMoney",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.transMoney);
                }
            },
            {data:"custName",className:'center',bSortable:true},
            {data:"custAcc",className:'center',bSortable:true},
            {data:"rmk",className:'center',bSortable:true},
            {data:"dateSourceName",className:'center',bSortable:true},
            {data:"createDate",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                	if(full.createDate){
                        return bt.date.format(full.createDate, "yyyy-MM-dd hh:mm:ss");
                	}
                	return '';
                }
            },
            {data:"statusName",className:'center',bSortable:true}
        ]
    });
    //导入初始化
    importExcel = function () {
        up = bt.btImport.init({
            url: basePath+"/import/excel/uploadCustom", 
            customParameUrl:basePath +"/ras/revenue/importConf",
            code:"80000067",
            width:"100%",
            heigth:"100%",
            hideDownTempBtn:true,
            success:function(head,data,select,cacheCode){
            	var datasource = $("#datasource").find('select').val();
            	bt.loading.show();
            	$.ajax({
                    type:"POST", 
	                url: basePath + "/ras/received/saveImport?datasource="+datasource, 
	                dataType:"json",
	                data:{"cacheCode":cacheCode},   
	                success:function(result){
	                    parent.layer.alert(result.msg, {icon: result.success?1:2});
	                    bt.loading.hide();
	                	bt.datatable.refreshCurrentPage();
	                }
            	});
            }
        });
    };
});
//作废
function tovoid(data){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var rowData = data.splice(0,len);
    for(i=0;i<len;i++){
    	if(bt.isnotResource('ras:receivedImport:op',rowData[i].corpId)){
    		layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
    		return;
    	}
    }
    parent.layer.confirm(bt.lang('common.message.confirm.tovoid'),function(){
        var ids;
        for(i=0;i<len;i++){
        	if(ids){
        		ids=ids+","+rowData[i].id;
        	}else{
        		ids=rowData[i].id;
        	}
        }
        $.post(basePath + "/ras/received/batchStorage/tovoid",{"ids":ids}, function(result){
        	if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
        		bt.datatable.refreshCurrentPage();
        	}else{
 	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
        	}
        },'JSON');
    });
}
//单位回调函数
function corpCallback(data){
    bt.rasShop.get("shopId").init({"corpId":data.id});
}
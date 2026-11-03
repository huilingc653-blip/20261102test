var billTable = {};
var form,$,billPool,billPoolCorp,moneyType;
bt.use(['form','billPool', 'datatable', 'btdate', 'select', 'dateType','mulselect', 'dicts','billPoolCorp','moneyType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.billPoolCorp.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/pool/abbillInstructionManage/datatableTestPoolLimit"
        },
        buttons: [
				{
				    hide:bt.isnotResource('bill:accebill:abBillInstruction:opTest'),
				    text:bt.lang('common.button.failure'),
				    action: function ( e, dt, node, config ) {
				        var data = dt.rows('.selected').data();
				        batchOp(data,'-1');
				    }
				},
				{
				    hide:bt.isnotResource('bill:accebill:abBillInstruction:opTest'),
				    text:bt.lang('common.button.sendSucce'),
				    action: function ( e, dt, node, config ) {
				        var data = dt.rows('.selected').data();
				        batchOp(data,'18');
				    }
				},
				{
	                hide:bt.isnotResource('bill:accebill:abBillInstruction:opTest'),
	                text:bt.lang('common.button.signSucce'),
	                action: function ( e, dt, node, config ) {
	                    var data = dt.rows('.selected').data();
	                    batchOp(data,'19');
	                }
	            }
        ]
        , columns: [
            {data: "corpName", bSortable: true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data: "limtTypeName", bSortable: true},
            {data: "typeName", bSortable: true},
            {data:"applyMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data: "createTime", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "rmk", bSortable: true},
            {data: "parentName", bSortable: true},
            {data: "bustypeLevel", bSortable: true},
            {data: "bpName", bSortable: true},
            {data: "checkTypeName", bSortable: true},
            {data: "checkMsg", bSortable: true},
            {data: "optStatusName", bSortable: true},
            {data: "returnCode", bSortable: true},
            {data: "returnMsg", bSortable: true}
        ],
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'4',
                    dataName:'applyMoney'
                }
            ]
        }
    });
});


/**
 * 进行批量操作
 * @param data 选择的数据
 * @param opType -1失败，17发送成功待回应，18回应待签收，19业务完成
 */
function batchOp(data,opType){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var showTitle="";
    var sendUrl="";
	showTitle=bt.lang('common.message.confirm.batchOp');
	sendUrl="/bill/pool/abbillInstructionManage/batchSucce";
    
	var rowData = data.splice(0,len);
	var ids;
    var limitType;
    for(i=0;i<len;i++){
    	if(bt.isnotResource('bill:accebill:abBillInstruction:opTest',rowData[i].corpId)){
			layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
			return;
    	}
    	if(ids){
    		ids=ids+","+rowData[i].id;
    		limitType=limitType+","+rowData[i].type;
    	}else{
    		ids=rowData[i].id;
    		limitType=rowData[i].type;
    	}
    }
    parent.layer.confirm(showTitle,function(){
		bt.jquery.ajax({
	    	type: "POST",
	    	url: basePath + sendUrl,
	       	data: JSON.stringify({"ids":ids,"opType":opType,"busType":"poolLimit","limitType":limitType}),//将对象序列化成JSON字符串
	       	dataType:"json",
	       	async: false, 
	       	contentType : 'application/json;charset=utf-8', //设置请求头信息 
	    	success: function(result){
	            parent.layer.alert(result.msg, {icon: result.success?1:2});
	    		if(result.success){
	    			bt.datatable.refreshCurrentPage();
	        	}
	    	},
	    	error: function(res){
	    	}
	    });
    });
    
}


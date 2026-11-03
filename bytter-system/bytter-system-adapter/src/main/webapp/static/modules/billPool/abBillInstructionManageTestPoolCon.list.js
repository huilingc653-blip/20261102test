var form,$,billPool,billPoolCorp,datatable,moneyType,upload;
bt.use(['form','billPool','billPoolCorp','dateType','datatable','mulselect','dicts','moneyType','upload'], function () {
    $ = bt.jquery; form = bt.form();
    upload=layui.upload;
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/pool/abbillInstructionManage/datatableTestPoolCon"
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
        ,columns: [
            {data:"corpCodeAndName",bSortable:true},
            {data:"appNo",bSortable:true},
            {data:"conOpTypeName",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"billFlagName",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
             },
            {data:"payer",bSortable:true},
            {data:"abs",bSortable:true},
            {data:"banendrsmtmkName",bSortable:true},
            {data:"startDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }
             },
            {data:"endDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }},
            {data:"payeeacc",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"pbCodeAndName",bSortable:true},
            {data:"conOptStatusName",bSortable:true},
            {data:"returnCode",bSortable:true},
            {data:"returnMsg",bSortable:true}
        ],
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'6',
                    dataName:'billMoney'
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
    var type;
    for(i=0;i<len;i++){
    	if(bt.isnotResource('bill:accebill:abBillInstruction:opTest',rowData[i].corpId)){
			layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
			return;
    	}
    	if(ids){
    		ids=ids+","+rowData[i].conId;
    		type=type+","+rowData[i].conOpType;
    	}else{
    		ids=rowData[i].conId;
    		type=rowData[i].conOpType;
    	}
    }
    parent.layer.confirm(showTitle,function(){
		bt.jquery.ajax({
	    	type: "POST",
	    	url: basePath + sendUrl,
	       	data: JSON.stringify({"ids":ids,"opType":opType,"busType":"poolCon","type":type}),//将对象序列化成JSON字符串
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

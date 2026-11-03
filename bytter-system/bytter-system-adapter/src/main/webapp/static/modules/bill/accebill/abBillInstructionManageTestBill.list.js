var billTable={};
bt.use(['form','select','datatable','dicts','btdate','moneyType','corp','element','bank','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.mulselect.init();
    //生成列表
    billTable = bt.datatable.init({
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
            'url': basePath+"/bill/accebill/abbillInstructionManage/datatableTest"
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
                    batchOp(data,'17');
                }
            },
            {
                hide:bt.isnotResource('bill:accebill:abBillInstruction:opTest'),
                text:bt.lang('common.button.succeSign'),
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
            },
            {
                hide:bt.isnotResource('bill:accebill:abBillInstruction:opTest'),
                text:bt.lang('common.button.failureSign'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchOp(data,'-4');
                }
            }
        ]
        ,columns: [
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data:"origNo",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"briefIdName",bSortable:true},
            {data:"applyDate",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.billMoney);
                }
            },
            {data:"payer",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"reqNm",bSortable:true},
            {data:"recNm",bSortable:true},
            {data:"recAcctId",bSortable:true},
            {data:"signUpFlagName",bSortable:true},
            {data:"optStatusName",bSortable:true},
            {data:"returnCode",bSortable:true},
            {data:"returnMsg",bSortable:true},
            {data: "newStmIssuofpprIndName", bSortable: true},
            {data: "blgrdbagcrclPrmtIndName", bSortable: true},
            {data: "sbbllRngStrtSn", bSortable: true},
            {data: "sbbllRngEndSn", bSortable: true},
            {data: "blStdAmt", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return bt.money.format(full.blStdAmt);
                    }else {
                        return "";
                    }
                }},
            {data: "blRmtrUnnIdNo", bSortable: true},
            /*{data: "transMoney", bSortable: true,
                className: 'textRight', width: 120,
                render: function (data, type, full, meta) {
                    if (full.newStmIssuofpprInd === '1' && full.blgrdbagcrclPrmtInd === '1' && full.bailType === '03') {
                        return '<input lay-verify="required" style="width: 100px" class="layui-input bt-input-inline bt-biz-moneyType" ' +
                            'value="'+bt.money.format(full.transMoney)+'" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
                            'data-id="transMoney_'+full.vvid+'" data-name="transMoney_'+full.vvid+'" data-biz-name="transMoney_'+full.vvid+'" />';
                    }
                    return bt.money.format(full.transMoney);
                }},*/
            /*{data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                return html;
            }}*/
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ],
        fixedColumns:{
        	rightColumns:1
        },
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'9',
                    dataName:'billMoney'
                }/*,
                {
                    colNum:'25',
                    dataName:'blStdAmt'
                },
                {
                    colNum:'27',
                    dataName:'transMoney'
                }*/
            ]
        }
    });
    bt.btdate.init({
    	elem:"#startDate",
    	startname:"ged_startDate",
    	endname:"led_startDate"
    });
    bt.btdate.init({
    	elem:"#endDate",
    	startname:"ged_endDate",
    	endname:"led_endDate"
    });
    bt.btdate.init({
    	elem:"#applyDate",
    	startname:"ged_applyDate",
    	endname:"led_applyDate"
    });
    var moneyType = bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
    bt.bank.init({elem:'.bank'});
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
	sendUrl="/bill/accebill/abbillInstructionManage/batchSucce";
    
	var rowData = data.splice(0,len);
    var ids;
    for(i=0;i<len;i++){
    	if(bt.isnotResource('bill:accebill:abBillInstruction:opTest',rowData[i].applyCorpId)){
			layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
			return;
    	}
    	if(ids){
    		ids=ids+","+rowData[i].id;
    	}else{
    		ids=rowData[i].id;
    	}
    }
    parent.layer.confirm(showTitle,function(){
		bt.jquery.ajax({
	    	type: "POST",
	    	url: basePath + sendUrl,
	       	data: JSON.stringify({"ids":ids,"opType":opType}),//将对象序列化成JSON字符串
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

function logInfo(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var ids="";
    if(data.id){
    	if(ids){
    		ids+=",";
    	}
    	ids+=data.id;
    }
    if(data.vvid){
    	if(ids){
    		ids+=",";
    	}
    	ids+=data.vvid;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abLog/abLogView?ids=" + ids,
        end:function(){
//        	billTable.draw();
			bt.datatable.refreshCurrentPage();
        }
    });
}
/**
 * 进行票据信息查看
 * @param id 签收表ID
 * @param type front正面，back背面
 */
var billInfo=function(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var vid="";
    var recId="";
    var origNo="";
    if(data.recId){
    	recId=data.recId;
    }
    if(data.vid){
    	vid=data.vid;
    }
    if(data.origNo){
    	origNo=data.origNo;
    }
    parent.layer.open({
    	title:bt.lang('common.button.billinfo'),
        type: 2,
        area: ['850px', '730px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abvoucherinfo/billEcdsInfoView?id=" + vid +"&recId=" + recId +"&origNo="+ origNo
    });
}

function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if(data.type==0){
    	emitView(data);
    }else{
    	receiveView(data);
    }
}

function emitView(data){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abemitbill/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/view/" + data.id
    });
}

function receiveView(data){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abreceivebill/view?viceId="+data.vvid+"&accId="+data.id);
        return;
    }
   layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?viceId="+data.vvid+"&accId="+data.id
    });
}
//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}
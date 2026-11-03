var billTable={};
bt.use(['form','select','datatable','dicts','btdate','moneyType','corp','element','bank','mulselect','accountTable'], function(){
    $ = bt.jquery,form = bt.form();
    bt.mulselect.init();
    bt.accountTable.init({urldata:{accountType :'2,3',electricBill:'1'}});
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
            'url': basePath+"/bill/accebill/abbillInstructionManage/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('bill:accebill:abBillInstruction:send'),
                text:bt.lang('common.button.send'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchOp(data,'Send');
                }
            },{
                hide:bt.isnotResource('bill:accebill:abBillInstruction:repulse'),
                text:bt.lang('common.button.return'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    data.mergeOpt = '1';
                    batchOp(data,'Repulse');
                }
            }
            ,{
                hide:bt.isnotResource('bill:accebill:abBillInstruction:repulse'),
                text:bt.lang('拆票退回'),
                className:'mergeReturn',
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    data.mergeOpt = '0';
                    batchOp(data,'Repulse');
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
            {data: "bankAcc", bSortable: true},
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data:"origNo",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"briefIdName",bSortable:true},
            {data:"billTransferType",bSortable:true},
            {data:"applyDate",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.billMoney);
                }
            },
            {data:"payer",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"cnAccrecords",bSortable:true,className: 'textRight',
                render: function (data, type, full, meta) {
                    return data === '' || data === null ? full.cnAccrecordsName : data;
                }
            },
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"reqNm",bSortable:true},
            {data:"recNm",bSortable:true},
            {data:"recAcctId",bSortable:true},
            {data:"signUpFlagName",bSortable:true},
            {data:"optStatusName",bSortable:true},
            {data:"returnCode",bSortable:true},
            {data:"returnMsg",bSortable:true},

            {data:"newStmIssuofpprInd",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict('AB_BILL_NEW_STM_ISSUOFPPR_IND',full.newStmIssuofpprInd);
                }},
            {data:"blgrdbagcrclPrmtInd",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict('AB_BILL_BLGRDBAGCRCL_PRMT_IND',full.blgrdbagcrclPrmtInd);
                }},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"blStdAmt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return bt.money.format(full.blStdAmt);
                    }else {
                        return "";
                    }
                }
            },
            {data:"socCode",bSortable:true},
            {data:"batchNo",bSortable:true},

            /*{data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="billInfo(this)">'+bt.lang("common.button.billinfo")+'</a>&nbsp;|&nbsp;\
                <a class="" href="#" data-row="'+meta.row+'" onclick="logInfo(this)">'+bt.lang("common.button.logInfo")+'</a>';
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
                }
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

//票据单位账号回调
function bankAccCallBack(data){
    var $ = bt.jquery;
    $("#bankAcc").val(data.bankAcc);
}

/**
 * 进行批量操作
 * @param data 选择的数据
 * @param type Send发送，Repulse打回
 */
function batchOp(data,type){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var showTitle="";
    var sendUrl="";
	showTitle=bt.lang('common.message.confirm.batch'+type);
	sendUrl="/bill/accebill/abbillInstructionManage/batch"+type;

	var rowData = data.splice(0,len);
    var ids;
    for(i=0;i<len;i++){
    	if(("Send"==type && bt.isnotResource('bill:accebill:abBillInstruction:send',rowData[i].applyCorpId))
    			||("Repulse"==type && bt.isnotResource('bill:accebill:abBillInstruction:repulse',rowData[i].applyCorpId))){
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
	       	data: JSON.stringify({"ids":ids,"mergeOpt":data.mergeOpt}),//将对象序列化成JSON字符串
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
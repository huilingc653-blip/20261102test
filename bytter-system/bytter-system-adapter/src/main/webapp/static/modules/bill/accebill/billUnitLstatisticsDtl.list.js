var billTable={};
bt.use(['form','element','datatable','billPool','select','dicts','btdate','mulselect','moneyType','rateType','externalCorpBankAcc','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    //客商组件
    var externalCorpBankAcc=bt.externalCorpBankAcc.init();
    bt.dateType.init();
    bt.billPool.init();
    bt.mulselect.init();


    bt.btdate.init({
        elem:"#applyDate",
        startname:"ged_applyDate",
        endname:"led_applyDate"
    });
    bt.btdate.init({
        elem:"#businessDate",
        startname:"ged_businessDate",
        endname:"led_businessDate",
        format:"YYYY-MM-DD",
        startDefaultVal:$("#ged_businessDate").val()!=''?new Date($("#ged_businessDate").val()).Format("yyyy-MM-dd"):"",
        endDefaultVal:new Date($("#led_businessDate").val()).Format("yyyy-MM-dd")
    });
    bt.moneyType.init();
    bt.dicts.init();

    //选择收票人
    bt.bailPayeeCallback=function(data,dom){
        $("#payee").val(data.name);
    }
    //选择前手
    bt.bailCnAccrecordsCallback=function(data,dom){
        $("#cnAccrecords").val(data.name);
    }

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable", 
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[11,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [
	       {
	           text: bt.lang('accebill.common.button.electView'),
	           action: function ( e, dt, node, config ) {
	        		var data = dt.rows('.selected').data();
	           		batchBillView(data);
	           }
	       }],
        ajax: {
            'url': basePath+"/bill/accebill/abbillQuery/billUnitLstatisticsDtlData"
        },columns: [
            {data:"corpCodeAndName",bSortable:true},//1
            {data:"bankAcc",bSortable:true},//2
            {data:"bankTypeName",bSortable:true},//2
            {data:"origNo",bSortable:true},//3

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

            {data:"busTypeName",bSortable:true},//4
            {data:"sourceName",bSortable:true},//5
            {data:"billFlagName",bSortable:true},//6
            {data:"billTypeName",bSortable:true},//7
            {data:"billMoney",bSortable:true,className:'textRight',//8
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.billMoney);
                }
            },
            {data:"bpName",bSortable:true},//9
            {data:"countPoolStatusName",bSortable:true},//9
            {data:"applyName",bSortable:true},//9
            {data:"applyDate",bSortable:true},//10
            {data:"businessDate",bSortable:true},//11
            {data:"startDate",bSortable:true},//12
            {data:"endDate",bSortable:true},//13
            {data:"interestRates",bSortable:true,className:'textRight',//14
                render: function ( data, type, full, meta ) {
                	if(full.interestRates==null
                			||full.interestRates==undefined){
                		return ""
                	}else{
                        return bt.rateType.getFun().format(full.interestRates);
                	}
                }
            },
            {data:"interest",bSortable:true,className:'textRight',//15
                render: function ( data, type, full, meta ) {
                	if(full.interest==null
                			||full.interest==undefined){
                		return ""
                	}else{
                    	return bt.money.format(full.interest);
                	}
                }
            },
            {data:"payer",bSortable:true},//16
            {data:"payee",bSortable:true},//17
            {data:"acceptor",bSortable:true},//17
            {data:"cnAccrecords",bSortable:true},//18
            {data:"rearAccrecords",bSortable:true},//19
            {data:"sendSignName",bSortable:true},
            {data:"busStatusName",bSortable:true},//20
            {data:"billStatusName",bSortable:true},//21
            {data:"instructStatusName",bSortable:true},//21
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	// html += '&nbsp;|&nbsp;<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
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
                    colNum:'8',
                    dataName:'billMoney'
                }
            ]
        }
    });
    
  //选择收票人
    bt.bailPayeeCallback=function(data,dom){  
    	$("#payee").val(data.name); 
    }
    //选择前手
    bt.bailCnAccrecordsCallback=function(data,dom){  
    	$("#cnAccrecords").val(data.name);  
    } 
});

function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if(data.type==0){
    	emitView(data);
    }else{
    	receiveView(data);
    }
}

function batchBillView(data){
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var rowData = data.splice(0,len);
    var accIds="";
    for(i=0;i<len;i++){
    	if(accIds.length>0){
    		accIds+=",";
    	}
		accIds+=rowData[i].id;
    }
//	if(bt.navtab){
//		bt.navtab.addNavtab(bt.lang('common.button.view') ,
//				basePath + "/bill/accebill/abvoucherinfo/batchBillEcdsInfoView?accIds=" + accIds);
//		return;
//	}
    top.batchBillEcdsInfoViewAccIds=accIds;
	parent.layer.open({
		type: 2,
		area: ['900px', '700px'],
		maxmin: true,
		content: basePath + "/bill/accebill/abvoucherinfo/batchBillEcdsInfoView?accIds=post"
	});
}

function emitView(data){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abemitbill/view?accId=" + data.id+"&isShowPool="+true);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/view?accId=" + data.id+"&isShowPool="+true
    });
}

function receiveView(data){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abreceivebill/view?viceId="+data.vvid+"&accId="+data.id+"&isShowPool="+true);
        return;
    }
   layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?viceId="+data.vvid+"&accId="+data.id+"&isShowPool="+true,
    });
}
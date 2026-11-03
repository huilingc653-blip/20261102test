var billTable={};
var moneyType={};
var dicts={};
var externalCorpBankAcc={};
var idsValArray=[];
bt.use(['form','select','datatable','dicts','btdate','moneyType','corp','bankInfo','externalCorpBankAcc','element','btImport','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    moneyType = bt.moneyType.init();
    bt.corp.init();
    bt.bankInfo.init();
    bt.mulselect.init();
    externalCorpBankAcc=bt.externalCorpBankAcc.init();
    externalCorpBankAcc.set({"type":4});
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
            'url': basePath+"/bill/accebill/abbillSearchforApply/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('bill:accebill:abBillSearchforApply:apply'),
                text:bt.lang('common.button.searchForApply'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchOp(data,'Apply');
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
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
            {data:"bankAcc",bSortable:true},
            {data:"origNo",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.billMoney);
                }
            },
            {
      		   data:null,
      		 className:'textRight',
      		   bSortable:false,
          	   render:function(data, type, full, meta){
          		   var reInfo="";
          		   //if(full.status=='11' || full.newStmIssuofpprInd == "1"){
                   if(full.newStmIssuofpprInd == '1' && full.blgrdbagcrclPrmtInd != '1'){
          			 reInfo=reInfo='<input id="'+(full.id||'')+'SearchForMoney" name="'+(full.id||'')+'SearchForMoney" value="'+(full.searchForMoney||'')+'" lay-verify="required" '+
                         'class="layui-input bt-biz-moneyType" readonly="readonly" type="text" placeholder="'+bt.lang("bill.accebill.searchForMoney")+'" data-biz-name="searchForMoney"  />';
          		   }else{
          			 reInfo='<input id="'+(full.id||'')+'SearchForMoney" name="'+(full.id||'')+'SearchForMoney" value="'+(full.searchForMoney||'')+'" lay-verify="required" '+
            		   'class="layui-input bt-biz-moneyType" type="text" placeholder="'+bt.lang("bill.accebill.searchForMoney")+'" data-biz-name="searchForMoney"  />';
          		   }
          		   return reInfo;
          	   }
  			},
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"payer",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"cnAccrecords",bSortable:true},
            {data:"rcrsTpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict('AB_RCRS_TP',full.rcrsTp);
                }},
            {data:"statusName",bSortable:true},
            {data:"briefIdName",bSortable:true},

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
            /*{data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="billInfo(this)">'+bt.lang("common.button.billinfo")+'</a>&nbsp;|&nbsp;\
                <a class="" href="#" data-row="'+meta.row+'" onclick="logInfo(this)">'+bt.lang("common.button.logInfo")+'</a>';
                return html;
            }}*/
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	// html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ],
        /*fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },*/
        btClickRowCallback:function(data){
            bt.setRcrsTpInfo(data);
            setExternalCorpBankAccCorpId();
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            bt.moneyType.init("searchForMoney");
            idsValArray = []
            setSumMoney();
        },
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

    dicts=bt.dicts.init();

    //开户行选择
    bt.bailCorpCallback = function(data,dom){
    	$("#recNm").val(data.name);
    	$("#recAcctId").val(data.externalAcc);
    	$("#recAcctsvcr").val(data.bankSourceCode);
    	$("#recAcctsvcrName").val(data.bank);
        $("#recCmonid").val(data.socCode);
    }

    //保证人银行选择
    bt.bailBankInfoCallback = function(data,dom){
    	$("#recNm").val(data.bankName);
    	$("#recAcctId").val("0");
    	$("#recAcctsvcr").val(data.bankCode);
    	$("#recAcctsvcrName").val(data.bankName);
        $("#recCmonid").val(data.socCode);
    }

    //开户行选择
    bt.bankinfoCallback = function(data,dom){
    	$("#recAcctsvcr").val(data.bankCode);
    	$("#recAcctsvcrName").val(data.bankName);
    }

    bt.setRcrsTpInfo = function(data){
    	var data = billTable.rows('.selected').data();
    	var len = data.length;
    	var isRcrsrsncd=false;
    	if(len>0){
        	var rowData = data.splice(0,len);
    		for(i=0;i<len;i++){
    			if(rowData[i].rcrsTp=='RT01'){
    				isRcrsrsncd=true;
    			}
    		}
    	}
		if(isRcrsrsncd){
    		$('#rcrsrsncdSpan').show();
    		$("select[name='rcrsrsncd']").attr("lay-verify","required");
		}else{
    		$('#rcrsrsncdSpan').hide();
    		$("select[name='rcrsrsncd']").attr("lay-verify","");
    		dicts.get('rcrsrsncd').val(" ");
		}
    }
    //导入初始化
    importExcel = function () {
        //证书签名
        var signDataMsg = signData("追索通知导入", 9);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        up = bt.btImport.init({
            code: "20000004",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            autoCloseWindown:false,
            success: function (head,data,select) {
                bt.loading.show();
                $.ajax({
                    type:"POST",
                    url: basePath + "/bill/accebill/abbillSearchforApply/saveImport",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(up.getSelectData()),
                    success:function(result){
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                        if (result.success) {
                            up.closeWindown();
                            bt.datatable.refreshCurrentPage();
                        }
                    },
                    error:function(){
                        bt.loading.hide();
                    }
                });
            }
        });
    };
});

/**
 * 进行批量操作
 * @param data 选择的数据
 * @param type Send发送，Repulse打回
 */
function batchOp(data,type){
	if(!bt.form().verifyFun('#dataForm')){
        return false;
    }
	var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var showTitle="";
    var sendUrl="";
	showTitle=bt.lang('common.message.confirm.batch'+type);
	sendUrl="/bill/accebill/abbillSearchforApply/batch"+type;
	var rowData = data.splice(0,len);
    var ids,searchForMoneys,rcrsTps;
    for(i=0;i<len;i++){
    	if(bt.isnotResource('bill:accebill:abBillSearchforApply:apply',rowData[i].applyCorpId)){
    		layer.alert(bt.lang('common.message.dataResourceCheck',[(i+1)]), {icon: 7});
    		return;
    	}
    	var searchForMoney=$('#'+rowData[i].id+'SearchForMoney').val();
    	if(ids){
    		ids=ids+","+rowData[i].id;
    		rcrsTps=rcrsTps+","+rowData[i].rcrsTp;
    		searchForMoneys=searchForMoneys+";"+searchForMoney;
    	}else{
    		ids=rowData[i].id;
    		rcrsTps=rowData[i].rcrsTp;
    		searchForMoneys=searchForMoney;
    	}
    }
    //证书签名
    var signDataMsg = signData("追索通知提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(showTitle,function(){
    	var sendDate={"ids":ids,
    			"searchForMoneys":searchForMoneys,
    			"rcrsTps":rcrsTps,
    			"rcrsrsncd":dicts.get("rcrsrsncd").val(),
    			"rmk":$("#rmk").val(),
    			"recNm":$("#recNm").val(),
    			"recCmonid":$("#recCmonid").val(),
    			"recAcctId":$("#recAcctId").val(),
    			"recAcctsvcr":$("#recAcctsvcr").val()};
		bt.jquery.ajax({
	    	type: "POST",
	    	url: basePath + sendUrl,
	       	data: JSON.stringify(sendDate),//将对象序列化成JSON字符串
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
    var recId=data.recId;
    var origNo=data.origNo;
    parent.layer.open({
    	title:bt.lang('common.button.billinfo'),
        type: 2,
        area: ['850px', '730px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abvoucherinfo/billEcdsInfoView?recId=" + recId +"&origNo="+ origNo
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

//设置客商的普通客商所属单位信息
function setExternalCorpBankAccCorpId(){
	var params={"type":"4"};
	var data = bt.datatable.getSelectData("#appTable");
	if(data.length>0){
		var corpId=""
	    bt.each(data,function(i,v){
	    	if(corpId.indexOf(v.corpId)<0){
		    	if(corpId.length>0){
		    		corpId+=","
		    	}
		    	corpId+=v.corpId;
	    	}
	    });
		params.corpId=corpId;
	}
	externalCorpBankAcc.set(params);
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].id)<0){
                    idsValArray.push(datas[i].id);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].id);
                if(index > -1){
                    objArrayRemove(idsValArray,index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function (){
    var showHTML=bt.lang("sys.common.isSelectSize",[idsValArray.length]);
    if($("#sumMoneyBillSpan").text()!=''){
        $("#sumMoneyBillSpan").text(showHTML);
    }else{
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">'+showHTML+'</span>');
    }
}

var objArrayRemove = function(obj,index){
    if (index > -1) {
        obj.splice(index, 1);
    }
};


//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}


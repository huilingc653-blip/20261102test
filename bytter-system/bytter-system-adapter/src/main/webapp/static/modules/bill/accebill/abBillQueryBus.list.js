var billTable={};
var idsValArray=[];
bt.use(['form','select','dicts','btdate','moneyType','corp','mulselect','element','datatable','rateType','externalCorpBankAcc'], function(){
    $ = bt.jquery,form = bt.form();
    //客商组件
    var externalCorpBankAcc=bt.externalCorpBankAcc.init();
    bt.dateType.init();
    bt.mulselect.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        isInitData: false,
        order: [[25,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillQuery/dataQueryBusTable"
        },
        buttons: [
            {
                text: bt.lang('accebill.common.button.electView'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchBillView(data);
                }
            }
        ]
        ,columns: [
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    if(full.applyCorpName==null || full.applyCorpName==''){
                        return '';
                    }else{
                        return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                    }
                }
            },//1
            {data:"corpCodeAndName",bSortable:true},//2
            {data: "busCorpName", bSortable: true},//3
            {data: "busPersonnel", bSortable: true},//4
            {data:"bankAcc",bSortable:true},//5
            {data:"bankTypeName", bSortable: true},//6
            {data:"validSign",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict('DRAFT_VAILD_SIGN',full.validSign);
                }},//7
            {data:"origNo",bSortable:true},//8
            {data:"newStmIssuofpprInd",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict('AB_BILL_NEW_STM_ISSUOFPPR_IND',full.newStmIssuofpprInd);
                }},//9
            {data:"blgrdbagcrclPrmtInd",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict('AB_BILL_BLGRDBAGCRCL_PRMT_IND',full.blgrdbagcrclPrmtInd);
                }},//10
            {data:"sbbllRngStrtSn",bSortable:true},//11
            {data:"sbbllRngEndSn",bSortable:true},//12
            {data: "blStdAmt", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return bt.money.format(full.blStdAmt);
                    }else {
                        return "";
                    }
                }},
            {
                data: "payNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.payNo != "" && full.payNo != null) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewNisBillhead(this)">' + full.payNo + '</a>';
                    }
                    return html;
                }
            },
            {data:"socCode",bSortable:true},
            {data:"batchNo",bSortable:true},

            {data:"briefIdName",bSortable:true},//4
            {data:"sourceName",bSortable:true},//5
            {data:"enterStockName",bSortable:true},//6
            {data:"billFlagName",bSortable:true},//7
            {data:"billTypeName",bSortable:true},//8
            {data:"billMoney",bSortable:true,className:'textRight',//9
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.billMoney);
                }
            },
            {data:"applyName",bSortable:true},//10
            {data:"applyDate",bSortable:true, render: function ( data, type, full, meta ) {
                    var applyDate = full.applyDate;
                    // if(full.applyDate){
                    //     applyDate = applyDate.toString().substring(0,10);
                    // }
                    return applyDate;
                }},//11
            {data:"businessDate",bSortable:true},//12
            {data:"startDate",bSortable:true},//13
            {data:"endDate",bSortable:true},//14
            {data:"interestRates",bSortable:true,className:'textRight',//15
                render: function ( data, type, full, meta ) {
                	if(full.interestRates==null
                			||full.interestRates==undefined){
                		return ""
                	}else{
                        return bt.rateType.getFun().format(full.interestRates);
                	}
                }
            },
            {data:"interest",bSortable:true,className:'textRight',//16
                render: function ( data, type, full, meta ) {
                	if(full.interest==null
                			||full.interest==undefined){
                		return ""
                	}else{
                    	return bt.money.format(full.interest);
                	}
                }
            },
            {data:"payer",bSortable:true},//17
            {data:"payerBankName",bSortable:true},
            {data:"payee",bSortable:true},//18
            {data:"acceptor",bSortable:true},
            {data:"cnAccrecords",bSortable:true},//19
            {data:"rearAccrecords",bSortable:true},//20
            {data:"sendSignName",bSortable:true},//21
            {data:"busStatusName",bSortable:true},//22
            {data:"instructStatusName",bSortable:true},//23
            {data:"returnCode",bSortable:true},
            {data:"returnMsg",bSortable:true},
            {data:"abs",bSortable:true},
            {data:"rmk",bSortable:true},
            {data:"rsn",bSortable:true},
            {data:"receiveTrader",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(full.briefId=='6001'){
                        return full.trader;
                    }else{
                        return "";
                    }
                }
            },
            {data:"endorseTrader",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(full.briefId=='010'){
                        return full.trader;
                    }else{
                        return "";
                    }
                }
            },
            {data:"isAutosignName",bSortable:true},

            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	// html += '&nbsp;|&nbsp;<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ],
        btClickRowCallback:function(data){
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            idsValArray = []
            setSumMoney();
        },
        fixedColumns:{
        	rightColumns:1
        },
        summary: {
            pageSum: false,
            selectSum: true,
            sumList: [
                {
                    colNum: '9',
                    dataName: 'billMoney'
                }
            ]
        }
    });
    bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();

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
        content: basePath + "/bill/accebill/abreceivebill/view?viceId="+data.vvid+"&accId="+data.id,
    });
}


function billView(accIds){
    top.batchBillEcdsInfoViewAccIds=accIds;
    parent.layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abvoucherinfo/batchBillEcdsInfoView?accIds=post&isShowSyncro=0"
    });
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
    billView(accIds);
}

/**
 * 批量设置兑付确认备注
 * @param data
 */
function batchSetEmitPayRmk(data){
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var accIds="";
    var vvids="";
    for(i=0;i<len;i++){
        if("6120"!=rowData[i].briefId){
            layer.alert(bt.lang('存在不为兑付确认的流水，请重新选择！'), {icon: 7});
            return;
        }
        if(accIds.length>0){
            accIds+=",";
            vvids+=",";
        }
        accIds+=rowData[i].id;
        vvids+=rowData[i].vvid;
    }
    layer.open({
        type: 1,
        title: '设置兑付确认备注',
        content: '<div style="padding: 20px;"><input type="text" id="emitPayRmk" class="layui-input" placeholder="兑付确认备注"></div>',
        btn: ['确定', '取消'],
        yes: function(index, layero){
            var emitPayRmk = document.getElementById('emitPayRmk').value;
            if(emitPayRmk){
                $.ajax({
                    type: 'post',
                    contentType: 'application/json; charset=UTF-8',
                    data: JSON.stringify({
                        ids: accIds,
                        vvids: vvids,
                        payAbs: emitPayRmk
                    }),
                    asycn: false,
                    url: basePath + "/bill/accebill/abemitbillpay/batchSetEmitPayRmk",
                    success: function (result) {
                        parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        if (result.success) {
                            bt.datatable.refreshCurrentPage();
                            layer.close(index); // 关闭弹出层
                        }
                    },
                    dataType: 'json'
                });
            }else{
                layer.alert(bt.lang('请输入兑付确认备注！'), {icon: 7});
            }
        },
        btn2: function(index, layero){
            layer.close(index); // 关闭弹出层
        }
    });
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

//付款单查看
function viewNisBillhead(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + data.payId
    });
}

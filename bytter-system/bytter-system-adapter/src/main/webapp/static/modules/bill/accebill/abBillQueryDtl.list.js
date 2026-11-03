var billTable={};
bt.use(['form','select','dicts','btdate','moneyType','corp','element','datatable','rateType','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.mulselect.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillQuery/dataQueryDtltable"
        },
        buttons: []
        ,columns: [
            {data:"corpCodeAndName",bSortable:true},//1
            {data:"bankAcc",bSortable:true},//2
            {data:"bankTypeName",bSortable:true},//2
            {data:"origNo",bSortable:true},//3

            {data: "newStmIssuofpprInd", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict('AB_BILL_NEW_STM_ISSUOFPPR_IND',full.newStmIssuofpprInd);
                }},
            {data: "blgrdbagcrclPrmtInd", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict('AB_BILL_BLGRDBAGCRCL_PRMT_IND',full.blgrdbagcrclPrmtInd);
                }},
            {data: "sbbllRngStrtSn", bSortable: true},
            {data: "sbbllRngEndSn", bSortable: true},

            {data:"busTypeName",bSortable:true},//4
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
            {data:"applyDate",bSortable:true},//11
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
            {data:"cnAccrecords",bSortable:true},//19
            {data:"rearAccrecords",bSortable:true},//20
            {data:"sendSignName",bSortable:true},//21
            {data:"busStatusName",bSortable:true},//22
            {data:"instructStatusName",bSortable:true},//23
            {data:"returnCode",bSortable:true},
            {data:"returnMsg",bSortable:true},
            {data:"receiveTrader",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(full.busType=='6001'){
                        return full.trader;
                    }else{
                        return "";
                    }
                }
            },
            {data:"endorseTrader",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(full.busType=='010'){
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
        fixedColumns:{
        	rightColumns:1
        }
    });
    bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
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
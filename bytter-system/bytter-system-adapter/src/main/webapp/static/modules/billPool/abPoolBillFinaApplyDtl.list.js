var billTable={};
bt.use(['form','element','datatable','select','dicts','btdate','mulselect','moneyType','rateType','externalCorpBankAcc','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dateType.init();
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
        buttons: [],
        ajax: {
            'url': basePath+"/billPool/finaApplyBook/datatableFinaApplyQueryDtl"
        },columns: [
            {data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                    var reVal=full.corpName;
                    if(full.corpName){
                        reVal='('+full.corpCode+')'+reVal;
                    }
                    return reVal;
                }
            },
            {data:"origNo",bSortable:true},//2
            {data:"billMoney",bSortable:true,className:'textRight',//3
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data:"busType",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_POOL_TYPE",full.busType);
                }},//4
            {data:"billStatus",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_STATUS",full.billStatus);
                }},//5
            {data:"payer",bSortable:true},//6
            {data:"payerAcc",bSortable:true},//7
            {data:"payerBankNo",bSortable:true},//8
            {data:"acceptor",bSortable:true},//9
            {data:"acceptorAcc",bSortable:true},//10
            {data:"acceptorBankNo",bSortable:true},//11
            {data:"payee",bSortable:true},//12
            {data:"payeeAcc",bSortable:true},//13
            {data:"payeeBankno",bSortable:true},//14
            {data:"startDate",bSortable:true},//15
            {data:"endDate",bSortable:true},//16
            {data:"billTypeName",bSortable:true},//17
            {data:"invcNo",bSortable:true},//18
            {data:"realpayAmt",bSortable:true,className:'textRight',//19
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.realpayAmt);
                }
            },
            {data:"promsfee",bSortable:true,className:'textRight',//20
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.promsfee);
                }
            },
            {data:"bankendrsmtmk",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BANEM",full.bankendrsmtmk);
                }},//21
        ],

        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'3',
                    dataName:'billMoney'
                },{
                    colNum:'19',
                    dataName:'realpayAmt'
                },{
                    colNum:'20',
                    dataName:'promsfee'
                }
            ]
        }
    });

    bt.moneyType.init();
    bt.dicts.init();

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
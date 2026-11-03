var billTable={};
var idsValArray=new Array();
bt.use(['form','select','dicts','btdate','moneyType','corp','element','datatable','rateType','dateType'], function(){
    $ = bt.jquery,form = bt.form();

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillTransferDown/dataQueryDtltable"
        },
        buttons: []
        ,columns: [
            {data:"businessDate",bSortable:true},//1
            {data:"busTypeName",bSortable:true},//2
            {data:"allocatedCode",bSortable:true},//3
            {data:"applyCode",bSortable:true},//4
			{data:"recordCorpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                  	return '('+full.recordCorpCode+')'+full.recordCorpName;
                  }
              },
            {data:"recevieCorpName",bSortable:true,
                  render: function ( data, type, full, meta ) {
                    	return '('+full.recevieCorpCode+')'+full.recevieCorpName;
                    }
                },
            /*{data:"billNo",bSortable:true},//7*/
            {data:"recordBankacc",bSortable:true},//8
            {data:"recevieBankacc",bSortable:true},//9
            {data:"applyMoney",bSortable:true,className:'textRight',//10
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.applyMoney);
                }
            },
            {data:"allocationMoney",bSortable:true,className:'textRight',//11
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.allocationMoney);
                }
            },
            {data:"applyReason",bSortable:true},//12
            {data:"allocationReason",bSortable:true},//13
            {data:"recordDate",bSortable:true},//14
            {data:"billCurrencyName",bSortable:true},//15
            {data:"statusName",busStatus:true},//16
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {//17
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	// html += '&nbsp;|&nbsp;<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.aid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ],
        /*fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },*/
        btClickRowCallback:function(data){
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            idsValArray = new Array()
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
                    colNum:'9',
                    dataName:'applyMoney'
                },{
                    colNum:'10',
                    dataName:'allocationMoney'
                }
            ]
        }
    });
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
});

function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var urlPath=basePath;
    if(data.busType=='6204'){
    	urlPath+="/bill/accebill/abbilltransferdownmanage/view?businessId="+data.aid;
    }else{
    	urlPath+="/bill/accebill/abbilltransferdownapply/view?aid="+data.aid;
    }
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		urlPath);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: urlPath
    });
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].aid)<0){
                    idsValArray.push(datas[i].aid);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].aid);
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


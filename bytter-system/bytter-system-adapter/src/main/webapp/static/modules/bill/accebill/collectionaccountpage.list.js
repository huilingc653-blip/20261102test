var billTable={},dicts;
bt.use(['form','datatable','moneyType','btdate','corp','select','dateType','element','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    dicts=bt.dicts.init();

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillcollectionaccount/datatable?isApply=0"
        },
        buttons: []
        ,columns: [
            {data:"billTypeName",bSortable:true},
            {data:"billFlagName",bSortable:true},
            {data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data:"blHoldUnnIdNo",bSortable:true},
            {data:"payer",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"origNo",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"businessDate",bSortable:true},
            // {data:"signerok",render:function(data, type, full, meta){
            //         if(data==null||data=='null'){
            //             data='';
            //         }
            //         return '<input class="layui-input" value="'+data+'" name="signerok" id="'+full.aid+'signerok" lay-verify=""></input>';
            //     }
            // },
            {data:"enterStockName",bSortable:true},
            {data:"sendSignName",bSortable:true},
            {data:"newStmIssuofpprIndName",bSortable:true},
            {data:"blgrdbagcrclPrmtIndName",bSortable:true},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"blStdAmt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.blStdAmt);
                }
            },
            {data:"bankendrsmtmkName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html ='<a class="" href="#" data-row="'+meta.row+'" onclick="selectCollect(this)">'+bt.lang("common.button.select")+'</a>';
                    html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ],
        fixedColumns:{
            rightColumns:1
        },
        summary:{
            pageSum:true,
            sumList:[
                {
                    colNum:'8',
                    dataName:'billMoney'
                }
            ]
        }
    });


});


var closeForms=function(){
    parent.layer?parent.layer.closeAll():layer.closeAll();
}

function selectCollect(el){
    var rowData = billTable.row(layui.jquery(el).data("row")).data();

    // if(!$("#"+rowData.aid+"signerok").val()){
    //     //你选择的数据没有填写收款人!
    //     layer.alert(bt.lang('accebill.abbillcollectionaccount.message.signerok',[(1)]), {icon: 7});
    //     return;
    // }

    var billInfo={"id":rowData.aid};

    var currentParentIframe = top.$('.layui-tab-content > .layui-show').find('iframe')[0]['name'];
    // 调父页面的回调方法
    top.frames[currentParentIframe].window.busTypeCallback(billInfo,$("#row").val());

}

function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();

    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId="+data.vid+"&viceId="+data.vvid+"&accId="+data.aid
    });
}

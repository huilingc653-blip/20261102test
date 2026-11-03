var billTable={};
bt.use(['form','datatable','moneyType','btdate','corp','select','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
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
            'url': basePath+"/bill/accebill/abbilltransferdownapply/dataTableApprove"
        }
        ,columns: [
				   {data:"allocatedCode",bSortable:true},
                   {data:"recordCorpName",bSortable:true,
                       render: function ( data, type, full, meta ) {
                          	return '('+full.recordCorpCode+')'+full.recordCorpName;
                          }
                      },
                   {data:"recevieBankacc",bSortable:true},
                   {data:"allocationMoney",bSortable:true,className:'textRight',
                       render: function ( data, type, full, meta ) {
                       	return bt.money.format(full.allocationMoney);
                       }
                   },
                   {data:"allocationReason",bSortable:true},
                   /*{data:"statusName",bSortable:true},*/
                   {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                   	var html='';
                	if($("#flowStatus").val()=='todo'){
                		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
                	}
                   if($("#flowStatus").val() == 'pass'){
                       html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                       html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.aid + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                   }else {
                       html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                   }
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
                    colNum:'4',
                    dataName:'allocationMoney'
                }
            ]
        }
    });
    var moneyType = bt.moneyType.init();
});


/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var aid = data.aid;
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/bill/accebill/abbilltransferdownapply/approve?businessId=" + aid + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbilltransferdownapply/approve?businessId=" + aid + "&taskId=" + data.taskId
    });
}

function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
            basePath + "/bill/accebill/abbilltransferdownapply/view?aid="+data.aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbilltransferdownapply/view?aid="+data.aid
    });
}

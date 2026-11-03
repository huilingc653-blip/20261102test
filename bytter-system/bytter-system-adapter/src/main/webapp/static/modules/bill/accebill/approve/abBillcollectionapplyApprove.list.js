var billTable={};
bt.use(['form','datatable','moneyType','btdate','corp','select','dateType','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
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
            'url': basePath+"/bill/accebill/abbillcollectionapply/dataTableApprove"
        }
        ,columns: [
                   {data:"billTypeName",bSortable:true},       
                   {data:"billFlagName",bSortable:true},
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
                   {data:"enterStockName",bSortable:true},
                   {data:"isOverdueName",bSortable:true},
                   {data:"sttlmmkName",bSortable:true},
                   {data:"applyName",bSortable:true},
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
                    colNum:'7',
                    dataName:'billMoney'
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
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/bill/accebill/abbillcollectionapply/approve?businessId=" + aid + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillcollectionapply/approve?businessId=" + aid + "&taskId=" + data.taskId
    });
}

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}

function view(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.aid
    });
}


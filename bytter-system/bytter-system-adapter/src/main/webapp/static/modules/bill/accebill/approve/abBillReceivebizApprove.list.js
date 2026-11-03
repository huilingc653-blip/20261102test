
var billTable={};
bt.use(['form','select','datatable','dicts','btdate','mulselect','moneyType','corp','element','bank'], function(){
    $ = bt.jquery,form = bt.form();
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
        ajax: {
            'url': basePath+"/bill/accebill/abbillreceivebiz/dataTableApprove"
        }
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
            {data:"billTypeName",bSortable:true},
            {data:"signerType",bSortable:true},
            {data:"origNo",bSortable:true},
            {data:"bussdt",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.billMoney);
                }
            },
            {data:"payer",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"reqNm",bSortable:true},
            {data:"recNm",bSortable:true},
            {data:"recAcctId",bSortable:true},
            {data:"recBankTypeName",bSortable:true},
            {data:"sgnupmkName",bSortable:true},
            {data:"applyName", bSortable: true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
            	if($("#flowStatus").val()=='todo'){
            		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
            	}
                if($("#flowStatus").val() == 'pass'){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.businessKey + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
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
                    colNum:'6',
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
    	elem:"#bussdt",
    	startname:"ged_bussdt",
    	endname:"led_bussdt"
    });
    var moneyType = bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
    bt.bank.init({elem:'.bank'});
});


/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/bill/accebill/abbillreceivebiz/approve?businessId=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillreceivebiz/approve?businessId=" + data.id + "&taskId=" + data.taskId
    });
}


function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.businessDtl') + '-' + data.origNo, basePath + "/bill/accebill/abbillreceivebiz/businessDtlView?id=" + data.id + "&msgtype=" + data.msgtype);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillreceivebiz/businessDtlView?id=" + data.id + "&msgtype=" + data.msgtype,
        end: function () {
//        	billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}



bt.use(['form','datatable','btdate','moneynum','corp','select','dicts','element','moneyType','dateType','jquery'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillpledgeapply/dataTableApprove"
        }
        ,columns: [
            {data:"billType",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_TYPE",full.billType);
                }
            },
            {data:"billFlag",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_FLAG",full.billFlag);
                }
            },
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
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"enterStock",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_ENTER_STOCK",full.enterStock);
                }
            },
            {data:"billSource",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("AB_BILL_SOURCE",full.billSource);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
            	if($("#flowStatus").val()=='todo'){
            		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
            	}
                if($("#flowStatus").val() == 'pass'){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.accId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
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

	bt.btdate.init({
		elem : "#startDate",
		startname : "ged_startDate",
		endname : "led_startDate"
	});
	
	bt.btdate.init({
    	elem:"#endDate",
    	startname:"ged_endDate",
    	endname:"led_endDate"
    });
});

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/bill/accebill/abbillpledgeapply/approve?businessId=" + data.accId + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillpledgeapply/approve?businessId=" + data.accId + "&taskId=" + data.taskId
    });
}
//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vouId + "&viceId=" + data.viceId + "&accId=" + data.accId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vouId + "&viceId=" + data.viceId + "&accId=" + data.accId
    });
}

var billTable={};
bt.use(['form','select','datatable','dicts','btdate','moneyType','corp','element','bank'], function(){
    $ = bt.jquery,form = bt.form();
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
            'url': basePath+"/bill/accebill/abbillSearchforManage/dataTableApprove"
        },
        columns: [
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
            {data:"origNo",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"appldt",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }
            },
            {data:"rcrsokmoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.rcrsokmoney);
                }
            },
            {data:"reqamt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.reqamt);
                }
            },
            {data:"rcrsokmoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.rcrsokmoney);
                }
            },
            // {data:"billMoney",bSortable:true,className:'textRight',
            //     render: function ( data, type, full, meta ) {
            //     	return bt.money.format(full.billMoney);
            //     }
            // },
            {data:"payer",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"startDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }
            },
            {data:"endDate",bSortable:true},
            {data:"rcrsTpName",bSortable:true},
            {data:"rcrsrsncdName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
            	if($("#flowStatus").val()=='todo'){
            		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
            	}
                if($("#flowStatus").val() == 'pass'){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }else {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }
                return html;
            }}
        ],
        btDrawCallback:function(){
        	bt.moneyType.init("rcrsokmoney");
        },
        fixedColumns:{
        	rightColumns:1
        },
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                     {
                    	 colNum:'5',
                    	 dataName:'rcrsokmoney'
                     },
                     {
                    	 colNum:'6',
                    	 dataName:'reqamt'
                     },
                     {
                         colNum:'7',
                         dataName:'billMoney'
                     }
            ]
        }
    });

    bt.btdate.init({
    	elem:"#appldt",
    	startname:"ged_appldt",
    	endname:"led_appldt"
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
    var moneyType = bt.moneyType.init();
    bt.dicts.init();
    bt.corp.init();
    bt.bank.init({elem:'.bank'});
});


/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/bill/accebill/abbillSearchforManage/approve?businessId=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillSearchforManage/approve?businessId=" + data.id + "&taskId=" + data.taskId
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
            basePath + "/bill/accebill/abreceivebill/view?viceId=" + data.vvid + "&accId=" + data.id + "&briefId=9022");
        return;
    }
    layer.open({
        type: 2,
        area: ['900px', '700px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?viceId=" + data.vvid + "&accId=" + data.id + "&briefId=9022"
    });
}





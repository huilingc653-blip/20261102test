var billTable={};
bt.use(['form','datatable','moneyType','btdate','corp','select','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    
    //申请单位初始化
	bt.select.init({
        elem:'#corpId',
        name:"eqs_recevieCorpId",
        valueField:"id",
        textField:"name",
        placeholder:{value:'',text:bt.lang('common.placeholder.select')},
        search: true,
        disabled: false,
        url:basePath+"/bill/accebill/abbilltransfercollection/getCorpIdByNetId?netId="+$("#netId").val()
    });
	
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
            'url': basePath+"/bill/accebill/abbilltransferdownmanage/dataTableApprove"
        }
        ,columns: [
				   {data:"allocatedCode",bSortable:true},
				   {data:"applyCode",bSortable:true},
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
                   /*{data:"billNo",bSortable:true},*/
                   {data:"recevieBankacc",bSortable:true},
                   {data:"applyMoney",bSortable:true,className:'textRight',
                       render: function ( data, type, full, meta ) {
                       	return bt.money.format(full.applyMoney);
                       }
                   },
                   {data:"allocationMoney",bSortable:true,className:'textRight',
                       render: function ( data, type, full, meta ) {
                       	return bt.money.format(full.allocationMoney);
                       }
                   },
                   {data:"applyReason",bSortable:true},
                   {data:"allocationReason",bSortable:true},
                  /* {data:"statusName",bSortable:true},*/
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
                    colNum:'6',
                    dataName:'applyMoney'
                },
                {
                    colNum:'7',
                    dataName:'allocationMoney'
                }
            ]
        }
    });
    var moneyType = bt.moneyType.init();
});

//中心回调函数
function netCallback(data){
    //收票单位初始化
    bt.select.init({
        elem:'#corpId',
        name:"eqs_recevieCorpId",
        valueField:"id",
        textField:"name",
        placeholder:{value:'',text:bt.lang('common.placeholder.select')},
        search: true,
        disabled: false,
        url:basePath+"/bill/accebill/abbilltransfercollection/getCorpIdByNetId?netId="+data.id
    });
}

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var aid = data.aid;
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/bill/accebill/abbilltransferdownmanage/approve?businessId=" + aid + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbilltransferdownmanage/approve?businessId=" + aid + "&taskId=" + data.taskId
    });
}

function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
            basePath + "/bill/accebill/abbilltransferdownmanage/view?businessId="+data.aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbilltransferdownmanage/view?businessId="+data.aid
    });
}


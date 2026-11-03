bt.use(['form','datatable','dateType','selectType','moneyType','dicts','bank'], function(){
    $ = bt.jquery,form = bt.form();
    bt.moneyType.init();
    bt.dicts.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.bank.init({
        elem:'.bank',
    });
    bt.datatable.init({
    	elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bdlink/bisexc/dataTableApprove"
        },
        buttons: [
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"voucherNo",bSortable:true,className:'center'},
            {data:"bifName",bSortable:true,className:'center'},
            {data:"curName",bSortable:true},
            {data:"voucherTypeName",bSortable:true},
            {data: "corpName", bSortable: true},
            {data:"payerAcc",bSortable:true},
            {data:"payerAccName",bSortable:true},
            {data:"payerBank",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data:"payeeAcc",bSortable:true},
            {data:"payeeAccName",bSortable:true},
            {data:"payeeBank",bSortable:true},
            {data:"reqDate",bSortable:true},
            {data:"cbsBillNo",bSortable:true,className:'center'},
            {data:"cmdType",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("bisExc.cmdType",full.cmdType);
                }
            },
            {data:"busOriType",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("bisExc.busOriType",full.busOriType);
                }
            },
            {data:"purpose",bSortable:true},
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
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',
                    dataName: 'payMoney'
                }
            ]
        },
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bdlink/bisexc/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisexc/view/" + data.id
    });
}

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/bdlink/bisexc/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisexc/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}

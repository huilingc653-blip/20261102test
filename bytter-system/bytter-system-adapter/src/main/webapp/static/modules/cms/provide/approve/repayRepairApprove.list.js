bt.use(['form','datatable','corp','moneyType','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        checkbox:true,
        ajax: {
            'url': basePath+"/cms/provide/loanrepay/dataTableRepayRepairApprove"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"corpName"},
            {data:"billCode"},
            /*{data:"createDate"},*/
            {data:"infoBillCode"},
            {data:"sendCode"},
            {data:"repayMoney",className:"textRight",
            	render: function ( data, type, full, meta ){
            	return bt.money.format(full.repayMoney);
            }},
            {data:"repayDate"},
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
        summary:{
            pageSum: true,//小计功能开启
            selectSum: true,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '5', //汇总列序号
                    dataName: 'repayMoney' //汇总列还款金额
                }
            ]
        }
    });
});


/**审批*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
	bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/cms/provide/loanrepay/viewRepayRepairApprove?businessId=" + data.id + "&taskId=" + data.taskId);
	return;
    }
    parent.layer.open({
	type: 2,
	area: ['850px', '530px'],
	maxmin: true,
	content: basePath + "/cms/provide/loanrepay/viewRepayRepairApprove?businessId=" + data.id + "&taskId=" + data.taskId
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/cms/provide/loanrepay/viewRepay?sendBillCode=" + data.sendCode + "&repayId=" + data.id + "&show=1");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loanrepay/viewRepay?sendBillCode=" + data.sendCode + "&repayId=" + data.id + "&show=1"
    });
}
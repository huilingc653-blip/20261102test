bt.use(['form','datatable','corp','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();//单位
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        checkbox:true,
        ajax: {
            'url': basePath+"/cms/provide/loansend/dataTableSendAppvoer"
        },
        buttons: [
           {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"corpName", bSortable: false},
            {data:"billCode"},
            {data:"loanCode"},
            {data:"bankName", bSortable: false},
            /*{data:"createDate"},*/
            {data:"curName", bSortable: false},
            {data:"contractSumMoney",className:"textRight",render: function ( data, type, full, meta ){
            	return bt.money.format(full.contractSumMoney);
            }},
            {data:"loanSendOutTypeName", bSortable: false},
            {data:"contractCode", bSortable: false},
            {data:"money",className:"textRight",render: function ( data, type, full, meta ){
            	return bt.money.format(full.money);
            }},
            {data:"repayTypeName", bSortable: false},
            {data:"plTerm", bSortable: false, render: function ( data, type, full, meta ){
                    var htm = ""
                    if(full.plTerm == null){
                        htm += 0+"";
                    }else{
                        htm += full.plTerm;
                    }
                    return htm + bt.dict('PrecativeLoanInfo.ternUnit',full.ternUnit);
            }},
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
            pageSum: false,//小计功能关闭
            allSum: true,//汇总功能开启
            selectSum: true,//已选汇总功能开启
            // groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '9',//汇总列序号
                    dataName: 'money',//汇总列数据
                }
            ]
        }
    });
});


/**审批*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
	bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/cms/provide/loansend/viewSendApprove?businessId=" + data.id + "&taskId=" + data.taskId);
		return;
    }
    parent.layer.open({
	type: 2,
	area: ['850px', '530px'],
	maxmin: true,
	content: basePath + "/cms/provide/loansend/viewSendApprove?businessId=" + data.id + "&taskId=" + data.taskId
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/provide/loaninfo/viewPerfectlyLoanInfo?infoBillCode=" + data.loanCode + "&sendBillCode=" + data.billCode + "&show=1");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loaninfo/viewPerfectlyLoanInfo?infoBillCode=" + data.loanCode + "&sendBillCode=" + data.billCode + "&show=1"
    });
}

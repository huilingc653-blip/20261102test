bt.use(['form','datatable','corp','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();//单位
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        checkbox:true,
        ajax: {
            'url': basePath+"/cms/provide/loaninfo/datatableRepairApprove"
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
        ],
        columns: [
            {data:"corpName", bSortable: false},
			{data:"billCode", bSortable: true},
            /*{data:"createDate"},*/
            {data:"bankName", bSortable: false},
            {data:"curName", bSortable: false},
            {data:"plMoney", bSortable: true, className:"textRight",render: function ( data, type, full, meta ){
            	return bt.money.format(full.plMoney);
            }},
            {data:"plDate", bSortable: true},
            {data:"repayDate", bSortable: true},
            {data:"repayTypeName", bSortable: false},
            {data:"contractTermName", bSortable: false},
            {data:"plTerm", bSortable: false, render: function ( data, type, full, meta ){
            	return full.plTerm + full.ternUnitName;
            }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
            	if($("#flowStatus").val()=='todo'){
            		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a><span class="text-explode">|</span>';
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
            pageSum: true,//小计功能关闭
            selectSum: true,//已选汇总功能开启
            // groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '5',//汇总列序号
                    dataName: 'plMoney',//汇总列数据
                }
            ]
        }
    });
});

/**审批*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
	bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/cms/provide/loaninfo/infoRepairApprove?businessId=" + data.id + "&taskId=" + data.taskId);
	return;
    }
    parent.layer.open({
	type: 2,
	area: ['850px', '530px'],
	maxmin: true,
	content: basePath + "/cms/provide/loaninfo/infoRepairApprove?businessId=" + data.id + "&taskId=" + data.taskId
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/provide/loaninfo/viewPerfectlyLoanInfo?infoBillCode=" + data.billCode + "&show=1");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loaninfo/viewPerfectlyLoanInfo?infoBillCode=" + data.billCode + "&show=1"
    });
}

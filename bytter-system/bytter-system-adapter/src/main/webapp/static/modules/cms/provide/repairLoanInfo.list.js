bt.use(['form','datatable','corp','moneyType','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    var dicts = bt.dicts.init();
    bt.corp.init();//单位
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        checkbox:true,
        order: [3, 'desc'],
        ajax: {
            'url': basePath+"/cms/provide/loaninfo/datatableRepair"
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
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"corpName", bSortable: false},
			{data:"billCode", bSortable: true},
            {data:"createDate", bSortable: true},
            {data:"bankName", bSortable:false},
            {data:"curName", bSortable:false},
            {data:"plMoney", bSortable:true, className:"textRight",render: function ( data, type, full, meta ){
            	return bt.money.format(full.plMoney);
            }},
            {data:"plDate", bSortable:true},
            {data:"repayDate", bSortable:true},
            {data:"repayTypeName", bSortable:false},
            {data:"contractTermName", bSortable:false},
            {data:"plTerm", bSortable:false, render: function ( data, type, full, meta ){
            	return full.plTerm + bt.dict('PrecativeLoanInfo.ternUnit',full.ternUnit);
            }},
            {data:"status", bSortable:false, render: function ( data, type, full, meta ){
            	return bt.dict('cms.bizStatus', full.bizStatus);
            }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '';
            	if(bt.isResource('cms:provide:info:edit')){
            		html = '<a class="" href="#" data-row="'+meta.row+'" onclick="edit(this)">'+bt.lang("common.button.makeup")+'</a>'
            	}
            	html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
            	// html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="bt.workFlow.trackFlowByBusinessId(\''+ full.id +'\')">'+bt.lang("common.button.trackFlow")+'</a>';
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
                    colNum: '6', //汇总列序号
                    dataName: 'plMoney' //汇总列借款金额
                }
            ]
        }
    });
});

function edit(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms.loan.repair.data'),basePath + "/cms/provide/loaninfo/repair?id=" + data.id);

    }
}

function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/provide/loaninfo/viewPerfectlyLoanInfo?infoBillCode=" + data.billCode+"&show=1");

    }
}

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);

        }
	}
}

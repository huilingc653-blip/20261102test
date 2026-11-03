bt.use(['form','datatable','corp','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();//单位
    bt.moneyType.init();

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        checkbox:true,
        ajax: {
            'url': basePath+"/cms/provide/loaninfo/listChangeDataTable"
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
            {data:"corpName"},
			{data:"billCode"},
            {data:"bankName"},
            {data:"curName"},
            {data:"plMoney",className:"textRight",render: function ( data, type, full, meta ){
            	return bt.money.format(full.plMoney);
            }},
            {data:"plDate"},
            {data:"repayDate"},
            {data:"repayTypeName",render: function ( data, type, full, meta ){
                    return bt.empty(full.repayTypeName)? full.repayTypeName1:full.repayTypeName;
                }},
            {data:"contractTerm",
                render: function (data, type, full, meta) {
                    return bt.dict("Provide.ContractTerm",full.contractTerm);
                }
            },
            {data:"plTerm",render: function ( data, type, full, meta ){
            	var htm = ""
                	if(full.plTerm == null){
                		htm += 0+"";
                	}else{
                		htm += full.plTerm;
                	}
                	return htm + bt.dict('PrecativeLoanInfo.ternUnit',full.ternUnit);
            }},
            {data:"status",render: function ( data, type, full, meta ){
                    return bt.dict('cms.bizStatus', full.bizStatus);
                }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '';
            	if(bt.isResource('cms:provide:info:edit')){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="edit(this)">' + bt.lang("变更") + '</a> ';
            	}
            	html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
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
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/provide/loaninfo/changeLoanInfo?infoBillCode=" + data.billCode);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loaninfo/changeLoanInfo?infoBillCode=" + data.billCode,
        end:function(){
            datatable && datatable.draw();
        }
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

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);

        }
	}
}

bt.use(['form','datatable','moneyType','btdate','corp','moneyType'], function(){
    bt.corp.init();
    bt.dateType.init();
    bt.dateType.init();
    moneyType=bt.moneyType.init();
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [1,'desc'],
        ajax: {
            'url': basePath+"/cms/loan/cmsloansend/loanContractList"
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
            {data:"corpName",className:'center',bSortable:true},
            {data:"netName",className:'center',bSortable:true},
            {data:"loanCode",className:'center',bSortable:true},
            {data:"billCode",className:'center',bSortable:true},
            {data:"createDate",className:'center',bSortable:true},
            {data:"curName",className:'center',bSortable:true},
            {data:"loanMoney",className:'textRight',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.loanMoney);
                }
            },
            {data:"remainMoney",className:'textRight',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return moneyFun.format(moneyFun.sub(full.loanMoney,full.repayedMoney));
                }
            },
            {data:"loanDate",className:'center',bSortable:true},
            {data:"repayDate",className:'center',bSortable:true},
            {data:"repayTypeName",className:'center',bSortable:true},
            {data:"loanClass",className:'center',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.LoanClass",full.loanClass);
                }
            },
            /*{data:"loanType",className:'center',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("Cms.PlType.A",full.loanType);
                }
            },*/
            {data:"loanTerm",className:'center',bSortable:true,
            	render: function ( data, type, full, meta ) {
            		if(full.ternUnit==2){
            			return full.loanTerm + bt.lang("cms.loan.ternUnit.day");
            		}else if(full.ternUnit==1){
            			return full.loanTerm + bt.lang("cms.loan.ternUnit.month");
            		}else{
            			return full.loanTerm;
            		}
                }
            },
            {data:"bizStatus",className:'center',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.bizStatus",full.bizStatus);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '';
            	if(bt.isResource('cms:loan:cmsloancontract:edit',full.corpId)){
	            	if(full.status==95){
	            		html = ' <a class="" href="#" data-row="'+meta.row+'" onclick="addContract(this)">'+bt.lang("cms.loanContract.add")+'</a>';
	            	}else{
	            		html = ' <a class="" href="#" data-row="'+meta.row+'" onclick="updateContract(this)">'+bt.lang("cms.loanContract.update")+'</a>\
	            		<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="printContract(this)">'+bt.lang("cms.loanContract.print")+'</a>';
	            	}
            	}
            	html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>'
            	+ '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
            	// html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: false,//已选汇总功能开启
            outerData: true,
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '7', //汇总列序号
                    dataName: 'loanMoney' //汇总列数据
                },
                {
                    colNum: '8', //汇总列序号
                    dataName: 'remainMoney' //汇总列数据
                }
            ]
        }
    });
});

//查看
function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/loan/cmsloansend/view/" + data.id);
            return;
        }
	    parent.layer.open({
	        type: 2,
	        area: ['850px', '530px'],
	        maxmin: true,
	        content: basePath + "/cms/loan/cmsloaninfo/view/" + data.id
	    });
    }
}
//合同生成
function addContract(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('cms.loanContract.add'),basePath + "/cms/loan/cmscontract/add?contractCode=" + data.contractCode + "&liCode="+data.billCode + "&corpId="+data.corpId);
            return;
        }
    }
}
//合同修改
function updateContract(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('cms.loanContract.update'),basePath + "/cms/loan/cmscontract/update?liCode=" + data.billCode);
            return;
        }
    }
}
//合同打印
function printContract(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('cms.loanContract.print'),basePath + "/cms/loan/cmscontract/print?liCode=" + data.billCode);
            return;
        }
    }
}

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);
            return;
        }
	}
}

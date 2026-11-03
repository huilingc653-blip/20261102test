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
            'url': basePath+"/cms/provide/loansend/datatable"
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
            {data:"loanCode"},
            {data:"bankName"},
            {data:"createDate"},
            {data:"curName"},
            {data:"contractSumMoney",className:"textRight",render: function ( data, type, full, meta ){
            	return bt.money.format(full.contractSumMoney);
            }},
            {data:"waitRepayMoney",className:"textRight",render: function ( data, type, full, meta ){
            	return bt.money.format(full.waitRepayMoney);
            }},
            {data:"loanSendOutTypeName"},
            {data:"contractCode"},
            {data:"money",className:"textRight",render: function ( data, type, full, meta ){
            	return bt.money.format(full.money);
            }},
            {data:"repayTypeName"},
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
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a> ';
            	if(bt.isResource('cms:provide:info:edit')){
            		if(full.status == null || full.status == "" || full.status == -1 || full.status == 0 || full.status == 96){//暂存 打回 没有数据 才能操作
            			if(full.status == 0 || full.status == -1 || full.status == 96){
            				html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="edit(this)">'+bt.lang("cms.loan.applyModify")+'</a> ';
            			}else{
            				html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="edit(this)">'+bt.lang("cms.loan.loanApply")+'</a> ';
            			}

            			if(full.contractSumMoney == full.waitRepayMoney){
            				html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="back(this)">'+bt.lang("common.button.back")+'</a>';
            			}else{

            			}
            		}
            	}
            	html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
            	// if(full.id != null && full.id != ""){
            	// 	html += '\<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
            	// }
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
                    dataName: 'contractSumMoney' //汇总列合同金额
                },
                {
                    colNum: '7', //汇总列序号
                    dataName: 'waitRepayMoney' //汇总列待放款金额
                },
                {
                    colNum: '10', //汇总列序号
                    dataName: 'money' //汇总列发放金额
                }
            ]
        }
    });
});
function edit(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('table.cmsProvideLoanSend.sendApply'),basePath + "/cms/provide/loansend/edit?sendBillCode="+data.billCode+"&billCode="+data.loanCode);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loansend/edit/"+data.billCode,
        end:function(){
            datatable && datatable.draw();
        }
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

function back(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();

    parent.layer.confirm(bt.lang('common.message.confirm.back'),function(){
    	bt.loading.show();
        $.post(basePath + "/cms/provide/loansend/back/" + data.loanCode, function(result){
        	bt.navtab.refreshCurrentTable();
            parent.layer.msg(result.msg, {icon: result.success?1:2});
            bt.loading.hide();
        },'JSON');
    });
}

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.loanCode+","+data.billCode);

        }
	}
}

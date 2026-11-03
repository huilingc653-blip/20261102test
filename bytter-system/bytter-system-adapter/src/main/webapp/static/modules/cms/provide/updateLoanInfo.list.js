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
            'url': basePath+"/cms/provide/loaninfo/datatableUpdate"
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
            {data:"createDate"},
            {data:"bankName"},
            {data:"curName"},
            {data:"plMoney",className:"textRight",render: function ( data, type, full, meta ){
            	return bt.money.format(full.plMoney);
            }},
            {data:"plDate"},
            {data:"repayDate"},
            {data:"repayTypeName"},
            {data:"contractTermName"},
            {data:"plTerm",render: function ( data, type, full, meta ){
            	var htm = ""
                	if(full.plTerm == null){
                		htm += 0+"";
                	}else{
                		htm += full.plTerm;
                	}
                	return htm + full.ternUnitName;
            }},
            {data:"status",render: function ( data, type, full, meta ){
            	return full.bizStatusName;
            }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '';
            	if(bt.isResource('cms:provide:info:edit')){
            		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="edit(this)">'+bt.lang("common.button.edit")+'</a> ';
            	}
            	if(bt.isResource('cms:provide:info:delete')){
            		html += ' \<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="del(this)">'+bt.lang("common.button.delete")+'</a>';
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
        bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/provide/loaninfo/update/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/loaninfo/update/" + data.id,
        end:function(){
            datatable && datatable.draw();
        }
    });
}

function del(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
    	bt.loading.show();
        $.post(basePath + "/cms/provide/loaninfo/delete/" + data.id, function(result){
        	bt.loading.hide();
        	bt.navtab.refreshCurrentTable();
            parent.layer.msg(result.msg, {icon: result.success?1:2});
        },'JSON');
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
            return;
        }
	}
}
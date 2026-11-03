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
        order: [2,'desc'],
        ajax: {
            'url': basePath+"/cms/loan/cmsloaninfo/listdatatable?pageType="+$("#pageType").val()
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
			{data:"corpName",className:'center',bSortable:false},
            {data:"netName",className:'center',bSortable:false},
            {data:"billCode",className:'center',bSortable:true},
            {data:"createDate",className:'center',bSortable:true},
            {data:"curName",className:'center',bSortable:false},
            {data:"loanMoney",className:'textRight',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return bt.money.format(full.loanMoney);
                }
            },
            {data:"loanDate",className:'center',bSortable:true},
            {data:"repayDate",className:'center',bSortable:true},
            {data:"repayTypeName",className:'center',bSortable:false,
            },
            {data:"loanClass",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.LoanClass",full.loanClass);
                }
            },
            /*{data:"loanType",className:'center',
            	render: function ( data, type, full, meta ) {
                    return bt.dict("Cms.PlType.A",full.loanType);
                }
            },*/
            {data:"loanTerm",className:'center',bSortable:false,
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
            {data:"bizStatus",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.bizStatus",full.bizStatus);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	if(bt.isResource('cms:loan:cmsloanrepair:edit',full.corpId)){
	            	if(full.status == "0" || full.status == "-1"){
	            		html +='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>\
	            		<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
	                }else if(full.status == "95"){
	                	html +='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="repair(this)">'+bt.lang("common.button.makeup")+'</a>';
	                }
            	}
            	html +='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
            	// html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: false,//已选汇总功能关闭
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '6',//汇总列序号
                    dataName: 'loanMoney',//汇总列贷款金额
                }
            ]
        }
    });
});
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/loan/cmsloaninfo/view/" + data.id);
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
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/loan/cmsloaninfo/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/loan/cmsloaninfo/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/loan/cmsloaninfo/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function repair(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('cms.loan.repair.data'),basePath + "/cms/loan/cmsloaninfo/repairData?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/loan/cmsloaninfo/repairData?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
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


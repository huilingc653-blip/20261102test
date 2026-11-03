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
            'url': basePath+"/cms/loan/cmsloaninfo/listCmsLoanStaySendData"
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
			{data:"corpName",className:'center',},
            {data:"netName",className:'center',},
            {data:"billCode",className:'center'},
            {data:"createDate",className:'center'},
            {data:"curName",className:'center',},
            {data:"loanMoney",className:'textRight',
            	render: function ( data, type, full, meta ) {
                    return bt.money.format(full.loanMoney);
                }
            },
            {data:"sendMoney",className:'textRight',
            	render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.sendMoney);
                }
            },
            {data:"loanDate",className:'center',},
            {data:"repayDate",className:'center',},
            {data:"loanSendOutType",className:'center',
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.loan.loanSendOutType",full.loanSendOutType);
                }
            },
            {data:"repayTypeName",className:'center'},
            {data:"loanTerm",className:'center',
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
            {data:"sendTime",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
            		if(full.sendTime>0){
            			return "<a href='javascript:void(0)' onclick='sendInfolist(\""+full.billCode+"\")' ><u>"+full.sendTime+"</u></a>";
            		}else{
            			return full.sendTime;
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
            	if(bt.isResource('cms:loan:cmsloansend:edit',full.corpId)){
	            	html +='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="loanApply(this)">'+bt.lang("cms.loan.loanApply")+'</a>';
	            	if(full.sendTime==0){
	            		html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="doBack(this)">'+bt.lang("common.button.back")+'</a>';
	            	}
            	}
            	html +='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
            	// html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: false,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '6', //汇总列序号
                    dataName: 'loanMoney' //汇总列数据
                },
                {
                    colNum: '7', //汇总列序号
                    dataName: 'sendMoney' //汇总列数据
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
//放款申请
function loanApply(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('cms.loan.loanApply'),basePath + "/cms/loan/cmsloaninfo/addloansend?id=" + data.id+"&sendBillCode="+data.sendBillCode);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/loan/cmsloaninfo/addloansend?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
//放款打回
function doBack(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	parent.layer.confirm(bt.lang('common.message.confirm.repulse'),function(){
        $.post(basePath + "/cms/loan/cmsloaninfo/sendBack/" + data.id, function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                	bt.navtab.refreshCurrent();
            }
        },'JSON');
    });
}
//还款列表
function sendInfolist(billCode){
	if(billCode){
		if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath+"/cms/loan/cmsloaninfo/sendInfolist?billCode="+billCode);
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

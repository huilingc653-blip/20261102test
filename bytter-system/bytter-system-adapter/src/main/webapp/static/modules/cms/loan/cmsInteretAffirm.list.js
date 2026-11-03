bt.use(['form','datatable','moneyType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [2,'desc'],
        ajax: {
            'url': basePath+"/cms/loan/cmsloanrepay/interetAffirm"
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
            {data:"loanCode",className:'center',bSortable:true},
            {data:"createDate",className:'center',bSortable:true},
            {data:"curName",className:'center',bSortable:false},
            {data:"loanMoney",className:'textRight',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.loanMoney);
                }
            },
            {data:"repayMoney",className:'textRight',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.repayMoney);
                }
            },
            {data:"repayAccrual",className:'textRight',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.repayAccrual);
                }
            },
            {data:"repayDate",className:'center',bSortable:true},
            {data:"repaywayName",className:'center',bSortable:false},
            {data:"bizStatus",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.bizStatus",full.bizStatus);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	if(bt.isResource('cms:loan:interetaffirm:edit',full.corpId)){
	            	html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="repayAffirm(this)">'+bt.lang("cms.loan.repayAffirm")+'</a>\
	                <span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="doBack(this)">'+bt.lang("common.button.back")+'</a>';
            	}
            	html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
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
                    dataName: 'loanMoney' //汇总列发放金额
                },
                {
                    colNum: '7', //汇总列序号
                    dataName: 'repayMoney' //汇总列还款金额
                },
                {
                    colNum: '8', //汇总列序号
                    dataName: 'repayAccrual' //汇总列还款利息
                }
            ]
        }
    });
});
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/loan/cmsloansend/view/" + data.sendId);
            return;
        }
	    parent.layer.open({
	        type: 2,
	        area: ['850px', '530px'],
	        maxmin: true,
	        content: basePath + "/cms/loan/cmsloansend/view/" + data.sendId
	    });
    }
}
function repayAffirm(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang("cms.loan.repayAffirm"),basePath + "/cms/loan/cmsloanrepay/repayAffirm?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['750px', '530px'],
            maxmin: true,
            content: basePath + "/cms/loan/cmsloanrepay/repayAffirm?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
//利息确认打回
function doBack(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	parent.layer.confirm(bt.lang('common.message.confirm.repulse'),function(){
        $.post(basePath + "/cms/loan/cmsloanrepay/back/" + data.id, function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                	bt.navtab.refreshCurrent();
            }
        },'JSON');
    });
}
//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.sendCode);
            return;
        }
	}
}

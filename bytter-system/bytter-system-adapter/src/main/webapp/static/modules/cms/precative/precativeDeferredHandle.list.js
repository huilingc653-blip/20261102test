bt.use(['form','datatable','btdate','corp','moneyType','rateType','dateType','selectType','date'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
       
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/precative/precativeloandeferred/queryHandle"
        },
        columns: [
            {data:"voucherNo",bSortable:false,className:'center'},
            {data:"billCode",bSortable:false,className:'center'},
            {data:"contractCode",bSortable:false,className:'center'},
            {data:"loanOppName",bSortable:false,className:'center'},
            {data:"precativeName",bSortable:false,className:'center'},
            {data:"depositAcc",bSortable:false,className:'center'},
            {data:"loanAcc",bSortable:false,className:'center'},
            {data:"curName",bSortable:false,className:'center'},
            {data:"money",bSortable:true,className:'textRight',
            	render: function (data, type, full, meta) {
            		return moneyFun.format(full.money);
            	}
            },
            {data:"term",bSortable:false,className:'center',
            	render : function(data,type,full,meta){
                    if (full.ternUnit == 'D') {
                        return full.term + bt.lang("cms.loan.ternUnit.day");
                    } else if (full.ternUnit == 'M') {
                        return full.term + bt.lang("cms.loan.ternUnit.month");
                    } else {
                        return full.term;
                    }
            	}
            },
            {data:"startDate",bSortable:true,className:'center'},
            {data:"endDate",bSortable:true,className:'center'},
            {data:"deferredRepayDate",bSortable:true,className:'center'},
            {data:"bizStatus",bSortable:false,className:'center',
            	render : function(data,type,full,meta){
            		return bt.dict("cms.bizStatus",full.bizStatus)
            	}
            },
            {data:null,bSortable:false,className:'center',
            	render: function (data, type, full, meta) {
	                var html = '';
	                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
	               
	            	   html +='\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="Handle(this)">'+bt.lang("common.button.deferred.handle")+'</a>';
	            	   html +=' \<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';     
	                return html;
            	}
            }
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
                    colNum: '9', //汇总列序号
                    dataName: 'money' //汇总列发放金额
                }
            ]
        }
    });
});

function Handle(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.deferred.handle'),basePath + "/cms/precative/precativeloandeferred/handle?voucherNo="+ data.voucherNo+"&sendNo="+data.sendNo+"&billCode="+data.billCode);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/precative/cmsprecativeloandeferred/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.deferred.update'),basePath + "/cms/precative/precativeloandeferred/view?voucherNo="+ data.voucherNo+"&sendBillCode="+data.sendNo+"&billCode="+data.billCode);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/precative/precativeloandeferred/view?voucherNo="+ data.voucherNo+"&sendBillCode="+data.sendNo+"&billCode="+data.billCode
    });
}

function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);
            return;
        }
	}
}

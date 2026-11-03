bt.use(['form','datatable','corp','money','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    bt.corp.init();
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
            'url': basePath+"/cms/precative/precativeloandeferred/Additional?pageType="+$("#pageType").val()
        },
        columns: [
            {data:"billCode",bSortable:false,className:'center'},
            {data:"sendNo",bSortable:false,className:'center'},
            {data:"precativeName",bSortable:false,className:'center'},
            {data:"loanOppName",bSortable:false,className:'center'},
            {data:"sendDate",bSortable:false,className:'center'},
            {data:"deadlineDate",bSortable:false,className:'center'},
            {data:"deferredRepayDate",bSortable:true,className:'center'},
            {data:"deferredLimt",bSortable:false,className:'center',
            	render : function(data,type,full,meta){
                    if (full.ternUnit == 'D') {
                        return full.deferredLimt + bt.lang("cms.loan.ternUnit.day");
                    } else if (full.ternUnit == 'M') {
                        return full.deferredLimt + bt.lang("cms.loan.ternUnit.month");
                    } else {
                        return full.deferredLimt;
                    }
            	}
            },
            {data:"money",bSortable:true,className:"textRight",
            	render: function (data, type, full, meta) {
            		return moneyFun.format(full.money);
            	}
            },
            //{data:"createDate",bSortable:true,className:'center'},
            {data:"bizStatus",bSortable:false,className:'center',
            	render : function(data,type,full,meta){
            		return bt.dict("cms.bizStatus",full.bizStatus)
            	}
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '';
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                if(bt.isResource('cms:precative:deferred:addit:edit')){
            	   html +='\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="Apply(this)">'+bt.lang("common.button.makeup")+'</a>';
                }
                html +=' \<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
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
                    colNum: '9', //汇总列序号
                    dataName: 'money' //汇总列发放金额
                }
            ]
        }
    });
});

function Apply(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.deferred.makeup'),basePath + "/cms/precative/precativeloandeferred/queryAdditional?voucherNo="+ data.voucherNo+"&sendNo="+data.sendNo+"&billCode="+data.billCode);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/precative/precativeloandeferred/queryAdditional?voucherNo="+ data.voucherNo+"&sendNo="+data.sendNo+"&billCode="+data.billCode ,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/cms/precative/precativeloandeferred/view?sendBillCode=" + data.sendNo +"&billCode="+data.billCode);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/precative/precativeloandeferred/view?sendBillCode=" + data.sendNo +"&billCode="+data.billCode
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


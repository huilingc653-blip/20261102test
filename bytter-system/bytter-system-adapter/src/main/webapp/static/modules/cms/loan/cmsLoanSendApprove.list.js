bt.use(['form','datatable','moneyType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'rtlip',
        colReorder: true,
        checkbox:true,order: [1,'desc'],
        ajax: {
            'url': basePath+"/cms/loan/cmsloansend/listSendApproveData"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        columns: [
            {data:"loanCode",className:'center',bSortable:false},
            {data:"billCode",className:'center',bSortable:false},
            {data:"createDate",className:'center',bSortable:false},
            {data:"curName",className:'center',bSortable:false},
            {data:"loanInfoMoney",className:'textRight',bSortable:false,
        		render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.loanInfoMoney);
                }
            },
            {data:"loanMoney",className:'textRight',bSortable:false,
        		render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.loanMoney);
                }
            },
            {data:"loanDate",className:'center',bSortable:false},
            {data:"repayDate",className:'center',bSortable:false},
            {data:"loanRate",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
                    return moneyFun.format((full.loanRate-0) * 100,{decimal:4}) +"%";
                }
            },
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
            {data:"corpName",className:'center',bSortable:false},
            {data:"netName",className:'center',bSortable:false},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
            	if($("#flowStatus").val()=='todo'){
            		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
            	}
                if($("#flowStatus").val() == 'pass'){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }else {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }
                return html;
            }}
        ],
        summary:{
            pageSum: false,//小计功能关闭
            allSum: true,//汇总功能开启
            selectSum: true,//已选汇总功能开启
            // groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '6',//汇总列序号
                    dataName: 'loanMoney',//汇总列数据
                }
            ]
        }
    });
});

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/cms/loan/cmsloansend/approve?id=" + data.id+"&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/loan/cmsloaninfo/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}

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
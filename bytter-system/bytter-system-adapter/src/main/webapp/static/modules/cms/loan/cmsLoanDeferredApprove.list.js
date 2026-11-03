bt.use(['form','datatable','moneyType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'rtlip',
        colReorder: true,
        checkbox:true,order: [1,'desc'],
        ajax: {
            'url': basePath+"/cms/loan/cmsloandeferred/listDeferredApproveData"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        columns: [
            {data:"billCode",className:'center',bSortable:true},
            {data:"liCode",className:'center',bSortable:false},
            {data:"createDate",className:'center',bSortable:false},
            {data:"matureDate",className:'center',bSortable:false},
            {data:"deferredRepayDate",className:'center',bSortable:false},
            {data:"deferredLimt",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
            		if(full.ternUnit==2){
            			return full.deferredLimt + bt.lang("cms.loan.ternUnit.day");
            		}else if(full.ternUnit==1){
            			return full.deferredLimt + bt.lang("cms.loan.ternUnit.month");
            		}else{
            			return full.deferredLimt;
            		}
                }
            },
            {data:"contractCode",className:'center',bSortable:false},
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
        ]
    });
});

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/cms/loan/cmsloandeferred/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/loan/cmsloandeferred/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/loan/cmsloandeferred/view/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/loan/cmsloandeferred/view/" + data.id
        });
    }
}

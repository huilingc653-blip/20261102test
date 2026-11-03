bt.use(['form','datatable','btdate','money','dateType', 'corp'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        ajax: {
            'url': basePath+"/cms/rat/ratbkrationadjust/dataTableApprove"
        },
        buttons: [
        ],
        columns: [
            {data:"adjustCode",bSortable:false,className:'center'},
            {data:"businessType",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("ratBkration.adjustType",full.businessType);
                }
            },
            {data:"corpName",bSortable:false,className:'center'},
            {data:"bankName",bSortable:false,className:'center'},
            {data:"curName",bSortable:false,className:'center'},
            {data:"createDate",bSortable:false,className:'center'},
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
    
    bt.dateType.init();
});
/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
    	bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/cms/rat/ratbkrationadjust/viewApprove?businessId=" + data.id + "&taskId=" + data.taskId);
    	return;
    }
    parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/cms/rat/ratbkrationadjust/viewApprove?businessId=" + data.id + "&taskId=" + data.taskId
    });
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        var businessType=$("#businessType").val();//业务种类（0-授信录入，1-授信分配）
        if(businessType=="0"){//授信录入调整
            bt.navtab.addNavtab(bt.lang('ration.bkration.view'),basePath + "/cms/rat/ratbkration/view/" + data.id);
        }else if(businessType=="1"){
            bt.navtab.addNavtab(bt.lang('ration.bkrationdis.view'),basePath + "/cms/rat/ratbkrationdis/view/" + data.id);
        }
        return;
    }
}

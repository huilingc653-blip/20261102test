bt.use(['form','datatable','dateType','moneyType','corp'], function(){
    $ = bt.jquery,form = bt.form();
    //时间
    bt.dateType.init();
    bt.corp.init();
    
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'rtlip',
        colReorder: true,
        checkbox:true,order: [1,'desc'],
        ajax: {
            'url': basePath+"/cms/provide/provideloandeferred/dataTableApprove"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        columns: [
            {data:"billCode",className:'center',bSortable:false},
            
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	return '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>';
            }}
        ]
    });
});
 
/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
	bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/cms/provide/provideloandeferred/listApprove?businessId=" + data.id + "&taskId=" + data.taskId);
	return;
    }
    parent.layer.open({
	type: 2,
	area: ['850px', '530px'],
	maxmin: true,
	content: basePath + "/cms/provide/provideloandeferred/listApprove?businessId=" + data.id + "&taskId=" + data.taskId
    });
}

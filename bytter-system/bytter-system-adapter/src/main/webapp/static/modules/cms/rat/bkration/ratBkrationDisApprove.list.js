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
            'url': basePath+"/cms/rat/ratbkrationdis/dataTableApprove"
        },
        buttons: [
        ],
        columns: [
            {data:"disCode",bSortable:false,className:'center'},
            {data:"curName",bSortable:false,className:'center'},
            {data:"money",bSortable:false,className:'textRight',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(data);
            	}
            },
            {data:"createDate",bSortable:false,className:'center'},
            {data:"corpName",bSortable:false,className:'center'},
            {data:"ratCode",bSortable:false,className:'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
            	if($("#flowStatus").val()=='todo'){
            		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
            	}
                if($("#flowStatus").val() == 'pass'){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDis(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
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
                    colNum: '3',//汇总列序号
                    dataName: 'money',//汇总列数据
                }
            ]
        }
    });
    
    var corpInit = bt.corp.init();
    bt.dateType.init();
});
/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
    	bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/cms/rat/ratbkrationdis/viewBkrationDisApprove?businessId=" + data.id + "&taskId=" + data.taskId);
    	return;
    }
    parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/cms/rat/ratbkrationdis/viewBkrationDisApprove?businessId=" + data.id + "&taskId=" + data.taskId
    });
}

function viewDis(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationdis.viewDis'),basePath + "/cms/rat/ratbkrationdis/viewDis/" + data.id);
        return;
    }
    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationdis/viewDis/"+data.id,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}

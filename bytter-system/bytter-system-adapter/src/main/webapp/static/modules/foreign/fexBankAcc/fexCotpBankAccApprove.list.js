var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','selectType','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    selectTypes = bt.selectType.init();// 下拉框
    // 生成列表
    datatable = bt.datatable.init({  
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/foreign/fexcotpbankacc/approveTables"
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
            {data:"cotpName",className:'center',bSortable:false},
            //{data:"corpName",className:'center',bSortable:false},
            {data:"bankAcc",className:'center',bSortable:false},
            {data:"accName",className:'center',bSortable:false},
            {data:"curName",className:'center',bSortable:false},
            {data:"bankName",className:'center',bSortable:false},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
              	if($("#flowStatus").val()=='todo'){
              		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
              	}
                if($("#flowStatus").val() == 'pass'){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }else {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }
              	return html;
              }}
        ]
    });
    bt.workFlow.beforeSubmit = function(submitData){   
    	var data = bt.datatable.getSelectData("#appTable");
        if(data.length < 1){
        	layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 2});
        	return;
        }
    	var ids = []; 
    	var taskIds = []; 
    	// 遍历行数据
    	$.each(data,function(i, tr) {
    		taskIds.push(tr.taskId);
    	});
    	bt.workFlow.taskList = taskIds;
    	return true;
    }
});

/**
 * 审核
 * @param el
 */
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/foreign/fexcotpbankacc/viewApprove?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcotpbankacc/viewApprove?id=" + data.id + "&taskId=" + data.taskId
    });
}

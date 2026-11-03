var datatable;
bt.use(['form','dateType','corp','moneyType','datatable'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        checkbox:true,
        order: [[1,'desc']],
        ajax: {
            'url': basePath+"/budget/fbsBudgetProcess/dataTableApprove"
        },
        buttons: []
        ,columns: [
            {data:"corpName",bSortable:true},
            {data:"itemCode",bSortable:true},
            {data:"itemName",bSortable:true},
            {data:"processMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.processMoney);
                }
            },
            {data:"processDate",bSortable:true,className:'center'},
            {data:"billNo",bSortable:true,className:'center'},
            {data:"createDate",bSortable:true,className:'center'},
            // {data:"statusName",bSortable:true},
            {data:"rmk",bSortable:true},
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
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '4',
                    dataName: 'processMoney'
                }
            ]
        }
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

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/budget/fbsBudgetProcess/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/budget/fbsBudgetProcess/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}



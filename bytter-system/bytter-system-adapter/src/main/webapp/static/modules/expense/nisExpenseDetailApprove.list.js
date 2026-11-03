bt.use(['form','datatable','dateType','selectType','money','bank'], function(){
    $ = bt.jquery;
    bt.selectType.init();
    bt.dateType.init();
    bt.bank.init({
        elem:'.bank',
    });

    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/expense/nisexpensedetail/dataTableApprove"
        },
        buttons: [
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"expenseId",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"expenseDate",bSortable:true},
            {data:"expenseMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.expenseMoney);
                }
            },
            {data:"accName",bSortable:true},
            {data:"bankAcc",bSortable:true},
            {data:"bankName",bSortable:true},
            {data:"bankCode",bSortable:true},
            {data:"busTypeName",bSortable:true},
            {data:"opTypeName",bSortable:true},
            {data:"purpose",bSortable:true},
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
                    dataName: 'expenseMoney'
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

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/expense/nisexpensedetail/view/" + data.id
    });
}

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/expense/nisexpensedetail/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/expense/nisexpensedetail/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}



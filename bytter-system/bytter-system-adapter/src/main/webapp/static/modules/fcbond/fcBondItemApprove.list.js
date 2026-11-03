bt.use(['form','datatable','dateType','selectType','money','bank','corp','dicts'], function(){
    $ = bt.jquery;
    bt.selectType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.corp.init();
    bt.bank.init({
        elem:'.bank',
    });

  //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[10,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bond/fcbondpreserve/bondItemApproveTable"
        },
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"corpName",bSortable:true,className:'center'},
            {data:"itemNo",bSortable:true,className:'center'},
            {data:"startDate",bSortable:true,className:'center'},
            {data:"endDate",bSortable:true,className:'center'},
            {data:"bankAcc",bSortable:true,className:'center'},
            {data:"accName",bSortable:true,className:'center'},
            {data:"bankName",bSortable:true,className:'center'},
            {data:"curId",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return full.curName;
                }},
            {data:"accBalance",bSortable:true,className:'center',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(full.accBalance);
                }},
            {data:"applyMoney",bSortable:true,className:'center',
            	render: function ( data, type, full, meta ) {
            		return bt.money.format(full.applyMoney);
                }},
            {data:"inputDate",bSortable:true,className:'center'},
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
                    colNum: '10',
                    dataName: 'applyMoney'
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

/*审批*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/bond/fcbondpreserve/infoApprove?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bond/fcbondpreserve/infoApprove?id=" + data.id + "&taskId=" + data.taskId
    });
}


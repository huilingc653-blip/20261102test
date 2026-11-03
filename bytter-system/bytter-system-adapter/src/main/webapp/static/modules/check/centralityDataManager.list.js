bt.use(['jquery','datatable',],function(){
	$ = bt.jquery;
	//表格控件初始化设置
	bt.datatable.init({
		elem: "#appTable",
		dom: 'Brtlip',
        colReorder: true,
		checkbox: true ,
		ajax: {
			url: basePath+"/finacial/check/centralityDataManage/datatable"
		},
		buttons: [
		    {
		    	text: '#springMessage("common.button.add")',
		    	action: function( e, dt, node, config ){
		    		add();
		    	}
		    },
		    {
		    	text: '#springMessage("common.button.batchDelete")',
		    	action:function(){
		    		
		    	}
		    }
		],
		columns: [
		    {
		    	data: "voucherNo",
		    	className:'center',
		    	bSortable:true
		    },
		    {
		    	data: "compAcc",
		    	className:'center',
		    	bSortable:true
		    },
		    {
		    	data: "tallyDate",
		    	className:'center',
		    	bSortable:true
		    },
		    {
		    	data: "debitMoney",
		    	className:'center',
		    	bSortable:true
		    },
		    {
		    	data: "creditMoney",
		    	className:'center',
		    	bSortable:true
		    },
		    {
		    	data: "billNo",
		    	className:'center',
		    	bSortable:true
		    },
		    {
		    	data: "redblueSign",
		    	className:'center',
		    	bSortable:true
		    },
		    {
		    	data: "compareSign",
		    	className:'center',
		    	bSortable:true
		    },
		    {
		    	data: null,
		    	bSortable:false,
		    	className:'center',
		    	render: function( data, type, full, meta ){
		    		var buts = "<button name='edit' data-row='"+meta.row+"' onclick='edit(this)' class='layui-btn layui-btn-mini layui-btn-normal'>"+bt.lang("common.button.edit")+"</button>";
		    		   buts += "<button name='del' data-row='"+meta.row+"' onclick='del(this)' class='layui-btn layui-btn-mini layui-btn-danger'>"+bt.lang("common.button.delete")+"</button>";
		    		return buts;
		    	}
		    }
		]
	});
});

//新增
function add(){
	parent.layer.open({
    	title: "#springMessage('finacial.check.centralityDataManager.title.add')",
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/check/centralityDataManage/add"
    });
}

//修改
function edit(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
    	title: "#springMessage('finacial.check.centralityDataManager.title.edit')",
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/check/centralityDataManage/edit/" + data.id
    });
}

//删除
function del(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	parent.layer.confirm("#springMessage('common.message.confirm.delete')",function(){
		var url = basePath + "/finacial/check/centralityDataManage/delete/" + data.id;
        $.post(url, function(result){
        	if(result.success){
        		location.reload();
        	}
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

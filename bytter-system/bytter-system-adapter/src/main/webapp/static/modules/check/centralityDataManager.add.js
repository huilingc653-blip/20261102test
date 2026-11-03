bt.use(['jquery','datatable'],function(){
	$ = bt.jquery;
	//表格控件初始化设置
	bt.datatable.init({
		elem: "#editTable",
		ajax: {
			url: basePath+"/finacial/check/centralityDataManage/datatable"
		},
		buttons: [
		    {
		    	text: bt.lang('common.addTr'),
		    	action: function( e, dt, node, config ){
		    		
		    	}
		    }
		],
		columns: [
		    {
		    	data: "voucherNo",
		    	className:'center',
		    	render:function(data, type, row, meta){
		    		var ht  = '<div class="layui-input-inline">'
		    			ht += '<input name="voucherNo" class="layui-input text" type="text">';
		    			ht += '<table id="example"><thead><tr>';	
		    			ht += '<th>'+bt.lang("common.button.delete")+'</th><th>'+bt.lang("common.button.delete")+'</th><th>'+bt.lang("common.button.delete")+'</th>';    
		    	        ht += '</tr></thead></table>';        
		    	        ht += '</div>';  
		    		return ht;
		    	}
		    },
		    {
		    	data: "compAcc",
		    	className:'center',
		    	render:function(data, type, row, meta){
		    		return "<input name='compAcc' id='' class='layui-input' disabled>";
		    	}
		    },
		    {
		    	data: "tallyDate",
		    	className:'center',
		    	render:function(data, type, row, meta){
		    		return "";
		    	}
		    },
		    {
		    	data: "debitMoney",
		    	className:'center',
		    	render:function(data, type, row, meta){
		    		return "";
		    	}
		    },
		    {
		    	data: "creditMoney",
		    	className:'center',
		    	render:function(data, type, row, meta){
		    		return "";
		    	}
		    },
		    {
		    	data: "redblueSign",
		    	className:'center',
		    	render:function(data, type, row, meta){
		    		return "";
		    	}
		    },
		    {
		    	data: "billNo",
		    	className:'center',
		    	render:function(data, type, row, meta){
		    		return "";
		    	}
		    },
		    {
		    	data: "compareSign",
		    	className:'center',
		    	render:function(data, type, row, meta){
		    		return "";
		    	}
		    },
		    {
		    	data: null,
		    	className:'center',
		    	render: function( data, type, full, meta ){
		    		var buts = "<button name='del' data-row='"+meta.row+"' onclick='del(this)' class='layui-btn layui-btn-mini layui-btn-danger'>"+bt.lang("common.button.delete")+"</button>";
		    		return buts;
		    	}
		    }
		]
	});
	
	//下拉表格初始化
	var selectTableInit = function(rowid){
		/*bt.datatable.init({
			elem: "#selectTable_"+rowid,
			ajax: {url: basePath+"/finacial/check/centralityDataManage/"},
			coumnls: [
			    {data:"name"},
			    {data:"name"},
			    {data:"name"}
			],
		});*/
	}
	
	//时间控件初始化
	var dateInit = function(rowid){
		bt.btdate.init({
			elem: "#",
			name: "",
		});
	}
	
});








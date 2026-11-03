var billTable={},corp;
bt.use(['form','datatable','btdate','select','dateType','dicts','selectType'],function(){
	  $ = bt.jquery,form = bt.form();
	    bt.dicts.init();
	    bt.dateType.init();
	    bt.selectType.init();
    //生成列表
    billTable = bt.datatable.init({ 
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/abpool/pledgee/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
                   {data:"bpCode",bSortable:true},
                   {data:"bpName",bSortable:true},
                   {data:"bpTypeName",bSortable:true},
                   {data:"bankAcc",bSortable:true},
                   {data:"accName",bSortable:true},
                   {data:"bankName",bSortable:true},
                   {data:"bankNo",bSortable:true},
                   {data:"rmk",bSortable:true},
                   {data:"validSignName",bSortable:true},
                   {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                	   
                	   var html='<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                	   if(data.validSign=="1"){
					   	   html = html+' | <a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,0)">'+bt.lang("common.button.taboo")+'</a>';
                	   }else if(data.validSign=="0"){ 
					   	   html = html+' | <a class="" href="#" data-row="'+meta.row+'" onclick="setting(this)">'+bt.lang("billPool.Pledgee.setting")+'</a>';
					   	   html = html+' | <a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,1)">'+bt.lang("common.button.enable")+'</a>';
//					   	   if(data.bankAcc!=null){
//						   	   html = html+' | <a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,2)">'+bt.lang("common.button.delete")+'</a>';
//					   	   } 
                	   }
					    return html;
				   }}
        ],
        fixedColumns:{
        	rightColumns:2
        }
    });
    
});

//查看详细
function view(el){
	 var data =  billTable.row(layui.jquery(el).data("row")).data();
	    parent.layer.open({
	        type: 2,
	        area: ['850px', '459px'],
	        maxmin: true,
	        content: basePath + "/abpool/pledgee/view/" + data.pId,
	        end:function(){
	        	bt.datatable.refreshCurrentPage();
	        }
	  });  
}
function setting(el){
	 var data =  billTable.row(layui.jquery(el).data("row")).data();
	    parent.layer.open({
	        type: 2,
	        area: ['850px', '459px'],
	        maxmin: true,
	        resize: false,
	        content: basePath + "/abpool/pledgee/permitDtl/" + data.pId,
	        end:function(){
	        	bt.datatable.refreshCurrentPage();
	        }
	  });  
}

function operation(el,order){
	 var data =  billTable.row(layui.jquery(el).data("row")).data();
	 if(order==1 && data.id==null){
		 layer.alert(bt.lang('billPool.Pledgee.please.tips'), {icon: 7});
		 return false;
	 }
	 var msg = order==0?"billPool.Pledgee.msg.taboo":"billPool.Pledgee.msg.enable";
	 parent.layer.confirm(bt.lang(msg),function(){
	        $.post(basePath + "/abpool/pledgee/opt",{"id":data.id,"opt":order}, function(result){ 
	        	bt.loading.hide();
	        	   parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
	               if (result.success) {
	                   bt.datatable.refreshCurrentPage();
	               }
	        },'JSON');
	   });  
	    
	    
}

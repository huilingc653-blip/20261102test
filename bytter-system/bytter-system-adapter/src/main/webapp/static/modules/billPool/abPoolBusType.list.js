var billTable={},corp;
bt.use(['form','datatable','btdate','ligerTreeTable','select','dateType','dicts','selectType'],function(){
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
            'url': basePath+"/abpool/bustype/datatable"
        },
        buttons: [
	          {
	              text: bt.lang("common.button.add"),
	              hide:bt.isnotResource('abpool:type:resource:op'),
	              action: function (e, dt, node, config) {
	                  add();
	              }
	          },
	          {
	              text: bt.lang("common.button.delete"),
	              hide:bt.isnotResource('abpool:type:resource:op'),
	              action: function (e, dt, node, config) {
	                  deleteColumn(dt.rows('.selected').data());
	              }
	          },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        
        ,columns: [
                   {data:"bustypeCode",bSortable:true},
                   {data:"bustypeName",bSortable:true},
                   {data:"parentBusName",bSortable:true},
                   {data:"bustypeLevel",bSortable:true},
                   {data:"validSignName",bSortable:true},
                   {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                	   var html='';
                	   html = html+'<a class="" href="#" data-row="'+meta.row+'" onclick="addNextLevel(this)">'+bt.lang("billPool.type.add.next.level")+'</a> |  ';
				   	   if(data.validSign=="0"){
						  html = html+'<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.update")+'</a> |  ';
					   	  html = html+'<a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,1)">'+bt.lang("common.button.enable")+'</a>';
				   	   }else{
					   	  html = html+'<a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,0)">'+bt.lang("common.button.taboo")+'</a>';
				   	   }
					   return html;
				   }}
        ],
        fixedColumns:{
        	rightColumns:2
        }
    });
    
});

function add(){
    parent.layer.open({
        type: 2,
        area: ['850px', '409px'],
        maxmin: true,
        content: basePath + "/abpool/bustype/addOrUpdate/"+null+"?opt=add",
        end:function(){
        	bt.datatable.refreshCurrentPage();
        }
   });
}

function update(el){
	 var data =  billTable.row(layui.jquery(el).data("row")).data();
	    parent.layer.open({
	        type: 2,
	        area: ['850px', '409px'],
	        maxmin: true,
	        content: basePath + "/abpool/bustype/addOrUpdate/" + data.id,
	        end:function(){
	        	bt.datatable.refreshCurrentPage();
	        }
	  }); 
}

//增加下级
function addNextLevel(el){
	 var data =  billTable.row(layui.jquery(el).data("row")).data();
	    parent.layer.open({
	        type: 2,
	        area: ['850px', '459px'],
	        maxmin: true,
	        content: basePath + "/abpool/bustype/addNextLevel/" + data.id,
	        end:function(){
	        	bt.datatable.refreshCurrentPage();
	        }
	  });  
}


//启用禁用
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
	        	if(result.success){
	        		parent.layer.alert(bt.lang("ab.pool.message.success"), {icon: result.success?1:2});
	        		bt.datatable.refreshCurrentPage();
	        	}else{
	        		parent.layer.alert(bt.lang("ab.pool.message.error"), {icon: result.success?1:2});
	        	}
	        },'JSON');
	   });  
}
//删除
function deleteColumn(data){
	var len = data.length;
	if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
	//校验数据权限
	var rowData = data.splice(0,len);
	
	parent.layer.confirm(bt.lang('billPool.type.message.confirm.delete'),function(){
        var ids;
        for(i=0;i<len;i++){
        	if(ids){
        		ids=ids+","+rowData[i].id;
        	}else{
        		ids=rowData[i].id;
        	}
        }
        $.post(basePath + "/abpool/bustype/batchOpt",{"ids":ids,"opt":"signed"}, function(result){ 
        	if(result.success){
                parent.layer.alert(bt.lang("ab.pool.message.success"), {icon: result.success?1:2});
        		bt.datatable.refreshCurrentPage();
        	}else{
				parent.layer.alert(bt.lang("ab.pool.message.error"), {icon: result.success?1:2});
			}
        },'JSON');
    });
}


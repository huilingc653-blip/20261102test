var billTable={};
bt.use(['form','datatable','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[2,'asc'],[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ras/auditStrategy/datatable"
        }, 
        buttons: [
            {
	            text:bt.lang('common.button.add'),
	            action: function ( e, dt, node, config ) {
	                add();
	            }
            }
            
        ]
        ,columns: [
			{data:"sortNo",bSortable:true},
            {data:"payTypeName",bSortable:true}, 
            {data:"name",bSortable:true},
            {data:"rulesName",bSortable:false},
            {data:"createDate",bSortable:true},
            {data:"validSignName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
            	if(full.validSign=='0'){
            		html+='<a class="" href="#" data-row="'+meta.row+'" onclick="batchValidSign(this,1)">'+bt.lang("common.button.enable")+'</a>';
            	}else{
            		html+='<a class="" href="#" data-row="'+meta.row+'" onclick="batchValidSign(this,0)">'+bt.lang("common.button.taboo")+'</a>';
            	}
        		html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteFun(this)">'+bt.lang("common.button.delete")+'</a>';
            	return html;
            }}
        ]
    });
});

//跳转至添加页面 
function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/ras/auditStrategy/add");
        return;
    }
	parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ras/auditStrategy/add",
        end:function(){
        	bt.datatable.refreshCurrentPage();
        }
    });
}


//编辑更新
function update(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
	if(bt.navtab){
      bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/ras/auditStrategy/update?id=" + data.id);
      return;
  }
  parent.layer.open({
      type: 2,
      area: ['850px', '530px'],
      maxmin: true,
      content: basePath + "/ras/auditStrategy/update?id=" + data.id,
      end:function(){
      	bt.datatable.refreshCurrentPage();
      }
  });
}


//删除数据
function deleteFun(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
    var msgTips='common.message.confirm.delete';
    layer.confirm(bt.lang(msgTips),function(){
    	bt.loading.show();
        $.post(basePath + "/ras/auditStrategy/delete",{"id":data.id}, function(result){
        	if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
        		bt.datatable.refreshCurrentPage();
        	}else{
  	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
        	}
	        bt.loading.hide();
        },'JSON');
    });
}


//设置有效和无效
function batchValidSign(el,validSign){
	var data = billTable.row(layui.jquery(el).data("row")).data();
    var msgTips;
    if(validSign=="1"){
    	msgTips='common.message.confirm.batchEnable';
    }else{
    	msgTips='common.message.confirm.taboo';
    }
    parent.layer.confirm(bt.lang(msgTips),function(){
    	bt.loading.show();
        $.post(basePath + "/ras/auditStrategy/batchValidSign",{"id":data.id,"validSign":validSign}, function(result){
        	if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
        		bt.datatable.refreshCurrentPage();
        	}else{
  	     	   parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});  
        	}
	        bt.loading.hide();
        },'JSON');
    });
}


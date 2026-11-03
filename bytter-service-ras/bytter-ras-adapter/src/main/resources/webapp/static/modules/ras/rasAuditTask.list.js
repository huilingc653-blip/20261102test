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
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ras/auditTask/datatable"
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
			{data:"code",bSortable:true},
            {data:"name",bSortable:true}, 
            {data:"payTypeName",bSortable:false},
            {data:"validSignName",bSortable:true},
            {data:"createDate",bSortable:true},
            {data:"startDate",bSortable:true},
            {data:"lastDate",bSortable:true},
            {data:"runStatusName",bSortable:true},
            {data:"runMsg",bSortable:true},
            {data:"updateName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
            	 if(!(full.runStatus=='R')){//非运行中
            		html+='<a class="" href="#" data-row="'+meta.row+'" onclick="scanparameter(this)">'+bt.lang("common.button.scanparameter")+'</a><span class="text-explode">|</span>';
            		html+='<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
            		
            	}
        		html+='<a class="" href="#" data-row="'+meta.row+'" onclick="clone(this)">'+bt.lang("common.button.clone")+'</a>';
	           	 if(!(full.runStatus=='R')){//非运行中
	           		html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteFun(this)">'+bt.lang("common.button.delete")+'</a>';
	           	 }
            	return html;
            }}
        ]
    });
});

//跳转至添加页面 
function add(){
	parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ras/auditTask/add",
        end:function(){
        	bt.datatable.refreshCurrentPage();
        }
    });
}

//设置扫描参数
function scanparameter(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/ras/auditTask/scanparameter?id=" + data.id,
		end:function(){
			bt.datatable.refreshCurrentPage();
		}
	});
}

//复制功能
function clone(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/ras/auditTask/clone?id=" + data.id,
		end:function(){
			bt.datatable.refreshCurrentPage();
		}
	});
}

//编辑更新
function update(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
  parent.layer.open({
      type: 2,
      area: ['850px', '530px'],
      maxmin: true,
      content: basePath + "/ras/auditTask/update?id=" + data.id,
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
	      $.post(basePath + "/ras/auditTask/delete",{"id":data.id}, function(result){
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


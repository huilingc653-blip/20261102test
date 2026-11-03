var billTable={};
bt.use(['form','datatable','corp','select','users','dicts' ], function(){
    $ = bt.jquery,form = bt.form();
  
    bt.funUserReset=function(){
    	bt.otheruser.get("userName").reset();
    }
    bt.otheruser=bt.users.init();
    bt.corp.init();
    bt.dicts.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true
        ,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/blankvou/bvstoragebox/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                hide:bt.isnotResource('bill:blankvou:bvStorageBox:save'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [           
            {data:"code",bSortable:true},
            {data:"name",bSortable:true},
            {data:"corpName",bSortable:true},
            {data:"keeperName",bSortable:true},
            {data:"address",bSortable:true},          
            {data:"validSignName",bSortable:true,},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	if(data=="1"){
            		if(bt.isResource('bill:blankvou:bvStorageBox:stop')){
            			html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="stop(this)">'+bt.lang("common.button.stop")+'</a>';
            		}
            		if(bt.isResource('bill:blankvou:bvStorageBox:update')){
            			html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
            		}          		
            	}else{
            		if(bt.isResource('bill:blankvou:bvStorageBox:start')){
            			html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="enable(this)">'+bt.lang("common.button.enable")+'</a>';
            		}
            		if(bt.isResource('bill:blankvou:bvStorageBox:delete')){
            			html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
            		} 
            	}
                return html;
            },
           
            }
            
        ],
        fixedColumns:{
            rightColumns:1
        }
    });
});
function add(){
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvstoragebox/add",
        end:function(){
            //location.reload();
//        	billTable.draw();
        	bt.datatable.refreshCurrentPage();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/blankvou/bvstoragebox/view/" + data.id
    });
}

function stop(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.stop'),function(){
        $.post(basePath + "/bill/blankvou/bvstoragebox/stop/" + data.id, function(result){
        	
        	if(result.success){
                parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            }else{
            	parent.layer?parent.layer.alert(bt.lang("bvStorageBox.noStop"), {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            }
            
            bt.datatable.refreshCurrentPage();
            
        },'JSON');
    });
}

function enable(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.enable'),function(){
        $.post(basePath + "/bill/blankvou/bvstoragebox/enable/" + data.id, function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        	bt.datatable.refreshCurrentPage();
        },'JSON');
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    
//    $.post(basePath + "/bill/blankvou/bvstoragebox/isUpdate/" + data.id, function(result){
//    	if(result.success){
//    		 parent.layer.open({
//    		        type: 2,
//    		        area: ['850px', '530px'],
//    		        maxmin: true,
//    		        content: basePath + "/bill/blankvou/bvstoragebox/update?id=" + data.id,
//    		        end:function(){
////    		        	billTable.draw();
//    		        	bt.datatable.refreshCurrentPage();
//    		        }
//    		    });
//        }else{
//        	parent.layer?parent.layer.alert(bt.lang("bvStorageBox.noUpdate"), {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
//        }
//        bt.datatable.refreshCurrentPage();
//    },'JSON');
	 parent.layer.open({
	    type: 2,
	    area: ['850px', '530px'],
	    maxmin: true,
	    content: basePath + "/bill/blankvou/bvstoragebox/update?id=" + data.id,
	    end:function(){
	    	bt.datatable.refreshCurrentPage();
	    }
	});
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/blankvou/bvstoragebox/delete/" + data.id, function(result){
//        	  billTable.draw();
        	bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}




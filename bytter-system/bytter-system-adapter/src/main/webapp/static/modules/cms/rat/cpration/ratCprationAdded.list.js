bt.use(['form','datatable','btdate','money','laypage','dateType'], function(){ 
    $ = bt.jquery,form = bt.form();
    var dateType = bt.dateType.init();
    
    bt.laypage({
		cont: 'page',
		pages: $("#total").val(),
		curr:$("#curr").val(),
		skip:true,
		jump:function(obj, first){
			var curr = obj.curr;
			//var ratDate = dateType.get("ratDate").val();
			var param = "";
			if(!first){
				param = "ratCode="+$("#ratCode").val()+"&start="+curr;
				location.href = basePath + "/cms/rat/ratcprationadd/addList?"+param;
			}
		}
    });
    
    //监听查询
    form.on('submit(search)', function(data){
    	search();
    });
    
    //监听分配按钮
    form.on('submit(add)', function(){
    	if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('ration.bkrationadd.add'),basePath + "/cms/rat/ratcprationadd/add?ratCode=" + $("#ratCode").val());
            return;
        }
        layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/rat/ratcprationadd/add?ratCode="+$("#ratCode").val(),
            end:function(){
                //datatable && datatable.draw();
            }
        });
        return false;
    });
    
    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	bt.navtab.refreshParent();
        	bt.navtab.closeCurrent();
        }
    }
    
});

function search(){
	var param = "";
	param = "start="+$("#curr").val() +"&ratCode="+$("#ratCode").val();
	location.href = basePath + "/cms/rat/ratcprationadd/addList?"+param;
}

function update(id){
	var ratCode = $("#ratCode").val();
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('ration.bkrationadd.edit'),basePath + "/cms/rat/ratcprationadd/update?id=" + id + "&ratCode="+ratCode);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/cms/rat/ratcprationadd/update?id=" + id + "&ratCode="+ratCode,
		end:function(){
			//datatable && datatable.draw();
		}
	});
}
function add(){
	var id = $("#ratCode").val();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationadd.add'),basePath + "/cms/rat/ratcprationadd/add?ratCode=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcprationadd/add?ratCode="+id,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}

function del(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/rat/ratcprationadd/delete/" + id, function(result){
        	if(bt.navtab){
        		bt.navtab.refreshCurrent();
        	}
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function view(id){
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('ration.cprationadd.view'),basePath + "/cms/rat/ratcprationadd/view/" + id);
		return;
	}
    parent.layer.open({
        type: 2,
        area: ['1150px', '830px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcprationadd/view/" + id
    });
}

closeTab = function(){
	if(!bt.navtab){
        parent.layer?parent.layer.closeAll():layer.closeAll();
    } else {
    	//bt.navtab.refreshParent();
    	bt.navtab.closeCurrent();
    }
}

//日志列表
function listMsg(id){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/rat/ratcprationlog/listlog?rationId=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcprationlog/listlog?rationId=" + id
    });
}
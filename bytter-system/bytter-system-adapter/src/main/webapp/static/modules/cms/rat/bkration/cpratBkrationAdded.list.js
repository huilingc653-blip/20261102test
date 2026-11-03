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
			var corpId = $("#corpId").val();
			var param = "";
			if(!first){
				param = "ratCode="+$("#ratCode").val()+"&start="+curr+"&corpId="+corpId;
				location.href = basePath + "/cms/rat/ratbkrationadd/cpaddList?"+param;
			}
		}
    });
    
    //监听查询
    form.on('submit(search)', function(data){
    	var param = "";
    	var corpId = $("#corpId").val();
		param = "start="+$("#curr").val() +"&ratCode="+$("#ratCode").val()+"&corpId="+corpId;
		location.href = basePath + "/cms/rat/ratbkrationadd/cpaddList?"+param;
    });
    
    bt.close = function(){
    	if(!bt.navtab){
            parent.layer?parent.layer.closeAll():layer.closeAll();
        } else {
        	//bt.navtab.refreshParent();
        	bt.navtab.closeCurrent();
        }
    }
    
});

function search(){
	var param = "";
	var corpId = $("#corpId").val();
	param = "start="+$("#curr").val() +"&ratCode="+$("#ratCode").val()+"&corpId="+corpId;
	location.href = basePath + "/cms/rat/ratbkrationadd/cpaddList?"+param;
}

function update(id){
	var ratCode = $("#ratCode").val();
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('ration.bkrationadd.cpedit'),basePath + "/cms/rat/ratbkrationadd/cpupdate?id=" + id + "&ratCode="+ratCode);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/cms/rat/ratbkrationadd/cpupdate?id=" + id + "&ratCode="+ratCode,
		end:function(){
			//datatable && datatable.draw();
		}
	});
}
function add(){
	var corpId = $("#corpId").val();
	var ratCode = $("#ratCode").val();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationadd.cpadd'),basePath + "/cms/rat/ratbkrationadd/cpadd?ratCode=" + ratCode+ "&corpId="+corpId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationadd/cpadd?ratCode="+ratCode+ "&corpId="+corpId,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}

function del(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/rat/ratbkrationadd/delete/" + id, function(result){
        	if(bt.navtab){
        		bt.navtab.refreshCurrent();
        	}
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function view(id){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationadd.cpview'),basePath + "/cms/rat/ratbkrationadd/cpview/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1150px', '830px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationadd/cpview/" + id
    });
}

closeTab = function(){
	if(!bt.navtab){
        parent.layer?parent.layer.closeAll():layer.closeAll();
    } else {
    	//bt.navtab.refreshParent();
    	var webObj = bt.navtab.getParentDom();
    	webObj.search();
    	bt.navtab.closeCurrent();
    }
}
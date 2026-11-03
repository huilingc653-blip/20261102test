var $, form;
bt.use(['form','datatable','btdate','money', 'relaypage', 'laypage','dateType'], function(){
    $ = bt.jquery;
    form = bt.form();

    bt.relaypage({
        cont: 'page',
        curr: $("#curr").val(),
        pages: $("#pageCount").val(),
        resultCount: $("#resultCount").val(),
        limit: $("#pageSize").val(), // 每页显示的数量
        limits: [10, 20, 50, 100, 500], // 每页条数选项
        layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
        skip: true,
        jump: function (obj, first) {
            if (!first) {
                var param = "businessType=" + $("#businessType").val() + "&businessId=" + $("#businessId").val();
                location.href = encodeURI(basePath + "/cms/rat/ratbkrationadjust/adjustList?" + param + "&start=" + obj.curr + "&len=" + obj.limit);
            }
        }
    });
    
    //监听查询
    form.on('submit(search)', function(data){
    	search();
    });
    
    //监听分配按钮
    form.on('submit(add)', function(){
        var businessType=$("#businessType").val();
        var businessId=$("#businessId").val();
    	if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('ration.maxMoneyAdjust.add'),basePath + "/cms/rat/ratbkrationadjust/addAdjust?businessId="+businessId+"&businessType="+businessType)
            return;
        }
        layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/rat/ratbkrationadjust/addAdjust?businessId="+businessId+"&businessType="+businessType,
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
    var param = "businessType=" + $("#businessType").val() + "&businessId=" + $("#businessId").val();
    location.href = encodeURI(basePath + "/cms/rat/ratbkrationadjust/adjustList?" + param + "&start=1" + "&len=10");
}

function update(id){
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/cms/rat/ratbkrationadjust/update?id=" + id);
		return;
	}
	parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/cms/rat/ratbkrationadjust/update?id=" + id,
		end:function(){
			//datatable && datatable.draw();
		}
	});
}

function del(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/rat/ratbkrationadjust/delete?id=" + id, function(result){
        	if(bt.navtab){
        		bt.navtab.refreshCurrent();
        	}
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function view(id){
	if(bt.navtab){
		bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/cms/rat/ratbkrationadjust/viewAdjust/" + id);
		return;
	}
    parent.layer.open({
        type: 2,
        area: ['1150px', '830px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratbkrationadjust/viewAdjust/" + id
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
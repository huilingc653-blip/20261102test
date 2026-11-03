var dateType;  
bt.use(['form','btdate','money','laypage','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    dateType = bt.dateType.init();
    
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    
    bt.laypage({
		cont: 'page',
		pages: $("#total").val(),
		curr:$("#curr").val(),
		skip:true,
		jump:function(obj, first){
			var curr = obj.curr;
			var ratDate = dateType.get("ratDate").val();
			var param = "";
			if(!first){
				param = "contractNo="+$("#contractNo").val()+"&ratDate="+ratDate+"&start="+curr;
				location.href = basePath + "/cms/rat/ratcpration/list?"+param;
			}
		}
    });
    
    //监听查询
    form.on('submit(search)', function(data){
    	search();
    });
    
    $("#search").click(function(){
    	//search();
    });
    
    //监听重置
	resetCode = function(){
	    var inputs = $('.rCode').find('input');
	    inputs.each(function(){
	        $(this).val("");
	    })
	}
});

function search(){
	var param = "";
	var ratDate = dateType.get("ratDate").val();
	param = "start="+$("#curr").val() +"&contractNo="+$("#contractNo").val()+"&ratDate="+ratDate;
	location.href = basePath + "/cms/rat/ratcpration/list?"+param;
}

function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkration.add'),basePath + "/cms/rat/ratcpration/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcpration/add",
        end:function(){
        }
    });
}
function view(id){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkration.view'),basePath + "/cms/rat/ratcpration/view/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1120px', '800px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcpration/view/" + id
    });
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
function update(id){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkration.update'),basePath + "/cms/rat/ratcpration/update?id=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcpration/update?id=" + id,
        end:function(){
        }
    });
}
function deleteColumn(id){
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/rat/ratcpration/delete/" + id, function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
            search();
        },'JSON');
    });
}
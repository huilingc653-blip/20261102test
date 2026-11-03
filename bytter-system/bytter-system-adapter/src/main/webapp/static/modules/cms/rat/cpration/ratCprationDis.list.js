var dateType; 
bt.use(['form','datatable','btdate','money','laypage','dateType'], function(){
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
				location.href = basePath + "/cms/rat/ratcprationdis/list?"+param;
			}
		}
    });
    
    //监听查询
    form.on('submit(search)', function(data){
    	search();
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
	location.href = basePath + "/cms/rat/ratcprationdis/list?"+param;
}

function add(id){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationdis.add'),basePath + "/cms/rat/ratcprationdis/add?ratCode=" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcprationdis/add?ratCode="+id,
        end:function(){
            //datatable && datatable.draw();
        }
    });
}
function viewDis(id){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('ration.bkrationdis.viewDis'),basePath + "/cms/rat/ratcprationdis/disList?ratCode=" + id);
        return;
    }
    layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/ratcprationdis/disList?ratCode="+id,
        end:function(){
            //datatable && datatable.draw();
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

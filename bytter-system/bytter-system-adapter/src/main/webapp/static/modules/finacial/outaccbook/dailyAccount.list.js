bt.use(['form','dicts','corp','dateType','jquery','selectType','laypage'], function(){
    $ = bt.jquery,form = bt.form();
   var selectType = bt.selectType.init();
    
    //字典初始化
   var dicts  = bt.dicts.init();
    
    //网点初始化
   var corp =  bt.corp.init();
    
   var dateType =  bt.dateType.init();
   
    bt.laypage({
		cont: 'page',
		pages: $("#size").val(),
		curr:$("#pageNum").val(),
		skip:true,
		jump:function(obj, first){
			var curr = obj.curr;
			pageNum = obj.curr;
			if(!first){
				/*$.post(basePath + "/finacial/outaccbook/queryoutaccbook/dividedTable?pageNum="+pageNum, function(result){
					//更改laypage控件的tatol  pageNum
					
					//用js删除当前table的除数据
					$("#appTable tr:gt(0)").remove();
					var tabTr = "";
					result.res.each(function (index, obj) {
						tabTr += "<tr></tr>";
					});
		            $("#appTable").append(tabTr);
		        },'JSON');*/
				
				var	param = "netId="+ corp.get("netId").val()+"&startDate="+dateType.get("startDate").val()
				+ "&curId="+selectType.get("curId").val() +"&pageNum="+curr+"&measureUnit="+dicts.get("measureUnit").val()
				+"&isShowChild="+dicts.get("isShowChild").val();
				
				
				location.href = basePath + "/finacial/outaccbook/queryoutaccbook/dailyaccountlist?param";
				
			}
		}
    });


    form.on('submit(search)', function(data){
    	 $("#pageNum").val("1");
    	
    	var action =basePath + "/finacial/outaccbook/queryoutaccbook/dailyaccountlist";
	    $("#searchForm").attr("action",action);
	    $("#searchForm").submit();
    	});
    
   
   

    $("#reset").click(function(){
    	reset();
    });

});

function reset(){
	bt.dicts.init().get("measureUnit").val("");
	bt.dateType.init().get("startDate").val("");
	bt.corp.init().get("netId").val("");
	bt.selectType.get("curId").val("");
}






bt.use(['form','datatable','dicts','corp','money','selectType','laypage','printType','dateType','selectTree'], function(){
    $ = bt.jquery,form = bt.form();
    bt.printType.init();
    var dateType = bt.dateType.init();
    var selectType = bt.selectType.init();
    //字典初始化
    var dicts = bt.dicts.init();
    
    //网点初始化
    var corp = bt.corp.init();
    //重置
    $("#reset").click(function(){
    	reset();
    });
    
    var subjectSortValue = $("#subjectSortValue").val();
    treeObj = bt.selectTree.initGetCurrent({
        url:basePath+"/finacial/paramconfig/fcsubject/queryAllSubject",
        autoParam: ["id", "subjectName"],
        selected : subjectSortValue,
        enable: true,
        idKey : "id",
        pIdKey : "fatherId",
		rootPId: "",
        elem:"#subjectSort",
        name:'subjectSort',
        checkType:'radio'
    });
    
    
    bt.laypage({
		cont: 'page',
		pages: $("#size").val(),
		curr:$("#pageNum").val(),
		skip:true, 
		jump:function(obj, first){
			var curr = obj.curr;
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
				var	param = "netId="+ corp.get("netId").val()+"&startDate="+dateType.get("startDate").val()+"&endDate="+dateType.get("endDate").val()
				+ "&curId="+selectType.get("curId").val() +"&pageNum="+curr +"&subjectSort="+dicts.get("subjectSort").val();
				
				location.href = basePath + "/finacial/outaccbook/queryoutaccbook/ledgerlist?"+param;
				
			}
		}
    });


    form.on('submit(search)', function(data){
    	 $("#pageNum").val("1");
    	
    	var action =basePath + "/finacial/outaccbook/queryoutaccbook/ledgerlist";
	    $("#searchForm").attr("action",action);
	    $("#searchForm").submit();
    	});
    
  
    
   
});


function reset(){
	bt.dicts.init().get("subjectSort").val("");
	bt.dateType.init().get("startDate").val("");
	bt.dateType.init().get("endDate").val("");
	bt.corp.init().get("netId").val("");
	bt.selectType.init().get("curId").val("");
}



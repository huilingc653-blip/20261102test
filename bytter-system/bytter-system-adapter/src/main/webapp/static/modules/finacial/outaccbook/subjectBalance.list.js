var treeObj;

bt.use(['form','datatable','dicts','corp','btdate','money','selectType','dateType','laypage','selectTree'], function(){
    $ = bt.jquery,form = bt.form();
    //时间
   var dateType = bt.dateType.init();
   var selectType = bt.selectType.init();
    //字典初始化
   var dicts =  bt.dicts.init();
    
    //网点初始化
    bt.corp.init();
    
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
				var	param = "netId="+ $("#netId").val()+"&startDate="+dateType.get("startDate").val()
						+"&subjectSort="+dicts.get("subjectSort").val() + "&curId="+selectType.get("curId").val()
						+"&pageNum="+curr;
				location.href = basePath + "/finacial/outaccbook/queryoutaccbook/subjectbalancelist?"+param;
			}
		}
    });


    form.on('submit(search)', function(data){
    	$("#pageNum").val("1");
    	var action =basePath + "/finacial/outaccbook/queryoutaccbook/subjectbalancelist";
	    $("#searchForm").attr("action",action);
	    $("#searchForm").submit();
    	});
   
    
   
   
});

function reset(){
	bt.dicts.init().get("subjectSort").val("");
	bt.dateType.init().get("startDate").val("");
	bt.corp.init().get("netId").val("");
	bt.selectType.init().get("curId").val("");
	treeObj.val('');
}






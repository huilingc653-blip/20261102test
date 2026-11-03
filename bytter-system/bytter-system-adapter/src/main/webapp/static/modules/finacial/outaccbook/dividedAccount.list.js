
 var treeObj;
bt.use(['form','dicts','corp','btdate','money','bank','laypage','dateType','printType','selectTree'], function(){
    $ = bt.jquery,form = bt.form();
    var pageNum;
    
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
   
    
    //字典初始化
    var dicts = bt.dicts.init();
    var dateType = bt.dateType.init();
    //网点初始化
    var corp = bt.corp.init();
    //账号
    bt.bank.init({
        elem:'#account',
    });
    bt.printType.init();
    $("#reset").click(function(){
    	reset();
    });
     
    bt.laypage({
		cont: 'page',
		pages: $("#accSize").val(),
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
				var param = "netId="+ corp.get("netId").val()+"&corpId="+ corp.get("corpId").val()+"&ge_tallyDate="+dateType.get("ge_tallyDate").val()+"&le_tallyDate="+dateType.get("le_tallyDate").val()
						+ "&pageNum="+curr +"&subjectSort="+dicts.get("subjectSortValue").val()+"&account="+$("#account").val();
				location.href = basePath + "/finacial/outaccbook/queryoutaccbook/dividedAccountList?"+param;
				
			}
		}
    });


    form.on('submit(search)', function(data){
    	$("#pageNum").val("1");
    	var action =basePath + "/finacial/outaccbook/queryoutaccbook/dividedAccountList?pageNum="+pageNum;
	    $("#searchForm").attr("action",action);
	    $("#searchForm").submit();
    });

});

function reset(){
	bt.dicts.init().get("subjectSort").val("");
	bt.dateType.init().get("ge_tallyDate").val("");
	bt.dateType.init().get("le_tallyDate").val("");
	bt.corp.init().get("netId").val("");
	bt.corp.init().get("corpId").val("");
	$("#account").val("");
	treeObj.val('');

}
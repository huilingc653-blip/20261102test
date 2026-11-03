bt.use(['form','corp','dicts','selectType','selectTable','selectTableType','printType'], function(){
	var $ = bt.jquery,form = bt.form();
	
	selectNetTable = function(data,elem){
		var corpCode = corp.get("corpId").val().trim();
		var netCode = corp.get("netId").val();
		selectTable.get("selectTable").setUrl({netId:netCode,corpId:corpCode});
	}
	bt.corp.init();
	bt.selectType.init();
	bt.beforeInitMulselect = function(data){
		var filterValue = ['K','I','Q','C','H','T','W','B','G','E','F','U','R','Z'];
		var data = data;
		for(var i=0,length= filterValue.length;i < length; i++){
            for(var j=0,len= data.length;j < len; j++){
				if(data[j].value == filterValue[i]){
					data.splice(j,1);
					break;
				}
            }
		}
		return data
	}
	
	bt.dicts.init();
	bt.printType.init();
	var selectTable = bt.selectTableType.init({
		 elem:"#blnctrAcc",
		 url:basePath+"/finacial/report/BalanceQuery/queryListAccount",
		 columns:[
		          {"data":"blnctrAcc", title: bt.lang("table.fcLessPayBill.account")},
		          {"data":"accName", title: bt.lang("table.accountDTO.accName")},
		 ],
		 clickCallback:function(data,$tr,$elem,full){
			 if(data){
	                $('#blnctrAcc').val(data.blnctrAcc);
	         }
		 },  
 	});
	
	 form.on('submit(search)', function(data){
    	// $("#pageNum").val("1");
    	
    	var action =basePath + "/finacial/report/BalanceQuery/getList";
	    $("#searchForm").attr("action",action);
	    $("#searchForm").submit();
    	});
	 
	 $("#reset").click(function(){
		 reset ();
	 })
	 
	 form.on('submit(excel)', function(data) {
		   var url = basePath + "/finacial/report/BalanceQuery/excel"; 
		   DownLoadFile({url:url,data:data.field,method:'get'}); 
		   return false;
	});
	 
	 
	 var DownLoadFile = function (options) {
		    var config = $.extend(true, { method: 'get' }, options);
		    var $iframe = $('<iframe id="down-file-iframe" />');
		    var $form = $('<form target="_self" method="' + config.method + '" />');
		    $form.attr("action", config.url);
		    for (var key in config.data) {
		                $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
		            }
		    $iframe.append($form);
		    $(document.body).append($iframe);
		    $form[0].submit();
		    $iframe.remove();
		}
})



 function reset (){
	bt.corp.init().get("netId").val("");
	bt.corp.init().get("corpId").val("");
	$("#blnctrAcc").val("");
	bt.dicts.init().get("accState").val("");
	bt.dicts.init().get("accType").val("");
	bt.selectType.init().get("subjectId").val("");
	bt.dicts.init().get("sumType").val("");
	bt.selectType.init().get("curId").val("");
}
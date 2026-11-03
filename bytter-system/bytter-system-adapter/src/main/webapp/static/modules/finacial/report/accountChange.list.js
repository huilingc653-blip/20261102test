bt.use(['form','corp','dicts','selectType','selectTable','selectTableType','dateType','printType',
		'datatablecore'], function(){
	var $ = bt.jquery,form = bt.form();
	
	selectNetTable = function(data,elem){
		var corpCode = corp.get("corpId").val();
		var netCode = corp.get("netId").val();
		selectTable1.get("selectTable1").setUrl({netId:netCode,corpId:corpCode});
		//selectTable2.get("selectTable2").setUrl({netId:netCode,corpId:corpCode});
	}
	var corp = bt.corp.init();
	bt.selectType.init();
	bt.dicts.init();
	bt.dateType.init();
	bt.printType.init();
	
	var selectTable1 = bt.selectTableType.init({
		 elem:"#account1",
		 url:basePath+"/finacial/report/accountChange/queryListAccount",
		 columns:[
		          {"data":"blnctrAcc", title: bt.lang("table.fcLessPayBill.account")},
		          {"data":"accName", title: bt.lang("table.accountDTO.accName")},
		 ],
		 clickCallback:function(data,$tr,$elem,full){
			 if(data){
	                $('#account1').val(data.blnctrAcc);
	         }
		 },  
 	});
	
	var selectTable2 = bt.selectTableType.init({
		 elem:"#account2",
		 url:basePath+"/finacial/report/accountChange/queryListAccount",
		 columns:[
		          {"data":"blnctrAcc", title: bt.lang("table.fcLessPayBill.account")},
		          {"data":"accName", title: bt.lang("table.accountDTO.accName")},
		 ],
		 clickCallback:function(data,$tr,$elem,full){
			 if(data){
	                $('#account2').val(data.blnctrAcc);
	         }
		 },  
	});
	
	 form.on('submit(search)', function(data){
  	 	var action =basePath + "/finacial/report/accountChange/queryList";
	    $("#searchForm").attr("action",action);
	    $("#searchForm").submit();
   	});
	 
	 $("#reset").click(function(){
		 reset ();
	 })
	 
	 form.on('submit(excel)', function(data) {
		   var url = basePath + "/finacial/report/accountChange/excel"; 
		   DownLoadFile({url:url,data:data.field,method:'post'}); 
		   return false;
	});
	 function reset (){
			bt.corp.init().get("netId").val("");
			bt.corp.init().get("corpId").val("");
			$("#account1").val("");
			$("#account2").val("");
			bt.selectType.init().get("subjectId").val("");
			$("#t1").val("");
			$("#t2").val("");
		//bt.selectType.init().get("curId").val("");
		}
			
		var DownLoadFile = function (options) {
		    var config = $.extend(true, { method: 'post' }, options);
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

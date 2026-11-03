bt.use(['form','element','dicts','datatable','page','jquery'], function(){
	 $ = bt.jquery,form = bt.form();
	 bt.page.init({
              elem:'#page',
              count:10,
              currentPage:empstr($("#curr").val()),
              pages:empstr($("#total").val()),
              record:false,
              lengthMenu:false,
              jump:function(obj){
            	  var curr = obj.curr;
  				  var param = "";
            	  if($("#pageType").val()=="interest"){//计息利息清单
						param = "netId="+$("#netId").val()+"&accType="+$("#accType").val()+"&opType="+$("#opType").val()+"&curr="+curr+"&poolSign=" + $("#poolSign").val();
						location.href = basePath + "/finacial/interest/fcinterest/interestBill?"+param;
					}
					if($("#pageType").val()=="rush"){//冲预提利息清单
						params = "netId="+$("#netId").val()+"&accType="+$("#accType").val()+"&opType="+$("#opType").val() +"&curr="+curr+"&endInstDate="+$("#endInstDate").val()+"&tallyDate="+$("#tallyDate").val();
						location.href = basePath + "/finacial/interest/fcinterest/interestRushBill?"+params
					}
              }
            });
            
     function empstr(str){
		 if(str==""||str=="0"||str==null||typeof(str)=="undefined"||isNaN(str)){
			 return 1;
		 }else{
			 return str;
		 }
	 }
 
});


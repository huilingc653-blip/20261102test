bt.use(['form','corp','selectTable','select','dicts','bttable','selectType','bank','moneyType'], function(){
    var $ = bt.jquery,form = bt.form();
	moneyType = bt.moneyType.init();
	moneyFun = bt.moneyType.getFun();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({ 
    	elem:'.bank'
    })

    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/allocation/excrulesman/save";
        $.post(basePath + action,data.field,function(result){
        	 parent.layer.alert(bt.lang(result.msg , result.params), {icon: result.success?1:2}, function(){
                 parent.layer.closeAll();
                 if(result.success){
                     if(bt.navtab) {
                         bt.navtab.refreshParentTable();
                         bt.navtab.refreshCurrent()
                     }else {
                         parent.layer ? parent.layer.closeAll() : layer.closeAll();
                     }
                 }
             })
         },'JSON');
        return false;
    });
 
    
    /**生成付款账号*/
    var payeeAccInit = bt.selectTable.init({
      	 elem: "#payeeAccTable",
      	 url: basePath+"/allocation/excrulesman/getBankAcc",
      	 urldata: {corpId: $("input[name='curpId']").val()},
      	 scrollX: true,
      	 scrollY:"200px",
      	 searchInput:"#payeeAccNo",
         paging:true,
      	 columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
      	 ],
      	 clickCallback:function(data){
      		 if(data){
      			 $('#payeeAccNo').val(data.bankAcc);
      			 $('#payeeAccId').val(data.id);
      			 $('#curpId').val(data.curId);
     			 $("#balanceDiv").text(moneyFun.format(data.balance));
      		 }else{
      			 $('input.text').val('');
      		 }
      	 }
    });
    
   
   bt.corpCallback = function(id){
    	//重新加载付款账号
    	payeeAccInit.get("payeeAccNo").reload({corpId: id});
    }
    bt.corp.init(); 
  
    /**增加行*/
    var rowMR=1;
    $("#addBookTr").on("click",function(){
    	var html='<tr row="'+rowMR+'">';
		html+='<td><input type="text" name="payerAccNos[' + rowMR + '].payerAccNo" class="layui-input bank" maxlength="32" lay-verify="required|number" /></td>';
		html+='<td><button data-type="del" class="layui-btn layui-btn-mini" >'+bt.lang("common.button.delete")+'</button></td>';
		html+='</tr>';
		$("#printMRTable>tbody").append(html);
		rowMR++;	
    });
    
     
    /**生成行列表*/
    var initPrintMRTable=function(){
	    //生成列表
	    bt.bttable.init({
	    	elem: "#printMRTable",
	    	scrollX : true,
		     columns:[
		               {
		            	   data:"payerAccNo",
		            	   render:function(data,rowMR){
		            		   var reInfo='<input type="text" name="payerAccNos[' + rowMR + '].payerAccNo" class="layui-input bank" maxlength="32" lay-verify="required|number"/>';
		            		   return reInfo;
		            	   }
		               },
		               {data:null,render:function(data,row){
		            	   var reInfo='';
            			   reInfo='<button data-type="del" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>';
		                   return reInfo;            
		               }}
		       ],   
		       delRowCallback:function(data,$tr){
		           $tr.remove();
		       },            
		       tableInitCallback:function(){  
		    	   bt.dicts.init();
	          }
	    });
    }
 
    initPrintMRTable();
});


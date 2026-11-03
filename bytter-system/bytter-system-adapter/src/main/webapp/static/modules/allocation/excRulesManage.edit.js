bt.use(['form','corp','selectTable','select','dicts','bttable','selectType','bank'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({ 
    	elem:'.bank'
    })
    
    
    //监听提交
   form.on('submit(submit)', function(data){
        var action = "/allocation/excrulesman/modify";
        $.post(basePath + action,data.field,function(result){
        	 parent.layer.alert(result.msg, {icon: result.success?1:2},function(){
                 parent.layer.closeAll();
                 if(result.success){
                     if(bt.navtab) {
                         bt.navtab.closeCurrent();
                     }else {
                         parent.layer ? parent.layer.closeAll() : layer.closeAll();
                     }
                 }
             })
         },'JSON');
        return false;
    });
    
    var corpId=$("#curpId").val();
    //获取付款账号
    var payeeAccInit = bt.selectTable.init({
     	 elem: "#payeeAccTable",
     	 url: basePath+"/allocation/excrulesman/getBankAcc",
     	 urldata: {corpId:corpId},
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
    	payeeAccInit.get("payeeAccNo").reload({corpId: id});
    }
    bt.corp.init();  

});

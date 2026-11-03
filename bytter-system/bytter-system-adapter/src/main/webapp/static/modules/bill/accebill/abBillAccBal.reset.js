var fromObj,corp,date;
bt.use(['form','corp','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    fromObj=form;
    date = bt.dateType.init();
    corp=bt.corp.init();
});

var resetAccBillBal=function(){
	if(!fromObj.verifyFun('#balFrom')){
        return false;
    }
	var action = "/bill/accebill/abbillaccbal/resetAbBillAccBal";
	var corpIds=corp.get('corpId').val();
	var beginDate=date.get("beginDate").val();
	bt.jquery.ajax({
    	type: "POST",
    	url: basePath + action,
       	data: "corpIds="+corpIds+"&beginDate="+beginDate,//将对象序列化成JSON字符串
       	async: false,
        dataType: "json",
    	success: function(result){
    		parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
    	},
    	error: function(res){
    		
    	}
    });
}
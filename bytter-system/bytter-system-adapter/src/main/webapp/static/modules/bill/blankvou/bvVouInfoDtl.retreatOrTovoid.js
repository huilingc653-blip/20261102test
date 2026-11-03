bt.use(['form'], function(){
    var $ = bt.jquery,form = bt.form();
    //提交退票
    form.on('submit(retreat)', function(data){
        bt.loading.show();
    	retreatOrTovoid("retreat",data);
    });
    //提交作废
    form.on('submit(tovoid)', function(data){
        bt.loading.show();
    	retreatOrTovoid("tovoid",data);
    });
    //提交批量退票
    form.on('submit(batchRetreat)', function(data){
        bt.loading.show();
    	retreatOrTovoid("batchRetreat",data);
    });
    //提交批量作废
    form.on('submit(batchTovoid)', function(data){
        bt.loading.show();
    	retreatOrTovoid("batchTovoid",data);
    });
    //返回
    $("[name='revert']").click(function(){
    	parent.layer.closeAll();
    });
});

function retreatOrTovoid(operate,data){
	var $ = bt.jquery,action="/bill/blankvou/bvvouinfodtl/"+operate+"/";
	if(operate=="retreat"||operate=="tovoid"){
		action = action+data.field.id;
	}else{
		action = action+data.field.ids;
	}
    $.post(basePath + action ,{"reason":data.field.reason},function(result){
        bt.loading.hide();
        parent.layer.alert(bt.lang("common.message.operate.fail"), {icon: result.success?1:2});
        if(result.success){
            parent.layer.closeAll();
        }
        return false;
    },'JSON');
}
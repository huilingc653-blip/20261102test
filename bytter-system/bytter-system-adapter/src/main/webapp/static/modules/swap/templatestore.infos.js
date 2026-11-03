var form,selectType;
bt.use(['form','element','selectType','dateType','selectCallback'], function(){
    form = bt.form(),$  = bt.jquery,element = bt.element();
    selectType = bt.selectType.init();
    form.on('submit(submit)', function(data){
        var serialIds = data.field.serialIds;
        var ids = serialIds.split(",");
        var count = 0;
        for(var i=0;i<ids.length;i++){
            if(ids[i]){
                data.field['serialId['+count+']'] = ids[i];
                count++;
            }
        }
       
        if($("#enteringIsEdit").val()==='1'){
            var flag = initTemplateStoreDate(data);
            if(!flag) return false;
        }
        var action = "/erp/swap/hisdtltemplate/modifys";
        $.post(basePath + action,data.field,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	if(bt.navtab) {
	                bt.navtab.refreshParent();
	                bt.navtab.closeCurrent();
	            }else {
	                parent.layer?parent.layer.closeAll():layer.closeAll();
	            }
            }
        },'JSON');
        return false;
    });
});

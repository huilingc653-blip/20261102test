
var rasShops={};
var dicts;
bt.use(['form','selectTable','dicts','selectType'], function(){
    var $ = bt.jquery,form = bt.form();     
    dicts = bt.dicts.init();
    //新增
    $("#btn_add").click(function(){
    	if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/ras/importConf/add");
            return;
        }
    });
}); 
function edit(id,fileType,isFormat,usePower){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/ras/importConf/add?id="+id+"&fileType="+fileType+"&isFormat="+isFormat+"&usePower="+usePower);
        return;
    }
}

function del(id){
	parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){ 
        $.post(basePath + "/ras/importConf/del/"+id, function(result){
           bt.navtab.refreshCurrent();
        },'JSON');
   });
	
}


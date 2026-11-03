bt.use(['form','layedit','btdate','moneynum','dateType'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.moneynum.init({elem:'#vouMoney',decimal:2});
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            required:true,
            istime: false,
            min: $("#inputDate").val()

        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    //监听提交使用
    form.on('submit(submit)', function(data){
    	var action = "/bill/blankvou/bvvouemployinfo/save";
        $.post(basePath + action,data.field,function(result){
            if(result.success){
                parent.layer?parent.layer.closeAll():layer.closeAll();
                parent.layer.alert(result.msg, {icon: result.success?1:2});
            }else{
            	parent.layer.alert(bt.lang("bill.blankvou.save.fail"), {icon: result.success?1:2});
            }
        },'JSON');
        return false;
    });
    //返回
    $("[name='revert']").click(function(){
    	parent.layer.closeAll();
    });
});

bt.use(['form','layedit','dateType','rateType'], function(){
    var $ = bt.jquery,form = bt.form();
    var dt=bt.dateType.init();
    var rt=bt.rateType.init();


    form.on('submit(submit)', function () {
        var obj=new Object();
        obj.id=$("#id").val();
        obj.status=11;
        obj.newInstrate=rt.get("newInstrate").val();
        obj.adjustTime=dt.get("adjustTime").val();
        if(obj.newInstrate && obj.adjustTime ){
            modify(obj);
        }
    });

    $("#close").on("click",function() {
        if (bt.navtab) {
            bt.navtab.refreshParent();
            bt.navtab.closeCurrent();
        }
    });

    function modify(obj) {
        var poolSign = $("#poolSign").val();

        var url = basePath+"/basic/business/fcinstrateadjust/modify/"+poolSign;
        $.post(url,obj,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab) {
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                }else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
        },'JSON');

    }
    function validFrom() {
        var ask = bt.lang("innerAccount.freeze.confirm");
        return ask;
    }

    $(document).ready(function(){
        if($('#isView').val() == 'Y'){ //查看还是复核界面
            $('#optButton').hide();
        }else{
            $('#optButton').show();
        }
    });
});

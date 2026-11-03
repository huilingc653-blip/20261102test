bt.use(['form','layedit','dateType','rateType'], function(){
    var $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    bt.rateType.init();


    $("#back").on("click",function() {
        var obj=new Object();
        obj.id=$("#id").val();
        obj.status=-1;
        apply(obj);

        layer.confirm(validFrom(), {
            btn: [bt.lang("common.button.submit"),bt.lang("common.button.close")] //按钮
        }, function(i){
            apply();
        });

    });
    
    $("#pass").on("click",function() {
        var obj=new Object();
        obj.id=$("#id").val();
        obj.status=95;
        apply(obj);
    });

    $("#close").on("click",function() {
        if (bt.navtab) {
            bt.navtab.closeCurrent();
        }
    });

    function apply(obj) {
        var url = basePath+"/basic/business/fcinstrateadjust/apply";
           $.post(url,obj,function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(bt.navtab) {
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

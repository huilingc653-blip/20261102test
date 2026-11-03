var form;
bt.use(['form','select','jquery','btdate','dateType','dicts'], function(){
        var $ = bt.jquery,
        form = bt.form();
        bt.empty = function(ms){
            if(typeof(ms) == "undefined" || ms == null || ms == ""){
                return true;
            }
            return false;
        }
        bt.dicts.init();
        //提交申请信息
        form.on('submit(retreat)', function(data){
            bt.loading.show();
            submitForm(data);
        });
	    //返回
        $("[name='revert']").click(function(){
            parent.layer.closeAll();
        });
    });

    function submitForm(data){
        $.post(basePath + '/billPool/intoRepeal/saveRepealDtl', data.field, function (result) {
            bt.loading.hide();
            if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                parent.layer.closeAll();
                 location.reload();//重刷页面
            }else{
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                location.reload();//重刷页面
            }
            return false;
        },'JSON');
    }
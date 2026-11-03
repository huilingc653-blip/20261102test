
bt.use(['form', 'jquery'], function () {
    var form = bt.form(), $ = bt.jquery;
    form.on('submit(menuSubmit)', function (data) {
        var action = $('input[name="id"]').val()?"update":"save";
        $.post(basePath + '/sys/region/' + action, data.field, function (result) {
            if(result.success){
                top.layer.closeAll();
            }
            top.layer.alert(result.msg, {icon: result.success ? 1 : 2});

        }, 'JSON');
        return false;
    });

    //自定义验证规则
    form.verify({
        provCode:function(value){
            var pattern = /^[0-9]\d{0,4}$/;// /^-?\d{0,5}$/;
            if($.trim(value) && !pattern.test($.trim(value))){
                return bt.lang('sys.region.code.show');
            }
        },
        cityCode:function(value){
            var pattern = /^[0-9]\d{0,4}$/;
            if($.trim(value) && !pattern.test($.trim(value))){
                return bt.lang('sys.region.code.show');
            }
        }
    });
	
 });   

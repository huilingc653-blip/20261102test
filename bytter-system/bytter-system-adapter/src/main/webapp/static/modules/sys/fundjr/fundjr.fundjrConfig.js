
var form ={};
var dicts={};
bt.use(['form','jquery','dicts'], function(){
    form = bt.form(),$  = bt.jquery;
    //监听提交
    form.on('submit(configSubmit)', function(data){
        $.post(basePath + "/sys/config/save",data.field,function(result){
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },"JSON");
        return false;
    });
    dicts=bt.dicts.init();
});


bt.use(['form','layedit','moneyType','dateType','corp','selectType','dicts','rateType'], function(){
    form = bt.form();
    var $ = bt.jquery

    //初始化日期
    bt.dateType.init();
    //初始化字典
    dict = bt.dicts.init();
    //初始化单位
    bt.corp.init();
    //初始化下拉框
    bt.selectType.init();

    //格式化数字
    bt.moneyType.init();

    // 格式化比率
    bt.rateType.init();


    //监听提交
    form.on('submit(submit)', function (data){
        const action = "/wage/wageReleaseConfig/update";
        let mask = layer.load(1, {shade: [0.1, '#FFF']});//遮罩层
        $.post(basePath + action, data.field, function(result){
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
            layer.close(mask);
            if(result.success){
                if(!bt.navtab){
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    bt.navtab.refreshParent();
                    bt.navtab.closeCurrent();
                }
            }else{
                bt.loading.hide();
            }
        },'JSON');
        return false;
    });

});
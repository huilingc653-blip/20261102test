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
        const action = "/wage/wageReleaseConfig/batchUpdateAmount";
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

function typeCallback(data) {
    if (data.value === '0') {
        $(".type-label").text('批量设置金额');
        $(".type-input").html('<input type="text" data-name="amount" lay-verify="required" autocomplete="off" class="layui-input bt-biz-moneyType" data-minus="true" placeholder="增加/减少的金额">');
        //格式化数字
        bt.moneyType.init();
    } else {
        $(".type-label").text('批量设置比率');
        $(".type-input").html('<input type="text" data-name="ratio" lay-verify="required" autocomplete="off" class="layui-input bt-biz-rateType" data-supermin="-100" placeholder="增加/减少的比率">');
        // 格式化比率
        bt.rateType.init();
    }
}

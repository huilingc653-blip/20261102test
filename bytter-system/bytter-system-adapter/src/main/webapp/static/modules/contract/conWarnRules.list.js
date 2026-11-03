var selectType,$,form,dicts,moneyType;
bt.use(['form','dicts','selectType','moneyType'], function(){
    $ = bt.jquery,form = bt.form();

    dicts=bt.dicts.init();
    selectType=bt.selectType.init();
    moneyType=bt.moneyType.init();

    // 此循环用于将预警提前日期和间隔日期位置互换（moneyType初始化时小的输入框必须在前面）
    // 用于大的数字在前面时，moneyTyp初始化后进行位置互换
    for(var i=1;i<$('tbody tr').length+1;i++){
        $('#endDays'+i).before($("#startDays"+i));
    }

    //监听保存
    form.on('submit(save)', function(data){
        var arrRules = $('#inputForm').serializeArray();
        var str='';
        if($('tbody tr').length < 1){
            layer.alert(bt.lang('contract.conWarnRules.selectData'), {icon: 7});
            return false;
        }
        for(var i=0;i<$('tbody tr').length;i++){
            arrRules[i*4+1].name = 'beforDays';
            arrRules[i*4+2].name = 'intervalDays';
            arrRules[i*4+3].name = 'validSigns';
            if(Number(arrRules[i*4+1].value) < Number(arrRules[i*4+2].value)){
                layer.alert(bt.lang('contract.conWarnRules.intervalDayBig'), {icon: 7});
                return false;
            }
            if(str.indexOf(arrRules[i*4].value) > -1){
                layer.alert(bt.lang('contract.conWarnRules.onlyRuleWarn'), {icon: 7});
                return false;
            }
            str += arrRules[i*4].value+',';
        }
        bt.loading.show();
        var action="/contract/conWarnRules/save";
        $.post(basePath + action, $.param(arrRules), function (result) {
            // parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    location.reload();//重载当前页面
                } else {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            }
            bt.loading.hide();
        }, 'JSON');
        return false;
    });

});

checkData = function checkData(data){
    var size=0;
    var arrConType = $("[name='conTypeIds']").serializeArray();
    for(var i=0; i<arrConType.length; i++){
        if(data.id === arrConType[i].value){
            size += 1;
        }
        if (size >= 2) {
            layer.alert(bt.lang('contract.conWarnRules.onlyRuleWarn'), {icon: 7});
            return false;
        }
    }
};

// 检查天数是否符合要求
checkDay = function checkDay(data){
    if(Number(data.value) > 10000){
        layer.alert(bt.lang('contract.conWarnRules.dayMax'), {icon: 7});
        data.value='';
        return false;
    }
};


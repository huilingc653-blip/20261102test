bt.use(['form', 'layedit','dicts', 'selectTree','rateType','selectType','moneyType','dateType','selectTableType'], function () {
    $ = bt.jquery, form = bt.form();
    dicts = bt.dicts.init();
    select = bt.selectType.init();
    moneyType = bt.moneyType.init();
    moneyFun=bt.moneyType.getFun();
    rateTypes = bt.rateType.init();//利率
    dateType = bt.dateType.init();
    dateFun = dateType.getFun();//获取时间操作方法
    //监听保存
    form.on('submit', function(data){
        bt.loading.show();

        var nm = $(this).attr("id");
        if(nm == "submit"){
            data.field.saveType = "1";
        }else {
            data.field.saveType = "0";
        }
        var action="/cms/equity/repo/save";
        $.post(basePath + action, data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    location.reload();
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshCurrent();

                }
            }else {
                bt.loading.hide();
            }

        }, 'JSON');
        return false;
    });

})

/**加载利息预计明细*/
function equityPlans(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'),basePath + "/cms/equity/repo/equityPlans?equityCode="+$("#equityCode").val()+"&repoPlanDetailId="+$("#repoPlanDetailId").val());
    }
}
/**判断字符串是否为空*/
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}


/**选择计划后执行的方法*/
function setSelectInterestData(repoDate,repoMoney,id){
    dateType.get("repoDate").val(repoDate);
    $("#repoPlanDetailId").val(id);
    moneyType.get("thisRepoMoney").val(repoMoney);

    // 回购日期和本地回购金额设置成只读
    dateType.get("repoDate").attr("readonly",true);
    moneyType.get("thisRepoMoney").attr("readonly",true);
}

bt.use(['form','corp','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();

    var dict;
    dict = bt.dicts.init();

    //监听提交
    form.on('submit(submit)', function (data) {
        var id = data.field.id;
        var status = data.field.status;
        var poolSign = data.field.poolSign;
        var poolId = data.field.poolId;
        var corpId = data.field.corpId;

        var poolChildFlag;
        var poolNoUseFlag;
        var accBalanceFlag;
        var existFlag;
        var useAccFlag;
        var relevanceFlag;
        $.ajaxSettings.async = false;

        //子单位调接口判断
        if(poolSign == "0"){
            $.get(basePath + "/pool/fcCorpPool/judgePool/" + poolId +"/" + poolSign, function (resultMap) {
                poolNoUseFlag = resultMap.poolNoUse;
            }, 'JSON');
            if(status == "0"){
                $.get(basePath + "/pool/fcCorpPool/vaildAcc/" +poolId +"/" + corpId, function (resultMap) {
                    accBalanceFlag = resultMap.accBalance;
                    existFlag = resultMap.existFlag;
                    useAccFlag = resultMap.useAccFlag;
                    relevanceFlag = resultMap.relevanceFlag;
                }, 'JSON');

            }
            $.ajaxSettings.async = true;
            //主体单位状态为禁用，子单位不允许启用
            if (poolNoUseFlag) {
                parent.layer.alert(bt.lang('nis.pool.fcCorpPool.poolUse.no'), {icon: 2});
                return false;
            }
            //该单位账户开户尚未完成，不允许禁用
            if (useAccFlag) {
                parent.layer.alert(bt.lang("nis.pool.fcCorpPool.fcAcc.use"), {icon: 2});
                return false;
            }
            //该单位已维护过账户关联关系，不允许禁用
            if (relevanceFlag) {
                parent.layer.alert(bt.lang("nis.pool.fcCorpPool.btBankAccCur.use"), {icon: 2});
                return false;
            }
            //资金池内部账户余额必须为0，且不能存在未结清的利息
            if (accBalanceFlag) {
                parent.layer.alert(bt.lang("nis.pool.fcCorpPool.balInterest.zero"), {icon: 2});
                return false;
            }
            //该单位存在资金调拨在途业务，不允许禁用
            if (existFlag) {
                parent.layer.alert(bt.lang("nis.pool.fcCorpPool.allocation.use"), {icon: 2});
                return false;
            }
        }else{
            if(status == "0"){
                $.get(basePath + "/pool/fcCorpPool/judgePool/" + id +"/" + poolSign, function (resultMap) {
                    poolChildFlag = resultMap.hasChildPool;
                }, 'JSON');
                $.ajaxSettings.async = true;
                if (poolChildFlag) {
                    parent.layer.alert(bt.lang('nis.pool.fcCorpPool.has.useChild'), {icon: 2});
                    return false;
                }
            }
        }
        var action = "/pool/fcCorpPool/modify";
        $.post(basePath + action, data.field, function (result) {
            if(result.success){
                parent.layer.closeAll();
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        }, 'JSON');
        return false;
    });

});


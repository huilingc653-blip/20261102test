var dicts,moneyType;
bt.use(['form','selectType','datatable','dicts','bank','moneyType','mulselect','moneyType'], function(){
    rulesCallback = function(){
        var array = dicts.get('rules').val();
        if (!array) {
            var rulesDef = $("#rulesDef").val();
            if (rulesDef) {
                array = $("#rulesDef").val().split(",");
            }
        }
        var flag = false;
        if (array) {
            for (var i = 0; i < array.length; i++) {
                if (array[i] === 'transTime') {
                    flag = true;
                }
            }
        }
        if(flag){
            $("#transTimeRuleTr").show();
        }else{
            $("#transTimeRuleTr").hide();
        }
    }

    transTimeRuleCallback = function(){
        var array = dicts.get('transTimeRule').val()
        var flag = false;
        if (array) {
            for (var i = 0; i < array.length; i++) {
                if (array[i] === '1') {
                    flag = true;
                }
            }
        }
        moneyType.get("transDays").val("");
        if(flag){
            $("#tranDaysDiv").show();
            moneyType.get('transDays').attr('lay-verify', 'required|number2');
        }else{
            $("#tranDaysDiv").hide();
            moneyType.get('transDays').attr('lay-verify', 'number2');
        }
    }
    dicts = bt.dicts.init();
    bt.selectType.init();
    moneyType = bt.moneyType.init();
    bt.mulselect.init();
    bt.bank.init();
    $ = bt.jquery,form = bt.form();

    //监听提交
    form.on('submit(submit)', function(data){
        var action = "/netbank/strategy/modify";
        var executeSort=$("#executeSort").val();
        var id = $("#id").val();
        var bankTypeId = $("#bankTypeId").val();
        var isType = $("#type").val();
        if (isType == "1") {   //银行回单自动勾兑流水
            data.field.dtlOnlyId = dicts.get("dtlOnlyIdReceiptSel").val();
            data.field.receiptOnlyId = dicts.get("receiptOnlyIdSel").val();
        } else if (isType == "0") { //付款单自动勾兑
            data.field.dtlOnlyId = dicts.get("dtlOnlyIdSel").val();
            data.field.billOnlyId = dicts.get("billOnlyIdSel").val();
        }
        var isExistId = dicts.get("isExistId").val();
        if(isExistId == undefined){
            isExistId = $("#isExistId").val();
        }
        data.field.rules = data.field.rules.toString();
        var transDays = moneyType.get("transDays").val();
        if (!isEmpty(executeSort) && !isEmpty(isType)&& !isEmpty(bankTypeId)) {
            $.ajax({
                url: basePath + "/netbank/strategy/executeUpdateSort",
                method: "POST",
                dataType: 'json',
                contentType: "application/json",
                data: JSON.stringify({'type': isType,'executeSort':executeSort,'rules':data.field.rules,'isExistId':isExistId,'bankTypeId':bankTypeId,'id':id,'transDays':transDays}),
                success: function (t) {
                    if (t == "1") {
                        layer.alert("该策略已存在，不可重复提交！", {icon: 7});
                    } else if(t == "2"){
                        layer.alert("同一银行同一任务类型的优先执行排序不可重复", {icon: 7});
                    } else{
                        $.post(basePath + action,data.field,function(result){
                            parent.layer.alert(result.msg, {icon: result.success?1:2}, function(){
                                parent.layer.closeAll();
                                if(result.success){
                                    if(bt.navtab) {
                                        parent.layer.closeAll();
                                    }else {
                                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                                    }
                                }
                            })
                        },'JSON');
                    }
                },
            });
            return false;
        }
    });
    toRegular();
});

function toRegular() {
    var isRegular = dicts.get("isExistId").val();
    var isType =  $("#type").val();
    if(isRegular == undefined){
        isRegular = $("#isExistId").val();
    }
    if(isRegular == "1"){
        $("#rulesa").hide();
        $("#transTimeRuleTr").hide();
        $("#rules").hide();
        $("#point").show();
        dicts.get("rules").setRequired(false);
        if (isType == "1") {   //银行回单自动勾兑流水
            $("#dtlOnlyId").val(dicts.get("dtlOnlyIdReceiptSel").val());
            $("#receiptOnlyId").val(dicts.get("receiptOnlyIdSel").val());
            bt.dicts.get('dtlOnlyIdReceiptSel').attr({'data-required': true});
            bt.dicts.get('receiptOnlyIdSel').attr({'data-required': true});
            bt.dicts.get('dtlOnlyIdSel').attr({'data-required': false});
            bt.dicts.get('billOnlyIdSel').attr({'data-required': false});
            $("#bisEbillNo").show();
            $("#bisDtlNo").hide();
        } else if (isType == "0") { //付款单自动勾兑
            $("#dtlOnlyId").val(dicts.get("dtlOnlyIdSel").val());
            $("#billOnlyId").val(dicts.get("billOnlyIdSel").val());
            bt.dicts.get('dtlOnlyIdReceiptSel').attr({'data-required': false});
            bt.dicts.get('receiptOnlyIdSel').attr({'data-required': false});
            bt.dicts.get('dtlOnlyIdSel').attr({'data-required': true});
            bt.dicts.get('billOnlyIdSel').attr({'data-required': true});
            $("#bisEbillNo").hide();
            $("#bisDtlNo").show();
        }else{
            $("#dtlOnlyId").val("");
            bt.dicts.get('dtlOnlyIdReceiptSel').attr({'data-required': false});
            bt.dicts.get('receiptOnlyIdSel').attr({'data-required': false});
            bt.dicts.get('dtlOnlyIdSel').attr({'data-required': false});
            bt.dicts.get('billOnlyIdSel').attr({'data-required': false});
            $("#bisEbillNo").hide();
            $("#bisDtlNo").hide();
        }
    }else {
        $("#rulesa").show();
        $("#rules").show();
        $("#point").hide();
        dicts.get("rules").setRequired(true);
        $("#dtlOnlyId").val("");
        bt.dicts.get('dtlOnlyIdReceiptSel').attr({'data-required': false});
        bt.dicts.get('receiptOnlyIdSel').attr({'data-required': false});
        bt.dicts.get('dtlOnlyIdSel').attr({'data-required': false});
        bt.dicts.get('billOnlyIdSel').attr({'data-required': false});
        $("#bisEbillNo").hide();
        $("#bisDtlNo").hide();
        rulesCallback();
    }
    form.renderIcon();
}


/**
 * 为空判断
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    if (value == null || value == "" || value == undefined || value == " ") {
        return true;
    } else {
        return false;
    }
}

var dicts,moneyType;
bt.use(['form', 'selectType', 'datatable', 'dicts', 'bank', 'moneyType', 'mulselect','moneyType'], function () {

    bt.mulselect.init();
    bt.selectType.init();
    var moneyType = bt.moneyType.init();
    bt.bank.init();
    $ = bt.jquery, form = bt.form();

    rulesCallback = function(){
        var array = dicts.get('rules').val()
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
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/netbank/strategy/save";
        var executeSort=$("#executeSort").val();
        var bankTypeId = dicts.get("bankTypeId").val();
        var isType = dicts.get("type").val();
        if (isType == "1") {   //银行回单自动勾兑流水
            data.field.dtlOnlyId = dicts.get("dtlOnlyIdReceiptSel").val();
            data.field.receiptOnlyId = dicts.get("receiptOnlyIdSel").val();
        } else if (isType == "0") { //付款单自动勾兑
            data.field.dtlOnlyId = dicts.get("dtlOnlyIdSel").val();
            data.field.billOnlyId = dicts.get("billOnlyIdSel").val();
        }
        data.field.rules = data.field.rules.toString();
        var isExistId = dicts.get("isExistId").val();
        if(isExistId == undefined){
            isExistId = $("#isExistId").val();
        }
        var transDays = moneyType.get("transDays").val();
        if (!isEmpty(executeSort) && !isEmpty(isType)) {
            $.ajax({
                url: basePath + "/netbank/strategy/executeSort",
                method: "POST",
                dataType: 'json',
                contentType: "application/json",
                data: JSON.stringify({'type': isType,'executeSort':executeSort,'rules':data.field.rules,'isExistId':isExistId,'bankTypeIds':bankTypeId,'transDays':transDays}),
                success: function (t) {
                    if (t == "1") {
                        layer.alert("该策略已存在，不可重复提交！", {icon: 7});
                    } else if(t == "2"){
                        layer.alert("同一银行同一任务类型的优先执行排序不可重复", {icon: 7});
                    } else{
                        $.post(basePath + action, data.field, function (result) {
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}, function () {
                                parent.layer.closeAll();
                                if (result.success) {
                                    if (bt.navtab) {
                                        bt.navtab.refreshParent();
                                        bt.navtab.refreshCurrent()
                                    } else {
                                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                                    }
                                }
                            })
                        }, 'JSON');
                    }
                },
            });
            return false;
        }

    });
// 说明显示隐藏
    showDesc();
});

function showDesc() {
    var tip_index1 = 0;
    $('#bankUnique').mouseenter(function () {
        tip_index1 = layer.tips(bt.lang('table.bank.unique.illustrate'), '#bankUnique', {time: 30000});
    }).mouseleave(function () {
        layer.close(tip_index1);
    });
}

function toRegular() {
    var isRegular = dicts.get("isExistId").val();
    var isType = dicts.get("type").val();
    if (isRegular == undefined) {
        isRegular = $("#isExistId").val();
    }

    if (isRegular == "1") {
        $("#rulesa").hide();
        $("#transTimeRuleTr").hide();
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
    } else {
        $("#rulesa").show();
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

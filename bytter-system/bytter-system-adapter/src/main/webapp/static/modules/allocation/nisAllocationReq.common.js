/**
 * 清除收款账户和付款账户信息
 */
function clearBankAcc() {
    var $ = bt.jquery;
    $("#payerAccId").val("");
    $("#payerAccName").val("");
    $("#payerBankName").val("");

    $("#payeeCorpId").val("");
    $("#payeeAccId").val("");
    $("#payeeAcc").val("");
    $("#payeeAccName").val("");
    $("#payeeBankName").val("");

    $("#curId").val("");
    $("#curName").val("");
}

function clearPayerAcc() {
    var $ = bt.jquery;
    $("#payeeAcc").val("");
    $("#payeeAccName").val("");
    $("#payeeBankName").val("");

    $("#payerAcc").val("");
    $("#payerAccName").val("");
    $("#payerBankName").val("");
    $("#payerCorpId").val("");

    $("#curName").val("");
}

function clearPayeeAcc() {
    var $ = bt.jquery;
    $("#payeeAcc").val("");
    $("#payeeAccName").val("");
    $("#payeeBankName").val("");

    $("#payerAcc").val("");
    $("#payerAccName").val("");
    $("#payerBankName").val("");
    $("#payeeCorpId").val("");

    $("#curName").val("");
}

/**
 * 清除数据
 */
function clearAccByPos() {
    var $ = bt.jquery;
    $("#payeeAcc").val("");
    $("#payeeAccName").val("");
    $("#payeeBankName").val("");
    bt.moneyType.get("payeeAccBalance").val(0);
    bt.moneyType.get("payeeFcAccBalance").val(0);
    $("#payeeProvCity").val("");

    $("#payerAcc").val("");
    $("#payerAccName").val("");
    $("#payerBankName").val("");
    bt.moneyType.get("payerAccBalance").val(0);
    bt.moneyType.get("payerFcAccBalance").val(0);
    $("#payerProvCity").val("");
}

/**
 * 清除付款账号余额
 */
function clearPayerBal() {
    bt.moneyType.get("payerAccBalance").val(0);
    bt.moneyType.get("payerFcAccBalance").val(0);
}
/**
 * 清除收款账号余额
 */
function clearPayeeBal() {
    bt.moneyType.get("payeeAccBalance").val(0);
    bt.moneyType.get("payeeFcAccBalance").val(0);
}
/**
 * 设置内部账户余额
 * @param bankAcc   账户
 * @param curId     币别ID
 * @param _id    赋值的ID
 */
function initFcAccBalance(bankAcc, curId, _id) {
    $.ajax({
        url: basePath + "/inneraccount/fconlineledger?getByBankAcc",
        method: 'POST',
        data: {bankAcc: bankAcc, curId: curId},
        dataType: 'json',
        success: function (result) {
            var data = result.obj;
            var fcAccBal = null == data ? 0 : data.checkBalance;
            if ($("input[data-name='" + _id + "']").length) {
                bt.moneyType.get(_id).val(fcAccBal);
            } else {
                $("#" + _id).val(fcAccBal);
            }
        }
    });
}

/**
 * 设置银行账户余额
 * @param bankAcc   账户
 * @param curId     币别ID
 * @param _id    赋值的ID
 */
function initBankAccBalance(bankAcc, curId, _id) {
    $.ajax({
        url: basePath + "/bankacc/bisaccbal?getBal",
        method: 'POST',
        data: {bankAcc: bankAcc, curId: curId},
        dataType: 'json',
        success: function (result) {
            var bankAccBal = null == result ? 0 : result;
            if ($("input[data-name='" + _id + "']").length) {
                bt.moneyType.get(_id).val(bankAccBal);
            } else {
                $("#" + _id).val(bankAccBal);
            }
        }
    });
}

/**
 * 设置省市
 * @param bankAccId   账户
 * @param curId     币别ID
 * @param _id    赋值的ID
 */
function initProvAndCity(bankAccId, curId, _id) {
    $.ajax({
        url: basePath + "/bankacc/btbankacc?getBankAccById",
        method: 'POST',
        data: {id: bankAccId, curId: curId},
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                var data = result.obj;
                $("#" + _id).val(data.provName + "  " + data.cityName);
                initAreaSign();
            }
        }
    });
}

/**
 * 设置跨行标志
 */
function initBank() {
    var form = bt.form();
    var flag = $("#payerBankTypeId").val() != "" && $("#payeeBankTypeId").val() != "" &&
        $("#payerBankTypeId").val() == $("#payeeBankTypeId").val();
    if ($("div[data-name='difBank']").length) {
        var difBank = $("input[name='difBank']");
        if (flag) {
            difBank[0].checked = true;
            difBank[1].checked = false;
        } else {
            difBank[0].checked = false;
            difBank[1].checked = true;
        }
    } else {
        if (flag) {
            $("#difBank").val(0);
        } else {
            $("#difBank").val(1);
        }
    }
    form.render();
};

/**
 * 设置地域标志
 */
function initAreaSign() {
    var form = bt.form();
    var flag = $("#payeeProvCity").val() && $("#payerProvCity").val()
        && $("#payeeProvCity").val() == $("#payerProvCity").val();
    if ($("div[data-name='areaSign']").length) {
        var areaSign = $("input[name='areaSign']");
        if (flag) {
            areaSign[0].checked = true;
            areaSign[1].checked = false;
        } else {
            areaSign[0].checked = false;
            areaSign[1].checked = true;
        }
    } else {
        if (flag) {
            $("#areaSign").val(0);
        } else {
            $("#areaSign").val(1);
        }
    }
    form.render();
}

/**
 * 校验银行账户余额不足
 * @param accbal    银行账户余额
 * @param money     资金调拨金额
 * @param _id       金额聚焦文本框ID
 * @returns {boolean}
 */
function checkBankAccBal(accBal, money, _id) {
    // 根据系统参数"银行账户余额"不足判断
    var bankaccOver = $("#bankaccOver").val();
    var bankaccFlag = false;
    if (bankaccOver == 0) {
        // 设置余额不足时不提醒
        bankaccFlag = true;
    } else if (bankaccOver == 1 || bankaccOver == 2) {
        if (parseFloat(accBal) < parseFloat(money)) {
            if (bankaccOver == 2) { // 设置提醒且控制
                parent.layer.alert(bt.lang('nis.allocation.message.money.gt.balance.stop'), {icon: 2}, function () {
                    parent.layer.closeAll();
                    $('[data-id ="' + _id + '"] ').addClass("layui-form-danger");
                    $('[data-id ="' + _id + '"] ').focus();
                });
                return false;
            } else {
                bankaccFlag = false;
            }
        } else {
            bankaccFlag = true;
        }
    }
    return bankaccFlag;
}

/**
 * 校验内部账户余额不足
 * @param accbal    银行账户余额
 * @param money     资金调拨金额
 * @param _id       金额聚焦文本框ID
 * @returns {boolean}
 */
function checkFcAccBal(fcBal, money, _id) {
    // 根据系统参数"内部账户余额"不足判断
    var accountOver = $("#accountOver").val();
    var accountFlag = false;
    if (accountOver == 0) {
        // 设置余额不足时不提醒
        accountFlag = true;
    } else if (accountOver == 1 || accountOver == 2) {
        if (parseFloat(fcBal) < parseFloat(money)) {
            if (accountOver == 2) { // 设置提醒且控制
                parent.layer.alert(bt.lang('nis.allocation.message.money.gt.fcbalance.stop'), {icon: 2}, function () {
                    parent.layer.closeAll();
                    $('[data-id ="' + _id + '"] ').addClass("layui-form-danger");
                    $('[data-id ="' + _id + '"] ').focus();
                });
                return false;
            } else {
                // 设置提醒且金额小于等于余额
                accountFlag = false;
            }
        } else {
            accountFlag = true;
        }
    }
    return accountFlag;
}
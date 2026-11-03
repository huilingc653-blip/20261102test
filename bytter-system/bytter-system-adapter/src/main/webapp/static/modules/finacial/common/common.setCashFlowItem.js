
/**
 * 加载现金流项目
 * @param rowMR
 * @param fcAcc
 * @param deibitMoney
 * @param creditMoney
 */
function loadCashFlowItem(rowMR,fcAcc,deibitMoney,creditMoney,subjectId) {
    if (isEmpty(rowMR)) {
        return;
    }
    if (isEmpty(fcAcc)) {
        fcAcc = $("#account" + rowMR).val();
    }
    if (isEmpty(deibitMoney)) {
        deibitMoney = $("#debitMoney" + rowMR).val();
    }
    if (isEmpty(creditMoney)) {
        creditMoney = $("#creditMoney" + rowMR).val();
    }
    /**
     * 设置现金流项目
     */
    setCashFlowItem(rowMR, fcAcc, deibitMoney, creditMoney, subjectId);
}


/**
 * 设置现金流项目
 * @param rowMR
 */
function setCashFlowItem(rowMR, fcAcc, debitMoney, creditMoney, subjectId) {
    var jsonData = {};
    jsonData.fcAcc = fcAcc;
    if (Number(debitMoney) > 0 || Number(debitMoney) < 0) {
        jsonData.debit = "1";
    } else if (Number(creditMoney) > 0 || Number(creditMoney) < 0) {
        jsonData.credit = "1";
    }
    if (isNotEmpty(subjectId)) {
        jsonData.subjectId = subjectId;
    }
    if ((isNotEmpty(fcAcc) || isNotEmpty(subjectId)) && (isNotEmpty(jsonData.debit) || isNotEmpty(jsonData.credit))) {
        $.ajax({
            url: basePath + "/finacial/paramconfig/voucherCash/getFcVoucherCash",
            type: "POST",
            data: jsonData,
            dataType: "json",
            success: function (data) {
                var html = "";
                if (isNotEmpty(data) && data.success) {
                    if (isNotEmpty(data.obj)) {
                        data = data.obj;
                        if (Number(debitMoney)) {
                            html += '<input type="hidden" value="' + data.debitItem + '" name="bookDtls[' + rowMR + '].cashFlux">';
                            if (isNotEmpty(data.debitItemName)) {
                                html += data.debitItemName;
                            }
                        } else {
                            html += '<input type="hidden" value="' + data.creditItem + '" name="bookDtls[' + rowMR + '].cashFlux">';
                            if (isNotEmpty(data.creditItemName)) {
                                html += data.creditItemName;
                            }
                        }
                    }
                    $("#cashFlowItem" + rowMR).html(html);
                }
            }
        });
    }
}

/**
 * 判断未空
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    if (null === value || value === undefined || value === "" || value === " ") {
        return true;
    } else {
        return false;
    }
}

/**
 *判断非空
 * @param value
 * @returns {boolean}
 */
function isNotEmpty(value) {
    return !isEmpty(value);
}

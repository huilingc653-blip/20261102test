package com.bytter.core.common.datatable;

import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.enums.SystemMessageEnum;
import com.bytter.core.utils.EmptyUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public final class SqlOrderValidator {
    private static final Pattern ORDER_COLUMN_PATTERN = Pattern.compile(
            "^[A-Za-z_][A-Za-z0-9_$]*(\\.[A-Za-z_][A-Za-z0-9_$]*)?(\\s*,\\s*[A-Za-z_][A-Za-z0-9_$]*(\\.[A-Za-z_][A-Za-z0-9_$]*)?)*$");
    private static final Set<String> TRUSTED_ORDER_COLUMNS = new HashSet<String>(Arrays.asList(
            "(Z.BILL_MONEY-BT_NVL_NUM(C.BAIL_MONEY,0))",
            "(BT_NVL_NUM((SELECT SUM(BILL_MONEY) FROM LC_BILL_ARRIVAL WHERE LC_ID = LC.ID AND STATUS > 0),0))",
            "(BT_NVL_NUM(LC.CREDIT_MONEY - (SELECT SUM(BILL_MONEY) FROM LC_BILL_ARRIVAL WHERE LC_ID = LC.ID AND STATUS > 0),0))",
            "(BT_NVL_NUM((SELECT SUM(PAY_MONEY) FROM LC_BILL_ARRIVAL WHERE LC_ID = LC.ID AND STATUS > 0),0))",
            "(bt_nvl_num(t2.payment_money,0))",
            "(bt_nvl_num(t1.bill_money,0))",
            "(bt_nvl_num(ld.reapy_money, 0))",
            "(bt_nvl_num(ld.deposit_money, 0))",
            "(bt_nvl_num(li.sum_incoming_money, 0))"



    ));

    private SqlOrderValidator() {
    }

    public static void validateColumn(String column) {
        if (EmptyUtils.isBlank(column)) {
            return;
        }
        if (!ORDER_COLUMN_PATTERN.matcher(column).matches()) {
            throw new ParameterException(SystemMessageEnum.ORDER_MESSAGE_PARAM_ERROR);
        }
    }

    public static void validateDirection(String direction) {
        if (!Order.checkOrderParam(direction)) {
            throw new ParameterException(SystemMessageEnum.ORDER_MESSAGE_PARAM_ERROR);
        }
    }

    public static void validateOrderStringSort(String sort) {
        if (EmptyUtils.isBlank(sort)) {
            return;
        }
        validateDirection(sort);
    }

    public static void validateOrderString(OrderString orderString) {
        if (orderString == null) {
            return;
        }
        validateOrderStringSort(orderString.getSort());
        if (!TRUSTED_ORDER_COLUMNS.contains(orderString.getColumn())) {
            validateColumn(orderString.getColumn());
        }
    }
}

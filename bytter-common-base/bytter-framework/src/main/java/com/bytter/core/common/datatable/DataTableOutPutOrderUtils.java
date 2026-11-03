package com.bytter.core.common.datatable;

import com.bytter.core.utils.Reflections;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.column;

public class DataTableOutPutOrderUtils {


    /**
     *
     * @param dataTablesInput    前后端分离的V1DataTablesInput
     * @param data        查询出来的字段
     * @param fieldStr    需要排序的字段 用,隔开   比如 "curName,bankAcc"
     * @param <T>
     */
    public static <T> void orderOutputList(V1DataTablesInput dataTablesInput, List<T> data, String fieldStr) {
        if (CollectionUtils.isEmpty(data) || CollectionUtils.isEmpty(dataTablesInput.getOrderColumns())) {
            return;
        }

        // 检查 orderColumns 中是否有字段在 fieldStr 中存在
        if (fieldStr == null || fieldStr.trim().isEmpty()) {
            return;
        }
        String[] extraFields = fieldStr.split(",");
        java.util.Set<String> extraFieldSet = new HashSet<>();
        for (String field : extraFields) {
            String trimmedField = field.trim().toLowerCase();
            if (!trimmedField.isEmpty()) {
                extraFieldSet.add(trimmedField);
            }
        }

        if (extraFieldSet.isEmpty()) {
            return;
        }

        List<OrderColumn> orderColumns = dataTablesInput.getOrderColumns();
        boolean hasMatchingField = false;
        for (OrderColumn orderColumn : orderColumns) {
            if (orderColumn.getField() != null && extraFieldSet.contains(orderColumn.getField().toLowerCase())) {
                hasMatchingField = true;
                break;
            }
        }

        if (!hasMatchingField) {
            return;
        }

        // 构建排序比较器链
        Comparator<T> comparator = null;

        // 处理 V1DataTablesInput 中的排序条件
        for (OrderColumn orderColumn : orderColumns) {
            String fieldName = orderColumn.getField();
            String dir = orderColumn.getOrder();

            if (fieldName == null || fieldName.trim().isEmpty()) {
                continue;
            }

            if (dir == null || (!"asc".equalsIgnoreCase(dir) && !"desc".equalsIgnoreCase(dir))) {
                dir = "asc";
            }

            Comparator<T> fieldComparator = createFieldComparator(fieldName, dir);

            if (fieldComparator != null) {
                if (comparator == null) {
                    comparator = fieldComparator;
                } else {
                    comparator = comparator.thenComparing(fieldComparator);
                }
            }
        }

        if (comparator != null) {
            List<T> sortedList = data.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());

            data.clear();
            data.addAll(sortedList);
        }
    }

    public static <T> void orderOutputList(DataTablesInput dataTablesInput, List<T> data) {
        if (CollectionUtils.isEmpty(data) || CollectionUtils.isEmpty(dataTablesInput.getOrder())) {
            return;
        }

        List<Order> orders = dataTablesInput.getOrder();
        List<Column> columns = dataTablesInput.getColumns();

        if (CollectionUtils.isEmpty(columns)) {
            return;
        }

        // 构建排序比较器链
        Comparator<T> comparator = null;

        for (Order order : orders) {
            Integer columnIndex = order.getColumn();
            String dir = order.getDir();

            if (columnIndex == null || columnIndex < 0 || columnIndex >= columns.size()) {
                continue;
            }

            Column column = columns.get(columnIndex);
            if (column == null || !Boolean.TRUE.equals(column.getOrderable())) {
                continue;
            }

            String fieldName = column.getName();
            if (fieldName == null || fieldName.trim().isEmpty()) {
                continue;
            }

            // 创建当前字段的比较器
            Comparator<T> fieldComparator = createFieldComparator(fieldName, dir);

            if (fieldComparator != null) {
                if (comparator == null) {
                    comparator = fieldComparator;
                } else {
                    comparator = comparator.thenComparing(fieldComparator);
                }
            }
        }

        if (comparator != null) {
            List<T> sortedList = data.stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());

            data.clear();
            data.addAll(sortedList);
        }
    }

    private static <T> Comparator<T> createFieldComparator(String fieldName, String dir) {
        boolean isAsc = "asc".equalsIgnoreCase(dir);

        return (o1, o2) -> {
            try {
                Object value1 = Reflections.getFieldValue(o1, fieldName);
                Object value2 = Reflections.getFieldValue(o2, fieldName);

                if (value1 == null && value2 == null) {
                    return 0;
                }
                if (value1 == null) {
                    return isAsc ? -1 : 1;
                }
                if (value2 == null) {
                    return isAsc ? 1 : -1;
                }

                int compareResult;

                if (value1 instanceof Comparable) {
                    if (value1 instanceof String) {
                        compareResult = ((String) value1).compareToIgnoreCase((String) value2);
                    } else if (value1 instanceof BigDecimal) {
                        compareResult = ((BigDecimal) value1).compareTo((BigDecimal) value2);
                    } else if (value1 instanceof Date) {
                        compareResult = ((Date) value1).compareTo((Date) value2);
                    } else if (value1 instanceof Number) {
                        compareResult = Double.compare(((Number) value1).doubleValue(),
                                ((Number) value2).doubleValue());
                    } else {
                        compareResult = ((Comparable) value1).compareTo(value2);
                    }
                } else {
                    compareResult = value1.toString().compareToIgnoreCase(value2.toString());
                }

                return isAsc ? compareResult : -compareResult;

            } catch (Exception e) {
                return 0;
            }
        };
    }
}

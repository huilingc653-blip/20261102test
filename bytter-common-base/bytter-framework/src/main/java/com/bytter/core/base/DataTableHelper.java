package com.bytter.core.base;
import com.bytter.core.common.datatable.Column;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.Order;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.common.datatable.SqlOrderValidator;
import com.bytter.core.common.persistence.PropertyFilter;
import com.bytter.core.enums.MatchTypeEnum;
import com.bytter.core.enums.PropertyTypeEnum;
import com.bytter.core.utils.EmptyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.criterion.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by zhouj on 2017/5/2.
 */
public class DataTableHelper {
	private static final Logger logger = LoggerFactory.getLogger(DataTableHelper.class);
    //排序字段
    protected static String orderby(DataTablesInput input) {
        StringBuffer sb = new StringBuffer();
        if (!EmptyUtils.isEmptyList(input.getOrder())) {
            for (int i = 0; i < input.getOrder().size(); i++) {
                Order order = input.getOrder().get(i);
                SqlOrderValidator.validateDirection(order.getDir());
                Column column = input.getColumns().get(order.getColumn());
                if (column.getOrderable() && !EmptyUtils.isBlank(column.getName())) {
                    SqlOrderValidator.validateColumn(column.getName());
                    sb.append(column.getName()).append(",");
                }
            }
        }
        if (input.getOrderString() != null) {
            OrderString orderString = input.getOrderString();
            SqlOrderValidator.validateOrderString(orderString);
            sb.append(orderString.getColumn()).append(" ").append(orderString.getSort()).append(",");
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }
    //排序方式
    protected static String order(DataTablesInput input) {
        StringBuffer sb = new StringBuffer();
        if (!EmptyUtils.isEmptyList(input.getOrder())) {
            for (int i = 0; i < input.getOrder().size(); i++) {
                Order order = input.getOrder().get(i);
                SqlOrderValidator.validateDirection(order.getDir());
                Column column = input.getColumns().get(order.getColumn());
                if (column.getOrderable() && !EmptyUtils.isBlank(column.getName())) {
                    sb.append(order.getDir()).append(",");
                }
            }
        }
        if (input.getOrderString() != null) {
            OrderString orderString = input.getOrderString();
            SqlOrderValidator.validateOrderString(orderString);
            sb.append(orderString.getColumn()).append(" ").append(orderString.getSort()).append(",");
        }
        if (sb.length() > 0) sb.setLength(sb.length() - 1);
        return sb.toString();
    }
    //列查询filter
    protected static Criterion[] filter(DataTablesInput input) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        if (!EmptyUtils.isEmpty(input.getSearch())) {
            List<Column> list = input.getColumns();
            for (Column column : list) {
                if (column.getSearchable() && !EmptyUtils.isBlank(column.getSearch().getValue())) {
                    Criterion criterion = Restrictions.like(column.getName(), column.getSearch().getValue(), MatchMode.ANYWHERE);
                    criterions.add(criterion);
                }
            }
        }
        return criterions.toArray(new Criterion[criterions.size()]);
    }
    /**
     * 搜索
     * eg.
     * EQS_name  eq的String类型name属性
     * LIKES_name_OR_email
     *
     * @param input {@link DataTablesInput}
     * @return 搜索属性集合
     */
    public static List<PropertyFilter> search(DataTablesInput input) {
        List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();
        if (!EmptyUtils.isEmpty(input.getFormdata())) {
            for (Map.Entry<String, Object> entry : input.getFormdata().entrySet()) {
                String filterName = entry.getKey();
                Object value = entry.getValue();
                try {
                    if (!EmptyUtils.isEmpty(value) && !EmptyUtils.isBlank(String.valueOf(value))) {
                        filterList.add(new PropertyFilter(filterName, String.valueOf(value)));
                    }
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
            }
        }
        return filterList;
    }
    /**
     * 兼容针对in多个查询时 类型统一转变为字符串
     * eg.
     * EQS_name  eq的String类型name属性
     * LIKES_name_OR_email
     *
     * @param input {@link DataTablesInput}
     * @return 搜索属性集合
     */
    public static List<PropertyFilter> searchWithIn(DataTablesInput input) {
        List<PropertyFilter> filterList = new ArrayList<PropertyFilter>();
        if (!EmptyUtils.isEmpty(input.getFormdata())) {
            for (Map.Entry<String, Object> entry : input.getFormdata().entrySet()) {
                String filterName = entry.getKey();
                Object value = entry.getValue();
                try {
                    if (!EmptyUtils.isEmpty(value) && !EmptyUtils.isBlank(String.valueOf(value))) {
                        if (filterName.toLowerCase().startsWith(MatchTypeEnum.IN.getValue()) && String.valueOf(value).contains(",")) {
                            filterName = filterName.substring(0, MatchTypeEnum.IN.getValue().length()) + PropertyTypeEnum.S.name().toLowerCase() + filterName.substring(MatchTypeEnum.IN.getValue().length() + 1);
                        }
                        filterList.add(new PropertyFilter(filterName, String.valueOf(value)));
                    }
                } catch (Exception e) {
                    logger.debug(e.getMessage());
                }
            }
        }
        return filterList;
    }
    /**
     * 查询字段
     *
     * @param input {@link DataTablesInput}
     * @return {@link ProjectionList}
     */
    public static ProjectionList select(DataTablesInput input) {
        ProjectionList pList = Projections.projectionList();
        if (!EmptyUtils.isEmpty(input.getSearch())) {
            List<Column> list = input.getColumns();
            for (Column column : list) {
                if (!EmptyUtils.isBlank(column.getData())) {
                    pList.add(Projections.property(column.getName()));
                }
            }
        }
        return pList;
    }
}

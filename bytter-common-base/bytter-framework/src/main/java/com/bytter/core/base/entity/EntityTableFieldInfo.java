package com.bytter.core.base.entity;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;
/**
 * 实体对应表和字段信息
 * 
 * @author zhengg
 *
 */
public class EntityTableFieldInfo {
    /**
     * 实体表信息缓存
     */
    public static final Map<Class<?>, EntityTableFieldInfo> ENTITY_TABLE = new HashMap<>();
    /**
     * 进行单例模式获取对象
     * 
     * @param entityCalss
     * @return
     */
    public static synchronized EntityTableFieldInfo getEntityTableFieldInfo(Class<?> entityCalss) {
        EntityTableFieldInfo entityTableInfo;
        if (ENTITY_TABLE.containsKey(entityCalss)) {
            entityTableInfo = ENTITY_TABLE.get(entityCalss);
        } else {
            entityTableInfo = new EntityTableFieldInfo(entityCalss);
            ENTITY_TABLE.put(entityCalss, entityTableInfo);
        }
        return entityTableInfo;
    }
    /**
     * 进行单例模式创建对象
     * 
     * @param entityCalss
     * @return
     */
    private EntityTableFieldInfo(Class<?> entityCalss) {
        this.entityCalss = entityCalss;
        initEntityTableFieldInfo();
    }
    /**
     * 表名称
     */
    private String entityTable;
    /**
     * 实体class
     */
    private Class<?> entityCalss;
    /**
     * 实体对应数据库表字段和属性取值方法名称 key为表字段值,value为属性取值方法名称
     */
    private Map<String, String> tableField;
    /**
     * 实体对应写入sql语句
     */
    private String insetSql;
    public String getInsetSql() {
        return insetSql;
    }
    public void setInsetSql(String insetSql) {
        this.insetSql = insetSql;
    }
    public String getEntityTable() {
        return entityTable;
    }
    public void setEntityTable(String entityTable) {
        this.entityTable = entityTable;
    }
    public Class<?> getEntityCalss() {
        return entityCalss;
    }
    public void setEntityCalss(Class entityCalss) {
        this.entityCalss = entityCalss;
    }
    public Map<String, String> getTableField() {
        return tableField;
    }
    public void setTableField(Map<String, String> tableField) {
        this.tableField = tableField;
    }
    /**
     * 对inset写入sql进行拼装
     */
    private void initInsetSql() {
        if (tableField != null && !tableField.isEmpty()) {
            // insert into table(name,age) values(?,?)
            StringBuilder sql = new StringBuilder("insert into ");
            sql.append(entityTable).append("(");
            StringBuilder values = new StringBuilder();
            for (Entry<String, String> entry : tableField.entrySet()) {
                if (values.length() > 0) {
                    sql.append(",");
                    values.append(",");
                }
                sql.append(entry.getKey());
                values.append("?");
            }
            sql.append(") values(").append(values.toString()).append(")");
            setInsetSql(sql.toString());
        }
    }
    /**
     * 对实体数据初始化
     */
    private void initEntityTableFieldInfo() {
        if (entityCalss != null) {
            this.setEntityTable(getTableName(entityCalss));
            if (StringUtils.isNotEmpty(entityTable)) {
                initTableFieldList(entityCalss);
                initInsetSql();
            }
        }
    }
    /**
     * 获取表明
     * 
     * @param clazz
     * @return
     */
    private String getTableName(Class<?> clazz) {
        Table annotation = (Table) clazz.getAnnotation(Table.class);
        if (annotation != null) {
            return annotation.name();
        }
        return null;
    }
    /**
     * 初始化表字段信息
     * 
     * @param clazz
     */
    private void initTableFieldList(Class<?> clazz) {
        Map<String, String> tableFieldInfo = new LinkedHashMap<>();
        // 通过方法获取字段注解信息
        List<Method> methodList = new ArrayList<>();
        BeanUtils.getAllMethods(clazz, methodList);
        boolean isField = true;// 用来判断是否需要通过属性获取
        for (Method method : methodList) {
            if (method.isAnnotationPresent(Column.class)) {
                if (isField) {
                    isField = false;// 设置不用通过属性获取
                }
                Column column = method.getAnnotation(Column.class);
                tableFieldInfo.put(column.name(), method.getName());
            } else if (!method.isAnnotationPresent(Transient.class) && method.getName().startsWith("get")) {
                tableFieldInfo.put(method.getName().substring(3), method.getName());
            }
        }
        if (isField) {
            tableFieldInfo = new LinkedHashMap<>();
            List<Field> fieldList = new ArrayList<>();
            BeanUtils.getAllFields(clazz, fieldList);
            for (Field field : fieldList) {
                if (field.isAnnotationPresent(Column.class)) {
                    if (isField) {
                        isField = false;// 设置不用通过属性获取
                    }
                    Column column = field.getAnnotation(Column.class);
                    tableFieldInfo.put(column.name(), "get" + field.getName().substring(0, field.getName().length())
                            + field.getName().substring(1));
                } else if (!field.isAnnotationPresent(Transient.class)) {
                    tableFieldInfo.put(field.getName(), "get" + field.getName().substring(0, field.getName().length())
                            + field.getName().substring(1));
                }
            }
        }
        if (!tableFieldInfo.isEmpty()) {
            setTableField(tableFieldInfo);
        }
    }
    /**
     * 获取实体对应值
     * 
     * @param entity
     *            实体对象
     * @param field
     *            表字段
     * @return
     */
    public Object getValueByEntity(Object entity, String field) {
        if (entityCalss != null && tableField != null && tableField.containsKey(field)) {
            Method method;
            try {
                method = entityCalss.getMethod(tableField.get(field));
                Object value = method.invoke(entity);
                if (value == null && field.equalsIgnoreCase("id")) {
                    value = UUID.randomUUID().toString().replace("-", "");
                }
                return value;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

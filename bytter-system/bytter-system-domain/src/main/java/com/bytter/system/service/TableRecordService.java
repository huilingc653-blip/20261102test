package com.bytter.system.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.dao.TableRecordDao;
import com.bytter.system.entity.TableRecord;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhoul on 2017/11/9.
 */
@Service("tableRecordService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TableRecordService extends BaseService {
    @Autowired
    private TableRecordDao tableRecordDao;
    @Override
    public BaseDao getEntityDao() {
        return tableRecordDao;
    }

    public static final Set<String> igKeys = new HashSet<>();
    static {
        // 外汇收付交易列表默认隐藏产品、收付方向（用户可选择显示）
        igKeys.add("DataTables_appTable_/foreign/fextranreq/list/4");
        igKeys.add("DataTables_appTable_/foreign/fextranreq/query/4");
    }


    public TableRecord getByTableKey(String tableKey,String userId) {
        TableRecord tableRecord = tableRecordDao.getByTableKeyAndUserId(tableKey, userId);
        if (null == tableRecord && igKeys.contains(tableKey)){
            tableRecord =tableRecordDao.getDefTableKey(tableKey);
        }
        return tableRecord;
    }


    public synchronized void saveOrUpdate(String tableKey, String content, String userId) {
        TableRecord tableRecord = tableRecordDao.getByTableKeyAndUserId(tableKey, userId);
        if (tableRecord != null) {
            tableRecord.setContent(content);
            tableRecordDao.update(tableRecord);
        } else {
            tableRecord = new TableRecord();
            tableRecord.setUserId(userId);
            tableRecord.setTableKey(tableKey);
            tableRecord.setContent(content);
            tableRecordDao.save(tableRecord);
        }
    }
    public void deleteByTableKey(String tableKey, String userId) {
        TableRecord tableRecord = tableRecordDao.getByTableKeyAndUserId(tableKey, userId);
        if (!EmptyUtils.isEmpty(tableRecord) && StringUtils.isNotBlank(tableRecord.getId())) {
            tableRecordDao.delete(tableRecord.getId());
        }
    }
}

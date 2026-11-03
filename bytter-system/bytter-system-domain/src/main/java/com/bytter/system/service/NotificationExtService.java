package com.bytter.system.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.system.dao.NotificationExtDao;
import com.bytter.system.entity.Notification;
import com.bytter.system.entity.NotificationExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("notificationExtService")
public class NotificationExtService  extends BaseService<NotificationExt, String> {

    @Autowired
    NotificationExtDao notificationExtDao;

    @Override
    public BaseDao<NotificationExt, String> getEntityDao() {
        return notificationExtDao;
    }
}

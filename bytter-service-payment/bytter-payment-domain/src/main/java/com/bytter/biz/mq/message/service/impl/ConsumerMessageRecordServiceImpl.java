package com.bytter.biz.mq.message.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytter.biz.mq.message.dao.ConsumerMessageRecordMapper;
import com.bytter.biz.mq.message.entity.ConsumerMessageRecord;
import com.bytter.biz.mq.message.service.ConsumerMessageRecordService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author YonJar
 * @date 2024/4/11.
 * @description
 */
@Service
@Transactional
public class ConsumerMessageRecordServiceImpl extends ServiceImpl<ConsumerMessageRecordMapper, ConsumerMessageRecord> implements ConsumerMessageRecordService {

}

package com.bytter.biz.finacial.mq.message.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytter.biz.finacial.mq.message.dao.ConsumerMessageRecordMapper;
import com.bytter.biz.finacial.mq.message.entity.ConsumerMessageRecord;
import com.bytter.biz.finacial.mq.message.service.ConsumerMessageRecordService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author YonJar
 * @date 2024/3/4.
 * @description
 */
@Service
@Transactional
public class ConsumerMessageRecordServiceImpl extends ServiceImpl<ConsumerMessageRecordMapper, ConsumerMessageRecord> implements ConsumerMessageRecordService {

}

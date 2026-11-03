##修改日期：2025-7-10
##修改人员：zrs
##头寸调拨审批企微消息提醒定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '自动同步多级单位内部账号余额', null, null, 'XXL', '', 'CRON', '0 0/10 * * * ?', 'DO_NOTHING', 'ROUND', 'AutoSyncLevelCorpBalancesJob', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='自动同步多级单位内部账号余额' limit 1);
commit;
##GOGO

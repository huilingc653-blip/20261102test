###修改人:zhous
###修改日期:2025-4-14
###修改内容:添加保证金账户自动销户定时任务
INSERT INTO xxl_job_info (id, job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select (select max(id)+1 from xxl_job_info), 14, '保证金账户自动销户', '2024-08-20 16:30:15', '2024-08-20 16:30:15', 'XXL', '', 'CRON', '0 0 1 * * ?', 'DO_NOTHING', 'FIRST', 'autoAccCancellTaskService', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', '2024-08-15 13:43:48', '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='保证金账户自动销户' limit 1);
commit;
##GOGO

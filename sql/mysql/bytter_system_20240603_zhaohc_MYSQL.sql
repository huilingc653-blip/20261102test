##修改人:zhaohc
##修改内容:添加定时任务
##修改时间:2024-06-03
INSERT INTO xxl_job_info (job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid, trigger_status, trigger_last_time, trigger_next_time)
select 15, '大参林SSO员工信息同步', now(), now(), 'XXL', '', 'CRON', '0 0 22 1/1 * ?', 'DO_NOTHING', 'FIRST', 'dslSsoSyncUserJob', '{"startDate":"2024-05-15","endDate":"2024-05-18"}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', now(), '', 0, 0, 0 from dual
where not exists (select 1 from xxl_job_info where job_desc ='大参林SSO员工信息同步' limit 1);
##GOGO
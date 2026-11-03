package com.bytter.job.admin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * 定时任务调度中心启动类。
 *
 * @author zhaohc
 */
@SpringBootApplication
public class BytterJobAdminApplication {
	public static void main(String[] args) {
        SpringApplication.run(BytterJobAdminApplication.class, args);
	}
}


### 运行环境

- jdk1.8 maven git
- 中间件 nacos(=2.2.0) redis(>=5.0) zipkin() minio
- 数据库 mysql（>=5.7） | oracle
开发环境部署 deploy.sh ${服务名}    

### spring系列版本

| SpringBoot | SpringCloud | SpringCloud-Alibaba | Dubbo  |     
|------------|-------------|---------------------|--------|
| 2.3.12     | Hoxton.SR12 | 2.2.9               | 3.1.10 |


### 本地运行

- 因存在循环依赖名，某些项目mvn打包构建存在问题，可以先执行项目下install_all_api.sh（需要maven和jdk环境）脚本构建所有依赖，
再打包启动自己项目
- dubbo调用需要调度到本机实例需把负载均衡模式设置为dev 配置参见system application-dev文件


### 开发环境

#### 资源

| 资源名              | 版本                           | 地址            | 端口   | 用户名        | 密码         |
|------------------|------------------------------|---------------|------|------------|------------|
| 服务器（ssh）         | CentOS Linux 7 (Core)        | 192.168.0.111 | 22   | root       | bytter.123     |
| 数据库（mysql）       | 8.0.40                       | 192.168.0.111 | 3306 | root       | bytter.123     |
| 缓存（redis）        | 6.2.6                        | 192.168.0.111 | 6379 | -          | bytter.123     |
| 注册配置中心（nacos）    | 2.2.0                        | 192.168.0.111 | 8848 | nacos      | nacos      |
| 消息队列（rabbitMQ）   | 3.8.21                       | 192.168.0.111 | 5672 | admin      | bytter.123    |
| 任务调度中心（xxl-job）  | 2.3.1                        | 192.168.0.111 | 8061 | admin      | 123456     |
| 文件存储（minio）      | RELEASE.2023-06-16T02-41-06Z | 192.168.0.111 | 9000 | admin | bytter.123 |



#### 中间件管理页面
* 服务首页：http://192.168.0.111:8080/
* 链路追踪：http://192.168.0.111:9411/zipkin/
* Nacos：http://192.168.0.111:8848/nacos/
* RabbitMQ：http://192.168.0.111:15672/
* Minio：http://192.168.0.111:9001/
* xxl-job：http://192.168.0.111:8061/toLogin
### 重构基本要求
- 业务服务只能依赖其他业务服务的client层
- 服务间不能直接查询其他服务的表，需通过dubbo调用或者做字段冗余
- 业务服务提供的对外api统一写到各自模块下的client包里面，api层需有详细文档
- 非对外接口建议定义到模块domain层
- 建议不要三张表以上关联查询，bi模块第一期不受约束
- 其他待补充

### 基础模块（7个）

- bytter-parent 依赖包，仓库、构建方式等统一管理，以pom方式进行打包
- bytter-common-base 管理通用工具类，通用依赖配置，pom打包
  - bytter-framework 工具类与核心配置类，用户上下文工具,entity,dao,controller等基类提供
  - bytter-poi excel、word及pdf下载上传，通用校验，数据解析封装 需重写
  - bytter-velocity 兼容springboot2.x.x使用velocity模版引擎包
- bytter-gateway 服务网关，统一异常处理，动态路由分发，鉴权、限流等功能 未实现完成
- bytter-system 提供用户管理，角色，组织，银行账号等基础配置服务 
- bytter-auth 提供用户认证，资源认证服务
- bytter-workflow 业务工作流相关功能提供
- bytter-job 定时任务模块

### 基础服务需要提供能力

- 统一鉴权，路由，负载均衡
- 限流、应用服务熔断降级处理
- 统一异常处理，链路日志追踪
- 任务调度、工作流、poi处理
- dubbo重试幂等工具，连续序列号，lock，事务问题
- 其他工具包

### 业务模块（17个）

  **语雀文档[模块与T6对应关系](https://bytter.yuque.com/xosf4a/rre4yv/gfx8sucoxago40tq)**

  **业务模块具体业务单元查看模块下的readme.md文件**

  **命名规则 前缀=公司名 中缀=架构层次名 后缀=业务领域名**

  **中缀意义 servcie业务流数据来源核心模块，prepose支撑业务✅本身不单独产生有业务流的数据，manager产生的业务数据流入后置处理？**

  **理论依赖manager->service->prepose?**

---------------------------------------
1. [x] bytter-service-payment 结算管理（记账）
2. [x] bytter-service-increment 增值管理
3. [x] bytter-service-fundwarn 资金监控
4. [x] bytter-service-report 报表
5. [x] bytter-service-bdlink 银企直联
6. [x] bytter-service-creditRating 机构关系管理
7. [x] bytter-service-ras 营收稽核
----------------------------------------
1. [x] bytte-manager-plan 资金计划
2. [x] bytter-manager-account 核算管理
----------------------------------------
1. [x] bytter-prepose-bill 票据（授信、担保、融资）
2. [x] bytter-prepose-credential 凭证管理
3. [x] bytter-prepose-erp erp对接模块
4. [x] bytter-prepose-check 资金核对
5. [x] bytter-prepose-billBankImp 票据银企服务
6. [x] bytter-prepose-letter 保函
7. [x] bytter-prepose-cashVoucher 空白凭证
8. [x] bytter-prepose-accountReceivable 应收款管理
----------------------------------------


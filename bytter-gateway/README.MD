# bytter-gateway（统一 API 网关）

> 2026-09-12 依据代码与配置事实新建（原仅有 Spring Initializr 的 `HELP.md`）：本服务是平台**统一 API 网关**
> （Spring Cloud Gateway · WebFlux 响应式，端口 8080），**无任何数据库访问——0 张表、无 JDBC/JPA/MyBatis 依赖与配置**；
> 它的"业务关系"全部体现在 **31 条路由 + 8 段过滤器链 + Dubbo 鉴权**上，配置与白名单在 `application-dev.yml`，
> 动态配置（H5 路由、XSS、Redis 连接）来自 Nacos。

## 模块介绍

**bytter-gateway** 是所有外部流量进入微服务的唯一入口，承担五件事：

1. **统一接入**：PC / APP（`customMethod` 重写请求方法并追加 `reqterminal=app`）/ H5（静态资源映射）三类终端入口；
2. **安全前置**：IP 白名单、SQL 注入拦截、请求体与响应体 XSS 清洗、请求体缓存供重复读；
3. **鉴权与上下文**：令牌校验 + URL 级资源鉴权（经 Dubbo 调 `bytter-auth`），通过后向下游注入 `x-client-token-user` 与 `X-Tenant-ID`；
4. **路由分发**：按 `Path` 断言把请求转发到 17 个业务服务（`lb://`），并对 14 个服务提供 Swagger 文档聚合；
5. **统一异常收敛**：把令牌失效、无权限、IP 非法、服务不可用等统一渲染为重定向或标准 JSON。

### 代码分层

```
- bytter-gateway
    - filter/           访问鉴权 AccessGatewayFilter · 安全过滤 filter/xss/ · IPCheckFilter
                        · CustomRewritePathFilter（APP 方法重写）· DevLoadBalancerFilter（开发环境 LB）
    - auth/             IAuthService / AuthServiceImpl（Dubbo 出口 = bytter-auth 的 ApiIAuthenticationService）
    - config/           路由装配与配置：FilterBeanConfig · IgnoreUrlsConfig · RedisConfig
                        · H5RouterConfig/H5ResourcesProperties · SwaggerResourceConfig · DevLbConfig/DevDubboLoadBalance/DevServerInstanceLoadBalancer
    - exception/        统一异常渲染 CustomErrorWebExceptionHandler + ExceptionAutoConfiguration + 4 个自定义异常
    - handler/          SwaggerHandler（/swagger-resources 三个端点）
    - context/ util/    GatewayCodeEnum 错误码 · Result/R · WebFluxUtil · SqlInjectionUtil · XssCleanRuleUtils
```

### 子域职责

| 子域（类族） | 职责 |
|---|---|
| `IPCheckFilter` | 取真实客户端 IP（`x-forwarded-for`/`Proxy-Client-IP`/`WL-Proxy-Client-IP`/`X-Real-IP`），回环放行，其余经 Dubbo `checkIp` 判白名单 |
| `SqlInjectionFilter` | 校验 query（GET）与 body（POST/PUT 的 form/json）的 SQL 注入模式，命中直接返回 200 + `code 403`（`/olap` 路径放行） |
| `CacheBodyGlobalFilter` + `XssFilter` + `XssResponseGlobalFilter` | 缓存请求体 → 清洗 JSON 请求体 XSS → 清洗 JSON 响应体 XSS（`security.xss.*` 可开关与排除） |
| `CustomRewritePathFilter` | 依据 query `customMethod` 重写 HTTP 方法并追加 `reqterminal=app`（移动端兼容） |
| `AccessGatewayFilter` | 鉴权核心：静态前缀与白名单放行 → 三处取令牌 → `checkToken` → JWT 解析 → `hasPermissionV1` → 注入下游头 |
| `AuthServiceImpl` | Dubbo 出口：`ignoreAuthentication`（前缀匹配）、`checkToken`/`hasPermission`/`hasPermissionV1`/`ipWhiteList`/`checkIp`/`tokenExpiredNotify`、本地 `getJwt` |
| `DevLoadBalancerFilter` + `DevServerInstanceLoadBalancer` + `DevDubboLoadBalance` | 开发环境"本机 IP 优先"的 HTTP 与 Dubbo 负载均衡 |
| `CustomErrorWebExceptionHandler` | 统一异常渲染：重定向（`/login`、`/403`、`/404`、`/503`）与标准 JSON 错误码 |
| `SwaggerResourceConfig` + `SwaggerHandler` | 聚合 `api-docs-*` 路由为 Swagger 资源并暴露 `/swagger-resources` |

## 数据与外部状态（0 表口径）

| 分区 | 内容 | 证据 |
|---|---|---|
| ① 本服务直连表 | **无**（0 张）——`pom.xml` 无 JDBC/MyBatis/JPA 依赖，无 `spring.datasource` 配置，账本 `tables`/`edges` 为空 | `bytter-gateway/pom.xml:16-96`；`tools/service-db-diagram/sources/gateway/gateway_ledger.json` |
| ② 进程内状态 | 仅请求级 body 缓存（`CacheBodyGlobalFilter`，含白名单 `/api/notification/haveReadAll`）；无本地业务缓存 | `filter/xss/CacheBodyGlobalFilter.java:31-53` |
| ③ 外部状态 | Redis：依赖 `spring-boot-starter-data-redis-reactive` 与 `config/RedisConfig.java`，**但限流器 `DefaultRedisRateLimiter` 的 `@Configuration` 已被注释 → 限流当前不生效**；`spring.redis.*` 取值来自 Nacos `common.yml`（仓库内未确认） | `config/DefaultRedisRateLimiter.java:15`；`pom.xml:62-66` |
| ④ 鉴权数据归属 | 令牌与 URL→资源码映射归 `bytter-auth`（其映射缓存与 `SYS_RESOURCES` 数据在 `bytter-auth`/`bytter-system` 侧），**网关自身不落库** | `auth/service/impl/AuthServiceImpl.java:32-33` |

> ⚠️ **本服务不产出 ERD**：没有直连表可画。服务关系由本文的「请求处理主流程 + 路由表」与
> `work/gateway_business_flow.workflow.tables.html` 表达（该图 7 节点 / 0 表引用，节点面板承载链路语义）。

## 请求处理主流程

过滤器按 `getOrder()` 升序执行（**前置安全过滤全部先于鉴权**）：

| 顺序 | 组件 | order | 关键行为 |
|---|---|---|---|
| 1 | `SqlInjectionFilter` | `HIGHEST_PRECEDENCE - 1`（表达式见风险节） | query/body SQL 注入检测，命中直接写响应不抛异常 |
| 2 | `IPCheckFilter` | `HIGHEST_PRECEDENCE` | 真实 IP → Dubbo `checkIp`，失败抛 `IpCheckException` |
| 3 | `CustomRewritePathFilter` | `HIGHEST_PRECEDENCE` | `customMethod` 重写方法 + 追加 `reqterminal=app` |
| 4 | `CacheBodyGlobalFilter` | `HIGHEST_PRECEDENCE` | POST/PUT 的 json/form body 缓存并装饰可重复读 |
| 5 | `XssFilter` | `HIGHEST_PRECEDENCE` | JSON 请求体 XSS 清洗（重建 body、删 `Content-Length`、置 `Transfer-Encoding: chunked`） |
| 6 | `AccessGatewayFilter` | 未声明（接口默认） | **鉴权核心**：`/static` 与白名单放行 → 取令牌 → `checkToken` → JWT 解析 → `hasPermissionV1` → 注入头 |
| 7 | `XssResponseGlobalFilter` | `-50` | JSON 响应体 XSS 清洗（仅命中 HTML 标签正则时改写） |
| 8 | `DevLoadBalancerFilter` | `10150` | 替换默认 LB 过滤器，传入 `clientIp` 供"本机优先"选择实例 |

主路径：**请求接入 → 前置安全过滤 → 白名单与取令牌 → 令牌校验与资源鉴权 → 上下文注入 → 路由匹配与负载均衡 → 转发下游/异常收敛**。

### 令牌提取与上下文注入

- 令牌来源三选一（按序）：Cookie `Access-Token` → Header `Authorization` → query `access_token`（`filter/AccessGatewayFilter.java:81-100`），取不到即 `TokenException`；
- 鉴权通过后写入两个下游必需头：`x-client-token-user`（subject 的 encode 值）与 `X-Tenant-ID`（租户 ID）（`:120-122`）；
- 白名单：`gate.ignore.authentication.startwith`（26 个前缀）由 `AuthServiceImpl` 按 `,` 拆分后 `url.startsWith` 匹配（`:46-52`），另有硬编码 `/static` 前缀放行（`AccessGatewayFilter.java:45,50-57`）。

### 异常与响应矩阵

| 触发 | 结果 | 证据 |
|---|---|---|
| `TokenException` / `ExpiredJwtException` | 303 重定向 `/login` | `exception/CustomErrorWebExceptionHandler.java:93-96` |
| 无资源权限（Ajax） | 403 JSON `{success:false,msg:...}` | `:97-108,173-181` |
| 无资源权限（非 Ajax） | 重定向 `/403` | `:106` |
| `IpCheckException` | 200 + `code 4003`「非法ip访问，请联系管理员」 | `:111-112`；`context/GatewayCodeEnum.java:14` |
| SQL 注入命中 | 200 + `code 403`「疑似SQL注入请求」 | `filter/xss/SqlInjectionFilter.java:160-163` |
| `RpcException` | `code CM5003`（认证服务未找到） | `:113` |
| 404 / 503 / 504 | 303 `/404`、308 `/503?serviceName=...`、`GATEWAY_CONNECT_TIME_OUT` | `:77-88,134-152` |

## 路由表（31 条 = 17 业务路由 + 14 文档路由）

全部显式声明在 `src/main/resources/application-dev.yml:20-177`；`spring.cloud.gateway.discovery.locator.enabled: false`（`:17-19`）表示**不按服务名自动生成路由**。

| # | 路由 id | 目标（`lb://`） | filters | 定义位置 |
|---|---|---|---|---|
| 1 | bytter-prepose-erp | bytter-prepose-erp | — | `:21-24` |
| 2 | bytter-auth | bytter-auth | — | `:25-28` |
| 3 | bytter-system | bytter-system | `PreserveHostHeader` | `:29-34` |
| 4 | bytter-prepose-bill | bytter-prepose-bill | — | `:35-38` |
| 5 | bytter-prepose-credential | bytter-prepose-credential | — | `:39-42` |
| 6 | bytter-prepose-cashvoucher | bytter-prepose-cashvoucher | — | `:43-46` |
| 7 | bytter-manager-plan | bytter-manager-plan | — | `:47-50` |
| 8 | bytter-prepose-check | bytter-prepose-check | — | `:51-54` |
| 9 | bytter-manager-account | bytter-manager-account | — | `:55-58` |
| 10 | bytter-service-fundwarn | bytter-service-fundwarn | — | `:59-62` |
| 11 | bytter-service-bdlink | bytter-service-bdlink | — | `:63-66` |
| 12 | bytter-service-payment | bytter-service-payment | — | `:67-70` |
| 13 | bytter-service-increment | bytter-service-increment | — | `:71-74` |
| 14 | bytter-service-report | bytter-service-report | — | `:75-78` |
| 15 | bytter-service-ras | bytter-service-ras | — | `:79-82` |
| 16 | bytter-prepose-accountReceivable | bytter-prepose-accountReceivable | — | `:83-86` |
| 17 | bytter-service-creditRating | bytter-service-creditRating | — | `:87-90` |
| 18-31 | `api-docs-bytter-{prepose-bill, prepose-erp, system(+workflow), prepose-credential, prepose-cashvoucher, manager-plan, prepose-check, manager-account, service-fundwarn, service-bdlink, service-payment, service-increment, service-report, prepose-accountReceivable}` | 对应业务服务 | `StripPrefix=1` | `:94-177` |

**Path 断言要点**：17 条业务路由的 `Path=` 断言是**逐接口枚举**（如 `bytter-system` 与 `bytter-service-payment` 各列举数十个前缀），不是按服务名统一前缀；
**同一前缀可能分散在多条路由**（例如 `/bill/**` 系分布在 prepose-bill / prepose-cashvoucher / service-report 三条路由上），新增接口时需确认归属路由，否则会 404。

**文档路由约定**：`api-docs-` 前缀为硬约定（`SwaggerResourceConfig` 按该前缀过滤生成资源），生产环境不配置接口文档（`:92-93`）；`StripPrefix=1` 使 `/api-docs-{svc}/v2/api-docs` 转发到后端 `/v2/api-docs`；`api-docs-bytter-system` 额外覆盖 `/api-docs-bytter-workflow/**`（`:109`）。注意 `bytter-auth`、`bytter-service-ras`、`bytter-service-creditRating` **没有** api-docs 路由。

## 服务发现与负载均衡

- **Nacos**：`bytter-gateway` 注册与配置（dev `192.168.0.111:8848` / `bytter-micro-dev`；test/prod 见 `bootstrap.yml`），`shared-configs: common.yml`；dev 的 `refresh-enabled: false`。
- **HTTP 负载均衡**：`@LoadBalancerClients(defaultConfiguration = DevLbConfig.class)` → `DevServerInstanceLoadBalancer`：取 `DevLoadBalancerFilter` 传入的 `clientIp`，**优先选择 host 与调用方 IP 相同的实例，否则随机**；`localhost`/IPv6 回环时改取本机网卡地址。
- **Dubbo 负载均衡**：SPI `dev` = `DevDubboLoadBalance`（`META-INF/dubbo/org.apache.dubbo.rpc.cluster.LoadBalance`），**本机 host 优先、否则回退随机**；`ribbon.enabled: false`。
- **超时**：网关 httpclient `connect-timeout 3000000` / `response-timeout 30000000`；Dubbo consumer/provider `timeout 10000`、`check false`。

## 接口能力与依赖

- **对外**：唯一入口端口 8080（`application-dev.yml:2`），`max-http-header-size 1024000`；HTTPS 配置存在但 `enabled: false`（`:4-9`）。
- **Dubbo 引用**：`bytter-auth` 的 `ApiIAuthenticationService`（`checkToken` / `hasPermission` / `hasPermissionV1` / `ipWhiteList` / `checkIp` / `tokenExpiredNotify` / `tenantLogout*`）。
- **HTTP 下游**：17 个业务服务（见路由表），配置中心与注册中心为 Nacos。
- **Redis**：`RedisConfig`（Lettuce 单机 + Jackson2Json 序列化）已装配，但当前无实际使用点（限流器被注释）。
- **无 MQ、无定时任务**（本模块未见相关代码）。

## 域边界与风险

- **网关不含业务逻辑、不落库**：只做过滤、鉴权与转发；所有业务数据由下游服务写入。
- **凭证一致性风险**：`AuthServiceImpl.getJwt` 使用**硬编码密钥 `ByTtEr`**（`auth/service/impl/AuthServiceImpl.java:85`），声明的 `spring.security.oauth2.jwt.signingKey`（`:39-40`）未被使用——若 Nacos 改了签名密钥，网关解析 JWT 将失败。
- **CORS 未配置**：全模块无 `CorsWebFilter`/跨域配置，跨域需由下游服务或前端代理解决。
- **HTTPS 与证书**：`enabled: false`，但仓库内随包 `src/main/resources/server.keystore`（PKCS12，`key-alias bytter-server`）且口令为默认值 `bytter123`（`application-dev.yml:4-9`）——启用前必须更换并改为外部注入。
- **限流未生效**：`DefaultRedisRateLimiter` 的 `@Configuration` 与重写方法已被注释（`config/DefaultRedisRateLimiter.java:15-31`），网关当前无速率限制保护。
- **代码事实（待复核，非本轮修复范围）**：
  - `SqlInjectionFilter` 的 order 写作 `Ordered.HIGHEST_PRECEDENCE - 1`（`:180`），`int` 溢出后语义反转；
  - `IgnoreUrlsConfig` 无任何引用、且绑定键与 yml 写法不匹配（真正生效的是 `AuthServiceImpl` 的 `@Value` 方式）；
  - `XssAndSqlException` 与 `GatewayCodeEnum.XSS_SQL_CHECK_EXCEPTION` 定义后未被使用；
  - `AccessGatewayFilter` 未声明 order，仅依赖自动配置的注册顺序；
  - `XssFilter` 清洗后删除 `Content-Length` 并置 `Transfer-Encoding: chunked`，需确认下游对 chunked 的兼容性。

## 交付图（`tools/service-db-diagram/work/`）

| 图 | 主题 | 说明 |
|---|---|---|
| `gateway_business_flow.workflow.tables.html` | 网关请求处理主流程 | 7 节点 / 4 泳道；**0 表引用**（本服务无直连表），点击节点展示该环节的链路语义与依赖 |

> **无 ERD**：本服务 0 张表（无数据源），因此不产出 ERD。
> 渲染源见 `tools/service-db-diagram/sources/gateway/`：`gateway_ledger.json`（0 表证据）+ `gateway_domain_flows.spec.json`（语义规格）+
> `gateway_business_flow.workflow.json`（布局）+ `gateway_node_tables_map.json`（节点说明）。

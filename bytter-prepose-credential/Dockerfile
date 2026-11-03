# 基础镜像
FROM harbor.t2.bytter.com/bytter/dsl-base:v1

# 设置工作目录
WORKDIR /app

# 创建日志目录
RUN mkdir -p /app/logs /app/data/dump /app/data/gc

# 将指定的jar移动至工作目录
ADD bytter-credential-start/target/bytter-credential-start-2.0.0.RELEASE.jar app.jar

# 配置spring激活环境
ENV SPRING_PROFILE=dev

# 配置内存大小
ENV MEMERY=$MEMERY

# 配置java运行环境，已提供默认的配置
ENV JAVA_OPTS="-XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m -XX:-UseLargePages -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:HeapDumpPath=/app/data/dump/dump_bytter-credential-start_%t.hprof -Xloggc:/app/data/gc/bytter-credential-start_%t.log"

# APM监控
ENV APM_OPTS=$APM_OPTS

EXPOSE 10061

ENTRYPOINT java ${MEMERY} ${JAVA_OPTS} ${APM_OPTS} -Duser.timezone=Asia/Shanghai -Djava.security.egd=file:/dev/./urandom -jar app.jar --spring.profiles.active=${SPRING_PROFILE}
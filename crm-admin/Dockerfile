# 构建阶段
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# 复制父 POM
COPY pom.xml .

# 复制 common 模块
COPY crm-common/pom.xml crm-common/
COPY crm-common/src crm-common/src

# 复制 modules 父 POM 和所有子模块
COPY crm-modules/pom.xml crm-modules/
COPY crm-modules/module-system crm-modules/module-system
COPY crm-modules/module-customer crm-modules/module-customer
COPY crm-modules/module-inquiry crm-modules/module-inquiry
COPY crm-modules/module-quotation crm-modules/module-quotation
COPY crm-modules/module-order crm-modules/module-order
COPY crm-modules/module-product crm-modules/module-product
COPY crm-modules/module-contract crm-modules/module-contract
COPY crm-modules/module-shipping crm-modules/module-shipping
COPY crm-modules/module-finance crm-modules/module-finance
COPY crm-modules/module-analytics crm-modules/module-analytics
COPY crm-modules/module-message crm-modules/module-message

# 复制 admin 模块
COPY crm-admin/pom.xml crm-admin/

# 下载依赖
RUN mvn dependency:go-offline -B

# 复制 admin 源代码并编译
COPY crm-admin/src crm-admin/src

RUN mvn clean package -DskipTests -B

# 运行阶段
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 安装必要的工具
RUN apk add --no-cache tzdata curl && \
    cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone

# 从构建阶段复制jar包
COPY --from=builder /app/crm-admin/target/crm-admin.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs

# 暴露端口
EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# JVM参数优化
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/logs"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]

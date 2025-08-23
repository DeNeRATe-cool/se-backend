FROM maven:3.9.2-eclipse-temurin-17 AS builder

WORKDIR /app

# 复制 pom.xml 和 settings.xml
COPY pom.xml .
COPY settings.xml /root/.m2/settings.xml

# 预下载依赖
RUN mvn dependency:go-offline -B -s /root/.m2/settings.xml

# 拷贝源码并打包
COPY src ./src
RUN mvn clean package -DskipTests -s /root/.m2/settings.xml

# ======================
# 运行阶段
# ======================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/se-backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

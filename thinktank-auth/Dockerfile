#使用java环境
FROM java:8-alpine

# 切换到容器内部的 /thinktank目录
WORKDIR /thinktank

# 添加要运行的jar文件
COPY target/thinktank-auth-1.0-SNAPSHOT.jar /thinktank/auth.jar

# 端口暴露
EXPOSE 8160

# 容器启动后运行的命令
ENTRYPOINT ["java","-jar","/thinktank/auth.jar"]
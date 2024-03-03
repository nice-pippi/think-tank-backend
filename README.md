## 项目介绍

think-tank是一个类似于贴吧的交流平台，包括客户端和后台管理端，主要功能如下：

**用户端**

- 注册登录：用户可以通过注册、登录来使用平台。
- 推荐功能：根据用户的兴趣以及行为，推荐相关帖子。
- 发帖功能：用户可以在该功能中发布问题帖或经验交流帖等，支持增加标签和邀请用户回答操作。
- 评论功能：用户可以在该功能中回答问题或分享经验，并支持评论的回复和点赞。
- 搜索功能：用户可以通过输入关键词来搜索与关键词相关的帖子或板块。
- 私信功能：用户可以与其他用户之间进行私信交流。
- 个人中心：用户可以在该功能中查看自己个人信息、已发帖子、收藏帖子等记录，并支持个人资料的修改。
- 板块管理：拥有板主权限的用户可以修改自己所在的版块的信息，例如板块头像、板块名称、板块简介等；并支持对板块的帖子进行删帖。
- 举报提交：用户可以对违规的帖子或评论进行举报。

**后台管理端**

- 数据统计：管理员可以在该功能中查看平台的用户分布情况以及一周内登录人次统计。
- 申请审核：管理员可以在该功能中审核板块创建申请以及板主权限申请，包括审核通过或驳回，并向申请者发送通知。
- 举报审核：管理员可以在该功能中审核用户举报的帖子或评论，并支持对违规帖子或评论进行删除或禁言用户等处罚措施。
- 分类管理：管理员可以对板块进行分类管理，包括创建、编辑和删除板块分类。
- 板块管理：管理员可以对板块进行管理，包括创建、编辑和删除板块。
- 板主管理：管理员可以对各个板块的版主进行身份管理，包括大小板主身份互换和降级普通用户。
- 帖子管理：管理员可以对帖子并对其进行查看、管理帖子标签、删除等操作。
- 用户管理：管理员可以对平台上的用户进行信息编辑、禁言、限制登录、删除等操作。



## 技术选型

**后端技术**

|         技术         |                     说明                      |                     版本                      |
| :------------------: | :-------------------------------------------: | :-------------------------------------------: |
|     Spring Cloud     |                微服务解决方案                 |                  Hoxton.SR9                   |
| Spring Cloud Alibaba |         集成阿里巴巴中间件的解决方案          |                 2.2.6.RELEASE                 |
|     Spring boot      |                Web应用开发框架                |                 2.3.7.RELEASE                 |
|       Sa-Token       |              国产轻量级认证框架               |                    1.37.0                     |
|        Docker        |                 用于容器部署                  |                    24.0.5                     |
|        Mysql         |                   数据存储                    |                    8.0.31                     |
|        Redis         |                   数据缓存                    |                    5.0.14                     |
|    Elasticsearch     |                   搜索引擎                    |                    7.12.1                     |
|        Kibana        | 与Elasticsearch搭配使用的开源分析与可视化平台 |                    7.12.1                     |
|        minIO         |                   文件存储                    | RELEASE.2023-09-07T02-05-02Z.hotfix.2befe55d4 |
|      Rabbit MQ       |                   消息队列                    |             rabbitmq:3-management             |
|       XXL-JOB        |                   任务调度                    |                     2.4.0                     |
|        Nginx         |                静态资源服务器                 |                    1.24.0                     |

**前端技术**

|           技术            |          说明           |  版本  |
| :-----------------------: | :---------------------: | :----: |
|            Vue            |        前端框架         | 3.2.13 |
|       Element Plus        |        UI组件库         | 2.3.12 |
|          echarts          |    数据可视化图表库     | 5.4.3  |
| vue-admin-template-master |     后台端模板框架      | 4.0.0  |
|           Quill           |      富文本编辑器       | 1.2.0  |
|           Axios           | 基于promise的网络请求库 | 1.5.0  |



## 系统模块

```
think-tank
├── think-tank-auth                             // 认证模块	[8160]
├── think-tank-gateway                          // 网关模块	[60100]
├── think-tank-user                             // 用户模块	[60101]
├── think-tank-admin                            // 后台模块	[60102]
├── think-tank-validatecode                     // 验证模块	[60103]
├── think-tank-search                           // 搜索模块	[60104]
├── think-tank-block                            // 板块模块	[60105]
├── think-tank-post                             // 帖子模块	[60106]
├── think-tank-file                             // 文件模块	[60107]
├── think-tank-message                          // 消息模块	[60108]
├── think-tank-api                              // 将feign的client抽取为独立模块
├── think-tank-generator                        // mybatis-plus-generator代码生成器
└── think-tank-common                           // 工具类以及通用代码
```



## 系统架构图

![](https://github.com/nice-pippi/think-tank-backend/blob/a8ffd8bbb960b54c5078194ddfc0497cf2510c33/document/image/%E5%BE%AE%E6%9C%8D%E5%8A%A1%E6%9E%B6%E6%9E%84.jpg)

## 业务框架图

![业务框架图](https://github.com/nice-pippi/think-tank-backend/blob/3c574e9f2b2c67f5b27c5aac5a844610706463e5/document/image/%E4%B8%9A%E5%8A%A1%E6%A1%86%E6%9E%B6%E5%9B%BE.jpg)



## 环境搭建

### 1.镜像准备

其中所涉及到的ip地址均要修改为Linux服务器地址，可通过`ip address`命令查看。

**Mysql**

```bash
docker pull mysql:8.0.31
```

**Nacos**

```bash
docker pull nacos/nacos-server:1.4.2
```

控制台：http://192.168.88.150:8848/nacos

**Elasticsearch、kibana**

```bash
docker pull elasticsearch:7.12.1
docker pull kibana:7.12.1

docker network create elastic
```

控制台：http://192.168.88.150:5601/app/dev_tools#/console

**Redis**

```bash
docker pull redis:5.0.14
```

**minIO**

```bash
docker pull minio/minio:RELEASE.2023-09-07T02-05-02Z.hotfix.2befe55d4
```

控制台：http://192.168.88.150:9000

账号、密码统一：minioadmin

**Sentinel**

将提供的`/document/resource/sentinel-dashboard-1.8.1.jar`包上传到Linux上

编写Dockerfile,内容如下:

```dockerfile
FROM openjdk:8
COPY sentinel-dashboard-1.8.1.jar /sentinel.jar
EXPOSE 8080
CMD ["java", "-jar", "/sentinel.jar"]
```

build命令构建镜像

```bash
docker build -t sentinel-dashboard-1.8.1 .
```

控制台：http://192.168.88.150:8080/#/dashboard

**Rabbit MQ**

```bash
docker pull rabbitmq:3-management
```

控制台：http://192.168.88.150:15672/#/

**XXL-JOB**

```bash
docker pull xuxueli/xxl-job-admin:2.4.0
```

控制台：http://192.168.88.150:9090/browser

**Nginx**

```bash
docker pull nginx:1.24.0
```

### 2.docker-compose配置

```yaml
version: '3.0'  
  
services:  
  mysql:  
    image: mysql:8.0.31  
    container_name: mysql  
    command: --default-authentication-plugin=mysql_native_password  
    environment:  
      MYSQL_ROOT_PASSWORD: pp520  
    volumes:  
      - /docker/mysql/data:/var/lib/mysql  
      - /docker/mysql/config:/etc/mysql/my.cnf  
    ports:  
      - "3306:3306"  
  
  nacos:  
    image: nacos/nacos-server:1.4.2  
    container_name: nacos  
    environment:  
      MODE: standalone  
    ports:  
      - "8848:8848"  
  
  elasticsearch:    
    image: elasticsearch:7.12.1  
    container_name: elasticsearch  
    networks:    
      - elastic    
    environment:    
      - discovery.type=single-node
    volumes:  
      - /docker/elasticsearch/data:/usr/share/elasticsearch/data
      - /docker/elasticsearch/plugins:/usr/share/elasticsearch/plugins
    ports:    
      - "9200:9200"   
      - "9300:9300"   
  
  kibana:    
    image: kibana:7.12.1    
    container_name: kibana  
    networks:    
      - elastic
    volumes:  
      - /docker/kibana/data:/usr/share/kibana/data
    ports:    
      - "5601:5601"   
      
  redis:    
    image: redis:5.0.14   
    container_name: redis   
    volumes:  
      - /docker/redis/config/redis.conf:/usr/local/etc/redis/redis.conf
      - /docker/redis/data:/data    
      - /docker/redis/logs/data:/logs
    command: --requirepass pp520
    ports:    
      - "6379:6379"      
  
  minio:    
    image: minio/minio:RELEASE.2023-09-07T02-05-02Z.hotfix.2befe55d4    
    container_name: minio    
    environment:    
      - MINIO_ACCESS_KEY=minioadmin    
      - MINIO_SECRET_KEY=minioadmin    
    volumes:    
      - /docker/minio/data:/data    
      - /docker/minio/config:/root/.minio    
    command: server /data --console-address ":9090" -address ":9000"  
    ports:    
      - "9000:9000"    
      - "9090:9090"   
      
  sentinel:  
    image: sentinel-dashboard-1.8.1 
    container_name: sentinel  
    ports:  
      - "8080:8080"  
      
  rabbitmq:  
    image: rabbitmq:3-management  
    container_name: rabbitmq
    hostname: mq1  
    ports:  
      - "15672:15672"  
      - "5672:5672"  
    environment:  
      - RABBITMQ_DEFAULT_USER=pp  
      - RABBITMQ_DEFAULT_PASS=123456
      
  xxl-job-admin:  
    image: xuxueli/xxl-job-admin:2.4.0
    container_name: xxl-job
    environment:
      PARAMS: '
        --spring.datasource.url=jdbc:mysql://192.168.88.150:3306/xxl_job?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai
        --spring.datasource.username=root
        --spring.datasource.password=pp520'
    ports:  
      - "8081:8080"
    volumes:  
      - /docker/xxl-job/logs:/data/applogs  
      
  nginx:  
    image: nginx:1.24.0
    container_name: nginx
    ports:  
      - "80:80"
      - "8585:8585"
    volumes:  
      - /docker/nginx/nginx.conf:/etc/nginx/nginx.conf
      - /docker/nginx/html:/etc/nginx/html
      
networks:    
  elastic:   
    driver: bridge
```

**注意**：启动前做好准备工作

- 建立好相应的`/docker/***` 目录
- 将解压好的ik分词器上传到本地 `/docker/elasticsearch/plugins` 的目录（网上自行查找 elasticsearch-analysis-ik-7.12.1.zip）
- nginx配置文件：/docker/nginx/nginx.conf（网上自行查找nignx配置文件，直接复制进去）
- nginx网页文件：/docker/nginx/html （写个简单的html网页，文件名为index.html，放到html目录下）

启动命令

```sh
docker-compose up -d
```



### 3.可能出现的问题以及解决方案

**Mysql**

​	若启动后远程连接Mysql报“10061错误”，解决方法如下：

（1）进入命令行

```SH
docker exec -it mysql bash
```

（2）登录

```SH
mysql -uroot -ppp520
```

（3）输入以下命令

```SH
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'pp520';
FLUSH PRIVILEGES;
exit;
exit;
```

（4）重启Mysql镜像

```
docker restart mysql
```

**Nacos**

服务无法读取到配置，需要打开Nacos网页控制台创建dev命名空间，然后将提供的`nacos_config`压缩包导入即可，具体步骤网上自行了解。（配置包在`/document/resource/document/resource/nacos_config`路径下）

**Kibana**

如果启动后网页打开kibana控制台显示：`Kibana server is not ready yet`，需要执行如下命令

```sh
curl -X DELETE http://localhost:9200/.kibana*
```

**XXL-JOB**

无法登录xxl-job控制台时，需要到GitHub下载sql文件，并且在数据库里执行该sql文件。

https://github.com/xuxueli/xxl-job/blob/master/doc/db/tables_xxl_job.sql



### 4.项目部署

**修改hosts文件**

路径：`C:\Windows\System32\drivers\etc\hosts`下追加以下内容：

```
192.168.88.150 www.think-tank.cn
```

**编辑nginx配置文件**

```sh
vi /docker/nginx/nginx.conf
```

```yaml

#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    upstream front {
        server localhost:60100;
    }

    server {
        listen       80;
        server_name  www.think-tank.cn localhost;

        location / {
            root   D:/CodeTools/nginx-1.23.1/html/think-tank-front;
            index  index.html index.htm;
            try_files $uri $uri/ /index.html;
            add_header Cache-Control "no-cache, no-store, must-revalidate";
            add_header Pragma "no-cache";
            add_header Expires 0;
            proxy_connect_timeout 5s;
            proxy_read_timeout 600s;
            proxy_send_timeout 15s;
        }

        location /api {
            rewrite /api/(.*) /$1 break;
            proxy_pass   http://front;    
        }     

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }

    server {
        listen       8888;
        server_name  localhost;

        location / {
            root   D:/CodeTools/nginx-1.23.1/html/think-tank-backend;
            index  index.html index.htm;
            try_files $uri $uri/ /index.html;
            add_header Cache-Control "no-cache, no-store, must-revalidate";
            add_header Pragma "no-cache";
            add_header Expires 0;
        }

        location /api {
            rewrite /api/(.*) /$1 break;
            proxy_pass   http://localhost:60100;    
        }     

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```

**打包前端工程项目**

1. 使用：`npm run build` 打包前端工程
2. 将dist目录重命名为 `think-tank-front`
3. 上传目录到Linux服务器的 `/docker/nginx/html` 路径
4. 重启nginx：`docker restart nginx`

**打包后端工程**

1. 将有Dockerfile文件的工程制作成镜像，具体步骤网上自行查找（IDEA制作docker镜像）
2. docker-compose追加配置


```yaml
  auth:  
    image: auth:1.0.0
    container_name: auth
    ports:  
      - "8160:8160"
    depends_on:
      - nacos
    restart: always
   
  gateway:  
    image: gateway:1.0.0
    container_name: gateway
    ports:  
      - "60100:60100"
    depends_on:
      - nacos
    restart: always
    
  user:  
    image: user:1.0.0
    container_name: user
    ports:  
      - "60101:60101"
    depends_on:
      - nacos
    restart: always    

  admin:  
    image: admin:1.0.0
    container_name: admin
    ports:  
      - "60102:60102"
    depends_on:
      - nacos
    restart: always
    
  validatecode:  
    image: validatecode:1.0.0
    container_name: validatecode
    ports:  
      - "60103:60103"
    depends_on:
      - nacos
    restart: always
    
  search:  
    image: search:1.0.0
    container_name: search
    ports:  
      - "60104:60104"
    depends_on:
      - nacos
    restart: always

  block:  
    image: block:1.0.0
    container_name: block
    ports:  
      - "60105:60105"
    depends_on:
      - nacos
    restart: always
    
  post:  
    image: post:1.0.0
    container_name: post
    ports:  
      - "60106:60106"
    depends_on:
      - nacos
    restart: always
    
  file:  
    image: file:1.0.0
    container_name: file
    ports:  
      - "60107:60107"
    depends_on:
      - nacos
    restart: always
    
  message:  
    image: message:1.0.0
    container_name: message
    ports:  
      - "60108:60108"
    depends_on:
      - nacos
    restart: always   
```

### 5.项目运行

```bash
# 启动所有容器
docker-compose up -d
```

等待容器全部执行完，访问网址：

- 客户端：http://www.think-tank.cn/

- 后台端：http://192.168.88.150:8888/#/login?redirect=%2Fdashboard
`						`分布式故障注入

项目目标：在已上线的项目中，通过注入故障，测试系统在故障情况下的可靠性和稳定性，以验证升级过程是否合理，并优化运维流程。

项目要求：

 实现故障注入功能，能够在项目的每一个子节点上注入故障，例如限制带宽、模拟节点故障等，以验证系统在故障情况下的表现。
 实现网格化计算，对每一个节点的进出流量进行计算和管理，确保数据流的可控和可追踪。
 实现数据层面的网关，对数据流进行路由和管理，例如将不同类型的数据路由到不同的节点或负载均衡器上，将故障数据路由到故障节点上等。
 实现管理层面的控制台，能够下达路由关系和故障注入指令，对系统进行监控和管理。
实现自动化测试和监控，能够自动化进行故障注入和监控系统的表现，以及自动化报告测试结果。
实现可扩展性和可维护性，能够方便地添加新的节点和故障类型，并且能够方便地进行系统维护和升级。

项目技术栈：

分布式计算和通信技术， RPC、消息队列等。
数据库技术， MySQL、Redis、influxDB等。
监控和报警技术， Prometheus、Grafana等。
自动化测试和部署技术 Docker等。
容器编排技术 Docker 

以上是项目需求总结，以下是项目架构设计信息：

控制台模块：使用前端技术栈和后端技术栈实现，提供用户界面，通过控制台用户可以下达故障注入指令、查看系统状态和监控结果等。与其他模块进行交互采用RESTful API或GraphQL进行通信。

注入模块：使用分布式计算和通信技术（如RPC、消息队列）实现，负责在指定节点上注入故障。该模块接收来自控制台模块的指令，在项目的每个子节点上注入故障，例如限制带宽、模拟节点故障等。与控制台模块进行通信以接收指令。

网格化计算模块：用于对每个节点的进出流量进行计算和管理的模块。它可以跟踪和记录数据流的路径和指标，并提供数据流控制和策略管理功能。

数据网关模块：实现数据层面的路由和管理功能。该模块负责将不同类型的数据路由到不同的节点或负载均衡器上，并根据配置将故障数据路由到故障节点上。它可以用于实现数据流的分发、聚合和筛选等功能。

监控模块：使用监控和报警技术（如Prometheus、Grafana）实现对系统的监控和报警功能。它收集各个节点的监控数据，并进行分析和可视化展示，以便及时发现系统故障和异常。

**Distributed Fault Injection**

**项目目录结构**

- DistributedFaultInjection/ (根目录)
  - fault-injection-manager/ (故障注入管理系统)
    - src/
      - main/
        - java/com/distributedfaultinjection/manager/
          - controller/ (HTTP请求处理)
          - service/ (业务逻辑)
          - dto/ (数据传输对象)
          - model/ (实体模型)
          - repository/ (数据访问层)
          - config/ (配置类)
        - resources/
          - application.properties (Spring Boot配置文件)
      - test/
    - pom.xml (Maven配置文件)
  - data-layer-operations/ (C数据层面操作模块)
    - src/
      - network/ (网络操作)
      - fault/ (故障注入逻辑)
      - common/ (公共代码)
    - Makefile
  - gateway-agent/ (网关Agent)
    - src/
      - main/
        - java/com/distributedfaultinjection/gateway/
          - interceptor/ (流量拦截与路由)
          - config/ (配置类)
        - resources/
          - application.properties (Spring Boot配置文件)
      - test/
    - pom.xml (Maven配置文件)
  - log-system/ (日志系统)
    - src/
      - main/
        - java/com/distributedfaultinjection/logsystem/
          - controller/ (日志接收)
          - service/ (日志处理)
          - model/ (日志模型)
          - repository/ (日志存储)
        - resources/
          - application.properties (Spring Boot配置文件)
      - test/
    - pom.xml (Maven配置文件)
  - influxdb/ (InfluxDB数据存储)
    - Dockerfile
    - config/
    - docker-compose.yml
  - existing-management-system/ (已存在的被注入管理系统)
    - frontend/ (前端代码目录)
    - backend/ (后端代码目录)
      - src/
        - main/
          - java/com/distributedfaultinjection/existing/
            - controller/ (HTTP请求处理)
            - service/ (业务逻辑)
            - mapper/ (MyBatis映射器)
            - model/ (实体模型)
            - config/ (配置类)
          - resources/
            - application.properties (Spring Boot配置文件)
        - test/
      - pom.xml (Maven配置文件)
  - README.md (项目文档)
  - .gitignore (Git忽略文件)
  - scripts/ (自动化脚本)
  - configs/ (系统配置文件)
  - CI\_CD/ (CI/CD配置文件)

**说明**

- 每个子项目都是一个独立的模块，可以单独构建和部署。
- 故障注入管理系统负责下达故障注入指令和管理。
- C数据层面操作模块负责在数据层面进行故障注入。
- 网关Agent负责流量的拦截和路由。
- 日志系统负责记录故障注入过程中产生的日志。
- InfluxDB用于存储日志数据。
- 已存在的被注入管理系统是待测试的目标系统。


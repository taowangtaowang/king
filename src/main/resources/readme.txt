总体计划:
加缓存(原生ConcurrentMap<String, Future<User>>缓存 redis缓存)
消息中间件
es
k8s/docker
数据库分库分表 mycat
sql优化
spark统计（mr统计）
版本迭代：springmvc——>dubbo——>springboot——>springcloud



1  tkmybatis 登录 20190701 √
2  自定义异常 swagger quartz (https://blog.csdn.net/xinpz/article/details/82344048)
3  自定义原生内存缓存 利用原生ConcurrentMap<String, Future<User>>缓存
4  mamcache
5  redis缓存  利用redis实现单点登陆session控制                           <3缓存的区别>
6  中间件activemq  rabbitmq Kafka                                        <3队列的区别>
7  es 结合队列刷数据 至es
8  k8s docker
9  分库分表 mycat的应用  主从复制读写分离
10 sql优化等
11 spark日志统计，访问统计等（flink统计）

框架迭代
springmvc——boot——cloud——dubbo

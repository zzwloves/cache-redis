该框架是在spring-data-redis框架的基础之上进行的进一步封装，该框架封装了redis的一些基本操作，可以将该框架当做一个工具类，
进一步减少一些重复性的代码工作

提供三种使用方式：
 方式一：在configuration配置类上加上 io.vicp.wloves.cache.redis.annotation.EnableCacheRedis注解
 方式二：将 io.vicp.wloves.cache.redis包放入到项目的扫描路径中，确保该包下的spring注解能生效
 方式三：将 classpath下的applicationContext-cache-redis.xml 加载到你的项目中

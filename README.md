# FreeTalk
### A community where you can speak freely
## 1.1 Community Homepage
   > TIPS:Any feature can be broken into several requests. 
   * Showing 10 posts
   * Paging display posts
## 2.1 Login Module
### 2.1.1 Mail Delivery
* setting of the mail
    > Start the client SMTP service
* Spring Email
    > Import jar package
    >
    > Mailbox parameter configuration
    >
    > Send mail using **JavaMailSender**
* Template Engine
    > Send HTML mail using **Thymeleaf**
### 2.1.2 Registration Function

> Front desk registration logic If the password cannot be less than a few digits, the username cannot be empty, and so on. These are verified with the bootstrap4 form, as follows:
>
>   div class="**invalid-feedback**"
>
> https://blog.csdn.net/j84491135/article/details/80433016

### 2.1.3 Session Management 
    
### 2.1.4 Generate verification code
> import Package: Kaptcha
* Generate random characters
* Generate Pic

### 2.1.5 Login and Logout
### 2.1.6 Display Login Information

**Interceptor**

Solve problems with a very low degree of coupling
    
use it to accomplish:
At the beginning of the request is a query to the logged in user

> We do not store user information in the browser at first, which is more sensitive. We save a ticket

 We upload the avatar directly in the Controller layer, not to the Service layer.
 Because MultipartFile is an object belonging to the SpringMVC presentation layer. The business layer only processes the update path so that a service can do it.
 
 ### 2.1.7 Check login status
For example, when the user is not logged in, the localhost:8080/community/user/setting in the browser will also enter the response page. This is a loophole in the system, so you need to check the login status.

Custom annotations: 
Idea: Intercept it with an annotation, and don't intercept it without annotation.

Custom annotation

**Use annotations to identify if they need to be logged in to access**


静态页面static可以直接访问，不用写方法。

## 3 社区核心功能
### 前缀树
### 发布帖子
### 帖子详情

### 事务管理
• 什么是事务

 事务是由N步数据库操作序列组成的逻辑执行单元，这系列操作要么全执行，要么全放弃执行。
 
  • 事务的特性（ACID） 
  - 原子性（Atomicity）：事务是应用中不可再分的最小执行体。
  - 一致性（Consistency）：事务执行的结果，须使数据从一个一致性状态，变为另一个一致性状态。 
  - 隔离性（Isolation）：各个事务的执行互不干扰，任何事务的内部操作对其他的事务都是隔离的。
  - 持久性（Durability）：事务一旦提交，对数据所做的任何改变都要记录到永久存储器中。
 
 事务的隔离性
 
  • 常见的并发异常 
  
  - 更新的问题：第一类丢失更新、第二类丢失更新。 
  - 读的问题：脏读、不可重复读、幻读。 
  
  > 幻读是指查询多行数据导致的不一致
  
  • 常见的隔离级别 
  
  利用4种隔离级别去处理
  以下由低到高： 加锁会限制数据库的性能 
  - Read Uncommitted：读取未提交的数据。 这种级别在并发的情况下是基本没法保证安全的 虽然效率最高
  - Read Committed：读取已提交的数据。  互联网应用对数据的完整性要求不是很高 对性能要求却很高的时候
  - Repeatable Read：允许你可重复读。
  - Serializable：串行化        要加锁 性能最低 数据安全性最高
  
  幻读一般是能够被接受的 往往是在统计的时候会出现幻读 统计一下当前有多少 个帖子
  过一会再统计一下用户 这种可以通过业务去规避 比如说将统计放在后半夜 这个时候几乎没有在线用户了 很难会出现幻读的问题
  
  实现机制 
  
 • 悲观锁（数据库） ：认为有并发就一定会有问题
 
  - 共享锁（S锁）   事务A对某数据加了共享锁后，其他事务只能对该数据加共享锁，能读到这个数据 但是不能改，但不能加排他锁。 
  - 排他锁（X锁）   事务A对某数据加了排他锁后，其他事务对该数据   其他事务不能读 也不能改     既不能加共享锁，也不能加排他锁。 
  
 • 乐观锁（自定义） 
 
 当你计算完之后准备写的时候 再看一下这个数据是否已经有人改过了 改过了 就放弃我的这次修改    
 
 怎么去识别变没变：通过加版本号、时间戳等
 
  - 版本号、时间戳等 
    在更新数据前，检查版本号是否发生变化。若变化则取消本次更新，否则就更新数据（版本号+1）
    
    
 Spring事务管理
 
 • 声明式事务 
 
  - 通过XML配置，声明某方法的事务特征。
  - 通过注解，声明某方法的事务特征。
  
 • 编程式事务 
   
  - 通过 TransactionTemplate 管理事务，并通过它执行数据库的操作。
  
  一套API 能够管理所有数据库的事务
  
  一般用声明式事务方便 但如果你的业务逻辑比较复杂 而只是想管理中间小部分的事务 可以用第二种
  
  
  comment表中status=0表示数据有效
  
  ### 私信列表
  会话 消息
  
  from_id是系统id，给用户发的是通知，不是私信了
  
  ### 发送私信
  ### 设置已读
  
  ### 统一处理异常
  针对表现层
   @ControllerAdvice 
   - 用于修饰类，表示该类是Controller的全局配置类。
   - 在此类中，可以对Controller进行如下三种全局配置： 
        异常处理方案、绑定数据方案、绑定参数方案。
       • @ExceptionHandler - 用于修饰方法，该方法会在所有Controller出现异常后被调用，用于处理捕获到的异常。
       • @ModelAttribute - 用于修饰方法，该方法会在所有Controller方法执行前被调用，用于为Model对象绑定参数。
       • @DataBinder - 用于修饰方法，该方法会在所有Controller方法执行前被调用，用于绑定参数的转换器
       
       
### 统一记录日志

之前所说的是控制器发生异常的记录日志 这里不发生异常也要去记录日志

这是系统需求 不要再业务需求里面耦合

AOP面向切面编程  


## Redis 
高性能存储方案

### Spring整合Redis

### 点赞
数据存储至Redis中提高性能

### 我收到的赞

### 关注，取消关注

### 关注列表，粉丝列表

### 优化登录模块
目前是将验证码存入到了session中，分布式部署的时候就会出现session共享问题。
每个服务器都从redis中读取数据，避免session共享的问题。

原来登录凭证是存在了mysql中，而处理每次请求的时候，都需要去查询一下这个凭证(在拦截器里每次都查),
登录凭证访问的频率是非常的高的，不进行优化，每次都从这里面取，性能会有很大的影响。
用Redis去存储用户的凭证。

每次请求时，要根据凭证去查用户，也是每次都从mysql里查，用户信息同样可以考虑用Redis存储。

> loginTicket的表的数据可以不用了，但是User表数据不能作废，只不过是把这个User缓存到Redis里面，然后过一会
就自动失效，自动过期。这符合实际的业务，因为用户访问这个网站，不可能一直都访问着，过一会数据就给清除。

## Kafka    TB级异步消息系统
服务器自动给某人发送的通知 很多地方都要去发送这个通知

基于事件为主体的一种封装
封装事件对象
事件的生产者
事件的消费者

## ES
ES支持http访问

### 社区搜索功能
• 搜索服务 

    - 将帖子保存至Elasticsearch服务器。
    - 从Elasticsearch服务器删除帖子。 
    - 从Elasticsearch服务器搜索帖子。 

• 发布事件 - 发布帖子时，将帖子异步的提交到Elasticsearch服务器。 

    - 增加评论时，将帖子异步的提交到Elasticsearch服务器。 
    - 在消费组件中增加一个方法，消费帖子发布事件。 

• 显示结果 

    - 在控制器中处理搜索请求，在HTML上显示搜索结果
    
## 安全高效
提高系统的安全性
Spring Security

SpringMVC的核心是DispatcherServlet，所有的请求都要提交给它，他会把请求发给一个一个的控制器，
具体由某一个控制器Controller处理请求，中间可以有拦截器Interceptor拦截请求。

Filter 是 JavaEE的标准 filter是去拦截Servlet的

http://www.spring4all.com/

## Redis高级数据类型
*HyperLogLog*
采用一种基数算法，用于完成独立总数的统计。
占据空间小，无论统计多少个数据，只占12K的内存空间。
不精确的统计算法，标准误差为0.81%。

*Bitmap*
不是一种独立的数据结构，实际上就是字符串。
支持按位存储数据，可以将其看成是byte数组。
适合存储索大量的连续的数据的布尔值。

## 网站数据统计
UV(Unique Visitor)
独立访客，需通过用户IP排重统计数据。
每次访问都要进行统计。
HyperLogLog，性能好，且存储空间小。

DAU 统计登录的用户。
日活跃用户，需通过用户ID排重统计数据。
访问过一次，则认为其活跃。
BitMap，性能好，且可以精确统计结果。

## 任务执行和调度

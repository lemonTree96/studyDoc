###  一.  MySQL 数据库体系架构

&emsp;&emsp;数据库是是依照某种数据模型组织起来数据的集合。在数据库之上存在着数据库实例程序，该程序是用户与操作系统之间的数据管理软件。因此，`MySQL`其实是一种基于客户端-服务器的数据管理软件(`DBMS`)，用于管理存放数据的文件。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201024172734488.png#pic_center)
&emsp;&emsp; MySQL数据库体系架构共分为3层，<font color=red>应用层，MySQL服务层，存储引擎层</font>，其结构如下图所示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201105172152522.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)


&emsp;&emsp;① **应用层**：包括连接池组件，主要有3个功能：
&emsp;&emsp;  <font color=green>● 连接处理</font>：当一个客户端向MySQL Server发送连接请求后，MySQL server会从线程池中分配一个线程来和客户端连接。连接池保证了MySQL的多并发。
&emsp;&emsp;   <font color=green>● 用户鉴权</font>：MySQL server对连接的用户进行鉴权处理，包括用户名，客户端主机地址，用户密码。
&emsp;&emsp;   <font color=green>● 安全管理</font>：MySQL server会根据用户的权限来判断用户可执行哪些操作。

&emsp;&emsp;② **服务层**：包括系统管理，SQL接口，SQL解析器，查询优化器，缓存。
&emsp;&emsp;  <font color=green>● 系统管理</font>：数据库备份和恢复，数据库安全管理，数据库复制管理，数据库集群管理，数据库分区，分库，分表管理，数据库元数据管理。
&emsp;&emsp;  <font color=green>● SQL接口</font>：接收用户的SQL命令并进行处理。
&emsp;&emsp;  <font color=green>● SQL解析器</font>：解析查询语句，最终生成语法树。语法检查通过后，解析器会<font color=orange>查询缓存</font>，如果缓存中有对应的语句，就直接返回结果<font color=orange>不进行接下来的优化执行操作</font>。
&emsp;&emsp;  <font color=green>● SQL查询优化器</font>：对查询语句进行优化，包括选择合适的索引，数据的读取方式。
&emsp;&emsp;  <font color=green>● 缓存</font>：包括全局和引擎特定的缓存，提高查询的效率。如果查询缓存中有命中的查询结果，则查询语句就可以从缓存中取数据，无须再通过解析和执行。

&emsp;&emsp;② **存储引擎层**：包括插件式的表存储引擎与底层物理文件。
&emsp;&emsp;  <font color=green>● 存储引擎</font>：在MySQL中，其存储引擎包括`InnoDB`(面向在线事务处理的引擎)，`MylSAM`(面向在线计算分析的引擎)， `NDB`(集群存储引擎)，`CSV`，`Archive`等。
&emsp;&emsp;  <font color=green>● 物理文件：</font> 物理文件通常包括<font color=red>日志文件，数据文件，Replication文件，参数配置文件</font>等。
***
###  二.  MySQL 服务层
#### 2.1 MySQL 语言(SQL接口)

 💗 **2.1.1  SQL语言组成及执行顺序**
&emsp;&emsp;`MySQL`通过结构化查询语言(`SQL`)来对数据进行增查改删。每个`SQL`语句都是由一个或多个关键字组成的。SQL有两种使用方式：<font color=green>交互式和嵌入式(在java/C++中使用，通过JDBC，Python等连接数据库)</font>。SQL语言由6个部分组成：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201105154357458.png#pic_center)
&emsp;&emsp; MySQL的语句执行共分为11步，其先后顺序如下图所示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201108144809779.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)

 💗 **2.1.2  MySQL数据类型**
&emsp;&emsp; MySQL的数据类型分为<font color=green>数值类型，字符类型(串数据类型)，日期时间类型</font>。如下图所示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201105154943330.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)

#### 2.2 MySQL 解析与查询优化
&emsp;&emsp; MySQL的解析与优化属于编译器的范畴，与C/C++的编译器没有本质区别。<font color=red>MySQL的解析分为词法分析，语法分析，中间代码生成，代码优化，目标执行代码的生成。</font>
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201105170734970.png#pic_center)

#### 2.3 MySQL 缓存机制
&emsp;&emsp;MySQL缓存是指SQL语言文本及缓存结果，用`Key-Value`形式保存在MySQL Server中。如果运行相同SQL语言时(对比时是逐字节进行对比)，则直接从缓存中获取结果，不需要再进行SQL语言解析，优化。如果这个表被修改(任何数据改变或结构改变)了，则该缓存将不再有效，查询缓存值得相关条目将被清空。因此对于频繁更新的表，缓存并不合适，只有对于一些不变的数据且有大量相同sql查询的表，缓存会节省很大的性能。
   ![在这里插入图片描述](https://img-blog.csdnimg.cn/20201107114934658.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)

 💗 **2.3.1  缓存规则**
&emsp;&emsp; ① 当开启缓存时，MySQL Server会自动将查询语句和结果集返回到内存中。
&emsp;&emsp; ② 缓存的结果是通过`Session`共享的，一个Client查询的缓存结果，另一个Client也可以使用。
&emsp;&emsp; ③ `where`条件中包含任何一个不确定的函数将永远不会被`Cache`，如`current_date()`，`now()`等。
&emsp;&emsp; ④ `MySQL Query Cache` 在查询过程中，只有两个SQL完全一致才会命中`Cache`，只要字符大小写或注释有点不同，查询缓存就认为是不同的查询；
&emsp;&emsp; ⑤ 在表结构或数据发生改变时，查询缓存中的数据不再有效。

 💗 **2.3.2  缓存机制内存管理**
&emsp;&emsp; MySQL的缓存内存使用内存池技术，内存池的基本单位是`block`(最小长度为`query_cache_min_res_unit`)，一个查询结果集的`cache`通过链表将`block`连接起来。
&emsp;&emsp;当MySQL Server启动的时候，会初始化缓存需要的内存(一个完整的空闲块)。当查询结果需要缓存的时候，先从空闲块中申请一个数据块为参数`query_cache_min_res_unit`配置的空间，即使缓存数据很小，申请数据块也是这个大小。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201107152551859.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
 💗 **2.3.3  MySQL缓存相关配置参数及状态**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201107161143785.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
***
###  三.  MySQL 存储引擎层
&emsp;&emsp;MySQL 存储引擎负责数据的存储和提取，MySQL的存储引擎层是插件式的，支持`InnoDB`，`MyISAM`，`Memory`等多个存储引擎。不同的存储引擎，其数据存储结构，数据索引方式都不相同。存储引擎的差别为：![在这里插入图片描述](https://img-blog.csdnimg.cn/20201107210546245.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
***
###  四.  MySQL 日志文件(不含InnoDB日志)
&emsp;&emsp; 日志文件是MySQL数据库的重要组成部分，日志文件记录着MySQL的运行过程。了解MySQL的日志文件，能够更好的了解MySQL的运行过程。MySQL日志系统分为系统日志和存储引擎日志，其框架如下图所示：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201110222448970.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
<div align=center><font color=blua>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ 这是一条分割线  ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</font></div>

#### 4.1 错误日志 error log
&emsp;&emsp;错误日志(`error log`)是 MySQL 中最常用的一种日志，主要记录 MySQL 服务器启动和停止过程中的信息、服务器在运行过程中发生的故障和异常情况等。需要利用错误日志`error log`来定位问题。一般情况下，`error log`文件放在`/var/log/mysqld.log`中，在MySQL中通过命令`show variables like 'log_error\G;`来查看error log存放的位置，并通过Linux命令`find / -name ./Ubuntu.err`查找文件的路径。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201111094145859.png#pic_center)
<div align=center><font color=blua>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ 这是一条分割线  ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</font></div>

#### 4.2 二进制日志 bin log
&emsp;&emsp;二进制日志是一个二进制文件，<font color=green>主要用于记录修改数据或有可能引起数据变更的MySQL语句(不包括`select`，`show`等不修改数据的语句)，二进制文件记录了对MySQL执行更改的所有操作，并记录了语句发生时间，执行时长，操作数据等信息。</font>
&emsp;&emsp;在每次重启MySQL Server时，会生成一个新的二进制日志文件<font color=red>。对于事务表，二进制日志只在事务提交的时候一次性写入。</font>

 💗 **4.2.1  二进制日志的管理**
 &emsp;&emsp; ① 查看系统二进制日志以及master状态(当前要写入的二进制日志)：
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20201111104111217.png#pic_center)
 &emsp;&emsp; ② 切换二进制文件：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201111104616903.png#pic_center)
 &emsp;&emsp; ③ 删除二进制文件：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201111105154406.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
 &emsp;&emsp; ④ 查看二进制文件：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201111111140595.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp;&emsp;<font color=red> ⑤ 利用`binlog`文件恢复数据</font>
&emsp;&emsp; `binlog`记录着修改数据或有可能引起数据变更的MySQL语句，因此利用`binlog`能够对误删除或系统宕机而存储失败的数据进行恢复。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20201111134750987.png#pic_center)
 <div align=center><font color=blua>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ 这是一条分割线  ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</font></div>

#### 4.3 慢查询日志 slow query log
&emsp;&emsp; 当MySQL语句执行时间大于`long_query_time`时，认为该语句执行时间过长，可以通过慢查询日志的方式来定位语句。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201111190010992.png#pic_center)


***
###  五.  MySQL InnoDB存储引擎
&emsp;&emsp;`MySQL InnoDB`引擎是默认的引擎，`InnoDB`的体系架构由一系列后台线程,内存池和文件组成。InnoDB引擎的体系结构如图所示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201109103552953.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)

<div align=center><font color=blua>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ 这是一条分割线  ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</font></div>

#### 5.1 InnoDB 引擎数据存储逻辑结构(磁盘结构)
&emsp;&emsp; InnoDB的磁盘主要包括数据表空间(`tablespace`)，数据字典(`data dictionary`)，<font color=red>双写缓冲区(`doublewrite buffer`)</font>，InnoDB日志(`redo log，undo log`)。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201120095628975.png#pic_center)
##### 5.1.1  表空间
 &emsp;&emsp;`InnoDB`引擎的数据表存储结构如下图所示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/202011181434244.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp;&emsp;表空间用于存储表结构和数据。<font color=green>表空间又分为系统表空间、独立表空间、通用表空间、临时表空间、Undo表空间</font>。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201118152432238.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201118152830919.png#pic_center)

&emsp;&emsp; MySQL中的数据都存放在表空间中，一个表空间可以分为<font color=red>**段，区，页，行**</font>四个部分。![在这里插入图片描述](https://img-blog.csdnimg.cn/20201118153000730.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
💗 **5.1.1.1  段空间**
&emsp; &emsp; 段是表空间文件中的主要组织结构，是一个逻辑概念，用来管理物理文件，是构成索引，表，回滚段的基本元素，常见的段包括<font color=green>数据段，索引段，回滚段</font>。在InnoDB的存储引擎表是索引组织的，因此，<font color=red>数据即索引，索引即数据。数据段是B+树上的叶子节点，索引段是B+树上的非叶子节点</font>。

💗 **5.1.1.2  区(簇)空间**
&emsp; &emsp; 区是由64个连续的页组成的，每个页大小为16KB，每个区大小为1MB。页是段的基本元素，一个段由多个区组成，一个段管理的空间大小是无限的，可以一直扩展，但每次扩展的最小单位是区。

💗 **5.1.1.3  页空间与行记录**
&emsp; &emsp;<font color=red>页是`InnoDB`磁盘管理的最小单元，**`InnoDB`默认每个页的大小为`16KB`**。</font>在逻辑上(页面号都是从小到大连续的)及物理上都是连续的。在向表中插入数据时，如果一个页面已经被写完，系统会从当前簇中分配一个新的空闲页面处理使用，如果当前区中的64个页面都被分配完，系统会从当前页面所在段中分配一个新的区，然后再从这个区中分配一个新的页面来使用；页在`InnoDB`中常见的页类型有：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201118161051777.png#pic_center)
&emsp; &emsp; <font color=red>行记录用于储存储数据库中每一行对应的数据，行与行之间通过一个**单向链表**的形式来连接</font>。行记录通过包括两个格式：`Compact`格式和`Redundant`格式。

&emsp; &emsp;<font color=green>**1. 页空间与行记录的基本结构**</font>
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201205104930972.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70)

##### 5.1.2   数据字典
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201120113617416.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
💗 **5.1.2.1  数据字典的格式**
 &emsp;&emsp; `InnoDB`数据字典是由内部系统表组成的，<font color=green>用来存储元数据信息，如表的描述，表对应每一列的类型，表的索引，每个索引有哪几个字段等</font>。在`InnoDB`中，系统表实际上是看不到的。有4个最基本的系统表来存储表的元数据：<font color=green>表(`SYS_TABLES`)，列(`SYS_COLUMNS`)，索引(`SYS_INDEXES`)，索引列(`SYS_FIELDS`)等信息</font>，通过这4个基本系统表，就可以获取其他系统表以及用户定义的表中所有的元数据。
 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20201119145442588.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
  &emsp;&emsp; 基本系统表是数据字典的根本，因此在`InnoDB`中，使用固定的页面(`0号表空间-0号文件-7号页面`)来记录基本系统表的聚簇索引和二级索引对应的B+数位置。

💗 **5.1.2.2  数据字典的创建和加载**
&emsp;&emsp; 数据字典的加载过程如下图所示：
&emsp;&emsp;   ● 新建数据库时，初始化库，索引创建数据字典管理B+树信息，并将系统表根页面存储在固定位置(0号表空间-0号文件-7号页面)
&emsp;&emsp;   ● 当用户访问一个表时，系统首先会从表对象缓冲池中查找该`表share对象`，如果找到直接从其实例化表对象空间中拿出一个空间的实例化对象使用。如果没有可用的实例化对象，则需要重新实例化该表(实例化时从`share对象`获取表信息)。如果没有`share对象`，则需要从系统表中构造一个`share对象`。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124152801386.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)

##### 5.1.3  双写数据缓冲区(double write)
&emsp; &emsp;<font color=SlateBlue> <u>**Q1. 为什么要引入double write ？**</u></font>
&emsp; &emsp;<font color=red>主要是为了解决`InnoDB`与操作系统在页面大小不一致情况下，内存写入到磁盘时出现问题而导致**数据损坏问题**。</font>
&emsp; &emsp; 由于<font color=red>在`InnoDB`中，页的大小为`16KB`</font>，其数据校验也是针对`16KB`进行计算的，<font color=green>数据从内存写入到磁盘中是以页为单位</font>。但是<font color=red>操作系统中页的大小为`4KB`</font>，因此，每写一次`InnoDB page`到磁盘，操作系统需要写4个块。当在特殊情况时(操作系统崩溃，断电等)，会造成`InnoDB page`写入磁盘时出现`partial page write`(部分页写入)，这会导致`page`损坏，数据产生混乱。 <font color=red>而`redo log`只能恢复一个旧的，数据校验完整的数据页，而不能恢复一个损坏的数据页，因此这时无法通过`redo log`来恢复数据。</font>

💗 **5.1.3.1  double write 工作流程**![在这里插入图片描述](https://img-blog.csdnimg.cn/20201124224400188.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp; &emsp;<font color=SlateBlue> <u>**Q2. 为什么不把double write中数据写入数据文件中 ？**</u></font>
&emsp; &emsp; ① `double write`里面的数据是连续的，如果直接写到`data page`里面，而`data page`的页又是离散的，写入会很慢。
&emsp; &emsp; ② `double write`里面的数据没有办法被及时的覆盖掉，导致`double write`的压力很大；短时间内可能会出现`double write`溢出的情况。

💗 **5.1.3.2  double write 系统参数**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201125170922177.png#pic_center)
##### 5.1.4   InnoDB 日志
&emsp;&emsp; `InnoDB`日志见5.5.2节。
&emsp;&emsp;
<div align=center><font color=blua>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ 这是一条分割线  ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</font></div>

#### 5.2 InnoDB 引擎内存(缓冲池)
&emsp;&emsp;由于Innodb引擎是在硬盘存储的，因此，提出了**缓冲池**方法，<font color=green>通过内存来弥补磁盘速度较慢对性能的影响</font>。缓冲池的大小直接影响着数据库的整体性能。
&emsp;&emsp;对数据库中页的操作过程如下：
&emsp;&emsp;&emsp;<font color=green>① 读取页操作：将从磁盘读到的页放在缓冲池，这一过程称为**页的FIX**。</font>
&emsp;&emsp;&emsp;② 修改页操作：首先修改缓冲池中的页，然后`Flush`到磁盘上。<font color=green>缓冲池是通过 `LRU`(`Lastest Recent Used`，最近最少使用)算法进行管理，并在`LRU`中加入了`midpoint`位置。</font>
&emsp;&emsp;缓冲池中缓存的数据页类型有：**数据页**(data page)，**索引页**(index page)，**插入缓冲**(insert buffer)，**自适应哈希索引**(adaptive hash index)，**锁信息**(lock info)，**数据字典信息**(data dictionary)

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201127204140861.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)

##### 5.2.1  缓冲池管理 - LRU
&emsp;&emsp; 缓冲池是通过LRU法进行管理。传统的LRU策略是将最近查询或使用的页在 LRU List 的前端，而最少使用的页在 LRU List的尾端。当不能存放新读取到的页时，将首先释放 LRU List中尾端的页。<font color=red>在InnoDB引擎中，对 LRU算法进行了改进，在LRU List中加入了`midpoint`位置，新读取到的页并不直接放入LRU List的首部，而是放到LRU List的`midpoint`位置(`LRU List的5/8处`)，然后根据参数`innodb_old_blocks_time`将`midpoint`位置的页面放入LRU List前端。</font>
&emsp;&emsp; 在缓冲池的调度过程中，有三个`List`，分别为`LRU List`，`Free List`，`Flush List`。
&emsp;&emsp; ① **Free List**：MySQL启动时，buffer pool会被初始化，此时buffer pool中缓存页都是空的内存，没有任何数据保存在里边。初始化后的缓存页就放在Free list中。<font color=green>Free list是一个双向链表</font>，其中的每一个结点都是缓存页对应的描述信息，通过描述信息可以找到指定的缓存页。<font color=green>当InnoDB读取到数据时，从Free List获取空闲缓存页，将数据放入缓冲页中，并将新页面放入LRU List的`midpoint`位置，同时删除`Free List`中的页。</font>
&emsp;&emsp; ② **LRU List**：LRU List用于页的管理和调度。
&emsp;&emsp; <font color=green>InnoDB引擎中，对LRU算法进行了改进，在LRU List中加入了`midpoint`位置，新读取到的页并不直接放入LRU List的首部，而是放到LRU LIst的`midpoint`位置(LRU List <font color=red>**5/8**</font>处)。</font>通过参数`innodb_old_blocks_time`可以控制`mid`页面加入到LRU List前端的时间。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201129160645133.png#pic_center)
&emsp;&emsp; ③ **Flush List**：在LRU List中的页被修改后，会导致缓冲池中的页和磁盘上的页的数据产生了不一致，则该页称为**脏页**，`Flush List`用于存储脏页，并通过`Checkpoint`技术将页刷新回磁盘。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201129155948898.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
##### 5.2.2  缓冲池Change buffer (原先为insert buffer)
&emsp; &emsp;<font color=SlateBlue> <u>**Q1. 为什么要引入Change buffer ？**</u></font>
&emsp;&emsp; 在实际应用中，表的索引除了聚集索引以外，还有很多的非聚集索引。对于非聚集索引来说，B+树的叶子节点插入不再有序，这时就需要离散的访问非聚集索引页，使插入性能降低。
&emsp;&emsp;  为了解决这一问题，InnoDB采用`change buffer`技术。对于非聚集索引的Insert和Update操作，不是每次都直接插入到索引页中，而是先插入到内存中。如果该索引页在缓冲池中，则直接插入，否则先将其放入到插入缓冲区中，再以一定的频率和索引页合并，此时同一个索引页的多个插入合并到一个IO操作中，提高效率。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201201172756770.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp; &emsp;<font color=SlateBlue> <u>**Q2. Change buffer会带来哪些问题 ？**</u></font>
&emsp; &emsp; ① 如果应用程序执行大量的插入和更新操作，且涉及非唯一的聚集索引，一旦出现宕机，这时就有大量内存中的插入缓冲区数据没有合并至索引页中，导致实例恢复时间会很长。
&emsp; &emsp; ② 在写密集时，`Change buffer`会占用过多的缓冲池内存。
##### 5.2.3  缓冲池 checkpoint 机制
&emsp; &emsp; 由于InnoDB采用`Write Ahead Log`策略来防止宕机使数据丢失，因此在事务提交之前，先写`redo log`，再修改内存数据页。这样就在内存中产生了脏页。当数据库宕机时，数据库不需要重做所有的日志，只需要执行上次刷入点之后的日志，这个点就叫做`Checkpoint`。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201201213740174.png)
&emsp; &emsp;  在Checkpoint机制中，存在两个Checkpoint：`Sharp Checkpoint`，`Fuzzy Checkpoint`
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201201221955198.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70)
<div align=center><font color=blua>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ 这是一条分割线  ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</font></div>

#### 5.3 InnoDB 引擎后台线程
&emsp;&emsp;`InnoDB`引擎采用多线程模型，后台线程包括4种线程：
&emsp;&emsp; ① `Master thread`: 后台的核心线程，主要负责将缓冲池中的数据异步刷新到磁盘，保证数据的一致性，<font color=green>包括脏页的刷新、合并插入缓冲(合并insert buffer)、undo页的回收(undo page clean)</font>等。
&emsp;&emsp; `Master thread`其内部是由多个循环组成的，`Master thread`根据数据库的运行状态在各种循环之间进行切换。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201109150940643.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp;&emsp; ② `IO thread`：`IO thread`主要负责处理IO请求，<font color=green>`IO thread`包含一个插入缓冲线程(`insert buffer thread`)，一个日志线程(`log thread`)，4个读线程(`read thread`)，4个写线程(`write thread`)。</font>
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201109211328237.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp;&emsp; ③ `purge thread`：`purge thread`用来回收事务提交后其被分配的undo页，可以通过`innodb_purge_threads=1`配置多个`purge thread`线程。
&emsp;&emsp; ④ `page cleaner thread`：将脏数据写入到磁盘，脏数据写盘后相应的redo就可以覆盖，然后达到`redo`循环使用的目的。
<div align=center><font color=blua>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ 这是一条分割线  ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</font></div>

#### 5.4 InnoDB 引擎索引
&emsp;&emsp; <font color=green>MySQL数据库中，数据都是存储在磁盘文件当中，当需要对数据进行操作时将数据从磁盘读入内存中，因此，<font color=orange>MySQL的数据读取的基本单位是页(磁盘块)。</font></font>
&emsp;&emsp; 为了提高数据库的查询速度，需要对数据库建立索引，如果不使用索引，MySQL必须从第一条记录开始读完整个数据库。<font color=red>索引是一种特殊的文件(表空间中文件)，该文件内部数据结构采用的是**B+树**。</font>MySQL中不同存储引擎与索引的对应关系如下表所示：![在这里插入图片描述](https://img-blog.csdnimg.cn/20200723135827290.png#pic_center)
&emsp;&emsp;<font color=red>`InnoDB`的数据存储是基于索引组织的，因此对于InnoDB存储引擎，数据即索引，索引即数据。</font>在`InnoDB`存储引擎中常见的索引包括：<font color=red>B+树索引，全文索引，自适应哈希索引。</font>InnoDB默认索引为B+树索引。
##### 5.4.1 B+树索引(B+ Tree)
&emsp;&emsp;B+树索引是以<font color=green>B+树</font>数据结构为基础，B+树索引不能找到一个给定键值的具体行，其能检索到的只是被查找数据行所在的页，然后数据库通过将页读入到内存，再在内存中进行查找。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201203145715164.png#pic_center)
💗 **5.4.1.1 B+树索引的"进化"过程**
&emsp;&emsp; 在B+树进化过程中，树每增加一个深度，查找到数据就会增加一次IO，因此，需要降低树的深度，同时尽可能增加每个结点(页面，16KB)能够存放的数据。![在这里插入图片描述](https://img-blog.csdnimg.cn/20201203143844845.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp;&emsp;  如：要在表中查询`id=7`的数据:
&emsp;&emsp;   ① 首先通过B+树索引进行查询，id=7在索引页2中
&emsp;&emsp;   ②  在索引页2中通过二分查找找到id=7的数据行

##### 5.4.2 B+树索引的分类
&emsp;&emsp;InnoDB存储引擎索引是一棵B+树，<font color=green>在InnoDB中每增加一个索引，就是增加一棵B+树</font>。在B+树中，完整的数据记录存储在B+树的叶子结点中，其余层次的结点都是内节点，内节点里存储的是目录项记录(Key值)。
&emsp;&emsp;InnoDB的索引分为两大类：
&emsp;&emsp; <font color=green>**1. 聚簇索引(主键索引)：** </font>聚簇索引根据<font color=red>**主键**</font>生成(按照每张表的主键构造一个B+树)，该索引的叶子结点处记录着**行记录**(即所有数据)。对于任意一个数据表，一定存在一个聚簇索引。![在这里插入图片描述](https://img-blog.csdnimg.cn/20201205112316222.png#pic_center)
&emsp;&emsp;聚集索引的特点如下:
&emsp; &emsp;① <font color=red>聚集索引表按照主键ID排序，检索的是**每一行数据的真实内容**。</font>
&emsp; &emsp;② 实际的数据页只能按照一棵B+树进行排序，因此<font color=red>每张表有且只有一个聚集索引。</font>
 &emsp; &emsp;③ 聚集索引对于主键的<font color=red>排序查找</font><font color=green>(因为其叶子结点通过双向链表连接)</font>和<font color=red>范围查找</font><font color=green>(通过叶子结点的上层中间结点可以得到叶子结点的范围)</font>速度非常快。
  &emsp; &emsp;④ 聚集索引的存储不是物理上连续，而是逻辑上连续的。

&emsp;&emsp; <font color=green>**2.  二级索引(辅助索引)**</font>：除了主键索引之外的索引称为辅助索，<font color=green>辅助索引的节点是以某一字段的数据</font>。辅助索引可以有多个，<font color=orange>辅助索引`B+`树的叶子节点不存储表中的数据，而是存储该列对应的主键，当查找数据时，需要根据主键再去聚集索引中进行查找</font>。根据聚集索引查找数据的过程叫做回表。非聚集索引不影响聚集索引的组织，因此每张表上可以有多个非聚集索引。
```sql
create table Test(
	`id` INT UNSIGNED AUTO_INCREMENT,
	`name` VARCHAR(255),
	`str` VARCHAR(15),
	PRIMARY KEY(`id`),  #id为主键，是聚集索引，表中的每行数据都是按照聚集索引id排序存储的，检索的是每一行数据的真实内容
	KEY(`name`)    #name是非聚集索引，name索引表节点按照name排序，检索的是每一行数据的主键
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201205144324675.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201205151532327.png#pic_center)
&emsp; &emsp;<font color=SlateBlue><u>**Q1. 聚集索引与辅助索引在查询数据时有什么区别吗 ？**</u></font>
&emsp; &emsp; ①  聚集索引在查询数据时更快。主键索引树的叶子节点直接就是要查询的整行数据。而非主键索引的叶子节点是主键的值，查到主键的值以后，还需要再通过**回表**，再进行多次查询。
&emsp; &emsp; ② 辅助索引叶子结点使用主键作为指针而不是使用地址值作为指针，减少了出现行移动或数据页分裂时辅助索引的维护工作。

&emsp; &emsp;<font color=SlateBlue><u>**Q2. 索引创建的基本原则 ？**</u></font>
&emsp; &emsp; <font color=red>① 最左前缀匹配原则</font>：MySQL会一直向右匹配直到遇到范围查询`(>、<、between、like)`就停止匹配。
&emsp; &emsp; <font color=red>② =和in可以乱序</font>：MySQL的查询优化器会优化成索引可以识别的形式。
&emsp; &emsp; <font color=red>③ 尽量选择区分度高的列作为索引，区分度的公式是`count(distinct col)/count(*)`，表示字段不重复的比例，比例越大我们扫描的记录数越少，唯一键的区分度是1。</font>
&emsp; &emsp; <font color=red>④ 索引列不能参与计算，保持列“干净”。</font>
&emsp; &emsp; <font color=red>⑤ 尽量扩展索引，而不是新建索引</font>：比如表中已经有a的索引，现在要加(a,b)的索引，那么只需要修改原来的索引即可。
##### 5.4.3 B+树索引的优化
&emsp; &emsp;在辅助索引中，每次查询都需要进行回表，从而影响效率，为了提高查询效率，需要对索引进行优化。
💗 **5.4.3.1 覆盖索引**
&emsp; &emsp; <font color=red>当一个索引包含所有需要查询的字段的值时，称为覆盖索引</font>。覆盖索引是指从辅助索引中直接查询到数据，而不需要进行回表从聚簇索引中查询到行数据。对于高频的请求上建立覆盖索引，<font color=red>不需要再进行回表操作查询整行记录，减少语句的执行时间。</font>
```sql
create table user (
	id int primary key,  #主键
	name varchar(20),
	sex varchar(5),
	index(name)    #普通索引
)engine=innodb;

select id,name from user where name='test'; 
# 1.命中name索引，索引叶子节点存储了主键id,通过name的索引树即可获取id和name,无需回表，
#   符合覆盖索引，直接返回查询所需要的数据。

select id,name,sex from user where name='test';*
# 2.命中name索引，索引叶子节点存储了主键id,通过name的索引树即可获取id和name,
#   但sex字段必须回表查询才能获取到，不符合覆盖索引，需要通过id聚集索引获取sex字段。
```
💗 **5.4.3.2 联合索引(复合索引) - 用于多条件查询where**
&emsp;&emsp;  无论是聚簇索引，还是覆盖索引，都是简单的单列索引。<font color=orange>当遇到多条件查询时，不可避免使用到多列索引，此时就需要建立联合索引。</font><font color=green>联合索引的数据结构仍然使用的是B+树，但与单列索引的B+树节点不同的是，联合索引的每个B+树节点包含**多个键值**。</font>
&emsp;&emsp; 联合索引遵循<font color=red>**最左匹配原则**</font>，具体是指，<font color=green>在联合索引匹配中，是根据建立联合索引的顺序，依次从最左边的索引开始匹配，直到索引失效为止【范围索引会导致索引失效(>、<、between、like)】。</font>如对(a,,b,c)建立索引，如果字段a匹配成功，才会继续匹配字段b...。联合索引的数据结构及匹配原理如下所示(两列联合索引)：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201211162901287.png#pic_center)
&emsp;&emsp;<font color=SlateBlue><u>**Q1. 对于多条件查询时应该如何建立索引 ？**</u></font>
&emsp;&emsp;① `select * from table  where a=1 and b=2 and c=3`；
&emsp;&emsp;&emsp;  (a,b,c)或者(c,b,a)或者(b,a,c)都可以，但是需要将区分度高的字段放在前面，区分度低的字段放在后边。
&emsp;&emsp;② `select * from table where a>1 and  b=2;`
&emsp;&emsp;&emsp; 对(b,a)建立索引，则两个字段都能用上索引，优化器会调整where后a,b的顺序。如果对(a,b)建立索引，由于a字段是范围查询会导致索引失效，b字段无法使用索引。
&emsp;&emsp;③  `select* from table where a>1 and b=2 and c>3;`
&emsp;&emsp;&emsp; 对(b,a)或(b,c)建立索引。
&emsp;&emsp;④ `select * from table where a=1 and b=2 and c>3;`
&emsp;&emsp;&emsp;  对(a,b,c)或(b,a,c)建立索引，对a,b字段需要将区分度高的字段放在前面，区分度低的字段放在后边。
&emsp;&emsp;⑤ `select * from table where a=1 and order by b;`
&emsp;&emsp;&emsp;  对a建立索引，则b字段会变成相对有序排列，避免了b字段再次排序。
&emsp;&emsp; ⑥ `select * from table where a in(1,2,3) and  b=1 and c>3 order by c;`
&emsp;&emsp;&emsp;  对(a,b)或(b,a)建立索引，将区分度高的字段放在前面。字段c在a,b建立索引后相对有序，因此无需索引。

##### 5.4.4 InnoDB 自适应哈希索引
💗 **5.4.4.1 哈希索引**
&emsp;&emsp; 在MySQL中，只有Memory引擎显示支持哈希索引。
&emsp;&emsp; 哈希索引是基于哈希表实现的，<font color=red>只有在匹配所有索引列的查询时才有效</font>。<font color=green>对于每一行的数据，存储引擎会对所有的索引列计算出一个哈希码</font>，哈希码的值很小，不同键值的行计算出的哈希码不一样。<font color=green>哈希索引将所有的哈希码存储在索引中，同时保存指向每个数据行的指针。</font>
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201220224413820.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp;&emsp;<font color=SlateBlue><u>**Q1. 哈希索引的缺点 ？**</u></font>
&emsp; &emsp; ① 哈希索引只包含哈希码和行指针，不存储字段值，因此不能使用索引中的值来避免读取行。但是由于哈希索引存储在内存中，因此读取行数据对性能的影响不大。
&emsp; &emsp; ② 哈希索引中的数据不是按照索引值顺序存储的，无法用于排序的。
&emsp; &emsp; ③ 哈希索引不支持部分索引列的匹配查找，因为哈希码是根据所有索引列来计算的。如对(a,b)建立哈希索引，如果只查询数据列a，则无法使用哈希索引。
&emsp; &emsp;  ④ 哈希索引只支持等值比较查询(`=，IN()，<=>`)，不支持范围查询，如where count>100。
&emsp; &emsp;  ⑤ 如果哈希索引冲突很多时，则索引维护的代价会很高。

💗 **5.4.4.2 InnoDB 中的哈希索引**
&emsp;&emsp; InnoDB中的哈希索引是**自适应哈希索引**，是InnoDB引擎提供的一个自动的，内部的行为，用户无法进行控制。<font color=green>当某些索引值使用很频繁时，InnoDB会在内存中基于B+树索引之上创建一个哈希索引。</font><font color=red>自适应哈希索引可以降低对辅助索引的频繁访问，提高查询效率，但自适应哈希索引会占用`innoDB buffer pool`。</font>
<div align=center><font color=blua>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ 这是一条分割线  ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</font></div>

#### 5.5 InnoDB 事务
&emsp;&emsp; 事务是数据库的重要特性之一。事务会把数据库从一种一致状态转换为另一种一致状态。<font color=green>事务在提交后，要么确保所有修改已经保存，要么所有修改都不保存，保持原来状态。</font>
&emsp;&emsp;InnoDB中的事务完全符合 <font color=red>**ACID特性**</font>:
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201230100133902.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70)

##### 5.5.1 事务的分类
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201229225248210.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
##### 5.5.2 事务的特性实现 - InnoDB日志
&emsp;&emsp;InnoDB日志主要包括`redo log`和`undo log`两种日志。
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020123010175860.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70)
&emsp;&emsp;
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201113100513942.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
💗 **5.5.2.1  undo log**
&emsp; &emsp; `undo log`主要有两个作用，提供回滚和多版本控制(`MVCC`)，**保证事务的原子性**。
&emsp; &emsp; `undo log`在事务回滚之后，只是将数据库逻辑的恢复到原来的样子，但是对于数据库的数据结构和存储页本身无法恢复到原来的样子。(如用户执行了Insert 100W条记录的事务，会导致分配一个新的段空间，在用户执行`Rollback`后，会将插入事务进行回滚，但表空间并不会变小)。因此，在InnoDB进行事务回滚时，实际上做的是与之前相反的工作，如insert => delete，delete => insert，update => 相反的update。
&emsp; &emsp; `undo log` 分为两种类型`update undo log`和`insert undo log`。`insert undo log`是指在insert操作中产生的`undo log`，`update undo log`是指在update操作中产生的`undo log`。

&emsp;&emsp; <font color=green>**1. undo log的文件结构** </font>
&emsp; &emsp;  在MySQL5.6以前，`undo log`是存储在`ibdata`中，在MySQL5.6以后，`undo log`可以独立成单独的文件，在单独文件中，`innoDB`采用回滚段(`rollback segment`)的方式进行管理，其文件结构如下图所示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201115213302202.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp;&emsp; <font color=green>**2.  undo log的工作原理** </font>
&emsp;&emsp; `undo log`会在事务开始之前就产生，首先对旧数据进行备份，并生成一个`update`语句(如果事务是`insert`，则会生成`delete`语句)，写入`undo log`，此时得到一个回滚指针，同时更新这个数据行的回滚指针和事务ID。事务提交后，`undo log`并不能立马被删除，而是放入待清理的链表，由`purge`线程判断是否有其他事务在使用`undo`段中表的上一个事务之前的版本信息，决定是否可以清理`undo log`的日志空间。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210101222846728.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
 &emsp;&emsp;

💗 **5.5.2.2  redo log (物理文件名称为ib_logfileX) - write ahead log策略**
&emsp;&emsp;  `redo log`是对事务执行过程中修改的数据进行备份，**保证事务的持久性**。
&emsp;&emsp; 为了解决缓冲池中的数据刷新到磁盘时，由于宕机造成缓冲池中的数据丢失问题，引入了`redo log`，即<font color=red>**write ahead log**</font>。当向事务commit时，<font color=red>必须先写`redo log`</font>，让`redo log`根据某种方式保存到磁盘中，变成`redo log file`，直到事务完成。当发生宕机时，读取磁盘的`redo log file`可以进行数据恢复。因此`redo log`<font color=red>保证了MySQL事务的**持久性**。</font>																																				
&emsp;&emsp;由于事务运行过程中，会不停的产生`redo log`，但若每次产生的`redo log`都通过`fsync`刷新到磁盘则会大大影响系统性能，因此，为了提高系统运行效率，通过`group commit`对`redo log`进行合并，然后再通过`fsync`刷新到磁盘当中。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210102221959554.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70)

&emsp; &emsp;<font color=SlateBlue> <u>**Q1. redo log与数据页，索引页的区别 ？**</u></font>
&emsp; &emsp; ① <font color=orange>数据页与索引页属于缓冲池的一部分</font>，用于存放事务过程中修改后数据与索引，而<font color=orange>`redo log`虽然也在内存中，但不是缓冲池的一部分</font>，其作用用于故障恢复，保证事务的持久性。
&emsp; &emsp; ② <font color=orange> `redo log`能够保证每次写入时同步刷新到磁盘中(`fsync`调用)</font>，而数据页与索引页由于其数据量较大，每次更改后刷新到磁盘其开销是很大的，因此 <font color=orange>数据页与索引页是通过`checkpoint`机制来实现刷新磁盘</font>。

&emsp; &emsp;<font color=SlateBlue> <u>**Q2. redo log与 二进制日志的区别 ？**</u></font>
&emsp; &emsp; <font color=green>① 二进制日志是由MySQL系统产生，对数据库进行的所有修改都会产生二进制日志</font>。而<font color=orange>`redo log`是`InnoDB`层产生的，只记录该存储引擎中表的修改，且**二进制日志先于`redo log`被记录**。</font>
&emsp; &emsp; <font color=green>② 二进制日志记录的是操作方法的**逻辑性语句**，是基于行格式的记录方式</font>。而<font color=orange>`redo log`是**物理格式上的日志**，记录的是每个页的修改。</font>
&emsp; &emsp; <font color=green>③ 二进制日志在事务提交时**一次性写入**(非事务表操作在每次语句执行成功后写入)，因此二进制日志中的记录方式和事务提交顺序有关，一次事务提交对应一次记录。</font>而<font color=orange>`redo log`在数据修改之前**写入`redo log buffer`**，然后对`redo log buffer`中的数据进行修改，通过`fsync()`刷新到磁盘。</font>

##### 5.5.3 事务的处理过程
&emsp; &emsp;事务处理过程如下：
&emsp; &emsp; ① 事务修改数据前，写入undo log，保证事务的原子性。
&emsp; &emsp; ② 更新完数据数据之后，写入redo log，保证事务的持久性。随后写入binlog，用于数据备份。
&emsp; &emsp; ③ 事务提交后，清理undo log信息(通过purge线程)，释放锁资源，刷新redo log盘，清理保存点列表，刷新binlog盘。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210102210107641.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70)
##### 5.5.4 事务的隔离性及存在的问题(多事务并发)
&emsp; &emsp; 对于MySQL的操作，如果只进行单个事务的提交，则不会存在事务的隔离性问题。但是当多个事务对数据库进行控制和修改时就会出现以下问题：
&emsp; &emsp; **① 脏读**：指事务读取到了其他事务未提交的数据，而未提交的数据可能出现回滚，最终不会存到数据库中。此时会导致事务读取到了并不存在的数据。
&emsp; &emsp; **② 不可重复读(虚读)**【通常针对数据update操作】：指在同一事务内，在不同时刻读到的同一批数据时不一样的，可能会受到其他事务的影响。
&emsp; &emsp; **③ 幻读**【通常针对数据insert操作】：指事务A对某些行的内容进行更改，但还未提交，此时 <font color=green>事务B插入了与事务A更改前的相同的记录行，并在事务A提交前先提交了</font>。这时，在事务A中查询，会发现仿佛更改操作未起作用(因为事务B插入了更改前的相同的记录行)。

&emsp; &emsp; 为了解决上面的问题，提出了事务的隔离，但事务的隔离是通过<font color=red>**锁机制**</font>来实现了，隔离级别越高，数据越安全，但系统性能越低。<font color=red>事务的隔离分为4个等级：未提交读，已提交读，可重复读，可串行化。</font>![在这里插入图片描述](https://img-blog.csdnimg.cn/20210107111148623.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp;&emsp; <font color=green>**1.未提交读** </font>
&emsp;&emsp; 未提交读，就是当前事务A可以读到其他事务B未提交的数据，但由于事务B未提交，无法保证事务A读到的数据最终与事务B提交后的数据一致，如果事务B发生回滚，则会出现脏数据问题。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210107223855785.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp;&emsp; <font color=green>**2.已提交读** </font>
&emsp;&emsp;  已提交读，就是当前事务A只能读到其他事务B已经提交过的数据，如果事务B发生rollback，则事务A读取的数据不会发生改变。已提交读是大多数数据库的默认事务隔离级别。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210107223627631.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
&emsp;&emsp; <font color=green>**3.可重复读** </font>
&emsp;&emsp; 可重复读，是指同一事务在不同时刻读取的数据是相同的，事务不会读取到其他事务对已有数据的修改，即使其他事务已提交。


<div align=center><font color=blua>▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬ 这是一条分割线  ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬</font></div>



#### 5.6 InnoDB 并发控制
#### 5.6 InnoDB 引擎锁机制
&emsp;&emsp; 为了保证数据库的多个事务之间能够并发的访问，同时确保每个用户能以一致的方式读取和修改数据，就需要锁机制。锁机制用于管理对共享资源的并发访问。在MySQL中，锁机制大体上分为两类：**`lock`**，**`latch`**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201227225309786.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
##### 5.5.1 InnoDB 锁类型 - 行级锁与意向锁
&emsp;&emsp;  InnoDB存储引擎支持**多粒度锁**，即<font color=green>**行锁**</font>和<font color=green>**表锁**</font>同时存在，其中表锁是利用行锁把表中的所有行都锁住而实现的。锁的类型如下：![在这里插入图片描述](https://img-blog.csdnimg.cn/20201228142904961.png)

💗 **5.5.1.1 行级锁 - 共享锁/排他锁**
&emsp;&emsp;对于行级锁，分为共享锁(`S Lock`)与排他锁(`X Lock`)。
&emsp;&emsp; ① 共享锁：一个事务读取一个数据行时，其他的事务也可以读，但不能对数据行增删改。
&emsp;&emsp; ② 排他锁：一个事务获取一个数据行的写锁时，其他事务不能再获取该行的任何锁，写锁的优先级最高。![在这里插入图片描述](https://img-blog.csdnimg.cn/20201228144902135.png#pic_center)
💗 **5.5.1.2 意向锁 - 表级锁**
&emsp;&emsp; 意向锁是<font color=red>**不与行级锁冲突的表级锁**，意向锁由InnoDB引擎自主维护，用户无法手动操作意向锁。</font>意向锁在保证事务并发性的前提下，实现了行锁与表锁共存，同时满足事务隔离性的要求。

&emsp;&emsp;<font color=SlateBlue><u>**Q1. 为什么需要意向锁 ？**</u></font>
&emsp;&emsp; 如果一个事务试图在该表级别上应用`S Lock`或`X Lock`，则受到前一个事务控制表级别意向锁的阻塞，第二个事务在锁定该表前不需要检查各个页或行锁，只需检查表的意向锁即可，从而提高性能。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20201228154935871.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201228160938376.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
💗 **5.5.1.3 事务与锁的状态监控**
![在这里插入图片描述](https://img-blog.csdnimg.cn/20201228173856512.png#pic_center)


##### 5.5.2 InnoDB 锁算法


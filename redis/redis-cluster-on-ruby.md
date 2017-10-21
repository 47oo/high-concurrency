# redis on ruby

## ruby 环境搭建
  1. redis的集群是交给ruby-trib.rb来管理的.也就是说需要ruby的运行环境。[download ruby](http://www.ruby-lang.org/en/downloads/).
  2. 我使用的ruby版本是2.4.2，下载是的源码包，需要自己编译
  3. 开始编译
  ``` shell
  # 进入ruby目录
  ./configure --prefix=/path/to/finename
  make
  make install
  ```
  4. 现在需要使用gem来下载redis的相关api
  ``` shell
  ./gem install redis # 开始我以为能成功，事实上，我想错了
  ```
  ![ruby-zlib](../images/ruby-zlib.jpg)
  > 如果出现以上问题，则使用

  ``` shell
  yum install zlib zlib-devel -y # 下载相关依赖及开发工具包
  cd $RUBY_SRC_HOME/ext/zlib
  ruby extconf.rb # 表示很无力吐槽，我艹。。。
  make&&make install
  ```
  > 但是你可能遇到一下问题

  ![ruby-zlib-check](../images/ruby-zlib-check.jpg)

  ```shell
  # 修改前面的这条命令即可
  ruby extconf.rb  --with-zlib-include=/usr/local/zlib/include/ --with-zlib-lib=/usr/local/zlib/lib
  ```

  > 然而你make的时候可能会遇到下面的问题= =

  ```shell
  make: *** No rule to make target `/include/ruby.h', needed by `zlib.o'.  Stop
  ```
  > 修改Makefile文件

  ```shell
  vim Makefile
  # 在开始部分添加一下内容
  top_srcdir=path/to/ruby/src
  # 然后执行
  make&&make install
  ```
  > 嗯，你以为你现在就能成功了吗？错了，哈哈

  ```shell
  # 当你再次下载redis的时候
  gem install redis
  ```
  > 你可能会出现一下错误

  ![ruby-opnessl](../images/ruby-openssl.jpg)

  ```shell
  yum install openssl openssl-devel -y
  cd $RUBY_SRC_HOME/ext/openssl
  ruby extconf.rb  --with-openssl-include=/usr/local/openssl/include/ --with-openssl-lib=/usr/local/openssl/lib
  make && make install
  ```
  > make 还是会出现类似的错误

  ![ruby-openssl-make](../images/ruby-openssl-make.jpg)

  > 修改策略同zlib

  ```shell
  gem install redis --version 4.0.0 # 现在就好了，想哭
  ```

##  redis 集群搭建

  1. 首先可以使用单台服务器做为集群，多台服务器可以集群
  2. 管理集群的节点必须要有ruby环境和redis相应的api
  3. 复制可执行文件redis的（就是编译好的文件）到其他服务器上，总之，是至少需要6个redis服务的，并且是分为3组主从结构
  4. 修改redis.conf配置文件
  ```shell
  port 7000
  bind 192.168.0.108 # 当前主机对外的ip地址
  cluster-enabled yes
  cluster-config-file nodes.conf # 这里使用全路径，不然在模拟的时候使用脚本开始会导致nodes.con重叠，导致redis开启失败
  cluster-node-timeout 5000
  appendonly yes
  daemonize    yes
  ```
  5. 启动redis，每个都要起噢
  6. 启动集群（第一次启动这个集群需要的操作）
  ```shell
/opt/redis/redis-trib.rb  create --replicas 1 192.168.0.108:7000 192.168.0.110:7000 192.168.0.111:7000 192.168.0.108:8000 192.168.0.110:8000 192.168.0.111:8000
# 其中 replicas 后面的数字表示主从模式下从节点的个数
  ./redis-cli -h 192.168.0.108 -p 7000 -c # 进入集群
  ```
  7. 第二次启动集群的时候，直接启动这些节点即可

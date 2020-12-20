学习笔记

作业主要是集中在以下几个java类中拓展：

1：**io.kimmking.rpcfx.demo.provider.ReflectResolver;**

这个是替代原来Bean查找的，实现的还比较成功...用到了javaassist，就是不太清楚是不是get到了问题的点

2: **io.kimmking.rpcfx.demo.consumer.RpcAspectJ**

这个作业想着生成一个Service的实现类，加上我自定义的AspectJ的注解进行处理；但是失败了，尝试着做了很多修改似乎都不行，应该是方向错了....

AspectJ似乎是编译期增强的，而我用ByteBuddy增强的字节码应该已经错过了统一增强扫描的机会...这个花了好长时间去调试...第三题没时间做了，现在想着先放弃这一题，做第三题去了.

看了下其他同学的，还没看全...但看到的似乎都没做对client这块进行操作...
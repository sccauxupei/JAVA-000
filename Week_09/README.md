学习笔记

作业主要是集中在以下几个java类中拓展：

1：**io.kimmking.rpcfx.demo.provider.ReflectResolver;**

这个是替代原来Bean查找的，实现的还比较成功...用到了javaassist，就是不太清楚是不是get到了问题的点

2: **io.kimmking.rpcfx.demo.consumer.RpcAspectJ**

这个作业想着生成一个Service的实现类，加上我自定义的AspectJ的注解进行处理；但是失败了，尝试着做了很多修改似乎都不行，应该是方向错了....

AspectJ似乎是编译期增强的，而我用ByteBuddy增强的字节码应该已经错过了统一增强扫描的机会...这个花了好长时间去调试...第三题没时间做了，现在想着先放弃这一题，做第三题去了.

看了下其他同学的，还没看全...但看到的似乎都没做对client这块进行操作...

12月20号：

有新想法了，生命周期理解的不透彻，我是调用时增强的字节码，应该不是这样玩的。新想法是在Bean初始化时将所有的Service方法都进行增强，我猜这样应该就能被AOP切到了。我找找该怎么做，估计过段时间就有结果了。

**io.kimmking.rpcfx.demo.consumer.RpcAspectJ**继承了BeanFactoryPostProcessor将所有符合条件的类增强注入了...还是不能被增强...以为这个时机已经很靠前了，之前是因为ApplicationContext完成之后调用后再增强的，比较靠后现在是在Spring进行配置的时候进行增强的...似乎还是不行...

我打个断点看看那个切面被增强的时候处于哪个生命周期吧...
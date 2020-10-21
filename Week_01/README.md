学习笔记

1.反射性能相较于直接调用，主要耗时的地方在哪里

​        因为以前也自己写过一点点关于反射的心得体会，而且这个问题好像在哪看过有解答，应该可以回答一点点，就在群上说了下想法：应该是加载、链接、初始化耗时比较多。后来仔细想，越想越不对劲，就去查了下，其实也是一直以来没搞的太明白，查出来以后，后面给出的两句话都有很大的问题的。后来同学给出了知乎上的解答，解答抽取自R大的博文，其实那时候也一直在看，没看完，就没说，R大的解释靠谱太多了，下面就来反思一下：

​        我说的加载、连接、初始化；其实所有new出的对象都会经历这一个步骤，并不单单是反射会这样；我把这个过程当成是主要耗时的原因是非常不合理的，连边都没挨着；断断续续些了这些文字，在这个过程中有同学说他被问过这么一个问题：

1. 为什么要用Class.forName()，应该是为了让程序更灵活，让程序可以根据配置去加载不同的类;

2. 说现在已经不用Class.forName()加载sql驱动了

   看了下Spring的源码（org.springframework.jdbc.datasource.embedded.SimpleDriverDataSourceFactory），这个类wrapper了一个org.springframework.jdbc.datasource.SimpleDriverDataSource类，通过这个类来创建setDriverClass,而Driver的加载，主要靠的是Class文件默认的Constructors,调用那个Constructor的newInstance()来创建的。

下面来看R大的解释：

R大博文的地址：https://www.iteye.com/blog/rednaxelafx-548536

2.Spring大事务主要阻塞在哪个层面（Spring事务的实现）





Week_001随手记

1. 基本类型计算出现转换时，是在编译时就进行转换的，这样JVM在运算的时候不用做任何处理，所以很快。
2. invokevirtual:C语言分实虚方法，如果是实方法，子类转为父方法时，可以直接使用父方法
3. invokedynamic
4. 初次调用MethodHandle实例时，初始化MethodHandle所在的类。
5. 加载（将字节码文件放到JVM里，Class.forName()和ClassLoader.loadClass()都默认只到加载阶段）
6. 由于类加载器有缓存，所以一个类在一个类加载器里只会被加载一次。
7. JDK9以后，ext和app不在继承URLClassLoader,JDK9可以new一个classLoader，然后把ClassLoader加进去。

# 这段时间在补缺，还有些补缺笔记没做，未总结完...待填充...
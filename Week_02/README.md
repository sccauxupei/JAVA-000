学习笔记

发现原来的分支错了，尴尬，没复查，现在试试成了没



Socket知识梳理

​	**上下文切换相关**

​	**Q1:阻塞、非阻塞/同步、异步 整理**

1. 阻塞、非阻塞：是从线程用不用停顿下来等待结果角度来考虑问题

2. 同步、异步：是指接受结果的时候，是接收方主动去不断询问；还是发送方通过Event/Callback主动推送给接收方。

   老师的说法：同步异步是个通信的模式，阻塞非阻塞是指线程的模式。

**NIO**(这块其实讲的...比较含糊，只能结合老师想表达的意思，还有自己以前看过的内容整理下)



有关poll和epoll:epoll用了红黑树，零拷贝

**延迟问题搞不定**：是因为对于线程来说数据是必要的，数据没准备好，一般来说线程都是要陷入等待，用Event方式，应当只是将被阻塞到的线程reuse起来。而不是浪费线程池中的一个线程，等待结果。

**多级缓冲模型**:建更小的坝，建更多的段，一段一段调节流量。->二八原则吧。



Proactor和Reactor:Proactor是服务端将数据处理好，客户端只需要接收数据而不需要进一步处理。而Reactor仅仅是拿到资源的使用权限。



当结果数据的**顺序/中间结果顺序**不重要的时候，可以用异步。



关于内核空间和用户空间





Servlet和JSP

doGet/doPost方法——》通过PrintWriter之类的对doPost/doGet方法，进行Hearer拼接/组装，最后对结果进行返回。



HTTP服务端：

Web服务端：封装了Session等会话相关的东西

J2EE服务端：封装了J2EE企业级需要的监控埋点MXBEAN等信息。  





​		了解了下JMX，在项目里做了一个小Demo，看到它的使用方式，想到了Spring Cloud里面的refresh，感觉特别像。大概看了下Cloud里面的实现，发现好像确实用的是JMX去实现的。

​		它主要起作用的是Scope这个，一般我们说Spring中的Scope时，想到的是Singleton或者Portotype，但其实早在SpringMVC阶段，就定义了Web相关的Scope，只不过这些东西刷面试题的时候并没有特别重点的点出，找到一篇博客讲Scope的（https://www.jianshu.com/p/188013dd3d02），正确性先搁置一边，大概他想表达的逻辑我理了一遍，没发现什么特别大的问题，跑跑打断点调试下估计就能验证了。

​		Spring Cloud中refresh的实现就是在原有的基础上定义了一个RefreshScope（定义这个Scope的目的，详见AbstractBeanFactory#doGetBean）。

​		它RefreshScope的写法感觉比较奇特，是直接在构造方法里对父类private name的成员变量进行重新赋值。
package cn.zs.mstpxu.memory.mxbean;

import javax.management.Notification;

import org.springframework.jmx.export.annotation.ManagedNotification;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.jmx.export.notification.NotificationPublisher;
import org.springframework.jmx.export.notification.NotificationPublisherAware;
import org.springframework.stereotype.Component;

import cn.zs.mstpxu.mxbean.interf.SpittleNotifier;

//@ManagedResource("spittle:name=SpitterNotifier")
//@ManagedNotification(notificationTypes="SpitterNotifier.OneMillionSpittles", name="TODO")

//@ManagedResource(objectName = "com.tang.jmx:type=SimpleBean", description = "这里是描述")

@Component
@ManagedResource(value = "cn.zs.mstpxu.mxbean:name=SpittleNotificationImpl", description = "test")
@ManagedNotification(notificationTypes="SpittleNotificationImpl.OneMillionSpittles", name="TODO")
public class SpittleNotificationImpl implements NotificationPublisherAware, SpittleNotifier {
	
    private NotificationPublisher notificationPublisher;
    
    //注入notificationPublisher
    @Override
    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }
    

	@ManagedOperation(description = "millionthSpittlePosted()")
    public void millionthSpittlePosted() {
        //发送通知
//        notificationPublisher.sendNotification(new Notification("SpittleNotifier.OneMillionSpittles", this, 0));
        notificationPublisher.sendNotification(new Notification("SpittleNotificationImpl.OneMillionSpittles", this, 0));
    }

}

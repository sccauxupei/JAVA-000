package cn.zs.mstpxu.memory.mxbean;

import org.springframework.jmx.export.annotation.ManagedNotification;
import org.springframework.jmx.export.annotation.ManagedResource;


@ManagedResource("spittle:name=SpitterNotifier")
@ManagedNotification(notificationTypes="SpitterNotifier.OneMillionSpittles", name="TODO")
public class TestMain {

	public static void main(String[] args) {
		JvmInfo.printGarbageCollectorInfo();
		
//		JvmInfo.printOperatingSystemInfo();
	}

}

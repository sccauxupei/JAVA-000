/**
 * @aothor Master_PXu 
 * @date 时间 2020年11月2日下午5:37:30
 * @project_name 项目名 ruoyi-quartz
 * @type_name 类名 TaskUtils
 * @function 功能 TODO Task的工具类
 */
package cn.zs.mstpxu;

import java.util.Calendar;

/**
 * @author Master_PeiXU
 *
 */
public abstract class TaskUtils {
	
	public static String genKey() {
		Calendar calendar = Calendar.getInstance();
		return "customer-in-" + calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH) - 1) + 
				"-" + calendar.get(Calendar.MILLISECOND);
	}
}

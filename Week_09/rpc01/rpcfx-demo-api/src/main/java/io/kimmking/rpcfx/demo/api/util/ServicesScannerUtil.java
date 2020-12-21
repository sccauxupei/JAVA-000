package io.kimmking.rpcfx.demo.api.util;

import java.util.Set;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import io.kimmking.rpcfx.demo.api.annotation.ServiceScanner;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月21日上午9:16:59
 * @project_name 项目名 rpcfx-demo-api
 * @type_name 类名 ServiceScanner
 * @function 功能 TODO
 */
public class ServicesScannerUtil extends ClassPathBeanDefinitionScanner {

	/**
	 * @param registry
	 */
	public ServicesScannerUtil(BeanDefinitionRegistry registry) {
		super(registry);
	}

	
    @Override
    protected void registerDefaultFilters() {
        // 添加自定义的过滤器
        addIncludeFilter(new AnnotationTypeFilter(ServiceScanner.class));
    }

    // 默认的话，还有其他过滤器，在限制一下
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return super.isCandidateComponent(beanDefinition) && beanDefinition.getMetadata().hasAnnotation(ServiceScanner.class.getName());
    }

    // 顺便打印一下，确认流程正确
    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> scanResult = super.doScan(basePackages);
        scanResult.forEach(beanDefinitionHolder -> System.err.println(beanDefinitionHolder.getBeanDefinition().getBeanClassName()));
        return scanResult;
    }
}

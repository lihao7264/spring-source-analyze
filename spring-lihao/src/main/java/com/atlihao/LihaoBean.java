package com.atlihao;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author lihao
 * @ClassName LihaoBean
 * @Since 2020/6/9
 * @Description
 */
public class LihaoBean implements InitializingBean, ApplicationContextAware {

	private ItBean itBean;

	public void setItBean(ItBean itBean) {
		this.itBean = itBean;
	}

	/**
	 * 构造函数
	 */
	public LihaoBean() {
		System.out.println("LihaoBean 构造器...");
	}


	/**
	 * InitializingBean 接口实现
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("LihaoBean afterPropertiesSet...");
	}

	public void print() {
		System.out.println("print方法业务逻辑执行");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		System.out.println("setApplicationContext....");
	}
}

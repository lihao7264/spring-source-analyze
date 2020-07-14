import com.atlihao.LihaoBean;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

/**
 * @author lihao
 * @ClassName IoCTest
 * @Since 2020/6/9
 * @Description
 */
public class IoCTest {

	/**
	 * IoC容器源码分析基础案例
	 */
	@Test
	public void testIoC() {
		// ApplicationContext是容器的高级接口，BeanFactory（顶级容器/根容器，规范了/定义了容器的基础行为）
		// ApplicationContext：Spring应用上下文，官方称之为IoC容器（map是IoC容器的一个成员叫做单例池--singletonObjects
		// 容器是一组组件和过程的集合，包括BeanFactory、单例池、BeanPostProcessor等以及之间的协作流程）
		// 1、构造器执行、初始化方法(afterPropertiesSet)执行、Bean后置处理器的before/after方法：AbstractApplicationContext#refresh#finishBeanFactoryInitialization
		// 2、Bean工厂后置处理器初始化、方法执行：AbstractApplicationContext#refresh#invokeBeanFactoryPostProcessors
		// 3、Bean后置处理器初始化、：AbstractApplicationContext#refresh#registerBeanPostProcessors
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		LihaoBean lihaoBean = applicationContext.getBean(LihaoBean.class);
		System.out.println(lihaoBean);
	}

	/**
	 * AOP源码分析基础案例
	 */
	@Test
	public void testAOP() {
		// ApplicationContext是容器的高级接口，BeanFactory（顶级容器/根容器，规范了/定义了容器的基础行为）
		// ApplicationContext：Spring应用上下文，官方称之为IoC容器（map是IoC容器的一个成员叫做单例池--singletonObjects
		// 容器是一组组件和过程的集合，包括BeanFactory、单例池、BeanPostProcessor等以及之间的协作流程）
		// 1、构造器执行、初始化方法(afterPropertiesSet)执行、Bean后置处理器的before/after方法：AbstractApplicationContext#refresh#finishBeanFactoryInitialization
		// 2、Bean工厂后置处理器初始化、方法执行：AbstractApplicationContext#refresh#invokeBeanFactoryPostProcessors
		// 3、Bean后置处理器初始化、：AbstractApplicationContext#refresh#registerBeanPostProcessors
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		LihaoBean lihaoBean = applicationContext.getBean(LihaoBean.class);
		lihaoBean.print();
		System.out.println(lihaoBean);
	}


	/**
	 * 游侠客笔试题--- Integer、int判断
	 */
	@Test
	public void testInt() {
		Integer i1 = 2;
		Integer i2 = 2;
		Integer i3 = new Integer(2);
		Integer i4 = new Integer(2);
		int i5 = 2;
		System.out.println(i1 == i2);  //true
		System.out.println(i1 == i3);  //false
		System.out.println(i1 == i5);  //true
		System.out.println(i3 == i4);  //false
		System.out.println(i3 == i5);  //true
	}

	/**
	 * 游侠客笔试题--- String判断
	 */
	@Test
	public void testString() {
		String s1 = "test1test2";
		String s2 = new String("test1test2");
		String s3 = "test1" + "test2";
		System.out.println(s3 == s1);  //true
		System.out.println(s3 == s2);  //false
	}

	/**
	 * 游侠客笔试题--- Double和Float判断
	 */
	@Test
	public void testDoubleAndFloat() {
		Double d = 2.44d;
		Float f = 2.44f;
		System.out.println(d.floatValue() == f);  //向上转型   true
		System.out.println(d == f.doubleValue()); //向下转型   false
	}

	/**
	 * 游侠客笔试题--- short判断
	 * 会编译报错,精度的问题,得转int
	 */
//	@Test
//	public void testShort() {
//		short s1 = 2;
//		s1 = s1 + s1;
//	}

}

/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context;

import java.io.Closeable;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.lang.Nullable;

/**
 * SPI interface to be implemented by most if not all application contexts.
 * Provides facilities to configure an application context in addition
 * to the application context client methods in the
 * {@link org.springframework.context.ApplicationContext} interface.
 *
 * <p>Configuration and lifecycle methods are encapsulated here to avoid
 * making them obvious to ApplicationContext client code. The present
 * methods should only be used by startup and shutdown code.
 *
 * 一个可配置的 ApplicationContext
 *
 * 它是一种SPI接口，将由大多数（如果不是全部）ApplicationContext 的子类实现。
 * 除了 ApplicationContext 接口中的应用程序上下文客户端方法外，还提供了用于配置 ApplicationContext 的功能。
 *
 * 配置和生命周期方法都封装在这里，以避免这些代码显式的暴露给 ApplicationContext 客户端代码。
 * 本方法仅应由启动和关闭容器的代码使用。
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 03.11.2003
 */
public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {

	/**
	 * Any number of these characters are considered delimiters between
	 * multiple context config paths in a single String value.
	 * 在单个String值中，可以将任意数量的这些字符视为多个上下文配置路径之间的分隔符。
	 * @see org.springframework.context.support.AbstractXmlApplicationContext#setConfigLocation
	 * @see org.springframework.web.context.ContextLoader#CONFIG_LOCATION_PARAM
	 * @see org.springframework.web.servlet.FrameworkServlet#setContextConfigLocation
	 */
	String CONFIG_LOCATION_DELIMITERS = ",; \t\n";

	/**
	 * Name of the ConversionService bean in the factory.
	 * If none is supplied, default conversion rules apply.
	 * 工厂中ConversionService bean的名称。 如果未提供任何内容，则适用默认转换规则。
	 * @since 3.0
	 * @see org.springframework.core.convert.ConversionService
	 */
	String CONVERSION_SERVICE_BEAN_NAME = "conversionService";

	/**
	 * Name of the LoadTimeWeaver bean in the factory. If such a bean is supplied,
	 * the context will use a temporary ClassLoader for type matching, in order
	 * to allow the LoadTimeWeaver to process all actual bean classes.
	 * 工厂中LoadTimeWeaver Bean的名称。
	 * 如果提供了这样的bean，则上下文将使用临时的ClassLoader进行类型匹配，以允许LoadTimeWeaver处理所有实际的bean类。
	 * @since 2.5
	 * @see org.springframework.instrument.classloading.LoadTimeWeaver
	 */
	String LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver";

	/**
	 * Name of the {@link Environment} bean in the factory.
	 * 工厂中{@link Environment} bean的名称。
	 * @since 3.1
	 */
	String ENVIRONMENT_BEAN_NAME = "environment";

	/**
	 * Name of the System properties bean in the factory.
	 * 工厂中系统属性Bean的名称。
	 * @see java.lang.System#getProperties()
	 */
	String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";

	/**
	 * Name of the System environment bean in the factory.
	 * 工厂中系统环境bean的名称。
	 * @see java.lang.System#getenv()
	 */
	String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";


	/**
	 * Set the unique id of this application context.
	 * 设置此应用程序上下文的唯一ID。
	 * @since 3.0
	 */
	void setId(String id);

	/**
	 * Set the parent of this application context.
	 * <p>Note that the parent shouldn't be changed: It should only be set outside
	 * a constructor if it isn't available when an object of this class is created,
	 * for example in case of WebApplicationContext setup.
	 * 设置此应用程序上下文的父级。
	 * 请注意，不应更改父级:仅当在创建此类的对象时不可用（例如在WebApplicationContext设置的情况下）时，
	 * 才应在构造函数外部设置父级。
	 * @param parent the parent context  父上下文
	 * @see org.springframework.web.context.ConfigurableWebApplicationContext
	 */
	void setParent(@Nullable ApplicationContext parent);

	/**
	 * Set the {@code Environment} for this application context.
	 * 为此应用程序上下文设置{@code Environment}。
	 * @param environment the new environment
	 * @since 3.1
	 */
	void setEnvironment(ConfigurableEnvironment environment);

	/**
	 * Return the {@code Environment} for this application context in configurable
	 * form, allowing for further customization.
	 * 以可配置的形式返回此应用程序上下文的{@code Environment}，以便进行进一步的自定义。
	 *
	 * @since 3.1
	 */
	@Override
	ConfigurableEnvironment getEnvironment();

	/**
	 * Add a new BeanFactoryPostProcessor that will get applied to the internal
	 * bean factory of this application context on refresh, before any of the
	 * bean definitions get evaluated. To be invoked during context configuration.
	 * 添加一个新的BeanFactoryPostProcessor，在刷新任何bean定义之前，将在刷新时将其应用于此应用程序上下文的内部bean工厂。
	 * 在上下文配置期间调用。
	 * @param postProcessor the factory processor to register  要注册的工厂处理器
	 */
	void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

	/**
	 * Add a new ApplicationListener that will be notified on context events
	 * such as context refresh and context shutdown.
	 * <p>Note that any ApplicationListener registered here will be applied
	 * on refresh if the context is not active yet, or on the fly with the
	 * current event multicaster in case of a context that is already active.
	 *
	 * 添加一个新的ApplicationListener，它将在发生上下文事件（例如上下文刷新和上下文关闭）时收到通知。
	 * 请注意，此处注册的所有ApplicationListener都将被应用
	 *         如果上下文尚未处于活动状态，则刷新或使用
	 *         如果上下文已经处于活动状态，则为当前事件广播器。
	 * @param listener the ApplicationListener to register
	 * @see org.springframework.context.event.ContextRefreshedEvent
	 * @see org.springframework.context.event.ContextClosedEvent
	 */
	void addApplicationListener(ApplicationListener<?> listener);

	/**
	 * Register the given protocol resolver with this application context,
	 * allowing for additional resource protocols to be handled.
	 * <p>Any such resolver will be invoked ahead of this context's standard
	 * resolution rules. It may therefore also override any default rules.
	 *
	 * 在此应用程序上下文中注册给定的协议解析器，从而允许处理其它资源协议。
	 * 任何此类解析程序都将在此上下文的标准解析规则之前调用。
	 * 因此，它也可以覆盖任何默认规则。
	 * @since 4.3
	 */
	void addProtocolResolver(ProtocolResolver resolver);

	/**
	 * Load or refresh the persistent representation of the configuration,
	 * which might an XML file, properties file, or relational database schema.
	 * <p>As this is a startup method, it should destroy already created singletons
	 * if it fails, to avoid dangling resources. In other words, after invocation
	 * of that method, either all or no singletons at all should be instantiated.
	 * 核心接口：在IOC容器的启动刷新时使用到
	 *
	 * 加载或刷新配置的持久表示形式，可能是XML文件、属性文件或关系数据库架构。
	 * 由于这是一种启动方法，因此如果失败，它应该销毁已创建的单例，以避免悬挂资源。
	 * 换句话说，在调用该方法之后，应该实例化所有单例或根本不实例化。
	 *
	 * @throws BeansException if the bean factory could not be initialized
	 * BeansException  如果无法初始化bean工厂
	 * @throws IllegalStateException if already initialized and multiple refresh
	 * attempts are not supported
	 * IllegalStateException  如果已经初始化并且不支持多次刷新尝试
	 */
	void refresh() throws BeansException, IllegalStateException;

	/**
	 * Register a shutdown hook with the JVM runtime, closing this context
	 * on JVM shutdown unless it has already been closed at that time.
	 * <p>This method can be called multiple times. Only one shutdown hook
	 * (at max) will be registered for each context instance.
	 *
	 * 向JVM运行时注册一个shutdown挂钩，除非JVM当时已经关闭，否则在JVM关闭时关闭该上下文。
	 * 可以多次调用此方法。
	 * 每个上下文实例仅注册一个关闭挂钩（最大数量）。
	 * @see java.lang.Runtime#addShutdownHook
	 * @see #close()
	 */
	void registerShutdownHook();

	/**
	 * Close this application context, releasing all resources and locks that the
	 * implementation might hold. This includes destroying all cached singleton beans.
	 * <p>Note: Does <i>not</i> invoke {@code close} on a parent context;
	 * parent contexts have their own, independent lifecycle.
	 * <p>This method can be called multiple times without side effects: Subsequent
	 * {@code close} calls on an already closed context will be ignored.
	 *
	 * 关闭此应用程序上下文，释放实现可能持有的所有资源和锁。
	 * 这包括销毁所有缓存的单例bean。
	 * 注意：不是在父上下文上调用{@code close}； 父级上下文具有自己的独立生命周期。
	 *       可以多次调用此方法，而不会产生副作用：在已关闭的上下文上进行的后续{@code close}调用将被忽略。
	 */
	@Override
	void close();

	/**
	 * Determine whether this application context is active, that is,
	 * whether it has been refreshed at least once and has not been closed yet.
	 *
	 * 确定此应用程序上下文是否处于活动状态，即是否至少刷新一次并且尚未关闭。
	 * @return whether the context is still active  上下文是否仍处于活跃状态
	 * @see #refresh()
	 * @see #close()
	 * @see #getBeanFactory()
	 */
	boolean isActive();

	/**
	 * Return the internal bean factory of this application context.
	 * Can be used to access specific functionality of the underlying factory.
	 * <p>Note: Do not use this to post-process the bean factory; singletons
	 * will already have been instantiated before. Use a BeanFactoryPostProcessor
	 * to intercept the BeanFactory setup process before beans get touched.
	 * <p>Generally, this internal factory will only be accessible while the context
	 * is active, that is, in-between {@link #refresh()} and {@link #close()}.
	 * The {@link #isActive()} flag can be used to check whether the context
	 * is in an appropriate state.
	 *
	 * 返回此应用程序上下文的内部bean工厂。
	 * 可用于访问基础工厂的特定功能。
	 * 注意：请勿使用此方法对bean工厂进行后置处理。
	 *      单例之前已经被实例化。
	 *      使用BeanFactoryPostProcessor来拦截Bean之前的BeanFactory设置过程。
	 *      通常，只有在上下文处于活动状态时，即{@link #refresh（）}和{@link #close（）}之间，才能访问此内部工厂。
	 *      {@link #isActive（）}标志可用于检查上下文是否处于适当的状态。
	 *
	 * @return the underlying bean factory  底层bean工厂
	 * @throws IllegalStateException if the context does not hold an internal
	 * bean factory (usually if {@link #refresh()} hasn't been called yet or
	 * if {@link #close()} has already been called)
	 * @see #isActive()
	 * @see #refresh()
	 * @see #close()
	 * @see #addBeanFactoryPostProcessor
	 */
	ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

}

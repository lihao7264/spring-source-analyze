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

package org.springframework.core.env;

/**
 * Interface representing the environment in which the current application is running.
 * Models two key aspects of the application environment: <em>profiles</em> and
 * <em>properties</em>. Methods related to property access are exposed via the
 * {@link PropertyResolver} superinterface.
 *
 * <p>A <em>profile</em> is a named, logical group of bean definitions to be registered
 * with the container only if the given profile is <em>active</em>. Beans may be assigned
 * to a profile whether defined in XML or via annotations; see the spring-beans 3.1 schema
 * or the {@link org.springframework.context.annotation.Profile @Profile} annotation for
 * syntax details. The role of the {@code Environment} object with relation to profiles is
 * in determining which profiles (if any) are currently {@linkplain #getActiveProfiles
 * active}, and which profiles (if any) should be {@linkplain #getDefaultProfiles active
 * by default}.
 *
 * <p><em>Properties</em> play an important role in almost all applications, and may
 * originate from a variety of sources: properties files, JVM system properties, system
 * environment variables, JNDI, servlet context parameters, ad-hoc Properties objects,
 * Maps, and so on. The role of the environment object with relation to properties is to
 * provide the user with a convenient service interface for configuring property sources
 * and resolving properties from them.
 *
 * <p>Beans managed within an {@code ApplicationContext} may register to be {@link
 * org.springframework.context.EnvironmentAware EnvironmentAware} or {@code @Inject} the
 * {@code Environment} in order to query profile state or resolve properties directly.
 *
 * <p>In most cases, however, application-level beans should not need to interact with the
 * {@code Environment} directly but instead may have to have {@code ${...}} property
 * values replaced by a property placeholder configurer such as
 * {@link org.springframework.context.support.PropertySourcesPlaceholderConfigurer
 * PropertySourcesPlaceholderConfigurer}, which itself is {@code EnvironmentAware} and
 * as of Spring 3.1 is registered by default when using
 * {@code <context:property-placeholder/>}.
 *
 * <p>Configuration of the environment object must be done through the
 * {@code ConfigurableEnvironment} interface, returned from all
 * {@code AbstractApplicationContext} subclass {@code getEnvironment()} methods. See
 * {@link ConfigurableEnvironment} Javadoc for usage examples demonstrating manipulation
 * of property sources prior to application context {@code refresh()}.
 *
 * 表示当前应用程序正在其中运行的环境的接口。
 * 它为应用环境制定了两个关键的方面：profile和properties。与
 * 属性访问有关的方法通过PropertyResolver这个父接口公开。
 *
 * profile机制保证了仅在给定 profile 处于激活状态时，才向容器注册的Bean定义的命名逻辑组。
 * 无论是用XML定义还是通过注解定义，都可以将Bean分配给指定的 profile。
 * 有关语法的详细信息，请参见spring-beans 3.1规范文档 或 @Profile 注解。
 * Environment 的作用是决定当前哪些配置文件（如果有）处于活动状态，以及默认情况下哪些配置文件（如果有）应处于活动状态。
 *
 * Properties在几乎所有应用程序中都起着重要作用，
 * 并且可能来源自多种途径：属性文件、JVM系统属性、系统环境变量、JNDI、ServletContext 参数、临时属性对象、Map等。
 * Environment 与 Properties 的关系是为用户提供方便的服务接口，以配置属性源，并从中解析属性值。
 *
 * 在ApplicationContext中管理的Bean可以注册为EnvironmentAware或使用 @Inject 标注在 Environment上，
 * 以便直接查询profile的状态或解析 Properties。
 *
 * 但是，在大多数情况下，应用程序级Bean不必直接与 Environment 交互，
 * 而是通过将${...}属性值替换为属性占位符配置器进行属性注入（例如 PropertySourcesPlaceholderConfigurer），
 * 该属性本身是 EnvironmentAware，当配置了 <context:property-placeholder/> 时，默认情况下会使用Spring 3.1的规范注册。
 *
 * 必须通过从所有AbstractApplicationContext子类的 getEnvironment()方法返回的 ConfigurableEnvironment 接口完成环境对象的配置。
 * 请参阅 ConfigurableEnvironment 的Javadoc以获取使用示例，这些示例演示在应用程序上下文 refresh() 方法被调用之前对属性源进行的操作。
 *
 * 总结：它是IOC容器的运行环境，它包括Profile和Properties两大部分，它可由一个到几个激活的Profile共同配置，它的配置可在应用级Bean中获取。
 * @author Chris Beams
 * @since 3.1
 * @see PropertyResolver
 * @see EnvironmentCapable
 * @see ConfigurableEnvironment
 * @see AbstractEnvironment
 * @see StandardEnvironment
 * @see org.springframework.context.EnvironmentAware
 * @see org.springframework.context.ConfigurableApplicationContext#getEnvironment
 * @see org.springframework.context.ConfigurableApplicationContext#setEnvironment
 * @see org.springframework.context.support.AbstractApplicationContext#createEnvironment
 */
public interface Environment extends PropertyResolver {

	/**
	 * Return the set of profiles explicitly made active for this environment. Profiles
	 * are used for creating logical groupings of bean definitions to be registered
	 * conditionally, for example based on deployment environment. Profiles can be
	 * activated by setting {@linkplain AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
	 * "spring.profiles.active"} as a system property or by calling
	 * {@link ConfigurableEnvironment#setActiveProfiles(String...)}.
	 * <p>If no profiles have explicitly been specified as active, then any
	 * {@linkplain #getDefaultProfiles() default profiles} will automatically be activated.
	 * 返回为此环境明确激活的配置文件集。
	 * Profiles用于创建Bean定义的逻辑分组，以便有条件地进行注册，例如基于部署环境。
	 * 可以通过将{@linkplain AbstractEnvironment＃ACTIVE_PROFILES_PROPERTY_NAME"spring.profiles.active"}设置为系统属性
	 * 或调用{@link ConfigurableEnvironment＃setActiveProfiles（String ...）}来激活配置文件。
	 * 如果未将任何配置文件明确指定为活动配置，则将自动激活任何{@linkplain #getDefaultProfiles（）默认配置文件}。
	 *
	 * @see #getDefaultProfiles
	 * @see ConfigurableEnvironment#setActiveProfiles
	 * @see AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
	 */
	String[] getActiveProfiles();

	/**
	 * Return the set of profiles to be active by default when no active profiles have
	 * been set explicitly.
	 * 当未显式设置活动profiles时，将缺省情况下返回一组profiles。(spring.profiles.default 配置)
	 * @see #getActiveProfiles
	 * @see ConfigurableEnvironment#setDefaultProfiles
	 * @see AbstractEnvironment#DEFAULT_PROFILES_PROPERTY_NAME
	 */
	String[] getDefaultProfiles();

	/**
	 * Return whether one or more of the given profiles is active or, in the case of no
	 * explicit active profiles, whether one or more of the given profiles is included in
	 * the set of default profiles. If a profile begins with '!' the logic is inverted,
	 * i.e. the method will return {@code true} if the given profile is <em>not</em> active.
	 * For example, {@code env.acceptsProfiles("p1", "!p2")} will return {@code true} if
	 * profile 'p1' is active or 'p2' is not active.
	 * @throws IllegalArgumentException if called with zero arguments
	 * or if any profile is {@code null}, empty, or whitespace only
	 *
	 * 返回一个或多个给定配置文件是否处于活动状态，
	 * 或者在没有显式活动配置文件的情况下，返回一个或多个给定配置文件是否包含在默认配置文件集中。
	 * 如果一个配置文件以"！"开头 逻辑取反，即如果给定的配置文件为不有效，则该方法将返回{@code true}。
	 * 例如，如果配置文件"p1"处于活动状态或"p2"未处于活动状态，
	 * 则{@code env.acceptsProfiles（"p1"，"!p2"）}将返回{@code true}。
	 * IllegalArgumentException 如果使用零参数调用，或者任何配置文件仅为{@code null}，为空或为空白
	 * @see #getActiveProfiles
	 * @see #getDefaultProfiles
	 * @see #acceptsProfiles(Profiles)
	 * @deprecated as of 5.1 in favor of {@link #acceptsProfiles(Profiles)}
	 */
	@Deprecated
	boolean acceptsProfiles(String... profiles);

	/**
	 * Return whether the {@linkplain #getActiveProfiles() active profiles}
	 * match the given {@link Profiles} predicate.
	 * 返回{@linkplain #getActiveProfiles（）活动配置文件}是否与给定的{@link Profiles}匹配。
	 */
	boolean acceptsProfiles(Profiles profiles);

}

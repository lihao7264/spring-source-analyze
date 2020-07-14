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

package org.springframework.core.io.support;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.UrlResource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * General purpose factory loading mechanism for internal use within the framework.
 *
 * <p>{@code SpringFactoriesLoader} {@linkplain #loadFactories loads} and instantiates
 * factories of a given type from {@value #FACTORIES_RESOURCE_LOCATION} files which
 * may be present in multiple JAR files in the classpath. The {@code spring.factories}
 * file must be in {@link Properties} format, where the key is the fully qualified
 * name of the interface or abstract class, and the value is a comma-separated list of
 * implementation class names. For example:
 *
 * <pre class="code">example.MyService=example.MyServiceImpl1,example.MyServiceImpl2</pre>
 *
 * where {@code example.MyService} is the name of the interface, and {@code MyServiceImpl1}
 * and {@code MyServiceImpl2} are two implementations.
 *
 *它是一个框架内部内部使用的通用工厂加载机制。
 *
 * SpringFactoriesLoader从 META-INF/spring.factories 文件中加载并实例化给定类型的工厂，
 * 这些文件可能存在于类路径中的多个jar包中。spring.factories 文件必须采用 properties 格式，
 * 其中key是接口或抽象类的全限定名，而value是用逗号分隔的实现类的全限定类名列表。
 * 例如：example.MyService=example.MyServiceImpl1,example.MyServiceImpl2
 * 其中 example.MyService 是接口的名称，而 MyServiceImpl1 和 MyServiceImpl2 是两个该接口的实现类。
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 3.2
 */
public final class SpringFactoriesLoader {

	/**
	 * The location to look for factories.
	 * <p>Can be present in multiple JAR files.
	 * 寻找工厂的位置。可以存在于多个JAR文件中。
	 */
	public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";


	private static final Log logger = LogFactory.getLog(SpringFactoriesLoader.class);

	// 缓存：classLoader--->META-INF/spring.factories中的属性
	private static final Map<ClassLoader, MultiValueMap<String, String>> cache = new ConcurrentReferenceHashMap<>();


	private SpringFactoriesLoader() {
	}


	/**
	 * Load and instantiate the factory implementations of the given type from
	 * {@value #FACTORIES_RESOURCE_LOCATION}, using the given class loader.
	 * <p>The returned factories are sorted through {@link AnnotationAwareOrderComparator}.
	 * <p>If a custom instantiation strategy is required, use {@link #loadFactoryNames}
	 * to obtain all registered factory names.
	 * @param factoryClass the interface or abstract class representing the factory
	 * @param classLoader the ClassLoader to use for loading (can be {@code null} to use the default)
	 * @throws IllegalArgumentException if any factory implementation class cannot
	 * be loaded or if an error occurs while instantiating any factory
	 * @see #loadFactoryNames
	 */
	public static <T> List<T> loadFactories(Class<T> factoryClass, @Nullable ClassLoader classLoader) {
		Assert.notNull(factoryClass, "'factoryClass' must not be null");
		ClassLoader classLoaderToUse = classLoader;
		if (classLoaderToUse == null) {
			classLoaderToUse = SpringFactoriesLoader.class.getClassLoader();
		}
		List<String> factoryNames = loadFactoryNames(factoryClass, classLoaderToUse);
		if (logger.isTraceEnabled()) {
			logger.trace("Loaded [" + factoryClass.getName() + "] names: " + factoryNames);
		}
		List<T> result = new ArrayList<>(factoryNames.size());
		for (String factoryName : factoryNames) {
			result.add(instantiateFactory(factoryName, factoryClass, classLoaderToUse));
		}
		AnnotationAwareOrderComparator.sort(result);
		return result;
	}

	/**
	 * Load the fully qualified class names of factory implementations of the
	 * given type from {@value #FACTORIES_RESOURCE_LOCATION}, using the given
	 * class loader.
	 * 使用给定的类加载器从 META-INF/spring.factories 中加载给定类型的工厂实现的全限定类名。
	 * @param factoryClass the interface or abstract class representing the factory  代表工厂的接口或抽象类
	 * @param classLoader the ClassLoader to use for loading resources; can be
	 * {@code null} to use the default
	 * 用于加载资源的ClassLoader;可以为{@code null}以使用默认值
	 * @throws IllegalArgumentException if an error occurs while loading factory names  如果加载工厂名称时发生错误
	 * @see #loadFactories
	 */
	public static List<String> loadFactoryNames(Class<?> factoryClass, @Nullable ClassLoader classLoader) {
		// 获取的是要被加载的接口/抽象类的全限定名
		String factoryClassName = factoryClass.getName();
		// 有默认值的获取
		return loadSpringFactories(classLoader).getOrDefault(factoryClassName, Collections.emptyList());
	}

	// 这个方法仅接收了一个类加载器
	private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
		// 1、获取本地缓存: 先从本地缓存中根据当前的类加载器获取是否有一个类型为 MultiValueMap<String, String> 的值。
		MultiValueMap<String, String> result = cache.get(classLoader);
		if (result != null) {
			return result;
		}
        // 那第一次从cache中肯定获取不到值，故下面的if结构肯定不进入，进入下面的try块。
		try {
			// 2、加载spring.factories
			// 获取当前 classpath 下所有jar包中有的 spring.factories 文件，并将它们加载到内存中。
			// 使用 classLoader 去加载了指定常量路径下的资源
			Enumeration<URL> urls = (classLoader != null ?
					// 利用classLoader读取资源
					classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
					// 利用系统的classLoader读取资源
					ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
			result = new LinkedMultiValueMap<>();
			// 遍历
			// 3、 缓存到本地
			// 拿到每一个文件，并用 Properties 方式加载文件，之后把这个文件中每一组键值对都加载出来，放入 MultiValueMap 中。
			// 如果一个接口/抽象类有多个对应的目标类，则使用英文逗号隔开。StringUtils.commaDelimitedListToStringArray会将大字符串拆成一个一个的全限定类名。
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				UrlResource resource = new UrlResource(url);
				// 加载资源
				Properties properties = PropertiesLoaderUtils.loadProperties(resource);
				// 遍历文件中的资源
				for (Map.Entry<?, ?> entry : properties.entrySet()) {
					// 取出前后空格
					String factoryClassName = ((String) entry.getKey()).trim();
					// 将配置值根据","进行拆分
					// 比如:"老李,老王"---->"老李"和"老王"两个字符串
					for (String factoryName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
						// 将对应的结果保存到结果中
						result.add(factoryClassName, factoryName.trim());
					}
				}
			}
			// 将结果放到缓存中
			// 整理完后，整个result放入cache中。下一次再加载时就无需再次加载 spring.factories 文件
			cache.put(classLoader, result);
			return result;
		}
		catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load factories from location [" +
					FACTORIES_RESOURCE_LOCATION + "]", ex);
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T instantiateFactory(String instanceClassName, Class<T> factoryClass, ClassLoader classLoader) {
		try {
			Class<?> instanceClass = ClassUtils.forName(instanceClassName, classLoader);
			if (!factoryClass.isAssignableFrom(instanceClass)) {
				throw new IllegalArgumentException(
						"Class [" + instanceClassName + "] is not assignable to [" + factoryClass.getName() + "]");
			}
			return (T) ReflectionUtils.accessibleConstructor(instanceClass).newInstance();
		}
		catch (Throwable ex) {
			throw new IllegalArgumentException("Unable to instantiate factory class: " + factoryClass.getName(), ex);
		}
	}

}

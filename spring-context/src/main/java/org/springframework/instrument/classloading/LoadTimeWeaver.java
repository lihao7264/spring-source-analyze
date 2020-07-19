/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.instrument.classloading;

import java.lang.instrument.ClassFileTransformer;

/**
 * Defines the contract for adding one or more
 * {@link ClassFileTransformer ClassFileTransformers} to a {@link ClassLoader}.
 *
 * <p>Implementations may operate on the current context {@code ClassLoader}
 * or expose their own instrumentable {@code ClassLoader}.
 * 定义将一个或多个{@link ClassFileTransformer ClassFileTransformers}添加到{@link ClassLoader}的合同。
 * 实现可以在当前上下文{@code ClassLoader}上运行，也可以公开自己的可检测{@code ClassLoader}。
 *
 * @author Rod Johnson
 * @author Costin Leau
 * @since 2.0
 * @see java.lang.instrument.ClassFileTransformer
 */
public interface LoadTimeWeaver {

	/**
	 * Add a {@code ClassFileTransformer} to be applied by this
	 * {@code LoadTimeWeaver}.
	 * 添加一个{@code ClassFileTransformer}，以供此{@code LoadTimeWeaver}应用。
	 * @param transformer the {@code ClassFileTransformer} to add   添加的{@code ClassFileTransformer}
	 */
	void addTransformer(ClassFileTransformer transformer);

	/**
	 * Return a {@code ClassLoader} that supports instrumentation
	 * through AspectJ-style load-time weaving based on user-defined
	 * {@link ClassFileTransformer ClassFileTransformers}.
	 * <p>May be the current {@code ClassLoader}, or a {@code ClassLoader}
	 * created by this {@link LoadTimeWeaver} instance.
	 *
	 * 返回一个{@code ClassLoader}，它通过基于用户定义的{@link ClassFileTransformer ClassFileTransformers}的AspectJ风格的加载时编织支持检测。
	 * 可以是当前的{@code ClassLoader}，也可以是由此{@link LoadTimeWeaver}实例创建的{@code ClassLoader}。
	 *
	 * @return the {@code ClassLoader} which will expose
	 * instrumented classes according to the registered transformers
	 */
	ClassLoader getInstrumentableClassLoader();

	/**
	 * Return a throwaway {@code ClassLoader}, enabling classes to be
	 * loaded and inspected without affecting the parent {@code ClassLoader}.
	 * <p>Should <i>not</i> return the same instance of the {@link ClassLoader}
	 * returned from an invocation of {@link #getInstrumentableClassLoader()}.
	 *
	 * 返回一个废弃的{@code ClassLoader}，从而可以加载和检查类，而不会影响父类{@code ClassLoader}。
	 * 应该不返回从调用{@link #getInstrumentableClassLoader（）}返回的{@link ClassLoader}的相同实例。
	 *
	 * @return a temporary throwaway {@code ClassLoader}; should return
	 * a new instance for each call, with no existing state
	 */
	ClassLoader getThrowawayClassLoader();

}

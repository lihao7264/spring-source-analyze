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

package org.springframework.web.servlet.config.annotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;

/**
 * Helps with configuring options for asynchronous request processing.
 * 帮助配置用于异步请求处理的选项。
 *
 * @author Rossen Stoyanchev
 * @since 3.2
 */
public class AsyncSupportConfigurer {

	//异步任务执行器
	@Nullable
	private AsyncTaskExecutor taskExecutor;

	//
	@Nullable
	private Long timeout;

	private final List<CallableProcessingInterceptor> callableInterceptors = new ArrayList<>();

	private final List<DeferredResultProcessingInterceptor> deferredResultInterceptors = new ArrayList<>();


	/**
	 * The provided task executor is used to:
	 * <ol>
	 * <li>Handle {@link Callable} controller method return values.
	 * <li>Perform blocking writes when streaming to the response
	 * through a reactive (e.g. Reactor, RxJava) controller method return value.
	 * </ol>
	 * <p>By default only a {@link SimpleAsyncTaskExecutor} is used. However when
	 * using the above two use cases, it's recommended to configure an executor
	 * backed by a thread pool such as {@link ThreadPoolTaskExecutor}.
	 *
	 * 提供的任务执行程序用于：
	 *  1、处理{@link Callable}控制器方法的返回值。
	 *  2、通过反应式（例如Reactor，RxJava）控制器方法返回值流式传输至响应时，执行阻塞操作。
	 *
	 * 默认情况下，仅使用{@link SimpleAsyncTaskExecutor}。
	 * 但是，在使用以上两种用例时，建议配置由线程池（例如{@link ThreadPoolTaskExecutor}）支持的执行程序。
	 * @param taskExecutor the task executor instance to use by default
	 */
	public AsyncSupportConfigurer setTaskExecutor(AsyncTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
		return this;
	}

	/**
	 * Specify the amount of time, in milliseconds, before asynchronous request
	 * handling times out. In Servlet 3, the timeout begins after the main request
	 * processing thread has exited and ends when the request is dispatched again
	 * for further processing of the concurrently produced result.
	 * <p>If this value is not set, the default timeout of the underlying
	 * implementation is used, e.g. 10 seconds on Tomcat with Servlet 3.
	 *
	 * 指定异步请求处理超时之前的时间（以毫秒为单位）。
	 * 在Servlet 3中，超时从主请求处理线程退出后开始，到再次分派请求以进一步处理并发结果时结束。
	 * 如果未设置此值，则底层实现的默认超时为 使用，例如 使用Servlet 3在Tomcat上的10秒。
	 * @param timeout the timeout value in milliseconds  超时值（以毫秒为单位）
	 */
	public AsyncSupportConfigurer setDefaultTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}

	/**
	 * Configure lifecycle interceptors with callbacks around concurrent request
	 * execution that starts when a controller returns a
	 * {@link java.util.concurrent.Callable}.
	 * 使用在控制器返回{@link java.util.concurrent.Callable}时开始的并发请求执行周围的回调来配置生命周期拦截器。
	 * @param interceptors the interceptors to register  注册的拦截器
	 */
	public AsyncSupportConfigurer registerCallableInterceptors(CallableProcessingInterceptor... interceptors) {
		this.callableInterceptors.addAll(Arrays.asList(interceptors));
		return this;
	}

	/**
	 * Configure lifecycle interceptors with callbacks around concurrent request
	 * execution that starts when a controller returns a {@link DeferredResult}.
	 * 使用在控制器返回{@link DeferredResult}时开始的并发请求执行周围的回调来配置生命周期拦截器。
	 * @param interceptors the interceptors to register  注册的拦截器
	 */
	public AsyncSupportConfigurer registerDeferredResultInterceptors(
			DeferredResultProcessingInterceptor... interceptors) {

		this.deferredResultInterceptors.addAll(Arrays.asList(interceptors));
		return this;
	}


	@Nullable
	protected AsyncTaskExecutor getTaskExecutor() {
		return this.taskExecutor;
	}

	@Nullable
	protected Long getTimeout() {
		return this.timeout;
	}

	protected List<CallableProcessingInterceptor> getCallableInterceptors() {
		return this.callableInterceptors;
	}

	protected List<DeferredResultProcessingInterceptor> getDeferredResultInterceptors() {
		return this.deferredResultInterceptors;
	}

}

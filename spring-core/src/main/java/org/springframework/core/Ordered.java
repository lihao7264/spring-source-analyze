/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.core;

/**
 * {@code Ordered} is an interface that can be implemented by objects that
 * should be <em>orderable</em>, for example in a {@code Collection}.
 *
 * <p>The actual {@link #getOrder() order} can be interpreted as prioritization,
 * with the first object (with the lowest order value) having the highest
 * priority.
 *
 * <p>Note that there is also a <em>priority</em> marker for this interface:
 * {@link PriorityOrdered}. Order values expressed by {@code PriorityOrdered}
 * objects always apply before same order values expressed by <em>plain</em>
 * {@link Ordered} objects.
 *
 * <p>Consult the Javadoc for {@link OrderComparator} for details on the
 * sort semantics for non-ordered objects.
 *
 * {@code Ordered}是一个接口，可以由应该<em> orderable </ em>的对象实现，例如在{@code Collection}中。
 *
 * 实际的{@link #getOrder（）顺序}可以解释为优先级，第一个对象（具有最小的顺序值）具有最高优先级。
 *
 * 请注意，此接口还有一个priority标记：{@link PriorityOrdered}。
 * {@code PriorityOrdered}对象表示的优先级始终在 plain  {@link Ordered}对象表示的相同优先级值之前应用。
 *
 * 有关非排序对象的排序语义的详细信息，请查阅Javadoc中的{@link OrderComparator}。
 *
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 07.04.2003
 * @see PriorityOrdered
 * @see OrderComparator
 * @see org.springframework.core.annotation.Order
 * @see org.springframework.core.annotation.AnnotationAwareOrderComparator
 */
public interface Ordered {

	/**
	 * Useful constant for the highest precedence value.
	 * 最高优先级值的常数。（Integer的最小值）
	 * @see java.lang.Integer#MIN_VALUE
	 */
	int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

	/**
	 * Useful constant for the lowest precedence value.
	 * 最低优先级值的常数。（Integer的最大值）
	 * @see java.lang.Integer#MAX_VALUE
	 */
	int LOWEST_PRECEDENCE = Integer.MAX_VALUE;


	/**
	 * Get the order value of this object.
	 * <p>Higher values are interpreted as lower priority. As a consequence,
	 * the object with the lowest value has the highest priority (somewhat
	 * analogous to Servlet {@code load-on-startup} values).
	 * <p>Same order values will result in arbitrary sort positions for the
	 * affected objects.
	 *
	 * 获取此对象的优先级。
	 * 较高的值将被解释为较低的优先级。
	 * 作为结果，具有最低值的对象具有最高优先级（与Servlet {@code load-on-startup}值类似）。
	 * 相同的顺序值将导致受影响对象的任意排序位置。
	 *
	 * @return the order value  次序值
	 * @see #HIGHEST_PRECEDENCE
	 * @see #LOWEST_PRECEDENCE
	 */
	int getOrder();

}

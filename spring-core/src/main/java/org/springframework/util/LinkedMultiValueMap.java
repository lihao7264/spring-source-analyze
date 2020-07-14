/*
 * Copyright 2002-2020 the original author or authors.
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

package org.springframework.util;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Simple implementation of {@link MultiValueMap} that wraps a {@link LinkedHashMap},
 * storing multiple values in a {@link LinkedList}.
 *
 * <p>This Map implementation is generally not thread-safe. It is primarily designed
 * for data structures exposed from request objects, for use in a single thread only.
 *
 * {@link MultiValueMap}的简单实现，它包装了一个{@link LinkedHashMap}，
 * 并在{@link LinkedList}中存储了多个值。
 *
 * 此Map实现通常不是线程安全的。
 * 它主要设计用于从请求对象公开的数据结构，仅在单个线程中使用。
 *
 * @author Arjen Poutsma
 * @author Juergen Hoeller
 * @since 3.0
 * @param <K> the key type
 * @param <V> the value element type
 */
public class LinkedMultiValueMap<K, V> extends MultiValueMapAdapter<K, V> implements Serializable, Cloneable {

	private static final long serialVersionUID = 3801124242820219131L;


	/**
	 * Create a new LinkedMultiValueMap that wraps a {@link LinkedHashMap}.
	 * 创建一个包装了{@link LinkedHashMap}的新LinkedMultiValueMap。
	 */
	public LinkedMultiValueMap() {
		super(new LinkedHashMap<>());
	}

	/**
	 * Create a new LinkedMultiValueMap that wraps a {@link LinkedHashMap}
	 * with the given initial capacity.
	 * 创建一个新的LinkedMultiValueMap，该包装以给定的初始容量包装{@link LinkedHashMap}。
	 * @param initialCapacity the initial capacity  初始容量
	 */
	public LinkedMultiValueMap(int initialCapacity) {
		super(new LinkedHashMap<>(initialCapacity));
	}

	/**
	 * Copy constructor: Create a new LinkedMultiValueMap with the same mappings as
	 * the specified Map. Note that this will be a shallow copy; its value-holding
	 * List entries will get reused and therefore cannot get modified independently.
	 *
	 * 复制构造函数：使用与指定Map相同的映射创建一个新的LinkedMultiValueMap。
	 * 注意这将是一个浅拷贝； 其值保留列表条目将被重用，因此无法独立进行修改。
	 * @param otherMap the Map whose mappings are to be placed in this Map  要在Map中放置其映射的Map
	 * @see #clone()
	 * @see #deepCopy()
	 */
	public LinkedMultiValueMap(Map<K, List<V>> otherMap) {
		super(new LinkedHashMap<>(otherMap));
	}


	/**
	 * Create a deep copy of this Map.  创建此Map的深拷贝。
	 * @return a copy of this Map, including a copy of each value-holding List entry
	 * (consistently using an independent modifiable {@link LinkedList} for each entry)
	 * along the lines of {@code MultiValueMap.addAll} semantics
	 *  此Map的副本，包括每个值持有列表条目的副本（每个条目始终使用独立的可修改的{@link LinkedList}），
	 *  并沿{@code MultiValueMap.addAll}语义行。
	 *
	 * @since 4.2
	 * @see #addAll(MultiValueMap)
	 * @see #clone()
	 */
	public LinkedMultiValueMap<K, V> deepCopy() {
		LinkedMultiValueMap<K, V> copy = new LinkedMultiValueMap<>(size());
		forEach((key, values) -> copy.put(key, new LinkedList<>(values)));
		return copy;
	}

	/**
	 * Create a regular copy of this Map.
	 * 创建此Map的常规拷贝。
	 * @return a shallow copy of this Map, reusing this Map's value-holding List entries
	 * (even if some entries are shared or unmodifiable) along the lines of standard
	 * {@code Map.put} semantics
	 * @since 4.2
	 * @see #put(Object, List)
	 * @see #putAll(Map)
	 * @see LinkedMultiValueMap#LinkedMultiValueMap(Map)
	 * @see #deepCopy()
	 */
	@Override
	public LinkedMultiValueMap<K, V> clone() {
		return new LinkedMultiValueMap<>(this);
	}

}

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

package org.springframework.util;

import java.util.List;
import java.util.Map;

import org.springframework.lang.Nullable;

/**
 * Extension of the {@code Map} interface that stores multiple values.
 *
 * {@code Map}接口的扩展，用于存储多个值。
 * 它实际上就是一个 Map<K, List<V>>。
 *
 * @author Arjen Poutsma
 * @since 3.0
 * @param <K> the key type
 * @param <V> the value element type
 */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {

	/**
	 * Return the first value for the given key.
	 * 返回给定键的第一个值。
	 * @param key the key
	 * @return the first value for the specified key, or {@code null} if none
	 */
	@Nullable
	V getFirst(K key);

	/**
	 * Add the given single value to the current list of values for the given key.
	 * 将给定的单个值添加到给定键的当前值列表中。
	 * @param key the key
	 * @param value the value to be added
	 */
	void add(K key, @Nullable V value);

	/**
	 * Add all the values of the given list to the current list of values for the given key.
	 * 将给定列表的所有值添加到给定键的当前值列表中。
	 * @param key they key
	 * @param values the values to be added
	 * @since 5.0
	 */
	void addAll(K key, List<? extends V> values);

	/**
	 * Add all the values of the given {@code MultiValueMap} to the current values.
	 * 将给定{@code MultiValueMap}的所有值添加到当前值。
	 * @param values the values to be added
	 * @since 5.0
	 */
	void addAll(MultiValueMap<K, V> values);

	/**
	 * Set the given single value under the given key.
	 * 在给定的键下设置给定的单个值。
	 * @param key the key
	 * @param value the value to set
	 */
	void set(K key, @Nullable V value);

	/**
	 * Set the given values under.
	 * 在下面设置给定值。
	 * @param values the values.
	 */
	void setAll(Map<K, V> values);

	/**
	 * Return a {@code Map} with the first values contained in this {@code MultiValueMap}.
	 * 返回包含此{@code MultiValueMap}中的第一个值的{@code Map}。
	 * @return a single value representation of this map
	 */
	Map<K, V> toSingleValueMap();

}

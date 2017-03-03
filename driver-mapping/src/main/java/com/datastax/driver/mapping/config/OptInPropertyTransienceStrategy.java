/*
 *      Copyright (C) 2012-2015 DataStax Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.datastax.driver.mapping.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

/**
 * A {@link PropertyTransienceStrategy} that adopts a conservative, opt-in approach that
 * only considers a property to be non-transient if it is explicitly annotated
 * with one of the following annotations:
 * <ol>
 * <li>{@link com.datastax.driver.mapping.annotations.Column Column}</li>
 * <li>{@link com.datastax.driver.mapping.annotations.Computed Computed}</li>
 * <li>{@link com.datastax.driver.mapping.annotations.ClusteringColumn ClusteringColumn}</li>
 * <li>{@link com.datastax.driver.mapping.annotations.Frozen Frozen}</li>
 * <li>{@link com.datastax.driver.mapping.annotations.FrozenKey FrozenKey}</li>
 * <li>{@link com.datastax.driver.mapping.annotations.FrozenValue FrozenValue}</li>
 * <li>{@link com.datastax.driver.mapping.annotations.PartitionKey PartitionKey}</li>
 * <li>{@link com.datastax.driver.mapping.annotations.Field Field}</li>
 * </ol>
 */
public class OptInPropertyTransienceStrategy implements PropertyTransienceStrategy {

    @Override
    public boolean isTransient(Class<?> mappedClass, String propertyName, Field field, Method getter, Method setter, Map<Class<? extends Annotation>, Annotation> annotations) {
        return Collections.disjoint(annotations.keySet(), DefaultPropertyTransienceStrategy.NON_TRANSIENT_ANNOTATIONS);
    }

}

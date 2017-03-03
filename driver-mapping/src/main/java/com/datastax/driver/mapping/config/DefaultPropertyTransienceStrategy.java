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

import com.datastax.driver.mapping.annotations.*;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A {@link PropertyTransienceStrategy} that adopts a permissive, opt-out approach that
 * will consider a property to be non-transient by default, unless:
 * <ol>
 * <li>The property is annotated with {@link com.datastax.driver.mapping.annotations.Transient @Transient};</li>
 * <li>The corresponding field is non-null and is marked with the keyword {@code transient};</li>
 * <li>The property name has been explicitly black-listed.</li>
 * </ol>
 */
public class DefaultPropertyTransienceStrategy implements PropertyTransienceStrategy {

    static final Set<Class<?>> NON_TRANSIENT_ANNOTATIONS = ImmutableSet.<Class<?>>of(
            Column.class,
            PartitionKey.class,
            ClusteringColumn.class,
            com.datastax.driver.mapping.annotations.Field.class,
            Computed.class,
            Frozen.class,
            FrozenKey.class,
            FrozenValue.class
    );

    private static final HashSet<String> DEFAULT_TRANSIENT_PROPERTIES = Sets.newHashSet(
            "class",
            // JAVA-1279: exclude Groovy's metaClass property
            "metaClass"
    );

    private final Set<String> transientProperties;

    /**
     * Creates a new instance with the default set of transient properties.
     * <p/>
     * The default set comprises the following property names:
     * {@code class} and {@code metaClass}.
     * These properties pertain to the {@link Object} class –
     * {@code metaClass} being specific to the Groovy language.
     */
    public DefaultPropertyTransienceStrategy() {
        this(DEFAULT_TRANSIENT_PROPERTIES);
    }

    /**
     * Creates a new instance with the given set of transient properties.
     * <p/>
     * Property names provided here will be considered transient throughout
     * all the mappers created with this strategy;
     * if a more fine-grained tuning is required, it is also possible
     * to use the {@link com.datastax.driver.mapping.annotations.Transient @Transient} annotation
     * on a specific property.
     *
     * @param transientProperties a set of property names to exclude from mapping.
     */
    public DefaultPropertyTransienceStrategy(String... transientProperties) {
        this(Sets.newHashSet(transientProperties));
    }

    /**
     * Creates a new instance with the given set of transient properties.
     * <p/>
     * Property names provided here will be considered transient throughout
     * all the mappers created with this strategy;
     * if a more fine-grained tuning is required, it is also possible
     * to use the {@link com.datastax.driver.mapping.annotations.Transient @Transient} annotation
     * on a specific property.
     *
     * @param transientProperties a set of property names to exclude from mapping.
     */
    public DefaultPropertyTransienceStrategy(Set<String> transientProperties) {
        this.transientProperties = transientProperties;
    }

    @Override
    public boolean isTransient(Class<?> mappedClass, String propertyName, Field field, Method getter, Method setter, Map<Class<? extends Annotation>, Annotation> annotations) {
        return annotations.containsKey(Transient.class)
                || (field != null && Modifier.isTransient(field.getModifiers()))
                // If a property is both annotated with a non-transient annotation and declared transient in transientProperties,
                // annotations take precedence (the property will not be transient)
                || transientProperties.contains(propertyName) && Collections.disjoint(annotations.keySet(), NON_TRANSIENT_ANNOTATIONS);
    }

}

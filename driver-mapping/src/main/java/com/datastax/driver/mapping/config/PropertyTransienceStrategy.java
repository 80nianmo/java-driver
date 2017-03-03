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

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * A strategy to determine which properties are transient, and which aren't.
 * <p/>
 * Transient properties will be ignored, whereas non-transient
 * ones will be mapped to Cassandra columns or UDT fields.
 */
public interface PropertyTransienceStrategy {

    /**
     * Returns {@code true} if the given property is transient (i.e., non-mapped),
     * {@code false} otherwise.
     *
     * @param mappedClass  The mapped class; this is necessarily a class annotated with
     *                     either {@link com.datastax.driver.mapping.annotations.Table @Table} or
     *                     {@link com.datastax.driver.mapping.annotations.UDT @UDT}.
     * @param propertyName The property name, as inferred by the mapper; never {@code null}.
     * @param field        The property field.
     *                     May be {@code null}, but {@code field}, {@code getter} and {@code setter} cannot be all {@code null}.
     * @param getter       The property getter, as inferred by the {@link PropertyAccessStrategy#locateGetter(Class, PropertyDescriptor) access strategy}.
     *                     May be {@code null}, but {@code field}, {@code getter} and {@code setter} cannot be all {@code null}.
     * @param setter       The property setter, as inferred by the {@link PropertyAccessStrategy#locateSetter(Class, PropertyDescriptor) access strategy}.
     *                     May be {@code null}, but {@code field}, {@code getter} and {@code setter} cannot be all {@code null}.
     * @param annotations  The property's annotations, as inferred by the mapper; may be empty, but never {@code null}.
     *                     The map will include all annotations found on the field, the getter and the setter methods.
     * @return {@code true} if the given property is transient (i.e., non-mapped), {@code false} otherwise.
     */
    boolean isTransient(Class<?> mappedClass, String propertyName, Field field, Method getter, Method setter, Map<Class<? extends Annotation>, Annotation> annotations);

}

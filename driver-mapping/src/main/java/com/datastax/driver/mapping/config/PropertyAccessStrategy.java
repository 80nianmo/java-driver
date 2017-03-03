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
import java.lang.reflect.Method;

/**
 * A strategy to determine how mapped properties are read and written.
 */
public interface PropertyAccessStrategy {

    enum PropertyAccessMode {

        /**
         * Use field access exclusively; property getters and setters, even if available, will be ignored.
         * <p/>
         * Note that fields do not need to be public for this access mode to work;
         * the mapper will try to access them via reflection if necessary.
         */
        FIELDS,

        /**
         * Use getters and setters exclusively; property fields, even if available, will be ignored.
         */
        GETTERS_AND_SETTERS,

        /**
         * Try getters and setters first, if available, then field access, as a last resort.
         * This is de default access mode.
         * <p/>
         * Note that fields do not need to be public for this access mode to work;
         * the mapper will try to access them via reflection if necessary.
         */
        BOTH;

        /**
         * Returns {@code true} if field access is allowed, {@code false} otherwise.
         *
         * @return {@code true} if field access is allowed, {@code false} otherwise.
         */
        public boolean isFieldAccessAllowed() {
            return this == FIELDS || this == BOTH;
        }

        /**
         * Returns {@code true} if getter and setter access is allowed, {@code false} otherwise.
         *
         * @return {@code true} if getter and setter access is allowed, {@code false} otherwise.
         */
        public boolean isGetterSetterAccessAllowed() {
            return this == GETTERS_AND_SETTERS || this == BOTH;
        }

    }

    /**
     * Returns the {@link PropertyAccessMode} to use when reading or writing a property.
     *
     * @return the {@link PropertyAccessMode} to use when reading or writing a property.
     */
    PropertyAccessMode getPropertyAccessMode();

    /**
     * Locates a getter method for the given mapped class and given property.
     * <p/>
     * If this method returns {@code null} then no getter will be used to access the given property.
     * In this case, the property <em>must</em> have a corresponding readable field, otherwise
     * the mapper will throw an {@link IllegalArgumentException} when attempting to map this property.
     * <p/>
     * This method is never called if the {@link #getPropertyAccessMode() access mode} for this strategy
     * is set to {@link PropertyAccessMode#FIELDS FIELDS}.
     * <p/>
     * Most users should rely on the implementation provided in {@link DefaultPropertyAccessStrategy#locateGetter(Class, PropertyDescriptor)}.
     * It is however possible to return any non-standard method, as long as it does
     * not take parameters, and its return type is assignable to (and covariant with) the property's type.
     * <p/>
     * This might be particularly useful for boolean properties whose names are verbs, e.g. "{@code hasAccount}":
     * one could then return the non-standard method {@code boolean hasAccount()} as its getter.
     *
     * @param mappedClass The mapped class; this is necessarily a class annotated with
     *                  either {@link com.datastax.driver.mapping.annotations.Table @Table} or
     *                  {@link com.datastax.driver.mapping.annotations.UDT @UDT}.
     * @param property  The property to locate a getter for; never {@code null}.
     * @return The getter method for the given base class and given property, or {@code null} if no getter was found.
     */
    Method locateGetter(Class<?> mappedClass, PropertyDescriptor property);

    /**
     * Locates a setter method for the given mapped class and given property.
     * <p/>
     * If this method returns {@code null} then no setter will be used to access the given property.
     * In this case, the property <em>must</em> have a corresponding writable field, otherwise
     * the mapper will throw an {@link IllegalArgumentException} when attempting to map this property.
     * <p/>
     * This method is never called if the {@link #getPropertyAccessMode() access mode} for this strategy
     * is set to {@link PropertyAccessMode#FIELDS FIELDS}.
     * <p/>
     * Most users should rely on the implementation provided in {@link DefaultPropertyAccessStrategy#locateSetter(Class, PropertyDescriptor)}.
     * It is however possible to return any non-standard method, as long as it accepts one single parameter type
     * that is contravariant with the property's type.
     *
     * @param mappedClass The mapped class; this is necessarily a class annotated with
     *                  either {@link com.datastax.driver.mapping.annotations.Table @Table} or
     *                  {@link com.datastax.driver.mapping.annotations.UDT @UDT}.
     * @param property  The property to locate a setter for; never {@code null}.
     * @return The setter method for the given base class and given property, or {@code null} if no setter was found.
     */
    Method locateSetter(Class<?> mappedClass, PropertyDescriptor property);

}

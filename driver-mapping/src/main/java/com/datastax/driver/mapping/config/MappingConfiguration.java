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

/**
 * The configuration to use for the mappers.
 * <p/>
 * The following strategies can be configured:
 * <ul>
 * <li>{@link PropertyAccessStrategy property access strategy}: defines how to access mapped properties.</li>
 * <li>{@link PropertyTransienceStrategy property transience strategy}: defines whether or not a given property is transient.</li>
 * <li>{@link HierarchyScanStrategy hierarchy scanning strategy}: defines how to scan for mapped properties in parent classes.</li>
 * </ul>
 */
public class MappingConfiguration {

    /**
     * Returns a new {@link Builder} instance.
     *
     * @return a new {@link Builder} instance.
     */
    public static MappingConfiguration.Builder builder() {
        return new MappingConfiguration.Builder();
    }

    /**
     * Builder for {@link MappingConfiguration} instances.
     */
    public static class Builder {

        private PropertyAccessStrategy propertyAccessStrategy = new DefaultPropertyAccessStrategy();

        private PropertyTransienceStrategy propertyTransienceStrategy = new DefaultPropertyTransienceStrategy();

        private HierarchyScanStrategy hierarchyScanStrategy = new DefaultHierarchyScanStrategy();

        /**
         * Sets the {@link PropertyAccessStrategy property access strategy} to use.
         * The default is {@link DefaultPropertyAccessStrategy}.
         *
         * @param propertyAccessStrategy the {@link PropertyAccessStrategy property access strategy} to use.
         * @return this {@link Builder} instance (to allow for fluent builder pattern).
         */
        public Builder withPropertyAccessStrategy(PropertyAccessStrategy propertyAccessStrategy) {
            this.propertyAccessStrategy = propertyAccessStrategy;
            return this;
        }

        /**
         * Sets the {@link PropertyTransienceStrategy property transience strategy} to use.
         * The default is {@link DefaultPropertyTransienceStrategy}.
         *
         * @param propertyTransienceStrategy the {@link PropertyTransienceStrategy property transience strategy} to use.
         * @return this {@link Builder} instance (to allow for fluent builder pattern).
         */
        public Builder withPropertyTransienceStrategy(PropertyTransienceStrategy propertyTransienceStrategy) {
            this.propertyTransienceStrategy = propertyTransienceStrategy;
            return this;
        }

        /**
         * Sets the {@link HierarchyScanStrategy hierarchy scan strategy} to use.
         * The default is {@link DefaultHierarchyScanStrategy}.
         *
         * @param hierarchyScanStrategy the {@link HierarchyScanStrategy hierarchy scan strategy} to use.
         * @return this {@link Builder} instance (to allow for fluent builder pattern).
         */
        public Builder withHierarchyScanStrategy(HierarchyScanStrategy hierarchyScanStrategy) {
            this.hierarchyScanStrategy = hierarchyScanStrategy;
            return this;
        }

        /**
         * Builds a new instance of {@link MappingConfiguration} with this builder's
         * settings.
         *
         * @return a new instance of {@link MappingConfiguration}
         */
        public MappingConfiguration build() {
            return new MappingConfiguration(propertyAccessStrategy, propertyTransienceStrategy, hierarchyScanStrategy);
        }
    }

    private final PropertyAccessStrategy propertyAccessStrategy;

    private final PropertyTransienceStrategy propertyTransienceStrategy;

    private final HierarchyScanStrategy hierarchyScanStrategy;

    private MappingConfiguration(PropertyAccessStrategy propertyAccessStrategy, PropertyTransienceStrategy propertyTransienceStrategy, HierarchyScanStrategy hierarchyScanStrategy) {
        this.propertyAccessStrategy = propertyAccessStrategy;
        this.propertyTransienceStrategy = propertyTransienceStrategy;
        this.hierarchyScanStrategy = hierarchyScanStrategy;
    }

    /**
     * Returns the {@link PropertyAccessStrategy}.
     *
     * @return the {@link PropertyAccessStrategy}.
     */
    public PropertyAccessStrategy getPropertyAccessStrategy() {
        return propertyAccessStrategy;
    }

    /**
     * Returns the {@link PropertyTransienceStrategy}.
     *
     * @return the {@link PropertyTransienceStrategy}.
     */
    public PropertyTransienceStrategy getPropertyTransienceStrategy() {
        return propertyTransienceStrategy;
    }

    /**
     * Returns the {@link HierarchyScanStrategy}.
     *
     * @return the {@link HierarchyScanStrategy}.
     */
    public HierarchyScanStrategy getHierarchyScanStrategy() {
        return hierarchyScanStrategy;
    }

}

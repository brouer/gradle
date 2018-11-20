/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.internal.model;

import org.gradle.api.Named;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.provider.DefaultPropertyState;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.provider.SetProperty;
import org.gradle.api.reflect.ObjectInstantiationException;
import org.gradle.internal.reflect.Instantiator;

import java.util.concurrent.Callable;

public class InstantiatorBackedObjectFactory implements ObjectFactory {
    private final Instantiator instantiator;
    private final ProviderFactory providerFactory;

    public InstantiatorBackedObjectFactory(Instantiator instantiator, ProviderFactory providerFactory) {
        this.instantiator = instantiator;
        this.providerFactory = providerFactory;
    }

    @Override
    public <T extends Named> T named(Class<T> type, String name) throws ObjectInstantiationException {
        throw new UnsupportedOperationException("This ObjectFactory implementation does not support constructing named objects");
    }

    @Override
    public SourceDirectorySet sourceDirectorySet(String name, String displayName) {
        throw new UnsupportedOperationException("This ObjectFactory implementation does not support constructing source directory sets");
    }

    @Override
    public <T> Property<T> property(Class<T> valueType) {
        return new DefaultPropertyState<T>(valueType);
    }

    @Override
    public <T> Property<T> property(Class<T> valueType, Callable<? extends T> defaultValue) {
        Property<T> property = property(valueType);
        property.set(providerFactory.provider(defaultValue));
        return property;
    }

    @Override
    public <T> ListProperty<T> listProperty(Class<T> elementType) {
        return broken();
    }

    @Override
    public <T> SetProperty<T> setProperty(Class<T> elementType) {
        return broken();
    }

    @Override
    public DirectoryProperty directoryProperty() {
        return broken();
    }

    @Override
    public RegularFileProperty fileProperty() {
        return broken();
    }

    private <T> T broken() {
        throw new UnsupportedOperationException("This ObjectFactory implementation does not support constructing property objects");
    }

    @Override
    public <T> T newInstance(Class<? extends T> type, Object... parameters) throws ObjectInstantiationException {
        return instantiator.newInstance(type, parameters);
    }
}

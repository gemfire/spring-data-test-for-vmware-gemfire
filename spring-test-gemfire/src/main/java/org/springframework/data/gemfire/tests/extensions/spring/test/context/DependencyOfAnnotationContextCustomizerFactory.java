/*
 * Copyright 2017-2024 Broadcom. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package org.springframework.data.gemfire.tests.extensions.spring.test.context;

import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.test.context.TestContext;

/**
 * Spring {@link ContextCustomizerFactory} implementation to create a {@link DependencyOfAnnotationContextCustomizer}
 * used to customize the Spring {@link ConfigurableApplicationContext} created by
 * the Spring {@link TestContext} framework.
 *
 * @author John Blum
 * @see org.springframework.context.ConfigurableApplicationContext
 * @see org.springframework.data.gemfire.tests.extensions.spring.test.context.DependencyOfAnnotationContextCustomizer
 * @see org.springframework.test.context.ContextCustomizer
 * @see org.springframework.test.context.ContextCustomizerFactory
 * @since 0.0.23
 */
public class DependencyOfAnnotationContextCustomizerFactory implements ContextCustomizerFactory {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public @NonNull ContextCustomizer createContextCustomizer(@NonNull Class<?> testClass,
			@NonNull List<ContextConfigurationAttributes> configAttributes) {

		return new DependencyOfAnnotationContextCustomizer();
	}
}

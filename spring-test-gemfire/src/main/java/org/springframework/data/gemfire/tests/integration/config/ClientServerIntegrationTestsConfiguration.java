/*
 * Copyright 2017-2024 Broadcom. All rights reserved.
 * SPDX-License-Identifier: Apache-2.0
 */
package org.springframework.data.gemfire.tests.integration.config;

import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.Pool;
import org.apache.geode.cache.server.CacheServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.util.ClassUtils;

import java.util.Collections;
import java.util.List;

import static org.springframework.data.gemfire.tests.integration.ClientServerIntegrationTestsSupport.GEMFIRE_CACHE_SERVER_PORT_PROPERTY;

/**
 * The {@link ClientServerIntegrationTestsConfiguration} class is a Spring {@link Configuration} class that registers
 * a {@link ClientCacheConfigurer} used to configure the {@link ClientCache} {@literal DEFAULT} {@link Pool} port
 * to connect to the launched Apache Geode/VMware GemFire Server during integration testing.
 *
 *
 * Finally, this class provides a Spring {@link Configuration} class enable the embedded Locator and Manager services
 * in a server providing the {@literal locator-manager} Spring profile is activated.
 *
 * @author John Blum
 * @see org.apache.geode.cache.client.ClientCache
 * @see org.apache.geode.cache.client.Pool
 * @see org.apache.geode.cache.server.CacheServer
 * @see org.springframework.context.annotation.Bean
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.context.annotation.Profile
 * @see org.springframework.data.gemfire.config.annotation.ClientCacheConfigurer
 * @since 1.0.0
 */
@Configuration
@SuppressWarnings("unused")
public class ClientServerIntegrationTestsConfiguration {

	private static final int DEFAULT_PORT = CacheServer.DEFAULT_PORT;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected Logger getLogger() {
		return this.logger;
	}

	@Bean
	@Conditional(SpringBootIsAbsentCondition.class)
	// Required bean to resolve property placeholders in Spring @Value annotations.
	static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	ClientCacheConfigurer clientCachePoolPortConfigurer(
			@Value("${" + GEMFIRE_CACHE_SERVER_PORT_PROPERTY + ":" + DEFAULT_PORT + "}") int port) {

		return (beanName, clientCacheFactoryBean) -> {

			List<ConnectionEndpoint> servers =
				Collections.singletonList(new ConnectionEndpoint("localhost", port));

			clientCacheFactoryBean.setServers(servers);
		};
	}

	public static class SpringBootIsAbsentCondition implements Condition {

		@Override
		@SuppressWarnings("all")
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

			return !ClassUtils.isPresent("org.springframework.boot.SpringApplication",
				Thread.currentThread().getContextClassLoader());
		}
	}
}

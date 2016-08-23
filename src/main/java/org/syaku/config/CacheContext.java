package org.syaku.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 7. 22.
 */
@Configuration
@ComponentScan(
		basePackages = "org.syaku.service",
		includeFilters =  {
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class)
		}
)
@EnableCaching
public class CacheContext implements CachingConfigurer {

	@Bean(destroyMethod="shutdown")
	public net.sf.ehcache.CacheManager ehCacheManager() {
		/*
		CacheConfiguration cacheConfiguration = new CacheConfiguration();
		cacheConfiguration.setName("default");
		cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
		cacheConfiguration.setMaxEntriesLocalHeap(1000);

		net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
		config.addCache(cacheConfiguration);

		return net.sf.ehcache.CacheManager.newInstance(config);
		*/
		System.out.println("ewqewqewqewqewqe==================");
		EhcacheFactoryBean bean = new EhcacheFactoryBean();
		bean.setConfigLocations("classpath*:cache.*.properties");
		bean.afterPropertiesSet();

		return bean.getObject();
	}

	@Bean
	@Override
	public CacheManager cacheManager() {
		return new EhCacheCacheManager(ehCacheManager());
	}

	@Override
	public CacheResolver cacheResolver() {
		return null;
	}

	@Override
	public KeyGenerator keyGenerator() {
		return new SimpleKeyGenerator();
	}

	@Override
	public CacheErrorHandler errorHandler() {
		return null;
	}
}

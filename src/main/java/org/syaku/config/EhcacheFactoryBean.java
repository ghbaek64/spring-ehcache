package org.syaku.config;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.springframework.beans.factory.FactoryBean;
import org.syaku.util.PropertyUtils;

import java.util.Properties;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 7. 28.
 */
public class EhcacheFactoryBean implements FactoryBean<CacheManager> {
	private final Properties properties;

	public EhcacheFactoryBean(Properties properties) {
		this.properties = properties;
	}

	@Override
	public CacheManager getObject() {
		Configuration config = new Configuration();
		config.setDynamicConfig(true);
		config.setUpdateCheck(true);
		config.setMonitoring("autodetect");

		String[] cacheNames = PropertyUtils.getValues(properties.getProperty("cache.names"));
		for(String cacheName : cacheNames) {
			CacheConfiguration cacheConfiguration = new CacheConfiguration();

			String memoryStoreEvictionPolicy = properties.getProperty("cache." + cacheName + ".memoryStoreEvictionPolicy");
			int maxEntriesLocalHeap = Integer.parseInt(properties.getProperty("cache." + cacheName + ".maxEntriesLocalHeap"));
			boolean eternal = properties.getProperty("cache." + cacheName + ".eternal").equals("true") ? true : false;
			int timeToIdleSeconds = Integer.parseInt(properties.getProperty("cache." + cacheName + ".timeToIdleSeconds"));
			int timeToLiveSeconds = Integer.parseInt(properties.getProperty("cache." + cacheName + ".timeToLiveSeconds"));
			boolean loggin = properties.getProperty("cache." + cacheName + ".loggin").equals("true") ? true : false;

			cacheConfiguration.setName(cacheName);
			cacheConfiguration.setMemoryStoreEvictionPolicy(memoryStoreEvictionPolicy);
			cacheConfiguration.setMaxEntriesLocalHeap(maxEntriesLocalHeap);
			cacheConfiguration.setEternal(eternal);
			cacheConfiguration.setTimeToIdleSeconds(timeToIdleSeconds);
			cacheConfiguration.setTimeToLiveSeconds(timeToLiveSeconds);
			cacheConfiguration.setLogging(loggin);
			cacheConfiguration.persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP));

			config.addCache(cacheConfiguration);
		}

		return CacheManager.newInstance(config);
	}

	@Override
	public Class<CacheManager> getObjectType() {
		return CacheManager.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}

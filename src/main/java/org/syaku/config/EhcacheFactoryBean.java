package org.syaku.config;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.core.io.Resource;
import org.syaku.util.PathMatchingResourceResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 7. 28.
 */
public class EhcacheFactoryBean extends EhCacheManagerFactoryBean {
	private String[] configLocations;

	private String cacheManagerName = "cacheManager";

	private boolean acceptExisting = false;

	private boolean shared = false;

	private CacheManager cacheManager;

	private boolean locallyManaged = true;

	public void setConfigLocation(Resource configLocation) {
	}

	public void setConfigLocations(String configLocations) {
		if (configLocations != null) {
			this.configLocations = configLocations.split(",");
		}
	}

	public void setConfigLocations(String[] configLocations) {
		this.configLocations = configLocations;
	}

	@Override
	public void afterPropertiesSet() throws CacheException {
		logger.info("Initializing EhCache CacheManager");
		Configuration configuration;

		if (configLocations == null) {
			configuration = ConfigurationFactory.parseConfiguration();

		} else {
			configuration = new Configuration();

			configuration.setDynamicConfig(true);
			configuration.setUpdateCheck(true);
			configuration.setMonitoring("autodetect");

			try {
				PathMatchingResourceResolver resolver = new PathMatchingResourceResolver();
				Resource[] resources = resolver.getResources(configLocations);

				for(Resource resource : resources) {
					Properties properties = new Properties();
					properties.load(resource.getInputStream());

					CacheConfiguration cacheConfiguration = new CacheConfiguration();

					String cacheName = properties.getProperty("cacheName");
					String memoryStoreEvictionPolicy = properties.getProperty("memoryStoreEvictionPolicy", "LRU");
					int maxEntriesLocalHeap = Integer.parseInt(properties.getProperty("maxEntriesLocalHeap", "0"));
					boolean eternal = properties.getProperty("eternal", "false").equals("true") ? true : false;
					int timeToIdleSeconds = Integer.parseInt(properties.getProperty("timeToIdleSeconds", "0"));
					int timeToLiveSeconds = Integer.parseInt(properties.getProperty("timeToLiveSeconds", "0"));
					boolean loggin = properties.getProperty("loggin", "false").equals("true") ? true : false;

					cacheConfiguration.setName(cacheName);
					cacheConfiguration.setMemoryStoreEvictionPolicy(memoryStoreEvictionPolicy);
					cacheConfiguration.setMaxEntriesLocalHeap(maxEntriesLocalHeap);
					cacheConfiguration.setEternal(eternal);
					cacheConfiguration.setTimeToIdleSeconds(timeToIdleSeconds);
					cacheConfiguration.setTimeToLiveSeconds(timeToLiveSeconds);
					cacheConfiguration.setLogging(loggin);
					cacheConfiguration.persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP));

					configuration.addCache(cacheConfiguration);
				}
			} catch (IOException e) {
				throw new CacheException(e.getMessage(), e);
			}
		}

		if (this.cacheManagerName != null) {
			configuration.setName(this.cacheManagerName);
		}

		if (this.shared) {
			// Old-school EhCache singleton sharing...
			// No way to find out whether we actually created a new CacheManager
			// or just received an existing singleton reference.
			this.cacheManager = CacheManager.create(configuration);
		}
		else if (this.acceptExisting) {
			// EhCache 2.5+: Reusing an existing CacheManager of the same name.
			// Basically the same code as in CacheManager.getInstance(String),
			// just storing whether we're dealing with an existing instance.
			synchronized (CacheManager.class) {
				this.cacheManager = CacheManager.getCacheManager(this.cacheManagerName);
				if (this.cacheManager == null) {
					this.cacheManager = new CacheManager(configuration);
				}
				else {
					this.locallyManaged = false;
				}
			}
		}
		else {
			// Throwing an exception if a CacheManager of the same name exists already...
			this.cacheManager = new CacheManager(configuration);
		}
	}
}

package org.syaku.config;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Properties;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 7. 28.
 */
@Configuration
@ComponentScan(
		basePackages = "org.syaku.service",
		includeFilters =  {
				@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Service.class)
		}
)
public class ConfigContext {
	private Properties properties;

	@PostConstruct
	public void prepare() throws Exception {
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();

		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(pathMatchingResourcePatternResolver.getResource("classpath:config.properties"));
		propertiesFactoryBean.setIgnoreResourceNotFound(true); // 프로퍼티 파일이 없는 경우 무시한다.
		propertiesFactoryBean.setFileEncoding("utf-8");
		propertiesFactoryBean.setLocalOverride(true); // 중복의 프로퍼티인 경우 나중에 프로퍼티를 사용한다.
		propertiesFactoryBean.setSingleton(true); // 싱글톤 여부 기본값 true

		propertiesFactoryBean.afterPropertiesSet();

		properties = propertiesFactoryBean.getObject();
	}

	@Bean
	public Properties config() throws Exception {
		return properties;
	}
}

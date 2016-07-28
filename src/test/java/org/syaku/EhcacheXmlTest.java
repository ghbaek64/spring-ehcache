package org.syaku;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.syaku.config.XmlCacheContext;
import org.syaku.service.EhcacheAnnotationService;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 7. 22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { XmlCacheContext.class })
public class EhcacheXmlTest {

	@Autowired private CacheManager cacheManager;
	@Autowired private EhcacheAnnotationService ehcacheService;

	@Test
	@Ignore
	public void live() throws InterruptedException {
		Cache cache = cacheManager.getCache("test");

		// 캐시에 title 엘리먼트 추가
		cache.put("title", "ehcache testing...");

		// 캐시에 title 엘리먼트 호출
		System.out.println(cache.get("title").get());

		// 5초후 캐시에 title 엘리먼트 호출
		Thread.sleep(5000);
		System.out.println(cache.get("title").get());

		// 캐시에 title 엘리먼트 추가
		cache.put("title2", "ehcache good!!!");

		System.out.println(cache.get("title2").get());

		System.out.println(cache.get("title").get());

		// 10초후 캐시에 title 엘리먼트 호출 하지만 10초동안 캐시를 호출하지 않으면 캐시가 삭제되게 됨 결과는 null
		Thread.sleep(10000);
		System.out.println(cache.get("title"));
	}

	@Test
	@Ignore
	public void annotation() throws InterruptedException {
		// 현재 시간을 캐시에 저장한다.
		System.out.println("#1 ==========>" + ehcacheService.getDate());

		Thread.sleep(5000);
		// 저장된 캐시를 삭제하고 현재 시간을 캐시에 저장한다.
		System.out.println("#2 ==========>" + ehcacheService.evict());

		Thread.sleep(5000);
		// 저장되어 있는 캐시를 얻는 다. (이미 저장되어 있는 데이터가 있기 때문에)
		System.out.println("#3 ==========>" + ehcacheService.getDate());

		Thread.sleep(5000);
		// 저장되어 있는 캐시를 현재 시간으로 갱신한다.
		System.out.println("#4 ==========>" + ehcacheService.put());

		Thread.sleep(5000);
		// 저장되어 있는 캐시를 얻는 다. (이미 저장되어 있는 데이터가 있기 때문에)
		System.out.println("#5 ==========>" + ehcacheService.getDate());

		Thread.sleep(10000);
		// 캐시 갱신기간이 만료되어 삭제되었다. 그리고 현재 시간을 캐시에 저장한다.
		System.out.println("#6 ==========>" + ehcacheService.getDate());
	}

	@Test
	//@Ignore
	public void annotationKey() throws InterruptedException {
		// 현재시간을 캐시에 키 이름을 getDateKey로 하여 저장한다.
		System.out.println("#1 ==========>" + ehcacheService.getDateKey());
		Thread.sleep(5000);

		// 현재시간을 캐시에 키 이름을 getDateKey2로 하여 저장한다.
		System.out.println("#2 ==========>" + ehcacheService.getDateKey2());
		Thread.sleep(5000);

		// 캐시에 저장된 키 값이 getDateKey인 데이터를 출력한다. (이미 저장되어 있는 캐시를 가져온다.)
		System.out.println("#1 ==========>" + ehcacheService.getDateKey());
		Thread.sleep(5000);

		// 캐시에 저장된 키 값이 getDateKey2인 데이터를 출력한다. (이미 저장되어 있는 캐시를 가져온다.)
		System.out.println("#2 ==========>" + ehcacheService.getDateKey2());
	}
}

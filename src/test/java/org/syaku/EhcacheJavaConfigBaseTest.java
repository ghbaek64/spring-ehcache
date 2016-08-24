package org.syaku;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.syaku.config.CacheContext;

/**
 * Spring JAVA 설정 기반 테스트
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 7. 22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { CacheContext.class })
public class EhcacheJavaConfigBaseTest extends EhcacheTest {

}

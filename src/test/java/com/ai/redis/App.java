/**
 *
 */
package com.ai.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Class Name		: App<br>
 *
 * Description		: 这里记述class说明<br>
 *
 * @author liuhb
 * @version $Revision$
 * @see
 *
 */
@RunWith(SpringRunner.class)
@SpringJUnitConfig(locations = {"classpath:beans.xml"})
public class App {

    @Test
    public void test() {
        Assert.assertTrue(Boolean.TRUE);
    }
}

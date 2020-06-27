package org.xmc.common.utils;

import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;

class SleepUtilTest extends JUnitTestBase {
    @Test
    void testSleep() {
        SleepUtil.sleep(100);
    }
}
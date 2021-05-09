package io.github.matwein.xmc.common.utils;

import org.junit.jupiter.api.Test;
import io.github.matwein.xmc.JUnitTestBase;

class SleepUtilTest extends JUnitTestBase {
    @Test
    void testSleep() {
        SleepUtil.sleep(100);
    }
}
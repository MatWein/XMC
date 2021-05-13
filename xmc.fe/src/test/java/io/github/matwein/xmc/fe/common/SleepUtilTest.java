package io.github.matwein.xmc.fe.common;

import io.github.matwein.xmc.JUnitTestBase;
import org.junit.jupiter.api.Test;

class SleepUtilTest extends JUnitTestBase {
    @Test
    void testSleep() {
        SleepUtil.sleep(100);
    }
}
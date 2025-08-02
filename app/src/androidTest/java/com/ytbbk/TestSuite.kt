package com.ytbbk

import com.ytbbk.service.YouTubeBackgroundServiceInstrumentedTest
import com.ytbbk.ui.WebViewUITest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MainActivityInstrumentedTest::class,
    YouTubeBackgroundServiceInstrumentedTest::class,
    WebViewUITest::class
)
class InstrumentedTestSuite

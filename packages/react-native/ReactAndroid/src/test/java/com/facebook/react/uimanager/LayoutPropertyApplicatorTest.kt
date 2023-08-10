package com.facebook.react.uimanager

import android.util.DisplayMetrics
import com.facebook.react.bridge.JavaOnlyMap
import com.facebook.soloader.SoLoader
import com.facebook.yoga.YogaConfig
import com.facebook.yoga.YogaConfigJNIBase
import com.facebook.yoga.YogaNative
import com.facebook.yoga.YogaNode
import com.facebook.yoga.YogaNodeFactory
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.any
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.api.mockito.PowerMockito.mockStatic
import org.powermock.api.mockito.PowerMockito.spy
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.RobolectricTestRunner
import org.powermock.api.mockito.PowerMockito.`when` as whenever

@PrepareForTest(PixelUtil::class, ReactYogaConfigProvider::class, YogaNodeFactory::class, YogaNative::class, YogaNode::class)
@RunWith(RobolectricTestRunner::class)
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "androidx.*", "android.*")
//@Ignore // TODO T14964130
class LayoutPropertyApplicatorTest {

  @get:Rule var rule = PowerMockRule()

  @Before
  fun setUp() {
    // @fathonyfath test alternative
    SoLoader.setInTestMode();

    // @mateusz1913 test alternative
    mockStatic(
      ReactYogaConfigProvider::class.java,
      YogaNodeFactory::class.java,
      YogaNative::class.java
    )

    var yogaConfigMock = mock(YogaConfigJNIBase::class.java)
    var yogaNodeMock = mock(YogaNode::class.java)

    whenever(ReactYogaConfigProvider.get()).thenAnswer { invocation -> yogaConfigMock }
    whenever(YogaNodeFactory.create(any(YogaConfig::class.java))).thenAnswer { invocation -> yogaNodeMock }


    DisplayMetricsHolder.setWindowDisplayMetrics(DisplayMetrics())
    DisplayMetricsHolder.setScreenDisplayMetrics(DisplayMetrics())
  }

  @After
  fun teardown() {
    DisplayMetricsHolder.setWindowDisplayMetrics(null)
    DisplayMetricsHolder.setScreenDisplayMetrics(null)
  }

  fun buildStyles(vararg keysAndValues: Any?): ReactStylesDiffMap? {
    return ReactStylesDiffMap(JavaOnlyMap.of(*keysAndValues))
  }

  @Test
  fun testDimensions() {
    var reactShadowNode = spy(LayoutShadowNode())
    var map = spy(buildStyles("width", 10.0, "height", 10.0, "left", 10.0, "top", 10.0))
    reactShadowNode.updateProperties(map)
    verify(reactShadowNode).setStyleWidth(ArgumentMatchers.anyFloat())
    verify(map)?.getFloat(ArgumentMatchers.eq("width"), ArgumentMatchers.anyFloat())
    verify(reactShadowNode).setStyleHeight(ArgumentMatchers.anyFloat())
    verify(map)?.getFloat(ArgumentMatchers.eq("height"), ArgumentMatchers.anyFloat())
    verify(reactShadowNode)
      .setPosition(ArgumentMatchers.eq(Spacing.START), ArgumentMatchers.anyFloat())
    verify(map)?.getFloat(ArgumentMatchers.eq("left"), ArgumentMatchers.anyFloat())
    verify(reactShadowNode)
      .setPosition(ArgumentMatchers.eq(Spacing.TOP), ArgumentMatchers.anyFloat())
    verify(map)?.getFloat(ArgumentMatchers.eq("top"), ArgumentMatchers.anyFloat())
    reactShadowNode = spy(LayoutShadowNode())
    map = spy(buildStyles())
    reactShadowNode.updateProperties(map)
    verify(reactShadowNode, never()).setStyleWidth(ArgumentMatchers.anyFloat())
    verify(map, never())?.getFloat(ArgumentMatchers.eq("width"), ArgumentMatchers.anyFloat())
    verify(reactShadowNode, never()).setStyleHeight(ArgumentMatchers.anyFloat())
    verify(map, never())?.getFloat(ArgumentMatchers.eq("height"), ArgumentMatchers.anyFloat())
    verify(reactShadowNode, never()).setPosition(ArgumentMatchers.eq(Spacing.START), ArgumentMatchers.anyFloat())
    verify(map, never())?.getFloat(ArgumentMatchers.eq("left"), ArgumentMatchers.anyFloat())
    verify(reactShadowNode, never())
      .setPosition(ArgumentMatchers.eq(Spacing.TOP), ArgumentMatchers.anyFloat())
    verify(map, never())?.getFloat(ArgumentMatchers.eq("top"), ArgumentMatchers.anyFloat())
  }

}

package com.coppernic.benchmark

import androidx.benchmark.macro.ExperimentalBaselineProfilesApi
import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalBaselineProfilesApi::class)
@RunWith(AndroidJUnit4ClassRunner::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate():Unit = rule.collectBaselineProfile(
        "com.coppernic.mobility"
    ){
        startActivityAndWait()
//        device.wait(Until.hasObject(By.text(" Ingresar Código")),30_000)
    }
}
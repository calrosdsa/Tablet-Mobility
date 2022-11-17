package com.example.benchmark

import android.annotation.SuppressLint
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */

@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    //    @Test
//    @SuppressLint("SuspiciousIndentation")
    @SuppressLint("SuspiciousIndentation")
    private fun startup(mode:CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.coppernic.mobility",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        compilationMode = mode,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
        device.wait(Until.hasObject(By.res("ingreso")),5000)
        device.findObject(By.res("ingreso")).click()
        device.wait(Until.gone(By.res("ingreso")), 5000)
        device.wait(Until.hasObject(By.res("input")),1000)
        val input =device.findObject(By.res("input"))
            repeat(30){
//                input.text = ""
                input.text = "${input.text}01"
            }
        device.waitForIdle(3000)
    }

    @Test
    fun startupNoBaseline() = startup(CompilationMode.None())

    @Test
    fun startupBaselineProfile() = startup(CompilationMode.Partial())
}
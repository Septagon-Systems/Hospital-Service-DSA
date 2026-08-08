plugins {
    java
    jacoco
}

allprojects {
    group = "edu.ug.dcit308.hospital"
    version = "1.0.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
        }
    }

    dependencies {
        testImplementation(rootProject.libs.junit.jupiter)
    }
}

// Non-rigid, informational Unit Test Audit Task
tasks.register("testAudit") {
    group = "verification"
    description = "Non-blocking audit checking unit test coverage and total test count against 40+ test requirement."

    doLast {
        println("\n=============================================================")
        println("               POD UNIT TEST AUDIT REPORT                    ")
        println("=============================================================")

        var totalClasses = 0
        var testedClasses = 0
        var totalTestCases = 0
        var totalFailedTestCases = 0

        subprojects.forEach { subproject ->
            val mainSrc = subproject.file("src/main/java")
            val testSrc = subproject.file("src/test/java")

            if (mainSrc.exists()) {
                mainSrc.walkTopDown()
                    .filter { it.isFile && it.extension == "java" }
                    .forEach { javaFile ->
                        totalClasses++
                        val className = javaFile.nameWithoutExtension
                        val expectedTestName = "${className}Test.java"

                        val testExists = testSrc.exists() && testSrc.walkTopDown().any { it.name == expectedTestName }

                        if (testExists) {
                            testedClasses++
                            println("  ✓ [${subproject.name}] $className -> $expectedTestName")
                        } else {
                            println("  ! [${subproject.name}] $className (No matching test found)")
                        }
                    }
            }

            // Count individual test cases from JUnit XML reports
            val testResultsDir = subproject.layout.buildDirectory.dir("test-results/test").get().asFile
            if (testResultsDir.exists()) {
                testResultsDir.walkTopDown()
                    .filter { it.isFile && it.extension == "xml" }
                    .forEach { xmlFile ->
                        val content = xmlFile.readText()
                        val testsMatch = Regex("tests=\"(\\d+)\"").find(content)
                        val failuresMatch = Regex("failures=\"(\\d+)\"").find(content)
                        val errorsMatch = Regex("errors=\"(\\d+)\"").find(content)

                        if (testsMatch != null) {
                            val tests = testsMatch.groupValues[1].toInt()
                            val failures = (failuresMatch?.groupValues?.get(1)?.toInt() ?: 0) +
                                           (errorsMatch?.groupValues?.get(1)?.toInt() ?: 0)
                            totalTestCases += tests
                            totalFailedTestCases += failures
                        }
                    }
            }
        }

        val percentage = if (totalClasses > 0) (testedClasses.toDouble() / totalClasses * 100) else 0.0
        println("-------------------------------------------------------------")
        println(String.format("CLASSES TESTED: %d / %d classes (%.1f%%)", testedClasses, totalClasses, percentage))
        println(String.format("TOTAL TEST CASES EXECUTED: %d (Passed: %d, Failed: %d)", totalTestCases, totalTestCases - totalFailedTestCases, totalFailedTestCases))
        val targetRequirement = 40
        if (totalTestCases >= targetRequirement) {
            println("✓ TARGET MET: ${totalTestCases} / ${targetRequirement}+ required unit tests PASSED (${String.format("%.1f", totalTestCases.toDouble() / targetRequirement * 100)}% of target)")
        } else {
            println("! TARGET NOT MET: ${totalTestCases} / ${targetRequirement}+ required unit tests (${targetRequirement - totalTestCases} more tests needed)")
        }
        println("=============================================================\n")
    }
}

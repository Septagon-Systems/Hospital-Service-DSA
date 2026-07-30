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
        
        // Print detailed test execution logs for every test case
        testLogging {
            events("passed", "skipped", "failed")
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
        }

        // Print explicit list of every test method executed
        afterTest(Closure(this) {
            val descriptor = this.call() as org.gradle.api.tasks.testing.TestDescriptor
            val result = this.properties["result"] as org.gradle.api.tasks.testing.TestResult
            val symbol = when (result.resultType.name) {
                "SUCCESS" -> "  ✓ PASSED"
                "FAILURE" -> "  ✕ FAILED"
                else -> "  - SKIPPED"
            }
            logger.lifecycle("$symbol: [${descriptor.className}] > ${descriptor.name}")
        })
    }

    dependencies {
        testImplementation(rootProject.libs.junit.jupiter)
    }
}

// Non-rigid, informational Unit Test Audit Task
tasks.register("testAudit") {
    group = "verification"
    description = "Non-blocking audit checking for matching unit test classes across modules."

    doLast {
        println("\n=============================================================")
        println("               POD UNIT TEST AUDIT REPORT                    ")
        println("=============================================================")

        var totalClasses = 0
        var testedClasses = 0

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
        }

        val percentage = if (totalClasses > 0) (testedClasses.toDouble() / totalClasses * 100) else 0.0
        println("-------------------------------------------------------------")
        println(String.format("SUMMARY: %d / %d classes have unit tests (%.1f%%)", testedClasses, totalClasses, percentage))
        println("=============================================================\n")
    }
}

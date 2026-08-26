plugins {
    application
}

application {
    mainClass.set("edu.ug.dcit308.hospital.app.Main")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    workingDir = rootProject.projectDir
}

dependencies {
    implementation(project(":datastructures"))
    implementation(project(":algorithms"))
    implementation(project(":graph"))
    implementation(project(":scheduling"))
    implementation(project(":db"))
    implementation(project(":benchmark"))
    runtimeOnly(rootProject.libs.slf4j.nop)
}

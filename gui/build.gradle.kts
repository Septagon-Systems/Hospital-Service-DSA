plugins {
    application
}

application {
    mainClass.set("edu.ug.dcit308.hospital.gui.GuiMain")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
    workingDir = rootProject.projectDir
}

tasks.named<Test>("test") {
    workingDir = rootProject.projectDir
}

dependencies {
    implementation(project(":datastructures"))
    implementation(project(":algorithms"))
    implementation(project(":graph"))
    implementation(project(":scheduling"))
    implementation(project(":db"))
    implementation(project(":benchmark"))
    implementation(project(":app"))
    runtimeOnly(rootProject.libs.slf4j.nop)
}

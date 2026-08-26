dependencies {
    implementation(project(":datastructures"))
    implementation(rootProject.libs.sqlite.jdbc)
    runtimeOnly(rootProject.libs.slf4j.nop)
}

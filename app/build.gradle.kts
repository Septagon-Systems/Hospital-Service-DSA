plugins {
    application
}

application {
    mainClass.set("edu.ug.dcit308.hospital.app.Main")
}

dependencies {
    implementation(project(":datastructures"))
    implementation(project(":algorithms"))
    implementation(project(":graph"))
    implementation(project(":scheduling"))
    implementation(project(":db"))
    implementation(project(":benchmark"))
}

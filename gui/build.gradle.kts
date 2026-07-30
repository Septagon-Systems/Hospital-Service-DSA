// If we ever decide on gui
plugins {
    application
}

application {
    mainClass.set("edu.ug.dcit308.hospital.gui.GuiMain")
}

dependencies {
    implementation(project(":app"))
}

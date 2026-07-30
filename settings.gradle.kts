pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "Hospital-Service-DSA"

include("datastructures")
include("algorithms")
include("graph")
include("scheduling")
include("db")
include("benchmark")
include("app")
// include("gui")

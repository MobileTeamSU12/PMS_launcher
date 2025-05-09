pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        val flutterStorageUrl = System.getenv("FLUTTER_STORAGE_BASE_URL") ?: "https://storage.googleapis.com"
        maven("$flutterStorageUrl/download.flutter.io")
//        maven(url = "D:/Dev/PJ/pms/build/host/outputs/repo")
    }
}

rootProject.name = "flutter module launcher"

include(":app")
val filePath = settingsDir.parentFile.toString() + "/pms/.android/include_flutter.groovy"
apply(from = File(filePath))
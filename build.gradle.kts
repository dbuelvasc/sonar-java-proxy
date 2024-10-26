import net.ltgt.gradle.errorprone.errorprone

plugins {
    id("com.diffplug.spotless")
    id("com.github.ben-manes.versions") version "0.51.0"
    id("net.ltgt.errorprone") version "4.0.0"
    id("org.sonarqube") version "5.1.0.4882"
}

apply(from = "$rootDir/gradle/ci.gradle.kts")

subprojects {
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "org.zaproxy.common")
    apply(plugin = "net.ltgt.errorprone")

    spotless {
        kotlinGradle {
            ktlint()
        }
    }

    project.plugins.withType(JavaPlugin::class) {
        dependencies {
            "errorprone"("com.google.errorprone:error_prone_core:2.28.0")
        }
    }

    tasks.withType<JavaCompile>().configureEach {
        options.errorprone {
            disableAllChecks.set(true)
            error(
                "MissingOverride",
                "WildcardImport",
            )
        }
    }
}

sonarqube {
  properties {
    property("sonar.projectKey", "dbuelvasc_sonar-java-proxy")
    property("sonar.organization", "dbuelvasc")
    property("sonar.host.url", "https://sonarcloud.io")
    property("sonar.exclusions", "**/*.gradle.kts")
  }
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.5.31" // Add Kotlin plugin
  id("maven-publish")
  id("elect86.magik") version "0.3.2"
}

group = "net.runelite"
version = "21062020"

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

sourceSets {
  main {
    java.srcDirs("src")
  }
  test {
    java.srcDirs("test")
  }
}

repositories {
  jcenter()
}

dependencies {
  testImplementation("junit:junit:4.12")
  testImplementation("org.assertj:assertj-core:3.12.2")
}

tasks.named<Jar>("jar") {
  archiveFileName.set("fernflower-\${project.version}.jar")
  manifest {
    attributes["Main-Class"] = "org.jetbrains.java.decompiler.main.decompiler.ConsoleDecompiler"
  }
}

publishing {
  publications {
    // you can pass a name to overwrite the default "maven"
    // createGithubPublication("my-name")
    createGithubPublication { this: MavenPublication
      // if your project already defines `groupId`, `artifactId` and `version`, then you can skip these here
      groupId = "net.runelite"
      artifactId = "fernflower"
      version = "21062020"

      from(components["java"])
    }.github {
      // this adds another (snapshot) publication, copying from the previous one:
      // - gav coordinates
      // - component type (java, javaPlatform or war)
      // - name, by default appended with the `Snapshot` postfix,
      // eg: publishMavenPublicationToGithubRepository ->
      // ->  publishMavenSnapshotPublicationToGithubRepository
      addSnapshotPublication()
    }
  }
  // don't use `repositories.github(..)`, it won't work
  // the dsl construct is necessary to distinguish it from a consume-only repo
  repositories {
    // don't use github(domain: String), that's for fetching, it won't work for publishing
    github {
      // this is optional since `github` is the default value, but it determines
      // the token name to fetch and the consequent publishing task name
      // eg: publishMavenPublicationToGithubRepository
      name = "github"

      // this is mandatory instead: $owner/$repo on github domain
      domain = "Edgeville-OSRS/fernflower" // aka https://github.com/kotlin-graphics/mary
    }
  }
}
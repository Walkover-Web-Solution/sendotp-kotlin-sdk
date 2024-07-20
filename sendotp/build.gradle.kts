plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.gradleup.nmcp").version("0.0.8")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.msg91.sendotp"
    compileSdk = 34

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_17
        targetCompatibility =  JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

group = "com.msg91.lib"
version = "1.0.0"
val artifactName = "sendotp"
val artifactDescription = "MS91 SendOTP Library for seamless OTP verification."
val artifactUrl = "https://github.com/Walkover-Web-Solution/sendotp-kotlin-sdk"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = group.toString()
                artifactId = artifactName
                version = version

                pom {
                    name.set(artifactName)
                    description.set(artifactDescription)
                    url.set(artifactUrl)

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("ItsAmanOP")
                            name.set("Aman Karpentar")
                            email.set("amankumargourh@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/Walkover-Web-Solution/sendotp-kotlin-sdk.git")
                        developerConnection.set("scm:git:ssh://github.com:Walkover-Web-Solution/sendotp-kotlin-sdk.git")
                        url.set(artifactUrl)
                    }
                }
            }
        }
    }

    signing {

        val keyId = findProperty("signing.keyId") as String
        val password = findProperty("signing.password") as String
        val signingKey = findProperty("signing.armoredKey") as String

        useInMemoryPgpKeys(keyId, signingKey, password)
        sign(publishing.publications)
    }

    nmcp {
        publishAllPublications {
            val sonaTypeUseName = findProperty("SONATYPE_USER_NAME") as String
            val sonaTypePassword = findProperty("SONATYPE_PASSWORD") as String

            username = sonaTypeUseName
            password = sonaTypePassword
            publicationType = "USER_MANAGED"
        }
    }
}
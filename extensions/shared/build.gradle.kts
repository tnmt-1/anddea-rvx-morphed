import java.lang.Boolean.TRUE

plugins {
    alias(libs.plugins.protobuf)
}

extension {
    name = "extensions/shared.mpe"
}

android {
    namespace = "app.morphe.extension"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }

    buildTypes {
        release {
            isMinifyEnabled = TRUE

            // 'libj2v8.so' is already included in the patch.
            ndk {
                abiFilters.add("")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    compileOnly(libs.annotation)
    compileOnly(libs.preference)

    implementation(libs.collections4)
    implementation(libs.gson)
    implementation(libs.lang3)
    implementation(libs.okhttp3)
    implementation(libs.protobuf.javalite)

    //noinspection UseTomlInstead
    implementation("com.eclipsesource.j2v8:j2v8:6.3.4@aar")

    implementation(libs.nanohttpd)
    implementation(libs.protobuf.javalite)

    coreLibraryDesugaring(libs.desugar.jdk.libs)
    compileOnly(project(":extensions:shared:stub"))
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

val sharedExtensionOutput = layout.buildDirectory.file("morphe/extensions/shared.mpe")

tasks.register("verifySharedExtension") {
    dependsOn("syncExtension")
    inputs.file(sharedExtensionOutput)

    doLast {
        val classes = sharedExtensionOutput.get()
            .asFile
            .readText(Charsets.ISO_8859_1)

        check("Lapp/morphe/extension/shared/utils/Utils;" in classes) {
            "The shared extension does not contain Utils."
        }
    }
}

tasks.named("check") {
    dependsOn("verifySharedExtension")
}

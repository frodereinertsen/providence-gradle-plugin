package net.morimekta.providence.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin definition for the providence gradle plugin.
 */
class ProvidencePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        // Add the 'providence' extension object
        project.extensions.create("providence", ProvidenceExtension)

        project.task("generateProvidenceSources") {
            doLast {
                println "generate ${project.providence.sources}"
            }
        }
    }
}


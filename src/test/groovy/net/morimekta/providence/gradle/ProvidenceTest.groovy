package net.morimekta.providence.gradle

import org.apache.tools.ant.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

/**
 * TODO(steineldar): Make a proper class description.
 */
class ProvidenceTest {
    @Test
    public void testGenerateSources() {
        Project project = ProjectBuilder.bilder().builder();
        project.pluginManager.apply 'net.morimekta.providence.gradle'
    }
}

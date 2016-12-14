/*
 * Copyright (c) 2016, Stein Eldar Johnsen
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.junit.Assert.assertEquals

class GenerateProvidenceTaskTest {
    @Rule
    public TemporaryFolder tmp = new TemporaryFolder()

    private File buildFile
    private List<File> pluginClasspath

    private static final String VERSION = '0.3.0'

    @Before
    void setUp() {
        buildFile = tmp.newFile('build.gradle')

        def pluginClasspathResource = getClass().classLoader.findResource("plugin-classpath.txt")
        if (pluginClasspathResource == null) {
            throw new IllegalStateException("Did not find plugin classpath resource, run `testClasses` build task.")
        }

        pluginClasspath = pluginClasspathResource.readLines().collect { new File(it) }
    }

    @Test
    void testGenerateProvidence() {
        buildFile << """
plugins {
    id 'org.gradle.java'
    id 'net.morimekta.providence.gradle'
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compile 'net.morimekta.providence:providence-core:${VERSION}'
}
"""
        def pvd = tmp.newFolder('src', 'main', 'providence')
        new File(pvd, 'test.thrift') << """
namespace java net.morimekta.test.groovy

struct NeededMessage {
    1: i32 a_grip;
}
"""
        def src = tmp.newFolder('src', 'main', 'java')
        new File(src, 'Test.java') << """
import net.morimekta.test.groovy.NeededMessage;

public class Test {
    public static void main(String... args) {
        System.out.println(NeededMessage.builder().setAGrip(5).build().toString());
    }
}
"""

        BuildResult result = GradleRunner.create()
                .withProjectDir(tmp.root)
                .withArguments('compileJava')
                .withPluginClasspath(pluginClasspath)
                .build()

        assertEquals(result.task(':generateProvidence').getOutcome(), TaskOutcome.SUCCESS)
        assertEquals(result.task(':compileJava').getOutcome(), TaskOutcome.SUCCESS)
    }

    @Test
    void testGenerateProvidenceWithParams() {
        buildFile << """
plugins {
    id 'org.gradle.java'
    id 'net.morimekta.providence.gradle'
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compile 'net.morimekta.utils:android-util:0.3.11'
    compile 'net.morimekta.providence:providence-core:${VERSION}'
}

providence {
    main {
        input = fileTree('src/main/android')
        input.include '*.thrift'
        android = true
    }
}
"""
        def pvd = tmp.newFolder('src', 'main', 'android')
        new File(pvd, 'test.thrift') << """
namespace java net.morimekta.test.groovy

struct NeededMessage {
    1: i32 a_grip;
}
"""
        def src = tmp.newFolder('src', 'main', 'java')
        new File(src, 'Test.java') << """
import net.morimekta.test.groovy.NeededMessage;

public class Test {
    public static void main(String... args) {
        System.out.println(NeededMessage.builder().setAGrip(5).build().toString());
    }
}
"""

        BuildResult result = GradleRunner.create()
                .withProjectDir(tmp.root)
                .withArguments('compileJava')
                .withPluginClasspath(pluginClasspath)
                .build()

        assertEquals(result.task(':generateProvidence').getOutcome(), TaskOutcome.SUCCESS)
        assertEquals(result.task(':compileJava').getOutcome(), TaskOutcome.SUCCESS)
    }
}

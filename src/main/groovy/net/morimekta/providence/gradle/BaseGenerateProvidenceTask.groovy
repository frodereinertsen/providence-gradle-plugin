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
package net.morimekta.providence.gradle

import net.morimekta.providence.generator.format.java.JavaGenerator
import net.morimekta.providence.generator.format.java.JavaOptions
import net.morimekta.providence.reflect.TypeLoader
import net.morimekta.providence.reflect.parser.ThriftProgramParser
import net.morimekta.providence.reflect.util.ReflectionUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.file.FileTree

/**
 * Plugin definition for the providence gradle plugin.
 *
 * generateProvidence {
 *     include {
 *         dir('idl')
 *     }
 *     input {
 *         files('src/main/providence/*.thrift')
 *     }
 * }
 */
abstract class BaseGenerateProvidenceTask extends DefaultTask {
    protected FileTree _defaultInput

    protected abstract ProvidenceExtensionParams getExtension()

    Collection<File> getIncludedFiles() {
        if (getExtension().include == null) {
            return new LinkedList<File>()
        }
        getExtension().include.files
    }

    FileTree getInputFiles() {
        if (getExtension().input == null) {
            return _defaultInput
        }
        getExtension().input
    }

    JavaOptions getOptions() {
        JavaOptions options = new JavaOptions()
        options.android = getExtension().android
        options.jackson = getExtension().jackson
        return options
    }

    BaseGenerateProvidenceTask(boolean _testing) {
        def taskDependsOnThis = _testing ? 'compileTestJava' : 'compileJava'

        Task compile = project.tasks.getByName(taskDependsOnThis);
        if (!compile) {
            throw new NullPointerException('Error: providence plugin must be applied *after* the java plugin')
        }
        compile.dependsOn(this)

        def self = this

        doLast({
            def includes = self.getIncludedFiles()
            def files = self.getInputFiles()
            if (files.empty) {
                return
            }

            def manager = new GradleOutputFileManager(self.project, self.outputs, _testing)
            def parser = new ThriftProgramParser()
            def loader = new TypeLoader(includes, parser)
            def generator = new JavaGenerator(manager, loader.registry, self.getOptions())

            files.each { file ->
                if (ReflectionUtils.isThriftFile(file.name)) {
                    generator.generate(loader.load(file))
                }
            }
        })
    }
}

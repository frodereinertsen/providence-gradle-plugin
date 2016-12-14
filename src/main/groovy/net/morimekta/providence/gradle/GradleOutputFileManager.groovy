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

import net.morimekta.providence.generator.util.FileManager
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskOutputs

/**
 * ...
 */
class GradleOutputFileManager extends FileManager {
    GradleOutputFileManager(Project project, TaskOutputs outputs, boolean testing) {
        super(new File(outputs.files.asPath))
        SourceSetContainer sourceSets = (SourceSetContainer) project.properties.get('sourceSets')
        SourceSet sourceSet = sourceSets.getByName('main')
        if (testing) {
            sourceSet = sourceSets.getByName('test')
        }
        sourceSet.allJava.srcDir(outputs.files.asPath)
        sourceSet.java.srcDir(outputs.files.asPath)
    }
}

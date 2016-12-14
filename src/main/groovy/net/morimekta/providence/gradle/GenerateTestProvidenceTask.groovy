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

/**
 * Plugin definition for the providence gradle plugin.
 */
class GenerateTestProvidenceTask extends BaseGenerateProvidenceTask {
    GenerateTestProvidenceTask() {
        super(true)
        _defaultInput = project.fileTree("src/test/providence")
        _defaultInput.include '**/*.thrift'
        outputs.dir "${project.buildDir.name}/generated-test-sources/providence"

        project.tasks.getByName('compileTestJava').dependsOn(this)
    }

    @Override
    protected ProvidenceExtensionParams getExtension() {
        return ((ProvidenceExtension) project.extensions.getByName('providence')).test
    }
}

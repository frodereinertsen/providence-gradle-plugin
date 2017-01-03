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

import net.morimekta.providence.generator.format.java.JavaOptions
import org.gradle.api.file.FileTree

/**
 * Plugin definition for a specific task for the providence gradle plugin.
 *
 * <pre>{@code
 * providence {
 *     main {
 *         input = fileTree('src/main/android') {
 *             include '*.thrift'
 *         }
 *         include = fileTree('idl')
 *         android = true
 *         jackson = false
 *         rw_binary = true
 *     }
 * }
 * }</pre>
 */
class ProvidenceExtensionParams extends JavaOptions {
    /**
     * Input files to be generated from
     */
    FileTree input     = null

    /**
     * Files to be included from the source thrift, but not be generated.
     */
    FileTree include   = null
}

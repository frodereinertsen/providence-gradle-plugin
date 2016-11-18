package net.morimekta.providence.gradle

import org.gradle.api.file.FileTree

/**
 * The extension object that define where the providence / thrift files are,
 * and how to handle them.
 */
class ProvidenceBuildInfo {
    /**
     * Which version of providence to use. This is set as dynamic in order to
     * be able to avoid having to update the gradle plugin without also having
     * to release a new providence plugin to match.
     */
    def GString providenceVersion = "0.3.0-SNAPSHOT"

    def boolean android = false
    def boolean tiny = false
    def boolean jackson = false

    def FileTree sources = null;
    def FileTree includes = null;

    def final String defaultSourcePath = "src/main/providence"
}

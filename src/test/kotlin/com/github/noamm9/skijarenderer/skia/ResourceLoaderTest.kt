package com.github.noamm9.skijarenderer.skia

import java.io.FileNotFoundException
import java.nio.file.Files
import kotlin.io.path.writeText
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ResourceLoaderTest {
    @Test
    fun `normalizeLocation trims whitespace and leading slashes`() {
        assertEquals("testdata/resource-bytes.txt", ResourceLoader.normalizeLocation(" /testdata/resource-bytes.txt "))
    }

    @Test
    fun `read loads classpath resources`() {
        assertEquals("classpath-bytes", ResourceLoader.read("testdata/resource-bytes.txt").decodeToString().trimEnd())
    }

    @Test
    fun `read loads file system resources`() {
        val tempFile = Files.createTempFile("nvg-resource", ".txt")
        tempFile.writeText("filesystem-bytes")

        try {
            assertContentEquals("filesystem-bytes".encodeToByteArray(), ResourceLoader.read(tempFile.toString()))
        }
        finally {
            Files.deleteIfExists(tempFile)
        }
    }

    @Test
    fun `read throws for missing resources`() {
        assertFailsWith<FileNotFoundException> {
            ResourceLoader.read("testdata/does-not-exist.txt")
        }
    }

    @Test
    fun `read from url`() {
        assertContentEquals("hi mom!".encodeToByteArray(), ResourceLoader.read("https://gist.githubusercontent.com/Noamm9/56d1e4027315b07d0dd1c8b77efb50af/raw/91cd64fbb87bc2c3495f9a12a3c23f78821a4ea3/test"))
    }
}
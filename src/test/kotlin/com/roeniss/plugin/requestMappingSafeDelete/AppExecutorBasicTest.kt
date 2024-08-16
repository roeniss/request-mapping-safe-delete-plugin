package com.roeniss.plugin.requestMappingSafeDelete

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class AppExecutorBasicTest : BasePlatformTestCase() {

    fun test_single_class_RequestMapping_to_single_method_RequestMapping() {
        assertEquals(
            """
@RequestMapping("/a")
class TestController {
    @RequestMapping("/b")
    fun test() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @RequestMapping("/a/b")
    fun test() {
        // ...
    }
}
"""
        )
    }

    fun test_single_class_RequestMapping_to_multiple_method_RequestMapping() {
        assertEquals(
            """
@RequestMapping("/a")
class TestController {
    @RequestMapping("/b")
    fun test() {
        // ...
    }

    @RequestMapping("/c")
    fun test2() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @RequestMapping("/a/b")
    fun test() {
        // ...
    }

    @RequestMapping("/a/c")
    fun test2() {
        // ...
    }
}
"""
        )
    }

    fun test_single_class_RequestMapping_to_no_annotation_method() {
        assertEquals(
            """
@RequestMapping("/a")
class TestController {
    fun test() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @RequestMapping("/a") fun test() {
        // ...
    }
}
"""
        )
    }

    fun test_single_class_RequestMapping_to_empty_annotation_method() {
        assertEquals(
            """
@RequestMapping("/a")
class TestController {
    @RequestMapping()
    fun test() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @RequestMapping("/a")
    fun test() {
        // ...
    }
}
"""
        )
    }

    fun test_multiple_class_RequestMapping_to_multiple_annotation_method() {
        assertEquals(
            """
@RequestMapping("/a", "/b")
class TestController {
    @RequestMapping("/c", "/d")
    fun test() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @RequestMapping("/a/c", "/a/d", "/b/c", "/b/d")
    fun test() {
        // ...
    }
}
"""
        )
    }

    fun test_delete_RequestMapping_when_no_parenthesis() {
        assertEquals(
            """
@RequestMapping
class TestController
""".execute(project),

            """
class TestController
""",
        )
    }

    fun test_delete_RequestMapping_when_empty_parenthesis() {

        assertEquals(
            """
@RequestMapping()
class TestController
""".execute(project),

            """
class TestController
"""
        )
    }
}

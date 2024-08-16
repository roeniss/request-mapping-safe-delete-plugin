package com.roeniss.plugin.requestMappingSafeDelete

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class AppExecutorAnnotationTest : BasePlatformTestCase() {

    fun test_single_class_RequestMapping_to_single_method_GetMapping() {
        assertEquals(
            """
@RequestMapping("/a")
class TestController {
    @GetMapping("/b")
    fun test() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @GetMapping("/a/b")
    fun test() {
        // ...
    }
}
"""
        )
    }

    // multiple == multiple values
    fun test_multiple_class_RequestMapping_to_no_annotation_method() {
        assertEquals(
            """
@RequestMapping("/a", "/b", "/c")
class TestController {
    @RequestMapping
    fun test() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @RequestMapping("/a", "/b", "/c")
    fun test() {
        // ...
    }
}
"""
        )
    }

    fun test_single_class_RequestMapping_to_single_method_PostMapping() {
        assertEquals(
            """
@RequestMapping("/a")
class TestController {
    @PostMapping("/b")
    fun test() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @PostMapping("/a/b")
    fun test() {
        // ...
    }
}
"""
        )
    }

    fun test_single_class_RequestMapping_to_single_method_PutMapping() {
        assertEquals(
            """
@RequestMapping("/a")
class TestController {
    @PutMapping("/b")
    fun test() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @PutMapping("/a/b")
    fun test() {
        // ...
    }
}
"""
        )
    }

    fun test_single_class_RequestMapping_to_single_method_PatchMapping() {
        assertEquals(
            """
@RequestMapping("/a")
class TestController {
    @PatchMapping("/b")
    fun test() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @PatchMapping("/a/b")
    fun test() {
        // ...
    }
}
"""
        )
    }

    fun test_single_class_RequestMapping_to_single_method_DeleteMapping() {
        assertEquals(
            """
@RequestMapping("/a")
class TestController {
    @DeleteMapping("/b")
    fun test() {
        // ...
    }
}
""".execute(project),

            """
class TestController {
    @DeleteMapping("/a/b")
    fun test() {
        // ...
    }
}
"""
        )
    }
}

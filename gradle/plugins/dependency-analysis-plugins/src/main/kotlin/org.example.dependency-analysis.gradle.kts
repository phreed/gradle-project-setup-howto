import org.example.dependencyanalysis.configurationPrefixesToSkip

// Install a check during dependency resolution that makes sure that 'dependencies' and 'dependency constraints'
// are defined in alphabetical order.
configurations.all {
    if (configurationPrefixesToSkip.any { name.startsWith(it) }) {
        return@all
    }

    val configuration = this
    withDependencies {
        val declaredInBuildFile = filter {
            // Ignore Gradle internal dependency types - like gradleApi()
            (it is ProjectDependency || it is ExternalModuleDependency) &&
            // Ignore dependencies to another variant of the project itself - used by test fixtures
            !(it is ProjectDependency && it.dependencyProject == project) &&
            // Ignore the platform dependencies added in plugins
            it.name != "platform"
        }
        val sortedProject = declaredInBuildFile.filterIsInstance<ProjectDependency>().sortedBy { it.group + ":" + it.name }
        val sortedExternal = declaredInBuildFile.filterIsInstance<ExternalModuleDependency>().sortedBy { it.group + ":" + it.name }
        if (declaredInBuildFile != sortedProject + sortedExternal) {
            throw RuntimeException("""
                    ${project.name}/build.gradle.kts
                    
                    ${configuration.name} dependencies are not declared in alphabetical order. Please use this order:
                        ${sortedProject.joinToString("\n                        ") {"${configuration.name}(project(\":${it.name}\"))"}}
                        ${sortedExternal.joinToString("\n                        ") {"${configuration.name}(\"${it.group}:${it.name}\")"}}
            """.trimIndent())
        }

        val declaredConstraints = configuration.dependencyConstraints.toList()
        val sortedConstraints = configuration.dependencyConstraints.sortedBy { it.group + ":" + it.name }
        if (declaredConstraints != sortedConstraints) {
            throw RuntimeException("""
                    ${project.name}/build.gradle.kts
                    
                    ${configuration.name} dependencies constraints are not declared in alphabetical order. Please use this order:
                        ${sortedConstraints.joinToString("\n                        ") {"${configuration.name}(\"${it.group}:${it.name}:${it.version}\")"}}
            """.trimIndent())
        }
    }
}

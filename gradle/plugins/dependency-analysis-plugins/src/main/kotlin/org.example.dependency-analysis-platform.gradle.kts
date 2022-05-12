plugins {
    id("java-platform")
    id("org.example.dependency-analysis")
}

// Install a task that checks if new versions are available for what is declared in the platform
tasks.register("checkForDependencyVersionUpgrades") {
    group = HelpTasksPlugin.HELP_GROUP
    doLast {
        val api = configurations.api.get()
        val resolver = configurations.create("dependencyVersionUpgrades") {
            extendsFrom(api)
            isCanBeResolved = true
            isCanBeConsumed = false
        }
        api.dependencies.forEach {
            dependencies.add(resolver.name, dependencies.platform("${it.group}:${it.name}:latest.release") as ModuleDependency) {
                isTransitive = false
            }
        }
        api.dependencyConstraints.forEach {
            dependencies.add(resolver.name, "${it.group}:${it.name}:latest.release") {
                isTransitive = false
            }
        }

        val platformDependencyUpgrades = api.dependencies.filter { declared -> declared.resolvedVersion(resolver) != declared.version }
        val constraintUpgrades = api.dependencyConstraints.filter { declared -> declared.resolvedVersion(resolver) != declared.version }
        if (platformDependencyUpgrades.isNotEmpty() || constraintUpgrades.isNotEmpty()) {
            throw RuntimeException("""
                    ${project.name}/build.gradle.kts
                    
                    The following dependency versions should be upgraded in 'gradle/platform/build.gradle.kts' (dependencies {} block):
                    
                        ${platformDependencyUpgrades.joinToString("\n                        ") { "api(platform(\"${it.group}:${it.name}:${it.resolvedVersion(resolver)}\"))" }}    
                    
                    The following dependency versions should be upgraded in 'gradle/platform/build.gradle.kts' (dependencies.constraints {} block):
                    
                        ${constraintUpgrades.joinToString("\n                        ") { "api(\"${it.group}:${it.name}:${it.resolvedVersion(resolver)}\")" }}
                    
                    If we cannot perform an upgrade, please add a '{ version { reject("...") } }' statement and a comment
                    for the versions we cannot support at the moment.
                """.trimIndent())
        }
    }
}

fun Dependency.resolvedVersion(resolver: Configuration) =
    resolver.incoming.resolutionResult.allComponents.find {
        it.moduleVersion!!.group == group && it.moduleVersion!!.name == name
    }?.moduleVersion?.version

fun DependencyConstraint.resolvedVersion(resolver: Configuration) =
    resolver.incoming.resolutionResult.allComponents.find {
        it.moduleVersion!!.group == group && it.moduleVersion!!.name == name
    }?.moduleVersion?.version ?: version // if no fitting version could be determined, return the declared one

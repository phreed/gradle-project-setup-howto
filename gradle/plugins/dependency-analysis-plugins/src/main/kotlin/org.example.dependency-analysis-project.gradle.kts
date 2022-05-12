import com.autonomousapps.AbstractPostProcessingTask
import com.autonomousapps.DependencyAnalysisSubExtension
import com.autonomousapps.model.Advice
import com.autonomousapps.model.ProjectCoordinates
import org.example.dependencyanalysis.configurationPrefixesToSkip

plugins {
    id("base")
    id("com.autonomousapps.dependency-analysis")
    id("org.example.dependency-analysis")
}

// Check that dependencies are always declared without version.
configurations.all {
    if (configurationPrefixesToSkip.any { name.startsWith(it) }) {
        return@all
    }

    val configuration = this
    withDependencies {
        forEach { dependency ->
            if (dependency is ExternalModuleDependency && !dependency.version.isNullOrEmpty()) {
                throw RuntimeException("""
                    ${project.name}/build.gradle.kts
                    
                    Dependencies with versions are not allowed. Please declare the dependency as follows:
                    
                        ${configuration.name}("${dependency.group}:${dependency.name}")
                    
                    All versions must be declared in 'gradle/platform'.
                    If the version is not yet defined there, add the following to 'gradle-platform-external/build.gradle.kts':
                    
                        api("${dependency.group}:${dependency.name}:${dependency.version}")
                """.trimIndent())
            }
        }
    }
}

// Configure a 'checkDependencyScopes' tasks that sued the 'com.autonomousapps.dependency-analysis' plugin
// To find unused dependencies and check 'api' vs. 'implementation' scopes.
val dependencyScopesCheck = tasks.register<AbstractPostProcessingTask>("checkDependencyScopes") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    doLast {
        val projectAdvice = projectAdvice().dependencyAdvice
        if (projectAdvice.isNotEmpty()) {
            val toAdd = projectAdvice.filter { it.toConfiguration != null && it.toConfiguration != "runtimeOnly" }.map { it.declaration(it.toConfiguration) }.sorted()
            val toRemove = projectAdvice.filter { it.fromConfiguration != null }.map { it.declaration(it.fromConfiguration) }.sorted()

            throw RuntimeException("""
                    ${projectAdvice().projectPath.substring(1)}/build.gradle.kts
                    
                    Please add the following dependency declarations: 
                        ${toAdd.joinToString("\n                        ")  { it }}
                    
                    Please remove the following dependency declarations: 
                        ${toRemove.joinToString("\n                        ")  { it }}
                """.trimIndent())
        }
    }
}

fun Advice.declaration(conf: String?) =
    if (coordinates is ProjectCoordinates) "${conf}(project(\"${coordinates.identifier}\"))"
    else "${conf}(\"${coordinates.identifier}\")"

tasks.check {
    dependsOn(dependencyScopesCheck)
}

if (project.name != "gradle-kotlin-dsl-accessors") { // https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/issues/241
    extensions.getByType<DependencyAnalysisSubExtension>().registerPostProcessingTask(dependencyScopesCheck)
}

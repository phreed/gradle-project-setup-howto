import com.autonomousapps.AbstractPostProcessingTask
import com.autonomousapps.DependencyAnalysisSubExtension
import com.autonomousapps.model.Advice
import com.autonomousapps.model.ProjectCoordinates
import org.example.dependencyanalysis.configurationPrefixesToSkip

plugins {
    id("base")
    id("com.autonomousapps.dependency-analysis")
    id("de.jjohannes.java-module-dependencies")
}

configurations.all {
    if (configurationPrefixesToSkip.any { name.startsWith(it) }) {
        return@all
    }

    val configuration = this
    withDependencies {
        val splitName = configuration.name.splitConfigurationName()
        if (splitName != null) {
            val (sourceSet, directive) = splitName
            val declaredInModuleFile = filter {
                // Ignore Gradle internal dependency types - like gradleApi()
                (it is ProjectDependency || it is ExternalModuleDependency) &&
                        // Ignore dependencies to another variant of the project itself - used by test fixtures
                        !(it is ProjectDependency && it.dependencyProject == project) &&
                        // Ignore the platform dependencies declared in 'dependency-rules'
                        it.name != "platform"
            }
            val sortedProject = declaredInModuleFile.filterIsInstance<ProjectDependency>().sortedBy {
                it.group + "." + it.name
            }
            val sortedExternal = declaredInModuleFile.filterIsInstance<ExternalModuleDependency>().sortedBy {
                javaModuleDependencies.moduleName(it.group + ":" + it.name) ?: it.name
            }
            if (declaredInModuleFile != sortedProject + sortedExternal) {
                throw RuntimeException("""
                    ${project.name}/src/${sourceSet}/java/module-info.java
                    
                    $directive dependencies are not declared in alphabetical order. Please use this order:
                        ${sortedProject.map { it.group + "." + it.name }.joinToString("\n                        ") {"$directive $it;"}}
                        
                        ${sortedExternal.map { javaModuleDependencies.moduleName(it.group + ":" + it.name) ?: it.name }.joinToString("\n                        ") {"$directive $it;"}}
            """.trimIndent())
            }
        }
    }
}

val dependencyScopesCheck = tasks.register<AbstractPostProcessingTask>("checkDependencyScopes") {
    doLast {
        val projectAdvice = projectAdvice().dependencyAdvice
        listOf("main", "test").forEach { sourceSet -> // analysis plugin only supports these two source sets right now
            val toAdd = projectAdvice.filter { it.toConfiguration != null && it.toConfiguration != "runtimeOnly" }.filter {
                it.toConfiguration?.splitConfigurationName()?.first == sourceSet
            }.map { it.declaration(it.toConfiguration!!) }.sorted()
            val toRemove = projectAdvice.filter { it.fromConfiguration != null }.filter {
                it.fromConfiguration?.splitConfigurationName()?.first == sourceSet
            }.map { it.declaration(it.fromConfiguration!!) }.sorted()

            if (toAdd.isNotEmpty() || toRemove.isNotEmpty()) {
                throw RuntimeException("""
                    ${projectAdvice().projectPath.substring(1)}/src/${sourceSet}/java/module-info.java
                    
                    Please add the following requires directives: 
                        ${toAdd.joinToString("\n                        ")  { it }}
                    
                    Please remove the following requires directives: 
                        ${toRemove.joinToString("\n                        ")  { it }}
                """.trimIndent())
            }
        }
    }
}

val directives = mapOf(
    "compileOnlyApi" to "requires static transitive",
    "compileOnly" to "requires static",
    "api" to "requires transitive",
    "implementation" to "requires",
)

fun String.splitConfigurationName(): Pair<String, String>? {
    val match = directives.filterKeys { endsWith(suffix = it, ignoreCase = true) }.asSequence().firstOrNull() ?: return null
    return Pair(substring(0, length - match.key.length).ifEmpty { "main" }, match.value)
}

fun Advice.declaration(conf: String) =
    if (coordinates is ProjectCoordinates) "${group}.${coordinates.identifier.replace(':', '.')};"
    else if (conf == "runtimeOnly") "// ${conf}(\"${coordinates.identifier}\") <-- add to build.gradle.kts"
    else "${conf.splitConfigurationName()!!.second} ${javaModuleDependencies.moduleName(coordinates.identifier)};"

tasks.check {
    dependsOn(dependencyScopesCheck)
}

if (project.name != "gradle-kotlin-dsl-accessors") { // https://github.com/autonomousapps/dependency-analysis-android-gradle-plugin/issues/241
    extensions.getByType<DependencyAnalysisSubExtension>().registerPostProcessingTask(dependencyScopesCheck)
}

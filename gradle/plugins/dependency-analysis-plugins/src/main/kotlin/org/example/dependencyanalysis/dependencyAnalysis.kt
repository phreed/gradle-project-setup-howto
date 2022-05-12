package org.example.dependencyanalysis

/**
 * Configurations to ignore when analysing dependency declaration order and format.
 */
val configurationPrefixesToSkip = listOf(
    // Configurations internally used by plugins
    "dependencyVersionUpgrades",
    "embeddedKotlin",
    "jacoco",
    "junitXmlToHtml",
    "kotlin",
    // Exclude 'testRuntimeOnly' as it is filled in our own plugins
    "testRuntimeOnly"
)

plugins {
    id("de.jjohannes.extra-java-module-info")
    id("de.jjohannes.java-module-dependencies")
}

extraJavaModuleInfo {
    failOnMissingModuleInfo.set(false)

    module("com.google.code.findbugs:jsr305", "javax.annotations.jsr305") {
        exports("javax.annotation")
    }
    module("org.apache.velocity:velocity-engine-core", "velocity.engine.core") {
        exports("org.apache.velocity.io")
    }

    // Automatic modules - they will see their transitive dependencies in the UNNAMED module (classpath)
    automaticModule("org.opensaml:opensaml", "org.opensaml")
    automaticModule("org.reflections:reflections", "org.reflections")
    automaticModule("org.jboss.resteasy:resteasy-core", "org.jboss.resteasy.core")
    automaticModule("org.jboss.resteasy:resteasy-guice", "org.jboss.resteasy.guice")
    automaticModule("org.jboss.resteasy:resteasy-jackson2-provider", "org.jboss.resteasy.jackson2.provider")
    automaticModule("org.apache.solr:solr-solrj", "org.apache.solr.solrj")
    automaticModule("com.github.racc:typesafeconfig-guice", "com.github.racc.typesafeconfigguice")

    automaticModule("org.apache.commons:commons-math3", "commons.math3") // required by org.apache.poi.ooxml
    automaticModule("com.zaxxer:SparseBitSet", "SparseBitSet") // required by org.apache.poi.poi

    // Merged
    automaticModule("org.apache.zookeeper:zookeeper", "org.apache.zookeeper") {
        mergeJar("org.apache.zookeeper:zookeeper-jute")
    }
}

javaModuleDependencies {
    // Override because there are multiple options
    moduleNameToGA.put("jakarta.activation", "com.sun.activation:jakarta.activation")
    moduleNameToGA.put("jakarta.mail", "com.sun.mail:jakarta.mail")
}

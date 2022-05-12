module org.example.product.coruscant {
    exports org.example.product.coruscant;

    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive com.google.common;
    requires transitive java.inject;
    requires transitive org.json;
    requires transitive org.opensaml;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.datatype.jsonorg;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires jakarta.activation;
    requires jakarta.mail;
    requires org.apache.httpcomponents.httpclient.fluent;
    requires org.reflections;
    requires org.slf4j;
}

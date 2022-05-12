module org.example.product.kamino {
    exports org.example.product.kamino;
    requires transitive org.example.product.coruscant;
    requires transitive org.jboss.resteasy.core;

    requires org.jboss.resteasy.guice;
    requires org.jboss.resteasy.jackson2.provider;

}

module org.example.product.app {
    exports org.example.product.app;

    requires org.example.product.bespin;
    requires org.example.product.corellia;
    requires org.example.product.kamino;
    requires org.example.product.kashyyyk;
    requires org.example.product.naboo;
    requires org.example.product.tatooine;

    requires com.google.guice;
    requires com.google.guice.extensions.servlet;
    requires java.servlet;
    requires org.slf4j;
}

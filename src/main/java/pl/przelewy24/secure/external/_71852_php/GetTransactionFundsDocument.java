/*
 * An XML document type.
 * Localname: GetTransactionFunds
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php;


/**
 * A document containing one GetTransactionFunds(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public interface GetTransactionFundsDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(GetTransactionFundsDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C109014B664CDB4FCB4E98E343B54AA").resolveHandle("gettransactionfunds6805doctype");
    
    /**
     * Gets the "GetTransactionFunds" element
     */
    pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument.GetTransactionFunds getGetTransactionFunds();
    
    /**
     * Sets the "GetTransactionFunds" element
     */
    void setGetTransactionFunds(pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument.GetTransactionFunds getTransactionFunds);
    
    /**
     * Appends and returns a new empty "GetTransactionFunds" element
     */
    pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument.GetTransactionFunds addNewGetTransactionFunds();
    
    /**
     * An XML GetTransactionFunds(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public interface GetTransactionFunds extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(GetTransactionFunds.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C109014B664CDB4FCB4E98E343B54AA").resolveHandle("gettransactionfundsb057elemtype");
        
        /**
         * Gets the "login" element
         */
        java.lang.String getLogin();
        
        /**
         * Gets (as xml) the "login" element
         */
        org.apache.xmlbeans.XmlString xgetLogin();
        
        /**
         * Tests for nil "login" element
         */
        boolean isNilLogin();
        
        /**
         * Sets the "login" element
         */
        void setLogin(java.lang.String login);
        
        /**
         * Sets (as xml) the "login" element
         */
        void xsetLogin(org.apache.xmlbeans.XmlString login);
        
        /**
         * Nils the "login" element
         */
        void setNilLogin();
        
        /**
         * Gets the "pass" element
         */
        java.lang.String getPass();
        
        /**
         * Gets (as xml) the "pass" element
         */
        org.apache.xmlbeans.XmlString xgetPass();
        
        /**
         * Tests for nil "pass" element
         */
        boolean isNilPass();
        
        /**
         * Sets the "pass" element
         */
        void setPass(java.lang.String pass);
        
        /**
         * Sets (as xml) the "pass" element
         */
        void xsetPass(org.apache.xmlbeans.XmlString pass);
        
        /**
         * Nils the "pass" element
         */
        void setNilPass();
        
        /**
         * Gets the "orderId" element
         */
        int getOrderId();
        
        /**
         * Gets (as xml) the "orderId" element
         */
        org.apache.xmlbeans.XmlInt xgetOrderId();
        
        /**
         * Tests for nil "orderId" element
         */
        boolean isNilOrderId();
        
        /**
         * Sets the "orderId" element
         */
        void setOrderId(int orderId);
        
        /**
         * Sets (as xml) the "orderId" element
         */
        void xsetOrderId(org.apache.xmlbeans.XmlInt orderId);
        
        /**
         * Nils the "orderId" element
         */
        void setNilOrderId();
        
        /**
         * Gets the "sessionId" element
         */
        java.lang.String getSessionId();
        
        /**
         * Gets (as xml) the "sessionId" element
         */
        org.apache.xmlbeans.XmlString xgetSessionId();
        
        /**
         * Tests for nil "sessionId" element
         */
        boolean isNilSessionId();
        
        /**
         * Sets the "sessionId" element
         */
        void setSessionId(java.lang.String sessionId);
        
        /**
         * Sets (as xml) the "sessionId" element
         */
        void xsetSessionId(org.apache.xmlbeans.XmlString sessionId);
        
        /**
         * Nils the "sessionId" element
         */
        void setNilSessionId();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument.GetTransactionFunds newInstance() {
              return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument.GetTransactionFunds) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument.GetTransactionFunds newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument.GetTransactionFunds) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument newInstance() {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.GetTransactionFundsDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

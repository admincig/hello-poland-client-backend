/*
 * XML Type:  Dispatch
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.Dispatch
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php;


/**
 * An XML Dispatch(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public interface Dispatch extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Dispatch.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C109014B664CDB4FCB4E98E343B54AA").resolveHandle("dispatch15bbtype");
    
    /**
     * Gets the "orderId" element
     */
    int getOrderId();
    
    /**
     * Gets (as xml) the "orderId" element
     */
    org.apache.xmlbeans.XmlInt xgetOrderId();
    
    /**
     * Sets the "orderId" element
     */
    void setOrderId(int orderId);
    
    /**
     * Sets (as xml) the "orderId" element
     */
    void xsetOrderId(org.apache.xmlbeans.XmlInt orderId);
    
    /**
     * Gets the "sessionId" element
     */
    java.lang.String getSessionId();
    
    /**
     * Gets (as xml) the "sessionId" element
     */
    org.apache.xmlbeans.XmlString xgetSessionId();
    
    /**
     * Sets the "sessionId" element
     */
    void setSessionId(java.lang.String sessionId);
    
    /**
     * Sets (as xml) the "sessionId" element
     */
    void xsetSessionId(org.apache.xmlbeans.XmlString sessionId);
    
    /**
     * Gets the "sellerId" element
     */
    int getSellerId();
    
    /**
     * Gets (as xml) the "sellerId" element
     */
    org.apache.xmlbeans.XmlInt xgetSellerId();
    
    /**
     * Sets the "sellerId" element
     */
    void setSellerId(int sellerId);
    
    /**
     * Sets (as xml) the "sellerId" element
     */
    void xsetSellerId(org.apache.xmlbeans.XmlInt sellerId);
    
    /**
     * Gets the "amount" element
     */
    int getAmount();
    
    /**
     * Gets (as xml) the "amount" element
     */
    org.apache.xmlbeans.XmlInt xgetAmount();
    
    /**
     * Sets the "amount" element
     */
    void setAmount(int amount);
    
    /**
     * Sets (as xml) the "amount" element
     */
    void xsetAmount(org.apache.xmlbeans.XmlInt amount);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static pl.przelewy24.secure.external._71852_php.Dispatch newInstance() {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.Dispatch parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.Dispatch) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

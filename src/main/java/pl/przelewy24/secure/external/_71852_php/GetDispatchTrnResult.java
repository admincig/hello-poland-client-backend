/*
 * XML Type:  GetDispatchTrnResult
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php;


/**
 * An XML GetDispatchTrnResult(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public interface GetDispatchTrnResult extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(GetDispatchTrnResult.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C109014B664CDB4FCB4E98E343B54AA").resolveHandle("getdispatchtrnresult979etype");
    
    /**
     * Gets the "result" element
     */
    pl.przelewy24.secure.external._71852_php.ArrayOfDispatchTransactionTrn getResult();
    
    /**
     * Sets the "result" element
     */
    void setResult(pl.przelewy24.secure.external._71852_php.ArrayOfDispatchTransactionTrn result);
    
    /**
     * Appends and returns a new empty "result" element
     */
    pl.przelewy24.secure.external._71852_php.ArrayOfDispatchTransactionTrn addNewResult();
    
    /**
     * Gets the "error" element
     */
    pl.przelewy24.secure.external._71852_php.GeneralError getError();
    
    /**
     * Sets the "error" element
     */
    void setError(pl.przelewy24.secure.external._71852_php.GeneralError error);
    
    /**
     * Appends and returns a new empty "error" element
     */
    pl.przelewy24.secure.external._71852_php.GeneralError addNewError();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult newInstance() {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

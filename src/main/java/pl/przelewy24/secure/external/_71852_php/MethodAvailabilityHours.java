/*
 * XML Type:  MethodAvailabilityHours
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php;


/**
 * An XML MethodAvailabilityHours(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public interface MethodAvailabilityHours extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MethodAvailabilityHours.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C109014B664CDB4FCB4E98E343B54AA").resolveHandle("methodavailabilityhours08a2type");
    
    /**
     * Gets the "mondayToFriday" element
     */
    java.lang.String getMondayToFriday();
    
    /**
     * Gets (as xml) the "mondayToFriday" element
     */
    org.apache.xmlbeans.XmlString xgetMondayToFriday();
    
    /**
     * Sets the "mondayToFriday" element
     */
    void setMondayToFriday(java.lang.String mondayToFriday);
    
    /**
     * Sets (as xml) the "mondayToFriday" element
     */
    void xsetMondayToFriday(org.apache.xmlbeans.XmlString mondayToFriday);
    
    /**
     * Gets the "saturday" element
     */
    java.lang.String getSaturday();
    
    /**
     * Gets (as xml) the "saturday" element
     */
    org.apache.xmlbeans.XmlString xgetSaturday();
    
    /**
     * Sets the "saturday" element
     */
    void setSaturday(java.lang.String saturday);
    
    /**
     * Sets (as xml) the "saturday" element
     */
    void xsetSaturday(org.apache.xmlbeans.XmlString saturday);
    
    /**
     * Gets the "sunday" element
     */
    java.lang.String getSunday();
    
    /**
     * Gets (as xml) the "sunday" element
     */
    org.apache.xmlbeans.XmlString xgetSunday();
    
    /**
     * Sets the "sunday" element
     */
    void setSunday(java.lang.String sunday);
    
    /**
     * Sets (as xml) the "sunday" element
     */
    void xsetSunday(org.apache.xmlbeans.XmlString sunday);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours newInstance() {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

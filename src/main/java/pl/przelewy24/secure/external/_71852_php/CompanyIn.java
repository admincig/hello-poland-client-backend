/*
 * XML Type:  CompanyIn
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.CompanyIn
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php;


/**
 * An XML CompanyIn(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public interface CompanyIn extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(CompanyIn.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C109014B664CDB4FCB4E98E343B54AA").resolveHandle("companyinedd1type");
    
    /**
     * Gets the "companyName" element
     */
    java.lang.String getCompanyName();
    
    /**
     * Gets (as xml) the "companyName" element
     */
    org.apache.xmlbeans.XmlString xgetCompanyName();
    
    /**
     * Sets the "companyName" element
     */
    void setCompanyName(java.lang.String companyName);
    
    /**
     * Sets (as xml) the "companyName" element
     */
    void xsetCompanyName(org.apache.xmlbeans.XmlString companyName);
    
    /**
     * Gets the "countryCode" element
     */
    java.lang.String getCountryCode();
    
    /**
     * Gets (as xml) the "countryCode" element
     */
    org.apache.xmlbeans.XmlString xgetCountryCode();
    
    /**
     * Sets the "countryCode" element
     */
    void setCountryCode(java.lang.String countryCode);
    
    /**
     * Sets (as xml) the "countryCode" element
     */
    void xsetCountryCode(org.apache.xmlbeans.XmlString countryCode);
    
    /**
     * Gets the "city" element
     */
    java.lang.String getCity();
    
    /**
     * Gets (as xml) the "city" element
     */
    org.apache.xmlbeans.XmlString xgetCity();
    
    /**
     * Sets the "city" element
     */
    void setCity(java.lang.String city);
    
    /**
     * Sets (as xml) the "city" element
     */
    void xsetCity(org.apache.xmlbeans.XmlString city);
    
    /**
     * Gets the "street" element
     */
    java.lang.String getStreet();
    
    /**
     * Gets (as xml) the "street" element
     */
    org.apache.xmlbeans.XmlString xgetStreet();
    
    /**
     * Sets the "street" element
     */
    void setStreet(java.lang.String street);
    
    /**
     * Sets (as xml) the "street" element
     */
    void xsetStreet(org.apache.xmlbeans.XmlString street);
    
    /**
     * Gets the "postCode" element
     */
    java.lang.String getPostCode();
    
    /**
     * Gets (as xml) the "postCode" element
     */
    org.apache.xmlbeans.XmlString xgetPostCode();
    
    /**
     * Sets the "postCode" element
     */
    void setPostCode(java.lang.String postCode);
    
    /**
     * Sets (as xml) the "postCode" element
     */
    void xsetPostCode(org.apache.xmlbeans.XmlString postCode);
    
    /**
     * Gets the "email" element
     */
    java.lang.String getEmail();
    
    /**
     * Gets (as xml) the "email" element
     */
    org.apache.xmlbeans.XmlString xgetEmail();
    
    /**
     * Sets the "email" element
     */
    void setEmail(java.lang.String email);
    
    /**
     * Sets (as xml) the "email" element
     */
    void xsetEmail(org.apache.xmlbeans.XmlString email);
    
    /**
     * Gets the "nip" element
     */
    java.lang.String getNip();
    
    /**
     * Gets (as xml) the "nip" element
     */
    org.apache.xmlbeans.XmlString xgetNip();
    
    /**
     * Sets the "nip" element
     */
    void setNip(java.lang.String nip);
    
    /**
     * Sets (as xml) the "nip" element
     */
    void xsetNip(org.apache.xmlbeans.XmlString nip);
    
    /**
     * Gets the "person" element
     */
    java.lang.String getPerson();
    
    /**
     * Gets (as xml) the "person" element
     */
    org.apache.xmlbeans.XmlString xgetPerson();
    
    /**
     * Sets the "person" element
     */
    void setPerson(java.lang.String person);
    
    /**
     * Sets (as xml) the "person" element
     */
    void xsetPerson(org.apache.xmlbeans.XmlString person);
    
    /**
     * Gets the "regon" element
     */
    java.lang.String getRegon();
    
    /**
     * Gets (as xml) the "regon" element
     */
    org.apache.xmlbeans.XmlString xgetRegon();
    
    /**
     * Sets the "regon" element
     */
    void setRegon(java.lang.String regon);
    
    /**
     * Sets (as xml) the "regon" element
     */
    void xsetRegon(org.apache.xmlbeans.XmlString regon);
    
    /**
     * Gets the "IBAN" element
     */
    java.lang.String getIBAN();
    
    /**
     * Gets (as xml) the "IBAN" element
     */
    org.apache.xmlbeans.XmlString xgetIBAN();
    
    /**
     * Tests for nil "IBAN" element
     */
    boolean isNilIBAN();
    
    /**
     * Sets the "IBAN" element
     */
    void setIBAN(java.lang.String iban);
    
    /**
     * Sets (as xml) the "IBAN" element
     */
    void xsetIBAN(org.apache.xmlbeans.XmlString iban);
    
    /**
     * Nils the "IBAN" element
     */
    void setNilIBAN();
    
    /**
     * Gets the "acceptance" element
     */
    boolean getAcceptance();
    
    /**
     * Gets (as xml) the "acceptance" element
     */
    org.apache.xmlbeans.XmlBoolean xgetAcceptance();
    
    /**
     * Tests for nil "acceptance" element
     */
    boolean isNilAcceptance();
    
    /**
     * Sets the "acceptance" element
     */
    void setAcceptance(boolean acceptance);
    
    /**
     * Sets (as xml) the "acceptance" element
     */
    void xsetAcceptance(org.apache.xmlbeans.XmlBoolean acceptance);
    
    /**
     * Nils the "acceptance" element
     */
    void setNilAcceptance();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static pl.przelewy24.secure.external._71852_php.CompanyIn newInstance() {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.CompanyIn parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.CompanyIn) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

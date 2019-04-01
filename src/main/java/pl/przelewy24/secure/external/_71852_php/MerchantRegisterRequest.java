/*
 * XML Type:  MerchantRegisterRequest
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php;


/**
 * An XML MerchantRegisterRequest(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public interface MerchantRegisterRequest extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MerchantRegisterRequest.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s8C109014B664CDB4FCB4E98E343B54AA").resolveHandle("merchantregisterrequest4693type");
    
    /**
     * Gets the "business_type" element
     */
    int getBusinessType();
    
    /**
     * Gets (as xml) the "business_type" element
     */
    org.apache.xmlbeans.XmlInt xgetBusinessType();
    
    /**
     * Sets the "business_type" element
     */
    void setBusinessType(int businessType);
    
    /**
     * Sets (as xml) the "business_type" element
     */
    void xsetBusinessType(org.apache.xmlbeans.XmlInt businessType);
    
    /**
     * Gets the "name" element
     */
    java.lang.String getName();
    
    /**
     * Gets (as xml) the "name" element
     */
    org.apache.xmlbeans.XmlString xgetName();
    
    /**
     * Sets the "name" element
     */
    void setName(java.lang.String name);
    
    /**
     * Sets (as xml) the "name" element
     */
    void xsetName(org.apache.xmlbeans.XmlString name);
    
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
     * Gets the "pesel" element
     */
    java.lang.String getPesel();
    
    /**
     * Gets (as xml) the "pesel" element
     */
    org.apache.xmlbeans.XmlString xgetPesel();
    
    /**
     * Sets the "pesel" element
     */
    void setPesel(java.lang.String pesel);
    
    /**
     * Sets (as xml) the "pesel" element
     */
    void xsetPesel(org.apache.xmlbeans.XmlString pesel);
    
    /**
     * Gets the "phone_number" element
     */
    java.lang.String getPhoneNumber();
    
    /**
     * Gets (as xml) the "phone_number" element
     */
    org.apache.xmlbeans.XmlString xgetPhoneNumber();
    
    /**
     * Sets the "phone_number" element
     */
    void setPhoneNumber(java.lang.String phoneNumber);
    
    /**
     * Sets (as xml) the "phone_number" element
     */
    void xsetPhoneNumber(org.apache.xmlbeans.XmlString phoneNumber);
    
    /**
     * Gets the "bank_account" element
     */
    java.lang.String getBankAccount();
    
    /**
     * Gets (as xml) the "bank_account" element
     */
    org.apache.xmlbeans.XmlString xgetBankAccount();
    
    /**
     * Sets the "bank_account" element
     */
    void setBankAccount(java.lang.String bankAccount);
    
    /**
     * Sets (as xml) the "bank_account" element
     */
    void xsetBankAccount(org.apache.xmlbeans.XmlString bankAccount);
    
    /**
     * Gets the "representatives" element
     */
    pl.przelewy24.secure.external._71852_php.ArrayOfRepresentative getRepresentatives();
    
    /**
     * Sets the "representatives" element
     */
    void setRepresentatives(pl.przelewy24.secure.external._71852_php.ArrayOfRepresentative representatives);
    
    /**
     * Appends and returns a new empty "representatives" element
     */
    pl.przelewy24.secure.external._71852_php.ArrayOfRepresentative addNewRepresentatives();
    
    /**
     * Gets the "contact_person" element
     */
    pl.przelewy24.secure.external._71852_php.ContactPerson getContactPerson();
    
    /**
     * Sets the "contact_person" element
     */
    void setContactPerson(pl.przelewy24.secure.external._71852_php.ContactPerson contactPerson);
    
    /**
     * Appends and returns a new empty "contact_person" element
     */
    pl.przelewy24.secure.external._71852_php.ContactPerson addNewContactPerson();
    
    /**
     * Gets the "technical_contact" element
     */
    pl.przelewy24.secure.external._71852_php.TechnicalContact getTechnicalContact();
    
    /**
     * Sets the "technical_contact" element
     */
    void setTechnicalContact(pl.przelewy24.secure.external._71852_php.TechnicalContact technicalContact);
    
    /**
     * Appends and returns a new empty "technical_contact" element
     */
    pl.przelewy24.secure.external._71852_php.TechnicalContact addNewTechnicalContact();
    
    /**
     * Gets the "address" element
     */
    pl.przelewy24.secure.external._71852_php.Address getAddress();
    
    /**
     * Sets the "address" element
     */
    void setAddress(pl.przelewy24.secure.external._71852_php.Address address);
    
    /**
     * Appends and returns a new empty "address" element
     */
    pl.przelewy24.secure.external._71852_php.Address addNewAddress();
    
    /**
     * Gets the "correspondence_address" element
     */
    pl.przelewy24.secure.external._71852_php.CorrespondenceAddress getCorrespondenceAddress();
    
    /**
     * Sets the "correspondence_address" element
     */
    void setCorrespondenceAddress(pl.przelewy24.secure.external._71852_php.CorrespondenceAddress correspondenceAddress);
    
    /**
     * Appends and returns a new empty "correspondence_address" element
     */
    pl.przelewy24.secure.external._71852_php.CorrespondenceAddress addNewCorrespondenceAddress();
    
    /**
     * Gets the "invoice_email" element
     */
    java.lang.String getInvoiceEmail();
    
    /**
     * Gets (as xml) the "invoice_email" element
     */
    org.apache.xmlbeans.XmlString xgetInvoiceEmail();
    
    /**
     * Sets the "invoice_email" element
     */
    void setInvoiceEmail(java.lang.String invoiceEmail);
    
    /**
     * Sets (as xml) the "invoice_email" element
     */
    void xsetInvoiceEmail(org.apache.xmlbeans.XmlString invoiceEmail);
    
    /**
     * Gets the "shop_url" element
     */
    java.lang.String getShopUrl();
    
    /**
     * Gets (as xml) the "shop_url" element
     */
    org.apache.xmlbeans.XmlString xgetShopUrl();
    
    /**
     * Sets the "shop_url" element
     */
    void setShopUrl(java.lang.String shopUrl);
    
    /**
     * Sets (as xml) the "shop_url" element
     */
    void xsetShopUrl(org.apache.xmlbeans.XmlString shopUrl);
    
    /**
     * Gets the "services_description" element
     */
    java.lang.String getServicesDescription();
    
    /**
     * Gets (as xml) the "services_description" element
     */
    org.apache.xmlbeans.XmlString xgetServicesDescription();
    
    /**
     * Sets the "services_description" element
     */
    void setServicesDescription(java.lang.String servicesDescription);
    
    /**
     * Sets (as xml) the "services_description" element
     */
    void xsetServicesDescription(org.apache.xmlbeans.XmlString servicesDescription);
    
    /**
     * Gets the "trade" element
     */
    java.lang.String getTrade();
    
    /**
     * Gets (as xml) the "trade" element
     */
    org.apache.xmlbeans.XmlString xgetTrade();
    
    /**
     * Sets the "trade" element
     */
    void setTrade(java.lang.String trade);
    
    /**
     * Sets (as xml) the "trade" element
     */
    void xsetTrade(org.apache.xmlbeans.XmlString trade);
    
    /**
     * Gets the "krs" element
     */
    java.lang.String getKrs();
    
    /**
     * Gets (as xml) the "krs" element
     */
    org.apache.xmlbeans.XmlString xgetKrs();
    
    /**
     * Sets the "krs" element
     */
    void setKrs(java.lang.String krs);
    
    /**
     * Sets (as xml) the "krs" element
     */
    void xsetKrs(org.apache.xmlbeans.XmlString krs);
    
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
     * Gets the "acceptance" element
     */
    boolean getAcceptance();
    
    /**
     * Gets (as xml) the "acceptance" element
     */
    org.apache.xmlbeans.XmlBoolean xgetAcceptance();
    
    /**
     * Sets the "acceptance" element
     */
    void setAcceptance(boolean acceptance);
    
    /**
     * Sets (as xml) the "acceptance" element
     */
    void xsetAcceptance(org.apache.xmlbeans.XmlBoolean acceptance);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest newInstance() {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

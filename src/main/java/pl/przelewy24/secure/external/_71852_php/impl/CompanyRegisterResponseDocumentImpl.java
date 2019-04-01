/*
 * An XML document type.
 * Localname: CompanyRegisterResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.CompanyRegisterResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one CompanyRegisterResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class CompanyRegisterResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.CompanyRegisterResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public CompanyRegisterResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COMPANYREGISTERRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "CompanyRegisterResponse");
    
    
    /**
     * Gets the "CompanyRegisterResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.CompanyRegisterResponseDocument.CompanyRegisterResponse getCompanyRegisterResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.CompanyRegisterResponseDocument.CompanyRegisterResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.CompanyRegisterResponseDocument.CompanyRegisterResponse)get_store().find_element_user(COMPANYREGISTERRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CompanyRegisterResponse" element
     */
    public void setCompanyRegisterResponse(pl.przelewy24.secure.external._71852_php.CompanyRegisterResponseDocument.CompanyRegisterResponse companyRegisterResponse)
    {
        generatedSetterHelperImpl(companyRegisterResponse, COMPANYREGISTERRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "CompanyRegisterResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.CompanyRegisterResponseDocument.CompanyRegisterResponse addNewCompanyRegisterResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.CompanyRegisterResponseDocument.CompanyRegisterResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.CompanyRegisterResponseDocument.CompanyRegisterResponse)get_store().add_element_user(COMPANYREGISTERRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML CompanyRegisterResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class CompanyRegisterResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.CompanyRegisterResponseDocument.CompanyRegisterResponse
    {
        private static final long serialVersionUID = 1L;
        
        public CompanyRegisterResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.CompanyRegisterResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.CompanyRegisterResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CompanyRegisterResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Tests for nil "return" element
         */
        public boolean isNilReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.CompanyRegisterResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CompanyRegisterResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.CompanyRegisterResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.CompanyRegisterResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.CompanyRegisterResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CompanyRegisterResult)get_store().add_element_user(RETURN$0);
                return target;
            }
        }
        
        /**
         * Nils the "return" element
         */
        public void setNilReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.CompanyRegisterResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CompanyRegisterResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.CompanyRegisterResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

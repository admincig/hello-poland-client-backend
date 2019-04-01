/*
 * An XML document type.
 * Localname: MerchantRegisterResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.MerchantRegisterResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one MerchantRegisterResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class MerchantRegisterResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.MerchantRegisterResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public MerchantRegisterResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MERCHANTREGISTERRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "MerchantRegisterResponse");
    
    
    /**
     * Gets the "MerchantRegisterResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.MerchantRegisterResponseDocument.MerchantRegisterResponse getMerchantRegisterResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.MerchantRegisterResponseDocument.MerchantRegisterResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterResponseDocument.MerchantRegisterResponse)get_store().find_element_user(MERCHANTREGISTERRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "MerchantRegisterResponse" element
     */
    public void setMerchantRegisterResponse(pl.przelewy24.secure.external._71852_php.MerchantRegisterResponseDocument.MerchantRegisterResponse merchantRegisterResponse)
    {
        generatedSetterHelperImpl(merchantRegisterResponse, MERCHANTREGISTERRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "MerchantRegisterResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.MerchantRegisterResponseDocument.MerchantRegisterResponse addNewMerchantRegisterResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.MerchantRegisterResponseDocument.MerchantRegisterResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterResponseDocument.MerchantRegisterResponse)get_store().add_element_user(MERCHANTREGISTERRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML MerchantRegisterResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class MerchantRegisterResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.MerchantRegisterResponseDocument.MerchantRegisterResponse
    {
        private static final long serialVersionUID = 1L;
        
        public MerchantRegisterResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.MerchantRegisterResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.MerchantRegisterResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.MerchantRegisterResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.MerchantRegisterResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.MerchantRegisterResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.MerchantRegisterResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.MerchantRegisterResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

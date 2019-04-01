/*
 * An XML document type.
 * Localname: MerchantExistsResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.MerchantExistsResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one MerchantExistsResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class MerchantExistsResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.MerchantExistsResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public MerchantExistsResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MERCHANTEXISTSRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "MerchantExistsResponse");
    
    
    /**
     * Gets the "MerchantExistsResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.MerchantExistsResponseDocument.MerchantExistsResponse getMerchantExistsResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.MerchantExistsResponseDocument.MerchantExistsResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.MerchantExistsResponseDocument.MerchantExistsResponse)get_store().find_element_user(MERCHANTEXISTSRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "MerchantExistsResponse" element
     */
    public void setMerchantExistsResponse(pl.przelewy24.secure.external._71852_php.MerchantExistsResponseDocument.MerchantExistsResponse merchantExistsResponse)
    {
        generatedSetterHelperImpl(merchantExistsResponse, MERCHANTEXISTSRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "MerchantExistsResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.MerchantExistsResponseDocument.MerchantExistsResponse addNewMerchantExistsResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.MerchantExistsResponseDocument.MerchantExistsResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.MerchantExistsResponseDocument.MerchantExistsResponse)get_store().add_element_user(MERCHANTEXISTSRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML MerchantExistsResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class MerchantExistsResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.MerchantExistsResponseDocument.MerchantExistsResponse
    {
        private static final long serialVersionUID = 1L;
        
        public MerchantExistsResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.MerchantExistsResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.MerchantExistsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantExistsResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.MerchantExistsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantExistsResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.MerchantExistsResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.MerchantExistsResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.MerchantExistsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantExistsResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.MerchantExistsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantExistsResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.MerchantExistsResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

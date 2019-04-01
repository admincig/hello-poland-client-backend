/*
 * An XML document type.
 * Localname: GetAffiliateCrcResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one GetAffiliateCrcResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class GetAffiliateCrcResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetAffiliateCrcResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETAFFILIATECRCRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "GetAffiliateCrcResponse");
    
    
    /**
     * Gets the "GetAffiliateCrcResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResponseDocument.GetAffiliateCrcResponse getGetAffiliateCrcResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResponseDocument.GetAffiliateCrcResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResponseDocument.GetAffiliateCrcResponse)get_store().find_element_user(GETAFFILIATECRCRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "GetAffiliateCrcResponse" element
     */
    public void setGetAffiliateCrcResponse(pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResponseDocument.GetAffiliateCrcResponse getAffiliateCrcResponse)
    {
        generatedSetterHelperImpl(getAffiliateCrcResponse, GETAFFILIATECRCRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "GetAffiliateCrcResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResponseDocument.GetAffiliateCrcResponse addNewGetAffiliateCrcResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResponseDocument.GetAffiliateCrcResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResponseDocument.GetAffiliateCrcResponse)get_store().add_element_user(GETAFFILIATECRCRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML GetAffiliateCrcResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class GetAffiliateCrcResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResponseDocument.GetAffiliateCrcResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetAffiliateCrcResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.GetAffiliateCrcResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

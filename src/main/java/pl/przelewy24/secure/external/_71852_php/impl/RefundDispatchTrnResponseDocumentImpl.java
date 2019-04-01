/*
 * An XML document type.
 * Localname: RefundDispatchTrnResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.RefundDispatchTrnResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one RefundDispatchTrnResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class RefundDispatchTrnResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RefundDispatchTrnResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public RefundDispatchTrnResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REFUNDDISPATCHTRNRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "RefundDispatchTrnResponse");
    
    
    /**
     * Gets the "RefundDispatchTrnResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.RefundDispatchTrnResponseDocument.RefundDispatchTrnResponse getRefundDispatchTrnResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.RefundDispatchTrnResponseDocument.RefundDispatchTrnResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.RefundDispatchTrnResponseDocument.RefundDispatchTrnResponse)get_store().find_element_user(REFUNDDISPATCHTRNRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "RefundDispatchTrnResponse" element
     */
    public void setRefundDispatchTrnResponse(pl.przelewy24.secure.external._71852_php.RefundDispatchTrnResponseDocument.RefundDispatchTrnResponse refundDispatchTrnResponse)
    {
        generatedSetterHelperImpl(refundDispatchTrnResponse, REFUNDDISPATCHTRNRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "RefundDispatchTrnResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.RefundDispatchTrnResponseDocument.RefundDispatchTrnResponse addNewRefundDispatchTrnResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.RefundDispatchTrnResponseDocument.RefundDispatchTrnResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.RefundDispatchTrnResponseDocument.RefundDispatchTrnResponse)get_store().add_element_user(REFUNDDISPATCHTRNRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML RefundDispatchTrnResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class RefundDispatchTrnResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RefundDispatchTrnResponseDocument.RefundDispatchTrnResponse
    {
        private static final long serialVersionUID = 1L;
        
        public RefundDispatchTrnResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.RefundCartResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.RefundCartResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RefundCartResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.RefundCartResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RefundCartResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.RefundCartResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.RefundCartResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.RefundCartResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RefundCartResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.RefundCartResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RefundCartResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.RefundCartResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

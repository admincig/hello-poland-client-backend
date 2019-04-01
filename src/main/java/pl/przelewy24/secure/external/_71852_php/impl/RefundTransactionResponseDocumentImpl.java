/*
 * An XML document type.
 * Localname: RefundTransactionResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.RefundTransactionResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one RefundTransactionResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class RefundTransactionResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RefundTransactionResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public RefundTransactionResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REFUNDTRANSACTIONRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "RefundTransactionResponse");
    
    
    /**
     * Gets the "RefundTransactionResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.RefundTransactionResponseDocument.RefundTransactionResponse getRefundTransactionResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.RefundTransactionResponseDocument.RefundTransactionResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.RefundTransactionResponseDocument.RefundTransactionResponse)get_store().find_element_user(REFUNDTRANSACTIONRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "RefundTransactionResponse" element
     */
    public void setRefundTransactionResponse(pl.przelewy24.secure.external._71852_php.RefundTransactionResponseDocument.RefundTransactionResponse refundTransactionResponse)
    {
        generatedSetterHelperImpl(refundTransactionResponse, REFUNDTRANSACTIONRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "RefundTransactionResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.RefundTransactionResponseDocument.RefundTransactionResponse addNewRefundTransactionResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.RefundTransactionResponseDocument.RefundTransactionResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.RefundTransactionResponseDocument.RefundTransactionResponse)get_store().add_element_user(REFUNDTRANSACTIONRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML RefundTransactionResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class RefundTransactionResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RefundTransactionResponseDocument.RefundTransactionResponse
    {
        private static final long serialVersionUID = 1L;
        
        public RefundTransactionResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.RefundTransactionResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.RefundTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RefundTransactionResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.RefundTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RefundTransactionResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.RefundTransactionResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.RefundTransactionResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.RefundTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RefundTransactionResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.RefundTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RefundTransactionResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.RefundTransactionResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

/*
 * An XML document type.
 * Localname: GetTransactionsByBatchResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one GetTransactionsByBatchResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class GetTransactionsByBatchResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetTransactionsByBatchResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETTRANSACTIONSBYBATCHRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "GetTransactionsByBatchResponse");
    
    
    /**
     * Gets the "GetTransactionsByBatchResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResponseDocument.GetTransactionsByBatchResponse getGetTransactionsByBatchResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResponseDocument.GetTransactionsByBatchResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResponseDocument.GetTransactionsByBatchResponse)get_store().find_element_user(GETTRANSACTIONSBYBATCHRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "GetTransactionsByBatchResponse" element
     */
    public void setGetTransactionsByBatchResponse(pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResponseDocument.GetTransactionsByBatchResponse getTransactionsByBatchResponse)
    {
        generatedSetterHelperImpl(getTransactionsByBatchResponse, GETTRANSACTIONSBYBATCHRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "GetTransactionsByBatchResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResponseDocument.GetTransactionsByBatchResponse addNewGetTransactionsByBatchResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResponseDocument.GetTransactionsByBatchResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResponseDocument.GetTransactionsByBatchResponse)get_store().add_element_user(GETTRANSACTIONSBYBATCHRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML GetTransactionsByBatchResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class GetTransactionsByBatchResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResponseDocument.GetTransactionsByBatchResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetTransactionsByBatchResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByBatchResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

/*
 * An XML document type.
 * Localname: GetTransactionFundsResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GetTransactionFundsResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one GetTransactionFundsResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class GetTransactionFundsResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionFundsResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetTransactionFundsResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETTRANSACTIONFUNDSRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "GetTransactionFundsResponse");
    
    
    /**
     * Gets the "GetTransactionFundsResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionFundsResponseDocument.GetTransactionFundsResponse getGetTransactionFundsResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionFundsResponseDocument.GetTransactionFundsResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionFundsResponseDocument.GetTransactionFundsResponse)get_store().find_element_user(GETTRANSACTIONFUNDSRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "GetTransactionFundsResponse" element
     */
    public void setGetTransactionFundsResponse(pl.przelewy24.secure.external._71852_php.GetTransactionFundsResponseDocument.GetTransactionFundsResponse getTransactionFundsResponse)
    {
        generatedSetterHelperImpl(getTransactionFundsResponse, GETTRANSACTIONFUNDSRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "GetTransactionFundsResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionFundsResponseDocument.GetTransactionFundsResponse addNewGetTransactionFundsResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionFundsResponseDocument.GetTransactionFundsResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionFundsResponseDocument.GetTransactionFundsResponse)get_store().add_element_user(GETTRANSACTIONFUNDSRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML GetTransactionFundsResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class GetTransactionFundsResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionFundsResponseDocument.GetTransactionFundsResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetTransactionFundsResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.GetTransactionFundsResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

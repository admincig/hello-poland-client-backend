/*
 * An XML document type.
 * Localname: DispatchTransactionResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.DispatchTransactionResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one DispatchTransactionResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class DispatchTransactionResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.DispatchTransactionResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public DispatchTransactionResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DISPATCHTRANSACTIONRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "DispatchTransactionResponse");
    
    
    /**
     * Gets the "DispatchTransactionResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.DispatchTransactionResponseDocument.DispatchTransactionResponse getDispatchTransactionResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.DispatchTransactionResponseDocument.DispatchTransactionResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.DispatchTransactionResponseDocument.DispatchTransactionResponse)get_store().find_element_user(DISPATCHTRANSACTIONRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "DispatchTransactionResponse" element
     */
    public void setDispatchTransactionResponse(pl.przelewy24.secure.external._71852_php.DispatchTransactionResponseDocument.DispatchTransactionResponse dispatchTransactionResponse)
    {
        generatedSetterHelperImpl(dispatchTransactionResponse, DISPATCHTRANSACTIONRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "DispatchTransactionResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.DispatchTransactionResponseDocument.DispatchTransactionResponse addNewDispatchTransactionResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.DispatchTransactionResponseDocument.DispatchTransactionResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.DispatchTransactionResponseDocument.DispatchTransactionResponse)get_store().add_element_user(DISPATCHTRANSACTIONRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML DispatchTransactionResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class DispatchTransactionResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.DispatchTransactionResponseDocument.DispatchTransactionResponse
    {
        private static final long serialVersionUID = 1L;
        
        public DispatchTransactionResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.DispatchTransactionResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.DispatchTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.DispatchTransactionResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.DispatchTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.DispatchTransactionResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.DispatchTransactionResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.DispatchTransactionResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.DispatchTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.DispatchTransactionResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.DispatchTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.DispatchTransactionResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.DispatchTransactionResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

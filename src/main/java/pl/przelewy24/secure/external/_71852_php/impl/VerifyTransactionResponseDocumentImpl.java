/*
 * An XML document type.
 * Localname: VerifyTransactionResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.VerifyTransactionResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one VerifyTransactionResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class VerifyTransactionResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.VerifyTransactionResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public VerifyTransactionResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VERIFYTRANSACTIONRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "VerifyTransactionResponse");
    
    
    /**
     * Gets the "VerifyTransactionResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.VerifyTransactionResponseDocument.VerifyTransactionResponse getVerifyTransactionResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.VerifyTransactionResponseDocument.VerifyTransactionResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.VerifyTransactionResponseDocument.VerifyTransactionResponse)get_store().find_element_user(VERIFYTRANSACTIONRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "VerifyTransactionResponse" element
     */
    public void setVerifyTransactionResponse(pl.przelewy24.secure.external._71852_php.VerifyTransactionResponseDocument.VerifyTransactionResponse verifyTransactionResponse)
    {
        generatedSetterHelperImpl(verifyTransactionResponse, VERIFYTRANSACTIONRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "VerifyTransactionResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.VerifyTransactionResponseDocument.VerifyTransactionResponse addNewVerifyTransactionResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.VerifyTransactionResponseDocument.VerifyTransactionResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.VerifyTransactionResponseDocument.VerifyTransactionResponse)get_store().add_element_user(VERIFYTRANSACTIONRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML VerifyTransactionResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class VerifyTransactionResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.VerifyTransactionResponseDocument.VerifyTransactionResponse
    {
        private static final long serialVersionUID = 1L;
        
        public VerifyTransactionResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.VerifyTransactionResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.VerifyTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.VerifyTransactionResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.VerifyTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.VerifyTransactionResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.VerifyTransactionResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.VerifyTransactionResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.VerifyTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.VerifyTransactionResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.VerifyTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.VerifyTransactionResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.VerifyTransactionResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

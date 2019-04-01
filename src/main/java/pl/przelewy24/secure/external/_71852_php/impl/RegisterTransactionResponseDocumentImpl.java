/*
 * An XML document type.
 * Localname: RegisterTransactionResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.RegisterTransactionResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one RegisterTransactionResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class RegisterTransactionResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RegisterTransactionResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public RegisterTransactionResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REGISTERTRANSACTIONRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "RegisterTransactionResponse");
    
    
    /**
     * Gets the "RegisterTransactionResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.RegisterTransactionResponseDocument.RegisterTransactionResponse getRegisterTransactionResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.RegisterTransactionResponseDocument.RegisterTransactionResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.RegisterTransactionResponseDocument.RegisterTransactionResponse)get_store().find_element_user(REGISTERTRANSACTIONRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "RegisterTransactionResponse" element
     */
    public void setRegisterTransactionResponse(pl.przelewy24.secure.external._71852_php.RegisterTransactionResponseDocument.RegisterTransactionResponse registerTransactionResponse)
    {
        generatedSetterHelperImpl(registerTransactionResponse, REGISTERTRANSACTIONRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "RegisterTransactionResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.RegisterTransactionResponseDocument.RegisterTransactionResponse addNewRegisterTransactionResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.RegisterTransactionResponseDocument.RegisterTransactionResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.RegisterTransactionResponseDocument.RegisterTransactionResponse)get_store().add_element_user(REGISTERTRANSACTIONRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML RegisterTransactionResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class RegisterTransactionResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RegisterTransactionResponseDocument.RegisterTransactionResponse
    {
        private static final long serialVersionUID = 1L;
        
        public RegisterTransactionResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.RegisterTransactionResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.RegisterTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RegisterTransactionResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.RegisterTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RegisterTransactionResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.RegisterTransactionResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.RegisterTransactionResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.RegisterTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RegisterTransactionResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.RegisterTransactionResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.RegisterTransactionResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.RegisterTransactionResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

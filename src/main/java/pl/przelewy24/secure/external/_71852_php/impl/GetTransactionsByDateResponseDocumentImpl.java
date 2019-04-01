/*
 * An XML document type.
 * Localname: GetTransactionsByDateResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one GetTransactionsByDateResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class GetTransactionsByDateResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetTransactionsByDateResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETTRANSACTIONSBYDATERESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "GetTransactionsByDateResponse");
    
    
    /**
     * Gets the "GetTransactionsByDateResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResponseDocument.GetTransactionsByDateResponse getGetTransactionsByDateResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResponseDocument.GetTransactionsByDateResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResponseDocument.GetTransactionsByDateResponse)get_store().find_element_user(GETTRANSACTIONSBYDATERESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "GetTransactionsByDateResponse" element
     */
    public void setGetTransactionsByDateResponse(pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResponseDocument.GetTransactionsByDateResponse getTransactionsByDateResponse)
    {
        generatedSetterHelperImpl(getTransactionsByDateResponse, GETTRANSACTIONSBYDATERESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "GetTransactionsByDateResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResponseDocument.GetTransactionsByDateResponse addNewGetTransactionsByDateResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResponseDocument.GetTransactionsByDateResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResponseDocument.GetTransactionsByDateResponse)get_store().add_element_user(GETTRANSACTIONSBYDATERESPONSE$0);
            return target;
        }
    }
    /**
     * An XML GetTransactionsByDateResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class GetTransactionsByDateResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResponseDocument.GetTransactionsByDateResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetTransactionsByDateResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.GetTransactionsByDateResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

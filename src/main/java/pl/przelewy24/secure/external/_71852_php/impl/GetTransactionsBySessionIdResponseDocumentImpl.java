/*
 * An XML document type.
 * Localname: GetTransactionsBySessionIdResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one GetTransactionsBySessionIdResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class GetTransactionsBySessionIdResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetTransactionsBySessionIdResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETTRANSACTIONSBYSESSIONIDRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "GetTransactionsBySessionIdResponse");
    
    
    /**
     * Gets the "GetTransactionsBySessionIdResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResponseDocument.GetTransactionsBySessionIdResponse getGetTransactionsBySessionIdResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResponseDocument.GetTransactionsBySessionIdResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResponseDocument.GetTransactionsBySessionIdResponse)get_store().find_element_user(GETTRANSACTIONSBYSESSIONIDRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "GetTransactionsBySessionIdResponse" element
     */
    public void setGetTransactionsBySessionIdResponse(pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResponseDocument.GetTransactionsBySessionIdResponse getTransactionsBySessionIdResponse)
    {
        generatedSetterHelperImpl(getTransactionsBySessionIdResponse, GETTRANSACTIONSBYSESSIONIDRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "GetTransactionsBySessionIdResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResponseDocument.GetTransactionsBySessionIdResponse addNewGetTransactionsBySessionIdResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResponseDocument.GetTransactionsBySessionIdResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResponseDocument.GetTransactionsBySessionIdResponse)get_store().add_element_user(GETTRANSACTIONSBYSESSIONIDRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML GetTransactionsBySessionIdResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class GetTransactionsBySessionIdResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResponseDocument.GetTransactionsBySessionIdResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetTransactionsBySessionIdResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.GetTransactionsBySessionIdResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

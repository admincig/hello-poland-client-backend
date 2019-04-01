/*
 * An XML document type.
 * Localname: GetTransactionBySessionIdResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one GetTransactionBySessionIdResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class GetTransactionBySessionIdResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetTransactionBySessionIdResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETTRANSACTIONBYSESSIONIDRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "GetTransactionBySessionIdResponse");
    
    
    /**
     * Gets the "GetTransactionBySessionIdResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResponseDocument.GetTransactionBySessionIdResponse getGetTransactionBySessionIdResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResponseDocument.GetTransactionBySessionIdResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResponseDocument.GetTransactionBySessionIdResponse)get_store().find_element_user(GETTRANSACTIONBYSESSIONIDRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "GetTransactionBySessionIdResponse" element
     */
    public void setGetTransactionBySessionIdResponse(pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResponseDocument.GetTransactionBySessionIdResponse getTransactionBySessionIdResponse)
    {
        generatedSetterHelperImpl(getTransactionBySessionIdResponse, GETTRANSACTIONBYSESSIONIDRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "GetTransactionBySessionIdResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResponseDocument.GetTransactionBySessionIdResponse addNewGetTransactionBySessionIdResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResponseDocument.GetTransactionBySessionIdResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResponseDocument.GetTransactionBySessionIdResponse)get_store().add_element_user(GETTRANSACTIONBYSESSIONIDRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML GetTransactionBySessionIdResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class GetTransactionBySessionIdResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResponseDocument.GetTransactionBySessionIdResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetTransactionBySessionIdResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

/*
 * An XML document type.
 * Localname: GetDispatchTrnResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GetDispatchTrnResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one GetDispatchTrnResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class GetDispatchTrnResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetDispatchTrnResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetDispatchTrnResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETDISPATCHTRNRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "GetDispatchTrnResponse");
    
    
    /**
     * Gets the "GetDispatchTrnResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetDispatchTrnResponseDocument.GetDispatchTrnResponse getGetDispatchTrnResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetDispatchTrnResponseDocument.GetDispatchTrnResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResponseDocument.GetDispatchTrnResponse)get_store().find_element_user(GETDISPATCHTRNRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "GetDispatchTrnResponse" element
     */
    public void setGetDispatchTrnResponse(pl.przelewy24.secure.external._71852_php.GetDispatchTrnResponseDocument.GetDispatchTrnResponse getDispatchTrnResponse)
    {
        generatedSetterHelperImpl(getDispatchTrnResponse, GETDISPATCHTRNRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "GetDispatchTrnResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.GetDispatchTrnResponseDocument.GetDispatchTrnResponse addNewGetDispatchTrnResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetDispatchTrnResponseDocument.GetDispatchTrnResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResponseDocument.GetDispatchTrnResponse)get_store().add_element_user(GETDISPATCHTRNRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML GetDispatchTrnResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class GetDispatchTrnResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetDispatchTrnResponseDocument.GetDispatchTrnResponse
    {
        private static final long serialVersionUID = 1L;
        
        public GetDispatchTrnResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.GetDispatchTrnResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

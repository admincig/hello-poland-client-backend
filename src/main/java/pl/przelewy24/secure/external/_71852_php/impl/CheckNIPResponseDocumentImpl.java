/*
 * An XML document type.
 * Localname: CheckNIPResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.CheckNIPResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one CheckNIPResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class CheckNIPResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.CheckNIPResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public CheckNIPResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CHECKNIPRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "CheckNIPResponse");
    
    
    /**
     * Gets the "CheckNIPResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.CheckNIPResponseDocument.CheckNIPResponse getCheckNIPResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.CheckNIPResponseDocument.CheckNIPResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.CheckNIPResponseDocument.CheckNIPResponse)get_store().find_element_user(CHECKNIPRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CheckNIPResponse" element
     */
    public void setCheckNIPResponse(pl.przelewy24.secure.external._71852_php.CheckNIPResponseDocument.CheckNIPResponse checkNIPResponse)
    {
        generatedSetterHelperImpl(checkNIPResponse, CHECKNIPRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "CheckNIPResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.CheckNIPResponseDocument.CheckNIPResponse addNewCheckNIPResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.CheckNIPResponseDocument.CheckNIPResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.CheckNIPResponseDocument.CheckNIPResponse)get_store().add_element_user(CHECKNIPRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML CheckNIPResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class CheckNIPResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.CheckNIPResponseDocument.CheckNIPResponse
    {
        private static final long serialVersionUID = 1L;
        
        public CheckNIPResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.CheckNIPResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.CheckNIPResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CheckNIPResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.CheckNIPResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CheckNIPResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.CheckNIPResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.CheckNIPResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.CheckNIPResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CheckNIPResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.CheckNIPResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CheckNIPResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.CheckNIPResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

/*
 * An XML document type.
 * Localname: TestAccessResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.TestAccessResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one TestAccessResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class TestAccessResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.TestAccessResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public TestAccessResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TESTACCESSRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "TestAccessResponse");
    
    
    /**
     * Gets the "TestAccessResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.TestAccessResponseDocument.TestAccessResponse getTestAccessResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.TestAccessResponseDocument.TestAccessResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.TestAccessResponseDocument.TestAccessResponse)get_store().find_element_user(TESTACCESSRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "TestAccessResponse" element
     */
    public void setTestAccessResponse(pl.przelewy24.secure.external._71852_php.TestAccessResponseDocument.TestAccessResponse testAccessResponse)
    {
        generatedSetterHelperImpl(testAccessResponse, TESTACCESSRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "TestAccessResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.TestAccessResponseDocument.TestAccessResponse addNewTestAccessResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.TestAccessResponseDocument.TestAccessResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.TestAccessResponseDocument.TestAccessResponse)get_store().add_element_user(TESTACCESSRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML TestAccessResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class TestAccessResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.TestAccessResponseDocument.TestAccessResponse
    {
        private static final long serialVersionUID = 1L;
        
        public TestAccessResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public boolean getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    return false;
                }
                return target.getBooleanValue();
            }
        }
        
        /**
         * Gets (as xml) the "return" element
         */
        public org.apache.xmlbeans.XmlBoolean xgetReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(RETURN$0, 0);
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
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(boolean xreturn)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RETURN$0);
                }
                target.setBooleanValue(xreturn);
            }
        }
        
        /**
         * Sets (as xml) the "return" element
         */
        public void xsetReturn(org.apache.xmlbeans.XmlBoolean xreturn)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(RETURN$0);
                }
                target.set(xreturn);
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
                org.apache.xmlbeans.XmlBoolean target = null;
                target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

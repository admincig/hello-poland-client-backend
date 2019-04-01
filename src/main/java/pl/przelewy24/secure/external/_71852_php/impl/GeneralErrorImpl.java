/*
 * XML Type:  GeneralError
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GeneralError
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * An XML GeneralError(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public class GeneralErrorImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GeneralError
{
    private static final long serialVersionUID = 1L;
    
    public GeneralErrorImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ERRORCODE$0 = 
        new javax.xml.namespace.QName("", "errorCode");
    private static final javax.xml.namespace.QName ERRORMESSAGE$2 = 
        new javax.xml.namespace.QName("", "errorMessage");
    
    
    /**
     * Gets the "errorCode" element
     */
    public int getErrorCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERRORCODE$0, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "errorCode" element
     */
    public org.apache.xmlbeans.XmlInt xgetErrorCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ERRORCODE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "errorCode" element
     */
    public void setErrorCode(int errorCode)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERRORCODE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ERRORCODE$0);
            }
            target.setIntValue(errorCode);
        }
    }
    
    /**
     * Sets (as xml) the "errorCode" element
     */
    public void xsetErrorCode(org.apache.xmlbeans.XmlInt errorCode)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ERRORCODE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(ERRORCODE$0);
            }
            target.set(errorCode);
        }
    }
    
    /**
     * Gets the "errorMessage" element
     */
    public java.lang.String getErrorMessage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERRORMESSAGE$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "errorMessage" element
     */
    public org.apache.xmlbeans.XmlString xgetErrorMessage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ERRORMESSAGE$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "errorMessage" element
     */
    public void setErrorMessage(java.lang.String errorMessage)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERRORMESSAGE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ERRORMESSAGE$2);
            }
            target.setStringValue(errorMessage);
        }
    }
    
    /**
     * Sets (as xml) the "errorMessage" element
     */
    public void xsetErrorMessage(org.apache.xmlbeans.XmlString errorMessage)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ERRORMESSAGE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ERRORMESSAGE$2);
            }
            target.set(errorMessage);
        }
    }
}

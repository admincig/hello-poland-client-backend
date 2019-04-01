/*
 * XML Type:  Currency
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.Currency
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * An XML Currency(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public class CurrencyImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.Currency
{
    private static final long serialVersionUID = 1L;
    
    public CurrencyImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CODE$0 = 
        new javax.xml.namespace.QName("", "code");
    private static final javax.xml.namespace.QName EXCHNG$2 = 
        new javax.xml.namespace.QName("", "exchng");
    
    
    /**
     * Gets the "code" element
     */
    public java.lang.String getCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "code" element
     */
    public org.apache.xmlbeans.XmlString xgetCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "code" element
     */
    public void setCode(java.lang.String code)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CODE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CODE$0);
            }
            target.setStringValue(code);
        }
    }
    
    /**
     * Sets (as xml) the "code" element
     */
    public void xsetCode(org.apache.xmlbeans.XmlString code)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CODE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CODE$0);
            }
            target.set(code);
        }
    }
    
    /**
     * Gets the "exchng" element
     */
    public float getExchng()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EXCHNG$2, 0);
            if (target == null)
            {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }
    
    /**
     * Gets (as xml) the "exchng" element
     */
    public org.apache.xmlbeans.XmlFloat xgetExchng()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(EXCHNG$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "exchng" element
     */
    public void setExchng(float exchng)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EXCHNG$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EXCHNG$2);
            }
            target.setFloatValue(exchng);
        }
    }
    
    /**
     * Sets (as xml) the "exchng" element
     */
    public void xsetExchng(org.apache.xmlbeans.XmlFloat exchng)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(EXCHNG$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlFloat)get_store().add_element_user(EXCHNG$2);
            }
            target.set(exchng);
        }
    }
}

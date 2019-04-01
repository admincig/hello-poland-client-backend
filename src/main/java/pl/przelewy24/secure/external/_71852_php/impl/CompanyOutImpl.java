/*
 * XML Type:  CompanyOut
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.CompanyOut
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * An XML CompanyOut(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public class CompanyOutImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.CompanyOut
{
    private static final long serialVersionUID = 1L;
    
    public CompanyOutImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SPID$0 = 
        new javax.xml.namespace.QName("", "spid");
    private static final javax.xml.namespace.QName LINK$2 = 
        new javax.xml.namespace.QName("", "link");
    private static final javax.xml.namespace.QName CRC$4 = 
        new javax.xml.namespace.QName("", "crc");
    
    
    /**
     * Gets the "spid" element
     */
    public java.lang.String getSpid()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SPID$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "spid" element
     */
    public org.apache.xmlbeans.XmlString xgetSpid()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SPID$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "spid" element
     */
    public void setSpid(java.lang.String spid)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SPID$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SPID$0);
            }
            target.setStringValue(spid);
        }
    }
    
    /**
     * Sets (as xml) the "spid" element
     */
    public void xsetSpid(org.apache.xmlbeans.XmlString spid)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SPID$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SPID$0);
            }
            target.set(spid);
        }
    }
    
    /**
     * Gets the "link" element
     */
    public java.lang.String getLink()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LINK$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "link" element
     */
    public org.apache.xmlbeans.XmlString xgetLink()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(LINK$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "link" element
     */
    public void setLink(java.lang.String link)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LINK$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(LINK$2);
            }
            target.setStringValue(link);
        }
    }
    
    /**
     * Sets (as xml) the "link" element
     */
    public void xsetLink(org.apache.xmlbeans.XmlString link)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(LINK$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(LINK$2);
            }
            target.set(link);
        }
    }
    
    /**
     * Gets the "crc" element
     */
    public java.lang.String getCrc()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CRC$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "crc" element
     */
    public org.apache.xmlbeans.XmlString xgetCrc()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CRC$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "crc" element
     */
    public void setCrc(java.lang.String crc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CRC$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CRC$4);
            }
            target.setStringValue(crc);
        }
    }
    
    /**
     * Sets (as xml) the "crc" element
     */
    public void xsetCrc(org.apache.xmlbeans.XmlString crc)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CRC$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CRC$4);
            }
            target.set(crc);
        }
    }
}

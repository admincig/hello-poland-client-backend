/*
 * XML Type:  MethodAvailabilityHours
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * An XML MethodAvailabilityHours(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public class MethodAvailabilityHoursImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.MethodAvailabilityHours
{
    private static final long serialVersionUID = 1L;
    
    public MethodAvailabilityHoursImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MONDAYTOFRIDAY$0 = 
        new javax.xml.namespace.QName("", "mondayToFriday");
    private static final javax.xml.namespace.QName SATURDAY$2 = 
        new javax.xml.namespace.QName("", "saturday");
    private static final javax.xml.namespace.QName SUNDAY$4 = 
        new javax.xml.namespace.QName("", "sunday");
    
    
    /**
     * Gets the "mondayToFriday" element
     */
    public java.lang.String getMondayToFriday()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MONDAYTOFRIDAY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "mondayToFriday" element
     */
    public org.apache.xmlbeans.XmlString xgetMondayToFriday()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MONDAYTOFRIDAY$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "mondayToFriday" element
     */
    public void setMondayToFriday(java.lang.String mondayToFriday)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MONDAYTOFRIDAY$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MONDAYTOFRIDAY$0);
            }
            target.setStringValue(mondayToFriday);
        }
    }
    
    /**
     * Sets (as xml) the "mondayToFriday" element
     */
    public void xsetMondayToFriday(org.apache.xmlbeans.XmlString mondayToFriday)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MONDAYTOFRIDAY$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(MONDAYTOFRIDAY$0);
            }
            target.set(mondayToFriday);
        }
    }
    
    /**
     * Gets the "saturday" element
     */
    public java.lang.String getSaturday()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SATURDAY$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "saturday" element
     */
    public org.apache.xmlbeans.XmlString xgetSaturday()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SATURDAY$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "saturday" element
     */
    public void setSaturday(java.lang.String saturday)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SATURDAY$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SATURDAY$2);
            }
            target.setStringValue(saturday);
        }
    }
    
    /**
     * Sets (as xml) the "saturday" element
     */
    public void xsetSaturday(org.apache.xmlbeans.XmlString saturday)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SATURDAY$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SATURDAY$2);
            }
            target.set(saturday);
        }
    }
    
    /**
     * Gets the "sunday" element
     */
    public java.lang.String getSunday()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SUNDAY$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "sunday" element
     */
    public org.apache.xmlbeans.XmlString xgetSunday()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SUNDAY$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "sunday" element
     */
    public void setSunday(java.lang.String sunday)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SUNDAY$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SUNDAY$4);
            }
            target.setStringValue(sunday);
        }
    }
    
    /**
     * Sets (as xml) the "sunday" element
     */
    public void xsetSunday(org.apache.xmlbeans.XmlString sunday)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SUNDAY$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SUNDAY$4);
            }
            target.set(sunday);
        }
    }
}

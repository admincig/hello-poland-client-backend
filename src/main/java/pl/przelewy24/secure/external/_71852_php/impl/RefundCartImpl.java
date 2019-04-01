/*
 * XML Type:  RefundCart
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.RefundCart
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * An XML RefundCart(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public class RefundCartImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RefundCart
{
    private static final long serialVersionUID = 1L;
    
    public RefundCartImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName POSID$0 = 
        new javax.xml.namespace.QName("", "posId");
    private static final javax.xml.namespace.QName AMOUNTPOS$2 = 
        new javax.xml.namespace.QName("", "amountPos");
    private static final javax.xml.namespace.QName AMOUNTCLIENT$4 = 
        new javax.xml.namespace.QName("", "amountClient");
    
    
    /**
     * Gets the "posId" element
     */
    public int getPosId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POSID$0, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "posId" element
     */
    public org.apache.xmlbeans.XmlInt xgetPosId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(POSID$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "posId" element
     */
    public void setPosId(int posId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(POSID$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(POSID$0);
            }
            target.setIntValue(posId);
        }
    }
    
    /**
     * Sets (as xml) the "posId" element
     */
    public void xsetPosId(org.apache.xmlbeans.XmlInt posId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(POSID$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(POSID$0);
            }
            target.set(posId);
        }
    }
    
    /**
     * Gets the "amountPos" element
     */
    public int getAmountPos()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AMOUNTPOS$2, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "amountPos" element
     */
    public org.apache.xmlbeans.XmlInt xgetAmountPos()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(AMOUNTPOS$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "amountPos" element
     */
    public void setAmountPos(int amountPos)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AMOUNTPOS$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(AMOUNTPOS$2);
            }
            target.setIntValue(amountPos);
        }
    }
    
    /**
     * Sets (as xml) the "amountPos" element
     */
    public void xsetAmountPos(org.apache.xmlbeans.XmlInt amountPos)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(AMOUNTPOS$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(AMOUNTPOS$2);
            }
            target.set(amountPos);
        }
    }
    
    /**
     * Gets the "amountClient" element
     */
    public int getAmountClient()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AMOUNTCLIENT$4, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "amountClient" element
     */
    public org.apache.xmlbeans.XmlInt xgetAmountClient()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(AMOUNTCLIENT$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "amountClient" element
     */
    public void setAmountClient(int amountClient)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AMOUNTCLIENT$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(AMOUNTCLIENT$4);
            }
            target.setIntValue(amountClient);
        }
    }
    
    /**
     * Sets (as xml) the "amountClient" element
     */
    public void xsetAmountClient(org.apache.xmlbeans.XmlInt amountClient)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(AMOUNTCLIENT$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(AMOUNTCLIENT$4);
            }
            target.set(amountClient);
        }
    }
}

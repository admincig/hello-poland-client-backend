/*
 * XML Type:  DispatchTransactionTrn
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.DispatchTransactionTrn
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * An XML DispatchTransactionTrn(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public class DispatchTransactionTrnImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.DispatchTransactionTrn
{
    private static final long serialVersionUID = 1L;
    
    public DispatchTransactionTrnImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ORDERID$0 = 
        new javax.xml.namespace.QName("", "orderId");
    private static final javax.xml.namespace.QName AMOUNT$2 = 
        new javax.xml.namespace.QName("", "amount");
    private static final javax.xml.namespace.QName SPID$4 = 
        new javax.xml.namespace.QName("", "spId");
    private static final javax.xml.namespace.QName DATE$6 = 
        new javax.xml.namespace.QName("", "date");
    
    
    /**
     * Gets the "orderId" element
     */
    public int getOrderId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ORDERID$0, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "orderId" element
     */
    public org.apache.xmlbeans.XmlInt xgetOrderId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ORDERID$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "orderId" element
     */
    public void setOrderId(int orderId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ORDERID$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ORDERID$0);
            }
            target.setIntValue(orderId);
        }
    }
    
    /**
     * Sets (as xml) the "orderId" element
     */
    public void xsetOrderId(org.apache.xmlbeans.XmlInt orderId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ORDERID$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(ORDERID$0);
            }
            target.set(orderId);
        }
    }
    
    /**
     * Gets the "amount" element
     */
    public int getAmount()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AMOUNT$2, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "amount" element
     */
    public org.apache.xmlbeans.XmlInt xgetAmount()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(AMOUNT$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "amount" element
     */
    public void setAmount(int amount)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AMOUNT$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(AMOUNT$2);
            }
            target.setIntValue(amount);
        }
    }
    
    /**
     * Sets (as xml) the "amount" element
     */
    public void xsetAmount(org.apache.xmlbeans.XmlInt amount)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(AMOUNT$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(AMOUNT$2);
            }
            target.set(amount);
        }
    }
    
    /**
     * Gets the "spId" element
     */
    public int getSpId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SPID$4, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "spId" element
     */
    public org.apache.xmlbeans.XmlInt xgetSpId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(SPID$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "spId" element
     */
    public void setSpId(int spId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SPID$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SPID$4);
            }
            target.setIntValue(spId);
        }
    }
    
    /**
     * Sets (as xml) the "spId" element
     */
    public void xsetSpId(org.apache.xmlbeans.XmlInt spId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(SPID$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(SPID$4);
            }
            target.set(spId);
        }
    }
    
    /**
     * Gets the "date" element
     */
    public java.lang.String getDate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATE$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "date" element
     */
    public org.apache.xmlbeans.XmlString xgetDate()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATE$6, 0);
            return target;
        }
    }
    
    /**
     * Sets the "date" element
     */
    public void setDate(java.lang.String date)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATE$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATE$6);
            }
            target.setStringValue(date);
        }
    }
    
    /**
     * Sets (as xml) the "date" element
     */
    public void xsetDate(org.apache.xmlbeans.XmlString date)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATE$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATE$6);
            }
            target.set(date);
        }
    }
}

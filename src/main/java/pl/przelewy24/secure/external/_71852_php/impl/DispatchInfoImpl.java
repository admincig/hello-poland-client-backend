/*
 * XML Type:  DispatchInfo
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.DispatchInfo
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * An XML DispatchInfo(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public class DispatchInfoImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.DispatchInfo
{
    private static final long serialVersionUID = 1L;
    
    public DispatchInfoImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ORDERID$0 = 
        new javax.xml.namespace.QName("", "orderId");
    private static final javax.xml.namespace.QName ORDERIDNEW$2 = 
        new javax.xml.namespace.QName("", "orderIdNew");
    private static final javax.xml.namespace.QName SESSIONID$4 = 
        new javax.xml.namespace.QName("", "sessionId");
    private static final javax.xml.namespace.QName SELLERID$6 = 
        new javax.xml.namespace.QName("", "sellerId");
    private static final javax.xml.namespace.QName AMOUNT$8 = 
        new javax.xml.namespace.QName("", "amount");
    private static final javax.xml.namespace.QName STATUS$10 = 
        new javax.xml.namespace.QName("", "status");
    private static final javax.xml.namespace.QName ERROR$12 = 
        new javax.xml.namespace.QName("", "error");
    
    
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
     * Gets the "orderIdNew" element
     */
    public int getOrderIdNew()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ORDERIDNEW$2, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "orderIdNew" element
     */
    public org.apache.xmlbeans.XmlInt xgetOrderIdNew()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ORDERIDNEW$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "orderIdNew" element
     */
    public void setOrderIdNew(int orderIdNew)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ORDERIDNEW$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ORDERIDNEW$2);
            }
            target.setIntValue(orderIdNew);
        }
    }
    
    /**
     * Sets (as xml) the "orderIdNew" element
     */
    public void xsetOrderIdNew(org.apache.xmlbeans.XmlInt orderIdNew)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ORDERIDNEW$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(ORDERIDNEW$2);
            }
            target.set(orderIdNew);
        }
    }
    
    /**
     * Gets the "sessionId" element
     */
    public java.lang.String getSessionId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SESSIONID$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "sessionId" element
     */
    public org.apache.xmlbeans.XmlString xgetSessionId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SESSIONID$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "sessionId" element
     */
    public void setSessionId(java.lang.String sessionId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SESSIONID$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SESSIONID$4);
            }
            target.setStringValue(sessionId);
        }
    }
    
    /**
     * Sets (as xml) the "sessionId" element
     */
    public void xsetSessionId(org.apache.xmlbeans.XmlString sessionId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SESSIONID$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SESSIONID$4);
            }
            target.set(sessionId);
        }
    }
    
    /**
     * Gets the "sellerId" element
     */
    public int getSellerId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SELLERID$6, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "sellerId" element
     */
    public org.apache.xmlbeans.XmlInt xgetSellerId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(SELLERID$6, 0);
            return target;
        }
    }
    
    /**
     * Sets the "sellerId" element
     */
    public void setSellerId(int sellerId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SELLERID$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SELLERID$6);
            }
            target.setIntValue(sellerId);
        }
    }
    
    /**
     * Sets (as xml) the "sellerId" element
     */
    public void xsetSellerId(org.apache.xmlbeans.XmlInt sellerId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(SELLERID$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(SELLERID$6);
            }
            target.set(sellerId);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AMOUNT$8, 0);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(AMOUNT$8, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AMOUNT$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(AMOUNT$8);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(AMOUNT$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(AMOUNT$8);
            }
            target.set(amount);
        }
    }
    
    /**
     * Gets the "status" element
     */
    public boolean getStatus()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(STATUS$10, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "status" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetStatus()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(STATUS$10, 0);
            return target;
        }
    }
    
    /**
     * Sets the "status" element
     */
    public void setStatus(boolean status)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(STATUS$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(STATUS$10);
            }
            target.setBooleanValue(status);
        }
    }
    
    /**
     * Sets (as xml) the "status" element
     */
    public void xsetStatus(org.apache.xmlbeans.XmlBoolean status)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(STATUS$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(STATUS$10);
            }
            target.set(status);
        }
    }
    
    /**
     * Gets the "error" element
     */
    public java.lang.String getError()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERROR$12, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "error" element
     */
    public org.apache.xmlbeans.XmlString xgetError()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ERROR$12, 0);
            return target;
        }
    }
    
    /**
     * Sets the "error" element
     */
    public void setError(java.lang.String error)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ERROR$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ERROR$12);
            }
            target.setStringValue(error);
        }
    }
    
    /**
     * Sets (as xml) the "error" element
     */
    public void xsetError(org.apache.xmlbeans.XmlString error)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ERROR$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ERROR$12);
            }
            target.set(error);
        }
    }
}

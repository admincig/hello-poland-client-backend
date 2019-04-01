/*
 * XML Type:  Transaction
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.Transaction
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * An XML Transaction(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public class TransactionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.Transaction
{
    private static final long serialVersionUID = 1L;
    
    public TransactionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName ORDERID$0 = 
        new javax.xml.namespace.QName("", "orderId");
    private static final javax.xml.namespace.QName SESSIONID$2 = 
        new javax.xml.namespace.QName("", "sessionId");
    private static final javax.xml.namespace.QName STATUS$4 = 
        new javax.xml.namespace.QName("", "status");
    private static final javax.xml.namespace.QName AMOUNT$6 = 
        new javax.xml.namespace.QName("", "amount");
    private static final javax.xml.namespace.QName CURRENCY$8 = 
        new javax.xml.namespace.QName("", "currency");
    private static final javax.xml.namespace.QName DATE$10 = 
        new javax.xml.namespace.QName("", "date");
    private static final javax.xml.namespace.QName DATEOFTRANSACTION$12 = 
        new javax.xml.namespace.QName("", "dateOfTransaction");
    private static final javax.xml.namespace.QName CLIENTEMAIL$14 = 
        new javax.xml.namespace.QName("", "clientEmail");
    private static final javax.xml.namespace.QName ACCOUNTMD5$16 = 
        new javax.xml.namespace.QName("", "accountMD5");
    private static final javax.xml.namespace.QName PAYMENTMETHOD$18 = 
        new javax.xml.namespace.QName("", "paymentMethod");
    private static final javax.xml.namespace.QName DESCRIPTION$20 = 
        new javax.xml.namespace.QName("", "description");
    private static final javax.xml.namespace.QName CLIENTNAME$22 = 
        new javax.xml.namespace.QName("", "clientName");
    private static final javax.xml.namespace.QName CLIENTADDRESS$24 = 
        new javax.xml.namespace.QName("", "clientAddress");
    private static final javax.xml.namespace.QName CLIENTCITY$26 = 
        new javax.xml.namespace.QName("", "clientCity");
    private static final javax.xml.namespace.QName CLIENTPOSTCODE$28 = 
        new javax.xml.namespace.QName("", "clientPostcode");
    private static final javax.xml.namespace.QName BATCHID$30 = 
        new javax.xml.namespace.QName("", "batchId");
    private static final javax.xml.namespace.QName FEE$32 = 
        new javax.xml.namespace.QName("", "fee");
    private static final javax.xml.namespace.QName STATEMENT$34 = 
        new javax.xml.namespace.QName("", "statement");
    
    
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
     * Gets the "sessionId" element
     */
    public java.lang.String getSessionId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SESSIONID$2, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SESSIONID$2, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SESSIONID$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SESSIONID$2);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SESSIONID$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SESSIONID$2);
            }
            target.set(sessionId);
        }
    }
    
    /**
     * Gets the "status" element
     */
    public int getStatus()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(STATUS$4, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "status" element
     */
    public org.apache.xmlbeans.XmlInt xgetStatus()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(STATUS$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "status" element
     */
    public void setStatus(int status)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(STATUS$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(STATUS$4);
            }
            target.setIntValue(status);
        }
    }
    
    /**
     * Sets (as xml) the "status" element
     */
    public void xsetStatus(org.apache.xmlbeans.XmlInt status)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(STATUS$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(STATUS$4);
            }
            target.set(status);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AMOUNT$6, 0);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(AMOUNT$6, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AMOUNT$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(AMOUNT$6);
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
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(AMOUNT$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(AMOUNT$6);
            }
            target.set(amount);
        }
    }
    
    /**
     * Gets the "currency" element
     */
    public java.lang.String getCurrency()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CURRENCY$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "currency" element
     */
    public org.apache.xmlbeans.XmlString xgetCurrency()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CURRENCY$8, 0);
            return target;
        }
    }
    
    /**
     * Sets the "currency" element
     */
    public void setCurrency(java.lang.String currency)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CURRENCY$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CURRENCY$8);
            }
            target.setStringValue(currency);
        }
    }
    
    /**
     * Sets (as xml) the "currency" element
     */
    public void xsetCurrency(org.apache.xmlbeans.XmlString currency)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CURRENCY$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CURRENCY$8);
            }
            target.set(currency);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATE$10, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATE$10, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATE$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATE$10);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATE$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATE$10);
            }
            target.set(date);
        }
    }
    
    /**
     * Gets the "dateOfTransaction" element
     */
    public java.lang.String getDateOfTransaction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATEOFTRANSACTION$12, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "dateOfTransaction" element
     */
    public org.apache.xmlbeans.XmlString xgetDateOfTransaction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATEOFTRANSACTION$12, 0);
            return target;
        }
    }
    
    /**
     * Sets the "dateOfTransaction" element
     */
    public void setDateOfTransaction(java.lang.String dateOfTransaction)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DATEOFTRANSACTION$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DATEOFTRANSACTION$12);
            }
            target.setStringValue(dateOfTransaction);
        }
    }
    
    /**
     * Sets (as xml) the "dateOfTransaction" element
     */
    public void xsetDateOfTransaction(org.apache.xmlbeans.XmlString dateOfTransaction)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DATEOFTRANSACTION$12, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DATEOFTRANSACTION$12);
            }
            target.set(dateOfTransaction);
        }
    }
    
    /**
     * Gets the "clientEmail" element
     */
    public java.lang.String getClientEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLIENTEMAIL$14, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "clientEmail" element
     */
    public org.apache.xmlbeans.XmlString xgetClientEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CLIENTEMAIL$14, 0);
            return target;
        }
    }
    
    /**
     * Sets the "clientEmail" element
     */
    public void setClientEmail(java.lang.String clientEmail)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLIENTEMAIL$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CLIENTEMAIL$14);
            }
            target.setStringValue(clientEmail);
        }
    }
    
    /**
     * Sets (as xml) the "clientEmail" element
     */
    public void xsetClientEmail(org.apache.xmlbeans.XmlString clientEmail)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CLIENTEMAIL$14, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CLIENTEMAIL$14);
            }
            target.set(clientEmail);
        }
    }
    
    /**
     * Gets the "accountMD5" element
     */
    public java.lang.String getAccountMD5()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ACCOUNTMD5$16, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "accountMD5" element
     */
    public org.apache.xmlbeans.XmlString xgetAccountMD5()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ACCOUNTMD5$16, 0);
            return target;
        }
    }
    
    /**
     * Sets the "accountMD5" element
     */
    public void setAccountMD5(java.lang.String accountMD5)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ACCOUNTMD5$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ACCOUNTMD5$16);
            }
            target.setStringValue(accountMD5);
        }
    }
    
    /**
     * Sets (as xml) the "accountMD5" element
     */
    public void xsetAccountMD5(org.apache.xmlbeans.XmlString accountMD5)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(ACCOUNTMD5$16, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(ACCOUNTMD5$16);
            }
            target.set(accountMD5);
        }
    }
    
    /**
     * Gets the "paymentMethod" element
     */
    public int getPaymentMethod()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PAYMENTMETHOD$18, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "paymentMethod" element
     */
    public org.apache.xmlbeans.XmlInt xgetPaymentMethod()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PAYMENTMETHOD$18, 0);
            return target;
        }
    }
    
    /**
     * Sets the "paymentMethod" element
     */
    public void setPaymentMethod(int paymentMethod)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PAYMENTMETHOD$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PAYMENTMETHOD$18);
            }
            target.setIntValue(paymentMethod);
        }
    }
    
    /**
     * Sets (as xml) the "paymentMethod" element
     */
    public void xsetPaymentMethod(org.apache.xmlbeans.XmlInt paymentMethod)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(PAYMENTMETHOD$18, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(PAYMENTMETHOD$18);
            }
            target.set(paymentMethod);
        }
    }
    
    /**
     * Gets the "description" element
     */
    public java.lang.String getDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRIPTION$20, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "description" element
     */
    public org.apache.xmlbeans.XmlString xgetDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRIPTION$20, 0);
            return target;
        }
    }
    
    /**
     * Sets the "description" element
     */
    public void setDescription(java.lang.String description)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRIPTION$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DESCRIPTION$20);
            }
            target.setStringValue(description);
        }
    }
    
    /**
     * Sets (as xml) the "description" element
     */
    public void xsetDescription(org.apache.xmlbeans.XmlString description)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRIPTION$20, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRIPTION$20);
            }
            target.set(description);
        }
    }
    
    /**
     * Gets the "clientName" element
     */
    public java.lang.String getClientName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLIENTNAME$22, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "clientName" element
     */
    public org.apache.xmlbeans.XmlString xgetClientName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CLIENTNAME$22, 0);
            return target;
        }
    }
    
    /**
     * Sets the "clientName" element
     */
    public void setClientName(java.lang.String clientName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLIENTNAME$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CLIENTNAME$22);
            }
            target.setStringValue(clientName);
        }
    }
    
    /**
     * Sets (as xml) the "clientName" element
     */
    public void xsetClientName(org.apache.xmlbeans.XmlString clientName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CLIENTNAME$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CLIENTNAME$22);
            }
            target.set(clientName);
        }
    }
    
    /**
     * Gets the "clientAddress" element
     */
    public java.lang.String getClientAddress()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLIENTADDRESS$24, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "clientAddress" element
     */
    public org.apache.xmlbeans.XmlString xgetClientAddress()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CLIENTADDRESS$24, 0);
            return target;
        }
    }
    
    /**
     * Sets the "clientAddress" element
     */
    public void setClientAddress(java.lang.String clientAddress)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLIENTADDRESS$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CLIENTADDRESS$24);
            }
            target.setStringValue(clientAddress);
        }
    }
    
    /**
     * Sets (as xml) the "clientAddress" element
     */
    public void xsetClientAddress(org.apache.xmlbeans.XmlString clientAddress)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CLIENTADDRESS$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CLIENTADDRESS$24);
            }
            target.set(clientAddress);
        }
    }
    
    /**
     * Gets the "clientCity" element
     */
    public java.lang.String getClientCity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLIENTCITY$26, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "clientCity" element
     */
    public org.apache.xmlbeans.XmlString xgetClientCity()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CLIENTCITY$26, 0);
            return target;
        }
    }
    
    /**
     * Sets the "clientCity" element
     */
    public void setClientCity(java.lang.String clientCity)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLIENTCITY$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CLIENTCITY$26);
            }
            target.setStringValue(clientCity);
        }
    }
    
    /**
     * Sets (as xml) the "clientCity" element
     */
    public void xsetClientCity(org.apache.xmlbeans.XmlString clientCity)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CLIENTCITY$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CLIENTCITY$26);
            }
            target.set(clientCity);
        }
    }
    
    /**
     * Gets the "clientPostcode" element
     */
    public java.lang.String getClientPostcode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLIENTPOSTCODE$28, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "clientPostcode" element
     */
    public org.apache.xmlbeans.XmlString xgetClientPostcode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CLIENTPOSTCODE$28, 0);
            return target;
        }
    }
    
    /**
     * Sets the "clientPostcode" element
     */
    public void setClientPostcode(java.lang.String clientPostcode)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLIENTPOSTCODE$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CLIENTPOSTCODE$28);
            }
            target.setStringValue(clientPostcode);
        }
    }
    
    /**
     * Sets (as xml) the "clientPostcode" element
     */
    public void xsetClientPostcode(org.apache.xmlbeans.XmlString clientPostcode)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CLIENTPOSTCODE$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CLIENTPOSTCODE$28);
            }
            target.set(clientPostcode);
        }
    }
    
    /**
     * Gets the "batchId" element
     */
    public int getBatchId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BATCHID$30, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "batchId" element
     */
    public org.apache.xmlbeans.XmlInt xgetBatchId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BATCHID$30, 0);
            return target;
        }
    }
    
    /**
     * Sets the "batchId" element
     */
    public void setBatchId(int batchId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BATCHID$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BATCHID$30);
            }
            target.setIntValue(batchId);
        }
    }
    
    /**
     * Sets (as xml) the "batchId" element
     */
    public void xsetBatchId(org.apache.xmlbeans.XmlInt batchId)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BATCHID$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(BATCHID$30);
            }
            target.set(batchId);
        }
    }
    
    /**
     * Gets the "fee" element
     */
    public int getFee()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FEE$32, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "fee" element
     */
    public org.apache.xmlbeans.XmlInt xgetFee()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(FEE$32, 0);
            return target;
        }
    }
    
    /**
     * Sets the "fee" element
     */
    public void setFee(int fee)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FEE$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(FEE$32);
            }
            target.setIntValue(fee);
        }
    }
    
    /**
     * Sets (as xml) the "fee" element
     */
    public void xsetFee(org.apache.xmlbeans.XmlInt fee)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(FEE$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(FEE$32);
            }
            target.set(fee);
        }
    }
    
    /**
     * Gets the "statement" element
     */
    public java.lang.String getStatement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(STATEMENT$34, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "statement" element
     */
    public org.apache.xmlbeans.XmlString xgetStatement()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(STATEMENT$34, 0);
            return target;
        }
    }
    
    /**
     * Sets the "statement" element
     */
    public void setStatement(java.lang.String statement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(STATEMENT$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(STATEMENT$34);
            }
            target.setStringValue(statement);
        }
    }
    
    /**
     * Sets (as xml) the "statement" element
     */
    public void xsetStatement(org.apache.xmlbeans.XmlString statement)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(STATEMENT$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(STATEMENT$34);
            }
            target.set(statement);
        }
    }
}

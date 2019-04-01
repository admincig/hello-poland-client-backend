/*
 * An XML document type.
 * Localname: RefundTransaction
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.RefundTransactionDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one RefundTransaction(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class RefundTransactionDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RefundTransactionDocument
{
    private static final long serialVersionUID = 1L;
    
    public RefundTransactionDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REFUNDTRANSACTION$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "RefundTransaction");
    
    
    /**
     * Gets the "RefundTransaction" element
     */
    public pl.przelewy24.secure.external._71852_php.RefundTransactionDocument.RefundTransaction getRefundTransaction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.RefundTransactionDocument.RefundTransaction target = null;
            target = (pl.przelewy24.secure.external._71852_php.RefundTransactionDocument.RefundTransaction)get_store().find_element_user(REFUNDTRANSACTION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "RefundTransaction" element
     */
    public void setRefundTransaction(pl.przelewy24.secure.external._71852_php.RefundTransactionDocument.RefundTransaction refundTransaction)
    {
        generatedSetterHelperImpl(refundTransaction, REFUNDTRANSACTION$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "RefundTransaction" element
     */
    public pl.przelewy24.secure.external._71852_php.RefundTransactionDocument.RefundTransaction addNewRefundTransaction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.RefundTransactionDocument.RefundTransaction target = null;
            target = (pl.przelewy24.secure.external._71852_php.RefundTransactionDocument.RefundTransaction)get_store().add_element_user(REFUNDTRANSACTION$0);
            return target;
        }
    }
    /**
     * An XML RefundTransaction(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class RefundTransactionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RefundTransactionDocument.RefundTransaction
    {
        private static final long serialVersionUID = 1L;
        
        public RefundTransactionImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName LOGIN$0 = 
            new javax.xml.namespace.QName("", "login");
        private static final javax.xml.namespace.QName PASS$2 = 
            new javax.xml.namespace.QName("", "pass");
        private static final javax.xml.namespace.QName REQUESTID$4 = 
            new javax.xml.namespace.QName("", "requestId");
        private static final javax.xml.namespace.QName LIST$6 = 
            new javax.xml.namespace.QName("", "list");
        
        
        /**
         * Gets the "login" element
         */
        public java.lang.String getLogin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LOGIN$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "login" element
         */
        public org.apache.xmlbeans.XmlString xgetLogin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(LOGIN$0, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "login" element
         */
        public boolean isNilLogin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(LOGIN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "login" element
         */
        public void setLogin(java.lang.String login)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LOGIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(LOGIN$0);
                }
                target.setStringValue(login);
            }
        }
        
        /**
         * Sets (as xml) the "login" element
         */
        public void xsetLogin(org.apache.xmlbeans.XmlString login)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(LOGIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(LOGIN$0);
                }
                target.set(login);
            }
        }
        
        /**
         * Nils the "login" element
         */
        public void setNilLogin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(LOGIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(LOGIN$0);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "pass" element
         */
        public java.lang.String getPass()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PASS$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "pass" element
         */
        public org.apache.xmlbeans.XmlString xgetPass()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PASS$2, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "pass" element
         */
        public boolean isNilPass()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PASS$2, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "pass" element
         */
        public void setPass(java.lang.String pass)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PASS$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PASS$2);
                }
                target.setStringValue(pass);
            }
        }
        
        /**
         * Sets (as xml) the "pass" element
         */
        public void xsetPass(org.apache.xmlbeans.XmlString pass)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PASS$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PASS$2);
                }
                target.set(pass);
            }
        }
        
        /**
         * Nils the "pass" element
         */
        public void setNilPass()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PASS$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PASS$2);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "requestId" element
         */
        public int getRequestId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REQUESTID$4, 0);
                if (target == null)
                {
                    return 0;
                }
                return target.getIntValue();
            }
        }
        
        /**
         * Gets (as xml) the "requestId" element
         */
        public org.apache.xmlbeans.XmlInt xgetRequestId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(REQUESTID$4, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "requestId" element
         */
        public boolean isNilRequestId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(REQUESTID$4, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "requestId" element
         */
        public void setRequestId(int requestId)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REQUESTID$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(REQUESTID$4);
                }
                target.setIntValue(requestId);
            }
        }
        
        /**
         * Sets (as xml) the "requestId" element
         */
        public void xsetRequestId(org.apache.xmlbeans.XmlInt requestId)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(REQUESTID$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(REQUESTID$4);
                }
                target.set(requestId);
            }
        }
        
        /**
         * Nils the "requestId" element
         */
        public void setNilRequestId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(REQUESTID$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(REQUESTID$4);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "list" element
         */
        public pl.przelewy24.secure.external._71852_php.ArrayOfRefund getList()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfRefund target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfRefund)get_store().find_element_user(LIST$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Tests for nil "list" element
         */
        public boolean isNilList()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfRefund target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfRefund)get_store().find_element_user(LIST$6, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "list" element
         */
        public void setList(pl.przelewy24.secure.external._71852_php.ArrayOfRefund list)
        {
            generatedSetterHelperImpl(list, LIST$6, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "list" element
         */
        public pl.przelewy24.secure.external._71852_php.ArrayOfRefund addNewList()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfRefund target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfRefund)get_store().add_element_user(LIST$6);
                return target;
            }
        }
        
        /**
         * Nils the "list" element
         */
        public void setNilList()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfRefund target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfRefund)get_store().find_element_user(LIST$6, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.ArrayOfRefund)get_store().add_element_user(LIST$6);
                }
                target.setNil();
            }
        }
    }
}

/*
 * An XML document type.
 * Localname: DispatchTransaction
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.DispatchTransactionDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one DispatchTransaction(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class DispatchTransactionDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.DispatchTransactionDocument
{
    private static final long serialVersionUID = 1L;
    
    public DispatchTransactionDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DISPATCHTRANSACTION$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "DispatchTransaction");
    
    
    /**
     * Gets the "DispatchTransaction" element
     */
    public pl.przelewy24.secure.external._71852_php.DispatchTransactionDocument.DispatchTransaction getDispatchTransaction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.DispatchTransactionDocument.DispatchTransaction target = null;
            target = (pl.przelewy24.secure.external._71852_php.DispatchTransactionDocument.DispatchTransaction)get_store().find_element_user(DISPATCHTRANSACTION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "DispatchTransaction" element
     */
    public void setDispatchTransaction(pl.przelewy24.secure.external._71852_php.DispatchTransactionDocument.DispatchTransaction dispatchTransaction)
    {
        generatedSetterHelperImpl(dispatchTransaction, DISPATCHTRANSACTION$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "DispatchTransaction" element
     */
    public pl.przelewy24.secure.external._71852_php.DispatchTransactionDocument.DispatchTransaction addNewDispatchTransaction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.DispatchTransactionDocument.DispatchTransaction target = null;
            target = (pl.przelewy24.secure.external._71852_php.DispatchTransactionDocument.DispatchTransaction)get_store().add_element_user(DISPATCHTRANSACTION$0);
            return target;
        }
    }
    /**
     * An XML DispatchTransaction(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class DispatchTransactionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.DispatchTransactionDocument.DispatchTransaction
    {
        private static final long serialVersionUID = 1L;
        
        public DispatchTransactionImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName LOGIN$0 = 
            new javax.xml.namespace.QName("", "login");
        private static final javax.xml.namespace.QName PASS$2 = 
            new javax.xml.namespace.QName("", "pass");
        private static final javax.xml.namespace.QName BATCHID$4 = 
            new javax.xml.namespace.QName("", "batchId");
        private static final javax.xml.namespace.QName DETAILS$6 = 
            new javax.xml.namespace.QName("", "details");
        
        
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
         * Gets the "batchId" element
         */
        public int getBatchId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BATCHID$4, 0);
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
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BATCHID$4, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "batchId" element
         */
        public boolean isNilBatchId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BATCHID$4, 0);
                if (target == null) return false;
                return target.isNil();
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BATCHID$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BATCHID$4);
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
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BATCHID$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(BATCHID$4);
                }
                target.set(batchId);
            }
        }
        
        /**
         * Nils the "batchId" element
         */
        public void setNilBatchId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BATCHID$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(BATCHID$4);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "details" element
         */
        public pl.przelewy24.secure.external._71852_php.ArrayOfDispatch getDetails()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfDispatch target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfDispatch)get_store().find_element_user(DETAILS$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Tests for nil "details" element
         */
        public boolean isNilDetails()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfDispatch target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfDispatch)get_store().find_element_user(DETAILS$6, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "details" element
         */
        public void setDetails(pl.przelewy24.secure.external._71852_php.ArrayOfDispatch details)
        {
            generatedSetterHelperImpl(details, DETAILS$6, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "details" element
         */
        public pl.przelewy24.secure.external._71852_php.ArrayOfDispatch addNewDetails()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfDispatch target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfDispatch)get_store().add_element_user(DETAILS$6);
                return target;
            }
        }
        
        /**
         * Nils the "details" element
         */
        public void setNilDetails()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfDispatch target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfDispatch)get_store().find_element_user(DETAILS$6, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.ArrayOfDispatch)get_store().add_element_user(DETAILS$6);
                }
                target.setNil();
            }
        }
    }
}

/*
 * An XML document type.
 * Localname: GetTransactionBySessionId
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one GetTransactionBySessionId(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class GetTransactionBySessionIdDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdDocument
{
    private static final long serialVersionUID = 1L;
    
    public GetTransactionBySessionIdDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName GETTRANSACTIONBYSESSIONID$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "GetTransactionBySessionId");
    
    
    /**
     * Gets the "GetTransactionBySessionId" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdDocument.GetTransactionBySessionId getGetTransactionBySessionId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdDocument.GetTransactionBySessionId target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdDocument.GetTransactionBySessionId)get_store().find_element_user(GETTRANSACTIONBYSESSIONID$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "GetTransactionBySessionId" element
     */
    public void setGetTransactionBySessionId(pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdDocument.GetTransactionBySessionId getTransactionBySessionId)
    {
        generatedSetterHelperImpl(getTransactionBySessionId, GETTRANSACTIONBYSESSIONID$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "GetTransactionBySessionId" element
     */
    public pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdDocument.GetTransactionBySessionId addNewGetTransactionBySessionId()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdDocument.GetTransactionBySessionId target = null;
            target = (pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdDocument.GetTransactionBySessionId)get_store().add_element_user(GETTRANSACTIONBYSESSIONID$0);
            return target;
        }
    }
    /**
     * An XML GetTransactionBySessionId(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class GetTransactionBySessionIdImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.GetTransactionBySessionIdDocument.GetTransactionBySessionId
    {
        private static final long serialVersionUID = 1L;
        
        public GetTransactionBySessionIdImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName LOGIN$0 = 
            new javax.xml.namespace.QName("", "login");
        private static final javax.xml.namespace.QName PASS$2 = 
            new javax.xml.namespace.QName("", "pass");
        private static final javax.xml.namespace.QName SESSIONID$4 = 
            new javax.xml.namespace.QName("", "sessionId");
        
        
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
         * Tests for nil "sessionId" element
         */
        public boolean isNilSessionId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SESSIONID$4, 0);
                if (target == null) return false;
                return target.isNil();
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
         * Nils the "sessionId" element
         */
        public void setNilSessionId()
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
                target.setNil();
            }
        }
    }
}

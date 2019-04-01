/*
 * An XML document type.
 * Localname: CheckNIP
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.CheckNIPDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one CheckNIP(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class CheckNIPDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.CheckNIPDocument
{
    private static final long serialVersionUID = 1L;
    
    public CheckNIPDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CHECKNIP$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "CheckNIP");
    
    
    /**
     * Gets the "CheckNIP" element
     */
    public pl.przelewy24.secure.external._71852_php.CheckNIPDocument.CheckNIP getCheckNIP()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.CheckNIPDocument.CheckNIP target = null;
            target = (pl.przelewy24.secure.external._71852_php.CheckNIPDocument.CheckNIP)get_store().find_element_user(CHECKNIP$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CheckNIP" element
     */
    public void setCheckNIP(pl.przelewy24.secure.external._71852_php.CheckNIPDocument.CheckNIP checkNIP)
    {
        generatedSetterHelperImpl(checkNIP, CHECKNIP$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "CheckNIP" element
     */
    public pl.przelewy24.secure.external._71852_php.CheckNIPDocument.CheckNIP addNewCheckNIP()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.CheckNIPDocument.CheckNIP target = null;
            target = (pl.przelewy24.secure.external._71852_php.CheckNIPDocument.CheckNIP)get_store().add_element_user(CHECKNIP$0);
            return target;
        }
    }
    /**
     * An XML CheckNIP(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class CheckNIPImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.CheckNIPDocument.CheckNIP
    {
        private static final long serialVersionUID = 1L;
        
        public CheckNIPImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName LOGIN$0 = 
            new javax.xml.namespace.QName("", "login");
        private static final javax.xml.namespace.QName PASS$2 = 
            new javax.xml.namespace.QName("", "pass");
        private static final javax.xml.namespace.QName NIP$4 = 
            new javax.xml.namespace.QName("", "nip");
        
        
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
         * Gets the "nip" element
         */
        public java.lang.String getNip()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NIP$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "nip" element
         */
        public org.apache.xmlbeans.XmlString xgetNip()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NIP$4, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "nip" element
         */
        public boolean isNilNip()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NIP$4, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "nip" element
         */
        public void setNip(java.lang.String nip)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NIP$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NIP$4);
                }
                target.setStringValue(nip);
            }
        }
        
        /**
         * Sets (as xml) the "nip" element
         */
        public void xsetNip(org.apache.xmlbeans.XmlString nip)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NIP$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NIP$4);
                }
                target.set(nip);
            }
        }
        
        /**
         * Nils the "nip" element
         */
        public void setNilNip()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NIP$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NIP$4);
                }
                target.setNil();
            }
        }
    }
}

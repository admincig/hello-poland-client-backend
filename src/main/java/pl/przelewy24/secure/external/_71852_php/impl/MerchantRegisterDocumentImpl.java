/*
 * An XML document type.
 * Localname: MerchantRegister
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.MerchantRegisterDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one MerchantRegister(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class MerchantRegisterDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.MerchantRegisterDocument
{
    private static final long serialVersionUID = 1L;
    
    public MerchantRegisterDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MERCHANTREGISTER$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "MerchantRegister");
    
    
    /**
     * Gets the "MerchantRegister" element
     */
    public pl.przelewy24.secure.external._71852_php.MerchantRegisterDocument.MerchantRegister getMerchantRegister()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.MerchantRegisterDocument.MerchantRegister target = null;
            target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterDocument.MerchantRegister)get_store().find_element_user(MERCHANTREGISTER$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "MerchantRegister" element
     */
    public void setMerchantRegister(pl.przelewy24.secure.external._71852_php.MerchantRegisterDocument.MerchantRegister merchantRegister)
    {
        generatedSetterHelperImpl(merchantRegister, MERCHANTREGISTER$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "MerchantRegister" element
     */
    public pl.przelewy24.secure.external._71852_php.MerchantRegisterDocument.MerchantRegister addNewMerchantRegister()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.MerchantRegisterDocument.MerchantRegister target = null;
            target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterDocument.MerchantRegister)get_store().add_element_user(MERCHANTREGISTER$0);
            return target;
        }
    }
    /**
     * An XML MerchantRegister(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class MerchantRegisterImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.MerchantRegisterDocument.MerchantRegister
    {
        private static final long serialVersionUID = 1L;
        
        public MerchantRegisterImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName LOGIN$0 = 
            new javax.xml.namespace.QName("", "login");
        private static final javax.xml.namespace.QName PASS$2 = 
            new javax.xml.namespace.QName("", "pass");
        private static final javax.xml.namespace.QName MERCHANT$4 = 
            new javax.xml.namespace.QName("", "merchant");
        
        
        /**
         * Gets the "login" element
         */
        public int getLogin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LOGIN$0, 0);
                if (target == null)
                {
                    return 0;
                }
                return target.getIntValue();
            }
        }
        
        /**
         * Gets (as xml) the "login" element
         */
        public org.apache.xmlbeans.XmlInt xgetLogin()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(LOGIN$0, 0);
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
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(LOGIN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "login" element
         */
        public void setLogin(int login)
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
                target.setIntValue(login);
            }
        }
        
        /**
         * Sets (as xml) the "login" element
         */
        public void xsetLogin(org.apache.xmlbeans.XmlInt login)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(LOGIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(LOGIN$0);
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
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(LOGIN$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(LOGIN$0);
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
         * Gets the "merchant" element
         */
        public pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest getMerchant()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest)get_store().find_element_user(MERCHANT$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Tests for nil "merchant" element
         */
        public boolean isNilMerchant()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest)get_store().find_element_user(MERCHANT$4, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "merchant" element
         */
        public void setMerchant(pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest merchant)
        {
            generatedSetterHelperImpl(merchant, MERCHANT$4, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "merchant" element
         */
        public pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest addNewMerchant()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest)get_store().add_element_user(MERCHANT$4);
                return target;
            }
        }
        
        /**
         * Nils the "merchant" element
         */
        public void setNilMerchant()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest target = null;
                target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest)get_store().find_element_user(MERCHANT$4, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest)get_store().add_element_user(MERCHANT$4);
                }
                target.setNil();
            }
        }
    }
}

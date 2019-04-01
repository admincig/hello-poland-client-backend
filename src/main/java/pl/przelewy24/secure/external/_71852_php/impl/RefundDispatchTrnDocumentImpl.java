/*
 * An XML document type.
 * Localname: RefundDispatchTrn
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.RefundDispatchTrnDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one RefundDispatchTrn(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class RefundDispatchTrnDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RefundDispatchTrnDocument
{
    private static final long serialVersionUID = 1L;
    
    public RefundDispatchTrnDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName REFUNDDISPATCHTRN$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "RefundDispatchTrn");
    
    
    /**
     * Gets the "RefundDispatchTrn" element
     */
    public pl.przelewy24.secure.external._71852_php.RefundDispatchTrnDocument.RefundDispatchTrn getRefundDispatchTrn()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.RefundDispatchTrnDocument.RefundDispatchTrn target = null;
            target = (pl.przelewy24.secure.external._71852_php.RefundDispatchTrnDocument.RefundDispatchTrn)get_store().find_element_user(REFUNDDISPATCHTRN$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "RefundDispatchTrn" element
     */
    public void setRefundDispatchTrn(pl.przelewy24.secure.external._71852_php.RefundDispatchTrnDocument.RefundDispatchTrn refundDispatchTrn)
    {
        generatedSetterHelperImpl(refundDispatchTrn, REFUNDDISPATCHTRN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "RefundDispatchTrn" element
     */
    public pl.przelewy24.secure.external._71852_php.RefundDispatchTrnDocument.RefundDispatchTrn addNewRefundDispatchTrn()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.RefundDispatchTrnDocument.RefundDispatchTrn target = null;
            target = (pl.przelewy24.secure.external._71852_php.RefundDispatchTrnDocument.RefundDispatchTrn)get_store().add_element_user(REFUNDDISPATCHTRN$0);
            return target;
        }
    }
    /**
     * An XML RefundDispatchTrn(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class RefundDispatchTrnImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.RefundDispatchTrnDocument.RefundDispatchTrn
    {
        private static final long serialVersionUID = 1L;
        
        public RefundDispatchTrnImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName LOGIN$0 = 
            new javax.xml.namespace.QName("", "login");
        private static final javax.xml.namespace.QName PASS$2 = 
            new javax.xml.namespace.QName("", "pass");
        private static final javax.xml.namespace.QName ORDERID$4 = 
            new javax.xml.namespace.QName("", "orderId");
        private static final javax.xml.namespace.QName BATCH$6 = 
            new javax.xml.namespace.QName("", "batch");
        private static final javax.xml.namespace.QName REFUNDCART$8 = 
            new javax.xml.namespace.QName("", "refundCart");
        
        
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
         * Gets the "orderId" element
         */
        public int getOrderId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ORDERID$4, 0);
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
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ORDERID$4, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "orderId" element
         */
        public boolean isNilOrderId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ORDERID$4, 0);
                if (target == null) return false;
                return target.isNil();
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
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ORDERID$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ORDERID$4);
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
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ORDERID$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(ORDERID$4);
                }
                target.set(orderId);
            }
        }
        
        /**
         * Nils the "orderId" element
         */
        public void setNilOrderId()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ORDERID$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(ORDERID$4);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "batch" element
         */
        public int getBatch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BATCH$6, 0);
                if (target == null)
                {
                    return 0;
                }
                return target.getIntValue();
            }
        }
        
        /**
         * Gets (as xml) the "batch" element
         */
        public org.apache.xmlbeans.XmlInt xgetBatch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BATCH$6, 0);
                return target;
            }
        }
        
        /**
         * Tests for nil "batch" element
         */
        public boolean isNilBatch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BATCH$6, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "batch" element
         */
        public void setBatch(int batch)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BATCH$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BATCH$6);
                }
                target.setIntValue(batch);
            }
        }
        
        /**
         * Sets (as xml) the "batch" element
         */
        public void xsetBatch(org.apache.xmlbeans.XmlInt batch)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BATCH$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(BATCH$6);
                }
                target.set(batch);
            }
        }
        
        /**
         * Nils the "batch" element
         */
        public void setNilBatch()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlInt target = null;
                target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BATCH$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(BATCH$6);
                }
                target.setNil();
            }
        }
        
        /**
         * Gets the "refundCart" element
         */
        public pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart getRefundCart()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart)get_store().find_element_user(REFUNDCART$8, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Tests for nil "refundCart" element
         */
        public boolean isNilRefundCart()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart)get_store().find_element_user(REFUNDCART$8, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "refundCart" element
         */
        public void setRefundCart(pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart refundCart)
        {
            generatedSetterHelperImpl(refundCart, REFUNDCART$8, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "refundCart" element
         */
        public pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart addNewRefundCart()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart)get_store().add_element_user(REFUNDCART$8);
                return target;
            }
        }
        
        /**
         * Nils the "refundCart" element
         */
        public void setNilRefundCart()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart target = null;
                target = (pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart)get_store().find_element_user(REFUNDCART$8, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.ArrayOfRefundCart)get_store().add_element_user(REFUNDCART$8);
                }
                target.setNil();
            }
        }
    }
}

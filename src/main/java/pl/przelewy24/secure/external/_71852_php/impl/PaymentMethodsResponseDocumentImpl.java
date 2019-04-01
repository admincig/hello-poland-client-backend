/*
 * An XML document type.
 * Localname: PaymentMethodsResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.PaymentMethodsResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one PaymentMethodsResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class PaymentMethodsResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.PaymentMethodsResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public PaymentMethodsResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PAYMENTMETHODSRESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "PaymentMethodsResponse");
    
    
    /**
     * Gets the "PaymentMethodsResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.PaymentMethodsResponseDocument.PaymentMethodsResponse getPaymentMethodsResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.PaymentMethodsResponseDocument.PaymentMethodsResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.PaymentMethodsResponseDocument.PaymentMethodsResponse)get_store().find_element_user(PAYMENTMETHODSRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "PaymentMethodsResponse" element
     */
    public void setPaymentMethodsResponse(pl.przelewy24.secure.external._71852_php.PaymentMethodsResponseDocument.PaymentMethodsResponse paymentMethodsResponse)
    {
        generatedSetterHelperImpl(paymentMethodsResponse, PAYMENTMETHODSRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "PaymentMethodsResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.PaymentMethodsResponseDocument.PaymentMethodsResponse addNewPaymentMethodsResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.PaymentMethodsResponseDocument.PaymentMethodsResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.PaymentMethodsResponseDocument.PaymentMethodsResponse)get_store().add_element_user(PAYMENTMETHODSRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML PaymentMethodsResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class PaymentMethodsResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.PaymentMethodsResponseDocument.PaymentMethodsResponse
    {
        private static final long serialVersionUID = 1L;
        
        public PaymentMethodsResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.PaymentMethodsResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.PaymentMethodsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.PaymentMethodsResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Tests for nil "return" element
         */
        public boolean isNilReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.PaymentMethodsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.PaymentMethodsResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.PaymentMethodsResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.PaymentMethodsResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.PaymentMethodsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.PaymentMethodsResult)get_store().add_element_user(RETURN$0);
                return target;
            }
        }
        
        /**
         * Nils the "return" element
         */
        public void setNilReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.PaymentMethodsResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.PaymentMethodsResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.PaymentMethodsResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

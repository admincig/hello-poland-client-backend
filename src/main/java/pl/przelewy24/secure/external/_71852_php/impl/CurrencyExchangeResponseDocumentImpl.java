/*
 * An XML document type.
 * Localname: CurrencyExchangeResponse
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.CurrencyExchangeResponseDocument
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * A document containing one CurrencyExchangeResponse(@https://secure.przelewy24.pl/external/71852.php) element.
 *
 * This is a complex type.
 */
public class CurrencyExchangeResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.CurrencyExchangeResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public CurrencyExchangeResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CURRENCYEXCHANGERESPONSE$0 = 
        new javax.xml.namespace.QName("https://secure.przelewy24.pl/external/71852.php", "CurrencyExchangeResponse");
    
    
    /**
     * Gets the "CurrencyExchangeResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.CurrencyExchangeResponseDocument.CurrencyExchangeResponse getCurrencyExchangeResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.CurrencyExchangeResponseDocument.CurrencyExchangeResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.CurrencyExchangeResponseDocument.CurrencyExchangeResponse)get_store().find_element_user(CURRENCYEXCHANGERESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "CurrencyExchangeResponse" element
     */
    public void setCurrencyExchangeResponse(pl.przelewy24.secure.external._71852_php.CurrencyExchangeResponseDocument.CurrencyExchangeResponse currencyExchangeResponse)
    {
        generatedSetterHelperImpl(currencyExchangeResponse, CURRENCYEXCHANGERESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "CurrencyExchangeResponse" element
     */
    public pl.przelewy24.secure.external._71852_php.CurrencyExchangeResponseDocument.CurrencyExchangeResponse addNewCurrencyExchangeResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.CurrencyExchangeResponseDocument.CurrencyExchangeResponse target = null;
            target = (pl.przelewy24.secure.external._71852_php.CurrencyExchangeResponseDocument.CurrencyExchangeResponse)get_store().add_element_user(CURRENCYEXCHANGERESPONSE$0);
            return target;
        }
    }
    /**
     * An XML CurrencyExchangeResponse(@https://secure.przelewy24.pl/external/71852.php).
     *
     * This is a complex type.
     */
    public static class CurrencyExchangeResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.CurrencyExchangeResponseDocument.CurrencyExchangeResponse
    {
        private static final long serialVersionUID = 1L;
        
        public CurrencyExchangeResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RETURN$0 = 
            new javax.xml.namespace.QName("", "return");
        
        
        /**
         * Gets the "return" element
         */
        public pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult getReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult)get_store().find_element_user(RETURN$0, 0);
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
                pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "return" element
         */
        public void setReturn(pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult xreturn)
        {
            generatedSetterHelperImpl(xreturn, RETURN$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "return" element
         */
        public pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult addNewReturn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult)get_store().add_element_user(RETURN$0);
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
                pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult target = null;
                target = (pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult)get_store().find_element_user(RETURN$0, 0);
                if (target == null)
                {
                    target = (pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult)get_store().add_element_user(RETURN$0);
                }
                target.setNil();
            }
        }
    }
}

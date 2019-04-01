/*
 * XML Type:  CurrencyExchangeResult
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * An XML CurrencyExchangeResult(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public class CurrencyExchangeResultImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.CurrencyExchangeResult
{
    private static final long serialVersionUID = 1L;
    
    public CurrencyExchangeResultImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESULT$0 = 
        new javax.xml.namespace.QName("", "result");
    private static final javax.xml.namespace.QName ERROR$2 = 
        new javax.xml.namespace.QName("", "error");
    
    
    /**
     * Gets the "result" element
     */
    public pl.przelewy24.secure.external._71852_php.ArrayOfCurrency getResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.ArrayOfCurrency target = null;
            target = (pl.przelewy24.secure.external._71852_php.ArrayOfCurrency)get_store().find_element_user(RESULT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "result" element
     */
    public void setResult(pl.przelewy24.secure.external._71852_php.ArrayOfCurrency result)
    {
        generatedSetterHelperImpl(result, RESULT$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "result" element
     */
    public pl.przelewy24.secure.external._71852_php.ArrayOfCurrency addNewResult()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.ArrayOfCurrency target = null;
            target = (pl.przelewy24.secure.external._71852_php.ArrayOfCurrency)get_store().add_element_user(RESULT$0);
            return target;
        }
    }
    
    /**
     * Gets the "error" element
     */
    public pl.przelewy24.secure.external._71852_php.GeneralError getError()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GeneralError target = null;
            target = (pl.przelewy24.secure.external._71852_php.GeneralError)get_store().find_element_user(ERROR$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "error" element
     */
    public void setError(pl.przelewy24.secure.external._71852_php.GeneralError error)
    {
        generatedSetterHelperImpl(error, ERROR$2, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "error" element
     */
    public pl.przelewy24.secure.external._71852_php.GeneralError addNewError()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.GeneralError target = null;
            target = (pl.przelewy24.secure.external._71852_php.GeneralError)get_store().add_element_user(ERROR$2);
            return target;
        }
    }
}

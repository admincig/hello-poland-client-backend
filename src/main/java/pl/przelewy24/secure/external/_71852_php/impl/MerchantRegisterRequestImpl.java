/*
 * XML Type:  MerchantRegisterRequest
 * Namespace: https://secure.przelewy24.pl/external/71852.php
 * Java type: pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest
 *
 * Automatically generated - do not modify.
 */
package pl.przelewy24.secure.external._71852_php.impl;
/**
 * An XML MerchantRegisterRequest(@https://secure.przelewy24.pl/external/71852.php).
 *
 * This is a complex type.
 */
public class MerchantRegisterRequestImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements pl.przelewy24.secure.external._71852_php.MerchantRegisterRequest
{
    private static final long serialVersionUID = 1L;
    
    public MerchantRegisterRequestImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName BUSINESSTYPE$0 = 
        new javax.xml.namespace.QName("", "business_type");
    private static final javax.xml.namespace.QName NAME$2 = 
        new javax.xml.namespace.QName("", "name");
    private static final javax.xml.namespace.QName EMAIL$4 = 
        new javax.xml.namespace.QName("", "email");
    private static final javax.xml.namespace.QName PESEL$6 = 
        new javax.xml.namespace.QName("", "pesel");
    private static final javax.xml.namespace.QName PHONENUMBER$8 = 
        new javax.xml.namespace.QName("", "phone_number");
    private static final javax.xml.namespace.QName BANKACCOUNT$10 = 
        new javax.xml.namespace.QName("", "bank_account");
    private static final javax.xml.namespace.QName REPRESENTATIVES$12 = 
        new javax.xml.namespace.QName("", "representatives");
    private static final javax.xml.namespace.QName CONTACTPERSON$14 = 
        new javax.xml.namespace.QName("", "contact_person");
    private static final javax.xml.namespace.QName TECHNICALCONTACT$16 = 
        new javax.xml.namespace.QName("", "technical_contact");
    private static final javax.xml.namespace.QName ADDRESS$18 = 
        new javax.xml.namespace.QName("", "address");
    private static final javax.xml.namespace.QName CORRESPONDENCEADDRESS$20 = 
        new javax.xml.namespace.QName("", "correspondence_address");
    private static final javax.xml.namespace.QName INVOICEEMAIL$22 = 
        new javax.xml.namespace.QName("", "invoice_email");
    private static final javax.xml.namespace.QName SHOPURL$24 = 
        new javax.xml.namespace.QName("", "shop_url");
    private static final javax.xml.namespace.QName SERVICESDESCRIPTION$26 = 
        new javax.xml.namespace.QName("", "services_description");
    private static final javax.xml.namespace.QName TRADE$28 = 
        new javax.xml.namespace.QName("", "trade");
    private static final javax.xml.namespace.QName KRS$30 = 
        new javax.xml.namespace.QName("", "krs");
    private static final javax.xml.namespace.QName NIP$32 = 
        new javax.xml.namespace.QName("", "nip");
    private static final javax.xml.namespace.QName REGON$34 = 
        new javax.xml.namespace.QName("", "regon");
    private static final javax.xml.namespace.QName ACCEPTANCE$36 = 
        new javax.xml.namespace.QName("", "acceptance");
    
    
    /**
     * Gets the "business_type" element
     */
    public int getBusinessType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BUSINESSTYPE$0, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "business_type" element
     */
    public org.apache.xmlbeans.XmlInt xgetBusinessType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BUSINESSTYPE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "business_type" element
     */
    public void setBusinessType(int businessType)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BUSINESSTYPE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BUSINESSTYPE$0);
            }
            target.setIntValue(businessType);
        }
    }
    
    /**
     * Sets (as xml) the "business_type" element
     */
    public void xsetBusinessType(org.apache.xmlbeans.XmlInt businessType)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BUSINESSTYPE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(BUSINESSTYPE$0);
            }
            target.set(businessType);
        }
    }
    
    /**
     * Gets the "name" element
     */
    public java.lang.String getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "name" element
     */
    public org.apache.xmlbeans.XmlString xgetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "name" element
     */
    public void setName(java.lang.String name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NAME$2);
            }
            target.setStringValue(name);
        }
    }
    
    /**
     * Sets (as xml) the "name" element
     */
    public void xsetName(org.apache.xmlbeans.XmlString name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NAME$2);
            }
            target.set(name);
        }
    }
    
    /**
     * Gets the "email" element
     */
    public java.lang.String getEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EMAIL$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "email" element
     */
    public org.apache.xmlbeans.XmlString xgetEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EMAIL$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "email" element
     */
    public void setEmail(java.lang.String email)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(EMAIL$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(EMAIL$4);
            }
            target.setStringValue(email);
        }
    }
    
    /**
     * Sets (as xml) the "email" element
     */
    public void xsetEmail(org.apache.xmlbeans.XmlString email)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(EMAIL$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(EMAIL$4);
            }
            target.set(email);
        }
    }
    
    /**
     * Gets the "pesel" element
     */
    public java.lang.String getPesel()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PESEL$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "pesel" element
     */
    public org.apache.xmlbeans.XmlString xgetPesel()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PESEL$6, 0);
            return target;
        }
    }
    
    /**
     * Sets the "pesel" element
     */
    public void setPesel(java.lang.String pesel)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PESEL$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PESEL$6);
            }
            target.setStringValue(pesel);
        }
    }
    
    /**
     * Sets (as xml) the "pesel" element
     */
    public void xsetPesel(org.apache.xmlbeans.XmlString pesel)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PESEL$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PESEL$6);
            }
            target.set(pesel);
        }
    }
    
    /**
     * Gets the "phone_number" element
     */
    public java.lang.String getPhoneNumber()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PHONENUMBER$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "phone_number" element
     */
    public org.apache.xmlbeans.XmlString xgetPhoneNumber()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PHONENUMBER$8, 0);
            return target;
        }
    }
    
    /**
     * Sets the "phone_number" element
     */
    public void setPhoneNumber(java.lang.String phoneNumber)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PHONENUMBER$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PHONENUMBER$8);
            }
            target.setStringValue(phoneNumber);
        }
    }
    
    /**
     * Sets (as xml) the "phone_number" element
     */
    public void xsetPhoneNumber(org.apache.xmlbeans.XmlString phoneNumber)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PHONENUMBER$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PHONENUMBER$8);
            }
            target.set(phoneNumber);
        }
    }
    
    /**
     * Gets the "bank_account" element
     */
    public java.lang.String getBankAccount()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BANKACCOUNT$10, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "bank_account" element
     */
    public org.apache.xmlbeans.XmlString xgetBankAccount()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BANKACCOUNT$10, 0);
            return target;
        }
    }
    
    /**
     * Sets the "bank_account" element
     */
    public void setBankAccount(java.lang.String bankAccount)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BANKACCOUNT$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BANKACCOUNT$10);
            }
            target.setStringValue(bankAccount);
        }
    }
    
    /**
     * Sets (as xml) the "bank_account" element
     */
    public void xsetBankAccount(org.apache.xmlbeans.XmlString bankAccount)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(BANKACCOUNT$10, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(BANKACCOUNT$10);
            }
            target.set(bankAccount);
        }
    }
    
    /**
     * Gets the "representatives" element
     */
    public pl.przelewy24.secure.external._71852_php.ArrayOfRepresentative getRepresentatives()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.ArrayOfRepresentative target = null;
            target = (pl.przelewy24.secure.external._71852_php.ArrayOfRepresentative)get_store().find_element_user(REPRESENTATIVES$12, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "representatives" element
     */
    public void setRepresentatives(pl.przelewy24.secure.external._71852_php.ArrayOfRepresentative representatives)
    {
        generatedSetterHelperImpl(representatives, REPRESENTATIVES$12, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "representatives" element
     */
    public pl.przelewy24.secure.external._71852_php.ArrayOfRepresentative addNewRepresentatives()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.ArrayOfRepresentative target = null;
            target = (pl.przelewy24.secure.external._71852_php.ArrayOfRepresentative)get_store().add_element_user(REPRESENTATIVES$12);
            return target;
        }
    }
    
    /**
     * Gets the "contact_person" element
     */
    public pl.przelewy24.secure.external._71852_php.ContactPerson getContactPerson()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.ContactPerson target = null;
            target = (pl.przelewy24.secure.external._71852_php.ContactPerson)get_store().find_element_user(CONTACTPERSON$14, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "contact_person" element
     */
    public void setContactPerson(pl.przelewy24.secure.external._71852_php.ContactPerson contactPerson)
    {
        generatedSetterHelperImpl(contactPerson, CONTACTPERSON$14, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "contact_person" element
     */
    public pl.przelewy24.secure.external._71852_php.ContactPerson addNewContactPerson()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.ContactPerson target = null;
            target = (pl.przelewy24.secure.external._71852_php.ContactPerson)get_store().add_element_user(CONTACTPERSON$14);
            return target;
        }
    }
    
    /**
     * Gets the "technical_contact" element
     */
    public pl.przelewy24.secure.external._71852_php.TechnicalContact getTechnicalContact()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.TechnicalContact target = null;
            target = (pl.przelewy24.secure.external._71852_php.TechnicalContact)get_store().find_element_user(TECHNICALCONTACT$16, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "technical_contact" element
     */
    public void setTechnicalContact(pl.przelewy24.secure.external._71852_php.TechnicalContact technicalContact)
    {
        generatedSetterHelperImpl(technicalContact, TECHNICALCONTACT$16, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "technical_contact" element
     */
    public pl.przelewy24.secure.external._71852_php.TechnicalContact addNewTechnicalContact()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.TechnicalContact target = null;
            target = (pl.przelewy24.secure.external._71852_php.TechnicalContact)get_store().add_element_user(TECHNICALCONTACT$16);
            return target;
        }
    }
    
    /**
     * Gets the "address" element
     */
    public pl.przelewy24.secure.external._71852_php.Address getAddress()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.Address target = null;
            target = (pl.przelewy24.secure.external._71852_php.Address)get_store().find_element_user(ADDRESS$18, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "address" element
     */
    public void setAddress(pl.przelewy24.secure.external._71852_php.Address address)
    {
        generatedSetterHelperImpl(address, ADDRESS$18, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "address" element
     */
    public pl.przelewy24.secure.external._71852_php.Address addNewAddress()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.Address target = null;
            target = (pl.przelewy24.secure.external._71852_php.Address)get_store().add_element_user(ADDRESS$18);
            return target;
        }
    }
    
    /**
     * Gets the "correspondence_address" element
     */
    public pl.przelewy24.secure.external._71852_php.CorrespondenceAddress getCorrespondenceAddress()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.CorrespondenceAddress target = null;
            target = (pl.przelewy24.secure.external._71852_php.CorrespondenceAddress)get_store().find_element_user(CORRESPONDENCEADDRESS$20, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "correspondence_address" element
     */
    public void setCorrespondenceAddress(pl.przelewy24.secure.external._71852_php.CorrespondenceAddress correspondenceAddress)
    {
        generatedSetterHelperImpl(correspondenceAddress, CORRESPONDENCEADDRESS$20, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "correspondence_address" element
     */
    public pl.przelewy24.secure.external._71852_php.CorrespondenceAddress addNewCorrespondenceAddress()
    {
        synchronized (monitor())
        {
            check_orphaned();
            pl.przelewy24.secure.external._71852_php.CorrespondenceAddress target = null;
            target = (pl.przelewy24.secure.external._71852_php.CorrespondenceAddress)get_store().add_element_user(CORRESPONDENCEADDRESS$20);
            return target;
        }
    }
    
    /**
     * Gets the "invoice_email" element
     */
    public java.lang.String getInvoiceEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INVOICEEMAIL$22, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "invoice_email" element
     */
    public org.apache.xmlbeans.XmlString xgetInvoiceEmail()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(INVOICEEMAIL$22, 0);
            return target;
        }
    }
    
    /**
     * Sets the "invoice_email" element
     */
    public void setInvoiceEmail(java.lang.String invoiceEmail)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INVOICEEMAIL$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(INVOICEEMAIL$22);
            }
            target.setStringValue(invoiceEmail);
        }
    }
    
    /**
     * Sets (as xml) the "invoice_email" element
     */
    public void xsetInvoiceEmail(org.apache.xmlbeans.XmlString invoiceEmail)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(INVOICEEMAIL$22, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(INVOICEEMAIL$22);
            }
            target.set(invoiceEmail);
        }
    }
    
    /**
     * Gets the "shop_url" element
     */
    public java.lang.String getShopUrl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SHOPURL$24, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "shop_url" element
     */
    public org.apache.xmlbeans.XmlString xgetShopUrl()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SHOPURL$24, 0);
            return target;
        }
    }
    
    /**
     * Sets the "shop_url" element
     */
    public void setShopUrl(java.lang.String shopUrl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SHOPURL$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SHOPURL$24);
            }
            target.setStringValue(shopUrl);
        }
    }
    
    /**
     * Sets (as xml) the "shop_url" element
     */
    public void xsetShopUrl(org.apache.xmlbeans.XmlString shopUrl)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SHOPURL$24, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SHOPURL$24);
            }
            target.set(shopUrl);
        }
    }
    
    /**
     * Gets the "services_description" element
     */
    public java.lang.String getServicesDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SERVICESDESCRIPTION$26, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "services_description" element
     */
    public org.apache.xmlbeans.XmlString xgetServicesDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SERVICESDESCRIPTION$26, 0);
            return target;
        }
    }
    
    /**
     * Sets the "services_description" element
     */
    public void setServicesDescription(java.lang.String servicesDescription)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SERVICESDESCRIPTION$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SERVICESDESCRIPTION$26);
            }
            target.setStringValue(servicesDescription);
        }
    }
    
    /**
     * Sets (as xml) the "services_description" element
     */
    public void xsetServicesDescription(org.apache.xmlbeans.XmlString servicesDescription)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SERVICESDESCRIPTION$26, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SERVICESDESCRIPTION$26);
            }
            target.set(servicesDescription);
        }
    }
    
    /**
     * Gets the "trade" element
     */
    public java.lang.String getTrade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TRADE$28, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "trade" element
     */
    public org.apache.xmlbeans.XmlString xgetTrade()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TRADE$28, 0);
            return target;
        }
    }
    
    /**
     * Sets the "trade" element
     */
    public void setTrade(java.lang.String trade)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TRADE$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TRADE$28);
            }
            target.setStringValue(trade);
        }
    }
    
    /**
     * Sets (as xml) the "trade" element
     */
    public void xsetTrade(org.apache.xmlbeans.XmlString trade)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TRADE$28, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TRADE$28);
            }
            target.set(trade);
        }
    }
    
    /**
     * Gets the "krs" element
     */
    public java.lang.String getKrs()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(KRS$30, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "krs" element
     */
    public org.apache.xmlbeans.XmlString xgetKrs()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(KRS$30, 0);
            return target;
        }
    }
    
    /**
     * Sets the "krs" element
     */
    public void setKrs(java.lang.String krs)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(KRS$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(KRS$30);
            }
            target.setStringValue(krs);
        }
    }
    
    /**
     * Sets (as xml) the "krs" element
     */
    public void xsetKrs(org.apache.xmlbeans.XmlString krs)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(KRS$30, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(KRS$30);
            }
            target.set(krs);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NIP$32, 0);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NIP$32, 0);
            return target;
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NIP$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NIP$32);
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
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NIP$32, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NIP$32);
            }
            target.set(nip);
        }
    }
    
    /**
     * Gets the "regon" element
     */
    public java.lang.String getRegon()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REGON$34, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "regon" element
     */
    public org.apache.xmlbeans.XmlString xgetRegon()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REGON$34, 0);
            return target;
        }
    }
    
    /**
     * Sets the "regon" element
     */
    public void setRegon(java.lang.String regon)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(REGON$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(REGON$34);
            }
            target.setStringValue(regon);
        }
    }
    
    /**
     * Sets (as xml) the "regon" element
     */
    public void xsetRegon(org.apache.xmlbeans.XmlString regon)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(REGON$34, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(REGON$34);
            }
            target.set(regon);
        }
    }
    
    /**
     * Gets the "acceptance" element
     */
    public boolean getAcceptance()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ACCEPTANCE$36, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "acceptance" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetAcceptance()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(ACCEPTANCE$36, 0);
            return target;
        }
    }
    
    /**
     * Sets the "acceptance" element
     */
    public void setAcceptance(boolean acceptance)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ACCEPTANCE$36, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ACCEPTANCE$36);
            }
            target.setBooleanValue(acceptance);
        }
    }
    
    /**
     * Sets (as xml) the "acceptance" element
     */
    public void xsetAcceptance(org.apache.xmlbeans.XmlBoolean acceptance)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(ACCEPTANCE$36, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(ACCEPTANCE$36);
            }
            target.set(acceptance);
        }
    }
}

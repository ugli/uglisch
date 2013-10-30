<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" queryBinding="xpath2">
    <!--
 Schematron structural schema to validate purchase orders 
-->
    <!--  Note: content models are treated as "open".  -->
    <!--  1999-11-11  -->
    <title>Schema for Purchase Order Example</title>
    <!--  First, simulate types  -->
    <pattern>
        <rule context="name| city | state | zip">
            <!--
 This rule couples the elements of the address type together, so that
			if one appears anywhere, the others must also. 
-->
            <assert test="parent::*/street">An address should have a street.</assert>
            <assert test="parent::*/city">An address should have a city.</assert>
            <assert test="parent::*/state">An address should have a state.</assert>
            <assert test="parent::*/zip">An address should have a zip.</assert>
            <assert test="parent::*/@type">An address should have a type attribute.</assert>
        </rule>
        <rule context="/name | /city | /state | /zip">
            <report test="self::*">
                The elements
                <name/>
                is not expected at the top of a document. They form part of an address.
            </report>
        </rule>
    </pattern>
    <pattern>
        <rule context="shipTo | shipDate | Items">
            <!--
 This rule couples the elements of the address type together, so that
			if one appears anywhere, the others must also. 
-->
            <assert test="parent::*/shipTo">A purchase order should have a shipTo element.</assert>
            <assert test="parent::*/shipDate">A purchase order should have a shipDate element.</assert>
            <assert test="parent::*/Items">A purchase order should have an Items element.</assert>
            <assert test="parent::*/@orderDate">
                A purchase order should have an orderDate attribute.
            </assert>
        </rule>
        <rule context="/shipTo | /shipDate | /Items">
            <report test="self::*">
                The element
                <name/>
                is not expected at the top of a document. It should be in the body of a purchase order.
            </report>
        </rule>
    </pattern>
    <!--  Next, the specific elements  -->
    <pattern>
        <rule context="PurchaseOrder">
            <assert test="shipTo">
                A purchase order element should have a shipping address. 
There should be a shipTo element contained by the PurchaseOrder element.
            </assert>
            <assert test="@orderDate">
                A purchase order element should have a shipping address.
 There should be a shipTo element contained by the PurchaseOrder element.
            </assert>
            <assert test="@orderDate1">
                A purchase order element should have a shipping address. 
There should be a shipTo element contained by the PurchaseOrder element.
            </assert>
            <assert test="1 eq 2">
                testar lite
            </assert>
        </rule>
        <rule context="shipTo">
            <assert test="name">
                A shipping address element should have a name element.
            </assert>
            <!--
 The rest of the content model is tested through the
				addressType pattern. 
-->
        </rule>
        <rule context="Items">
            <assert test="count(child::*) = count(child::Item)">The Items element can only contain Item elements</assert>
        </rule>
        <rule context="Item">
            <assert test="productName">The Item element should contain a product name.</assert>
            <assert test="quantity">The Item element should contain a quantity element</assert>
            <assert test="price">The Item element should contain a price element.</assert>
        </rule>
        <rule context="comment">
            <report test="child::*">A comment should have no subelements</report>
            <assert test="parent::Item | parent::PurchaseOrder ">
                A comment can only appear in an Item or a Purchase Order
            </assert>
        </rule>
    </pattern>
</schema>
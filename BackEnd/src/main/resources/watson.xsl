<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : watson.xsl
    Created on : 17 December 2008, 18:47
    Author     : Laurian Gridinoc
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml" indent="true"/>



    <xsl:template match="/">
        <uk.ac.open.powermagpie.context.Term>
            <lexical>rabbit</lexical>
            <OpenK>false</OpenK>
            <matches>
                <xsl:for-each select="*">
                    <uk.ac.open.powermagpie.context.Match>
                        <sc>
                            <xsl:copy-of select="node()[name(.) = 'uk.ac.open.kmi.watson.clientapi.SemanticContentResult']/*" />
                        </sc>
                        <ntt>
                            <xsl:copy-of select="node()[name(.) = 'uk.ac.open.kmi.watson.clientapi.SemanticContentResult']/node()[name(.) = 'entityResultList']/node()" />
                        </ntt>
                        <context>foo</context>
                    </uk.ac.open.powermagpie.context.Match>
                </xsl:for-each>
            </matches>
        </uk.ac.open.powermagpie.context.Term>
    </xsl:template>

</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns="http://www.topografix.com/GPX/1/1">
    <!-- Output GPX file -->
    <xsl:output method="xml" indent="yes"/>

    <!-- Root template -->
    <xsl:template match="/">
        <gpx version="1.1" creator="Catalog to GPX Transformer" xmlns="http://www.topografix.com/GPX/1/1">
            <xsl:apply-templates select="catalog/node"/>
        </gpx>
    </xsl:template>

    <!-- Template to transform each node to a waypoint -->
    <xsl:template match="node">
        <wpt lat="{@lat}" lon="{@lon}">
            <xsl:element name="name">node_<xsl:value-of select="@id"/>
            </xsl:element>
            <xsl:element name="extensions">
                <xsl:apply-templates select="tag[not(@k='name')]"/>
            </xsl:element>
        </wpt>
    </xsl:template>

    <!-- Template to transform each tag into an element within extensions -->
    <xsl:template match="tag">
        <xsl:variable name="safeName" select="translate(@k, ':', '_')"/>
        <xsl:element name="{$safeName}">
            <xsl:value-of select="@v"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="tag[@k='name']"/>
</xsl:stylesheet>
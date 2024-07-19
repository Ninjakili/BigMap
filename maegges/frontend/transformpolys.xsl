<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns="http://www.topografix.com/GPX/1/1">
    <!-- Using the GPX namespace -->
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

    <!-- Match the root element and create the GPX structure -->
    <xsl:template match="/">
        <gpx version="1.1" creator="Example Transformation">
            <xsl:apply-templates select="catalog/polygon"/>
        </gpx>
    </xsl:template>

    <!-- Match each polygon and create a <trk> for each -->
    <xsl:template match="polygon">
        <trk>
            <!-- Use the polygon id as the track name -->
            <name><xsl:value-of select="@id"/></name>
            <trkseg>
                <xsl:apply-templates select="vertex"/>
            </trkseg>
        </trk>
    </xsl:template>

    <!-- Match each vertex and create a <trkpt> (track point) for each -->
    <xsl:template match="vertex">
        <trkpt lat="{@lon}" lon="{@lat}"/>
    </xsl:template>
</xsl:stylesheet>
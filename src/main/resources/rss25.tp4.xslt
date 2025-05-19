<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:p="http://univ.fr/rss25">

  <xsl:param name="id"/>

  <xsl:output method="html" indent="yes"/>

  <xsl:template match="/">
    <html>
      <head>
        <title>Détail de l'article</title>
        <meta charset="UTF-8"/>
      </head>
      <body>
        <xsl:choose>
          <xsl:when test="/p:feed/p:item[position() = $id]">
            <xsl:apply-templates select="/p:feed/p:item[position() = $id]"/>
          </xsl:when>
          <xsl:otherwise>
            <h1>Erreur</h1>
            <p>status : ERROR</p>
            <p>id : <xsl:value-of select="$id"/></p>
          </xsl:otherwise>
        </xsl:choose>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="p:item">
    <div>
      <h2><xsl:value-of select="p:title"/></h2>
      <p><b>guid :</b> <xsl:value-of select="p:guid"/></p>
      <p><b>Publié le :</b> <xsl:value-of select="p:published"/></p>
      <p><b>Mise à jour :</b> <xsl:value-of select="p:updated"/></p>
      <p><b>Contenu :</b> <xsl:value-of select="p:content"/></p>
      <xsl:if test="p:image">
        <img>
          <xsl:attribute name="src"><xsl:value-of select="p:image/@href"/></xsl:attribute>
          <xsl:attribute name="alt"><xsl:value-of select="p:image/@alt"/></xsl:attribute>
        </img>
      </xsl:if>
      <p><b>Catégories :</b></p>
      <ul>
        <xsl:for-each select="p:category">
          <li><xsl:value-of select="@term"/></li>
        </xsl:for-each>
      </ul>
      <p><b>Auteurs :</b></p>
      <ul>
        <xsl:for-each select="p:author">
          <li><xsl:value-of select="p:name"/></li>
        </xsl:for-each>
      </ul>
    </div>
  </xsl:template>

</xsl:stylesheet>

<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:p="http://univ.fr/rss25"
    exclude-result-prefixes="p">

    <xsl:output method="html" indent="yes" encoding="UTF-8"/>

    <!-- === Template principal pour résumé === -->
        <xsl:template match="/p:resume">
        <html>
            <head>
                <title>Résumé des Articles</title>
                <style>
                    body { font-family: Arial; margin: 20px; }
                    h1 { color: #007bff; }
                    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
                    th, td { padding: 8px; border: 1px solid #ccc; }
                    th { background-color: #f4f4f4; }
                </style>
            </head>
            <body>
                <h1>Résumé des Articles</h1>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Titre</th>
                            <th>Date</th>
                            <th>Catégories</th>
                            <th>GUID</th>
                        </tr>
                    </thead>
                    <tbody>
                        <xsl:for-each select="p:item">
                            <tr>
                                <td><xsl:value-of select="p:id"/></td>
                                <td><xsl:value-of select="p:title"/></td>
                                <td>
                                    <xsl:choose>
                                        <xsl:when test="p:published"><xsl:value-of select="p:published"/></xsl:when>
                                        <xsl:when test="p:updated"><xsl:value-of select="p:updated"/></xsl:when>
                                        <xsl:otherwise>–</xsl:otherwise>
                                    </xsl:choose>
                                </td>
                                <td>
                                    <xsl:for-each select="p:category">
                                        <xsl:value-of select="@term"/>
                                        <xsl:if test="position() != last()">, </xsl:if>
                                    </xsl:for-each>
                                </td>
                                <td><a href="{p:guid}"><xsl:value-of select="p:guid"/></a></td>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>


    <!-- === Template principal pour le détail === -->
    <xsl:template match="/p:feed">
        <html>
            <head>
                <title>Détail de l'article</title>
                <style>
                    body { font-family: Arial; margin: 20px; }
                    h1 { color: #007bff; }
                    .field { margin-bottom: 10px; }
                    .label { font-weight: bold; }
                </style>
            </head>
            <body>
                <h1>Détail de l'article</h1>
                <xsl:for-each select="p:item">
                    <div class="field"><span class="label">ID :</span> <xsl:value-of select="p:id"/></div>
                    <div class="field"><span class="label">Titre :</span> <xsl:value-of select="p:title"/></div>
                    <div class="field"><span class="label">Auteur :</span> <xsl:value-of select="p:author/p:name"/></div>
                    <div class="field"><span class="label">Date de publication :</span> <xsl:value-of select="p:published"/></div>
                    <div class="field"><span class="label">Date de mise à jour :</span> <xsl:value-of select="p:updated"/></div>
                    <div class="field"><span class="label">Catégories :</span>
                        <xsl:for-each select="p:category">
                            <xsl:value-of select="@term"/>
                            <xsl:if test="position() != last()">, </xsl:if>
                        </xsl:for-each>
                    </div>
                    <div class="field"><span class="label">Lien :</span>
                        <a href="{p:link}" target="_blank"><xsl:value-of select="p:link"/></a>
                    </div>
                    <div class="field"><span class="label">Description :</span> <xsl:value-of select="p:description"/></div>
                    <div class="field"><span class="label">GUID :</span> <xsl:value-of select="p:guid"/></div>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>

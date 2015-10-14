<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="header">
		<header class="header" id="home">
			<xsl:call-template name="navbar" />
			<xsl:call-template name="promo" />
		</header>
	</xsl:template>

	<xsl:template name="navbar">
		<nav class="navbar navbar-default js-navbar-top js-toggle-class">
			<div class="container">
				<div class="navbar-header">
					<a class="navbar-brand" href="#home">
						<xsl:value-of select="//captions/brand/@caption" />
					</a>
				</div>
				<div class="dropdown dropdown-lang">
					<a href="#" class="dropdown-toggle animated fadeInRight" data-toggle="dropdown">
						<i class="fa fa-globe"></i>
						<xsl:value-of select="upper-case(//@lang)" />
					</a>
					<ul class="dropdown-menu">
						<xsl:apply-templates select="//availablelangs" />
					</ul>
				</div>
			</div>
		</nav>
	</xsl:template>

	<xsl:template name="promo">
		<div class="promo">
			<div class="container">
				<div class="row">
					<div class="col-xs-12">
						<h1 class="heading promo__heading animated fadeInLeft">
							<span>
								<xsl:value-of select="//captions/promo_heading/@caption" />
							</span>
						</h1>
						<p class="heading__sub promo-heading__sub animated fadeInRight delay_1">
							<span>
								<xsl:value-of select="//captions/promo_heading_sub/@caption" />
							</span>
						</p>
						<a href="#contact" class="promo__btn btn btn-lg btn-primary animated fadeInUp delay_2">
							<xsl:value-of select="//captions/contact_us/@caption" />
						</a>
						<!-- <a href="#about" class="promo__btn btn btn-lg btn-link animated fadeInUp delay_2">
							<xsl:value-of select="//captions/learn_more/@caption" />
						</a> -->
					</div>
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="availablelangs">
		<xsl:if test="count(value[entry = 'ON']) > 1">
			<xsl:apply-templates select="value[entry[1] = 'ON']" mode="lang" />
		</xsl:if>
	</xsl:template>

	<xsl:template match="value" mode="lang">
		<li>
			<a class="lang" href="Provider?id={//request/@id}&amp;lang={entry[2]}">
				<xsl:value-of select="entry[3]" />
			</a>
		</li>
	</xsl:template>

</xsl:stylesheet>

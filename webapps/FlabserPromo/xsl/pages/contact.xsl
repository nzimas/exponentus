<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="contact">
		<section class="section contact" id="contact">

			<!-- CONTACT -->
			<div class="container">
				<div class="row">
					<div class="col-sm-12">
						<h3 class="heading">
							<xsl:value-of select="//captions/contact_us/@caption" />
						</h3>
						<p class="heading__sub" style="margin-bottom:40px">
							<xsl:value-of select="//captions/contact_us_on_lang/@caption" />
						</p>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-7 form-group contact-form">
						<h4>
							<xsl:value-of select="//captions/message_title/@caption" />
						</h4>
						<form method="post" action="contact">
							<input type="text" name="email" placeholder="Email" class="form-control contact-email contact-error" required="required" />
							<input type="text" name="subject" class="form-control contact-subject contact-error" required="required">
								<xsl:attribute name="placeholder" select="//captions/message_subject/@caption" />
							</input>
							<textarea name="message" class="form-control contact-error" required="required">
								<xsl:attribute name="placeholder" select="//captions/message_text/@caption" />
							</textarea>
							<button type="submit" class="btn btn-contact">
								<xsl:value-of select="//captions/send_message/@caption" />
							</button>
						</form>
					</div>
					<div class="col-sm-5 contact-address">
						<h4>
							<xsl:value-of select="//captions/address/@caption" />
						</h4>
						<p>
							<i class="fa fa-map-marker"></i>
							x ???? Street, Almaty, Kazakhstan
						</p>
						<p>
							<i class="fa fa-phone"></i>
							<xsl:value-of select="//captions/phone/@caption" />: +7 xxx xxx xx xx
						</p>
					</div>
				</div>
			</div>
		</section>
	</xsl:template>

</xsl:stylesheet>
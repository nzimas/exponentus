<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="login">
		<div class="container">
			<h1 class="text-center">
				<xsl:value-of select="//captions/login_title/@caption" />
			</h1>
			<section>
				<div class="login">
					<div class="login-error" id="login-error">
						<xsl:value-of select="//captions/login_data_incorrect/@caption" />
					</div>
					<form method="post" name="login-form" class="login-form">
						<input class="input" type="text" name="login" value="" required="required" placeholder="{//captions/login_login/@caption}" />
						<input class="input" type="password" name="pwd" value="" required="required" placeholder="{//captions/login_pwd/@caption}" />
						<div class="login-form-bottom">
							<label class="noauth">
								<input type="checkbox" name="noauth" value="1" />
								<xsl:value-of select="//captions/login_alien_device/@caption" />
							</label>
							<button type="submit" class="btn">
								<xsl:value-of select="//captions/login_button/@caption" />
							</button>
						</div>
					</form>
					<xsl:if test="//request/@id = 'login'">
						<div class="social">
							<a href="#vk" rel="nofollow">
								<i class="social-icon-vk" />
							</a>
							<a href="#fb" rel="nofollow">
								<i class="social-icon-fb" />
							</a>
							<a href="#twitter" rel="nofollow">
								<i class="social-icon-twitter" />
							</a>
						</div>
						<footer>
							<ul class="help-list">
								<li>
									<a href="?id=password-recovery" rel="nofollow">
										<xsl:value-of select="//captions/lost_password/@caption" />
									</a>
								</li>
								<li>
									<a href="?id=retry-send-verify-email" rel="nofollow">
										<xsl:value-of select="//captions/no_verify_mail/@caption" />
									</a>
								</li>
							</ul>
						</footer>
					</xsl:if>
				</div>
			</section>
		</div>
	</xsl:template>

</xsl:stylesheet>
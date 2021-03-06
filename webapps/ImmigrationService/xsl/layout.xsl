﻿<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="templates/constants.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />

	<xsl:template name="layout">
		<xsl:param name="w_title" select="//captions/promo/@caption" />

		<xsl:call-template name="HTML-DOCTYPE" />
		<html>
			<head>
				<meta charset="utf-8" />
				<meta http-equiv="X-UA-Compatible" content="IE=edge" />
				<meta name="format-detection" content="telephone=no" />
				<meta name="format-detection" content="email=no" />
				<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
				<link rel="shortcut icon" href="favicon.png" />
				<title>
					<xsl:value-of select="$w_title" />
				</title>
				<link rel="stylesheet" href="/SharedResources/vendor/font-awesome/css/font-awesome.min.css" />
				<link rel="stylesheet" href="/SharedResources/vendor/bootstrap/css/bootstrap.min.css" />
				<link rel="stylesheet" href="/SharedResources/css/animate.css" />

				<link rel="stylesheet" href="css/layout.css" />
				<link rel="stylesheet" href="css/navbar.css" />
				<link rel="stylesheet" href="css/heading.css" />
				<link rel="stylesheet" href="css/promo.css" />
				<link rel="stylesheet" href="css/about.css" />
				<link rel="stylesheet" href="css/how-it-works.css" />
				<link rel="stylesheet" href="css/contact.css" />
				<link rel="stylesheet" href="css/icons-lang.css" />
			</head>
			<body>
				<div class="layout wrapper">
					<xsl:call-template name="_content" />
				</div>
				<!-- js libs
				======================== -->
				<script src="/SharedResources/vendor/jquery/jquery-2.1.4.min.js"></script>
				<script src="/SharedResources/vendor/bootstrap/js/bootstrap.min.js"></script>
				<script src="https://www.google.com/recaptcha/api.js"></script>
				<!-- Begin Cookie Consent plugin by Silktide - http://silktide.com/cookieconsent -->
				<script type="text/javascript">
				    window.cookieconsent_options = {
				    	"message":"<xsl:value-of select="//captions/cookieconsent_message/@caption" />",
				    	"dismiss":"OK",
				    	"learnMore":"",
				    	"link":null,
				    	"theme":"dark-bottom"
				    };
				</script>
				<script type="text/javascript" src="//s3.amazonaws.com/cc.silktide.com/cookieconsent.latest.min.js"></script>
				<!-- End Cookie Consent plugin -->
				<!-- app
				======================== -->
				<script src="js/app.js"></script>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="_content" />

</xsl:stylesheet>

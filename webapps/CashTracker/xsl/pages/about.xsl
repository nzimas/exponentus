<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="section_about">
		<section>
			<div class="container">
				<h1>
					<xsl:value-of select="//captions/about/@caption" />
				</h1>
				<xsl:choose>
					<xsl:when test="//@lang = 'RUS'">
						<xsl:call-template name="about_ru" />
					</xsl:when>
					<xsl:when test="//@lang = 'ENG'">
						<xsl:call-template name="about_en" />
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="about_en" />
					</xsl:otherwise>
				</xsl:choose>
			</div>
		</section>
	</xsl:template>

	<xsl:template name="about_en">
		<section>
			Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the
			industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it
			to make a type specimen book. It has survived not only five centuries, but also the leap into electronic
			typesetting,
			remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets
			containing Lorem
			Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including
			versions of Lorem
			Ipsum.
		</section>
		<section style="text-align:center">
			<div class="about_img">
				<img src="img/screen/desktop-ui.png" />
			</div>
			<div class="about_img">
				<img src="img/screen/desktop-ui.png" />
			</div>
			<div class="about_img">
				<img src="img/screen/desktop-ui.png" />
			</div>
			<div class="clearfix"></div>
		</section>
	</xsl:template>

	<xsl:template name="about_ru">
		<section>
			Lorem Ipsum - это текст-"рыба", часто используемый в печати и вэб-дизайне. Lorem Ipsum является стандартной
			"рыбой" для текстов на латинице с начала XVI века. В то время некий безымянный печатник создал большую коллекцию
			размеров и форм шрифтов, используя Lorem Ipsum для распечатки образцов. Lorem Ipsum не только успешно пережил без
			заметных изменений пять веков, но и перешагнул в электронный дизайн. Его популяризации в новое время послужили
			публикация листов Letraset с образцами Lorem Ipsum в 60-х годах и, в более недавнее время, программы электронной
			вёрстки типа Aldus PageMaker, в шаблонах которых используется Lorem Ipsum.
		</section>
		<section style="text-align:center">
			<div class="about_img">
				<img src="img/screen/desktop-ui.png" />
			</div>
			<div class="about_img">
				<img src="img/screen/desktop-ui.png" />
			</div>
			<div class="about_img">
				<img src="img/screen/desktop-ui.png" />
			</div>
			<div class="clearfix"></div>
		</section>
	</xsl:template>

</xsl:stylesheet>

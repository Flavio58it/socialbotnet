<#macro masterTemplate title="Willkommen">
        <!DOCTYPE html
                PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
                "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>${title} | Tinyverse</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
    <div id="root">
        <div class="header">
        	<div class="header-inner">
	            <nav class="menu">
	                <ul>
	                    <li>
	                        <a href="/">Startseite</a>
	                    </li>
	                    <#if authenticatedUser??>
		                    <li>
		                        <a href="/timeline/${authenticatedUser.username}">Eigene Timeline</a>
		                    </li>
		                    <li>
		                        <a href="/logout">Abmelden</a>
		                    </li>
		                <#else>
		                    <li>
		                        <a href="/register">Registrieren</a>
		                    </li>
		                    <li>
		                        <a href="/login">Anmelden</a>
		                    </li>
		                </#if>
		            </ul>
		        </nav>
	            <div id="brand">
	                <a href="/">Social Bot Netz</a>
	            </div>
	            <div style="clear: both;"></div>
            </div>
		</div>
        <div class="main">
            <#nested />
        </div>
        <div class="footer">
            <p>
                SocialBotNetz &mdash; Ein botfreundliches soziales Netzwerk
            </p>

        </div>
    </div>
</body>
        </html>
        </#macro>
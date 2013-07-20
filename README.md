Locsy - your own location server
=====

Installation
===

Install tomcat 7 and MongoDB on your server. You can simply copy the locsy.war file into
tomcats webapps directory and it will be deployed automatically. You need to add a Locsy admin
user to your tomcat-users.xml file:

<pre><code>
&lt;tomcat-users&gt;
  &lt;user username="user" password="password" roles="locsyAdmin" /&gt;
&lt;/tomcat-users&gt;
</code></pre>

After you've done all this you can simply go to the url yourserver.com/locsy/settings.html and add new users to your
Locsy installation.


I did an evaluation of Geb, Spock, Groovy for a work related UI automation project. I wanted to get some seat time with the tool. 


Notes:

MAVEN_HOME env var needs to point to the root of your maven install
M2_HOME env var needs to point to the root of your maven install

If you are on windows, you may need to set MAVEN_OPTS -Dmaven.multiModuleProjectDirectory=$M2_HOME

Run tests via command line:

    ./mvnw test
    
    ./mvnw test site


Get maven plugins to load:

Click File -> Settings.
Expand Build, Execution, Deployment -> Build Tools -> Maven.
Check Use plugin registry.
Click OK or Apply.
For IntelliJ 14.0.1, open the preferences---not settings---to find the plugin registry option:
Click File -> Preferences.
Regardless of version, also invalidate the caches:
Click File -> Invalidate Caches / Restart.
Click Invalidate and Restart.




# Compiling with your Plugin
You need maven to use Surge obviously if there is shading involved

### 1. Add the maven coordinates to your pom

In your pom.xml
``` xml
<repository>
  <id>surge</id>
  <url>https://raw.githubusercontent.com/cyberpwnn/Central/master/</url>
</repository>

<dependency>
  <groupId>org.cyberpwn</groupId>
  <artifactId>Surge</artifactId>
  <version>1.7</version>
  <scope>provided</scope>
</dependency>
```

### 2. Update your plugin.yml

Your plugin.yml will no longer specify YOUR plugin. Instead it will specify a ghost plugin inside of surge (which will exist at runtime)
``` yaml
name: YOUR_NAME
version: YOUR_VERSION
main: surge.Main
```

### 3. Create a Plugin class

You do not need to extend java plugin, just create a class like so

``` java
import surge.control.Disable;
import surge.control.Enable;
import surge.control.Instance;
import surge.control.Plugin;

@Plugin
public class SomeCoolPlugin
{
	// Auto instance creation
	@Instance
	public SomeCoolPlugin instance;

	@Enable
	public void enable()
	{
		// you can call this method whatever you like
	}

	@Disable
	public void disable()
	{
		// you can call this method whatever you like
	}
}
```

### 4. Configure your pom SHADE plugin
You need to include surge in your jar

In your build section: 
``` xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.1.0</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
		        <artifactSet>
                            <includes>
                                <include>org.cyberpwn:Surge</include>
                            </includes>
                        </artifactSet>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

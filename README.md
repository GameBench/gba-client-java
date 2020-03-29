[GBA getting started](https://docs.gamebench.net/automation-interface-usage/http-api/#getting-started)

Please note this will only work with GBA version v1.5.0 or greater.

## Installation

### Requirements

* Java 1.8 or later

### Gradle users

```
plugins {
    id 'maven'
}

repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/gamebench/gba-client-java")
        credentials {
            username = System.getenv("USERNAME")
            password = System.getenv("TOKEN")
        }
    }
    jcenter()
}

dependencies {
    implementation "com.gamebench:gba-client-java:0.1.0"
}
```

## Usage

GbaClientExample.java

```java
import com.gamebench.gbaclientlib.*;

import java.util.List;

public class GbaClientExample {
    public static void main(String[] args) {
        try {
            GbaClient gbaClient = new GbaClient("http://localhost:8000");
            List<Device> devices = gbaClient.listDevices();
            System.out.println(devices);
        } catch (GbaClientException e) {
            e.printStackTrace();
        }
    }
}
```

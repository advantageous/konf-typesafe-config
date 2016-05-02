[Konf Website](http://advantageous.github.io/konf/)

# Konf - Typed Java Config Integration

This allows you to combine TypeSafe config and Konf.
You can have TypeSafe config be a fallback for Konf or the other way around.


You can load TypeSafe config as a Konf `Config` instance as follows:

#### Loading Typesafe config as a Konf Config object
```java

        Config config = TypeSafeConfig.typeSafeConfig();
        final String abc = config.getString("abc");
        assertEquals("abc", abc);
```

You can also chain TypeSafe config as fallback or Konfig as a fallback
for TypeSafe config as follows:


#### Konf as a fallback for TypeSafe config. 

```java


import static io.advantageous.config.ConfigLoader.config;
import static io.advantageous.config.ConfigLoader.configs;
import static io.advantageous.config.ConfigLoader.load;

...

    Config config;
    ...
        config = configs(TypeSafeConfig.typeSafeConfig(), config("test-config.js"));
```
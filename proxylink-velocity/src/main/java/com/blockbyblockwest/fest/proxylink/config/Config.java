package com.blockbyblockwest.fest.proxylink.config;

import com.blockbyblockwest.fest.proxylink.ProxyLinkVelocity;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class Config {

  private final File configFile;
  private ConfigurationNode rootNode;

  public Config(File configFile) {
    this.configFile = configFile;
  }

  public void load() throws IOException {
    copyFromJarOrCreateFile();

    ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader
        .builder().setDefaultOptions(
            ConfigurationOptions.defaults().setShouldCopyDefaults(true)).setFile(configFile)
        .build();

    rootNode = configLoader.load();

  }

  public ConfigurationNode getNode(Object... path) {
    return rootNode.getNode(path);
  }

  private void copyFromJarOrCreateFile() {
    if (!configFile.exists()) {
      try {
        configFile.getParentFile().mkdirs();
        try (InputStream input = ProxyLinkVelocity.class
            .getResourceAsStream("/" + configFile.getName())) {
          if (input != null) {
            Files.copy(input, configFile.toPath());
          } else {
            configFile.createNewFile();
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}

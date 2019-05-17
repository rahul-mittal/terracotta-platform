/*
 * Copyright (c) 2011-2019 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
package com.terracottatech.dynamic_config.xml.plugins;

import org.w3c.dom.Element;

import com.terracottatech.config.security.ObjectFactory;
import com.terracottatech.dynamic_config.config.Node;
import com.terracottatech.dynamic_config.xml.Utils;

import javax.xml.bind.JAXBElement;

public class Security {
  private static final ObjectFactory FACTORY = new ObjectFactory();

  private final Node node;

  public Security(Node node) {
    this.node = node;
  }

  public Element toElement() {
    com.terracottatech.config.security.Security security = createSecurity();

    JAXBElement<com.terracottatech.config.security.Security> jaxbElement = FACTORY.createSecurity(security);

    return Utils.createElement(jaxbElement);
  }

  com.terracottatech.config.security.Security createSecurity() {
    com.terracottatech.config.security.Security security = FACTORY.createSecurity();

    security.setSecurityRootDirectory(node.getSecurityDir().toString());

    if (node.getSecurityAuditLogDir() != null) {
      security.setAuditDirectory(node.getSecurityAuditLogDir().toString());
    }

    if (node.isSecuritySslTls()) {
      security.setSslTls(true);
    }

    if (node.getSecurityAuthc() != null) {
      com.terracottatech.config.security.Security.Authentication authentication = FACTORY.createSecurityAuthentication();

      switch (node.getSecurityAuthc()) {
        case "file": authentication.setFile(true); break;
        case "certificate": authentication.setCertificate(true); break;
        case "ldap": authentication.setLdap(true); break;
        default: throw new IllegalArgumentException("Unknown authentication type: " + node.getSecurityAuthc());
      }

      security.setAuthentication(authentication);
    }

    if (node.isSecurityWhitelist()) {
      security.setWhitelist(true);
    }

    return security;
  }
}

/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jgroups.tools.analyser.model;

import java.lang.reflect.Field;

import org.infinispan.factories.GlobalComponentRegistry;
import org.infinispan.factories.KnownComponentNames;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.marshall.GlobalMarshaller;
import org.infinispan.marshall.StreamingMarshaller;

public class MarshallerProvider {
   private StreamingMarshaller marshaller = null;

   public MarshallerProvider() {
      try {
         String ispnConfigXml = System.getProperty("ispn.config.xml");
         DefaultCacheManager dcm = new DefaultCacheManager(ispnConfigXml, false);
         dcm.getGlobalConfiguration().fluent().transport().clusterName("debug");
         dcm.getGlobalConfiguration().fluent().transport().addProperty("configurationFile", "jgroups-udp.xml");
         dcm.start();
         dcm.startCache();
         Field f = DefaultCacheManager.class.getDeclaredField("globalComponentRegistry");
         f.setAccessible(true);
         GlobalComponentRegistry gcr = (GlobalComponentRegistry) f.get(dcm);
         marshaller = gcr.getComponent(GlobalMarshaller.class, KnownComponentNames.GLOBAL_MARSHALLER);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public StreamingMarshaller getMarshaller() {
      return marshaller;
   }

}

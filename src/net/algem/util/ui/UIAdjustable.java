/*
 * @(#)UIAdjustable.java 2.9.4.12 28/09/15
 * 
 * Copyright (c) 1999-2015 Musiques Tangentes. All Rights Reserved.
 * 
 * This file is part of Algem.
 * Algem is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Algem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with Algem. If not, see http://www.gnu.org/licenses.
 * 
 */
package net.algem.util.ui;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.12
 * @since 2.9.4.12 24/09/15
 */
public interface UIAdjustable
{

  /**
   * Store some ui settings like the windows's size or location. 
   */
  void storeUISettings();
  
  /**
   * Gets some information about current ui settings.
   * @return a string describing some settings
   */
  String getUIInfo();
}

/*
 * @(#)AdministrativeSchedule.java 2.9.4.0 30/03/2015
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

package net.algem.planning;

import net.algem.util.BundleUtil;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.0
 * @since 2.9.4.0 24/03/2015
 */
public class AdministrativeSchedule 
   extends ScheduleObject
{

  public AdministrativeSchedule() {
  }

  public AdministrativeSchedule(Schedule d) {
    super(d);
  }

  @Override
  public String getScheduleLabel() {
    return person != null ? person.getFirstnameName() : "";
  }
  
  public String getRoomLabel() {
    return room != null ? room.getName() : getScheduleLabel();
  }

  @Override
  public String getScheduleDetail() {
    return BundleUtil.getLabel("Diary.label") + " " + getScheduleLabel();
  }

}

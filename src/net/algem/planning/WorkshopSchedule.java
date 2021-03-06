/*
 * @(#)WorkshopSchedule.java	2.9.6 16/03/16
 *
 * Copyright (c) 1999-2016 Musiques Tangentes. All Rights Reserved.
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
 * along with Algem. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.algem.planning;

import net.algem.course.Course;
import net.algem.util.BundleUtil;

/**
 * Workshop schedule.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.6
 */
public class WorkshopSchedule
        extends CourseSchedule
{

  public WorkshopSchedule() {
  }

  public WorkshopSchedule(Schedule d) {
    super(d);
  }

  @Override
  public String getScheduleDetail() {
    return BundleUtil.getLabel("Workshop.label") +" :" + ((Course) activity).getTitle() + "/" + person;
  }
}

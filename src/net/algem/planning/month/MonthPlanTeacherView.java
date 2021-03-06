/*
 * @(#)MonthPlanTeacherView.java	2.9.4.0 31/03/2015
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
 * along with Algem. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package net.algem.planning.month;

import net.algem.contact.Person;
import net.algem.contact.teacher.Teacher;
import net.algem.planning.Schedule;
import net.algem.planning.ScheduleDetailEvent;
import net.algem.planning.ScheduleObject;
import net.algem.util.model.GemList;
import net.algem.util.ui.GemChoice;
import net.algem.util.ui.GemChoiceFilterModel;

/**
 * comment
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.0
 */
public class MonthPlanTeacherView
        extends MonthPlanDetailView
{

  public MonthPlanTeacherView() {
    super(null);
  }
  
  public MonthPlanTeacherView(GemChoice teacher) {
    super(teacher);
  }
  
  @Override
  public boolean isNotFiltered(ScheduleObject p) {
    if (choice == null) {
      return Schedule.ADMINISTRATIVE != p.getType();
    }

    int id = choice.getKey();
    return Schedule.ADMINISTRATIVE != p.getType() && id > 0 && p.getPerson() != null && p.getPerson().getId() == id;
  }

  @Override
  public void detailChange(ScheduleDetailEvent evt) {
    Person p = ((ScheduleObject) evt.getSchedule()).getPerson();
    if (p != null && choice != null) {
      choice.setKey(p.getId());
    }
    //choix.setKey(evt.getSchedule().getPersonne().getId());
  }
  
  void reload(GemList<Teacher> list) {
    if (choice != null) {
      ((GemChoiceFilterModel) choice.getModel()).load(list);
    }
  }

}

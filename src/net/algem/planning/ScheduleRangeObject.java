/*
 * @(#)ScheduleRangeObject.java	2.6.a 19/09/12
 * 
 * Copyright (c) 1999-2012 Musiques Tangentes. All Rights Reserved.
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

import net.algem.contact.Person;

/**
 * Course schedule range.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.6.a
 * @since 1.0a 07/07/1999
 */
public class ScheduleRangeObject
        extends CourseSchedule
        implements java.io.Serializable
{

  private Person member;
  protected int memberId;
  protected int scheduleId;

  public ScheduleRangeObject() {
  }

  public ScheduleRangeObject(ScheduleRange d) {
    id = d.id;
    scheduleId = d.scheduleId;
    date = d.day;
    start = d.start;
    end = d.end;
    type = Schedule.COURSE_SCHEDULE;
    idper = d.teacherId;
    memberId = d.memberId;
    place = d.roomId;
    note = d.note;
  }

  public boolean equals(ScheduleRangeObject d) {
    return super.equals(d) && member.getId() == d.member.getId();
  }

  @Override
  public String toString() {
    return super.toString() + " " + member;
  }

  public void setMember(Person p) {
    member = p;
  }

  public Person getMember() {
    return member;
  }

  public int getMemberId() {
    return memberId;
  }

  public int getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(int id) {
    this.scheduleId = id;
  }
  
}

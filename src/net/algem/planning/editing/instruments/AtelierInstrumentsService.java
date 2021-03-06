/*
 * @(#)AtelierInstrumentsService.java 2.9.2 30/01/15
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
package net.algem.planning.editing.instruments;

import net.algem.config.Instrument;
import net.algem.contact.Person;
import net.algem.planning.Action;
import java.sql.SQLException;
import java.util.List;

/**
 * 
 * @author <a href="mailto:alexandre.delattre.biz@gmail.com">Alexd</a>
 * @version 2.9.2
 * @since 2.9.2
 */
public interface AtelierInstrumentsService {
    static class PersonInstrumentRow {
        public final Person person;
        public Instrument instrument;

        public PersonInstrumentRow(Person person, Instrument instrument) {
            this.person = person;
            this.instrument = instrument;
        }
    }

    /**
     * Retrieve instruments allocation, for a workshop action.
     * The allocation is a list of pair (person, instrument) called rows.
     * The implementation should make sure it returns a row for each participant of the workshop.
     *
     * @param action the action of the workshop
     * @return the allocation of instrument played by participant
     * @throws Exception
     */
    public List<PersonInstrumentRow> getInstrumentsAllocation(Action action) throws Exception;

    /**
     * Sets the instrument allocation, for a workshop action.
     * The allocation is a list of pair (person, instrument) called rows.
     *
     * @param action the action of the workshop
     * @param rows the rows to modify
     * @throws Exception
     */
    public void setInstrumentsAllocation(Action action, List<PersonInstrumentRow> rows) throws Exception;

    /**
     * Retrieve available instruments for a given person.
     * @param person
     * @return list of instrument played by this person
     * @throws SQLException
     */
    public List<Instrument> getAvailableInstruments(Person person) throws Exception;

    /**
     * Gets the allocated instrument for a given workshop action or person.
     * @param action
     * @param person
     * @return the instrument speficically allocated for this action, otherwise the first instrument of the person,
     * or null if no specific Instrument is defined.
   * @throws java.lang.Exception
     */
    public Instrument getAllocatedInstrument(Action action, Person person) throws Exception;

    /**
     * Gets the allocated instrument for a given workshop action or person.
     * @param actionId
     * @param personId
     * @return the instrument speficically allocated for this action, otherwise the first instrument of the person,
     * or null if no specific Instrument is defined.
   * @throws java.lang.Exception
     */
    public Instrument getAllocatedInstrument(int actionId, int personId) throws Exception;
}

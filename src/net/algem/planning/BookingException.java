/*
 * @(#)BookingException.java	2.9.5.0 09/02/16
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
package net.algem.planning;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version Expression projectVersion is undefined on line 13, column 15 in Templates/Classes/Exception.java.
 * @since Expression version is undefined on line 14, column 13 in Templates/Classes/Exception.java. 09/02/2016
 */
public class BookingException extends Exception {

    /**
     * Creates a new instance of <code>BookingException</code> without detail message.
     * @since 2.9.5.0 09/02/16
     */
    public BookingException() {
    }


    /**
     * Constructs an instance of <code>BookingException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BookingException(String msg) {
        super(msg);
    }
}

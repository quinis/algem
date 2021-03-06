/*
 * @(#)BillingIO.java 2.14.0 29/05/17
 * 
 * Copyright (c) 1999-2017 Musiques Tangentes. All Rights Reserved.
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
package net.algem.billing;

import java.sql.ResultSet;
import java.sql.SQLException;
import net.algem.config.ConfigKey;
import net.algem.config.ConfigUtil;
import net.algem.util.DataConnection;
import net.algem.util.model.TableIO;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.14.0
 * @since 2.4.d 07/06/12
 */
public abstract class BillingIO
        extends TableIO {

  /**
   * Retrieves the last quote/invoice number.
   * There must be no break in the numbering.
   * This number is composed as follows : the year on 2 digits, the month on 2 digits
   * and an incremental integer yymmN... (ex. : 1506124).
   *
   * @param table the table to query
   * @param dc connexion
   * @return a string-formatted billing id
   * @throws SQLException
   */
  protected static int getLastId(String table, DataConnection dc) throws SQLException {

    int last = -1;

    String query = "SELECT max(substring(numero from 5)::integer) FROM " + table;
    ResultSet rs = dc.executeQuery(query);
    if (rs.next()) {
      //System.out.println("last max "+ rs.getInt(1));
      last =  rs.getInt(1);
    }

    if (last == 0 || last == -1) {
      last = getFirstId(dc);
    }
    
    return last;
  }

  static int getFirstId(DataConnection dc) {
    String n = ConfigUtil.getConf(ConfigKey.ACCOUNTING_INVOICE_NUMBER.getKey());
    if (n != null) {
      return Integer.parseInt(n);
    }
    return -1;
  }
}

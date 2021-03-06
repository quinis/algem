/*
 * @(#)BankBranchView.java	2.8.m 04/09/13
 * 
 * Copyright (c) 1999-2013 Musiques Tangentes. All Rights Reserved.
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
package net.algem.bank;

import net.algem.contact.CodePostalCtrl;

/**
 * comment
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.8.m
 */
public interface BankBranchView
{

  public void setPostalCodeCtrl(CodePostalCtrl ctrl);

  public void setBankCodeCtrl(BankCodeCtrl ctrl);

  public String getBankName();

  public String getBankCode();

  public void setBankName(String s);

  public Bank getBank();

  public void setBank(Bank b);

  public BankBranch getBankBranch();

  public void setBankBranch(BankBranch g);

  public String getBranchCode();

  public String getBicCode();

  public void markBic(boolean ok);
}

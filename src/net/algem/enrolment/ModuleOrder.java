/*
 * @(#)ModuleOrder.java	2.9.4.13 05/11/15
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
package net.algem.enrolment;

import java.util.ArrayList;
import java.util.List;
import net.algem.planning.DateFr;

/**
 * Module order object model.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.13
 */
public class ModuleOrder
        implements java.io.Serializable
{

  private static final long serialVersionUID = 8232081164289977122L;
  
  protected int id;
  protected int idcmd;
  protected int payer;
  protected boolean stopped;
  
  /** Module id. */
  protected int module;
  
  /** Module index. */
  protected int index_module;
  
  /**
   * Amount of the order.
   * TODO : s'agit-il du prix de base, du prix total ?
   * Actuellement, aligné sur le montant d'une échéance.
   */
  protected double price;
  protected DateFr start;
  protected DateFr end;
  protected String modeOfPayment;
  protected PayFrequency payFrequency;
  protected int nOrderLines;
  protected String title;
  protected PricingPeriod pricing;
  protected int totalTime;
  protected double paymentAmount;
  
  private List<CourseOrder> courseOrders = new ArrayList<CourseOrder>();

  @Override
  public String toString() {
    return idcmd + "," + module + "," + price + "," + start + "," + end + "," + price + "," + modeOfPayment + "," + payFrequency + "," + nOrderLines;
  }

  public int getIdOrder() {
    return idcmd;
  }

  public void setIdOrder(int i) {
    idcmd = i;
  }

  public int getId() {
    return id;
  }

  public void setId(int i) {
    id = i;
  }

  public int getModule() {
    return module;
  }

  public void setModule(int i) {
    module = i;
  }

  public void setSelectedModule(int i) {
    index_module = i;
  }

  public int getSelectedModule() {
    return index_module;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double i) {
    price = i;
  }

  public int getPayer() {
    return payer;
  }

  public void setPayer(int i) {
    payer = i;
  }

  public DateFr getStart() {
    return start;
  }

  public void setStart(DateFr d) {
    start = d;
  }

  public DateFr getEnd() {
    return end;
  }

  public void setEnd(DateFr d) {
    end = d;
  }

  public String getModeOfPayment() {
    return modeOfPayment;
  }

  public void setModeOfPayment(String r) {
    modeOfPayment = r;
  }

  public PayFrequency getPayment() {
    return payFrequency;
  }

  public void setPayment(PayFrequency p) {
    payFrequency = p;
  }

  public PricingPeriod getPricing() {
    return pricing;
  }

  public void setPricing(PricingPeriod pricing) {
    this.pricing = pricing;
  }

  public int getTotalTime() {
    return totalTime;
  }

  public void setTotalTime(int minutes) {
    this.totalTime = minutes;
  }

  public double getPaymentAmount() {
    return paymentAmount;
  }

  public void setPaymentAmount(double p) {
    this.paymentAmount = p;
  }

  public int getNOrderLines() {
    return nOrderLines;
  }

  public void setNOrderLines(int i) {
    nOrderLines = i;
  }

  public void setTitle(String s) {
    title = s;
  }

  public String getTitle() {
    return title;
  }

  public List<CourseOrder> getCourseOrders() {
    return courseOrders;
  }

  public void setCourseOrders(List<CourseOrder> courseOrders) {
    this.courseOrders = courseOrders;
  }
  
  public void addCourseOrder(CourseOrder co) {
    courseOrders.add(co);
  }

  public boolean isStopped() {
    return stopped;
  }

  public void setStopped(boolean stopped) {
    this.stopped = stopped;
  }
  
  void clear() {
    courseOrders.clear();
  }

}

/**********************************************************************
 * $Source: /cvsroot/hibiscus/hibiscus/src/de/willuhn/jameica/hbci/rmi/filter/Attic/FilterTarget.java,v $
 * $Revision: 1.2 $
 * $Date: 2005/05/24 23:30:03 $
 * $Author: web0 $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/

package de.willuhn.jameica.hbci.rmi.filter;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.willuhn.jameica.hbci.rmi.Umsatz;

/**
 * Interface fuer ein einzelnes Filter-Ziel der Filter-Engine.
 * Um am Filter-Mechanismus beim Abrufen neuer Umsaetze teilnehmen
 * zu koennen, muss dieses Interface implementiert werden.
 * Wichtig: Die Implementierung muss einen parameterlosen
 * Konstruktor mit dem Modifier &quot;public&quot; (Bean-Spezifikation)
 * besitzen, um von der Filter-Engine instanziiert zu werden.
 */
public interface FilterTarget extends Remote
{
  /**
   * Liefert eine Liste von Umsatz-Filters, welche wiederrum die
   * Filterkritieren enthalten.
   * Dieses FilterTarget erhaelt anschliessend nur genau
   * die Umsaetze, die den Filter-Kriterien entsprechen.
   * @return Liste der Umsatzfilter.
   * @throws RemoteException
   */
  public Filter[] getFilters() throws RemoteException;

  /**
   * Wird von der Filter-Engine aufgerufen, wenn ein Umsatz einem der
   * obigen Filter entspricht. Der Filter, welcher den Treffer ausgeloest
   * hat, wird als Parameter mit uebergeben.
   * @param umsatz Umsatz.
   * @param filter Filter.
   * @throws RemoteException
   */
  public void match(Umsatz umsatz, Filter filter) throws RemoteException;
}


/**********************************************************************
 * $Log: FilterTarget.java,v $
 * Revision 1.2  2005/05/24 23:30:03  web0
 * @N Erster Code fuer OP-Verwaltung
 *
 * Revision 1.1  2005/05/09 23:47:24  web0
 * @N added first code for the filter framework
 *
 **********************************************************************/
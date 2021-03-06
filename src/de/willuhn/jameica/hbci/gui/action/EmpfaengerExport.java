/**********************************************************************
 * $Source: /cvsroot/hibiscus/hibiscus/src/de/willuhn/jameica/hbci/gui/action/EmpfaengerExport.java,v $
 * $Revision: 1.5 $
 * $Date: 2011/09/27 16:39:10 $
 * $Author: willuhn $
 * $Locker:  $
 * $State: Exp $
 *
 * Copyright (c) by willuhn.webdesign
 * All rights reserved
 *
 **********************************************************************/
package de.willuhn.jameica.hbci.gui.action;

import de.willuhn.jameica.gui.Action;
import de.willuhn.jameica.gui.GUI;
import de.willuhn.jameica.hbci.HBCI;
import de.willuhn.jameica.hbci.gui.dialogs.ExportDialog;
import de.willuhn.jameica.hbci.rmi.HibiscusAddress;
import de.willuhn.jameica.system.Application;
import de.willuhn.jameica.system.OperationCanceledException;
import de.willuhn.logging.Logger;
import de.willuhn.util.ApplicationException;
import de.willuhn.util.I18N;

/**
 * Action, ueber die Adressen exportieren werden koennen.
 * Als Parameter kann eine einzelnes Address-Objekt oder ein Array uebergeben werden.
 */
public class EmpfaengerExport implements Action
{

  /**
   * Erwartet ein Objekt vom Typ <code>HibiscusAddress</code> oder <code>HibiscusAddress[]</code>.
   * @see de.willuhn.jameica.gui.Action#handleAction(java.lang.Object)
   */
  public void handleAction(Object context) throws ApplicationException
  {
		I18N i18n = Application.getPluginLoader().getPlugin(HBCI.class).getResources().getI18N();

		if (context == null)
			throw new ApplicationException(i18n.tr("Bitte w�hlen Sie mindestens eine Adresse aus"));

    Object[] u = null;
		try
		{
			if (context instanceof HibiscusAddress)
			{
				u = new HibiscusAddress[1];
        u[0] = (HibiscusAddress) context;
			}
			else if (context instanceof HibiscusAddress[])
      {
        u = (Object[])context;
      }

			if (u == null)
        throw new ApplicationException(i18n.tr("Bitte w�hlen Sie einen oder mehrere Adressen aus"));

      ExportDialog d = new ExportDialog(u, HibiscusAddress.class);
      d.open();
		}
    catch (OperationCanceledException oce)
    {
      Logger.info(oce.getMessage());
      return;
    }
		catch (ApplicationException ae)
		{
			throw ae;
		}
		catch (Exception e)
		{
			Logger.error("error while exporting addresses",e);
			GUI.getStatusBar().setErrorText(i18n.tr("Fehler beim Exportieren der Adressen"));
		}
  }

}


/**********************************************************************
 * $Log: EmpfaengerExport.java,v $
 * Revision 1.5  2011/09/27 16:39:10  willuhn
 * @B XML-Export von Adressen funktionierte nicht mehr
 *
 * Revision 1.4  2011-05-11 10:20:29  willuhn
 * @N OCE fangen
 *
 * Revision 1.3  2007/07/16 12:48:32  willuhn
 * @B Fehler beim CSV-Import/Export von Adressen
 *
 * Revision 1.2  2007/04/23 18:07:14  willuhn
 * @C Redesign: "Adresse" nach "HibiscusAddress" umbenannt
 * @C Redesign: "Transfer" nach "HibiscusTransfer" umbenannt
 * @C Redesign: Neues Interface "Transfer", welches von Ueberweisungen, Lastschriften UND Umsaetzen implementiert wird
 * @N Anbindung externer Adressbuecher
 *
 * Revision 1.1  2006/10/05 16:42:28  willuhn
 * @N CSV-Import/Export fuer Adressen
 *
 **********************************************************************/
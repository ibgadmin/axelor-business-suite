/**
 * Axelor Business Solutions
 *
 * Copyright (C) 2014 Axelor (<http://axelor.com>).
 *
 * This program is free software: you can redistribute it and/or  modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axelor.apps.crm.service.batch;


import com.axelor.apps.base.db.Batch;
import com.axelor.apps.crm.db.CrmBatch;
import com.axelor.apps.crm.db.ICrmBatch;
import com.axelor.apps.crm.db.repo.CrmBatchRepository;
import com.axelor.exception.AxelorException;
import com.axelor.exception.db.IException;
import com.axelor.inject.Beans;

/**
 * InvoiceBatchService est une classe implémentant l'ensemble des batchs de
 * comptabilité et assimilé.
 * 
 * @author Geoffrey DUBAUX
 * 
 * @version 0.1
 */
public class CrmBatchService extends CrmBatchRepository{


// Appel 	
	
	/**
	 * Lancer un batch à partir de son code.
	 * 
	 * @param batchCode
	 * 		Le code du batch souhaité.
	 * 
	 * @throws AxelorException
	 */
	public Batch run(String batchCode) throws AxelorException {
				
		Batch batch;
		CrmBatch crmBatch = findByCode(batchCode);
		
		if (crmBatch != null){
			switch (crmBatch.getActionSelect()) {
			
			case ICrmBatch.BATCH_EVENT_REMINDER:
				batch = eventReminder(crmBatch);
				break;
				
			case ICrmBatch.BATCH_TARGET:
				batch = target(crmBatch);
				break;
				
			default:
				throw new AxelorException(String.format("Action %s inconnu pour le traitement %s", crmBatch.getActionSelect(), batchCode), IException.INCONSISTENCY);
			}
		}
		else {
			throw new AxelorException(String.format("Batch %s inconnu", batchCode), IException.INCONSISTENCY);
		}
		
		return batch;
	}
	
	
	public Batch eventReminder(CrmBatch crmBatch) {
		
		return Beans.get(BatchEventReminder.class).run(crmBatch);
		
	}
	
	public Batch target(CrmBatch crmBatch) {
		
		return Beans.get(BatchTarget.class).run(crmBatch);
		
	}

	
	
}
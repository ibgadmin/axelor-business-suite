/**
 * Axelor Business Solutions
 *
 * Copyright (C) 2017 Axelor (<http://axelor.com>).
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
package com.axelor.apps.production.web;

import java.util.List;

import javax.inject.Inject;

import com.axelor.apps.base.service.administration.GeneralService;
import com.axelor.apps.production.db.ProductionOrder;
import com.axelor.apps.production.exceptions.IExceptionMessage;
import com.axelor.apps.production.service.ProductionOrderSaleOrderService;
import com.axelor.apps.sale.db.SaleOrder;
import com.axelor.exception.AxelorException;
import com.axelor.i18n.I18n;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.common.base.Joiner;

public class ProductionOrderSaleOrderController {

	@Inject
	ProductionOrderSaleOrderService productionOrderSaleOrderService;

	@Inject
	protected GeneralService generalService;

	public void createProductionOrders(ActionRequest request, ActionResponse response) throws AxelorException {

		SaleOrder saleOrder = request.getContext().asType( SaleOrder.class );

		List<Long> productionOrderIdList = productionOrderSaleOrderService.generateProductionOrder(saleOrder);
		if (!productionOrderIdList.isEmpty()){
			response.setView(ActionView
							.define(I18n.get("Production order"))
				            .model(ProductionOrder.class.getName())
				            .add("grid", "production-order-grid")
				            .add("form", "production-order-form")
				            .domain("self.id in ("+Joiner.on(",").join(productionOrderIdList)+")")
				            .map());
		}else{
			response.setFlash(I18n.get(IExceptionMessage.PRODUCTION_ORDER_NO_GENERATION));
		}

	}


}

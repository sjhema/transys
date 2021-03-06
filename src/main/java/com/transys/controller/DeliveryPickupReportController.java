package com.transys.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.Address;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transys.core.util.MimeUtil;
import com.transys.model.DeliveryAddress;
import com.transys.model.Order;
import com.transys.model.SearchCriteria;

@Controller
@RequestMapping("/reports/deliveryPickupReport")
public class DeliveryPickupReportController extends CRUDController<Order> {
	public DeliveryPickupReportController(){	
		setUrlContext("reports/deliveryPickupReport");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/main.do")
	public String displayMain(ModelMap model, HttpServletRequest request) {
		request.getSession().removeAttribute("searchCriteria");
		setupList(model, request);
		setupCreate(model, request);
		return urlContext + "/list";
	}
	
	@Override
	public String list(ModelMap model, HttpServletRequest request) {
		setupList(model, request);
		setupCreate(model, request);
		return urlContext + "/list";
	}
	
	@Override
	public void setupCreate(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		//TODO fix me
		criteria.getSearchMap().remove("_csrf");
		
		List<Order> orderList = genericDAO.search(getEntityClass(), criteria, "id", null, null);
		model.addAttribute("ordersList", orderList);
		model.addAttribute("dumpsterSizeAggregation", setDumpsterSizeAggregation(model, orderList));
		
		String addrssQuery = "select obj from DeliveryAddress obj where obj.deleteFlag='1' order by obj.line1 asc";
		List<DeliveryAddress> addresses = genericDAO.executeSimpleQuery(addrssQuery);
		model.addAttribute("deliveryAddresses", addresses);
		model.addAttribute("deliveryDateFrom", criteria.getSearchMap().get("deliveryDateFrom"));
		model.addAttribute("deliveryDateTo", criteria.getSearchMap().get("deliveryDateTo"));
		model.addAttribute("pickupDateFrom", criteria.getSearchMap().get("pickDateFrom"));
		model.addAttribute("pickDateTo", criteria.getSearchMap().get("pickDateTo"));	
	}
	
	private String setDumpsterSizeAggregation(ModelMap model, List<Order> orderList) {
		StringBuffer selectedOrderIds = new StringBuffer();
		if (orderList == null || orderList.size() == 0) {
			// throw Exception??
			System.out.println("No orders matching criteria, report will be empty.");
		}
		
		for(Order o : orderList) {
			selectedOrderIds.append(o.getId() + ",");
		}
		List<?> aggregationResults = genericDAO.executeSimpleQuery("select dumpsterSize.size, count(*) from Order p where p.deleteFlag='1' and p.id IN (" + selectedOrderIds.substring(0,selectedOrderIds.lastIndexOf(",")) + ") group by p.dumpsterSize.size");
		List<String> dumpsterSizes = new ArrayList<String>();
		List<String> count = new ArrayList<String>();
		
		ObjectMapper objectMapper = new ObjectMapper();
		int index = 0;
		for (Object o : aggregationResults) {
			String jsonResponse = StringUtils.EMPTY;
			try {
				jsonResponse = objectMapper.writeValueAsString(o);
	
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonResponse = jsonResponse.substring(1, jsonResponse.length()-1);
			
			String [] tokens = jsonResponse.split(",");
			dumpsterSizes.add(index, tokens[0].substring(1, tokens[0].length()-1)); // eliminate quotes
			count.add(index++, tokens[1]);
		}

		StringBuffer aggregationResult = new StringBuffer();
		for (int i = 0; i < dumpsterSizes.size(); i++) {
			aggregationResult.append(dumpsterSizes.get(i) + " : " + count.get(i) + "   ");
		}
		
		return aggregationResult.toString();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/generateDeliveryPickupReport.do")
	public void export(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@RequestParam("type") String type, Object objectDAO, Class clazz) {
		try {
			List<Map<String,Object>> reportData = prepareReportData(model, request);

			type = setRequestHeaders(response, type, "deliveryPickupReport");
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			Map<String, Object> params = new HashMap<String, Object>();

			out = dynamicReportService.generateStaticReport("deliveryPickupReport", reportData, params, type, request);
			/*} else {
				out = dynamicReportService.generateStaticReport("ordersRevenueReport" + "print", reportData, params, type,
						request);
			}*/

			out.writeTo(response.getOutputStream());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Unable to create file :" + e);
			request.getSession().setAttribute("errors", e.getMessage());
		}
	}

	private List<Map<String, Object>> prepareReportData(ModelMap model, HttpServletRequest request) {
		SearchCriteria criteria = (SearchCriteria) request.getSession().getAttribute("searchCriteria");
		criteria.getSearchMap().remove("_csrf");
		
		List<Order> orderList = genericDAO.search(getEntityClass(), criteria,"id",null,null);
		String dumpsterSizeAggregation = setDumpsterSizeAggregation(model, orderList);
		
		List<Map<String, Object>> reportData = new ArrayList<Map<String, Object>>();
		for (Order anOrder : orderList) {
			DeliveryAddress deliveryAddress = anOrder.getDeliveryAddress();
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", StringUtils.EMPTY + anOrder.getId().toString());
			map.put("customer", StringUtils.EMPTY + anOrder.getCustomer().getCompanyName());
			map.put("deliveryAddress", StringUtils.EMPTY + deliveryAddress.getFullLine());
			map.put("city", StringUtils.EMPTY + deliveryAddress.getCity());
			map.put("dumpsterSize", StringUtils.EMPTY + anOrder.getDumpsterSize().getSize());
			map.put("dumpsterNum", (anOrder.getDumpster() == null ? StringUtils.EMPTY : anOrder.getDumpster().getDumpsterNum()));
			map.put("deliveryDate", anOrder.getFormattedDeliveryDate());
			map.put("pickupDate", anOrder.getFormattedPickupDate());
			
			Object deliveryDateFrom = criteria.getSearchMap().get("deliveryDateFrom");
			map.put("deliveryDateFrom", deliveryDateFrom == null ? StringUtils.EMPTY : deliveryDateFrom );
			
			Object deliveryDateTo = criteria.getSearchMap().get("deliveryDateTo");
			map.put("deliveryDateTo", deliveryDateTo == null ? StringUtils.EMPTY : deliveryDateTo );
			
			Object pickupDateFrom = criteria.getSearchMap().get("pickupDateFrom");
			map.put("pickupDateFrom", pickupDateFrom == null ? StringUtils.EMPTY : pickupDateFrom );
			
			Object pickupDateTo = criteria.getSearchMap().get("pickupDateTo");
			map.put("pickupDateTo", pickupDateTo == null ? StringUtils.EMPTY : pickupDateTo );
			
			map.put("dumpsterSizeAggregation", dumpsterSizeAggregation);
			
			ObjectMapper objectMapper = new ObjectMapper();
			String jSonResponse = StringUtils.EMPTY;
			try {
				jSonResponse = objectMapper.writeValueAsString(map);
				System.out.println(jSonResponse);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			reportData.add(map);
		}
		
		return reportData;
	}
	
}

package com.tarento.analytics.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tarento.analytics.ConfigurationLoader;
import com.tarento.analytics.config.AppConfiguration;
import com.tarento.analytics.constant.Constants;
import com.tarento.analytics.dto.DataSourceRequest;
import com.tarento.analytics.dto.RoleDto;
import com.tarento.analytics.exception.AINException;
import com.tarento.analytics.model.dashboardconfig.Actions;
import com.tarento.analytics.model.dashboardconfig.Dashboard;
import com.tarento.analytics.model.dashboardconfig.Dashboards;
import com.tarento.analytics.model.dashboardconfig.DataSources;
import com.tarento.analytics.model.dashboardconfig.RoleActions;
import com.tarento.analytics.model.dashboardconfig.RoleDashboard;
import com.tarento.analytics.model.dashboardconfig.TestConnection;
import com.tarento.analytics.model.dashboardconfig.UserRole;
import com.tarento.analytics.model.dashboardconfig.Visualizations;
import com.tarento.analytics.repository.MetadataRepository;
import com.tarento.analytics.service.MetadataService;

@Service("metadataService")
public class MetadataServiceImpl implements MetadataService {

	public static final Logger logger = LoggerFactory.getLogger(MetadataServiceImpl.class);

	@Autowired
	private ConfigurationLoader configurationLoader;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired 
	private AppConfiguration appConfig; 
	
	@Autowired
	private RetryTemplate retryTemplate; 
	
	@Autowired
	private MetadataRepository metadataRepository;

	@Autowired
	private RestService restService;

	@Override
	public Dashboards getDashboard(String profileName, String dashboardId, String catagory
			) throws AINException, IOException {
		
		JsonNode dashboardNode = metadataRepository.getDashboard(profileName, dashboardId);

		if (dashboardNode != null) {
			logger.info("Returning from Database Configurations");
			return new ObjectMapper().convertValue(dashboardNode, Dashboards.class);
		}

		return null;
	}
	
	@Override
	public ArrayNode getDashboardConfiguration(String profileName, String dashboardId, String catagory,
			List<RoleDto> roleIds) throws AINException, IOException {
		ArrayNode dashboardArray = JsonNodeFactory.instance.arrayNode();
		List<JsonNode> listOfDashboards = metadataRepository.getDashboardConfiguration(profileName, dashboardId);
		for(JsonNode eachDashboard : listOfDashboards) { 
			dashboardArray.add(eachDashboard); 
		}
		
		if(dashboardArray != null && !dashboardArray.isEmpty()) {
			logger.info("Returning from Database Configurations" );
			return dashboardArray; 
		}
		
		
		

		ObjectNode dashboardNode = configurationLoader.getConfigForProfile(profileName, ConfigurationLoader.MASTER_DASHBOARD_CONFIG);
		ArrayNode dashboardNodes = (ArrayNode) dashboardNode.findValue(Constants.DashBoardConfig.DASHBOARDS);

		ObjectNode roleMappingNode = configurationLoader.getConfigForProfile(profileName, ConfigurationLoader.ROLE_DASHBOARD_CONFIG);
		ArrayNode rolesArray = (ArrayNode) roleMappingNode.findValue(Constants.DashBoardConfig.ROLES);
		ArrayNode dbArray = JsonNodeFactory.instance.arrayNode();

		rolesArray.forEach(role -> {
			Object roleId = roleIds.stream()
					.filter(x -> role.get(Constants.DashBoardConfig.ROLE_ID).asLong() == (x.getId())).findAny()
					.orElse(null);
			if (null != roleId) {
				ArrayNode visArray = JsonNodeFactory.instance.arrayNode();
				ArrayNode widgetArray = JsonNodeFactory.instance.arrayNode();
				ArrayNode filterArray = JsonNodeFactory.instance.arrayNode();
				// checks role has given db id
				role.get(Constants.DashBoardConfig.DASHBOARDS).forEach(db -> {
					ArrayNode visibilityArray = (ArrayNode) db.findValue(Constants.DashBoardConfig.VISIBILITY);
					ObjectNode copyDashboard = objectMapper.createObjectNode();
					
					JsonNode name = JsonNodeFactory.instance.textNode("");
					JsonNode id = JsonNodeFactory.instance.textNode("");
					JsonNode style = JsonNodeFactory.instance.textNode("");
					JsonNode isActive = JsonNodeFactory.instance.booleanNode(false);
					JsonNode widgetTitle = JsonNodeFactory.instance.textNode("");
					JsonNode showFilters= JsonNodeFactory.instance.booleanNode(false);
					JsonNode showWidgets = JsonNodeFactory.instance.booleanNode(false);
					JsonNode showWidgetTitle = JsonNodeFactory.instance.booleanNode(false);
					
					// Set the FY Info in Title if needed
					// JsonNode title = JsonNodeFactory.instance.textNode(fyInfo);

					if (db.get(Constants.DashBoardConfig.ID).asText().equalsIgnoreCase(dashboardId)) {
						for (JsonNode dbNode : dashboardNodes) {
							if (dbNode.get(Constants.DashBoardConfig.ID).asText().equalsIgnoreCase(dashboardId)) {
								logger.info(String.format("dbNode: %s" ,dbNode));
								name = dbNode.get(Constants.DashBoardConfig.NAME);
								id = dbNode.get(Constants.DashBoardConfig.ID);
								style = dbNode.get(Constants.DashBoardConfig.STYLE);
								isActive = dbNode.get(Constants.DashBoardConfig.IS_ACTIVE); 
								widgetTitle = dbNode.get(Constants.DashBoardConfig.WIDGET_TITLE); 
								showFilters = dbNode.get(Constants.DashBoardConfig.SHOW_FILTERS); 
								showWidgets = dbNode.get(Constants.DashBoardConfig.SHOW_WIDGETS);
								showWidgetTitle = dbNode.get(Constants.DashBoardConfig.SHOW_WIDGET_TITLE);
								
								dbNode.get(Constants.DashBoardConfig.VISUALISATIONS).forEach(visual -> {
									visArray.add(visual);
								});
								dbNode.get(Constants.DashBoardConfig.WIDGET_CHARTS).forEach(widget -> {
									widgetArray.add(widget);
								});
								dbNode.get(Constants.DashBoardConfig.FILTERS).forEach(filter -> {
									JsonNode node = filter.deepCopy(); 
									applyVisilibityLayer(visibilityArray, node);
									filterArray.add(node);
								});
							}
							copyDashboard.set(Constants.DashBoardConfig.NAME, name);
							copyDashboard.set(Constants.DashBoardConfig.ID, id);
							copyDashboard.set(Constants.DashBoardConfig.STYLE, style);
							copyDashboard.set(Constants.DashBoardConfig.IS_ACTIVE, isActive);
							copyDashboard.set(Constants.DashBoardConfig.WIDGET_TITLE, widgetTitle);
							copyDashboard.set(Constants.DashBoardConfig.SHOW_FILTERS, showFilters);
							copyDashboard.set(Constants.DashBoardConfig.SHOW_WIDGETS, showWidgets);
							copyDashboard.set(Constants.DashBoardConfig.SHOW_WIDGET_TITLE, showWidgetTitle);
							copyDashboard.set(Constants.DashBoardConfig.WIDGET_CHARTS, widgetArray);
							copyDashboard.set(Constants.DashBoardConfig.FILTERS, filterArray);
							copyDashboard.set(Constants.DashBoardConfig.VISUALISATIONS, visArray);

						} // );
						dbArray.add(copyDashboard);
					}
				});
			}
		});
		return dbArray;
	}
	
	private void applyVisilibityLayer(ArrayNode visibilityArray, JsonNode filter) {
		try { 
		visibilityArray.forEach(visibility -> {
			String visibilityKey = visibility.get(Constants.DashBoardConfig.KEY).asText();
			String filterKey = filter.get(Constants.DashBoardConfig.KEY).asText();
			if(visibilityKey.equals(filterKey)) { 
				ArrayNode valuesAllowed = (ArrayNode) visibility.get(Constants.DashBoardConfig.VALUE);
				ArrayNode valuesAvailable = (ArrayNode) filter.get(Constants.DashBoardConfig.VALUES);
				ObjectNode availableValuesList = new ObjectMapper().createObjectNode(); 
				ArrayNode availableValuesArray = availableValuesList.putArray(Constants.DashBoardConfig.VALUES);
				List<String> allowedValuesList = new ArrayList<>();
				valuesAllowed.forEach(allowedValue -> { 
					allowedValuesList.add(allowedValue.asText());  
				});
				for(int i = 0 ; i < valuesAvailable.size() ; i++) { 
					if(allowedValuesList.contains(valuesAvailable.get(i).asText())) { 
						availableValuesArray.add(valuesAvailable.get(i).asText());  
					}
				}
				if(availableValuesArray.size() > 0) { 
					ObjectNode filterObjectNode = (ObjectNode) filter;
					filterObjectNode.put(Constants.DashBoardConfig.VALUES, availableValuesArray);
				}
			}
		});
		} catch (Exception e) { 
			
		}
	}
	
	@Override
	public List<Dashboard> getDashboardsForProfile(String profileName, List<RoleDto> rolesOfUser)
			throws AINException, IOException {
		List<Dashboard> dashboardList = new LinkedList<>(); 
		rolesOfUser.forEach(roleDto -> {
			String roleName = roleDto.getName();
			List<JsonNode> dashboards = metadataRepository.getDashboardsForProfile(profileName, roleName);
			for(JsonNode node : dashboards) { 
				Dashboard dashboard = new Dashboard(node.get(Constants.DashBoardConfig.ID).asText(), node.get(Constants.DashBoardConfig.NAME).asText(), null);
				dashboardList.add(dashboard);
			}
			
		});
		Set<String> nameSet = new HashSet<>();
		Set<String> idSet = new HashSet<>();
		return dashboardList.stream()
	            .filter(e -> nameSet.add(e.getName()))
	            .filter(e -> idSet.add(e.getId()))
	            .collect(Collectors.toList());
	}

	@Override
	public ArrayNode getReportsConfiguration(String profileName, String dashboardId, String catagory,
			List<RoleDto> roleIds) throws AINException, IOException {


		Calendar cal = Calendar.getInstance();
		cal.set(cal.getWeekYear()-1, Calendar.APRIL, 1);
		ObjectNode dashboardNode = configurationLoader.getConfigForProfile(profileName, ConfigurationLoader.MASTER_REPORTS_CONFIG);
		ArrayNode dashboardNodes = (ArrayNode) dashboardNode.findValue(Constants.DashBoardConfig.DASHBOARDS);

		ObjectNode roleMappingNode = configurationLoader.getConfigForProfile(profileName, ConfigurationLoader.ROLE_REPORTS_CONFIG);
		ArrayNode rolesArray = (ArrayNode) roleMappingNode.findValue(Constants.DashBoardConfig.ROLES);
		ArrayNode dbArray = JsonNodeFactory.instance.arrayNode();

		rolesArray.forEach(role -> {
			Object roleId = roleIds.stream()
					.filter(x -> role.get(Constants.DashBoardConfig.ROLE_ID).asLong() == (x.getId())).findAny()
					.orElse(null);
			if (null != roleId) {
				ArrayNode visArray = JsonNodeFactory.instance.arrayNode();
				ArrayNode filterArray = JsonNodeFactory.instance.arrayNode();
				// checks role has given db id
				role.get(Constants.DashBoardConfig.DASHBOARDS).forEach(db -> {
					ArrayNode visibilityArray = (ArrayNode) db.findValue(Constants.DashBoardConfig.VISIBILITY);
					ObjectNode copyDashboard = objectMapper.createObjectNode();
					
					JsonNode name = JsonNodeFactory.instance.textNode("");
					JsonNode id = JsonNodeFactory.instance.textNode("");

					if (db.get(Constants.DashBoardConfig.ID).asText().equalsIgnoreCase(dashboardId)) {
						// dasboardNodes.forEach(dbNode -> {
						for (JsonNode dbNode : dashboardNodes) {
							if (dbNode.get(Constants.DashBoardConfig.ID).asText().equalsIgnoreCase(dashboardId)) {
								logger.info("dbNode: " + dbNode);
								name = dbNode.get(Constants.DashBoardConfig.NAME);
								id = dbNode.get(Constants.DashBoardConfig.ID);
								dbNode.get(Constants.DashBoardConfig.VISUALISATIONS).forEach(visual -> {
									visArray.add(visual);
								});
								dbNode.get(Constants.DashBoardConfig.FILTERS).forEach(filter -> {
									JsonNode node = filter.deepCopy(); 
									applyVisilibityLayer(visibilityArray, node);
									filterArray.add(node);
								});
							}
							copyDashboard.set(Constants.DashBoardConfig.NAME, name);
							copyDashboard.set(Constants.DashBoardConfig.ID, id);
							copyDashboard.set(Constants.DashBoardConfig.FILTERS, filterArray);
							copyDashboard.set(Constants.DashBoardConfig.VISUALISATIONS, visArray);

						}
						dbArray.add(copyDashboard);
					}
				});
			}
		});
		return dbArray;
	}

	@Override
	public Object getReportsForProfile(String profileName, List<RoleDto> roleIds) throws AINException, IOException {
		ObjectNode roleMappingNode = configurationLoader.getConfigForProfile(profileName,
				ConfigurationLoader.ROLE_REPORTS_CONFIG);
		ArrayNode rolesArray = (ArrayNode) roleMappingNode.findValue(Constants.DashBoardConfig.ROLES);
		ArrayNode dbArray = JsonNodeFactory.instance.arrayNode();

		rolesArray.forEach(role -> {
			Object roleId = roleIds.stream()
					.filter(x -> role.get(Constants.DashBoardConfig.ROLE_ID).asLong() == (x.getId())).findAny()
					.orElse(null);
			logger.info(String.format("roleId: %s" , roleId));
			RoleDto dto = RoleDto.class.cast(roleId);
			if (dto != null && dto.getId() != null && role.get(Constants.DashBoardConfig.ROLE_ID).asLong() == dto.getId())
				role.get(Constants.DashBoardConfig.DASHBOARDS).forEach(db -> {
					JsonNode name = JsonNodeFactory.instance.textNode("");
					JsonNode id = JsonNodeFactory.instance.textNode("");
					JsonNode description = JsonNodeFactory.instance.textNode("");
					name = db.get(Constants.DashBoardConfig.NAME);
					id = db.get(Constants.DashBoardConfig.ID);
					description  = db.get(Constants.DashBoardConfig.DESCRIPTION);
					ObjectNode copyDashboard = objectMapper.createObjectNode();
					copyDashboard.set(Constants.DashBoardConfig.NAME, name);
					copyDashboard.set(Constants.DashBoardConfig.ID, id);
					copyDashboard.set(Constants.DashBoardConfig.DESCRIPTION, description);
					dbArray.add(copyDashboard);
				});

		});
		return dbArray;
	}

	@Override
	public List<String> getUserInfo(String authToken, String userId) {
		String url = appConfig.getUserReadHost() + appConfig.getUserReadApi() + userId; 
		List<String> roleList = new ArrayList<String>(); 
		HttpHeaders headers = new HttpHeaders();
		if (authToken != null && !authToken.isEmpty()) { 
			headers.add("Authorization", appConfig.getUserReadApiKey());
			headers.add("x-authenticated-user-token", authToken);
		}
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> headerEntity = new HttpEntity<>("{}", headers);

		JsonNode responseNode = null;
		try {
			ResponseEntity<Object> response = retryTemplate.getForEntity(url, headerEntity);
			responseNode = new ObjectMapper().convertValue(response.getBody(), JsonNode.class);
			logger.info(String.format("RestTemplate response :- %s" , responseNode));

		} catch (HttpClientErrorException e) {
			logger.error(String.format("get client exception: %s" , e.getMessage()));
		}
		if(responseNode != null && responseNode.get("result") != null && responseNode.get("result").get("response") != null && responseNode.get("result").get("response").get("roles") != null) { 
			ArrayNode arrayNode = (ArrayNode) responseNode.get("result").get("response").get("roles");
			arrayNode.forEach(eachNode -> roleList.add(eachNode.asText())
			);
		}
		logger.info(String.format("Role List : %s" , roleList.toString()));
		return roleList;
	}

	@Override
	public Object getDataSourceConfig(DataSourceRequest dataSourceRequest) {
		return metadataRepository.findForCriteria(dataSourceRequest);
	}

	@Override
	public JsonNode postChart(Visualizations visualizations) {
		JsonNode response = null; 
		if (visualizations.getId() != null) {
			response = metadataRepository.updateVisualizations(visualizations);
		}
		else {
			response = metadataRepository.postVisualizations(visualizations);
		}
		return response; 
	}
	
	@Override
	public JsonNode postDashboard(Dashboards dashboard) {
		JsonNode response = null; 
		if (dashboard.getId() != null) {
			response = metadataRepository.updateDashboards(dashboard);
		}
		else {
			response = metadataRepository.postDashboards(dashboard);
		}
		return response; 
	}
	
	@Override
	public JsonNode postUserRole(UserRole userRole){
		return metadataRepository.postUserRoleMappings(userRole); 
	}
	
	@Override
	public JsonNode postRoleDashboard(RoleDashboard roleDashboard){
		return metadataRepository.postRoleDashboards(roleDashboard); 
	}
	
	@Override
	public Boolean updateRoleDashboard(RoleDashboard roledashboard){
		return metadataRepository.updateRoleDashboards(roledashboard); 
	}
	
	@Override
	public Visualizations getChartConfiguration(String visualizationCode, String catagory) throws AINException, IOException {
		JsonNode chartNode = metadataRepository.getChartConfiguration(visualizationCode);
		
		if(chartNode != null) {
			logger.info("Returning from Database Configurations");
			Visualizations visualizations = new ObjectMapper().convertValue(chartNode, Visualizations.class); 
			return visualizations; 
		}
		
		return null; 
	}
	
	@Override
	public JsonNode postDataSource(DataSources dataSource) {
		JsonNode response = null; 
		if (dataSource.getId() != null) {
			response = metadataRepository.updateDataSource(dataSource);
		} else {
			response = metadataRepository.postDataSource(dataSource);
		}
		return response;
	}
	
	@Override
	public DataSources getDataSource(Long id, String catagory) throws AINException, IOException {
		JsonNode responseNode = metadataRepository.getDataSource(id);
		if(responseNode != null) {
			logger.info("Returning from Data source" );
			DataSources dataSources = new ObjectMapper().convertValue(responseNode, DataSources.class); 
			return dataSources; 
		}
		return null; 
	}
	
	@Override
	public JsonNode postRoleAction(RoleActions roleActions){
		return metadataRepository.postRoleActionMappings(roleActions); 
	}
	
	@Override
	public JsonNode postAction(Actions actions) {
		return metadataRepository.postActions(actions); 
	}
	
	@Override
	public JsonNode updateAction(Actions actions) {
		return metadataRepository.updateActions(actions); 
	}
	
	@Override
	public Boolean deleteAction(Long id) {
		return metadataRepository.deleteActions(id); 
	}
	
	@Override
	public ArrayNode getAction(Long id) throws AINException, IOException {
		ArrayNode actionsArray = JsonNodeFactory.instance.arrayNode();
		List<JsonNode> listOfActions = metadataRepository.getAction(id);
		for(JsonNode eachAction : listOfActions) { 
			actionsArray.add(eachAction); 
		}
		
		if(actionsArray != null) {
			logger.info("Returning from Actions" );
			return actionsArray; 
		}
		return actionsArray; 
	}
	
	@Override
	  public String testConnection(TestConnection testConnection) {
	    int response = restService.testConnection(testConnection.getUrl(),testConnection.getUsername(), testConnection.getPassword()); 
	    String connectionStatus = "";
	    if (response == 200) {
	    	connectionStatus = "The connection was successful!";
	    }
	    if (response == 401) {
	    	connectionStatus = "Connection failure! Authorization Required!";
	    }
	    if (response == 404) {
	    	connectionStatus = "Connection failure! URL not found!";
	    }
	    return connectionStatus;
	  }

	@Override
	public List<DataSources> getAllDataSources() throws AINException, IOException {
		List<DataSources> dataSourcesList = new ArrayList<>(); 
		List<JsonNode> listOfDataSource = metadataRepository.getAllDataSources(); 
		for (JsonNode eachDashboard : listOfDataSource) {
			DataSources dataSources = new ObjectMapper().convertValue(eachDashboard, DataSources.class);
			dataSourcesList.add(dataSources); 
		}
		if (!dataSourcesList.isEmpty()) {
			logger.info("Returning from Data source");
			return dataSourcesList;
		}
		return null;
	}

	@Override
	public List<Visualizations> getAllVisualizations() throws AINException, IOException {
		List<Visualizations> visualizationsList = new ArrayList<>(); 
		List<JsonNode> listOfVisualizations = metadataRepository.getAllVisualizations(); 
		for (JsonNode eachVisualization : listOfVisualizations) {
			visualizationsList.add(new ObjectMapper().convertValue(eachVisualization, Visualizations.class));   
		}

		if (!visualizationsList.isEmpty()) {
			logger.info("Returning from Visualizations");
			return visualizationsList;
		}
		return null;
	}
	
	@Override
	public List<Dashboards> getAllDashboards() throws AINException, IOException {
		List<Dashboards> dashboardList = new ArrayList<>(); 
		List<JsonNode> listOfDashboards = metadataRepository.getAllDashboards(); 
		for (JsonNode eachVDashboard : listOfDashboards) {
			dashboardList.add(new ObjectMapper().convertValue(eachVDashboard, Dashboards.class));
		}

		if (!dashboardList.isEmpty()) {
			logger.info("Returning from Dashboards");
			return dashboardList;
		}
		return null;
	}

}
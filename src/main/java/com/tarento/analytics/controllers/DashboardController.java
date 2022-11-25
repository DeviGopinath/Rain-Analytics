package com.tarento.analytics.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tarento.analytics.constant.Constants;
import com.tarento.analytics.constant.ErrorCode;
import com.tarento.analytics.dto.AggregateRequestDto;
import com.tarento.analytics.dto.DashboardsDto;
import com.tarento.analytics.dto.DataSourceDto;
import com.tarento.analytics.dto.RequestDto;
import com.tarento.analytics.dto.RoleDto;
import com.tarento.analytics.dto.TestConnectionDto;
import com.tarento.analytics.dto.UserDto;
import com.tarento.analytics.dto.VisualizationsDto;
import com.tarento.analytics.exception.AINException;
import com.tarento.analytics.model.ConfigDetail;
import com.tarento.analytics.model.Item;
import com.tarento.analytics.model.dashboardconfig.Actions;
import com.tarento.analytics.model.dashboardconfig.Dashboards;
import com.tarento.analytics.model.dashboardconfig.DataSources;
import com.tarento.analytics.model.dashboardconfig.RoleActions;
import com.tarento.analytics.model.dashboardconfig.RoleDashboard;
import com.tarento.analytics.model.dashboardconfig.TestConnection;
import com.tarento.analytics.model.dashboardconfig.UserRole;
import com.tarento.analytics.model.dashboardconfig.Visualizations;
import com.tarento.analytics.org.service.ClientServiceFactory;
import com.tarento.analytics.org.service.TarentoServiceImpl;
import com.tarento.analytics.repository.RedisRepository;
import com.tarento.analytics.service.MetadataService;
import com.tarento.analytics.utils.ModelDtoMapperUtil;
import com.tarento.analytics.utils.PathRoutes;
import com.tarento.analytics.utils.ResponseGenerator;

@RestController
@RequestMapping(PathRoutes.DashboardApi.DASHBOARD_ROOT_PATH)
public class DashboardController {

	public static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	public static final String TARENTO_ADMIN = "Tarento Admin";

	@Autowired
	TarentoServiceImpl tarentoServiceImpl;

	@Autowired
	private MetadataService metadataService;

	@Autowired
	private ClientServiceFactory clientServiceFactory;

	@Autowired
	private RedisRepository redisRepository;
	
	ObjectMapper objectMapper = new ObjectMapper();

	@GetMapping(value = PathRoutes.DashboardApi.TEST_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
	public String getTest() throws JsonProcessingException {
		return ResponseGenerator.successResponse("success");

	}

	@PostMapping(value = "/item")
	public String addItem(@RequestBody Item item) {
		redisRepository.save(item);
		return item.toString();
	}

	@GetMapping(value = "/item")
	public List<String> getItem(@RequestParam(value = "id", required = false) String id) {
		return redisRepository.findAll();
	}

	@GetMapping(value = PathRoutes.DashboardApi.GET_DASHBOARD_CONFIG + "/{profileName}" + "/{dashboardId}")
	public String getDashboardConfiguration(@PathVariable String profileName, @PathVariable String dashboardId,
			@RequestParam(value = "catagory", required = false) String catagory,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo) throws AINException, IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		UserDto user = gson.fromJson(xUserInfo, UserDto.class);
		user = new UserDto();
		RoleDto role = new RoleDto();
		role.setId(2068l);
		List<RoleDto> roles = new ArrayList<>();
		roles.add(role);
		user.setRoles(roles);
		logger.info("user {} ", xUserInfo);
		return ResponseGenerator.successResponse(
				metadataService.getDashboardConfiguration(profileName, dashboardId, catagory, user.getRoles()));
	}
	
	@GetMapping(value = PathRoutes.DashboardApi.GET_DASHBOARD)
	public String getDashboard(
			@RequestParam(value = "profileName", required = false) String profileName,
			@RequestParam(value = "dashboardId", required = false) String dashboardId,
			@RequestParam(value = "catagory", required = false) String catagory,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo) throws AINException, IOException {

		UserDto userInfo = createUserFromXUserInfo(xUserInfo);
		Dashboards dashboards = metadataService.getDashboard(profileName, dashboardId, catagory);
		DashboardsDto dashboardsDto = ModelDtoMapperUtil.returnDashboardData(dashboards, userInfo);
		JsonNode node = objectMapper.convertValue(dashboardsDto, JsonNode.class);
		ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
		return ResponseGenerator.successResponse(resultArray.add(node));
	}

	@GetMapping(value = PathRoutes.DashboardApi.GET_CHART_CONFIG)
	public String getChartConfiguration(
			@RequestParam(value = "visualizationCode", required = false) String visualizationCode,
			@RequestParam(value = "catagory", required = false) String catagory,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo) throws AINException, IOException {

		UserDto userInfo = createUserFromXUserInfo(xUserInfo);
		Visualizations visualizations = metadataService.getChartConfiguration(visualizationCode, catagory);
		VisualizationsDto visualizationsDto = ModelDtoMapperUtil.returnVisualizationData(visualizations, userInfo); 
		JsonNode node = objectMapper.convertValue(visualizationsDto, JsonNode.class);
		ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
		return ResponseGenerator.successResponse(resultArray.add(node));
	}

	@GetMapping(value = PathRoutes.DashboardApi.GET_REPORTS_CONFIG + "/{profileName}" + "/{dashboardId}")
	public String getReportsConfiguration(@PathVariable String profileName, @PathVariable String dashboardId,
			@RequestParam(value = "catagory", required = false) String catagory,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo) throws AINException, IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		UserDto user = gson.fromJson(xUserInfo, UserDto.class);
		ConfigDetail detail = new ConfigDetail();
		detail.getConfig();

		logger.info("user {} ", xUserInfo);
		return ResponseGenerator.successResponse(
				metadataService.getReportsConfiguration(profileName, dashboardId, catagory, user.getRoles()));
	}

	@GetMapping(value = PathRoutes.DashboardApi.GET_DASHBOARDS_FOR_PROFILE + "/{profileName}")
	public String getDashboardsForProfile(@PathVariable String profileName,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "X-Channel-Id", required = false) String channelId,
			@RequestHeader(value = "x-authenticated-userid", required = false) String fullUserId,
			@RequestHeader(value = "x-authenticated-user-token", required = false) String userToken)
			throws AINException, IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		UserDto user = gson.fromJson(xUserInfo, UserDto.class);
		user = new UserDto();
		RoleDto role = new RoleDto();
		role.setId(2068l);
		role.setName(TARENTO_ADMIN);
		RoleDto role2 = new RoleDto();
		role2.setId(2069l);
		role2.setName("PUBLIC");
		List<RoleDto> roles = new ArrayList<>();
		roles.add(role);
		roles.add(role2);
		user.setRoles(roles);
		logger.info("user {} ", xUserInfo);
		return ResponseGenerator.successResponse(metadataService.getDashboardsForProfile(profileName, user.getRoles()));
	}

	@GetMapping(value = PathRoutes.DashboardApi.GET_REPORTS_FOR_PROFILE + "/{profileName}")
	public String getReportsForProfile(@PathVariable String profileName,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo) throws AINException, IOException {
		UserDto user = new UserDto();
		RoleDto role = new RoleDto();
		role.setId(2068l);
		List<RoleDto> roles = new ArrayList<>();
		roles.add(role);
		user.setRoles(roles);
		return ResponseGenerator.successResponse(metadataService.getReportsForProfile(profileName, user.getRoles()));
	}

	@PostMapping(value = PathRoutes.DashboardApi.GET_CHART_V2 + "/{profileName}")
	public String getVisualizationChartV2(@PathVariable String profileName, @RequestBody RequestDto requestDto,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request)
			throws IOException {

		// Getting the request information only from the Full Request
		AggregateRequestDto requestInfo = requestDto.getAggregationRequestDto();
		String response = "";
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		UserDto user = gson.fromJson(xUserInfo, UserDto.class);
		user = new UserDto();
		RoleDto role = new RoleDto();
		role.setId(2068l);
		role.setName(TARENTO_ADMIN);
		List<RoleDto> roles = new ArrayList<>();
		roles.add(role);
		user.setRoles(roles);
		logger.info("user {} ", xUserInfo);
		try {
			if (requestDto.getAggregationRequestDto() == null) {
				logger.error("Please provide requested Visualization Details");
				throw new AINException(ErrorCode.ERR320, "Visualization Request is missing");
			}
			Object responseData = clientServiceFactory.get()
					.getAggregatedData(profileName, requestInfo, user.getRoles());
			response = ResponseGenerator.successResponse(responseData);
			// Commenting the User Request Data Push - This was earlier used for Analytics
			// of Users and the Dashboards that they were consuming.
		} catch (AINException e) {
			logger.error("error while executing api getVisualizationChartV2");
			response = ResponseGenerator.failureResponse(e.getErrorCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("error while executing api getVisualizationChartV2 {} ", e.getMessage());
		}
		return (response);
	}

	@PostMapping(value = PathRoutes.DashboardApi.POST_CHART)
	public String postVisualizationChart(@RequestBody VisualizationsDto visualizationsDto,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request) throws JsonProcessingException {
		UserDto userInfo = createUserFromXUserInfo(xUserInfo);
		JsonNode response = null;
		if(StringUtils.isBlank(visualizationsDto.getTitle())) { 
			return ResponseGenerator.failureResponse("Title is mandatory");
		}
		if(visualizationsDto.getData() != null && !visualizationsDto.getData().isEmpty()) { 
			if(visualizationsDto.getData().get(0).getInfo() != null) { 				
				if(StringUtils.isBlank(visualizationsDto.getData().get(0).getInfo().getChartType()))
					{return ResponseGenerator.failureResponse("Chart type is mandatory");}}
			if(visualizationsDto.getData().get(0).getAdditionalConfig() != null) { 
				if(StringUtils.isBlank(visualizationsDto.getData().get(0).getAdditionalConfig().getConfig()))
				{return ResponseGenerator.failureResponse("Configuration is mandatory");}}			
		}	
		Visualizations visualizations = ModelDtoMapperUtil.insertVisualizationData(visualizationsDto, userInfo);
		try {
			response = metadataService.postChart(visualizations);
			if(response != null && response.get("id") != null) { 
				visualizationsDto.setId(response.get("id").asLong());
				visualizationsDto.setTimestamp(response.get("timestamp").asLong());
				visualizationsDto.setCreatedBy(response.get("createdBy").asLong());
			}
		} catch (Exception e) {
			logger.error("error while executing api postVisualizationChart {} ", e.getMessage());
		}
		JsonNode node = objectMapper.convertValue(visualizationsDto, JsonNode.class);
		ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
		return ResponseGenerator.successResponse(resultArray.add(node));
	}

	@PostMapping(value = PathRoutes.DashboardApi.POST_DASHBOARD)
	public String postDashboardConfig(@RequestBody DashboardsDto dashboardsDto,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request) throws JsonProcessingException {
		UserDto userInfo = createUserFromXUserInfo(xUserInfo);
		JsonNode response = null;
		if(StringUtils.isBlank(dashboardsDto.getDashboardId())) { 
			return ResponseGenerator.failureResponse("Dashboard ID is mandatory");
		}
		Dashboards dashboard = ModelDtoMapperUtil.insertDashboardData(dashboardsDto, userInfo);		
		try {
			response = metadataService.postDashboard(dashboard);
//			metadataService.postRoleDashboard(createRoleDashboardObject(dashboard, userInfo));
			metadataService.postRoleDashboard(createRoleDashboardObject(dashboardsDto));
			if(response != null && response.get("id") != null) { 
				dashboardsDto.setId(response.get("id").asLong());
				dashboardsDto.setTimestamp(response.get("timestamp").asLong());
				dashboardsDto.setCreatedBy(response.get("createdBy").asLong());
			}
		} catch (Exception e) {
			logger.error("error while executing api postDashboardConfig {} ", e.getMessage());
		}
		JsonNode node = objectMapper.convertValue(dashboardsDto, JsonNode.class);
		ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
		return ResponseGenerator.successResponse(resultArray.add(node));
	}
	
	private RoleDashboard createRoleDashboardObject(DashboardsDto dashboardsDto) { 
		  RoleDashboard roleDashboard = new RoleDashboard();
		  List<Integer> roleIds = new ArrayList<>(); 
		  roleDashboard.setDashboardIdItem(dashboardsDto.getDashboardId());  
		//  List <RoleDto> accessList = (List<RoleDto>) objectMapper.convertValue(dashboardsDto.getData().get(0).getAccess(), RoleDto.class);
		  for(RoleDto roleDto : dashboardsDto.getData().get(0).getAccess()) { 
		   roleIds.add(Math.toIntExact(roleDto.getId()));
		  }
		  roleDashboard.setRoleIds(roleIds);
		  roleDashboard.setOrgId(0);
		  return roleDashboard;
		 }

	@PostMapping(value = PathRoutes.DashboardApi.GET_REPORT)
	public String getReport(@PathVariable String profileName, @RequestBody RequestDto requestDto,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request)
			throws IOException {
		UserDto user = new UserDto();
		logger.info("user {} ", xUserInfo);

		// Getting the request information only from the Full Request
		AggregateRequestDto requestInfo = requestDto.getAggregationRequestDto();
		Map<String, Object> headers = requestDto.getHeaders();
		String response = "";
		try {
			if (headers.isEmpty()) {
				logger.error("Please provide header details");
				throw new AINException(ErrorCode.ERR320, "header is missing");
			}
			if (headers.get("tenantId") == null) {
				logger.error("Please provide tenant ID details");
				throw new AINException(ErrorCode.ERR320, "tenant is missing");

			}
			if (requestDto.getAggregationRequestDto() == null) {
				logger.error("Please provide requested Visualization Details");
				throw new AINException(ErrorCode.ERR320, "Visualization Request is missing");
			}

			// To be removed once the development is complete
			if (StringUtils.isBlank(requestInfo.getModuleLevel())) {
				requestInfo.setModuleLevel(Constants.Modules.HOME_REVENUE);
			}
			Object responseData = clientServiceFactory.get()
					.getAggregatedData(profileName, requestInfo, user.getRoles());
			response = ResponseGenerator.successResponse(responseData);
		} catch (AINException e) {
			logger.error("error while executing api getReport");
			response = ResponseGenerator.failureResponse(e.getErrorCode(), e.getErrorMessage());
		} catch (Exception e) {
			logger.error("error while executing api getReport {} ", e.getMessage());
			// could be bad request or internal server error
			// response =
			// ResponseGenerator.failureResponse(HttpStatus.BAD_REQUEST.toString(),"Bad
			// request");
		}
		return response;
	}

	@PostMapping(value = PathRoutes.DashboardApi.POST_USER_ROLE)
	public String postUserRoleMapping(@RequestBody UserRole userRole,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request) {

		try {
			return ResponseGenerator.successResponse(metadataService.postUserRole(userRole));
		} catch (Exception e) {
			logger.error("error while executing api postUserRoleMapping {} ", e.getMessage());
		}
		return null;
	}

	@PostMapping(value = PathRoutes.DashboardApi.POST_ROLE_DASHBOARD)
	public JsonNode postRoleDashboardConfig(@RequestBody RoleDashboard roleDashboard,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request)
			throws IOException {

		JsonNode response = null;
		try {
			response = metadataService.postRoleDashboard(roleDashboard);
		} catch (Exception e) {
			logger.error("error while executing api postRoleDashboardConfig {} ", e.getMessage());
		}
		return response;
	}

	@PostMapping(value = PathRoutes.DashboardApi.UPDATE_ROLE_DASHBOARD)
	public Boolean updateRoleDashboardConfig(@RequestBody RoleDashboard roledashboard,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request) {
		Boolean response = null;
		try {
			response = metadataService.updateRoleDashboard(roledashboard);
		} catch (Exception e) {
			logger.error("error while executing api updateVisualizationConfig {} ", e.getMessage());
		}
		return response;
	}

	@PostMapping(value = PathRoutes.DashboardApi.POST_DATA_SOURCE)
	public String postDataSource(@RequestBody DataSourceDto dataSourceDTO,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request) throws JsonProcessingException {
		UserDto userInfo = createUserFromXUserInfo(xUserInfo);
		JsonNode response = null;
		DataSources dataSource = ModelDtoMapperUtil.insertDataSource(dataSourceDTO, userInfo);
		try {
			response = metadataService.postDataSource(dataSource);
			if(response != null && response.get("id") != null) { 
				dataSourceDTO.setId(response.get("id").asLong());
				dataSourceDTO.setTimestamp(response.get("timestamp").asLong());
				dataSourceDTO.setCreatedBy(response.get("createdBy").asLong());
			}
		} catch (Exception e) {
			logger.error("error while executing api post datasource config {} ", e.getMessage());
		}
		JsonNode node = objectMapper.convertValue(dataSourceDTO, JsonNode.class);
		ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
		return ResponseGenerator.successResponse(resultArray.add(node));
		}
	
	@GetMapping(value = PathRoutes.DashboardApi.GET_DATA_SOURCE)
	public String getDataSource(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "catagory", required = false) String catagory,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo) throws AINException, IOException {
		UserDto userInfo = createUserFromXUserInfo(xUserInfo);
		DataSources dataSources = metadataService.getDataSource(id, catagory);
		DataSourceDto dto = ModelDtoMapperUtil.returnDataSource(dataSources, userInfo); 
		JsonNode node = objectMapper.convertValue(dto, JsonNode.class);
		ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
		return ResponseGenerator.successResponse(resultArray.add(node));
	}
	
	@GetMapping(value = PathRoutes.DashboardApi.GET_ALL_DATA_SOURCES)
	public String getAllDataSources(@RequestHeader(value = "x-user-info", required = false) String xUserInfo) throws AINException, IOException {
		UserDto userInfo = createUserFromXUserInfo(xUserInfo);
		List<DataSources> dataSourcesList = metadataService.getAllDataSources(); 
		List<DataSourceDto> dataSourceDtoList = ModelDtoMapperUtil.returnDataSourceList(dataSourcesList, userInfo); 
		return ResponseGenerator
				.successResponse(dataSourceDtoList);
	}

	@PostMapping(value = PathRoutes.DashboardApi.POST_ROLE_ACTIONS)
	public String postRoleActions(@RequestBody RoleActions roleActions, 
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request)
			throws IOException {

		JsonNode response = null;
		try {
			response = metadataService.postRoleAction(roleActions);
		} catch (Exception e) {
			logger.error(String.format("error while executing api postRoleActions :  %s", e.getMessage()));
		}
		JsonNode node = objectMapper.convertValue(response, JsonNode.class);
		ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
		return ResponseGenerator.successResponse(resultArray.add(node));
//		return response;
	}

	@PostMapping(value = PathRoutes.DashboardApi.POST_ACTIONS)
	public String postActions(@RequestBody Actions actions, 
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request)
			throws IOException {

		JsonNode response = null;
		try {
			response = metadataService.postAction(actions);
		} catch (Exception e) {
			logger.error(String.format("error while executing api postActions :  %s", e.getMessage()));
		}
		JsonNode node = objectMapper.convertValue(response, JsonNode.class);
		ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
		return ResponseGenerator.successResponse(resultArray.add(node));
	}

	@PostMapping(value = PathRoutes.DashboardApi.UPDATE_ACTIONS)
	public String updateActions(@RequestBody Actions actions, 
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request)
			throws IOException {

		JsonNode response = null;
		try {
			response = metadataService.updateAction(actions);
		} catch (Exception e) {
			logger.error(String.format("error while executing api updateActions :  %s", e.getMessage()));
		}
		JsonNode node = objectMapper.convertValue(response, JsonNode.class);
		ArrayNode resultArray = JsonNodeFactory.instance.arrayNode();
		return ResponseGenerator.successResponse(resultArray.add(node));
	}

	@PostMapping(value = PathRoutes.DashboardApi.DELETE_ACTIONS + "/{id}")
	public String deleteActions(
			@RequestParam(value = "id", required = false) Long id,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo,
			@RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request)
			throws IOException {

		Boolean response = null;
		try {
			response = metadataService.deleteAction(id);
		} catch (Exception e) {
			logger.error(String.format("error while executing api deleteActions :  %s", e.getMessage()));
		}
		return ResponseGenerator.successResponse(response);
	}
	
	@GetMapping(value = PathRoutes.DashboardApi.GET_ACTIONS + "/{id}")
	public String getActions(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "catagory", required = false) String catagory,
			@RequestHeader(value = "x-user-info", required = false) String xUserInfo) throws AINException, IOException {
		
		return ResponseGenerator
				.successResponse(metadataService.getAction(id));
	}
	
	@PostMapping(value = PathRoutes.DashboardApi.POST_TEST_CONNECTION)
	  public String postTestConnection(@RequestBody TestConnectionDto testConnectionDto,
	      @RequestHeader(value = "x-user-info", required = false) String xUserInfo,
	      @RequestHeader(value = "Authorization", required = false) String authorization, ServletWebRequest request) {

	    String response = "";
	    
	    TestConnection testConnection = ModelDtoMapperUtil.testConnection(testConnectionDto);
	    try {
	      response = metadataService.testConnection(testConnection);
	    } catch (Exception e) {
	      logger.error("error while executing api post test connection {} ", e.getMessage());
	    }
	    return response;
	  }
	
	private UserDto createUserFromXUserInfo(String xUserInfo) { 
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		UserDto user = gson.fromJson(xUserInfo, UserDto.class);
		return user; 
	}
	
	@GetMapping(value = PathRoutes.DashboardApi.GET_ALL_VISUALIZATIONS)
	public String getAllVisualizations(@RequestHeader(value = "x-user-info", required = false) String xUserInfo) throws AINException, IOException {
		UserDto userInfo = createUserFromXUserInfo(xUserInfo);
		List<Visualizations> visualizationsList = metadataService.getAllVisualizations(); 
		List<VisualizationsDto> visualizationsDtoList = ModelDtoMapperUtil.returnVisualizationDataList(visualizationsList, userInfo);
		return ResponseGenerator
				.successResponse(visualizationsDtoList);
	}
	
	@GetMapping(value = PathRoutes.DashboardApi.GET_ALL_DASHBOARDS)
	public String getAllDashboards(@RequestHeader(value = "x-user-info", required = false) String xUserInfo) throws AINException, IOException {
		UserDto userInfo = createUserFromXUserInfo(xUserInfo);
		List<Dashboards> dashboardsList = metadataService.getAllDashboards();
		List<DashboardsDto> dashboardDtoList = ModelDtoMapperUtil.returnDashboardsList(dashboardsList, userInfo); 
		return ResponseGenerator
				.successResponse(dashboardDtoList);
	}

}

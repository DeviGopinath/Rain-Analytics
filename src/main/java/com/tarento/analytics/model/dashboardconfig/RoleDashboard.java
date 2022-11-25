package com.tarento.analytics.model.dashboardconfig;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tarento.analytics.dto.UserDto;
 

public class RoleDashboard {
 
 @JsonProperty("roleId")
 private Integer roleId;
 @JsonProperty("roleIds")
 private List<Integer> roleIds;
 @JsonProperty("dashboardId")
 private  List<String> dashboardId;
 @JsonProperty("dashboardIdItem")
 private String dashboardIdItem;
 @JsonProperty("orgId")
 private Integer orgId;
 @JsonProperty("newDashboardId")
 private  List<String> newDashboardId;
 
 public Integer getRoleId() {
  return roleId;
 }
 public void setRoleId(Integer roleId) {
  this.roleId = roleId;
 }
 public List<Integer> getRoleIds() {
  return roleIds;
 }
 public void setRoleIds(List<Integer> roleIds) {
  this.roleIds = roleIds;
 }
 public List<String> getDashboardId() {
  return dashboardId;
 }
 public void setDashboardId(List<String> dashboardId) {
  this.dashboardId = dashboardId;
 }
 public String getDashboardIdItem() {
  return dashboardIdItem;
 }
 public void setDashboardIdItem(String dashboardIdItem) {
  this.dashboardIdItem = dashboardIdItem;
 }
 public Integer getOrgId() {
  return orgId;
 }
 public void setOrgId(Integer orgId) {
  this.orgId = orgId;
 }
 public List<String> getNewDashboardId() {
  return newDashboardId;
 }
 public void setNewDashboardId(List<String> newDashboardId) {
  this.newDashboardId = newDashboardId;
 }

 
 

 
}
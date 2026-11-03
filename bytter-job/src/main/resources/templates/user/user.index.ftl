<!DOCTYPE html>
<html>
<head>
  	<#import "../common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
    <title>${I18n.admin_name}</title>
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["xxljob_adminlte_settings"]?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "user" />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>${I18n.user_manage}</h1>
		</section>
		
		<!-- Main content -->
	    <section class="content">
	    
	    	<div class="row">
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">${I18n.user_role}</span>
                        <select class="form-control" id="role" >
                            <option value="-1" >${I18n.system_all}</option>
                            <option value="1" >${I18n.user_role_admin}</option>
                            <option value="0" >${I18n.user_role_normal}</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">${I18n.user_username}</span>
                        <input type="text" class="form-control" id="username" autocomplete="on" >
                    </div>
                </div>
	            <div class="col-xs-1">
	            	<button class="btn btn-block btn-info" id="searchBtn">${I18n.system_search}</button>
	            </div>
	            <div class="col-xs-2">
	            	<button class="btn btn-block btn-success add" type="button">${I18n.user_add}</button>
	            </div>
          	</div>
	    	
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-body" >
			              	<table id="user_list" class="table table-bordered table-striped" width="100%" >
				                <thead>
					            	<tr>
                                        <th name="id" >ID</th>
                                        <th name="username" >${I18n.user_username}</th>
					                  	<th name="password" >${I18n.user_password}</th>
                                        <th name="role" >${I18n.user_role}</th>
					                  	<th name="permission" >${I18n.user_permission}</th>
					                  	<th>${I18n.system_opt}</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
				                <tfoot></tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
	    </section>
	</div>
	
	<!-- footer -->
	<@netCommon.commonFooter />
</div>

<!-- 新增.模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >${I18n.user_add}</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_username}<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="username" placeholder="${I18n.system_please_input}${I18n.user_username}" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_password}<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="password" placeholder="${I18n.system_please_input}${I18n.user_password}" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_role}<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="role" value="0" checked />${I18n.user_role_normal}
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="role" value="1" />${I18n.user_role_admin}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_permission}<font color="black">*</font></label>
                        <div class="col-sm-10">
							<#if groupList?exists && groupList?size gt 0>
								<#list groupList as item>
                                    <input type="checkbox" name="permission" value="${item.id}" />${item.title}(${item.appname})<br>
								</#list>
							</#if>
                        </div>
                    </div>

                    <hr>
					<div class="form-group">
						<div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
						</div>
					</div>

				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog"  aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
            	<h4 class="modal-title" >${I18n.user_update}</h4>
         	</div>
         	<div class="modal-body">
				<form class="form-horizontal form" role="form" >
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_username}<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="username" placeholder="${I18n.system_please_input}${I18n.user_username}" maxlength="20" readonly ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_password}<font color="red">*</font></label>
                        <div class="col-sm-8"><input type="text" class="form-control" name="password" placeholder="${I18n.user_password_update_placeholder}" maxlength="20" ></div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_role}<font color="red">*</font></label>
                        <div class="col-sm-10">
                            <input type="radio" name="role" value="0" />${I18n.user_role_normal}
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="radio" name="role" value="1" />${I18n.user_role_admin}
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="lastname" class="col-sm-2 control-label">${I18n.user_permission}<font color="black">*</font></label>
                        <div class="col-sm-10">
						<#if groupList?exists && groupList?size gt 0>
							<#list groupList as item>
                                <input type="checkbox" name="permission" value="${item.id}" />${item.title}(${item.appname})<br>
							</#list>
						</#if>
                        </div>
                    </div>

					<hr>
					<div class="form-group">
                        <div class="col-sm-offset-3 col-sm-6">
							<button type="submit" class="btn btn-primary"  >${I18n.system_save}</button>
							<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_cancel}</button>
                            <input type="hidden" name="id" >
						</div>
					</div>

				</form>
         	</div>
		</div>
	</div>
</div>

<!-- 任务权限配置模态框 -->
<div class="modal fade" id="jobPermissionModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">${I18n.job_permission_config}</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="col-md-3">
						<div class="form-group">
							<label>${I18n.job_group_filter}</label>
							<select class="form-control" id="permissionGroupFilter">
								<option value="-1">${I18n.system_all}</option>
								<#list groupList as group>
									<option value="${group.id}">${group.title}</option>
								</#list>
							</select>
						</div>
						<div class="form-group">
							<label>${I18n.job_search}</label>
							<input type="text" class="form-control" id="permissionJobSearch"
								   placeholder="${I18n.job_name_placeholder}">
						</div>
					</div>
					<div class="col-md-9">
						<div class="box box-solid">
							<div class="box-header">
								<h3 class="box-title">${I18n.job_permission_list}</h3>
							</div>
							<!-- 分页表格 -->
							<div class="box-body" style="height: 400px; overflow: hidden;">
								<table class="table table-bordered table-hover display" id="jobPermissionTable" style="width:100%">
									<thead>
									<tr>
										<th width="40%">${I18n.jobinfo_field_jobdesc}</th>
										<th width="30%">${I18n.jobinfo_field_jobgroup}</th>
										<th width="30%">${I18n.job_permission}</th>
									</tr>
									</thead>
									<tbody>
									<!-- 数据由DataTables动态加载 -->
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			 <div class="modal-footer">
			 	<button type="button" class="btn btn-success pull-left" id="saveAllReadOnly">${I18n.system_batch_rd}</button>
			 	<button type="button" class="btn btn-info pull-left" id="saveAllReadWrite">${I18n.system_batch_rw}</button>
			 	<button type="button" class="btn btn-warning pull-left" id="saveAllNoAccess">${I18n.system_batch_na}</button>
			 	<button type="button" class="btn btn-primary" id="saveJobPermissions">${I18n.system_save}</button>
			 	<button type="button" class="btn btn-default" data-dismiss="modal">${I18n.system_close}</button>
			 	<input type="hidden" id="permissionUserId">
			 </div>
		</div>
	</div>
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/js/user.index.1.js"></script>
</body>
</html>

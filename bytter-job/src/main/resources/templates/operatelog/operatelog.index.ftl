<!DOCTYPE html>
<html>
<head>
    <#import "../common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <!-- DataTables -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
    <!-- daterangepicker -->
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.css">
    <title>${I18n.admin_name} - ${I18n.operatelog_name}</title>
    <style>
        .log-type-badge {
            padding: 3px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 600;
        }
        .log-type-0 { background-color: #f0f0f0; color: #333; } /* 未知 */
        .log-type-1 { background-color: #d9edf7; color: #31708f; } /* 登录 */
        .log-type-2 { background-color: #dff0d8; color: #3c763d; } /* 任务操作 */
        .log-type-3 { background-color: #fcf8e3; color: #8a6d3b; } /* 系统配置 */
    </style>
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && cookieMap["xxljob_adminlte_settings"]?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
    <!-- header -->
    <@netCommon.commonHeader />
    <!-- left -->
    <@netCommon.commonLeft "operatelog" />

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>${I18n.operatelog_name}</h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-xs-3">
                    <div class="input-group">
                        <span class="input-group-addon">${I18n.operatelog_operateType}</span>
                        <select class="form-control" id="operateType">
                            <option value="">${I18n.system_all}</option>
                            <option value="${I18n.operateType_START}">${I18n.operateType_START}</option>
                            <option value="${I18n.operateType_STOP}">${I18n.operateType_STOP}</option>
                            <option value="${I18n.operateType_ADD}">${I18n.operateType_ADD}</option>
                            <option value="${I18n.operateType_UPDATE}">${I18n.operateType_UPDATE}</option>
                            <option value="${I18n.operateType_DELETE}">${I18n.operateType_DELETE}</option>
                            <option value="${I18n.operateType_TRIGGER}">${I18n.operateType_TRIGGER}</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <input type="text" class="form-control" id="username" placeholder="${I18n.system_please_input}${I18n.operatelog_username}">
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="input-group">
                        <input type="text" class="form-control" id="jobDesc" placeholder="${I18n.system_please_input}${I18n.operatelog_jobDesc}">
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="input-group">
                		<span class="input-group-addon">
	                  		${I18n.operatelog_operateTime}
	                	</span>
                        <input type="text" class="form-control" id="filterTime" readonly >
                    </div>
                </div>
                <div class="col-xs-1">
                    <button class="btn btn-block btn-info" id="searchBtn">${I18n.system_search}</button>
                </div>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-body">
                            <table id="operatelog_list" class="table table-bordered table-striped display" width="100%">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>${I18n.operatelog_operateTime}</th>
                                    <th>${I18n.operatelog_username}</th>
                                    <th>${I18n.operatelog_operateType}</th>
                                    <th>${I18n.operatelog_jobDesc}</th>
                                    <th>${I18n.operatelog_ip}</th>
                                    <th>${I18n.operatelog_description}</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
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

<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<!-- DatePicker -->
<script src="${request.contextPath}/static/adminlte/bower_components/moment/moment.min.js"></script>
<script src="${request.contextPath}/static/adminlte/bower_components/bootstrap-daterangepicker/daterangepicker.js"></script>
<script src="${request.contextPath}/static/js/operatelog.index.1.js"></script>
</body>
</html>
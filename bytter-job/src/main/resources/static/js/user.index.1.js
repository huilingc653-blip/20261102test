$(function() {

	// init date tables
	var userListTable = $("#user_list").dataTable({
		"deferRender": true,
		"processing" : true, 
	    "serverSide": true,
		"ajax": {
			url: base_url + "/user/pageList",
			type:"post",
	        data : function ( d ) {
	        	var obj = {};
                obj.username = $('#username').val();
                obj.role = $('#role').val();
	        	obj.start = d.start;
	        	obj.length = d.length;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    //"scrollX": true,	// scroll x，close self-adaption
	    "columns": [
	                {
	                	"data": 'id',
						"visible" : false,
						"width":'10%'
					},
	                {
	                	"data": 'username',
						"visible" : true,
						"width":'20%'
					},
	                {
	                	"data": 'password',
						"visible" : false,
                        "width":'20%',
                        "render": function ( data, type, row ) {
                            return '*********';
                        }
					},
					{
						"data": 'role',
						"visible" : true,
						"width":'10%',
                        "render": function ( data, type, row ) {
                            if (data == 1) {
                                return I18n.user_role_admin
                            } else {
                                return I18n.user_role_normal
                            }
                        }
					},
	                {
	                	"data": 'permission',
						"width":'10%',
	                	"visible" : false
	                },
	                {
						"data": I18n.system_opt ,
						"width":'20%',
	                	"render": function ( data, type, row ) {
	                		return function(){
								// html
                                tableData['key'+row.id] = row;
								var html = '<p id="'+ row.id +'" >'+
									'<button class="btn btn-warning btn-xs update" type="button">'+ I18n.system_opt_edit +'</button>  '+
									'<button class="btn btn-danger btn-xs delete" type="button">'+ I18n.system_opt_del +'</button>  '+
									'<button class="btn btn-info btn-xs permission" type="button">'+ I18n.job_permission +'</button>  '+
									'</p>';

	                			return html;
							};
	                	}
	                }
	            ],
		"language" : {
			"sProcessing" : I18n.dataTable_sProcessing ,
			"sLengthMenu" : I18n.dataTable_sLengthMenu ,
			"sZeroRecords" : I18n.dataTable_sZeroRecords ,
			"sInfo" : I18n.dataTable_sInfo ,
			"sInfoEmpty" : I18n.dataTable_sInfoEmpty ,
			"sInfoFiltered" : I18n.dataTable_sInfoFiltered ,
			"sInfoPostFix" : "",
			"sSearch" : I18n.dataTable_sSearch ,
			"sUrl" : "",
			"sEmptyTable" : I18n.dataTable_sEmptyTable ,
			"sLoadingRecords" : I18n.dataTable_sLoadingRecords ,
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : I18n.dataTable_sFirst ,
				"sPrevious" : I18n.dataTable_sPrevious ,
				"sNext" : I18n.dataTable_sNext ,
				"sLast" : I18n.dataTable_sLast
			},
			"oAria" : {
				"sSortAscending" : I18n.dataTable_sSortAscending ,
				"sSortDescending" : I18n.dataTable_sSortDescending
			}
		}
	});

    // table data
    var tableData = {};

	// search btn
	$('#searchBtn').on('click', function(){
        userListTable.fnDraw();
	});
	
	// job operate
	$("#user_list").on('click', '.delete',function() {
		var id = $(this).parent('p').attr("id");

		layer.confirm( I18n.system_ok + I18n.system_opt_del + '?', {
			icon: 3,
			title: I18n.system_tips ,
            btn: [ I18n.system_ok, I18n.system_cancel ]
		}, function(index){
			layer.close(index);

			$.ajax({
				type : 'POST',
				url : base_url + "/user/remove",
				data : {
					"id" : id
				},
				dataType : "json",
				success : function(data){
					if (data.code == 200) {
                        layer.msg( I18n.system_success );
						userListTable.fnDraw(false);
					} else {
                        layer.msg( data.msg || I18n.system_opt_del + I18n.system_fail );
					}
				}
			});
		});
	});

	// add role
    $("#addModal .form input[name=role]").change(function () {
		var role = $(this).val();
		if (role == 1) {
            $("#addModal .form input[name=permission]").parents('.form-group').hide();
		} else {
            $("#addModal .form input[name=permission]").parents('.form-group').show();
		}
        $("#addModal .form input[name='permission']").prop("checked",false);
    });

    jQuery.validator.addMethod("myValid01", function(value, element) {
        var length = value.length;
        var valid = /^[a-z][a-z0-9]*$/;
        return this.optional(element) || valid.test(value);
    }, I18n.user_username_valid );

	// add
	$(".add").click(function(){
		$('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var addModalValidate = $("#addModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {
            username : {
				required : true,
                rangelength:[4, 20],
                myValid01: true
			},
            password : {
                required : true,
                rangelength:[4, 20]
            }
        }, 
        messages : {
            username : {
            	required : I18n.system_please_input + I18n.user_username,
                rangelength: I18n.system_lengh_limit + "[4-20]"
            },
            password : {
                required : I18n.system_please_input + I18n.user_password,
                rangelength: I18n.system_lengh_limit + "[4-20]"
            }
        },
		highlight : function(element) {  
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {

            var permissionArr = [];
            $("#addModal .form input[name=permission]:checked").each(function(){
                permissionArr.push($(this).val());
            });

			var paramData = {
				"username": $("#addModal .form input[name=username]").val(),
                "password": $("#addModal .form input[name=password]").val(),
                "role": $("#addModal .form input[name=role]:checked").val(),
                "permission": permissionArr.join(',')
			};

        	$.post(base_url + "/user/add", paramData, function(data, status) {
    			if (data.code == "200") {
					$('#addModal').modal('hide');

                    layer.msg( I18n.system_add_suc );
                    userListTable.fnDraw();
    			} else {
					layer.open({
						title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
						content: (data.msg || I18n.system_add_fail),
						icon: '2'
					});
    			}
    		});
		}
	});
	$("#addModal").on('hide.bs.modal', function () {
		$("#addModal .form")[0].reset();
		addModalValidate.resetForm();
		$("#addModal .form .form-group").removeClass("has-error");
		$(".remote_panel").show();	// remote

        $("#addModal .form input[name=permission]").parents('.form-group').show();
	});

    // update role
    $("#updateModal .form input[name=role]").change(function () {
        var role = $(this).val();
        if (role == 1) {
            $("#updateModal .form input[name=permission]").parents('.form-group').hide();
        } else {
            $("#updateModal .form input[name=permission]").parents('.form-group').show();
        }
        $("#updateModal .form input[name='permission']").prop("checked",false);
    });

	// update
	$("#user_list").on('click', '.update',function() {

        var id = $(this).parent('p').attr("id");
        var row = tableData['key'+id];

		// base data
		$("#updateModal .form input[name='id']").val( row.id );
		$("#updateModal .form input[name='username']").val( row.username );
		$("#updateModal .form input[name='password']").val( '' );
		$("#updateModal .form input[name='role'][value='"+ row.role +"']").click();
        var permissionArr = [];
        if (row.permission) {
            permissionArr = row.permission.split(",");
		}
        $("#updateModal .form input[name='permission']").each(function () {
            if($.inArray($(this).val(), permissionArr) > -1) {
                $(this).prop("checked",true);
            } else {
                $(this).prop("checked",false);
            }
        });

		// show
		$('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
	});
	var updateModalValidate = $("#updateModal .form").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,
		highlight : function(element) {
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {

            var permissionArr =[];
            $("#updateModal .form input[name=permission]:checked").each(function(){
                permissionArr.push($(this).val());
            });

            var paramData = {
                "id": $("#updateModal .form input[name=id]").val(),
                "username": $("#updateModal .form input[name=username]").val(),
                "password": $("#updateModal .form input[name=password]").val(),
                "role": $("#updateModal .form input[name=role]:checked").val(),
                "permission": permissionArr.join(',')
            };

            $.post(base_url + "/user/update", paramData, function(data, status) {
                if (data.code == "200") {
                    $('#updateModal').modal('hide');

                    layer.msg( I18n.system_update_suc );
                    userListTable.fnDraw();
                } else {
                    layer.open({
                        title: I18n.system_tips ,
                        btn: [ I18n.system_ok ],
                        content: (data.msg || I18n.system_update_fail),
                        icon: '2'
                    });
                }
            });
		}
	});
	$("#updateModal").on('hide.bs.modal', function () {
        $("#updateModal .form")[0].reset();
        updateModalValidate.resetForm();
        $("#updateModal .form .form-group").removeClass("has-error");
        $(".remote_panel").show();	// remote

        $("#updateModal .form input[name=permission]").parents('.form-group').show();
	});

	// 任务权限配置表格
	var jobPermissionTable;
	function initJobPermissionTable(userId) {
		// 检查表格是否已初始化
		if ($.fn.DataTable.isDataTable('#jobPermissionTable')) {
			// 如果已存在，先销毁表格但保留DOM结构
			jobPermissionTable.destroy(false); // false 参数保留DOM元素
		}
		jobPermissionTable = $('#jobPermissionTable').DataTable({
			"retrieve": true, // 可复用
			"deferRender": true,
			"processing" : true,
			"serverSide": true,
			"scrollY": "270px",  // 固定表格高度
			"scrollCollapse": true,  // 允许内容折叠
			"paging": true, // 开启分页功能
			"pageLength": 10, // 默认每页显示10条
			"lengthMenu": [5, 10, 25, 50, 100], // 分页大小选择
			"searching": false,
			"ordering": false,
			"ajax": {
				"url": base_url + "/jobpermission/pageList",
				"type": "POST",
				"data": function (d) {
					d.userId = userId;
					d.jobGroup = $("#permissionGroupFilter").val();
					d.jobDesc = $("#permissionJobSearch").val();
				}
			},
			"columns": [
				{"data": "jobDesc", "width": "40%"},
				{"data": "jobGroupTitle", "width": "30%"},
				{
					"data": "id",
					"width": "30%",
					"render": function (data, type, row) {
						var select = '<select class="form-control permission-select" data-job-id="' + row.id + '">';
						select += '<option value="0">' + I18n.permission_no_access + '</option>';
						select += '<option value="1">' + I18n.permission_read_only + '</option>';
						select += '<option value="2">' + I18n.permission_read_write + '</option>';
						select += '</select>';
						return select;
					}
				}
			],
			"language": {
				"sProcessing": I18n.dataTable_sProcessing,
				"sLengthMenu": I18n.dataTable_sLengthMenu,
				"sZeroRecords": I18n.dataTable_sZeroRecords,
				"sInfo": I18n.dataTable_sInfo,
				"sInfoEmpty": I18n.dataTable_sInfoEmpty,
				"sInfoFiltered": I18n.dataTable_sInfoFiltered,
				"sInfoPostFix": "",
				"sSearch": I18n.dataTable_sSearch,
				"sUrl": "",
				"sEmptyTable": I18n.dataTable_sEmptyTable,
				"sLoadingRecords": I18n.dataTable_sLoadingRecords,
				"sInfoThousands": ",",
				"oPaginate": {
					"sFirst": I18n.dataTable_sFirst,
					"sPrevious": I18n.dataTable_sPrevious,
					"sNext": I18n.dataTable_sNext,
					"sLast": I18n.dataTable_sLast
				}
			},
			"drawCallback": function(settings) {
				// 设置权限值
				var api = this.api();
				var rows = api.rows({page:'current'}).nodes();
				var data = api.rows({page:'current'}).data();

				for (var i=0; i<data.length; i++) {
					var row = data[i];
					var select = $(rows[i]).find('.permission-select');
					select.val(row.permissionType);
				}
			}
		});
	}

	// 任务权限按钮
	$("#user_list").on('click', '.permission', function() {
		var userId = $(this).parent('p').attr("id");
		$("#permissionUserId").val(userId);

		// 初始化分页表格
		initJobPermissionTable(userId);
		// 显示模态框
		$('#jobPermissionModal').modal({backdrop: false, keyboard: false}).modal('show');
	});

	// 添加过滤事件
	$("#permissionGroupFilter, #permissionJobSearch").on('change keyup', function() {
		if (jobPermissionTable) {
			jobPermissionTable.ajax.reload();
		}
	});

	// 保存任务权限
	$("#saveJobPermissions").click(function() {
		var userId = $("#permissionUserId").val();
		var permissions = [];

		$("#jobPermissionTable .permission-select").each(function() {
			var jobId = $(this).data('job-id');
			var permissionType = $(this).val();

			permissions.push({
				userId: userId,
				jobId: jobId,
				permissionType: permissionType
			});
		});

		$.ajax({
			type: 'POST',
			url: base_url + "/jobpermission/save",
			contentType: "application/json",
			data: JSON.stringify(permissions),
			dataType: "json",
			success: function(data){
				if (data.code == 200) {
					layer.msg(I18n.system_save_success);
					// 不关闭模态框，继续配置
					// 刷新当前页
					if (jobPermissionTable) {
						jobPermissionTable.ajax.reload(null, false);
					}
				} else {
					layer.msg(data.msg || I18n.system_save_fail);
				}
			}
		});
	});
	$("#saveAllReadOnly").click(function() {
		batchSave(1);
	});
	$("#saveAllReadWrite").click(function() {
		batchSave(2);
	});
	$("#saveAllNoAccess").click(function() {
		batchSave(0);
	});

	function batchSave(permissionType){
		var userId = $("#permissionUserId").val();
		var jobGroup = $("#permissionGroupFilter").val();
		var jobDesc = $("#permissionJobSearch").val();
		// console.log(permissionType, userId, jobGroup, jobDesc)
		$.ajax({
			type: 'POST',
			url: base_url + "/jobpermission/batchSave",
			data: {userId: userId, permissionType: permissionType, jobGroup: jobGroup, jobDesc: jobDesc},
			dataType: "json",
			success: function(data){
				if (data.code == 200) {
					layer.msg(I18n.system_save_success);
					// 不关闭模态框，继续配置
					// 刷新当前页
					if (jobPermissionTable) {
						jobPermissionTable.ajax.reload(null, false);
					}
				} else {
					layer.msg(data.msg || I18n.system_save_fail);
				}
			}
		});
	}

});

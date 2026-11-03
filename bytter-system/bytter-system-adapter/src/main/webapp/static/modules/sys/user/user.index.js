bt.use(['jquery', 'corp', 'tableGrid', 'dicts','btImport'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;
    bt.dicts.init();
    var $ = bt.jquery;

    if(bt.isResource('sys:user:save')){
    var addFunc = function () {
        layer.open({
            type: 2,
            area: ['830px', '650px'],
            content: basePath + "/sys/user/view/",
            end: function (flag) {
                if(flag){
                    table.trigger("reloadGrid");
                }
            }
        });
    }
    }
    if(bt.isResource('sys:user:update')){
    var editFunc = function (rowData) {
        var id = rowData.id ? rowData.id : '';
        layer.open({
            type: 2,
            area: ['830px', '650px'],
            content: basePath + "/sys/user/view/?id=" + rowData.id,
            end: function (flag) {
                if(flag){
                    table.trigger("reloadGrid");
                }
            }
        });
    }
    }
    if(bt.isResource('sys:user:delete')){
    var delFunc = function () {
        var rowData = table.getSelectRowsData();
        var ids = [];
        var names = "";
        var flag = false;
        for (var i = 0; i < rowData.length; i++) {
            if (rowData[i].loginName === "admin") {
                flag = true;
                break;
            }
            ids.push(rowData[i].id);
            names += rowData[i].username;
            if (i != rowData.length - 1) {
                names += ",";
            }
        }
        if (flag) {
            layer.alert(bt.lang("sys.user.message.administrator.cannot.delete"), {icon: 2});
            return;
        }
        layer.confirm(bt.lang('confirm.delete.this.record'), function () {
            $.post(basePath + "/sys/user/delete", {ids: ids}, function (result) {
                table.trigger("reloadGrid");
                layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        });
    }
    }
    var table = bt.tableGrid.init({
        elem: "#userTable",
        url: basePath + "/sys/user/tableGrid.json",
        colModel: [
            {name: "loginName", label: bt.lang('sys.user.loginName.show')},
            {name: "username", label: bt.lang('sys.user.username.show')},
            {
                name: "corp",
                label: bt.lang('sys.user.corpContain.show'),
                width: 200,
                formatter: function (cellvalue, options, rowObject) {
                    if (!rowObject.corpCode) {
                        return "";
                    }
                    return "(" + rowObject.corpCode + ")" + rowObject.corpName;
                }
            },
            {
                name: "status",
                label: bt.lang('table.sysCorp.status'),
                width: 80,
                align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return rowObject.statusLang;
                }
            },
            {
                name: "isAdmin",
                label: bt.lang('sys.user.isAdmin'),
                width: 80,
                align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return rowObject.isAdmin ? bt.lang('common.sc_yn.yes') : bt.lang('common.sc_yn.no');
                }
            },
            {name: "createTime", width: 150, align: "center",label: bt.lang('common.table.createTime')},
            {name: "updateTime", width: 150, align: "center",label: bt.lang('common.table.updateTime')},
            {
                label: bt.lang('common.table.thead.operation'),
                sortable: false,
                width: 250,align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    var html = '';
                    if(bt.isResource('sys:user:roles:assign')){
                        html = '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="assign-role">' + bt.lang('sys.user.assign.role') + '</a>';
                    }
                    // html += '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="copy-resource-user">'+bt.lang('sys.dual.CopyRolesComponent')+'</a>';

                    if(bt.isResource('sys:user:copy:permissions')){
                        if(html){html += '<span class="text-explode">|</span>';}
                        html += '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="copy-resource-user">' + bt.lang('table.user.copyPermissions') + '</a>';
                    }
                    if(bt.isResource('sys:user:recover') && rowObject.status === 0){
                        if(html){html += '<span class="text-explode">|</span>';}
                        html += '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="user-recover">' + bt.lang('sys.user.recover') + '</a>';
                    }
                    console.log(bt.isResource('sys:user:resource:assign'))
                    if(bt.isResource('sys:user:resource:assign')){
                        if(html){html += '<span class="text-explode">|</span>';}
                        html += '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="user-special">' + bt.lang('sys.dual.AssignResourceComponent') + '</a>';
                    }
                    if ($("#encrypt").val() != 0) {
                        if (rowObject.userCadn == null || rowObject.userCadn == "") {
                            if(bt.isResource('sys:user:caBinding')){
                                if(html){html += '<span class="text-explode">|</span>';}
                                html += '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="user-caBinding">' + bt.lang('sys.login.button.caBinding') + '</a>';
                            }
                        } else {
                            if(bt.isResource('sys:user:caDel')){
                                if(html){html += '<span class="text-explode">|</span>';}
                                html += '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="user-caDel">' + bt.lang('sys.login.bttion.caDelete') + '</a>';
                            }
                        }
                    }
                    /*if(html){html += '<span class="text-explode">|</span>';}
                    html += '<a href="javascript:void(0);" data-row="' + rowObject.id + '" class="user-corp-role">'+bt.lang('sys.dual.userCorpRoleComponent')+'</a>';*/
                    return html;
                }
            }
        ],
        addFunc: addFunc,
        editFunc: editFunc,
        delFunc: delFunc,
    });

    if(bt.isResource('sys:user:lock')) {
        table.customButton({
            title: bt.lang('sys.user.button.lock'),
            buttonicon: "fa-lock",
            onClickButton: function () {
                var rowData = table.getSelectRowsData();
                var ids = [];
                var names = "";
                for (var i = 0; i < rowData.length; i++) {
                    if (rowData[i].status != 1) {
                        layer.alert(rowData[i].loginName + "(" + rowData[i].username + ")" + bt.lang('sys.user.isLock.fail'), {icon: 2});
                        return;
                    }
                    ids.push(rowData[i].id);
                    names += rowData[i].username;
                    if (i != rowData.length - 1) {
                        names += ",";
                    }

                }
                if (ids.length == 0) {
                    layer.alert(bt.lang('sys.user.please.selectUser'), {icon: 2});
                    return;
                }
                layer.confirm(bt.lang('sys.user.isLock.listUser') + names, function () {
                    $.post(basePath + "/sys/user/lock", {ids: ids}, function (result) {
                        table.trigger("reloadGrid");
                        layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, "JSON");
                });
            }
        });
    }

    if(bt.isResource('sys:user:unlock')) {
        table.customButton({
            title: bt.lang('sys.user.button.unlock'),
            buttonicon: "fa-unlock",
            onClickButton: function () {
                var rowData = table.getSelectRowsData();
                var ids = [];
                var names = "";
                for (var i = 0; i < rowData.length; i++) {
                    if (rowData[i].status != 3) {
                        layer.alert(rowData[i].loginName + "(" + rowData[i].username + ")" + bt.lang('sys.user.isUnLock.fail'), {icon: 2});
                        return;
                    }
                    ids.push(rowData[i].id);
                    names += rowData[i].username;
                    if (i != rowData.length - 1) {
                        names += ",";
                    }
                }
                if (ids.length == 0) {
                    layer.alert(bt.lang('sys.user.please.selectUser'), {icon: 2});
                    return;
                }
                layer.confirm(bt.lang('sys.user.isUnLock.listUser') + names, function () {
                    $.post(basePath + "/sys/user/unLock", {ids: ids}, function (result) {
                        table.trigger("reloadGrid");
                        layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, "JSON");
                });
            }
        });
    }

    if(bt.isResource('sys:user:password:reset')) {
        table.customButton({
            title: bt.lang('sys.user.button.resetPassword'),
            buttonicon: "fa-eraser",
            onClickButton: function () {
                var rowData = table.getSelectRowsData();
                var ids = [];
                var names = "";
                for (var i = 0; i < rowData.length; i++) {
                    ids.push(rowData[i].id);
                    names += rowData[i].username;
                    if (i != rowData.length - 1) {
                        names += ",";
                    }
                }
                if (ids.length == 0) {
                    layer.alert(bt.lang('sys.user.please.selectUser'), {icon: 2});
                    return;
                }
                layer.confirm(bt.lang('sys.user.isResetPassword.listUser') + names, function () {
                    $.post(basePath + "/sys/user/resetPassword", {ids: ids}, function (result) {
                        layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, "JSON");
                });
            }
        });
    }

	table.on('click', '.user-corp-role', function (event) {
        var data = table.grid("getOriginalData",$(this).data("row"));
        layer.open({
            type: 2,
            area: ['1024px', '650px'],
            maxmin: false,
            title: bt.lang('sys.dual.AssignResourceComponent')+" - " + data.username,
            content: basePath + "/sys/user/userCorpRole?userId=" + data.id +"&corpId=" + data.corpId
        });

    });
    
    table.on('click', '.user-special', function (event) {
        var data = table.grid("getOriginalData", $(this).data("row"));
        $.get(basePath + "/sys/user/checkUserHasRoles", {userId: data.id}, function (result) {
            if (result.success) {
                layer.open({
                    type: 2,
                    area: ['1024px', '650px'],
                    maxmin: false,
                    title: bt.lang('sys.dual.AssignResourceComponent') + " - " + data.username,
                    content: basePath + "/sys/user/assignResource?userId=" + data.id + "&corpId=" + data.corpId
                });
            } else {
                layer.alert(result.msg, {icon: 2});
            }
        }, 'JSON');
    });
    table.on('click', '.copy-resource-user', function (event) {
        var data = table.grid("getOriginalData", $(this).data("row"));
        bt.navtab.addNavtab(bt.lang("table.user.copyPermissions"), basePath + "/sys/user/coypPermissionsOpt?id=" + data.id);
        /*        $.get(basePath + "/sys/user/checkUserHasRoles", {userId: data.id}, function (result) {
                    if (result.success) {
                        layer.open({
                            type: 2,
                            area: ['850px', '650px'],
                            content: basePath + "/sys/user/copyRoles?sourceUser=" + data.id
                        });
                    } else {
                        layer.alert(result.msg,{icon:2});
                    }
                }, 'JSON');*/

    });
    table.on('click', '.assign-role', function (event) {
        var data = table.grid("getOriginalData", $(this).data("row"));
        layer.open({
            type: 2,
            area: ['850px', '650px'],
            content: basePath + "/sys/user/assignRoles?userId=" + data.id
        });

    });
    table.on('click', '.user-recover', function (event) {
        var data = table.grid("getOriginalData", $(this).data("row"));
        layer.confirm(bt.lang('sys.user.isRecover'), function () {
            $.post(basePath + "/sys/user/recover", {id: data.id}, function (result) {
                table.trigger("reloadGrid");
                layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, "JSON");
        });
    });
    table.on('click', '.user-caDel', function (event) {
        var data = table.grid("getOriginalData", $(this).data("row"));
        layer.confirm(bt.lang('sys.user.isDelete.UserCa'), function () {
            $.post(basePath + "/sys/user/caDel", {id: data.id}, function (result) {
                table.trigger("reloadGrid");
                layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, "JSON");
        });
    });
    table.on('click', '.user-caBinding', function (event) {
        var data = table.grid("getOriginalData", $(this).data("row"));
        var userCadn = "";
        var encrypt = $("#encrypt").val();
        $("#cadn").val("");
        if (encrypt == 0) {
            layer.alert(bt.lang('sys.config.encrypt'), {icon: 2});
            return false;
        }
        try {
            if (encrypt == 1) {
                var certList = selectiTrusCa();
                layer.open({
                    title: '天威证书列表',
                    content: certList,
                    yes: function (index, layero) {
                        CurCert = selectSingleCert("", layero.find("select option:selected").val());
                        userCadn = layero.find("select option:selected").val();
                        layer.close(index);
                        if (userCadn != "") {
                            $.post(basePath + "/sys/user/caBinding", {
                                id: data.id,
                                userCadn: userCadn
                            }, function (result) {
                                table.trigger("reloadGrid");
                                layer.alert(result.msg, {icon: result.success ? 1 : 2});
                            }, "JSON");
                        }
                    }
                });
            } else if (encrypt == 2) {
                userbinding(saveUserCadn);

                function saveUserCadn(userCadn) {
                    $.post(basePath + "/sys/user/caBinding", {id: data.id, userCadn: userCadn}, function (result) {
                        table.trigger("reloadGrid");
                        layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, "JSON");
                }
            } else if (encrypt == 3) {
                selectXyCa();
                userCadn = getXyCertCN();
                if (userCadn != "") {
                    $.post(basePath + "/sys/user/caBinding", {id: data.id, userCadn: userCadn}, function (result) {
                        table.trigger("reloadGrid");
                        layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, "JSON");
                }
            } else if (encrypt == 4) {
                //判断是否插入key
                var key = keyExist();
                if(key != "true"){
                    layer.alert(bt.lang('sys.login.insertCertificate'), {icon: 2});
                    return false;
                }
                //判断key用户名和绑定用户是否一致
                var certName = getCertName();
                if(data.loginName != certName){
                    layer.alert(bt.lang('sys.ca.cert.name'), {icon: 2});
                    return false;
                }
                userCadn = getCertSN();
                if (userCadn != "") {
                    $.post(basePath + "/sys/user/caBinding", {id: data.id, userCadn: userCadn}, function (result) {
                        table.trigger("reloadGrid");
                        layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, "JSON");
                }
            }else if(encrypt == 5){
                userCadn = getCertSN();
                if (userCadn != "") {
                    $.post(basePath + "/sys/user/caBinding", {id: data.id, userCadn: userCadn}, function (result) {
                        table.trigger("reloadGrid");
                        layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }, "JSON");
                }
            }
        } catch (e) {
            layer.alert(bt.lang("sys.ca.abnormal"), {icon: 2});
            return false;
        }
    });

    bt.corp.init();

    /**
     * 导入
     */
    var importFunc = function () {
        up = bt.btImport.init({
            code: "90000003",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/sys/user/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        table.trigger("reloadGrid");
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    }

    if(bt.isResource('sys:user:save')) {
        table.grid("customButton", {
            title: "导入",
            buttonicon: "fa-upload",
            onClickButton: importFunc
        })
    }

    /**
     * 导出
     */
    var downloadFunc = function () {
        var url = basePath + "/sys/user/downloadUser";
        var data = {}
        var fieldElem = $('#searchForm').find('input,select,textarea') //获取所有表单域
        bt.each(fieldElem,function (i,item) {
            if(item.name){
                data[item.name] = item.value
            }
        })
        DownLoadFile({url: url, data: data, method: 'post'});
        // window.location.href = basePath + "/sys/user/downloadUser";
    }

    //导出表格
    var DownLoadFile = function (options) {
        var config = $.extend(true, {method: 'post'}, options);
        var $iframe = $('<iframe id="down-file-iframe" style="display: none"/>');
        var $form = $('<form target="_self" method="post" />');
        $form.attr('action', config.url);
        for (var key in config.data) {
            $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
        }
        $iframe.append($form);
        $(document.body).append($iframe);
        $form[0].submit();
        $iframe.remove();
        return false;
    }

    if(bt.isResource('sys:user:select')) {
        table.grid("customButton", {
            title: "导出",
            buttonicon: "fa-download",
            onClickButton: downloadFunc
        })
    }

});

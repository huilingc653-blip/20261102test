var dicts;
bt.use(['form', 'datatable', 'corp', 'selectType', 'dicts', 'bankType'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;
    var $ = bt.jquery, form = bt.form();
    var bankAccountUrl = basePath + "/sys/accountGrant/datatable";
    var innerAccountUrl = basePath + "/sys/accountGrant/dataTableAllNormal";
    var currentAccountType = 1;
    var table;
    var isOnline = $('input[name="eqs_isOnline"]');
    var userId = $("#userId").val();
    var accountType;
    var accountCorpId = $("#accountCorpId").val();
    var assigned;
    var tableBind = true;
    // 账户类型切换回调事件
    bt.accountType = function (data, type) {
        $('input[name="ins_corpId"]').val('');
        var type = $(data.elem).val();
        currentAccountType = type;
        accountType = type;
        switch (type) {
            // 资金池内部账号
            case "1":
                $("#innerAccount").removeClass('hidden');
                $("#innerAccountTable").removeClass('hidden');
                $("#account").addClass('hidden');
                $("#bankAccountTable").addClass('hidden');
                $("#poolInnerAccountTable").addClass('hidden');

                $('.placeholder-account').attr('type', 'hidden');
                $('input[name="likes_bankAcc"]').attr('type', 'hidden').val('');
                $('input[name="likes_blnctrAcc"]').attr('type', 'text');
//                    $('input:[name="likes_blnctrAcc"]').addClass('hidden')

                $('input[name="ins_corpId"]').val($("#innerAccountCorp").val());

                $("#eqs_poolSign").val(0);
                corp.get("corpId_element").setUrl();
                dicts.get("innerAccountType").attr({"data-displayData": null});
                bt.initTable(data);
                break;
            // 在线银行账号，离线银行账号
            case "2":
            case "3":
                $("#account").removeClass('hidden');
                $("#bankAccountTable").removeClass('hidden');
                $("#innerAccount").addClass('hidden');
                $("#innerAccountTable").addClass('hidden');
                $("#poolInnerAccountTable").addClass('hidden');

                $('.placeholder-account').attr('type', 'hidden');
                $('input[name="likes_blnctrAcc"]').attr('type', 'hidden').val('');
                $('input[name="likes_bankAcc"]').attr('type', 'text');

                if (currentAccountType == "2") {
                    isOnline.val(1);
                } else {
                    isOnline.val(0);
                }

                $('input[name="ins_corpId"]').val($("#accountCorp").val());

                corp.get("corpId_element").setUrl();
                bt.initTable(data);
                break;
            // 内部账号
            case "4":
                $("#innerAccount").removeClass('hidden');
                $("#poolInnerAccountTable").removeClass('hidden');
                $("#account").addClass('hidden');
                $("#bankAccountTable").addClass('hidden');
                $("#innerAccountTable").addClass('hidden');

                $('.placeholder-account').attr('type', 'hidden');
                $('input[name="likes_bankAcc"]').attr('type', 'hidden').val('');
                $('input[name="likes_bankAccOffline"]').attr('type', 'hidden').val('');
                $('input[name="likes_blnctrAcc"]').attr('type', 'text');

                $('input[name="ins_corpId"]').val($("#innerAccountCorp").val());

                $("#eqs_poolSign").val(1);
                corp.get("corpId_element").setUrlPool();
                dicts.get("innerAccountType").attr({"data-displayData": "01,02,07"});

                bt.initTable(data);
                break;
            default:
                $("#innerAccount").addClass('hidden');
                $("#account").addClass('hidden');

                $('input[name="likes_blnctrAcc"]').attr('type', 'hidden').val('');
                $('input[name="likes_bankAcc"]').attr('type', 'text');

                $('input[name="ins_corpId"]').val('');
        }
    };
    accountGrantSearchCallBack = function(data){
        if(data.value == "1"){
            $("#accountGrantFunctionlable").removeClass('hidden');
            $("#accountGrantFunction").removeClass('hidden');
        }else{
            $("#accountGrantFunctionlable").addClass("hidden");
            $("#accountGrantFunction").addClass('hidden');
        }
    };
    bt.getInnerAccount = function () {
        return [
            {
                data: "blnctrAcc",
                bSortable: false, render: function (data, type, full, meta) {
                    return full.blnctrAcc;
                }
            }, {
                data: "accName",
                bSortable: false, render: function (data, type, full, meta) {
                    return full.accName;
                }
            }, {
                data:currentAccountType === "1"? "accName":"netPoolName",
                bSortable: true, render: function (data, type, full, meta) {
                    if (currentAccountType === "1") {
                        return full.netName;
                    } else {
                        return full.netPoolName;
                    }
                }
            }, {
                data: "corpName",
                bSortable: false, render: function (data, type, full, meta) {
                    return full.corpName;
                }
            }, {
                data: "curName",
                bSortable: false, render: function (data, type, full, meta) {
                    return full.curName;
                }
            }, {
                data: "accTypeName",
                bSortable: false, render: function (data, type, full, meta) {
                    return full.accTypeName;
                }
            }, {

                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="queryDetail" lay-skin="primary" ' + (checkHasUserIdAndPermission(full, 1) ? 'checked="checked"' : '') + ' />';
                }
            }, {

                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="queryBalance" lay-skin="primary" ' + (checkHasUserIdAndPermission(full, 2) ? 'checked="checked"' : '') + ' />';
                }
            }, {
                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="transferAccounts" lay-skin="primary" ' + (checkHasUserIdAndPermission(full, 3) ? 'checked="checked"' : '') + ' />';
                }
            }
        ];
    };

    bt.getBankAccount = function () {
        return [
            {
                data: "bankAcc",
                bSortable: false, render: function (data, type, full, meta) {
                    //账号
                    return full.bankAcc;
                }
            }, {
                data: "accName",
                bSortable: false, render: function (data, type, full, meta) {
                    //账户名称
                    return full.accName;
                }
            }, {
                data: "corpName",
                bSortable: false, render: function (data, type, full, meta) {
                    //开户单位
                    return full.corpName;
                }
            }, {
                data: "bankTypeName",
                bSortable: false, render: function (data, type, full, meta) {
                    //银行类别
                    return full.bankTypeName;
                }
            }, {
                data: "bankName",
                bSortable: false, render: function (data, type, full, meta) {
                    //开户行
                    return full.bankName;
                }
            }, {
                data: "curName",
                bSortable: false, render: function (data, type, full, meta) {
                    //币别
                    return full.curName;
                }
            }, {
                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="queryDetail" lay-skin="primary" ' + (checkHasUserIdAndPermission(full, 1) ? 'checked="checked"' : '') + ' />';
                }
            }, {
                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="queryBalance" lay-skin="primary" ' + (checkHasUserIdAndPermission(full, 2) ? 'checked="checked"' : '') + ' />';
                }
            }, {
                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="transferAccounts" lay-skin="primary" ' + (checkHasUserIdAndPermission(full, 3) ? 'checked="checked"' : '') + ' />';
                }
            }
        ];
    };

    bt.getAssignData = function () {
        $.ajax({
            type: "get",
            url: basePath + "/sys/accountGrant/assigned",
            dataType: "json",
            async: false,
            data: {accountType: currentAccountType, userId: userId},
            success: function (result) {
                assigned = result;

            }
        });
    };

    bt.initTable = function (data) {
        var text = $(data.elem).find('option:selected').text();
        if (table) {
            table.destroy();
        }
        bt.getAssignData();
        var flag = currentAccountType === "1" || currentAccountType === "4";
        //生成列表
        table = bt.datatable.initMultiTables({
            elem: '#' + (flag ? currentAccountType === "4" ? 'poolInnerAccountTable' : 'innerAccountTable' : 'bankAccountTable'),
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': flag ? innerAccountUrl : bankAccountUrl
            },
            stateKey:flag ? 'innerAccountTable_assignAccount_stateKey' : 'bankAccountTable_assignAccount_stateKey',
            exportFile: { //输出文档设置
                printTitle:text, //设置打印标题　默认无标题
                outputFilename: text,//设置导出文件名称　默认为父级选项卡内容或标题名称
            },
            colReorder: false,
            selectCss: false,
            columns: flag ? bt.getInnerAccount() : bt.getBankAccount(),
            btDrawCallback: function (settings) {
                //检查当前页面，是否全部选择了权限
                checkCurrentPageIsAllChecked();
            },
        });
        table.off("preXhr.dt").on('preXhr.dt', function (e, settings, data) {
            bt.getAssignData();
        });
        if (tableBind) {
            tableBind = false;
        }
    };

    form.on('radio(chooseAssignType)', function (data) {
        if (data.value == 1) {
            location.href = basePath + "/sys/accountGrant/list";
        } else {
            location.href = basePath + "/sys/accountGrant/listUser";
        }
    });

    $("#submitAssign").click(function () {
        bt.loading.show();
        if (!table) {
            bt.loading.hide();
            layer.alert(bt.lang('accountGrant.sysAccountType'), {icon: 2});
            return;
        }


        var accountGrant = [];

        var queryDetailData = queryDetail.data();
        var queryBalanceData = queryBalance.data();
        var transferAccountsData = transferAccounts.data();

        //只更新当前页的数据
        // table.rows().nodes().data().each(function (k, v) {
        //     var kd = k.id;
        //     if (!accountGrant[kd]) {
        //         accountGrant[kd] = {};
        //     }
        //     accountGrant[kd].accountId = kd;
        //     accountGrant[kd].accountType = accountType;
        //     accountGrant[kd].corpId = k.corpId;
        //     accountGrant[kd].userId = userId;
        // });

        $.each(queryDetailData, function (kd, e) {
            accountGrant[kd] = {};
            accountGrant[kd].accountId = kd;
            accountGrant[kd].accountType = accountType;
            accountGrant[kd].corpId = e.corpId;
            accountGrant[kd].userId = userId;
            accountGrant[kd]['canQueryDetail'] = e.canQueryDetail;
            if (checkHasUserIdAndPermission(e, 2)) {
                accountGrant[kd]['canQueryBalance'] = 1;
            }
            if (checkHasUserIdAndPermission(e, 3)) {
                accountGrant[kd]['canTransferAccounts'] = 1;
            }
        });
        $.each(queryBalanceData, function (kd, e) {
            if (accountGrant[kd]) {
                accountGrant[kd]['canQueryBalance'] = e.canQueryBalance;
            } else {
                accountGrant[kd] = {};
                accountGrant[kd].accountId = kd;
                accountGrant[kd].accountType = accountType;
                accountGrant[kd].corpId = e.corpId;
                accountGrant[kd].userId = userId;
                accountGrant[kd]['canQueryBalance'] = e.canQueryBalance;
            }
            if (checkHasUserIdAndPermission(e, 1)) {
                accountGrant[kd]['canQueryDetail'] = 1;
            }
            if (checkHasUserIdAndPermission(e, 3)) {
                accountGrant[kd]['canTransferAccounts'] = 1;
            }
        });
        $.each(transferAccountsData, function (kd, e) {
            if (accountGrant[kd]) {
                accountGrant[kd]['canTransferAccounts'] = e.canTransferAccounts;
            } else {
                accountGrant[kd] = {};
                accountGrant[kd].accountId = kd;
                accountGrant[kd].accountType = accountType;
                accountGrant[kd].corpId = e.corpId;
                accountGrant[kd].userId = userId;
                accountGrant[kd]['canTransferAccounts'] = e.canTransferAccounts;
            }
            if (checkHasUserIdAndPermission(e, 1)) {
                accountGrant[kd]['canQueryDetail'] = 1;
            }
            if (checkHasUserIdAndPermission(e, 2)) {
                accountGrant[kd]['canQueryBalance'] = 1;
            }
        });
        var postArr = [];
        for (var k in accountGrant) {
            postArr.push(accountGrant[k]);
        }


        $.ajax({
            type: "POST",
            url: basePath + "/sys/accountGrant/assignUser",
            data: JSON.stringify(postArr),//将对象序列化成JSON字符串
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                bt.loading.hide();
                layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    //清空存储在dom的数据
                    queryDetail && queryDetail.removeData();
                    queryBalance && queryBalance.removeData();
                    transferAccounts && transferAccounts.removeData();
                    //重新加载授权数据
                    bt.getAssignData();
                }
            }
        });
    });

    /**
     * 设置是否选中成功
     * @param userId
     * @param type
     * @returns {boolean}
     */
    function checkHasUserIdAndPermission(data, type) {
        switch (type) {
            case 1:
                var temp = queryDetail.data(data.id);
                if (temp && temp.canQueryDetail === 1) {
                    return true;
                }
                break;
            case 2:
                var temp = queryBalance.data(data.id);
                if (temp && temp.canQueryBalance === 1) {
                    return true;
                }
                break;
            case 3:
                var temp = transferAccounts.data(data.id);
                if (temp && temp.canTransferAccounts === 1) {
                    return true;
                }
                break;
        }

        for (var k in assigned) {
            var accountGrant = assigned[k];
            if (accountGrant.accountId != data.id) {
                continue;
            }
            // 账号变更为离线，账号授权为在线时，跳过以达到页面不勾选效果
            if (data.isOnline == 0 && accountGrant.accountType == 2) {
                continue;
            }
            // 账号变更为在，账号授权为离线时，跳过以达到页面不勾选效果
            if (data.isOnline == 1 && accountGrant.accountType == 3) {
                continue;
            }
            switch (type) {
                case 1:
                    if (!queryDetail.data(data.id) && accountGrant.canQueryDetail) {
                        return true;
                    }
                    break;
                case 2:
                    if (!queryBalance.data(data.id) && accountGrant.canQueryBalance) {
                        return true;
                    }
                    break;
                case 3:
                    if (!transferAccounts.data(data.id) && accountGrant.canTransferAccounts) {
                        return true;
                    }
                    break;
            }
        }

        return false;
    }

    function setOrRemove(index, jqueryDom, checked) {
        var tableData = table.row(index).data();
        if (checked) {
            var temp = jqueryDom.data(tableData.id);
            if (!temp) {
                jqueryDom.data(tableData.id, tableData);
            }
        } else {
            jqueryDom.removeData(tableData.id);
        }
    }

    function checkAll(data, dataDom) {
        //index是表格列的下标（0开始）
        var index = data.othis.parents('th').index();
        table.rows().nodes().to$().each(function () {
            var that = $(this);
            var child;
            if (data.elem.checked) {
                child = that.find('td:eq(' + index + ') input[type="checkbox"]');
            } else {
                child = that.find('td:eq(' + index + ') input[type="checkbox"]');
            }
            child.each(function (index, item) {
                item.checked = data.elem.checked;
            });
            setOrRemove(that.index(), dataDom, data.elem.checked);
        });
        form.render('checkbox');
    }

    function checkAll(data, dataDom, type) {
        //index是表格列的下标（0开始）
        var index = data.othis.parents('th').index();
        table.rows().nodes().to$().each(function () {
            var that = $(this);
            var child;
            if (data.elem.checked) {
                child = that.find('td:eq(' + index + ') input[type="checkbox"]');
            } else {
                child = that.find('td:eq(' + index + ') input[type="checkbox"]');
            }
            child.each(function (index, item) {
                item.checked = data.elem.checked;
            });

            //多次更改权限的时候，就更新中间数据结构对应账号的权限信息。
            var rowIndex = that.index();
            var tableData = table.row(rowIndex).data();
            if (dataDom.data(tableData.id)) {
                tableData = dataDom.data(tableData.id);
            }
            if (data.elem.checked) {
                tableData[type] = 1;
            } else {
                tableData[type] = 0;
            }
            dataDom.data(tableData.id, tableData);
        });
        form.render('checkbox');
    }

    /**
     * 检查数据列表是否全选了权限
     * @param dataList
     */
    function checkAccountListIsAllChecked(dataList) {
        if (dataList) {
            var queryDetailAllChecked = true;
            var queryBalanceAllChecked = true;
            var transferAccountsAllChecked = true;

            for (var i = 0; i < dataList.length; i++) {
                if (!checkHasUserIdAndPermission(dataList[i], 1)) {
                    queryDetailAllChecked = false;
                }
                if (!checkHasUserIdAndPermission(dataList[i], 2)) {
                    queryBalanceAllChecked = false;
                }
                if (!checkHasUserIdAndPermission(dataList[i], 3)) {
                    transferAccountsAllChecked = false;
                }
            }
            var queryDetailAllElement = $(".queryDetailAll");
            var queryBalanceAllElement = $(".queryBalanceAll");
            var transferAccountsAllElement = $(".transferAccountsAll");
            if (queryDetailAllChecked) {
                queryDetailAllElement && queryDetailAllElement.prop("checked", true);
            } else {
                queryDetailAllElement && queryDetailAllElement.prop("checked", false);
            }
            if (queryBalanceAllChecked) {
                queryBalanceAllElement && queryBalanceAllElement.prop("checked", true);
            } else {
                queryBalanceAllElement && queryBalanceAllElement.prop("checked", false);
            }
            if (transferAccountsAllChecked) {
                transferAccountsAllElement && transferAccountsAllElement.prop("checked", true);
            } else {
                transferAccountsAllElement && transferAccountsAllElement.prop("checked", false);
            }
            form.render('checkbox', 'queryDetailAll');
            form.render('checkbox', 'queryBalanceAll');
            form.render('checkbox', 'transferAccountsAll');
        }
    }

    /**
     * 检查当前页面是否全选了权限
     * 如果全选了，则将最上面的复选框选上。
     */
    function checkCurrentPageIsAllChecked() {
        var dataList = [];
        table.rows().nodes().data().each(function (data, index) {
            dataList.push(data);
        });
        checkAccountListIsAllChecked(dataList);
    }


    var queryDetail = $('.queryDetail');
    var queryBalance = $('.queryBalance');
    var transferAccounts = $('.transferAccounts');

    form.on('checkbox(queryDetail)', function (data) {

        var rowIndex = data.othis.parents('tr').index();
        var tableData = table.row(rowIndex).data();
        if (queryDetail.data(tableData.id)) {
            tableData = queryDetail.data(tableData.id);
        }
        if (data.elem.checked) {
            tableData.canQueryDetail = 1;
        } else {
            tableData.canQueryDetail = 0;
        }
        queryDetail.data(tableData.id, tableData);
        checkCurrentPageIsAllChecked();
        form.render('checkbox');
    });

    form.on('checkbox(queryDetailAll)', function (data) {
        //checkAll(data, queryDetail);
        checkAll(data, queryDetail, 'canQueryDetail');
    });

    form.on('checkbox(queryBalance)', function (data) {
        //多次更改权限的时候，就更新中间数据结构对应账号的权限信息。
        var rowIndex = data.othis.parents('tr').index();
        var tableData = table.row(rowIndex).data();
        if (queryBalance.data(tableData.id)) {
            tableData = queryBalance.data(tableData.id);
        }
        if (data.elem.checked) {
            tableData.canQueryBalance = 1;
        } else {
            tableData.canQueryBalance = 0;
        }
        queryBalance.data(tableData.id, tableData);
        checkCurrentPageIsAllChecked();
        form.render('checkbox');
    });
    form.on('checkbox(queryBalanceAll)', function (data) {
        //checkAll(data, queryBalance);
        checkAll(data, queryBalance, 'canQueryBalance');
    });

    form.on('checkbox(transferAccounts)', function (data) {
        var rowIndex = data.othis.parents('tr').index();
        var tableData = table.row(rowIndex).data();
        if (transferAccounts.data(tableData.id)) {
            tableData = transferAccounts.data(tableData.id);
        }
        if (data.elem.checked) {
            tableData.canTransferAccounts = 1;
        } else {
            tableData.canTransferAccounts = 0;
        }
        transferAccounts.data(tableData.id, tableData);
        checkCurrentPageIsAllChecked();
        form.render('checkbox');
    });
    form.on('checkbox(transferAccountsAll)', function (data) {
        //checkAll(data, transferAccounts);
        checkAll(data, transferAccounts, 'canTransferAccounts');
    });


    //单位回调函数
    bt.callBackData = function (obj) {
        $("#poolType").val(obj.type);
    };
    var corp = bt.corp.init();
    dicts = bt.dicts.init();
    var selectType = bt.selectType.init();
});

function checkAccountGrantFunction() {
    var accountGrantFunction = dicts.get('accountGrantFunction').val();
    if (accountGrantFunction.length === 0) {
        layer.alert(bt.lang("message.account.grant.accountGrantFunction"), {icon: 2});
        // dicts.get('accountGrantFunction').val("1,2,3");
        dicts.get('accountGrantFunction').attr({'data-defaultvalue': 'all'});
    }
}

// 重置
function resetAssignAccount() {
    $("#accountGrantFunctionlable").addClass("hidden");
    $("#accountGrantFunction").addClass('hidden');
}


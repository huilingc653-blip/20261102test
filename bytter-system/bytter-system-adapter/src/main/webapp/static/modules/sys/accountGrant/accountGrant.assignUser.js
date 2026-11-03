var $, form, layer
bt.use(['datatable', 'jquery', 'form', 'corp'], function () {
    layer = parent ? parent.bt.layer : bt.layer;
    form = bt.form();
    $ = bt.jquery;
    var accountId = $("#accountId").val();
    var accountType = $("#accountType").val();
    var accountCorpId = $("#accountCorpId").val();

    var assigned;
    $.ajax({
        type: "get",
        url: basePath + "/sys/accountGrant/assigned",
        dataType: "json",
        async: false,
        data: {accountId: accountId, accountType: accountType},
        success: function (result) {
            assigned = result;
        }
    });


    var table = bt.datatable.init({
        elem: "#userTable",
        ajax: {
            url: basePath + "/sys/user/datatable.json",
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        scrollY: '400px',
        selectCss: false,
        colReorder: false,
        stateKey: 'userTable_assignUser_stateKey',
        columns: [
            {data: "loginName"},
            {data: "username"},
            {data: "corpName"},
            {
                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="queryDetail" lay-skin="primary" ' + (checkHasUserIdAndPermission(full, 1) ? 'checked="checked"' : '') + ' />';
                }
            },
            {
                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="queryBalance" lay-skin="primary" ' + (checkHasUserIdAndPermission(full, 2) ? 'checked="checked"' : '') + ' />';
                }
            }, {
                className: "text-center", orderable: false, render: function (data, type, full, meta) {
                    return '<input type="checkbox" name="like[write]" lay-filter="transferAccounts" lay-skin="primary" ' + (checkHasUserIdAndPermission(full, 3) ? 'checked="checked"' : '') + ' />';
                }
            }
        ],
        drawCallback: function (settings) {
            queryDetailIsAllChecked();
            queryBalanceIsAllChecked();
            transferAccountsIsAllChecked();
            form.render('checkbox');
        }
    });

    //发生异步请求表格数据的时候，清楚dom中的data
    table.on('preXhr.dt', function (e, settings, data) {
        //移除所有的数据
        queryDetail.removeData();
        queryBalance.removeData();
        transferAccounts.removeData();
    })

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

        table.rows().nodes().data().each(function (k, v) {
            var kd = k.id;
            if (!accountGrant[kd]) {
                accountGrant[kd] = {};
            }
            accountGrant[kd].accountId = accountId;
            accountGrant[kd].accountType = accountType;
            accountGrant[kd].corpId = accountCorpId;
            accountGrant[kd].userId = kd;

        });

        $.each(queryDetailData, function (kd, e) {
            if (accountGrant[kd]) {
                accountGrant[kd]['canQueryDetail'] = 1;
            }
        });
        $.each(queryBalanceData, function (kd, e) {
            if (accountGrant[kd]) {
                accountGrant[kd]['canQueryBalance'] = 1;
            }
        });
        $.each(transferAccountsData, function (kd, e) {
            if (accountGrant[kd]) {
                accountGrant[kd]['canTransferAccounts'] = 1;
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
        for (var k in assigned) {
            var accountGrant = assigned[k];
            if (accountGrant.userId != data.id) {
                continue;
            }
            // 账号变更为离线，账号授权为在线时，跳过以达到页面不勾选效果
            if ($("#accountType").val() == 3 && accountGrant.accountType == 2) {
                continue;
            }
            // 账号变更为在，账号授权为离线时，跳过以达到页面不勾选效果
            if ($("#accountType").val() == 2 && accountGrant.accountType == 3) {
                continue;
            }
            switch (type) {
                case 1:
                    if (accountGrant.canQueryDetail) {
                        queryDetail.data(data.id, data);
                        return true
                    }
                    break;
                case 2:
                    if (accountGrant.canQueryBalance) {
                        queryBalance.data(data.id, data);
                        return true
                    }
                    break;
                case 3:
                    if (accountGrant.canTransferAccounts) {
                        transferAccounts.data(data.id, data);
                        return true
                    }
                    break;
            }
        }
        return false;
    }

    function setOrRemove(index, jqueryDom, checked) {
        var tableData = table.row(index).data();
        if (checked) {
            jqueryDom.data(tableData.id, tableData);
        } else {
            jqueryDom.removeData(tableData.id);
        }
    }

    function checkAll(data, dataDom) {
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

    var queryDetail = $('.queryDetail');
    var queryBalance = $('.queryBalance');
    var transferAccounts = $('.transferAccounts');

    form.on('checkbox(queryDetail)', function (data) {
        setOrRemove(data.othis.parents('tr').index(), queryDetail, data.elem.checked);
        if (!data.elem.checked) {
            queryDetail.find('input[type="checkbox"]').each(function (index, item) {
                item.checked = data.elem.checked;
            });
        } else {
            queryDetailIsAllChecked();
        }
        form.render('checkbox');
    })
    form.on('checkbox(queryDetailAll)', function (data) {
        checkAll(data, queryDetail);
    })

    form.on('checkbox(queryBalance)', function (data) {
        setOrRemove(data.othis.parents('tr').index(), queryBalance, data.elem.checked);
        if (!data.elem.checked) {
            queryBalance.find('input[type="checkbox"]').each(function (index, item) {
                item.checked = data.elem.checked;
            });
        } else {
            queryBalanceIsAllChecked();
        }
        form.render('checkbox');
    });
    form.on('checkbox(queryBalanceAll)', function (data) {
        checkAll(data, queryBalance);
    })

    form.on('checkbox(transferAccounts)', function (data) {
        setOrRemove(data.othis.parents('tr').index(), transferAccounts, data.elem.checked);
        if (!data.elem.checked) {
            transferAccounts.find('input[type="checkbox"]').each(function (index, item) {
                item.checked = data.elem.checked;
            });
        } else {
            transferAccountsIsAllChecked();
        }
    });
    form.on('checkbox(transferAccountsAll)', function (data) {
        checkAll(data, transferAccounts);
    })

    bt.corp.init();
});

/**
 * 判断是否全选中-明细查询
 */
function queryDetailIsAllChecked() {
    var checkBoxs = $('[lay-filter="queryDetail"]').length;
    var checkedBoxs = $('[lay-filter="queryDetail"]:checked').length;
    if (checkBoxs && checkBoxs == checkedBoxs) {
        // 证明全选
        $('[lay-filter="queryDetailAll"]').prop('checked', true)
    }
}

/**
 * 判断是否全选中-查询余额
 */
function queryBalanceIsAllChecked() {
    var checkBoxs = $('[lay-filter="queryBalance"]').length;
    var checkedBoxs = $('[lay-filter="queryBalance"]:checked').length;
    if (checkBoxs && checkBoxs == checkedBoxs) {
        // 证明全选
        $('[lay-filter="queryBalanceAll"]').prop('checked', true)
    }
}

/**
 * 判断是否全选中-转账
 */
function transferAccountsIsAllChecked() {
    var checkBoxs = $('[lay-filter="transferAccounts"]').length;
    var checkedBoxs = $('[lay-filter="transferAccounts"]:checked').length;
    if (checkBoxs && checkBoxs == checkedBoxs) {
        // 证明全选
        $('[lay-filter="transferAccountsAll"]').prop('checked', true)
    }
}

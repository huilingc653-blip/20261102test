bt.use(['form', 'datatable', 'corp', 'selectType', 'dicts', 'bankType'], function () {
    var $ = bt.jquery, form = bt.form();

    var bankAccountUrl = basePath + "/sys/accountGrant/datatable";
    var innerAccountUrl = basePath + "/sys/accountGrant/dataTableAllNormal";
    var currentAccountType = 1;
    var table;
    var isOnline = $('input[name="eqs_isOnline"]');
    var tableBind = true;

    bt.accountType = function (data, type) {
        var type = $(data.elem).val();
        currentAccountType = type;
        switch (type) {
            case "1":
                $("#innerAccount").removeClass('hidden');
                $("#innerAccountTable").removeClass('hidden');
                $("#account").addClass('hidden');
                $("#bankAccountTable").addClass('hidden');
                $("#poolInnerAccountTable").addClass('hidden');


                $('.placeholder-account').attr('type', 'hidden');
                $('input[name="likes_bankAcc"]').attr('type', 'hidden').val('');
                $('input[name="likes_bankAccOffline"]').attr('type', 'hidden').val('');
                $('input[name="likes_blnctrAcc"]').attr('type', 'text');

                $("#eqs_poolSign").val(0);
                corp.get("eqs_corpId").setUrl();
                dicts.get("innerAccountType").attr({"data-displayData": null});
                bt.initTable();
                break;
            case "2":
                $("#account").removeClass('hidden');
                $("#bankAccountTable").removeClass('hidden');
                $("#innerAccount").addClass('hidden');
                $("#innerAccountTable").addClass('hidden');
                $("#poolInnerAccountTable").addClass('hidden');
                $('input[name="likes_blnctrAcc"]').attr('type', 'hidden').val('');
                $('input[name="likes_bankAccOffline"]').attr('type', 'hidden').val('');
                $('input[name="likes_bankAcc"]').attr('type', 'text');
                $('.placeholder-account').attr('type', 'hidden');

                if (currentAccountType == "2") {
                    isOnline.val(1);
                } else {
                    isOnline.val(0);
                }
                $("#eqs_poolSign").removeAttr("value");
                corp.get("eqs_corpId").setUrl();
                bt.initTable();
                break;
            case "3":
                $("#account").removeClass('hidden');
                $("#bankAccountTable").removeClass('hidden');
                $("#innerAccount").addClass('hidden');
                $("#innerAccountTable").addClass('hidden');
                $("#poolInnerAccountTable").addClass('hidden');

                $('.placeholder-account').attr('type', 'hidden');

                $('input[name="likes_bankAcc"]').attr('type', 'hidden').val('');
                $('input[name="likes_bankAccOffline"]').attr('type', 'text');
                $('input[name="likes_blnctrAcc"]').attr('type', 'hidden').val('');

                if (currentAccountType == "2") {
                    isOnline.val(1);
                } else {
                    isOnline.val(0);
                }
                $("#eqs_poolSign").removeAttr("value");
                corp.get("eqs_corpId").setUrl();
                bt.initTable();
                break;
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

                $("#eqs_poolSign").val(1);
                corp.get("eqs_corpId").setUrlPool();
                dicts.get("innerAccountType").attr({"data-displayData": "01,02,07"});
                bt.initTable();
                break;
            default:
                $("#innerAccount").addClass('hidden');
                $("#account").addClass('hidden');

                $('input[name="likes_bankAcc"]').attr('type', 'hidden').val('');
                $('input[name="likes_blnctrAcc"]').attr('type', 'hidden').val('');
                $('input[name="likes_bankAccOffline"]').attr('type', 'hidden').val('');
                $('input[name="likes_accoutType"]').attr('type', 'text');
        }

    }

    /*绘制内部账号的数据表格*/
    bt.getInnerAccount = function () {
        return [
            {
                data: "blnctrAcc", bSortable: true, render: function (data, type, full, meta) {
                    return full.blnctrAcc;
                }
            },
            {
                data: "netName", bSortable: true, render: function (data, type, full, meta) {
                    if (currentAccountType === "1") {
                        return full.netName;
                    } else {
                        return full.netPoolName;
                    }
                }
            },
            {
                data: "accName", bSortable: true, render: function (data, type, full, meta) {
                    return full.accName;
                }
            },
            {
                data: "corpId", bSortable: true, render: function (data, type, full, meta) {
                    return full.corpName;
                }
            }, {
                data: "curId", bSortable: true, render: function (data, type, full, meta) {
                    return full.curName;
                }
            }, {
                data: "accTypeName", bSortable: true, render: function (data, type, full, meta) {
                    return bt.dict("InnerAccount.AccType", full.accType);
                }
            },
            {
                bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    return '<a href="javascript:void(0);" data-row="' + meta.row + '" class="assgin-user">' + bt.lang('accountGrant.assginUser') + '</a>';
                }
            }
        ];
    }

    /*绘制银行账号的数据表格*/
    bt.getBankAccount = function () {
        return [
            {
                data: "bankAcc", bSortable: true, render: function (data, type, full, meta) {
                    //账号
                    return full.bankAcc;
                }
            },
            {
                data: "accName", bSortable: true, render: function (data, type, full, meta) {
                    //账户名称
                    return full.accName;
                }
            },
            {
                data: "corpId", bSortable: true, render: function (data, type, full, meta) {
                    //开户单位
                    return full.corpName;
                }
            }, {
                data: "bankTypeName", bSortable: true, render: function (data, type, full, meta) {
                    //银行类别
                    return full.bankTypeName;
                }
            }, {
                data: "bankName", bSortable: true, render: function (data, type, full, meta) {
                    //开户行
                    return full.bankName;
                }
            }, {
                data: "attributeName", bSortable: true, render: function (data, type, full, meta) {
                    //账户属性
                    return full.attributeName;
                }
            }, {
                data: "curId", bSortable: false, render: function (data, type, full, meta) {
                    //币别
                    return full.curName;
                }
            },
            {
                bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    return '<a href="javascript:void(0);" data-row="' + meta.row + '" class="assgin-user">' + bt.lang('accountGrant.assginUser') + '</a>';
                }
            }
        ];
    }

    bt.initTable = function () {
        if (table) {
            table.clear();
            table.destroy();
        }
        var flag = currentAccountType === "1" || currentAccountType === "4";
        //生成列表
        table = bt.datatable.init({
            elem: '#' + (flag ? currentAccountType === "4" ? 'poolInnerAccountTable' : 'innerAccountTable' : 'bankAccountTable'),
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': flag ? innerAccountUrl : bankAccountUrl
            },
            colReorder: false,
            selectCss: false,
            columns: flag ? bt.getInnerAccount() : bt.getBankAccount()
        });
        if (tableBind) {
            tableBind = false;
        }
    }

    form.on('radio(chooseAssignType)', function (data) {
        if (data.value == 1) {    /*账号选用户*/
            location.href = basePath + "/sys/accountGrant/list";
        } else {                /*用户选账号*/
            location.href = basePath + "/sys/accountGrant/listUser";
        }
    });

    $(document).on('click', '.assgin-user', function (event) {
        var data = table.row($(this).data('row')).data();
        var title;
        if (currentAccountType === "1" || currentAccountType === "4") {
            title = data.blnctrAcc + ' - ' + data.accName + ' -' + bt.lang('accountGrant.assginUser');
        } else {
            title = data.bankAcc + ' - ' + data.accName + ' - ' + bt.lang('accountGrant.assginUser');
        }
        parent.layer.open({
            type: 2,
            title: title,
            area: ['1024px', '650px'],
            content: basePath + "/sys/accountGrant/assignUser/accountId/" + data.id + "/accountType/" + currentAccountType + "/accountCorpId/" + data.corpId
        });
        event.stopPropagation();
    });


    //单位回调函数
    bt.callBackData = function (obj) {
        $("#poolType").val(obj.type);
    };
    var corp = bt.corp.init();
    var dicts = bt.dicts.init();
    var selectType = bt.selectType.init();
});

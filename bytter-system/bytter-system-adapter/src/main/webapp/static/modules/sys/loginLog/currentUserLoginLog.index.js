bt.use(['form', 'datatable', 'dicts', 'dateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[6, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/sys/loginLog/datatableByCurrentUser"
        },
        buttons: [{
            extend: "colvis",
            text: bt.lang('common.button.theadHide'),
            columns: ':not(:first):not(:last)'
        }
        ]
        , columns: [
            {data: "username", bSortable: true},
            {
                data: "reason", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.reason ? ('<span style="color:red;">' + bt.lang(full.reason) + '</span>') : bt.lang("sys.message.login.success");
                }
            },
            {data: "typeLang", bSortable: false},
            {
                data: "userAgent", bSortable: false,
                render: function (data, type, full, meta) {
                    // 处理 xss 攻击
                    return filterXSS(data);
                }
            },
            {data: "host", bSortable: true},
            {
                data: "loginDate", bSortable: true,
                render: function (data, type, full, meta) {
                    if (full.loginDate) {
                        return full.loginDate;
                    }
                    return "";
                }
            },
            {
                data: "LogoutDate", bSortable: true,
                render: function (data, type, full, meta) {
                    if (full.LogoutDate) {
                        return full.LogoutDate;
                    }
                    return "";
                }
            }

        ]
    });
});

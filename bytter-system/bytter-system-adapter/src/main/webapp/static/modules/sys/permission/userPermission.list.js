bt.use(['form', 'datatable', 'corp', 'mulselect','selectType','dateType','element'], function () {
    // 初始化需要的 bt 子模块
    $ = bt.jquery;
    form = bt.form();
    bt.corp.init();
    bt.mulselect.init();
    bt.selectType.init();
    bt.dateType.init();

    // 生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: false, // 固定列
        checkbox: false,
        order: [],
        // isInitData: false, // 初始化不加载数据
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            initQueryParams: function (params) {
                // 可以在这里设置默认查询参数
                return params;
            }
        },
        ajax: {
            'url': basePath + "/sys/user/getUserPermissionList"
        },
        columns: [
            {data: "loginName", bSortable: false, className: 'center'},
            {data: "username", bSortable: false, className: 'center'},
            {data: "corpCode", bSortable: false, className: 'center'},
            {data: "corpName", bSortable: false, className: 'center'},
            {data: "roleName", bSortable: false, className: 'center'},
            {data: "assignTime", bSortable: false, className: 'center'}
        ],
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/sys/user/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    });
});

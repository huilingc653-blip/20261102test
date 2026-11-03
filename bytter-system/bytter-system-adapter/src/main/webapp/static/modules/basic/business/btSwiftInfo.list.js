bt.use(['form', 'datatable', 'btImport', 'selectType','selectTable'], function () {
    $ = bt.jquery, form = bt.form();
    var selectType = bt.selectType.init();

    /**初始化国家下拉表格,组件:datatable;  begin*/
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/basic/business/btswiftinfo/datatable"
        },
        selectCss: false,
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "bankTypeName", bSortable: false},
            {data: "swiftCode", bSortable: true},
            {data: "localClearNo", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "nameZh", bSortable: true},
            {data: "cityNameZh", bSortable: true},
            {data: "bankAdd", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });
    /**下拉表格  end*/

    //所在国家/地区组件初始化
    var countryInit = bt.selectTable.init({
        elem: "#countryCodeTable",
        url: basePath + "/basic/business/syscountrycust/listSysCountryCust",
        searchInput: "#eqs_nameZh",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            var countryCode = "0";
            if (data) {
                countryCode = data.id;
                $('#eqs_nameZh').val(data.nameZh);
            } else {
                $('#eqs_nameZh').val('');
            }

            cityInit.get("eqs_cityNameZh").rebuild({
                url: basePath + "/basic/business/syscountrycust/listSysCountryArea/" + countryCode,
                columns: [
                    {data: "code",},
                    {data: "nameZh",},
                    {data: "nameEn",},
                ]
            });
        },
    })

    var countryCode = "0";

    //所在城市组件初始化
    var cityInit = bt.selectTable.init({
        elem: "#cityNameTable",
        url: basePath + "/basic/business/syscountrycust/listSysCountryArea/" + countryCode,
        searchInput: "#eqs_cityNameZh",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "code",},
            {data: "nameZh",},
            {data: "nameEn",},
        ],
        clickCallback: function (data, $tr) {
            if (data) {
                $('#eqs_cityNameZh').val(data.nameZh);
            } else {
                $('#eqs_cityNameZh').val('');
            }
        },
    })

    /**初始化导入组件,组件:btImport;  begin*/
    importExcel = function () {
        up = bt.btImport.init({
            code: "90000002",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/basic/business/btswiftinfo/import",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    }
    /**导入组件  end*/
});


/**
 * 新增
 */
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/basic/business/btswiftinfo/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/btswiftinfo/add",
        end: function () {
            table && table.draw();
        }
    });
}


/**
 * 查询
 * @param el
 */
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/basic/business/btswiftinfo/view/" + data.id
    });
}


/**
 * 更新
 * @param el
 */
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/basic/business/btswiftinfo/add?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/btswiftinfo/add?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}


/**
 * 删除
 * @param el
 */
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/basic/business/btswiftinfo/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

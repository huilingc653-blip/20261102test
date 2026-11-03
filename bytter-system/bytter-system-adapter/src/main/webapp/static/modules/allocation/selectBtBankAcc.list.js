bt.use(['form', 'layedit', 'selectType', 'datatable'], function () {
    var $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    //生成列表
    bt.datatable.initMultiTables({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        clickRowSelected: false,
        order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/allocation/bisaccgatherpolicy/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            },
            {
                text: bt.lang('common.button.submit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if (len < 1) {
                        layer.msg('请至少选择1条父账号');
                        return;
                    }
                    var ids = "";
                    var childIds = "";
                    var rowData = data.splice(0, len);
                    for (var i = 0; i < rowData.length; i++) {
                        if (ids == "") {
                            ids += rowData[i].id;
                        } else {
                            ids += "," + rowData[i].id;
                        }
                    }
                    var childData = bt.datatable.getJumpSelectedData("childTable");
                    var childLen = childData.length;
                    if (childLen < 1) {
                        layer.msg('请至少选择1条子账号');
                        return;
                    }
                    for (var i = 0; i < childData.length; i++) {
                        if (childIds == "") {
                            childIds += childData[i].id;
                        } else {
                            childIds += "," + childData[i].id;
                        }
                    }
                    var gatherPolicyId = $("#gatherPolicyId").val();//策略ID
                    // var prentAccId = $("#prentAccId").val();//父账号ID
                    var curId = $("#curId").val();//币别Id

                    parent.layer.confirm(bt.lang('common.message.confirm.submit'), function () {
                        $.post(basePath + "/allocation/bisaccgatherpolicy/saveChild", {
                            ids: ids,
                            gatherPolicyId: gatherPolicyId,
                            childIds: childIds,
                            curId: curId
                        }, function (result) {
                            if (result.success) {
                                location.reload();
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            }
                            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                        return false;
                    });
                }
            }
        ]
        , columns: [
            {
                data: "id",
                width: "28px",
                className: "center ckbx28 extraCol",
                orderable: false,render:function (data) {
                    return '<input type="radio" name="id" value="'+data+'" lay-ignore class="single-check">'
                }
            },
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "attributeName", bSortable: true},
            {data: "bifName", bSortable: true},
            {data: "natureName", bSortable: true}
        ],
        btInitComplete:function(){
            $("#appTable").on('click','.single-check',function () {
                var that = this
                $("#appTable").find('.selected').removeClass('selected');
                $(that).closest('tr').addClass('selected');
                $("#eqs_prentAccId").val(bt.datatable.getSelectData("#appTable")[0].id);
                getChildDate();
            })
        }
    });

    var childTable = null;
    //获取流水表格数据
    function getChildDate() {
        if (null == childTable) {
            childTable = bt.datatable.initMultiTables({
                    elem: "#childTable",
                    colReorder: true,
                    checkbox: true,
                    jumpPageSelect: true,
                    order: [[1, 'asc']],
                    searchForm: {
                        formElem: "#searchFormChild",
                        formFilter: "searchChild"
                    },
                    ajax: {
                        'url': basePath + "/allocation/bisaccgatherpolicy/childDatatable"
                    }
                    , columns: [
                        {data: "bankAcc", bSortable: false},
                        {data: "accName", bSortable: false},
                        {data: "curName", bSortable: false},
                        {data: "bankName", bSortable: false},
                        {data: "corpName", bSortable: false},
                        {data: "attributeName", bSortable: false},
                        {data: "bifName", bSortable: false},
                        {data: "natureName", bSortable: false}
                    ]
                });
        } else {
            bt.datatable.refreshTable("childTable");
        }
    }

});

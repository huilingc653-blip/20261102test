var datatable;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'bank','selectType','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/wage/niswage/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        otherExport:{ // 其他导出
            text:bt.lang('nis.button.exportSelect'), // 按钮文本
            callback:function(processing){ // 其他导出执行方法
                var data = datatable.rows('.selected').data();
                exports(data);
                processing(false)
            }
        },
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "wageNo", bSortable: true, className: 'center'},
            {data: "wageTitle", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "payerAcc", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "wages", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.wages);
                }
            },
            {data: "wageDay", bSortable: true, className: 'center'},
            {data: "wishPayday", bSortable: true, className: 'center'},
            {data: "addWord", bSortable: true},
            {data: "statusName", bSortable: false, className: 'center'},
            {
                data: "landingLimitResult", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("enterpriseLimit.landingResult", full.landingLimitResult);
                }
            },
            // {data: "status", bSortable: true, className: 'center',
            //     render: function (data, type, full, meta) {
            //         return bt.dict("nisBillhead.status", full.status);
            //     }
            // },
            {
                data: "successNum", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDetail(this,0)">' + full.successNum + '</a>';
                    return html;
                }
            },
            {
                data: "successWages", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.successWages);
                }
            },
            {
                data: "failNum", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDetail(this,2)">' + full.failNum + '</a>';
                    return html;
                }
            },
            {
                data: "failWages", bSortable: false, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.failWages);
                }
            },
            {
                data: "waitNum", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDetail(this,1)">' + full.waitNum + '</a>';
                    return html;
                }
            },
            // {data:"voucherStat",bSortable:true,className:'center',
            //     render: function ( data, type, full, meta ) {
            //     	return bt.dict("bisExc.voucherStat",full.voucherStat);
            //     }
            // },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = "";
                    // if ((full.status >= 11 && full.status < 95) && $("#loginName").val() === full.createBy) {
                    //     html += '<a class="" href="#" data-row="' + meta.row + '" onclick="withdraw(this)">' + bt.lang("common.button.withdraw") + '</a><span class="text-explode">|</span>';
                    // }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/wage/niswage/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        },
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '6',
                    dataName: 'wages'
                }
            ]
        },
        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            if(aData.failNum > 0){
                //动态设置class属性
                $(nRow).attr("class","col-error")
            }
        }
    });
    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });

    /**
     * 撤回
     * @param el
     */
    withdraw = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            $.post(basePath + "/payment/bpm/task/batchRevoke", {ids: data.id}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        }
    }
});

/**
 * 导入功能
 */
exports = function (data) {
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids="";
    var rowData = data.splice(0,len);
    for(var i=0;i<rowData.length;i++){
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    var url = basePath + "/wage/niswage/exportDtl?ids=" + ids + "&opType=listQuery";
    window.location.href = url;
};

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/wage/niswage/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/wage/niswage/view/" + data.id
    });
}

/**
 * 查看
 * @param id
 */
 function viewDetail(el,opType) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/wage/niswagedetail/view/" + data.id + "/"+opType);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/wage/niswagedetail/view/" + data.id + "/"+opType
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/wage/niswage/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/wage/niswage/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/wage/niswage/delete/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量删除
function deleteBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/wage/niswage/batchDelete", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量提交
function submitBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        $.post(basePath + "/wage/niswage/batchSubmit", {ids: ids}, function (result) {
            datatable.draw();
            showBatchSubmitResult(result);
        }, 'JSON');
    });
}

function showBatchSubmitResult(result) {
    if (paymentLimitAlert.isBatchResult(result)) {
        parent.layer.alert(paymentLimitAlert.batchContent(result), paymentLimitAlert.batchAlertOptions());
        return;
    }
    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
}

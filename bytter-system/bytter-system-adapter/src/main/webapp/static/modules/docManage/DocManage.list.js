var datatable;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType','element'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });
    bt.reset  = function(){
        bt.dicts.get('status').attr({'value':'0,-1,11,95'})
    }

        //客商选择
        bt.bankinfoCallback = function (data, dom) {
//            $("#payerBankNo").val(data.bankCode);
//            $("#payerBankName").val(data.bankName);
        };

    importExcel = function () {
        up = bt.btImport.init({
            code: "10000063",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/accountReceivable/docManage/saveImport",
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

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[1, 'asc'],[13, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/accountReceivable/docManage/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('account:receivable:doc:manage:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
//             {
//                hide: bt.isnotResource('account:receivable:doc:manage:apply'),
//                text: bt.lang('common.button.batchSubmit'),
//                action: function (e, dt, node, config) {
//                    var data = dt.rows('.selected').data();
//                    submitBatch(data);
//                }
//            },
            {
                hide: bt.isnotResource('account:receivable:doc:manage:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            },
            {
                hide: bt.isnotResource('account:receivable:doc:manage:apply'),
                text: bt.lang('批量导入'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "code", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "batchBusTypeName", bSortable: true},
            {
                data: "source", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("writeOff.docSource", full.source);
                }
            },
            {
                data: "voucherType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("account.receivable.doc.voucherType", full.voucherType);
                }
            },
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("docManage.status", full.status);
                }
            },
            {data: "receiptDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "agingDate", bSortable: true},
            {
                data: "receivableMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.receivableMoney);
                }
            },
            {data: "curName", bSortable: true},
            {data: "externalName", bSortable: true},
             {
                data: "externalType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("external.type", full.externalType);
                }
            },
            {data: "contractCode", bSortable: true},

            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                if (('0'==full.status || '-1'==full.status) && (full.reasonStatus != '105')) {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                }
                if ('95'== full.status || full.reasonStatus == '105') {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="addChange(this)">' + bt.lang("变更") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="invalidated(this)">' + bt.lang("作废") + '</a><span class="text-explode">|</span>';
                }

                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                return html;
            }
            }
        ]
    });
     $(".buttons-colvis").hide();
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
    $('#hide-search').hide()
    listNisCorpBusVOByCorpIdAndCdSign();
});



function showMoreBtn(){
   if($('#hide-search')[0].style.display!='none'){
        $('.ui-more-btn ').text('更多筛选')
       $('.ui-arrow-down').attr('src',basePath+'/static/images/table/icon_arrow_down.svg')
       $('.ui-more-btn ').removeClass('ui-more-btn-active')
       $('#hide-search').hide()
   }else{
        $('.ui-more-btn ').text('收起筛选')
       $('.ui-arrow-down').attr('src',basePath+'/static/images/table/icon_arrow_up.png')
       $('.ui-more-btn ').addClass('ui-more-btn-active')
       $('#hide-search').show()
   }
}

bt.corpCallback = function (id) {
    // 重新设置业务类别
    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=0&corpId=" + id;
    bt.selectType.get("eqs_batchBusType").attr("data-url", busTypeUrl);
    form.render()
}

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('应收单新增'), basePath + "/accountReceivable/docManage/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/accountReceivable/docManage/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}
//变更
function addChange(el) {
    parent.layer.setTitle("变更应收单")
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        parent.layer.open({
            type: 2,
            hideTitleBorder: true,
            hideMaxBtn: true,
            area: ['600px', '350px'],
            maxmin: true,
            content: basePath + "/accountReceivable/docManage/invalidated?id=" + data.id+'&type=2',
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
    parent.layer.initTitle();
}

//作废
function invalidated(el) {
    parent.layer.setTitle("作废应收单")
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        parent.layer.open({
            type: 2,
            hideTitleBorder: true,
            hideMaxBtn: true,
            area: ['600px', '350px'],
            maxmin: true,
            content: basePath + "/accountReceivable/docManage/invalidated?id=" + data.id+'&type=3',
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
    parent.layer.initTitle();
}
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('应收单详情'), basePath + "/accountReceivable/docManage/view?id=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/accountReceivable/docManage/view?id=" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('应收单编辑'), basePath + "/accountReceivable/docManage/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            title: '应收单编辑',
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/accountReceivable/docManage/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el,status) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm('删除应收单后无法恢复，是否确认删除？',function(){
        $.post(basePath + "/accountReceivable/docManage/batchDelete",{ids: data.id}, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });

    // parent.layer.customConfirmV2('删除应收单后无法恢复，是否确认删除？', {icon: 2}, ['取消','确认删除'], function(index){
    // parent.layer.close(index);
    // }, function(index){
    //     $.post(basePath + "/accountReceivable/docManage/batchDelete",{ids: data.id}, function (result) {
    //         datatable && datatable.draw();
    //         parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
    //     }, 'JSON');
    //     }, 'listDocManageCustomStyle', '删除应收单','warning', true);
         parent.layer.initTitle();
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
        $.post(basePath + "/accountReceivable/docManage/batchDelete", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });

    // parent.layer.customConfirmV2('批量删除应收单后无法恢复，是否确认删除？', {icon: 2}, ['取消','确认删除'], function(index){
    //     parent.layer.close(index);
    // }, function(index){
    //     $.post(basePath + "/accountReceivable/docManage/batchDelete", {ids: ids}, function (result) {
    //         datatable.draw();
    //         parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
    //     }, 'JSON');
    // }, 'listDocManageCustomStyle', '批量删除应收单','warning', true);

 parent.layer.initTitle();
}


//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}
    listNisCorpBusVOByCorpIdAndCdSign =function (){
        var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=0";
        bt.selectType.get("eqs_batchBusType").attr("data-url", busTypeUrl);
    }

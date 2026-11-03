var table;
bt.use(['datatable', 'jquery', 'corp', 'bankInfo','btImport','element','dicts','mulselect','selectType'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;
    var element = bt.element();
    var dataform = bt.dataform;
    bt.dicts.init();
    var $ = bt.jquery,form = bt.form();
    var corp = bt.corp.init();
    bt.bankInfo.init();
    bt.mulselect.init();
    bt.selectType.init();
    /*element.on('tab(externalCorpTabBrief)', function(data){
        var status = $(this).hasClass('tab-type') ? 1 : 0;
        $("input[name='isCommon']").val(status);
        showOrHiddenCorp($("input[name='isCommon']").val());
        table.draw();
    });*/

    importExcel = function () {
        up = bt.btImport.init({
            code: "40000002",
            width: "100%",
            height: "100%",
            headRows: 1,
            titleRows: 0,
            page: false,
            success: function (head,data,select) {
                setTimeout(function () {
                    bt.loading.show();
                })
                $.ajax({
                    type:"POST",
                    url: basePath + "/sys/externalCorp/saveImport",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(up.getSelectData()),
                    success:function(result){
                        bt.loading.hide();
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                    }
                });
            }
        });
    }

    function escapeBelongCorpHtml(str) {
        return String(str)
            .replace(/&/g, '&amp;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;');
    }

    function formatBelongCorp(full) {
        if (!full || !full.corpName) {
            return "";
        }
        var names = String(full.corpName).split(',');
        var codes = full.corpCode ? String(full.corpCode).split(',') : [];
        var parts = [];
        for (var i = 0; i < names.length; i++) {
            var name = $.trim(names[i]);
            if (!name) {
                continue;
            }
            var code = codes[i] ? $.trim(codes[i]) : '';
            parts.push(code ? ('(' + code + ')' + name) : name);
        }
        return parts.join(',');
    }

    function renderBelongCorp(data, type, full) {
        var text = formatBelongCorp(full);
        if (!text) {
            return "";
        }
        if (type !== 'display') {
            return text;
        }
        // 完整内容进单元格，省略号随列宽自适应；拉宽列即可看到更多，不再写死 max-width / 截断前两项
        return '<div class="ellipsis" title="' + escapeBelongCorpHtml(text) + '">'
            + escapeBelongCorpHtml(text) + '</div>';
    }

     table = bt.datatable.init({
        elem: "#externalCorpTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1,'asc']],
        searchForm: {
        	formElem: "#searchForm",
        	formFilter: "search"
        },
        ajax: {
            'url': basePath + "/sys/externalCorp/datatable.json",
        },
        buttons: [
            {
                text: bt.lang("common.button.add"),
                action: function (e, dt, node, config) {
                    add();
                }
            },{
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            },{
                text: bt.lang("common.button.batchSubmit"),
                action: function (e, dt, node, config) {
                    //获取选中行数据1
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(!len){
                        layer.msg(bt.lang('common.message.confirm.selectData'));
                        return;
                    }
                    var rowData = data.splice(0, len);
                    var ids = [];
                    for (var i = 0; i < rowData.length; i++) {
                        ids.push(rowData[i].id);
                    }
                    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
                        $.post(basePath + "/sys/externalCorp/batchSubmit", {externalIds: ids}, function (result) {
                            table.draw();
                            layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });

                }
            }, {
                text: bt.lang("common.button.batchDelete"),
                action: function (e, dt, node, config) {
                    //获取选中行数据
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(!len){
                        layer.msg(bt.lang('common.message.confirm.selectData'));
                        return;
                    }
                    var rowData = data.splice(0, len);
                    var ids = [];
                    for (var i = 0; i < rowData.length; i++) {
                        ids.push(rowData[i].id);
                    }
                    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
                        $.post(basePath + "/sys/externalCorp/delete", {externalIds: ids}, function (result) {
                            table.draw();
                            layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }, 'JSON');
                    });
                }
            }
        ],
        columns: [
            {data: "code",bSortable:true},
            {data: "name",bSortable:true},
            {data: "abbreviate", orderable: false},
            {data: "nameEn", orderable: false},
            {data: "isNativeStr", orderable: false,},
            {data: "corpName", orderable: false, width: "240px", render: function (data, type, full, meta) {
                    return renderBelongCorp(data, type, full);
                }},
            {data: "sourceSystem",bSortable:true},
            {data: "statusString",bSortable:true},
            {data: "externalTypeStr",bSortable:true},
            {data: "supplierName",bSortable:true,
                render:function (data,type,full,meta) {
                    if (full.externalType == 1 || full.externalType == 2){
                        return full.supplierName;
                    }else{
                        return "";
                    }
                }
            },
            {data: "bwTypeName",bSortable:true},
            {data: "socCode",bSortable:true},
            {data: "proName",bSortable:true},
            {data: "auditStatusName",bSortable:true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if ((full.auditStatus !== 11)) {
                        html += '<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        html += '&nbsp;<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
//                        if (bt.isResource('netbank:allocation:typeAsk:apply')) {
//                        }
                    html += '&nbsp;<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
         // summary: {
         //     pageSum: true,
         //     allSum: false,
         //     selectSum: true,
         //     format: {
         //         decimal: 2
         //     }
         // },
         // 分批导出全部符合列表查询条件的数据
         customExportAll: function (params, processing, data) {
             var exportUrl = basePath + "/sys/externalCorp/exportAll";
             openExportExcel(exportUrl, data);
             processing(false);
         }
    });

    //showOrHiddenCorp(1);

    function add() {
        var url = basePath + "/sys/externalCorp/addOrUpdate?isCommon="+$("input[name='isCommon']").val();
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.add.data'), url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: url,
            end: function () {
                datatable && datatable.draw();
            }
        })
    }

    view = function (el) {
        var data = layui.jquery("#externalCorpTable").DataTable().row(layui.jquery(el).data("row")).data();
        var url = basePath + "/sys/externalCorp/view?id=" + data.id + "&corpId=" + data.corpId;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), url);

        }
    }

    update = function (el) {
        var data = layui.jquery("#externalCorpTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            var url = basePath + "/sys/externalCorp/addOrUpdate?id=" + data.id + "&corpId=" + data.corpId;
            if (bt.navtab) {
                bt.navtab.addNavtab(bt.lang('common.update.data'), url);
                return;
            }
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: url,
                end: function () {
                    datatable && datatable.draw();
                }
            });
        }
    }

    deleteColumn = function (el) {
        var data = layui.jquery("#externalCorpTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            var ids = [data.id];
            parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
                $.post(basePath + "/sys/externalCorp/delete", {externalIds: ids}, function (result) {
                    table && table.draw();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            });
        }
    }

    /*function showOrHiddenCorp(value) {
        /!*Tab为共享单位时,隐藏单位列*!/
        var column = table.column(5);
        if (value == 1){
            column.visible(false);
            $(".inlineCorp").attr("style", "display:none");
        }else{
            column.visible(true);
            $(".inlineCorp").attr("style", "display:inlineblock");
        }
    }*/
});

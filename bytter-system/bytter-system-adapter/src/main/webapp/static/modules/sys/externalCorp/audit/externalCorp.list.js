bt.use(['datatable', 'jquery', 'corp', 'bankInfo','btImport','element','dicts','mulselect','selectType'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;
    var element = bt.element();
    var dataform = bt.dataform;
    bt.dicts.init();
    var $ = bt.jquery,form = bt.form();
    var corp = bt.corp.init();
    bt.mulselect.init();
    bt.bankInfo.init();
    bt.selectType.init();

    /*element.on('tab(externalCorpTabBrief)', function(data){
        status = this.getAttribute('data-status');
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
            success: function (head,data,select) {
                $.ajax({
                    type:"POST",
                    url: basePath + "/sys/externalCorp/saveImport",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(up.getSelectData()),
                    success:function(result){
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                    }
                });
            }
        });
    }

    var table = bt.datatable.init({
        elem: "#externalCorpTable",
        ignoreButtons:['colvis','output','print'],
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1,'asc']],
        searchForm: {
        	formElem: "#searchForm",
        	formFilter: "search"
        },
        ajax: {
            'url': basePath + "/sys/externalCorp/audit/datatable.json",
        },
        buttons: [
            {
                text:  bt.lang("common.button.batchCheck"),
                action: function (e, dt, node, config) {
                    //获取选中行数据
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(!len){
                        layer.msg(bt.lang('common.message.confirm.selectData'));
                        return;
                    }
                    var rowData = data.splice(0, len);
                    var ids = "";
                    for (var i = 0; i < rowData.length; i++) {
                        ids = ids+rowData[i].id+",";
                    }
                    parent.layer.confirm(bt.lang('common.message.confirm.batchCheck'), function () {
                        batchAudit(ids, 95, $('textarea[name="auditMessage"]').val());
                    });
                }
            },{
                text:  bt.lang("common.button.batchBack"),
                action: function (e, dt, node, config) {
                    var auditMessage = $('textarea[name="auditMessage"]').val();
                    if(!auditMessage){
                        parent.layer.alert(bt.lang("bpm.alert.pleaseInputApproveComment"),{icon:2});
                        return;
                    }
                    //获取选中行数据
                    var data = dt.rows('.selected').data();
                    var len = data.length;
                    if(!len){
                        layer.msg(bt.lang('common.message.confirm.selectData'));
                        return;
                    }
                    var rowData = data.splice(0, len);
                    var ids = "";
                    for (var i = 0; i < rowData.length; i++) {
                        ids = ids+rowData[i].id+",";
                    }
                    parent.layer.confirm(bt.lang("common.message.confirm.batchBack"), function () {
                        batchAudit(ids, -1, $('textarea[name="auditMessage"]').val());
                    });
                }
            }
        ],
        columns: [
            {data: "code",bSortable:true},
            {data: "name",bSortable:true},
            {data: "abbreviate", orderable: false},
            {data: "nameEn", orderable: false},
            {data: "isNative", orderable: false,
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isNative);
                }
            },
            {data: "corpName", orderable: false,render:function (data, type, full, meta) {
                    if(!full.corpName){
                        return "";
                    }
                    return '<div style="max-width:200px" class="ellipsis" title="' + full.corpName + '">' + full.corpName + '</div>'
                }},
            {data: "sourceSystem",bSortable:true},
            {data: "status",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("bankAcc.validSign", full.status);
                }
            },
            {data: "externalType",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("external.type", full.externalType);
                }
            },
            {data: "supplierName",bSortable:true,
                render:function (data,type,full,meta) {
                    if (full.externalType == 1 || full.externalType == 2){
                        return full.supplierName;
                    }else{
                        return "";
                    }
                }
            },
            {data: "bwType",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("SYS_BUS_ENTITY_TYPE", full.bwType);
                }
            },
            {data: "socCode",bSortable:true},
            {data: "proName",bSortable:true},
            {data: "auditStatus",bSortable:true,
                render:function (data,type,full,meta) {
                    return bt.dict("externalCorp.auditStatus", full.auditStatus);
                }
            },
            {data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (full.auditStatus === 11) {
                        html += '&nbsp;<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="audit(this)">' + bt.lang("common.button.check") + '</a>';
                    }
                    html += '&nbsp;<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
    });

    //showOrHiddenCorp(1);


    view = function (el) {
        var data = layui.jquery("#externalCorpTable").DataTable().row(layui.jquery(el).data("row")).data();
        var url = basePath + "/sys/externalCorp/view?id=" + data.id + "&corpId=" + data.corpId + "&actionType=viewWithAuditLog";
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), url);

        }
    }

    audit = function (el) {
        var data = layui.jquery("#externalCorpTable").DataTable().row(layui.jquery(el).data("row")).data();
        var url = basePath + "/sys/externalCorp/view?id=" + data.id + "&corpId=" + data.corpId + "&actionType=audit";
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.check'), url);

        }
    }


    function showOrHiddenCorp(value) {
        /*Tab为共享单位时,隐藏单位列*/
        var column = table.column(5);
        if (value == 1){
            column.visible(false);
            $(".inlineCorp").attr("style", "display:none");
        }else{
            column.visible(true);
            $(".inlineCorp").attr("style", "display:inlineblock");
        }
    }

    /**
     * 批量审核
     * @param target
     * @returns {boolean}
     */

    function batchAudit(ids, target, message) {
        $.post(basePath + "/sys/externalCorp/audit", {
            ids: ids,
            target: target,
            auditMessage: message
        }, function (result) {
            table.draw();
            layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
        return false;
    }
});

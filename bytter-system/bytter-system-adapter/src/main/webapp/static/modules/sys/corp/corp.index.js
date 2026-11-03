bt.use(['jquery', 'form', 'element', 'treeGrid', 'btImport', 'corp'], function () {
    $ = bt.jquery, form = bt.form();
    var corp = bt.corp.init();

    function reloadGrid(treeGridTable) {
        treeGridTable.grid("resetSelection"); // 重置选中项
        treeGridTable.trigger("reloadGrid");
    }
    // 反选
    // var selectedId = '';
    // $("#corpTable").on("click","tbody tr",function () {
    //     if(selectedId === $(this).attr("id") && treeGrid){
    //         setTimeout(function () {
    //             treeGrid.grid("resetSelection");
    //         })
    //         selectedId = '';
    //     }else{
    //         selectedId = $(this).attr("id")
    //     }
    // })
    var addFunc = function (rowData,isAddChild) {
        var id = '';
        if(isAddChild){
            id = (rowData && rowData.id) ? rowData.id : '';
        }

        if (rowData && rowData.status == 0) {
            id = "";
        }
        // if(bt.navtab){
        //      bt.navtab.addNavtab('网点/单位管理新增',basePath + "/sys/corp/view/?parentId=" + id)
        // }
        parent.layer.open({
            title: '网点/单位管理新增',
            type: 2,
            area: ['1000px', '680px'],
            maxmin: true,
            content: basePath + "/sys/corp/view/?parentId=" + id,
            end: function () {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                reloadGrid(treeGrid)
            }
        });
    }

    var editFunc = function (rowData) {
        var id = rowData.id ? rowData.id : '';
        parent.layer.open({
            type: 2,
            area: ['1000px', '680px'],
            maxmin: true,
            content: basePath + "/sys/corp/view/?id=" + id,
            end: function (flag) {
                if(flag){
                    reloadGrid(treeGrid)
                }
                // treeGrid.trigger("reloadGrid");
            }
        });
    }
    var delFunc = function (rowData) {
        var id = rowData.id ? rowData.id : '';
        // if (rowData.status == 0) {
        //     return;
        // }
        // var corpUserFlag;
        // var corpChildFlag;
        // $.ajaxSettings.async = false;
        // $.get(basePath + "/sys/corp/judgeCorp/" + id, function (resultMap) {
        //     corpUserFlag = resultMap.hasUser;
        //     corpChildFlag = resultMap.hasChildCorp;
        // }, 'JSON');
        // $.ajaxSettings.async = true;
        // if (corpChildFlag) {
        //     parent.layer.alert(bt.lang('sys.corp.has.child.delete'), {icon: 2});
        // } else {
        //     if (corpUserFlag) {
        //         parent.layer.alert(bt.lang('sys.corp.has.user.delete'), {icon: 2});
        //     } else {
                parent.layer.confirm(bt.lang("sys.sysCorp.delete"), {icon: 3}, function () {
                    bt.loading.show();
                    $.get(basePath + "/sys/corp/delete/" + id, function (result) {
                        treeGrid.trigger("reloadGrid");
                        if(result.obj) {
                            var tableHtml = "<div style='padding-top: 1%;padding-right: 1%;padding-bottom: 1%;padding-left: 1%;'>";
                            for (var i = 0; i < result.obj.length; i++){
                                var corpInfo=result.obj[i];
                                tableHtml += "<br>"+bt.lang("table.sysCorp.name")+" : ("+corpInfo.corpCode+")"+corpInfo.corpName+"<br> <table class='layui-table'>";
                                for (var j = 0; j < corpInfo.sysList.length; j++) {
                                    var sysInfo=corpInfo.sysList[j];
                                    tableHtml += "<tr>";
                                    tableHtml += "<td rowspan='" +sysInfo.size+ "' width='20%'>"+sysInfo.sysName+"</td>";
                                    for(var k = 0; k < sysInfo.busList.length; k++){
                                        var busInfo=sysInfo.busList[k];
                                        if(k>0){
                                            tableHtml += "<tr>";
                                        }
                                        tableHtml += "<td rowspan='" +busInfo.size+ "' width='20%'>"+busInfo.busName+"</td>";
                                        for(var l = 0; l < busInfo.errorMsgs.length; l++){
                                            if(l>0){
                                                tableHtml += "<tr>";
                                            }
                                            tableHtml += "<td width='60%'>"+busInfo.errorMsgs[l]+"</td>";
                                            if(l>0){
                                                tableHtml += "</tr>";
                                            }
                                        }
                                        if(k>0){
                                            tableHtml += "</tr>";
                                        }
                                    }
                                    tableHtml += "</tr>";
                                }
                                tableHtml += "</table>";
                            }
                            tableHtml += "</div>";
                            parent.layer.open({
                                type: 1,
                                title: "<font color=\"#FF0000\">"+result.msg+"</font>",
                                maxmin: true,
                                area : [ '60%', '80%' ],
                                btn: [bt.lang("common.button.close")],
                                content: tableHtml
                            });
                        }else{
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        }
                        bt.loading.hide();
                    }, 'JSON');
                });
            // }
        // }
    }

    /**
     * 导入
     */
    var importFunc = function () {
        up = bt.btImport.init({
            code: "90000001",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/sys/corp/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        if (result.success) {
                            reloadGrid(treeGrid)
                            // treeGrid.trigger("reloadGrid");
                        } else {
                            parent.layer.alert(result.msg, {icon: 2});
                        }
                    }
                });
            }
        });
    }

    /**
     * 导出
     */
    var downloadFunc = function () {
        var corpIds = corp.get("ins_corpId").val().trim();
        var status = $('#status').val().trim();
        var query = "";
        if (corpIds!=null && corpIds.length>0){
            // console.log("corpIds==>" + corpIds + ",size==>" + corpIds.length);
            query += "id="+corpIds;
        }
        if (status!=null && status.length>0){
            // console.log("status==>" + status + ",size==>" + status.length);
            query = query!==""?query+"&":query;
            query += "status="+status;
        }
        query = query !== "" ? "?" + query : query;
        console.log("query==>" + query);
        window.location.href = basePath + "/sys/corp/downloadCorp" + query;
    }

    var treeGrid = bt.treeGrid.init({
        elem: "#corpTable",
        url: basePath + "/sys/corp/tree.json",
        ExpandColumn: "name",
        colModel: [
            {
                name: "name",
                label: bt.lang("table.sysCorp.name"),
                formatter: function (cellvalue, options, rowObject) {
                    return "(" + rowObject.code + ")" + rowObject.name;
                }
            },
            {
                name: "url",
                label: bt.lang("table.sysCorp.type"),
                width: 30,
                align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return rowObject.type == 0 ? '<span style="color:red;">' + bt.lang("common.button.center") + '</span>' : bt.lang("common.button.resetAcceptor");
                }
            },
            {name: "netName", label: bt.lang("table.sysCorp.netName"), width: 50, align: "center"},
            {name: "unitAttributeName", label: bt.lang("sys.sysCorp.unitAttribute"), width: 30, align: "center"},
            {name: "unitCmName", label: bt.lang("sys.sysCorp.unitCm"), width: 30},
            {name: "curName", label: bt.lang("table.sysCorp.curId"), width: 30, align: "center"},
            {
                name: "status",
                label: bt.lang('table.sysCorp.status'),
                width: 20,
                align: "center",
                formatter: function (cellvalue, options, rowObject) {
                    return cellvalue == 1 ? bt.lang("common.button.enable") : '<span style="color:#cacaca;">' + bt.lang("common.button.taboo") + '</span>';
                }
            },
            {name: "sort", label: bt.lang("common.message.sort"), width: 20, editable: true, align: "center"},
            {
                label: bt.lang('common.table.thead.operation'),
                width: 30,
                formatter: function (cellvalue, options, rowObject) {
                    var html = '';
                    if (rowObject.status === 1) {
                        html += '<a href="javascript:void(0)" row="' + rowObject.id + '" class="add-sub">' + bt.lang("common.link.add") + '</a>';
                    }
                    return html;
                }
            },
        ],
        cellurl: basePath + '/sys/corp/updateSort',
        navPosition: "top",
        addFunc: addFunc,
        editFunc: editFunc,
        delFunc: delFunc,
        afterSaveCell: function (rowid, cellname, value, iRow, iCol) {
            // treeGrid.trigger("reloadGrid");
            reloadGrid(treeGrid)
        }
    });
    treeGrid.on("click", ".add-sub", function () {
        var data = treeGrid.getOriginalData($(this).attr("row"));
        addFunc(data,true);
    });
    treeGrid.grid("customButton", {
        title: "导入",
        buttonicon: "fa-upload",
        onClickButton: importFunc
    })
    treeGrid.grid("customButton", {
        title: "导出",
        buttonicon: "fa-download",
        onClickButton: downloadFunc
    })


    $("#searchBtn").click(function () {
        var corpIds = corp.get("ins_corpId").val();
        var status = $('#status').val();
        $("#corpTable").jqGrid('setGridParam', {
            postData: {
                'id': corpIds,
                'status': status
            }
        }).trigger("reloadGrid").trigger("resetSelection"); //重新载入
    });

    $("#unitMigration").click(function () {
        layer.open({
            type: 2,
            area: ['1024px', '650px'],
            maxmin: false,
            title: "单位迁移",
            content: basePath + "/sys/user/assignCorps"
        });
    });

    $("#queryReset").click(function () {
        corp.get("ins_corpId").val("");
        corp.get("ins_corpId").render();

        $("#status").val("");
        form.render('select');
    });
});

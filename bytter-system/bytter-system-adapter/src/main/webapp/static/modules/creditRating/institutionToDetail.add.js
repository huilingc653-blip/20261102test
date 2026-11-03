var dicts, dateTypes, warnDataTable, moneyType, bankAccAtt, bankAccNature, bttable, bookBttable, visitBtTable, region,
    form, rateType;
bt.use(['form', 'element', 'datatable', 'select', 'checkboxs', 'btdate', 'selectType', 'moneyType', 'corp', 'dicts', 'dateType'
    , 'mulCorp', 'mulselect', 'mulBankAccInfo', 'mulAccInfo', 'domesticAndOverseasBank', 'bttable', 'btUpload', 'region', 'btImport', 'rateType'], function () {
    var $ = bt.jquery;
    form = bt.form();
    selectType = bt.selectType.init();
    dicts = bt.dicts.init();
    moneyType = bt.moneyType.init();
    dateTypes = bt.dateType.init();//时间
    region = bt.region.init();
    rateType = bt.rateType.getFun();

    //一些事件触发
    var oldOrgKey ='';
    element.on('tab(institution)', function (data) {
        if ("evaluation" == $(data.elem.context).attr('lay-id')) {
            var currentOrgKey = $("#org-box").find('> div:not(.layui-hide)').find('input').val();

            if(oldOrgKey != currentOrgKey){
                //
                updateOrgEvaluateInfo(currentOrgKey);

                oldOrgKey = currentOrgKey;
            }
        }
    });


    var loadingHmtl = '<tr><td colspan="5">加载中...</td></tr>'
    function updateOrgEvaluateInfo(orgKey){
        $("#infoTbody").html(loadingHmtl);
        $.post(basePath + "/credit/crTaskOrg/queryByOrgName",{"orgName":orgKey},function (result) {
            // infoTbody
            // parent.layer.msg(result.msg, {icon: result.data.length > 0 ? 1 : 2});
            if (result.data.length > 0) {
                updateInfoTbody(result.data)
            }
        }, 'JSON');
    }

    function updateInfoTbody(list){
        var html = '';
        if(list && list.length){
            $.each(list,function (i,item){
                html += '<tr>'
                html += '<td>'+(item.orgAvg||'')+'</td>'
                html += '<td>'+(item.evaluationLevel||'')+'</td>'
                html += '<td>'+(bt.date.format(item.evaluateStartDate, "yyyy-MM-dd hh:mm:ss")||'')+'</td>'
                html += '<td>'+(bt.date.format(item.evaluateEndDate, "yyyy-MM-dd hh:mm:ss")||'')+'</td>'
                html += '<td><a class="review-detail" href="#" data-id="'+item.id+'" data-orgid="'+item.orgId+'">明细</a></td>'
                html += '</tr>'
            })
        }else{
            html = '<tr><td colspan="5">暂时无数据</td></tr>'
        }
        $("#infoTbody").html(html);
    }

    // 明细
    $("#infoTbody").on('click','.review-detail',function (data){
        //data.taskNo
        // debugger;
        // var taskNo = $(this).data('task-no');
        reviewDetailFunction($(this).data());
    })


    $("#close").click(function () {
        bt.navtab.closeCurrent();
    });

    form.verify({
        emailFex: function (value) {
            var vals = value.split(";");
            for (var i = 0; i < vals.length; i++) {
                if (vals[i] && !/^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/.test(vals[i])) {
                    return bt.lang('sys.form.verify.emailError')//'邮箱格式不正确';
                }
            }
        }
    });

    // form.verify({
    //     tableData: function (value, item) {
    //         var value = $.trim(value);
    //         if(value == ''){
    //             return;
    //         }
    //         var tableElem = $(item).siblings('.select-tablecss').find('.dataTables_scrollBody table');
    //         var id = tableElem.attr('id');
    //         var table = new $.fn.dataTable.Api( '#'+id );
    //         var columns = table.columns().header();
    //         columns = columns.length;
    //         var flag = false;
    //         for (var i = 0; i < columns; i++) {
    //             var index = table.column(i).data().indexOf(value);
    //             if (index !== -1) {
    //                 flag = true;
    //                 break
    //             }
    //         }
    //         if (!flag) {
    //             return bt.lang('sys.form.verify.mustBeInTable') //"输入的值不在下拉表格中"
    //         }
    //     }
    // })


    var domesticAndOverseasBank = bt.domesticAndOverseasBank.init();// 境内境外联行号
    bt.domesticAndOverseasBankBeforeClick = function () {
        domesticAndOverseasBank.set({
            "joinBankType": true,
        });
        return true;
    }

    // var gid = $("#annexName").val();
    var uploader = bt.btUpload.init({
        elem: "#uploader",
        auto: false,
        gid: ''
    });
    /**
     *  生成股权结构列表
     */
    bttable = bt.bttable.init({
        elem: "#ownershipStructure",
        mulselect: false,
        checkbox: false,
        'url': basePath + "/credit/ownershipStructure/detailDatatable?ids=" + $("#ssIds").val(),
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    ownershipStructureAdd();
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcelToOwnershipStructure();

                }
            },
            {
                text: bt.lang('common.button.exports'),
                action: function (e, dt, node, config) {
                    exportExcelToOwnershipStructure();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {
                data: "rowNum", className: "center", render: function (data, row) {
                    return data.rowNum;
                }
            },
            {
                data: "investor", className: "center", render: function (data, row) {
                    return data.investor;
                }
            },
            {
                data: "financingWay", className: "center", render: function (data, row) {
                    // return data.financingWayName;
                    return bt.dict("cms.equity.financingWay", data.financingWay);
                }
            },
            {
                data: "investmentMethod", className: "center", render: function (data , row) {
                    return bt.dict("cms.equity.invWay", data.investmentMethod);

                }
            },
            {
                data: "contributionAmount", className: "textRight", render: function (data) {
                    return bt.money.format(data.contributionAmount);
                }
            },
            {
                data: "shareholdingRatio", className: "center", render: function (data, row) {
                    return rateType.format(data.shareholdingRatio, 'percent', 2);
                }
            },
            {
                data: "assetName", className: "center", render: function (data, row) {
                    return data.assetName;
                }
            },
            {
                data: "rmk", className: "center", render: function (data, row) {
                    return data.rmk;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" id="' + data.id + '" onclick="updateOwnershipStructure(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="' + data.id + '" onclick="ownershipStructureDel(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="' + data.id + '" onclick="ownershipStructureView(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });

    /**
     *  生成 联系人通讯录 列表
     */
    bookBttable = bt.bttable.init({
        elem: "#contactAddressBook",
        mulselect: false,
        checkbox: false,
        'url': basePath + "/credit/contactAddressBook/detailDatatable?ids=" + $("#bookIds").val(),
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    addBook();
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    // importExcel();
                    importExcelToContactAddressBook();
                }
            },
            {
                text: bt.lang('common.button.exports'),
                action: function (e, dt, node, config) {
                    exportExcelToContactAddressBook();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {
                data: "rowNum", className: "center", render: function (data, row) {
                    return data.rowNum;
                }
            },
            {
                data: "investor", className: "center", render: function (data, row) {
                    return data.name;
                }
            },
            {
                data: "financingWay", className: "center", render: function (data, row) {
                    return data.sex;
                }
            },
            {
                data: "investmentMethod", className: "center", render: function (data, row) {
                    return data.job;
                }
            },
            {
                data: "contributionAmount", className: "textRight", render: function (data) {
                    return data.telephoneNum;
                }
            },
            {
                data: "shareholdingRatio", className: "center", render: function (data, row) {
                    return data.email
                }
            },
            {
                data: "assetName", className: "center", render: function (data, row) {
                    return data.weChatNum;
                }
            },
            {
                data: "qqNum", className: "center", render: function (data, row) {
                    return data.qqNum;
                }
            },
            {
                data: "rmk", className: "center", render: function (data, row) {
                    return data.rmk;
                }
            },
            {
                data: "creator", className: "center", render: function (data, row) {
                    return data.creator;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" id="' + data.id + '" onclick="updateBook(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="' + data.id + '" onclick="delBook(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="' + data.id + '" onclick="viewBook(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });

    /**
     *  生成 拜访记录登记 列表
     */
    visitBtTable = bt.bttable.init({
        elem: "#visitRecordRegistration",
        mulselect: false,
        checkbox: false,
        'url': basePath + "/credit/visitRecordRegistration/detailDatatable?ids=" + $("#visitIds").val(),
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    visitAdd();
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcelToVisitRecordRegistration();
                }
            },
            {
                text: bt.lang('common.button.exports'),
                action: function (e, dt, node, config) {
                    exportExcelToVisitRecordRegistration();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 2//代表冻结右边一列
        },
        columns: [
            {
                data: "rowNum", className: "center", render: function (data, row) {
                    return data.rowNum;
                }
            },
            {
                data: "visitingMatters", className: "center", render: function (data, row) {
                    return data.visitingMatters;
                }
            },
            {
                data: "visitingPersonnel", className: "center", render: function (data, row) {
                    return data.visitingPersonnel;
                }
            },
            {
                data: "visitingObj", className: "center", render: function (data, row) {
                    // return bt.dict("cms.equity.invWay", data.invWay);
                    return data.visitingObj;
                }
            },
            {
                data: "visitingObjJob", className: "center", render: function (data, row) {
                    return data.visitingObjJob;
                }
            },
            {
                data: "visitDate", className: "center", render: function (data) {
                    return data.visitDateStr;
                }
            },
            {
                data: "rmk", className: "center", render: function (data, row) {
                    return data.rmk;
                }
            },
            {
                data: "creator", className: "center", render: function (data, row) {
                    return data.creator;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" id="' + data.id + '" onclick="visitUpdate(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="' + data.id + '" onclick="visitDel(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" id="' + data.id + '" onclick="visitView(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
    //监听提交
    form.on('submit', function (data) {
        bt.loading.show();
        var opType = $(data.elem).val();

        // debugger
        var institutionName2 = data.field.institutionName2;
        var institutionName = data.field.institutionName;
        var shouBankName = data.field.shouBankName;
        // var shouBankName = data.field.shouBankNameId;
        if ((institutionName2 != "undefined" && institutionName2 != null && institutionName2 != "" && institutionName2 != " ")
            && (institutionName == "undefined" || institutionName == null || institutionName == "" || institutionName == " ")) {
            data.field.institutionName = institutionName2;
        }
        if ((shouBankName != "undefined" && shouBankName != null && shouBankName != "" && shouBankName != " ")
            && (institutionName == "undefined" || institutionName == null || institutionName == "" || institutionName == " ")) {
            data.field.institutionName = shouBankName;
        }

        //获取附件id
        data.field.attachmentId = uploader.getGroupId();
        verify(data);

        var action = "/credit/rawData/register/" + $(this).attr("id");
        $.post(basePath + action, data.field, function (result) {
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (!bt.navtab) {
                    location.reload();
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                } else {
                    if (data.field.id == "") {
                        bt.navtab.refreshCurrent();
                    } else {
                        bt.navtab.closeCurrent();
                        bt.navtab.refreshCurrent();
                    }
                }
            } else {
                bt.loading.hide();
            }
        }, 'JSON');
        return false;
    });

});

//添加 股权结构 信息
function ownershipStructureAdd() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/credit/ownershipStructure/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/ownershipStructure/add",
        end: function () {
            location.reload();
        }
    });
}

//查看 股权结构 信息
function ownershipStructureView(el) {
    var id = $(el).attr("id")
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/credit/ownershipStructure/viewOwnershipStructure/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/ownershipStructure/viewOwnershipStructure/" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

//修改 股权结构 信息
function updateOwnershipStructure(el) {
    var id = $(el).attr("id")
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/credit/ownershipStructure/update/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/ownershipStructure/update/" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

//刪除 股权结构 信息
function ownershipStructureDel(el) {
    var id = $(el).attr("id")
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        bt.loading.show();
        $.post(basePath + "/credit/ownershipStructure/deleteOwnershipStructure/" + id, function (result) {
            bt.loading.hide();
            var ssIds = $("#ssIds").val()
            var idArray = ssIds.split(',')
            for (var i = 0; i < idArray.length; i++) {
                if (idArray[i] == id) {
                    idArray.splice(i, 1)
                }
            }
            ssIds = idArray.toString()
            $("#ssIds").val(ssIds)
            bttable.reload();
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });


}

importExcelToOwnershipStructure = function () {
    up = bt.btImport.init({
        code: "120000001",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                // url: basePath + "/cms/equity/info/saveImport", credit/ownershipStructure
                url: basePath + "/credit/ownershipStructure/saveImport",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(up.getSelectData()),
                success: function (result) {
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    if (result.success) {
                        var ssIds = $("#ssIds").val()
                        if (ssIds == "") {
                            ssIds = result.obj
                        } else {
                            ssIds = ssIds + "," + result.obj
                        }
                        bttable.reload({url: basePath + "/credit/ownershipStructure/detailDatatable?ids=" + ssIds});
                        bt.jquery("#ssIds").val(ssIds);
                        // console.log(ssIds)
                    }
                }
            });
        }
    });
};

exportExcelToOwnershipStructure = function () {
    var url = basePath + "/credit/ownershipStructure/exportAll?id=" + $("#ssIds").val();
    window.location.href = url;

}

//添加 联想人通讯录 信息
function addBook() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/credit/contactAddressBook/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/contactAddressBook/add",
        end: function () {
            location.reload();
        }
    });
}

//查看 联想人通讯录 信息
function viewBook(el) {
    var id = $(el).attr("id")
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/credit/contactAddressBook/viewContactAddressBook/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/contactAddressBook/viewContactAddressBook/" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

//修改 联想人通讯录 信息
function updateBook(el) {
    var id = $(el).attr("id")
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/credit/contactAddressBook/update/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/contactAddressBook/update/" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

//刪除 联想人通讯录 信息
function delBook(el) {
    var id = $(el).attr("id")
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        bt.loading.show();
        $.post(basePath + "/credit/contactAddressBook/deleteContactAddressBook/" + id, function (result) {
            bt.loading.hide();
            var bookIds = $("#bookIds").val()
            var idArray = bookIds.split(',')
            for (var i = 0; i < idArray.length; i++) {
                if (idArray[i] == id) {
                    idArray.splice(i, 1)
                }
            }
            bookIds = idArray.toString()
            $("#bookIds").val(bookIds)
            bookBttable.reload();
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });


}

//联系人通讯录 导入
importExcelToContactAddressBook = function () {
    up = bt.btImport.init({
        code: "120000002",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                // url: basePath + "/cms/equity/info/saveImport",
                url: basePath + "/credit/contactAddressBook/saveImport",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(up.getSelectData()),
                success: function (result) {
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    if (result.success) {
                        var bookIds = $("#bookIds").val()
                        if (bookIds == "") {
                            bookIds = result.obj
                        } else {
                            bookIds = bookIds + "," + result.obj
                        }
                        bookBttable.reload({url: basePath + "/credit/contactAddressBook/detailDatatable?ids=" + bookIds});
                        bt.jquery("#bookIds").val(bookIds);
                        // console.log(bookIds)
                    }
                }
            });
        }
    });
};
//联系人通讯录 导出
exportExcelToContactAddressBook = function () {
    var url = basePath + "/credit/contactAddressBook/exportAll?id=" + $("#bookIds").val();
    window.location.href = url;

}

//添加 拜访记录登记 信息
function visitAdd() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/credit/visitRecordRegistration/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/visitRecordRegistration/add",
        end: function () {
            location.reload();
        }
    });
}

//查看 拜访记录登记 信息
function visitView(el) {
    var id = $(el).attr("id")
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/credit/visitRecordRegistration/viewVisitRecordRegistration/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/visitRecordRegistration/viewVisitRecordRegistration/" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

//修改 拜访记录登记 信息
function visitUpdate(el) {
    var id = $(el).attr("id")
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/credit/visitRecordRegistration/update/" + id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/visitRecordRegistration/update/" + id,
        end: function () {
            datatable && datatable.draw();
        }
    });
}

//刪除 拜访记录登记 信息
function visitDel(el) {
    var id = $(el).attr("id")
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        bt.loading.show();
        $.post(basePath + "/credit/visitRecordRegistration/deleteVisitRecordRegistration/" + id, function (result) {
            bt.loading.hide();
            var visitIds = $("#visitIds").val()
            var idArray = visitIds.split(',')
            for (var i = 0; i < idArray.length; i++) {
                if (idArray[i] == id) {
                    idArray.splice(i, 1)
                }
            }
            visitIds = idArray.toString()
            $("#visitIds").val(visitIds)
            visitBtTable.reload();
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}


//拜访记录登记 导入
importExcelToVisitRecordRegistration = function () {
    up = bt.btImport.init({
        code: "120000003",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                // url: basePath + "/cms/equity/info/saveImport",
                url: basePath + "/credit/visitRecordRegistration/saveImport",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(up.getSelectData()),
                success: function (result) {
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    if (result.success) {
                        var visitIds = $("#visitIds").val()
                        if (visitIds == "") {
                            visitIds = result.obj
                        } else {
                            visitIds = visitIds + "," + result.obj
                        }
                        visitBtTable.reload({url: basePath + "/credit/visitRecordRegistration/detailDatatable?ids=" + visitIds});
                        bt.jquery("#visitIds").val(visitIds);
                        // console.log(ssIds)
                    }
                }
            });
        }
    });
};

//联系人通讯录 导出
exportExcelToVisitRecordRegistration = function () {
    var url = basePath + "/credit/visitRecordRegistration/exportAll?id=" + $("#visitIds").val();
    window.location.href = url;

}
function instTypesCallBack(data) {
    // org-box
    $("#org-box").find('>div').addClass('layui-hide');
    $("#institutionName2").removeAttr("lay-verify");
    // 001 headOffice 002 branchOffice  other
    if(data.value == "001"){
        $("#headOffice").removeClass('layui-hide');
        $("#bankTypeTR").show()
    }else if(data.value == "002"){
        $("#branchOffice").removeClass('layui-hide');
        selectType.get("institutionName").attr({"data-required":false});
        $("#bankTypeTR").show()
    }else{
        $("#other").removeClass('layui-hide');
        selectType.get("institutionName").attr({"data-required":false});
        $("#institutionName2").attr("lay-verify","required");
        $("#bankTypeTR").hide();
    }
}

// function openEvaDetail(data){
//     debugger;
//     console.log(data)
// }
function initBankTypeCallBack(data) {
    // debugger;
    // console.log(data)
    $("#bankTypeId").val(data.id);
    if(data.bankkind) {
        dicts.get("bankType").val(data.bankkind);
    }else {
        dicts.get("bankType").val('');
    }
}

// function addContactInfo(data){
//     // {name;xxx,text：xxxxxx}
//     for (var key in data){
//         data[key] = data[key] || ''
//     }
//     // tableid #contactAddressBook
//     var rowIndex = ($('#contactAddressBook tbody tr').length + 1) || 1;
//     var html ='<tr>'
//     html += '<td >'+rowIndex+'</td>'
//     html += '<td >'+data.userName+'<input type="hidden" name="userName" value="'+data.userName+'"></td>'
//     html += '<td >'+data.gender+'<input type="hidden" name="gender" value="'+data.gender+'"></td>'
//     html += '<td >'+data.job+'<input type="hidden" name="job" value="'+data.job+'"></td>'
//     html += '<td >'+data.telephoneNum+'<input type="hidden" name="telephoneNum" value="'+data.telephoneNum+'"></td>'
//     html += '<td >'+data.email+'<input type="hidden" name="email" value="'+data.email+'"></td>'
//     html += '<td >'+data.weChatNum+'<input type="hidden" name="weChatNum" value="'+data.weChatNum+'"></td>'
//     html += '<td >'+data.QQNum+'<input type="hidden" name="QQNum" value="'+data.QQNum+'"></td>'
//     html += '<td >'+data.remark+'<input type="hidden" name="remark" value="'+data.remark+'"></td>'
//     html += '<td></td>'
//     html += '<td><button type="button" class="table-edit" id="add">edit</button></td>'
//     html +='</tr>'
//     $('#contactAddressBook tbody').append(html);
// }
//
// var currentRowByContact = null
// $('contactAddressBook').on('click','.table-edit',function (){
//     currentRowByContact = getCurrentRowData($(this).parents('tr'))
//     addBook(true)
// })
//
// function getCurrentRowData($tr){
//     var fileds = $tr.find('input');
//     var rowData = {}
//     bt.each(fileds,function (){
//         rowData[$(this).attr('name')]:$(this).val()
//     })
//
//     return rowData
// }
/**
 * 设置国家
 * @param data
 * @param elem
 * @param bv
 */
function setArea(data, elem, bv) {
    var url = basePath + "/basic/business/syscountrycust/listSysCountryArea/" + data.id;
    if (data.namezh == "中国") {
        selectType.get("place").attr({"data-value": "", "data-url": url, "data-required": false});
        region.get('payeeAddr').set([{
            name: 'prov',
            defaultValue: '',
            required: true
        }, {
            name: 'city',
            defaultValue: '',
            required: true
        }]);
        $('#addressDiv').hide();
        $('#addrDiv').show();
    } else {
        selectType.get("place").attr({"data-value": "", "data-url": url, "data-required": false});
        region.get('payeeAddr').set([{
            name: 'prov',
            defaultValue: '',
            required: false
        }, {
            name: 'city',
            defaultValue: '',
            required: false
        }]);
        $('#addressDiv').show();
        $('#addrDiv').hide();
    }
    selectType.render();
    // 移除 form.renderIcon() 调用，避免误删机构名称必填标识
    // form.renderIcon();
}

//境内境外银行选择回调
function domesticAndOverseasBankCallback(data, dom) {
    $("#shouBankName").val(data.bankName);
    $("#bankCode").val(data.bankCode);
    $.ajax({
        url: basePath + "/basic/business/btbanktype/getBankType",
        method: 'POST',
        data: {bankTypeId: data.bankTypeId},
        dataType: 'json',
        success: function (data) {
            if (data) {
                if(data.bankKind){
                    dicts.get("bankType").val(data.bankKind);
                }else {
                    dicts.get("bankType").val("");
                }
            }else {
                dicts.get("bankType").val("");
            }
        }
    })
}

//关闭页面
closeForm = function () {
    parent.layer ? parent.layer.closeAll() : layer.closeAll();
}

//明细
function reviewDetailFunction(obj) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/credit/crTaskOrg/reviewTaskDetails/"+obj.id +'/'+obj.orgid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/crTaskOrg/reviewTaskDetails/"+obj.id +'/'+obj.orgid,
        end: function () {
            location.reload();
        }
    });
}

function verify(result) {
    //评分级别校验
    if (result.field.institutionName.length> 200){
        parent.layer.msg(bt.lang("机构名称长度不能超过100个汉字"), {icon: 2});
        return false;
    }
    // var trs = $("#dataTable tbody tr");
    // var lastrow=Number($(trs[trs.length - 1]).attr("data-row"));
    // var scoringSymbol=dicts.get("scoringSymbol"+lastrow).val();//最后一行符号
    // var value=moneyTypes.get("scoreLimitLevel"+lastrow).val();//此行值
    // if(Number(value)!=0 && (scoringSymbol=="10" || scoringSymbol=="11")){
    //     parent.layer.msg(bt.lang("同方向评分标准最低下限分应为0"), {icon: 2});
    //     return false;
    // }
}

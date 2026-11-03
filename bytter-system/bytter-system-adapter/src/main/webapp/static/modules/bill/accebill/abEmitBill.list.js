var billTable = {};
var idsValArray = [];
var rateType,ration;
bt.use(['form', 'datatable', 'moneyType', 'btdate', 'corp', 'select','mulselect', 'dateType', 'btImport', 'dicts','rateType','ratioType','accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    rateType = bt.rateType.init();
    ratio = bt.ratioType.init();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.mulselect.init();
    bt.accountTable.init({urldata:{accountType :'2,3',electricBill:'1'}});
    var notPayImport=!$("#payImport").val();
    if(!notPayImport){
        $("#payOrigNo").val(top.payImportOrigNo);
        top.payImportOrigNo="";
    }

    var columns = [
        {data: "appNo", bSortable: true},
        {data: "batchNo", bSortable: true},
        {data: "billTypeName", bSortable: true},
        {data: "billFlagName", bSortable: true},
        {data: "insideSingName", bSortable: true},
        {data: "emitTypeName", bSortable: true},
        {data: "applyCorpName", bSortable: true,
            render: function (data, type, full, meta) {
                return '(' + full.applyCorpCode + ')' + full.applyCorpName;
            }
        },
        {
            data: "corpName", bSortable: true,
            render: function (data, type, full, meta) {
                return '(' + full.corpCode + ')' + full.corpName;
            }
        },
        {data: "bankAcc", bSortable: true},
        {data: "busCorpName", bSortable: true},
        {data: "busPersonnel", bSortable: true},
        {data: "payer", bSortable: true},
        {data: "payerbankno", bSortable: true},
        {data: "payerBankName", bSortable: true},
        {data: "acceptor", bSortable: true},
        {data: "acceptorbankno", bSortable: true},
        {data: "emitGuandisSignName", bSortable: true},
        {data: "origNo", bSortable: true},
        {
            data: "billMoney", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.billMoney);
            }
        },
        {data: "startDate", bSortable: true},
        {data: "endDate", bSortable: true},
        {data: "payee", bSortable: true},
        {data: "payeeacc", bSortable: true},
        {data: "payeeBankName", bSortable: true},
        {data: "sourceName", bSortable: true},
        {data: "sendSignName", bSortable: true},
        {data: "statusName", bSortable: true},
        {data: "isEnsureName", bSortable: true},
        {
            data: "payNo", bSortable: true, className: 'center',
            render: function (data, type, full, meta) {
                var html = "";
                if (full.payNo != "" && full.payNo != null) {
                    html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewNisBillhead(this)">' + full.payNo + '</a>';
                }
                return html;
            }
        },
        {
            data: "attachmentNum",
            bSortable: false,
            className: 'center',
            render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="attachmentView(this)">' + bt.lang("bill.accebill.attachment.num", [(full.attachmentNum || 0)]) + '</a>';
                return html;
            }
        },

        {data: "newStmIssuofpprIndName", bSortable: true},
        {data: "blgrdbagcrclPrmtIndName", bSortable: true},
        {data: "sbbllRngStrtSn", bSortable: true},
        {data: "sbbllRngEndSn", bSortable: true},
        {data: "blStdAmt", bSortable: true, render: function (data, type, full, meta) {
                if(full.newStmIssuofpprInd == 1){
                    return full.blStdAmt;
                }else {
                    return "";
                }
            }},
        {data: "blRmtrUnnIdNo", bSortable: true},
        {
            data: "bailpercent", bSortable: true, className: "textRight",
            render: function (data, type, full, meta) {
                var html;
                if (data || data == "0") {
                    html = '<div id="rpdintrstRate' + full.accId + '" name="rpdintrstRate">' + bt.rateType.getFun().format(full.bailpercent, 'percent', 2, ',') + '</div>'
                } else {
                    html = '<div id="rpdintrstRate' + full.accId + '" name="rpdintrstRate"></div>'
                }
                return html;
            }
        },
        {
            data: "bailmoney", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.bailmoney);
            }
        },
        {
            data: "bailrate", bSortable: true, className: "textRight",
            render: function (data, type, full, meta) {
                var html;
                if (data || data == "0") {
                    html = '<div id="rpdintrstRate' + full.accId + '" name="rpdintrstRate">' + bt.rateType.getFun().format(full.bailrate, 'percent', 2, ',') + '</div>'
                } else {
                    html = '<div id="rpdintrstRate' + full.accId + '" name="rpdintrstRate"></div>'
                }
                return html;
            }
        },
        {data: "bailAcc", bSortable: true},
        {data: "contractNo", bSortable: true, className: 'textRight'},
        {data: "ratItemName", bSortable: true, className: 'textRight'},
        {data: "isOpenRat", bSortable: true,
            render: function (data, type, full, meta) {
                return bt.dict('sf_yn',full.isOpenRat);
            }
        },
        {data: "rmk", bSortable: true},
        /* {data: "blAcptrTpcd", bSortable: true},
         {data: "blRcvpymtpsIdTpcd", bSortable: true},*/

        {
            data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                if (data.status == 0 || data.status == -1) {//暂存、审批打回
                    if (bt.isResource('bill:accebill:abemitbill:save', full.applyCorpId)) {
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                    }
                    if (bt.isResource('bill:accebill:abemitbill:delete', full.applyCorpId)) {
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    }
                }
                //  html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.aid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }
        }
    ]

    var tableTitelObj = {}
    bt.each($("#appTable thead tr th"),function (i,th){
        tableTitelObj[columns[i].data] = $(th).text() || '';
    })


    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[2, 'desc'], [1, 'desc']],
        pageSizeOptions:[10, 25, 50, 100, 500,1000,2000,5000],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abemitbill/datatable"
        },
        buttons: [
            //新增、删除、提交、导入、导出
            {
                text: bt.lang('common.button.add'),
                hide: !notPayImport || bt.isnotResource('bill:accebill:abemitbill:save'),
                action: function (e, dt, node, config) {
                    add("0");
                }
            }, {
                text: bt.lang('common.button.batchAdd'),
                hide: !notPayImport || bt.isnotResource('bill:accebill:abemitbill:save'),
                action: function (e, dt, node, config) {
                    add("1");
                }
            }, {
                text: bt.lang('common.button.delete'),
                hide: bt.isnotResource('bill:accebill:abemitbill:delete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            }, {
                text: bt.lang('common.button.submit'),
                hide: bt.isnotResource('bill:accebill:abemitbill:submit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            }, {
                text: bt.lang('common.button.batchNoSubmit'),
                hide: true,
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatchNo(data);
                }
            }, {
                text: bt.lang('common.button.imports'),
                hide: !notPayImport,
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    //layer.alert(bt.lang('common.message.indevelopment'));
                    importExcel();
                }
            },/*{
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	exports(data);
                }
            },*/
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: columns,
        /*fnDrawCallback: function () {
            idsValArray = new Array();
            setSumMoney();
        },*/
        btClickRowCallback: function (data) {
            opIdsValArray(data);
        },
        btDrawCallback: function (setting) {
            idsValArray = []
            setSumMoney();
        },
        customExportSelected:function (params,processing){
          var selectedList = bt.datatable.getSelectData("#appTable");
          var ids = "";
          var exportExcelUrl = "/bill/accebill/abemitbill/exportSelData";
          for(var idx = 0; idx < selectedList.length; idx++){
              if(ids == ""){
                  ids = selectedList[idx].aid;
              }else {
                  ids = ids + "," + selectedList[idx].aid;
              }
          }
            var sumLists = tableSummary.sumList
          // tableTitelObj
            var tableColumns = params.columns;
          var newColumns = [];
            bt.each(tableColumns,function (i,col){
                col.title = tableTitelObj[col.data];
                if(col.title){
                    newColumns.push(col);
                    for(var cindex = 0; cindex < sumLists.length; cindex++){
                        var colName = sumLists[cindex].dataName;
                        if(colName == col.name){
                            col.isPagesum = true;
                        }
                    }
                }
            })
            params.columns = newColumns;
            openExportSelectedExcel(exportExcelUrl,JSON.stringify(params),ids);
            processing(false)
        },
        fixedColumns: {
            rightColumns: 1
        },
        summary: tableSummary
    });

    var tableSummary = {
        pageSum: true,
        selectSum: true,
        sumList: [
            {
                colNum: '15',
                dataName: 'billMoney'
            }
        ]
    }
    var openExportSelectedExcel = function (exportUrl, params, code) {
        if (!exportUrl) {
            exportUrl = basePath + "/export/excel/exportAll";
        }
        var form = $("<form></form>").attr("style", "display:none").attr("action", exportUrl).attr("method", "post");
        try {
            form.append($("<input></input>").attr("type", "hidden").attr("name", "params").attr("value", encodeURIComponent(params)));
            form.append($("<input></input>").attr("type", "hidden").attr("name", "ids").attr("value", encodeURIComponent(code)));
            form.appendTo('body');
            form.submit();
        } catch (e) {
            console.log(e);
        } finally {
            form.remove();
        }
    }
    //格式化数字
    $.each($("input[decimal]"), function (i, item) {
        if ($(item).attr("decimal") != "") {
            bt.moneynum.init({elem: $(item), decimal: $(item).attr('decimal') * 1});
            //bt.moneynum.setnum({trigger:$(item),decimal:$(item).attr('decimal')*1});
        }
    });
    //时间
    $.each($("div[type='date']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"), function (i, item) {
        bt.btdate.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });
    //导入初始化
    importExcel = function () {
        //证书签名
        var signDataMsg = signData("开票登记导入", 9);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        up = bt.btImport.init({
            code: "1008001",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            autoCloseWindown:false,
            success: function (head, data, select) {
                bt.loading.show();
                $.ajax({
                    type: "POST",
                    url: basePath + "/bill/accebill/abemitbill/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(select),
                    //重写success方法
                    success: function (result) {
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        if(result.success){
                            up.closeWindown();
                            bt.datatable.refreshCurrentPage();
                        }
                    },
                    error:function(){
                        bt.loading.hide();
                    }
                });
            }
        });
    }
});

//票据单位账号回调
function bankAccCallBack(data){
    var $ = bt.jquery;
    $("#bankAcc").val(data.bankAcc);
}

//比较保证金比例
function compareBailpercent(){
    var geg_bailpercent = rateType.get("geg_bailpercent").val();
    var leg_bailpercent = rateType.get("leg_bailpercent").val();
    if(geg_bailpercent && leg_bailpercent && geg_bailpercent > leg_bailpercent){
        rateType.get("leg_bailpercent").val(geg_bailpercent);
    }
}

//比较保证金利率
function compareBailrate(){
    var geg_bailrate = rateType.get("geg_bailrate").val();
    var leg_bailrate = rateType.get("leg_bailrate").val();
    if(geg_bailrate && leg_bailrate && geg_bailrate > leg_bailrate){
        rateType.get("leg_bailrate").val(geg_bailrate);
    }
}

function add(isBatchBill) {
    if (bt.navtab) {
        var title = bt.lang('common.button.add');
        if (isBatchBill == '1') {
            title = bt.lang('common.button.batchAdd');
        }
        bt.navtab.addNavtab(title, basePath + "/bill/accebill/abemitbill/add?isBatchBill=" + isBatchBill);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/add?isBatchBill=" + isBatchBill,
        end: function () {
//        	billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

function update(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.aid;
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.edit'), basePath + "/bill/accebill/abemitbill/update/" + aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/update/" + aid,
        end: function () {
//            billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

function deleteColumn(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.aid;
    var reason = $("#reason").val();
    if (data.source == '10' && isEmpty(reason)) {
        //付款票据的删除原因必填
        layer.alert(bt.lang('abemitbillback.backReasonRequired'), {icon: 7});
        return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bill/accebill/abemitbill/delete", {"aid": aid, "reason": reason}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

function view(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.aid;

    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abemitbill/view/" + aid);
        return;
    }

    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abemitbill/view/" + aid
    });
}

function deleteBatch(data) {
    if (!bt.form().verifyFun('#dataForm')) {
        return false;
    }
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var reason = $("#reason").val();
    var aids;
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abemitbill:delete', rowData[i].applyCorpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
        if (rowData[i].source == '10' && isEmpty(reason)) {
            //付款票据的删除原因必填
            layer.alert(bt.lang('abemitbillback.deleteReasonRequired'), {icon: 7});
            return;
        }
        if (aids) {
            aids = aids + "," + rowData[i].aid;
        } else {
            aids = rowData[i].aid;
        }
        /*if(rowData[i].status!=0&&rowData[i].status!=1&&rowData[i].status!=-1){
            layer.alert(bt.lang('bill.accebill.abEmitBill.delete.check'), {icon: 7});
            return;
        }*/
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/bill/accebill/abemitbill/batchDelete", {"aids": aids, "reason": reason}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

function submitBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var vids, vvids, aids;
    // {isControl:是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递） ,params:[{name:检查名称,appNo:申请单据号,corpId:单位ID},{name:检查名称,appNo:申请单据号,corpId:单位ID}]}
    var bwDataParams = [];
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abemitbill:save', rowData[i].applyCorpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
        //付款过来暂存数据判断必须要暂存过才可以提交
        if (rowData[i].source == '10' && !rowData[i].createBy) {
            layer.alert(bt.lang('bill.emit.isBillPayadd.notTS', [(i + 1)]), {icon: 7});
            return;
        }
        /*if(rowData[i].status!=0&&rowData[i].status!=1&&rowData[i].status!=-1){
            layer.alert(bt.lang('bill.accebill.abEmitBill.submit.check'), {icon: 7});
            return;
        }*/
        if (vids) {
            vids = vids + "," + rowData[i].vid;
        } else {
            vids = rowData[i].vid;
        }
        if (vvids) {
            vvids = vvids + "," + rowData[i].vvid;
        } else {
            vvids = rowData[i].vvid;
        }
        if (aids) {
            aids = aids + "," + rowData[i].aid;
        } else {
            aids = rowData[i].aid;
        }
        bwDataParams.push({"name": rowData[i].payee, "appNo": rowData[i].appNo, "corpId": rowData[i].corpId})
    }
    var emitData = {"vids": vids, "vvids": vvids, "aids": aids};
    bt.loading.show();
    //对黑白名单进行验证
    var reData = externalCorpCheckBatch({"params": bwDataParams});
    bt.loading.hide();
    if (reData.reCode == '3') {
        parent.layer ? parent.layer.alert(reData.msg, {icon: 2}) : layer.alert(reData.msg, {icon: 2});

    } else if (reData.reCode == '2') {
        parent.layer.confirm(reData.msg, function () {
            emitSubmit(emitData);
        });
    } else {
        emitSubmit(emitData);
    }
}

//批次提交
function submitBatchNo(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var vids, vvids, aids;
    // {isControl:是否支持强控0否1是不必填（默认支持强控，部分业务场景无强控需要传递） ,params:[{name:检查名称,appNo:申请单据号,corpId:单位ID},{name:检查名称,appNo:申请单据号,corpId:单位ID}]}
    var bwDataParams = [];
    var corpId, acceptor;
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abemitbill:save', rowData[i].applyCorpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
        //付款过来暂存数据判断必须要暂存过才可以提交
        if (rowData[i].source == '10' && !rowData[i].createBy) {
            layer.alert(bt.lang('bill.emit.isBillPayadd.notTS', [(i + 1)]), {icon: 7});
            return;
        }
        //批次提交需要持票人、承兑人一致
        if (i == 0) {
            corpId = rowData[i].corpId;
            acceptor = rowData[i].acceptor;
        } else {
            if (corpId != rowData[i].corpId || acceptor != rowData[i].acceptor) {
                layer.alert(bt.lang('bill.emit.batchNoSumit.payerAndAcceptor.notAgr'), {icon: 7});
                return;
            }
        }

        if (vids) {
            vids = vids + "," + rowData[i].vid;
        } else {
            vids = rowData[i].vid;
        }
        if (vvids) {
            vvids = vvids + "," + rowData[i].vvid;
        } else {
            vvids = rowData[i].vvid;
        }
        if (aids) {
            aids = aids + "," + rowData[i].aid;
        } else {
            aids = rowData[i].aid;
        }
        bwDataParams.push({"name": rowData[i].payee, "appNo": rowData[i].appNo, "corpId": rowData[i].corpId})
    }
    var emitData = {"vids": vids, "vvids": vvids, "aids": aids};
    bt.loading.show();
    //对黑白名单进行验证
    var reData = externalCorpCheckBatch({"params": bwDataParams});
    bt.loading.hide();
    if (reData.reCode == '3') {
        parent.layer ? parent.layer.alert(reData.msg, {icon: 2}) : layer.alert(reData.msg, {icon: 2});

    } else if (reData.reCode == '2') {
        parent.layer.confirm(reData.msg, function () {
            emitBatchNoSubmit(emitData);
        });
    } else {
        emitBatchNoSubmit(emitData);
    }
}

function emitSubmit(emitData) {
    //证书签名
    var signDataMsg = signData("开票登记批量提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        bt.loading.show();
        $.post(basePath + "/bill/accebill/abemitbill/batchSubmit", emitData, function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

function emitBatchNoSubmit(emitData) {
    //证书签名
    var signDataMsg = signData("开票登记批次提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchNoSubmit'), function () {
        bt.loading.show();
        $.post(basePath + "/bill/accebill/abemitbill/batchNoSubmit", emitData, function (result) {
            bt.loading.hide();
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
        }, 'JSON');
    });
}

/*
function exports(data){
	parent.layer.alert(bt.lang("common.message.indevelopment"));
}*/

function attachmentView(el) {
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var isNotOp = data.status != 11 ? '0' : '1';
    var params = {
        "id": data.aid,
        "attachmentId": (data.attachmentId || null),
        "vvid": (data.vvid || null),
        "briefId": (data.briefId || null),
        "isNotOp": isNotOp
    };
    parent.layer.open({
        type: 2,
        area: ['820px', '400px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillattachment/view?" + $.param(params),
        end: function () {
            billTable.draw();
        }
    });
}

/**判断字符串是否为空*/
function isEmpty(ms) {
    if (typeof (ms) == "undefined" || ms == null || ms == "") {
        return true;
    }
    return false;
}

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(datas[i].aid) < 0) {
                    idsValArray.push(datas[i].aid);
                }
            } else {
                var index = idsValArray.indexOf(datas[i].aid);
                if (index > -1) {
                    objArrayRemove(idsValArray, index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function () {
    var showHTML = bt.lang("sys.common.isSelectSize", [idsValArray.length]);
    var sumBillMoney = 0;
    var sumBailmoney = 0;
    //合计已选票据包金额和已选保证金金额
    if(idsValArray && idsValArray.length>0){
        var selectedList = bt.datatable.getSelectData("#appTable");
        for(var i=0;i<selectedList.length;i++){
            if(selectedList[i].billMoney){
                sumBillMoney += selectedList[i].billMoney;
            }
            if(selectedList[i].bailmoney){
                sumBailmoney += selectedList[i].bailmoney;
            }
        }
    }
    showHTML+=",已选票据总金额:"+bt.money.format(sumBillMoney);
    showHTML+=",已选保证金总金额:"+bt.money.format(sumBailmoney);
    if ($("#sumMoneyBillSpan").text() != '') {
        $("#sumMoneyBillSpan").text(showHTML);
    } else {
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span>');
    }
}

var objArrayRemove = function (obj, index) {
    if (index > -1) {
        obj.splice(index, 1);
    }
};

//付款单查看
function viewNisBillhead(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + data.payId
    });
}

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);

}

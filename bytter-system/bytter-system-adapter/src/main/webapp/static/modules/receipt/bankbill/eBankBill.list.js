var datatable;
bt.use(['form', 'datatable','corp', 'dateType', 'moneyType', 'dicts', 'selectType', 'bank','accountTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    var LODOP = getLodop();
    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);

    }

    accountCallBack2 = function (data) {
        $("#likes_oppAccNo").val(data.bankAcc);
    }
    cdSignCallback = function(data){
        var cdSign = data.value;
        $("#likes_payAccNo").val("");
        $("#payAccNo").val("");
        $("#likes_payAccName").val("");
        $("#likes_payeeAccNo").val("");
        $("#payeeAccNo").val("");
        $("#likes_payeeAccName").val("");

        // 单位id
        var corpId = corps.get("ins_corpId").val();
        changeAccs(corpId,cdSign);
    }
    var dicts = bt.dicts.init();
    var selectType = bt.selectType.init();
    bt.bank.init({
        elem: ".bank",
    });
    payAccNoCallBack = function (data) {
        $("#bankAcc").val(data.bankAcc);
        $("#likes_bankAcc").val(data.bankAcc);
        $("#likes_accName").val(data.accName);
    };
    payeeAccNoCallBack = function (data) {
        $("#oppAccNo").val(data.bankAcc);
        $("#likes_oppAccNo").val(data.bankAcc);
        $("#likes_oppAccName").val(data.accName);
    };
    var accountTable = bt.accountTable.init();
    corpCallback = function (id) {
        var cdSign = dicts.get("cdSign").val();
        changeAccs(id,cdSign);
    };
    /*单位初始化*/
    var corps= bt.corp.init();
    bt.moneyType.init();

    var changeAccs = function (corpId,cdSign) {
        // 如果单位id有值，则设置本方和对方账号的查询单位
        if(!isEmpty(corpId)){
            if(isEmpty(cdSign)){
                $("#payAccNoDiv").show();
                $("#payeeAccNoDiv").show();
                $("#payAccNoDivSelect").hide();
                $("#payeeAccNoDivSelect").hide();
                accountTable.get("likes_bankAcc").setCorp(corpId);
                accountTable.get("likes_oppAccNo").setCorp(corpId);
            }else if(cdSign == "1"){
                $("#payAccNoDiv").hide();
                $("#payeeAccNoDiv").show();
                $("#payAccNoDivSelect").show();
                $("#payeeAccNoDivSelect").hide();
                accountTable.get("bankAcc").setCorp(corpId);
                accountTable.get("likes_oppAccNo").setCorp(corpId);
            }else if(cdSign == "0"){
                $("#payAccNoDiv").show();
                $("#payeeAccNoDiv").hide();
                $("#payAccNoDivSelect").hide();
                $("#payeeAccNoDivSelect").show();
                accountTable.get("likes_bankAcc").setCorp(corpId);
                accountTable.get("oppAccNo").setCorp(corpId);
            }
        }else{
            // 清空本方和对方账号的单位查询条件
            accountTable.get("likes_bankAcc").setCorp("");
            accountTable.get("bankAcc").setCorp("");
            accountTable.get("likes_oppAccNo").setCorp("");
            accountTable.get("oppAccNo").setCorp("");
        }
    };

    /**
     * 生成列表
     */
    var datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[2,'asc'],[15,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            isInitData:"false"
        },
        ajax: {
            'url': basePath + "/receipt/bisbankreceiptdtl/datatable"
        },
        // ignoreButtons: ['print'],
        buttons: [
            {
                text: bt.lang('common.button.batchPreview'),
                action: function (e, dt, node, config) {
                    batchPreview(e, dt, node, config);
                }
            },
            {
                hide:bt.isnotResource('netbank:receipt:bankbill:apply'),
                text: bt.lang('common.button.batchUpdate'),
                action: function (e, dt, node, config) {
                    batchUpdate(e, dt, node, config);
                }
            },
            {
                text: bt.lang('common.button.batchPrint'),
                action: function (e, dt, node, config) {
                    batchPrint(e, dt, node, config);
                }
            },
            {
                text: bt.lang('common.button.batchDownload'),
                action: function (e, dt, node, config) {
                    batchDownload(e, dt, node, config);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 3//代表冻结右边一列
        },
        columns: [
            {data:"corpName",bSortable:false},
            {data: "charageDate", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "receiptCheckCode", bSortable: true},
            {data: "receiptNo", bSortable: true},
            {
                data: "transAmt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.transAmt);
                }
            },
            {data: "cdSign", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAccDtl.cdSign",full.cdSign);
                }
            },
            {data: "bifName", bSortable: true},
            // {data: "payAccNo", bSortable: true},
            // {data: "payAccName", bSortable: true},
            // {data: "payBankName", bSortable: true},
            // {data: "payeeAccNo", bSortable: true},
            // {data: "payeeAccName", bSortable: true},
            // {data: "payeeBankName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "oppAccBank", bSortable: true},
            {data: "usefor", bSortable: true},
            {data: "prints", bSortable: true, className: 'center'},
            {data: "isFrom", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("BisBank.Receipt.Dtl",full.isFrom);
                }
            },
            {data: "status", bSortable: true},
            {
                data: "dtlId", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = '';
                    if(full.dtlId!=null){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="dtl(this)">'+bt.lang("common.button.viewFlow")+'</a>';
                    }else{
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="dtlno(this)">'+bt.lang("common.button.targetFlow")+'</a>';
                    }
                    return html;
                }
            },{
                data: "billId", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = '';
                    if(full.cdSign=="0"){
                        html += '<a class="" href="#" data-row="'+meta.row+'" >'+bt.lang("table.fbsBudget.detail.nothing")+'</a>';
                    }else {
                        if(full.dtlId==null){
                            html += '<a class="" href="#" data-row="'+meta.row+'" >'+bt.lang("table.fbsBudget.detail.nothing")+'</a>';
                        }else{
                            if(full.billFrom=="no_one"){
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="billno(this)">' + bt.lang("common.button.targetPayment") + '</a>';
                            }else {
                                if (full.billId == null||full.billId =="") {
                                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="billno(this)">' + bt.lang("common.button.targetPayment") + '</a>';
                                } else {
                                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bill(this)">' + bt.lang("common.button.viewPayment") + '</a>';
                                }
                            }

                        }
                    }

                    return html;
                }
            },

            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="preview(this)">' + bt.lang("common.button.excel.preview") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="downloadFile(this)">' + bt.lang("common.button.download") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="print(this)">' + bt.lang("common.button.print") + '</a>';
                    if(bt.isResource('netbank:receipt:bankbill:apply',full.dtlId)) {
                        html += '</a><span class="text-explode">|</span>' + '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });
});

/**
 * 预览
 * @param el
 */
function preview(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(!$(el).hasClass('layui-disabled')){
        $(el).addClass('layui-disabled')
        parent.layer.open({
            title: "预览",
            // title: false, //不显示标题栏
            type: 2,
            area: ['870px', '80%'],
            closeBtn :0,//不显示关闭按钮
            content: [basePath + "/receipt/bisbankreceiptdtl/view?id=" + data.id]
            , btn: [bt.lang('common.button.print'), '<i class="layui-icon">&#x1006;</i> ']
            , yes: function () {
                print(el);
            }
            , btn2: function () {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            },
            end:function () {
                $(el).removeClass('layui-disabled')
            }
        });

        // top.layer.open({
        //     title: "预览",
        //     type: 2,
        //     area: ['1000px', '640px'],
        //     content: location.origin+'/receipt/bisbankreceiptdtl/initImg?id='+data.id,
        //     cancel:function () {
        //         layer.close();
        //     },
        //     end:function () {
        //         $(el).removeClass('layui-disabled')
        //     }
        // });


    }else {
        layer.msg(bt.lang('正在打开页面'),{time: 800}); //正在打开页面
    }
}

/**
 * 打印
 * @param el
 */
function print(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    // if(!$(el).hasClass('layui-disabled')){
    //     $(el).addClass('layui-disabled')
    //     parent.layer.open({
    //         type: 2,
    //         zIndex: -100,
    //         shade: 0,
    //         content: basePath + "/receipt/bisbankreceiptdtl/print?id=" + data.id,
    //         end:function () {
    //             $(el).removeClass('layui-disabled')
    //         }
    //     });
    // }else{
    //     layer.msg(bt.lang('common.msg.printing'),{time: 800}); //正在打印中
    // }
    if(!$(el).hasClass('layui-disabled')){
        $(el).addClass('layui-disabled')
    }else{
        layer.msg(bt.lang('common.msg.printing'),{time: 800}); //正在打印中
    }
    parent.layer.open({
        type: 2,
        zIndex: -100,
        shade: 0,
        content: basePath + "/receipt/bisbankreceiptdtl/print?id=" + data.id,
        end:function () {
            $(el).removeClass('layui-disabled')
        }
    });

}

/**
 * 批量打印预览
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchPreview(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.open({
        title: false, //不显示标题栏
        type: 2,
        area: ['838px', '450px'],
        closeBtn :0,//不显示关闭按钮
        content: basePath + "/receipt/bisbankreceiptdtl/batchPreview?ids=" + ids
        , btn: [bt.lang('common.button.print'), '<i class="layui-icon">&#x1006;</i> ']
        , yes: function () {
            batchPrint(e, dt, node, config);
        }
        , btn2: function () {
            parent.layer ? parent.layer.closeAll() : layer.closeAll();
        }
    });
}

/**
 * 批量打印
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchPrint(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var loadIndex = layer && layer.load(1, {shade: [0.3, '#fff']});
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.open({
        type: 2,
        zIndex: -100,
        shade: 0,
        content: basePath + "/receipt/bisbankreceiptdtl/batchPrint?ids=" + ids,
        success:function (layero, index) {
            layer && layer.close(loadIndex);
        }
    });
}
//查看关联的流水
function dtl(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var size=data.dtlId.split("/").length;
    if(size==1){
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/bisaccdtl/view/" + data.dtlId+",rules"
        });
    }else {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/receipt/bisbankreceiptdtl/dtl?id=" + data.dtlId
        });
    }
}
//查看关联的付款单
function bill(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var size=data.billId.split("/").length;
    if(size==1){
        if(data.billFrom=="nis_billhead") {
            parent.layer.open({
                type: 2,
                area: ['850px', '530px'],
                maxmin: true,
                content: basePath + "/netbank/nisbillhead/view/" + data.billId
            });
        }else if(data.billFrom=="nis_wage"){
            parent.layer.open({
                type: 2,
                area: ['950px', '530px'],
                maxmin: true,
                content: basePath + "/wage/niswage/view/" + data.billId
            });
        }else if(data.billFrom=="fex_cash_flow"){
            parent.layer.open({
                type: 2,
                area: ['950px', '530px'],
                maxmin: true,
                content: basePath + "/foreign/fexcashflow/view/" + data.billId
            });
        }
    }else{
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/receipt/bisbankreceiptdtl/bill?id=" + data.billId
        });
    }
}
//未关联流水提醒
function dtlno(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab( bt.lang('common.receipt.check'),basePath + "/receipt/bisbankreceiptblending/list/"+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/receipt/bisbankreceiptblending/list"

    })
}
//未关联付款单提醒
function billno(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var size=data.dtlId.split("/").length;
    if(size==1) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.pay.check'), basePath + "/bankacc/paymentblending/list/" + data.dtlId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/paymentblending/list"

        });
    }else {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/receipt/bisbankreceiptdtl/morebill?id=" + data.dtlId
        });
    }
}
//编辑
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath +  "/receipt/bisbankreceiptdtl/updatepage?id=" + data.id+"&corpName="+data.corpName);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath +  "/receipt/bisbankreceiptdtl/updatepage?id=" + data.id+"&corpName="+data.corpName,
            end: function () {
                location.reload();
            }
        });
    }
}
/**
 * 批量修改
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchUpdate(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if(rowData[i].dtlId==null) {
            if (ids == "") {
                ids += rowData[i].id;
            } else {
                ids += "," + rowData[i].id;
            }
        }
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/receipt/bisbankreceiptdtl/batchUpdate?ids=" + ids );
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/receipt/bisbankreceiptdtl/batchUpdate?ids=" + ids ,
        end: function () {
            location.reload();
        }
    });
}

/**
 * 下载
 * @param el
 */
function downloadFile(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    window.location = basePath + "/receipt/bisbankreceiptdtl/download?id=" + data.id;
}

/**
 * 批量下载
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchDownload(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    var form = $("<form></form>").attr("style", "display:none").attr("action", basePath + "/receipt/bisbankreceiptdtl/batchDownload").attr("method", "post");
    try {
        form.append($("<input></input>").attr("type", "hidden").attr("name", "ids").attr("value", ids));
        form.appendTo('body');
        form.submit();
    } catch (e) {
        console.log(e);
    } finally {
        form.remove();
    }
}

function isEmpty(stringData) {
    if (null == stringData || stringData == "" || stringData == undefined || stringData == " " || stringData == "null") {
        return true;
    } else {
        return false;
    }
}

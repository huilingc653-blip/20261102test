var  moneyType;
bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType','mulselect','btImport'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    moneyType = bt.moneyType.init();
    bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });
    var selectType = bt.selectType.init();

    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
        selectType.get("eqs_curId").val(data.curId);
    }
    var accountTable = bt.accountTable.init();

    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_bankAcc").setCorp(id);
    }

    bt.corp.init();

    /**
     * 导入功能
     */
    importExcel = function () {
        up = bt.btImport.init({
            code: "100000073",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success:function(head,data,select,cacheCode){
                $.ajax({
                    type: "POST",
                    url: basePath + "/bankacc/btbankaccdtl/saveHisDtlImport",
                    dataType: "json",
                    data:{"cacheCode":cacheCode},
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
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
    };

    var tableSummary = {
        pageSum: true,//小计功能关闭
        selectSum: true,//已选汇总功能开启
        // groupColumn: 'cdSignName',//分类汇总列参数
        format: {
            decimal: 2
        },
        sumList: [
            {
                colNum: '10',
                dataName: 'amtIn'
            },
            {
                colNum: '11',
                dataName: 'amtOut'
            }
        ]
    };

    // summary: {
    //     pageSum:true,
    //     selectSum:true,
    //     groupColumn: 'cdSignName',//分类汇总列参数
    //     // allSum: true,
    //     // selectSum: false,
    //     sunTextCol: 2,
    //     customSum:{
    //         renderHtml:function (html) {
    //             if(html && html.indexOf('</tr>') !== -1){
    //                 var htmlArr = html.split('</tr>')
    //                 var len = htmlArr.length
    //                 if(len > 2){
    //                     var selectHtml = '' //选中小计
    //                     var pageHtml = '' //当前页合计
    //                     for (var i = 0; i < htmlArr.length; i++) {
    //                         if(htmlArr[i].indexOf('selectSum') !== -1){
    //                             selectHtml +=htmlArr[i]
    //                             selectHtml +='</tr>'
    //
    //                         }else if(htmlArr[i].indexOf('pageSum') !== -1){
    //                             pageHtml +=htmlArr[i]
    //                             pageHtml +='</tr>'
    //                         }
    //                     }
    //                     return (selectHtml + pageHtml)
    //                 }
    //             }
    //             return html
    //         }
    //     },
    //     format: {
    //         decimal: 2
    //     },
    //     sumList: [
    //         {
    //             colNum: '9',
    //             dataName: 'amtIn'
    //         },
    //         {
    //             colNum: '10',
    //             dataName: 'amtOut'
    //         }
    //     ]
    // };

    var columns = [
        {data: "transTime", bSortable: true},
        {data: "corpName", bSortable: true},
        {data: "dtlId", bSortable: true},
        {data: "bankAcc", bSortable: true},
        {data: "accName", bSortable: true},
        // {data: "fcAccGroupName", bSortable: true},
        {data: "bankName", bSortable: true},
        {data: "attributeName", bSortable: false},
        {data: "natureName", bSortable: false},
        {data:"accTypeName",bSortable:false},
        {data: "isOnlineStr", bSortable: true,
            render: function(data, type, full, meta){
                return bt.dict("sf_yn", full.isOnline)
            }
        },
        // {data: "cdSignName", bSortable: true},
        {data: "curName", bSortable: true},
        {
            data: "amtIn", bSortable: false, className: 'textRight', bSortable: true,
            render: function (data, type, full, meta) {
                return bt.money.format(full.amtIn);
            }
        },
        {
            data: "amtOut", bSortable: false, className: 'textRight', bSortable: true,
            render: function (data, type, full, meta) {
                return bt.money.format(full.amtOut);
            }
        },
        {
            data: "bal", bSortable: true, className: 'textRight',
            render: function (data, type, full, meta) {
                return bt.money.format(full.bal);
            }
        },
        {data: "bankSerialNo", bSortable: true},
        {data: "oppAccNo", bSortable: true},
        {data: "oppAccName", bSortable: true,render: function (data, type, full, meta) {
                return '<div style="min-width: 240px;"><pre>'+(data||'')+'</pre></div>'
            }},
        {data: "oppAccBank", bSortable: true},
        {data: "remark", bSortable: true},
        {data: "postscript", bSortable: true},
        {data: "projectRemark", bSortable: true},
        {data: "uses", bSortable: true},
        {data: "syncStatus", bSortable: false},
        {
            data: null, bSortable: false, className: 'center',
            render: function (data, type, full, meta) {
                var html = '';
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang('common.button.view') + '</a>';
                if(full.receiptNum>0){
                    html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewreceipt(this)">' + bt.lang("common.button.viewReceipt") + '</a>';
                }
                // else{
                //     html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="exportReceipt(this)">' + bt.lang("bankacc.export.receipt") + '</a>';
                // }
                return html;
            }
        }
    ];

    var tableTitelObj = {};
    bt.each($("#appTable thead tr th"),function (i,th){
        tableTitelObj[columns[i].data] = $(th).text() || '';
    });

    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            initQueryParams:function (params) {
                var insCorpId = params.ins_corpId;
                if(insCorpId == '' || insCorpId == null){
                    var corpId = $("#corpId").val();
                    if(corpId){
                        params.ins_corpId= corpId;
                        $("#corpId").val("");
                    }
                }
                return params
            }
        },
        fixedColumns:{
            rightColumns:1
        },
        checkbox: true,
        order: [[1, 'desc']],
        ajax: {
            'url': basePath + "/bankacc/bisaccdtl/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bankAcc:btBankAccDtl:importHisDtl'),
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        insertInformation: function (settings) { //底部插入信息回调方法，返回html字符串
            var showHTML =  '已选笔数小计: '+bt.datatable.getSelectData('#appTable').length || 0;
            return '<div style="color: red" <span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span></div>';
        },
        btClickRowCallback:function(){
            $("#sumMoneyBillSpan").text('已选笔数小计: '+bt.datatable.getSelectData('#appTable').length || 0)
        },
        columns: columns,
        summary: tableSummary,
        customExportSelected:function (params,processing){
            var selectedList = bt.datatable.getSelectData("#appTable");
            if(!selectedList || selectedList.length <= 0){
                layer.alert(bt.lang('common.msg.temporarily_no_data'), {icon: 7});
                return;
            }
            var ids = "";
            var exportExcelUrl = "/bankacc/bisaccdtl/exportSelected";
            for(var idx = 0; idx < selectedList.length; idx++){
                if(ids == ""){
                    ids = selectedList[idx].id;
                }else {
                    ids = ids + "," + selectedList[idx].id;
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
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/bankacc/bisaccdtl/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
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
});

function view(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccdtl/view/" + data.id
    })
}

//查看关联的电子回单
function viewreceipt(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data.receiptFlag==='0'){
        parent.layer.open({
            title: bt.lang("common.button.view"), //不显示标题栏
            type: 2,
            area: ['810px', '450px'],
            closeBtn :0,//不显示关闭按钮
            content: [basePath + "/netbank/nisbillhead/viewReceipt/" + data.receiptId]
            , btn: [bt.lang('common.button.download'), bt.lang('common.button.print'), '<i class="layui-icon">&#x1006;</i> ']
            , yes: function () {
                window.location = basePath + "/receipt/bisbankreceiptdtl/download?id=" + data.receiptId;
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
            ,btn2: function () {
                print(data.receiptId);
            }
            , btn3: function () {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
        });
    }else if(data.receiptFlag==='1'){
        parent.layer.open({
            title: bt.lang("common.button.view"), //不显示标题栏
            type: 2,
            area: ['810px', '450px'],
            closeBtn :0,//不显示关闭按钮
            content: [basePath + "/netbank/nisbillhead/viewHisReceipt/" + data.receiptId]
            , btn: [bt.lang('common.button.download'), bt.lang('common.button.print'), '<i class="layui-icon">&#x1006;</i> ']
            , yes: function () {
                window.location = basePath + "/receipt/bisbankreceiptdtlhis/download?id=" + data.receiptId;
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
            ,btn2: function () {
                hisprint(data.receiptId);
            }
            , btn3: function () {
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            }
        });
    }
    /**
     * 打印电子回单
     * @param id
     */
    function print(id) {
        parent.layer.open({
            type: 2,
            zIndex: -100,
            shade: 0,
            content: basePath + "/netbank/nisbillhead/print?id=" + id
        });
    }
    /**
     * 打印历史电子回单
     * @param id
     */
    function hisprint(id) {
        parent.layer.open({
            type: 2,
            zIndex: -100,
            shade: 0,
            content: basePath + "/netbank/nisbillhead/printHis?id=" + id
        });
    }

}
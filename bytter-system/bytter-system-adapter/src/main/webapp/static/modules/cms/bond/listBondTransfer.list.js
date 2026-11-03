bt.use(['form','datatable','btdate','dateType','selectType', 'moneyType','rateType', 'btImport'], function(){
    var dateType = bt.dateType.init();
    bt.selectType.init()
    $ = bt.jquery, form = bt.form();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    var issueId = $("#issueId").val();
    var isHistory = $("#isHistory").val();
    if(isHistory == '0') {
        /**
         * 导入功能 Uncaught TypeError: Cannot read properties of undefined (reading 'init')
         */
        importExcel = function () {
            up = bt.btImport.init({
                code: "70000022",
                titleRows: 0,
                width: "100%",
                height: "100%",
                headRows: 1,
                success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                    $.ajax({
                        type: "POST",
                        url: basePath + "/cms/bond/transfer/saveImport",
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
            order: [[1, 'asc']],
            colReorder: true,
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath + "/cms/bond/transfer/datatable/"+issueId
            },
            buttons: [
                {
                    text: bt.lang('common.button.add'),
                    hide: bt.isnotResource('cms:bond:transfer:add'),
                    action: function (e, dt, node, config) {
                        add();
                    }
                }, {
                    text: bt.lang('common.button.imports'),
                    hide: bt.isnotResource('netbank:bankAcc:btBankAccDtl:apply'),
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
            fixedColumns: {
                rightColumns: 1//代表冻结右边一列
            }
            , columns: [
                {data: "transferDate", className: 'center', bSortable: true},
                {
                    data: "transferPrice", className: 'center', bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.money.format(data);
                    }
                },
                {data: "transferAmount", className: 'center', bSortable: true},
                {data: "totalTransferAmount", className: 'center', bSortable: true},
                {
                    data: "totalTransferRate", className: 'textRight', render: function (data, type, full, meta) {
                        return bt.rateType.getFun().format(full.totalTransferRate);
                    }
                },
                {data: "totalTransferMoney", className: 'textRight', bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.money.format(data);
                    }
                },
                {data: "statusName", className: 'center',bSortable: true, render: function (data, type, full, meta) {
                        return full.status === 95 ? '审批通过' : bt.dict("AB_BILL_EMITBILL_STATUS", full.status);
                    }
                },
                {
                    data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                        var html = '';
                        if (full.status !== 11 && full.status !== 95) {
                            if(bt.isResource('cms:bond:transfer:update')) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a> ';
                            }
                            if(bt.isResource('cms:bond:transfer:delete')) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a> ';
                            }
                        }
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                        return html;
                    }
                }
            ],
            fixedColumns: {
                rightColumns: 8
            },
            summary: {
                // pageSum: true,//小计功能开启
                //  selectSum: true,//已选汇总功能开启
                //groupColumn: 'curName',//分类汇总列参数
                pageSum: true,
                // allSum: true,
                selectSum: false,
                format: {
                    decimal: 2
                },
                sumList: [
                    {
                        colNum: '3',
                        dataName: 'transferAmount',
                        format: 0,
                        money: false
                    },
                    {
                        colNum: '4',
                        dataName: 'totalTransferAmount',
                        format: 0,
                        money: false
                    },
                    {
                        colNum: '5',
                        dataName: 'totalTransferRate',
                    },
                    {
                        colNum: '6',
                        dataName: 'totalTransferMoney'
                    }
                ]
            }
        });
        // table 初始化结束
    }else {
        //生成列表
        datatable = bt.datatable.init({
            elem: "#appTable",
            dom: 'Brtlip',
            order: [[1, 'asc']],
            colReorder: true,
            searchForm: {
                formElem: "#searchForm",
                formFilter: "search"
            },
            ajax: {
                'url': basePath + "/cms/bond/transfer/datatable/"+issueId
            },
            fixedColumns: {
                rightColumns: 1//代表冻结右边一列
            }
            , columns: [
                {data: "transferDate", className: 'center', bSortable: true},
                {
                    data: "transferPrice", className: 'center', bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.money.format(data);
                    }
                },
                {data: "transferAmount", className: 'center', bSortable: true},
                {data: "totalTransferAmount", className: 'center', bSortable: true},
                // {data:"totalTransferRate",bSortable:true},

                {
                    data: "totalTransferRate", className: 'textRight', render: function (data, type, full, meta) {
                        return totalTransferRate = bt.rateType.getFun().format(full.totalTransferRate);
                    }
                },
                {data: "totalTransferMoney", className: 'textRight', bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.money.format(data);
                    }
                },
                {data: "statusName", bSortable: true, render: function (data, type, full, meta) {
                        if(full.status == 95){
                            return "审批通过";
                        } else {
                            return bt.dict("AB_BILL_EMITBILL_STATUS",full.status);
                        }
                    }
                },
            ],
            summary: {
                // pageSum: true,//小计功能开启
                //  selectSum: true,//已选汇总功能开启
                //groupColumn: 'curName',//分类汇总列参数
                pageSum: true,
                // allSum: true,
                selectSum: false,
                format: {
                    decimal: 2
                },
                sumList: [
                    {
                        colNum: '3',
                        dataName: 'transferAmount',
                        format: 0,
                        money: false
                    },
                    {
                        colNum: '4',
                        dataName: 'totalTransferAmount',
                        format: 0,
                        money: false
                    },
                    {
                        colNum: '5',
                        dataName: 'totalTransferRate',
                    },
                    {
                        colNum: '6',
                        dataName: 'totalTransferMoney'
                    }
                ]
            }
        });
        // table 初始化结束
    }
});
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/bond/transfer/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/bond/transfer/view/" + data.id
        });
    }
}
function add(){
    var issueId=$("#issueId").val();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/bond/transfer/edit?issueId="+issueId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/bond/transfer/edit",
        end:function(){
            datatable && datatable.draw();
            bt.navtab.closeCurrent();
            bt.navtab.refreshParent();
        }
    });
}

function update(el){
    var issueId=$("#issueId").val();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/bond/transfer/edit?issueId="+issueId+"&id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/bond/transfer/edit?issueId="+issueId+"&id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/bond/transfer/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

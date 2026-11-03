bt.use(['form','datatable','btdate','dateType','selectType', 'moneyType','rateType', 'btImport'], function(){
    var dateType = bt.dateType.init();
    bt.selectType.init()
    $ = bt.jquery, form = bt.form();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    var issueId = $("#issueId").val();
    var isHistroy = $("#isHistroy").val();
    if(isHistroy == '0') {
        //导入初始化
        importExcel = function () {
            up = bt.btImport.init({
                code: "70000023",
                titleRows: 0,
                width: "90%",
                height: "90%",
                headRows: 1,
                success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                    $.ajax({
                        type: "POST",
                        url: basePath + "/cms/bond/swap/saveImport",
                        dataType: "json",
                        contentType: "application/json",
                        data: JSON.stringify(select),
                        success: function (result) {
                            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                            location.reload();
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
                'url': basePath + "/cms/bond/swap/datatable/"+issueId
            },
            buttons: [
                {
                    text: bt.lang('common.button.add'),
                    hide: bt.isnotResource('cms:bond:transfer:query'),
                    action: function (e, dt, node, config) {
                        add();
                    }
                },
                {
                    text: bt.lang('common.button.imports'),
                    action: function (e, dt, node, config) {
                        importExcel();
                    }
                }
            ],
            fixedColumns: {
                rightColumns: 1//代表冻结右边一列
            }
            , columns: [
                {data: "swapDate", className: 'center', bSortable: true},
                {data: "swapPrice", className: 'center', bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.money.format(data);
                    }
                },
                {data: "swapAmount", className: 'center', bSortable: true},
                {data: "totalSwapAmount", className: 'center', bSortable: true},
                {data: "totalSwapMoney", className: 'textRight', bSortable: true,
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
                {
                    data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                        var html = '';
                        if (full.status !== 11 && full.status !== 95) {
                            if(bt.isResource('cms:bond:swap:update')) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a> ';
                            }
                            if(bt.isResource('cms:bond:swap:del')) {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a> ';
                            }
                        }
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                        return html;
                    }
                }
            ],
            fixedColumns: {
                rightColumns: 7
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
                        dataName: 'swapAmount',
                        format: 0,
                        money: false
                    },
                    {
                        colNum: '4',
                        dataName: 'totalSwapAmount',
                        format: 0,
                        money: false
                    },
                    {
                        colNum: '6',
                        dataName: 'totalSwapMoney'
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
                'url': basePath + "/cms/bond/swap/datatable/"+issueId
            },
            fixedColumns: {
                rightColumns: 1//代表冻结右边一列
            }
            , columns: [
                {data: "swapDate", className: 'center', bSortable: true},
                {data: "swapPrice", className: 'center', bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.money.format(data);
                    }
                },
                {data: "swapAmount", className: 'center', bSortable: true},
                {data: "totalSwapAmount", className: 'center', bSortable: true},
                {data: "totalSwapMoney", className: 'textRight', bSortable: true,
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
                }
            ],
            fixedColumns: {
                rightColumns: 7
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
                        dataName: 'swapAmount',
                        format: 0,
                        money: false
                    },
                    {
                        colNum: '4',
                        dataName: 'totalSwapAmount',
                        format: 0,
                        money: false
                    },
                    {
                        colNum: '6',
                        dataName: 'totalSwapMoney'
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
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/bond/swap/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/bond/swap/view/" + data.id
        });
    }
}
function add(){
    var issueId=$("#issueId").val();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/bond/swap/edit?issueId="+issueId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/bond/swap/edit",
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
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/bond/swap/edit?issueId="+issueId+"&id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/bond/swap/edit?issueId="+issueId+"&id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/bond/swap/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

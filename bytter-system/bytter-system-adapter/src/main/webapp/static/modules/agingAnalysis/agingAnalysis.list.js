var byCusTable = null;
var byBillTable;
var byContractTable;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'dicts','selectType' ], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    //bt.dateType.init();
    bt.dicts.init();
    bt.selectType.init();
    form.on('select(dimensionFilterName)',function(data){
        switch(data.value){
            case 'byCus':
                getCusTable();
                showDiv('byCus');
                break;
            case 'byBill':
                getBillTable();
                showDiv('byBill');
                break;
            case 'byContract':
                getContractTable();
                showDiv('byContract');
                break;
            default:
                break;
        }
        $('.buttons-colvis').hide();
    })
    //从首页合同金额跳过来要按交易合同查询
    var type = $('[name="type"]').val();
    if(type === 'byContract'){
        $('select[name="dimension"]').val('byContract');
        form.render('select');
        getContractTable();
        showDiv('byContract');
    }else{
        getCusTable();
        showDiv('byCus');
    }
    $('.buttons-colvis').hide();
});

function showDiv(type){
    const allType = ['byCus','byBill','byContract'];
    allType.forEach(item =>{
        $('#' + item + 'Div').hide();
        $('#' + item + 'SearchDiv').hide();
    })
    $('#' + type + 'Div').show();
    $('#' + type + 'SearchDiv').show();
};

function getCusTable(){
    if(byCusTable == null){
        //生成列表
        byCusTable = bt.datatable.initMultiTables({
            elem: "#byCusTable",
            dom: 'Brtlip',
            colReorder: false,
            checkbox: true,
            clickRowSelected:false,
            ordering: false,
            searchForm: {
                formElem: "#searchForm",
                formFilter: "byCusSearch"
            },
            ajax: {
                'url': basePath + "/accountReceivable/agingAnalysis/queryAging"
            },
            buttons: [
                 {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ],
            fixedColumns: {
                rightColumns: 1//代表冻结右边一列
            },
            btInitComplete: function(){
                if($.fn.dataTable.RowsGroup){
                   new $.fn.dataTable.RowsGroup(byCusTable,[1,1]);
                }
            },
            columns: [
                {data: "corpName", bSortable: false, className: "textCenter"},
                {data: "externalName", bSortable: false, className: "textCenter"},
                {data: "curName", bSortable: false, className: "textCenter"},
                {data: "writeOffBal", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.writeOffBal);
                    }
                },
                {data: "thirtyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.thirtyDay);
                    }
                },
                {data: "sixtyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.sixtyDay);
                    }
                },
                {data: "ninetyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.ninetyDay);
                    }
                },
                {data: "overNinetyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.overNinetyDay);
                    }
                }
            ]
        });
    }else{
        bt.datatable.refreshTable("byCusTable");
    }
}

function getBillTable(){
    if(byBillTable == null){
        byBillTable = bt.datatable.initMultiTables({
            elem: "#byBillTable",
            colReorder: false,
            checkbox: true,
            clickRowSelected:false,
            ordering: false,
            searchForm: {
                formElem: "#searchForm",
                formFilter: "byBillSearch"
            },
            ajax: {
                'url': basePath + "/accountReceivable/agingAnalysis/queryAging"
            },
            buttons: [
                 {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ],
            fixedColumns: {
                rightColumns: 1//代表冻结右边一列
            },
            btInitComplete: function(){
                if($.fn.dataTable.RowsGroup){
                   new $.fn.dataTable.RowsGroup(byBillTable,[1,1]);
                }
            },
            columns: [
                {data: 'code', bSortable: false, className: "textCenter"},
                {data: "corpName", bSortable: false, className: "textCenter"},
                {data: "externalName", bSortable: false, className: "textCenter"},
                {data: "curName", bSortable: false, className: "textCenter"},
                {data: "writeOffBal", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.writeOffBal);
                    }
                },
                {data: "thirtyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.thirtyDay);
                    }
                },
                {data: "sixtyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.sixtyDay);
                    }
                },
                {data: "ninetyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.ninetyDay);
                    }
                },
                {data: "overNinetyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.overNinetyDay);
                    }
                }
            ]
        });
    }else{
        bt.datatable.refreshTable("byBillTable");
    }
}

function getContractTable(){
    if(byContractTable == null){
        byContractTable = bt.datatable.initMultiTables({
            elem: "#byContractTable",
            colReorder: false,
            checkbox: true,
            clickRowSelected:false,
            ordering: false,
            searchForm: {
                formElem: "#searchForm",
                formFilter: "byContractSearch"
            },
            ajax: {
                'url': basePath + "/accountReceivable/agingAnalysis/queryAging"
            },
            buttons: [
                 {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ],
            fixedColumns: {
                rightColumns: 1//代表冻结右边一列
            },
            btInitComplete: function(){
                if($.fn.dataTable.RowsGroup){
                    new $.fn.dataTable.RowsGroup(byContractTable,[1,1]);
                }
            },
            columns: [
                {data: "contractCode", bSortable: false, className: "textCenter"},
                {data: "code", bSortable: false, className: "textCenter"},
                {data: 'corpName', bSortable: false, className: "textCenter"},
                {data: "externalName", bSortable: false, className: "textCenter"},
                {data: "curName", bSortable: false, className: "textCenter"},
                {data: "writeOffBal", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.writeOffBal);
                    }
                },
                {data: "thirtyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.thirtyDay);
                    }
                },
                {data: "sixtyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.sixtyDay);
                    }
                },
                {data: "ninetyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.ninetyDay);
                    }
                },
                {data: "overNinetyDay", bSortable: false, className: "textCenter",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.overNinetyDay);
                    }
                }
            ]
        });
    }else{
        bt.datatable.refreshTable("byContractTable");
    }
}

function resetTable(){
    getCusTable();
    showDiv('byCus');
}
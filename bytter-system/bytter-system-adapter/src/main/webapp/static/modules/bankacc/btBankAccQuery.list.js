bt.use(['form', 'datatable', 'corp', 'bank', 'rateType','mulselect','selectType','dicts', 'dateType','mulselect','element'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.mulselect.init();
    bt.selectType.init();
    bt.dicts.init();//此初始化测试没有构建到
    bt.dateType.init();
    bt.bank.init({
        elem: '.bank',
    });
    bt.mulselect.init();
    var rateType = bt.rateType.getFun();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [1, 'desc'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            initQueryParams:function(params) {
            if(params.eqs_validSign === undefined){
                  params.eqs_validSign = '1'
            }
            return params
       		}
        },
        ajax: {
            'url': basePath + "/bankacc/btbankaccquery/datatableWithAllCurId"
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
        columns: [
            {data: "bankAcc", bSortable: true, className: 'center'},
            // {data: "shareholding", bSortable: false, className: 'center'},
            {data: "accName", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true, className: 'center'},
            {data: "curName", bSortable: false, className: 'center'},
            {data: "electricBillName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.electricBill);
                }},
            {data: "circulationModeName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("bankAcc.circulationMode", full.circulationMode);
                }},
            {data: "isDomesticBankName",bSortable:true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAcc.isDomesticBank", full.isDomesticBank);
                }},
            {data: "accTypeName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAcc.accType", full.accType);
                }},
            {data: "attributeName", bSortable: true},
            {data: "natureName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            // {data: "fcAccGroupName", bSortable: false, className: 'center'},
            {data: "bankName", bSortable: true},
            {data: "provName", bSortable: true},
            {data: "cityName", bSortable: true},
            {
                data: "rates", bSortable: true,
                render: function (data, type, full, meta) {
                    return rateType.format(full.rates);
                }
            },
            {data: "regDate", bSortable: true},
            {
                data: "closeDate", bSortable: true,
                render: function (data, type, full, meta) {
                    if(full.status == "-2") {
                        if (full.closeDate) {
                            return full.closeDate;
                        } else {
                            return full.closeDate;
                        }
                    }
                    return "";
                }
            },
            {
                data: "status", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                   var html="";
                   if(full.status == -2){
                       html = bt.lang('table.bisAccDtlCheck.statusDel');
                   }else{
                       html = bt.lang('table.bisAccDtlCheck.statusNormal');
                   }
                    return html;
                }
            },
            {data: "checkExpireDate", bSortable: true},
            {data: "createBy", bSortable: true},
            {data: "validSignStr", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("bankAcc.validSign", full.validSign);
                }},
            {data: "isOnlineStr", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isOnline);
                }},
            {data: "isCapitalPoolStr", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isCapitalPool);
                }},
            {data: "rmk", bSortable: true},
            // {data: "parentArea", bSortable: true},
            // {data: "operationAreaName", bSortable: true},
            // {data: "realTimeBal", bSortable: true,
            //     render: function (data, type, full, meta) {
            //         return bt.money.format(full.realTimeBal);
            //     }},
            // {data: "bindingBankAccStr", bSortable: true},
            // {data: "bindingBankAccCorpName", bSortable: true},
            // {data: "bindingBankAccAccName", bSortable: true},
            // {data: "bindingBankAccBankName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>'+'</a>';
                    if ("" == full.attachmentId || null == full.attachmentId){
                        html+= '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="addAttachment(this)">上传附件</a>';
                    }
                    return html;
                }
            }
        ]
        // ,
        // // 暂时注释导出全部回调
        // customExportAll: function (params, processing, data) {
        //     var exportUrl = basePath + "/bankacc/btbankaccquery/exportAll";
        //     openExportExcel(exportUrl, data);
        //     processing(false);
        // }
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bankacc/btbankaccquery/view?id=" + data.id + "&curId=" + data.curId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccquery/view?id=" + data.id + "&curId=" + data.curId
    });
}

function addAttachment(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab('账户详情', basePath + "/bankacc/btbankaccquery/update?id=" + data.id + "&curId=" + data.curId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccquery/view?id=" + data.id + "&curId=" + data.curId
    });
}

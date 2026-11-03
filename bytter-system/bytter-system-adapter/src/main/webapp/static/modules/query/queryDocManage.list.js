var datatable;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType','element'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });

        //客商选择
        bt.bankinfoCallback = function (data, dom) {
//            $("#payerBankNo").val(data.bankCode);
//            $("#payerBankName").val(data.bankName);
        };


    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        clickRowSelected:false,
        order: [[1, 'asc'],[13, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/accountReceivable/query/docManage/datatable"
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
            {data: "code", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "batchBusTypeName", bSortable: true},
            {
                data: "source", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("writeOff.docSource", full.source);
                }
            },
            {
                data: "voucherType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("account.receivable.doc.voucherType", full.voucherType);
                }
            },
            {data: "receiptDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "agingDate", bSortable: true},
            {
                data: "receivableMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.receivableMoney);
                }
            },
            {
                data: "writeOffBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.writeOffBal);
                }
            },
            {data: "curName", bSortable: true},
            {data: "externalName", bSortable: true},
            {
                data: "externalType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("external.type", full.externalType);
                }
            },
            {data: "writeOffStatusName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("writeOff.status",full.writeOffStatus);
                }
            },
            {data: "writeOffTypeName", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("writeOff.type",full.writeOffType);
                }
            },
            {data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                return html;
            }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: false,
            groupColumn: 'curName',
            format: {
                decimal: 2
            },
             sumList: [
                 {
                     money: true,
                     colNum: '9',
                     dataName: 'receivableMoney'
                 },{
                     money: true,
                     colNum:'10',
                     dataName:'writeOffBal'
                 }
             ]
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

    $('#hide-search').hide()
});

function changeCurShowCallback(data){
    $("#eqs_curId").val(data.id);
}

bt.corpCallback = function (id) {
    // 重新设置业务类别
    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=0&corpId=" + id;
    bt.selectType.get("eqs_busType").attr("data-url", busTypeUrl);
    form.render()
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/accountReceivable/query/docManage/view?id=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/accountReceivable/query/docManage/view?id=" + data.id
    });
}

//业务种类初始化前回调
busBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}

function showMoreBtn(){
   if($('#hide-search')[0].style.display!='none'){
        $('.ui-more-btn ').text('更多筛选')
       $('.ui-arrow-down').attr('src',basePath+'/static/images/table/icon_arrow_down.svg')
       $('.ui-more-btn ').removeClass('ui-more-btn-active')
       $('#hide-search').hide()
   }else{
        $('.ui-more-btn ').text('收起筛选')
       $('.ui-arrow-down').attr('src',basePath+'/static/images/table/icon_arrow_up.png')
       $('.ui-more-btn ').addClass('ui-more-btn-active')
       $('#hide-search').show()
   }
}

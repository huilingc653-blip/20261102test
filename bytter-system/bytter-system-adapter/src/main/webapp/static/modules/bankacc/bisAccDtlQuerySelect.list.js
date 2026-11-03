bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.moneyType.init();
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

    //生成列表
    var table = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        fixedColumns:{
            rightColumns:1
        },
        order: [[1, 'desc']],
        ajax: {
            'url': basePath + "/bankacc/bisaccdtl/datatable"
        },
        buttons: [{
            text: bt.lang('common.button.select'),
            action: function (e, dt, node, config) {
                var data = dt.rows('.selected').data();
                batchSelectProduct(data);
            }
        },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "transTime", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "attributeName", bSortable: false},
            {data: "natureName", bSortable: false},
            {data:"accTypeName",bSortable:false},
            {data: "cdSignName", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amt);
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
            {data: "oppAccName", bSortable: true},
            {data: "oppAccBank", bSortable: true},
            {data: "remark", bSortable: true},
            {data: "postscript", bSortable: true},
            {data: "projectRemark", bSortable: true},
            {data: "uses", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang('common.button.view') + '</a>';
                    return html;
                }
            }
        ],
        summary: {
            pageSum:true,
            selectSum:true,
            groupColumn: 'cdSignName',//分类汇总列参数
            // allSum: true,
            // selectSum: false,
            sunTextCol: 2,
            customSum:{
                renderHtml:function (html) {
                    if(html && html.indexOf('</tr>') !== -1){
                        var htmlArr = html.split('</tr>')
                        var len = htmlArr.length
                        if(len > 2){
                            var selectHtml = '' //选中小计
                            var pageHtml = '' //当前页合计
                            for (var i = 0; i < htmlArr.length; i++) {
                                if(htmlArr[i].indexOf('selectSum') !== -1){
                                    selectHtml +=htmlArr[i]
                                    selectHtml +='</tr>'

                                }else if(htmlArr[i].indexOf('pageSum') !== -1){
                                    pageHtml +=htmlArr[i]
                                    pageHtml +='</tr>'
                                }
                            }
                            return (selectHtml + pageHtml)
                        }
                    }
                    return html
                }
            },
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '9',
                    dataName: 'amt'
                }
            ]
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

function batchSelectProduct(data) {
    var currentParentIframe = top.$('.layui-tab-content > .layui-show').find('iframe')[0]['name'];
    var func =  top.frames[currentParentIframe].window.btSelectCallBack['bt-biz-bisAccDtl']['callback'];

    //验证批量数据
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    if (func) {
        func(rowData);
    }
}

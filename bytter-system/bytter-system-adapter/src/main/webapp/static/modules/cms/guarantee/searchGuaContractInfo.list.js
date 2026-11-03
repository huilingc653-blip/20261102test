var datatable;
bt.use(['form', 'datatable', 'dateType', 'select', 'corp', 'money', 'moneyType', 'bank', 'btImport', 'upload', 'dicts', 'mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    mulselects = bt.mulselect.init();

    //生成列表  
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        scrollY: '600px',
        order: [[10, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/cms/guarantee/guaContractInfo/searchdatatable"
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
            {data: "corpName", className: 'center', bSortable: true},       // 融资主体
            {data: "guaNo", className: 'center', bSortable: true},          // 担保编号
            {data: "guaTypeName", className: 'center', bSortable: true},    // 担保方式
            {data: "guaPerson", className: 'center', bSortable: true},  // 担保单位
            {data: "debtorPerson", className: 'center', bSortable: true},   // 债务人
            {data: "creditorPerson", className: 'center', bSortable: true}, // 债权人
            {data: "curName", className: 'center', bSortable: true},    // 币别
            {
                // 担保额度
                data: "guaMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.guaMoney);
                }
            },
            {data: "contractCode", className: 'center', bSortable: true},   // 合同号
            {data: "startDate", className: 'center', bSortable: true},  // 担保开始日期
            {data: "endDate", className: 'center', bSortable: true},    // 担保结束日期
            {data: "operator", className: 'center', bSortable: true},       // 操作人
            {
                // 状态
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("guaContract.status", full.status);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a style="height:26px;line-height: 26px " href="#" data-row="' + meta.row + '" onclick="listMsg(this)" class="operate">' + bt.lang("table.guaContract.detailInfo") + '</a><br>';
                    html += '<a style="height:26px;line-height: 26px"  href="#" data-row="' + meta.row + '" onclick="useList(this)" class="operate">' + bt.lang("table.guaContract.useInfo") + '</a><br>';
                    if (!bt.empty(full.conId) && $('#relateCon').val() == '1') {
                        html += '<a style="height:26px;line-height: 26px"  href="#" data-row="' + meta.row + '" onclick="conView(this)" class="operate">' + bt.lang("common.button.conInfo") + '</a>';
                    }
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,//小计功能开启
            selectSum: true,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8', //汇总列序号
                    dataName: 'guaMoney' //汇总列担保额度
                }
            ]
        }
    });
});

//担保方式占用轨迹
function listMsg(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.guaContract.detailInfo'), basePath + "/cms/guarantee/guaContractInfo/queryEmployDtl/" + data.id);
            return;
        }
    }
}

//担保合同使用情况
function useList(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.guaContract.useInfo'), basePath + "/cms/guarantee/guaContractOccup/occupList?id=" + data.id);
            return;
        }
    }
}

/**
 * 查看合同信息
 * @param el
 */
function conView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var conId = data.conId;
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/contract/conCommon/conView?id=" + conId
    });
}

/**判断字符 串是否为空*/
bt.empty = function (ms) {
    if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
        return true;
    }
    return false;
}

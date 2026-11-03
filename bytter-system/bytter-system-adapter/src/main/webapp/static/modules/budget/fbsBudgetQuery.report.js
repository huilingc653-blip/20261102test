var dicts;
var selectType;
var dateType;
bt.use(['form','corp','dateType','selectType','dicts','datatable'], function(){
    $ = bt.jquery,form = bt.form();
    var corp = bt.corp.init();
    dateType = bt.dateType.init();
    selectType = bt.selectType.init();
    dicts = bt.dicts.init();

    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        isInitData: false,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/budget/fbsbudgetreport/dataTableReport"
        },
        buttonsLayout:false,
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "corpName", bSortable: true},
            {data: "beginDate", bSortable: true, className: 'center'},
            {data: "endDate", bSortable: true, className: 'center'},
            {
                data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("fbsBudget.status", full.status);
                }
            },
            {data: "timeType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fbs.timeType", full.timeType);
                }
            },
            {data: "curName", bSortable: true, className: 'center'},
            {
                data: "viewType", bSortable: false, width: 180, className: 'center',
                render: function (data, type, full, meta) {
                    return '<div class="bt-biz-dict" data-type="radio" data-group="budgetReport.viewType" data-name="viewType' + full.id + '" data-biz-name="viewType' + full.id + '" data-defaultvalue="0"></div>';
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        btInitComplete: function () {
            dicts.render();
        },
        btDrawCallback: function () {
            dicts.render();
        }
    });
});

//查看
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var url = basePath + "/budget/fbsbudget/view/" + data.id+ "/report";
    var viewType = dicts.get("viewType"+data.id).val();
    if(viewType == "1"){
        //比较模式
        url = basePath + "/budget/fbsbudgetreport/seachFbsBudget";
        url += "?corpId=" + data.corpId + "&timeType=" + selectType.get("eqs_timeType").val();
        url += "&seachBeginDate=" + dateType.get("beginDate").val()+ "&seachEndDate=" + dateType.get("endDate").val();
        url += "&viewType=" + viewType;
    }
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), url);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: url
    });
}
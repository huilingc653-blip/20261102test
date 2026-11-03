var corp,dateType,selectType,selectTree;
bt.use(['form','corp','dateType','selectType', 'datatable','dicts','selectTree'], function(){
    $ = bt.jquery,form = bt.form();
     corp = bt.corp.init();
     dateType = bt.dateType.init();
     selectType = bt.selectType.init();
    bt.dicts.init();

    form.on('submit(submit)', function (data) {
        var timeType = selectType.get("timeType").val();
        var beginDate = dateType.get("beginDate").val();
        var endDate = dateType.get("endDate").val();
        var url = basePath + "/budget/fbsBudgetProcessReport/seachManyTemp";
        url += "?&timeType=" + timeType;
        url += "&beginDate=" + beginDate + "&endDate=" + endDate;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.search'), url);
            return;
        }
    });

    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: false,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/budget/fbsBudgetProcessReport/dataTable"
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
            {data: "period", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "beginDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {
                data: "timeType", bSortable: true, className: "center",
                render: function (data, type, full, meta) {
                    return bt.dict("fbs.timeType", full.timeType);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

viewTypeCallback = function (data) {
    if(data.value == '1'){
        $("#itemCodeTr").show();
        $("#manyButton").show();
        $("#singleButton").hide();
        selectTree = bt.selectTree.initGetCurrent({
            url:basePath+"/budget/fbsitem/tree",
            elem:".itemCode",
            name:'itemCode',
            bizName:'itemCode',
            checkType: 'check',
            required:true,
            setting:{
                check : {
                    chkboxType: {"Y": "s", "N": "ps"}
                }
            }
        });
    }else{
        $("#itemCodeTr").hide();
        $("#manyButton").hide();
        $("#singleButton").show();
        selectTree = bt.selectTree.initGetCurrent({
            url:basePath+"/budget/fbsitem/tree",
            elem:".itemCode",
            name:'itemCode',
            bizName:'itemCode',
            checkType: 'check',
            setting:{
                check : {
                    chkboxType: {"Y": "s", "N": "ps"}
                }
            }
        });
    }
}

//查看
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        var url = basePath + "/budget/fbsBudgetProcessReport/seach";
        url += "?corpId=" + data.corpId + "&timeType=" + data.timeType;
        url += "&beginDate=" + data.beginDate + "&endDate=" + data.endDate + "&isInfo=true";
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.search'), url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: url,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}
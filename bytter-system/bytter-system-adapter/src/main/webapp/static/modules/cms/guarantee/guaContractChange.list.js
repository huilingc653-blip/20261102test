var datatable, mulselects;
bt.use(['form', 'datatable', 'dateType', 'select', 'corp', 'money', 'moneyType', 'bank', 'btImport', 'upload', 'dicts', 'mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();

    // 初始化下拉选择
    mulselects = bt.mulselect.init();

    // 生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[2, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/cms/guarantee/guaContractChange/datatable"
        },
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "corpName", bSortable: true},
            {data: "guaNo", bSortable: true},
            {data: "guaTypeName", bSortable: true},
            {data: "guaPerson", bSortable: true},
            {data: "debtorPerson", bSortable: true},
            {data: "creditorPerson", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "guaMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.guaMoney);
                }
            },
            {data: "contractCode", bSortable: true},
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "updateDate", bSortable: true},
            {data: "operator", className: 'center', bSortable: true},       // 操作人
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('gua:contract:change:apply', full.corpId) && full.status == 95) {//更新权限
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("table.guaContract.change") + '</a><span class="text-explode"></span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="viewHistory(this)">' + bt.lang("table.guaContract.history") + '</a><span class="text-explode"></span>';
                    }/*else{
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode"></span>';
             	   	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>'; 
                }*/
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


//查看历史信息详情
function viewHistory(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.guaContract.history'), basePath + "/cms/guarantee/guaContractChange/queryGuaContractHistory/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/guaContractChange/queryGuaContractHistory/" + data.id,
            end: function () {
            }
        });
    }
}

//查看担保合同信息
/*function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '500px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaContractInfo/viewInfo/" +data.id
    });
}*/

//变更
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/cms/guarantee/guaContractChange/editContractInfo/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/guaContractChange/editContractInfo/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

//日志列表
/*function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
	    if(bt.navtab){
	         bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/guarantee/gualog/listlog?guaId=" + data.id);
	         return;
	     }
	  }
}*/


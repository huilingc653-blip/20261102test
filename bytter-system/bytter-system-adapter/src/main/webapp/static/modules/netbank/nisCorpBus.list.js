bt.use(['form', 'datatable', 'corp', 'selectType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.selectType.init();
    bt.corp.init();

    //生成列表
    var table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/niscorpbus/dataTableList"
        },
        buttons: [
            {
                text: bt.lang('common.button.setting'),
                action: function (e, dt, node, config) {
                    add();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        }
        , columns: [
            {
                data: "corpName", bSortable: false,
                render: function (data, type, full, meta) {
                    return "(" + full.corpCode + ")" + full.corpName;
                }
            },
            {data: "busTypeName", bSortable: false,
                render: function (data, type, full, meta) {
                    return '<div class="break-row">'+full.busTypeName+'</div>';
                }
            },
            {data: null, bSortable: false,className: "center",
                render: function (data, type, full, meta) {
                    var html = '<a href="javascript:copyBus(\''+full.corpId+'\');"  class="model-copy">' + bt.lang("common.button.clone") + '</a>';
                    html += '<span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });
});

function copyBus(corpId) {
    var copyedCorpId=corpId;
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.clone'), basePath + "/netbank/niscorpbus/copy?corpId="+copyedCorpId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/niscorpbus/copy?corpId="+copyedCorpId,
        end: function () {
            table && table.draw();
        }
    });
}

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.setting'), basePath + "/netbank/niscorpbus/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/niscorpbus/add",
        end: function () {
            table && table.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/niscorpbus/view/" + data.corpId
    });
}



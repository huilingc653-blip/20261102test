bt.use(['form', 'datatable', 'selectType', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/btbankaccrev/changeTable"
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
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "curName", bSortable: false},
            {data: "accTypeName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "updateDate", bSortable: true,
                render:function (data,type,full,meta) {
                    if (!bt.empty(full.updateDate)){
                        return full.updateDate;
                    } else {
                        return full.createDate;
                    }
                }
            },
            {data: "", bSortable: false,
                render:function (data,type,full,meta) {
                    if (!bt.empty(full.updateBy)){
                        return full.updateByName;
                    } else {
                        return full.createByName;
                    }
                }
            },
            {data: "opRession", bSortable: false},
            {data:"attachmentNum",bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="attachmentView(this)">'+bt.lang("bill.accebill.attachment.num",[(full.attachmentNum)])+'</a>';
                    return html;
                }
             },
            {data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("bankAcc.status", full.status);
                }
            }
        ]
    });
});

function attachmentView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['820px', '400px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccrev/viewAttachment?attachmentId=" + data.attachmentId,
    });
}

/** 判断字符串是否为空*/
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}


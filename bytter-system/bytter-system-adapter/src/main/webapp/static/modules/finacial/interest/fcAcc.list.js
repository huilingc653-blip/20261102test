var appTable;
bt.use(['form', 'corp', 'dateType', 'dicts', 'bank', 'rateType', 'money', 'datatable', 'selectType','bttable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dateType.init();
    bt.dicts.init();
    var select = bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });

    appTable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        paging: true,
        ordering: true,
        buttonsLayout: false,
        lengthMenu: [100],
        ignoreButtons: ['colvis', 'output', 'print', 'onOffBtn'],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/finacial/interest/listFcAccInterest/datatableFor"
        },
        columns: [
            {
                data: "account", bSortable: true, className: "center",
                visible: false,
                render: function (data, type, full, metd) {
                    return $("#account").html(full.blnctrAcc);
                }
            },
            {
                data: "accName", bSortable: true, className: "center",
                visible: false,
                render: function (data, type, full, metd) {
                    return $("#accName").html(full.accName);
                }
            },
            {
                data: "curName", bSortable: true, className: "center",
                visible: false,
                render: function (data, type, full, metd) {
                    return $("#curName").html(full.curName);
                }
            },
            {
                data: "interestStartDate",
                bSortable: true,
                className: "center"
            },
            {
                data: "blnInstDate",
                bSortable: true,
                className: "center"
            },
            {
                data: "days",
                bSortable: true,
                className: "center"
            },
            {
                data: "cumulation",
                bSortable: true,
                className: "textRight",
                render: function (data, type, full, metd) {
                    var cumulation = bt.money.format(full.cumulation);
                    return cumulation;
                }
            },
            {
                data: "interestRate",
                bSortable: true,
                className: "textRight",
                render: function (data, type, full, metd) {
                    var interestRate = bt.rateType.getFun().format(full.interestRate, 'percent', 4);
                    return interestRate;
                }
            },
            {
                data: "interest",
                bSortable: true,
                className: "textRight",
                render: function (data, type, full, meta) {
                    var interest = bt.money.format(full.interest);
                    return interest;
                }
            }
        ]
    });
});


/**
 * 当没有数据时清空账户，账户名，币别数据
 */
$("#appTable").on( 'xhr.dt', function (e, settings, json) {
    if (json.data == null || json.data.length == 0) {
        $("#account").html("");
        $("#accName").html("");
        $("#curName").html("");
    }
} );

function reset() {
    $("#date").val("");

}

function formatDate(data, input) {
    var date = addDate(data);
    $("#date").val(date);
}


function addDate(date, days) {
    if (days == undefined || days == '') {
        days = 1;
    }
    var date = new Date(date);
    date.setDate(date.getDate() + days);
    var month = date.getMonth() + 1;
    var day = date.getDate();
    return date.getFullYear() + '-' + month + '-' + day;
}


$.each($("div[type='list']"), function (i, item) {
    bt.select.init({
        elem: "[field='" + $(item).attr("field") + "']",
        name: $(item).attr("field"),
        defaultVal: $(item).attr("defaultVal"),
        valueField: $(item).attr("dictField"),
        textField: $(item).attr("dictText"),
        placeholder: {value: "", text: ""},
        search: true,
        disabled: false,
        url: $(item).attr("fieldhref")

    });
});


$("#print").click(function () {
    var id = "";
    $.each(appTable.rows().data(),function (index,item) {
        if (isEmpty(id)) {
            id = item.id;
        } else {
            id += "," + item.id;
        }
    })
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/interest/listFcAccInterest/print?id=" + id,
    });
});


$("#batchPrint").click(function () {
    var formData = $("#searchForm").serializeObject();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(data).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['900px', '530px'],
        maxmin: true,
        btn:[],
        content: basePath + "/finacial/interest/listFcAccInterest/batchPrint?" + $.param(formData),
    });
});


/**判断字符串是否为空*/
function isEmpty(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}

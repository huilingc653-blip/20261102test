var selectType;
bt.use(['form','datatable','corp','moneyType','dateType','selectType', 'mulselect'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    bt.mulselect.init();
    bt.corp.init();//单位
    bt.moneyType.init();//金额
    var dateTypes = bt.dateType.init();//日期
    selectType = bt.selectType.init();//下拉选择
   // var dicts = bt.dicts.init();//字典
    //生成列表

    var height = $(window).height();
    var corpTableHeight = height - 210;
    if (corpTableHeight < 400){
        corpTableHeight = 400;
    }
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: false,
        dom:'Brtlip',//自定义dom布局
        order: [[0, 'desc']],
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        scrollY: corpTableHeight,
        checkbox:false,
        ajax: {
            'url': basePath+"/bi/cms/report/loan/searchloanrepay"
        },
        fixedColumns: {
            rightColumns: 6//代表冻结右边一列
        },
        columns: [
            {data:"corpId",bSortable:true,className:"center",render: function ( data, type, full, meta ){
                    var html = '';
                    if (full.corpId != null && full.corpId != "") {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewSplit(\''+full.corpId+'\')">' + full.corpName + '</a>';
                    }
                    return html;
                }},
            {data:"netCode",bSortable:false,className:"center",render: function ( data, type, full, meta ){
                    return full.netName;
                }},
            {data:"beginningLoan",bSortable:false,className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.beginningLoan);
                }},

            {data:"currentLoan",bSortable:false,className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.currentLoan);
                }},
            {data:"currentRepay",bSortable:false,className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.currentRepay);
                }},

            {data:"endingLoan",bSortable:false,className:"textRight",render: function ( data, type, full, meta ){
                    return bt.money.format(full.endingLoan);
                }}
        ],
        summary: {
            pageSum: true,
           // allSum: true,
           // selectSum: false,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    money: true,
                    colNum: '2',
                    dataName: 'beginningLoan'
                },{
                    money: true,
                    colNum:'3',
                    dataName:'currentLoan'
                },
                {
                    money: true,
                    colNum:'4',
                    dataName:'currentRepay'
                },
                {
                    money: true,
                    colNum:'5',
                    dataName:'endingLoan'
                }
            ]
        }
    });

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == "null"){
            return true;
        }
        return false;
    }
});
function viewSplit(corpId) {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('cms.loan.query'), basePath + "/cms/loan/cmsloaninfo/listQuery?menuId=5e1ca70b8f354fa6b499eba213f0c98a&corpId=" + corpId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/cms/loan/cmsloaninfo/listQuery?menuId=5e1ca70b8f354fa6b499eba213f0c98a&corpId=" + corpId
    });
}
